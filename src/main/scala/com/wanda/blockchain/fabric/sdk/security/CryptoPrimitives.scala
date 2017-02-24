package com.wanda.blockchain.fabric.sdk.security

import java.io.StringWriter
import java.security.{KeyPair, KeyPairGenerator, SecureRandom, Security}
import java.security.spec.ECGenParameterSpec
import javax.security.auth.x500.X500Principal

import com.wanda.blockchain.fabric.sdk.helper.Config
import com.wanda.blockchain.fabric.sdk.util.SDKUtil
import org.apache.commons.logging.LogFactory
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.openssl.jcajce.JcaPEMWriter
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder
import org.bouncycastle.pkcs.PKCS10CertificationRequest
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder
import org.bouncycastle.util.io.pem.PemObject

import scala.beans.BeanProperty
import scala.util.{Failure, Success, Try}

/**
  * Created by zhou peiwen on 2017/2/13.
  */
class CryptoPrimitives{

  private[this] val logger = LogFactory.getLog(this.getClass)

  private val SECURITY_PROVIDER = BouncyCastleProvider.PROVIDER_NAME;

  @BeanProperty
  var curveName:String = _

  var hashAlgorithm:String = Config.getHashAlgorithm

  var securityLevel:Int = Config.getSecurityLevel

  def this(hashAlgorithm:String,securityLevel:Int){
    this
    this.hashAlgorithm = hashAlgorithm
    this.securityLevel = securityLevel
    Security.addProvider(new BouncyCastleProvider)
    init
  }

  /**
    * init the cryptoprimitives
    */
  private def init = {
    require(this.securityLevel == 256 || this.securityLevel == 384,s"Illegal level ${this.securityLevel}, only support 256 and 384 for now.")
    require(!SDKUtil.isEmptyString(this.hashAlgorithm) && (this.hashAlgorithm.equalsIgnoreCase("SHA2") || this.hashAlgorithm.equalsIgnoreCase("SHA3")),
    s"Illegal hash function family:${this.hashAlgorithm}, must be either SHA2 or SHA3.")

    this.curveName = this.securityLevel match{
      case 256 => "P-256"
      case 384 => "secp384r1"
    }
  }

  def ecdsaKeyGen = generateKey("ECDSA",this.curveName)

  /**
    * generateKey
    * @param encryptionName
    * @param curveName
    * @return
    */
  private def generateKey(encryptionName:String,curveName:String):Try[KeyPair] = {

    try{
      val ecGenSpec = new ECGenParameterSpec(curveName)
      val g = KeyPairGenerator.getInstance(encryptionName, SECURITY_PROVIDER)
      g.initialize(ecGenSpec,new SecureRandom)
      Success(g.generateKeyPair)
    }catch{
      case e:Exception =>
        logger.error(s"Error when generateKey($encryptionName,$curveName)",e)
        Failure(e)
    }

  }

  /**
    * generateCertificationRequest
    * @param subject
    * @param pair
    * @return
    */
  def generateCertificationRequest(subject:String,pair:KeyPair): PKCS10CertificationRequest ={

    val p10Builder =  new JcaPKCS10CertificationRequestBuilder(new X500Principal("CN=" + subject), pair.getPublic)

    val csBuilder = new JcaContentSignerBuilder("SHA256withECDSA")

    val signer = csBuilder.build(pair.getPrivate)

    return p10Builder.build(signer)
  }

  /**
    * certificationRequestToPEM :convert a PKCS10CertificationRequest to PEM format
    * @param csr
    * @return
    */
  def certificationRequestToPEM(csr: PKCS10CertificationRequest):String = {
    val pemCSR = new PemObject("CERTIFICATE REQUEST",csr.getEncoded)
    val sw = new StringWriter
    val pemWriter = new JcaPEMWriter(sw)
    pemWriter.writeObject(pemCSR)
    sw.close
    sw.toString
  }

}
