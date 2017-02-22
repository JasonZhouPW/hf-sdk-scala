package com.wanda.blockchain.fabric.sdk.memberService

import com.wanda.blockchain.fabric.sdk._

import scala.util.Try

/**
  * Created by zhou peiwen on 2017/2/13.
  */
trait MemberServices {

  /**
    * get security level
    *
    * @return
    */
  def getSecurityLevel: Int

  /**
    * set security level
    *
    * @param securityLevel
    */
  def setSecurityLevel(securityLevel: Int): Unit

  /**
    * get hash algorithm
    *
    * @return
    */
  def getHashAlgorithm: String

  /**
    * set hash algorithm
    * @param hashAlgorithm
    */
  def setHashAlgorithm(hashAlgorithm: String): Unit

  /**
    * register method
    * @param req
    * @param registrar
    * @return
    */
  def register(req: RegistrationRequest, registrar: User): String

  /**
    * user enroll
    * @param req
    * @return
    */
  def enroll(req: EnrollmentRequest): Try[Enrollment]

  /**
    * get tcert batch
    * @param req
    */
  def getTCertBatch(req: GetTCertBatchRequest): Unit

}
