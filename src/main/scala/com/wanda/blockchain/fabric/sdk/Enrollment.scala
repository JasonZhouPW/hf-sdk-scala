package com.wanda.blockchain.fabric.sdk

import java.security.KeyPair

import org.hyperledger.fabric.protos.common.common.Status.BAD_REQUEST

import scala.beans.BeanProperty

/**
  * Created by zhou peiwen on 2017/2/13.
  */
class Enrollment extends Serializable {

  @BeanProperty
  var key: KeyPair = _

  @BeanProperty
  var cert: String = _

  @BeanProperty
  var chainKey: String = _

  @BeanProperty
  var publicKey: String = _

  def getPrivateKey = this.key.getPrivate

  //todo ？？？
  def getMSPID = "DEFAULT"

}