package com.wanda.blockchain.fabric.sdk

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, ObjectInputStream, ObjectOutputStream}

import com.wanda.blockchain.fabric.sdk.memberService.MemberServices
import org.apache.commons.logging.LogFactory
import org.bouncycastle.util.encoders.Hex

import scala.beans.BeanProperty
import scala.collection.mutable
import scala.util.{Failure, Success}

/**
  * Created by zhou peiwen on 2017/2/11.
  */
class User extends Serializable {

  private[this] val logger = LogFactory.getLog(this.getClass)

  @BeanProperty
  @transient
  var chain: Chain = _

  @BeanProperty
  var name:String = _

  @BeanProperty
  var roles:List[String] = _

  @BeanProperty
  var affiliation:String = _

  @BeanProperty
  var attrs:Map[String,String] = _

  @BeanProperty
  var account:String = _

  @BeanProperty
  var enrollmentSecret:String = _

  @BeanProperty
  var enrollment:Enrollment = _

  @BeanProperty
  @transient
  var memberServices:MemberServices = _

  @BeanProperty
  @transient
  var keyValStore:KeyValStore = _

  @BeanProperty
  var keyValStoreName:String = _

  @BeanProperty
  var tcertGetterMap:mutable.Map[String ,TcertGetter] = _

  @BeanProperty
  var tcertBatchSize:Int = _

  def this(name:String,chain:Chain) = {
    this
    require(chain!= null ,"A valid chain must be provided.")
    this.name = name
    this.chain = chain
    this.memberServices = chain.getMemberServices
    this.keyValStore = chain.getKeyValueStore
    this.keyValStoreName =toKeyValStoreName(this.name)
    this.tcertBatchSize = chain.getTcertBatchSize
    this.tcertGetterMap = mutable.Map()

  }

  def this(name:String) = {
    this
    this.name = name
  }

  /**
    * Determine if this name has been enrolled
    * @return
    */
  def isEnrolled:Boolean = this.enrollment != null

  /**
    * Register to CA
    * @param registrationRequest
    */
  def register(registrationRequest: RegistrationRequest):Unit = {
    require(registrationRequest.getEnrollmentID == name,"registration enrollment ID and member name are not equal")
    this.enrollmentSecret = memberServices.register(registrationRequest,chain.getRegistrar)
    this.saveState

  }

  /**
    * Enroll the user and return the enrollment results
    * @param enrollmentSecret
    * @return
    */
  def enroll(enrollmentSecret:String):Enrollment = {
    val req = new EnrollmentRequest
    req.setEnrollmentID(getName)
    req.setEnrollmentSecret(enrollmentSecret)
    logger.debug(s"Enrolling [req = $req]")

    this.enrollment = memberServices.enroll(req) match{
      case Success(enrollment) => enrollment
      case Failure(f) => logger.warn(s"enroll failed:$f")
        null
    }
    this.saveState
    this.enrollment

  }

  /**
    * restore the state of this user to keyValue store
    */
  def saveState:Unit = {
    val bos = new ByteArrayOutputStream
    try{
      val oos = new ObjectOutputStream(bos)
      oos.writeObject(this)
      oos.flush
      keyValStore.setValue(keyValStoreName,Hex.toHexString(bos.toByteArray))
      bos.close
    }catch{
      case e:Exception => logger.warn(s"Could not save state of member ${this.name}",e)
    }
  }

  /**
    * Restore the state of this user from the key value store, if not found ,do nothing.
    * @return
    */
  def restoreState:User = {
    Option(keyValStore.getValue(keyValStoreName)) match{
      case Some(memberStr) => val serialized = Hex.decode(memberStr)
        val bis = new ByteArrayInputStream(serialized)
        try{
          val ois = new ObjectInputStream(bis)
          val state:User = ois.readObject.asInstanceOf[User]
          if(state != null){
            this.name = state.name
            this.roles = state.roles
            this.account = state.account
            this.affiliation = state.affiliation
            this.enrollment = state.enrollment
            this.enrollmentSecret = state.enrollmentSecret
            this.attrs = state.attrs
            this
          }else{
            logger.warn(s"Could not find state of member ${this.name}")
            null
          }
        }catch{
          case e:Exception => logger.warn(s"Could not restore state of member ${this.name}",e)
            null
        }
      case _ => logger.warn(s"Could not restore state of member ${this.name}")
        null
    }

  }

  /**
    * Get keyValue store name
    * @param name
    * @return
    */
  private def toKeyValStoreName(name:String):String = s"member.$name"
}
