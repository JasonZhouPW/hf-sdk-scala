package com.wanda.blockchain.fabric.sdk.memberService
import java.io.{PrintWriter, StringReader, StringWriter}
import java.net.URL
import javax.json.Json
import javax.json.JsonObject
import org.bouncycastle.util.encoders.Hex
import com.wanda.blockchain.fabric.sdk.util.SDKUtil._
import com.wanda.blockchain.fabric.sdk._
import com.wanda.blockchain.fabric.sdk.exception.EnrollmentException
import com.wanda.blockchain.fabric.sdk.security.CryptoPrimitives
import org.apache.http.auth.UsernamePasswordCredentials
import java.util.Base64

import scala.util.{Failure, Success, Try}

/**
  * Created by dell on 2017/2/13.
  */
class MemberServicesFabricCAImpl(val url:String,val pem:String) extends MemberServices{

  checkUrl(url)

  private val COP_BASE_PATH = "/api/v1/cfssl/"

  private val COP_ENROLLMENT_BASE = COP_BASE_PATH + "enroll"

  private val DEFAULT_HASH_ALGORITHM = "SHA2"

  //todo make configurable
  private val  DEFAULT_SECURITY_LEVEL = 256

  private val cryptoPrimitives = new CryptoPrimitives(DEFAULT_HASH_ALGORITHM, DEFAULT_SECURITY_LEVEL);

  /**
    * validate the member service url
    * @param url
    */
  private def checkUrl(url:String):Unit = {
    val purl = new URL(url)
    val proto = purl.getProtocol
    require(proto == "http" || proto == "https","MemberService only support http or https currently")

    val host = purl.getHost
    require(!isEmptyString(host),"host is empty")

    val path = purl.getPath
    require(isEmptyString(path),"MemberService not support path")

    val query = purl.getQuery
    require(isEmptyString(query),"MemberService no support query")


  }

  /**
    * get the crypto primitives
    * @return
    */
  def getCrypto = this.cryptoPrimitives

  /**
    * get security level
    *
    * @return
    */
  override def getSecurityLevel: Int = ???

  /**
    * set security level
    *
    * @param securityLevel
    */
  override def setSecurityLevel(securityLevel: Int): Unit = ???

  /**
    * get hash algorithm
    *
    * @return
    */
  override def getHashAlgorithm: String = ???

  /**
    * set hash algorithm
    *
    * @param hashAlgorithm
    */
  override def setHashAlgorithm(hashAlgorithm: String): Unit = ???

  /**
    * register method
    *
    * @param req
    * @param registrar
    * @return
    */
  override def register(req: RegistrationRequest, registrar: User): String = {
    //TODO not yet implemented
    null
  }

  /**
    * user enroll
    *
    * @param req
    * @return
    */
  override def enroll(req: EnrollmentRequest): Try[Enrollment] = {

    try{
      require(req != null,"EnrollmentRequest is empty")

      val user = req.getEnrollmentID
      val secret = req.getEnrollmentSecret
      if(isEmptyString(user))throw new RuntimeException("enrollmentRequest.enrollmentID is empty")
      if(isEmptyString(secret))throw new RuntimeException("enrollmentRequest.enrollmentSecret is empty")
      //TODO deal with failure
      val signingKeyPair = cryptoPrimitives.ecdsaKeyGen.get

      val csr = cryptoPrimitives.generateCertificationRequest(user,signingKeyPair)
      val pem = cryptoPrimitives.certificationRequestToPEM(csr)

      //TODO replace with gson
      val factory = Json.createObjectBuilder
      factory.add("certificate_request",pem)
      val postObject = factory.build


      val stringWriter = new StringWriter
      val jsonWriter = Json.createWriter(new PrintWriter(stringWriter))

      jsonWriter.writeObject(postObject)
      jsonWriter.close

      val str = stringWriter.toString
      //todo replace with scalaj or grpc call
      val responseBody = httpPost(url+COP_ENROLLMENT_BASE,str,new UsernamePasswordCredentials(user,secret))

      val reader = Json.createReader(new StringReader(responseBody))
      val jsonstr:JsonObject = reader.read.asInstanceOf[JsonObject]
      val result = jsonstr.getString("result")
      val success = jsonstr.getBoolean("success")

      if(!success){
        throw new EnrollmentException(s"Cop failed.result:$result")
      }

      val base64Decoder = Base64.getDecoder
      val signedPem = new String(base64Decoder.decode(result.getBytes))


      val enrollment = new Enrollment
      enrollment.setKey(signingKeyPair)
      enrollment.setPublicKey(Hex.toHexString(signingKeyPair.getPublic.getEncoded))
      enrollment.setCert(signedPem)
      Success(enrollment)
    }catch{
      case e:Exception =>Failure(e)
    }



  }




  /**
    * get tcert batch
    *
    * @param req
    */
  override def getTCertBatch(req: GetTCertBatchRequest): Unit = ???
}
