package com.wanda.blockchain.fabric.sdk

import java.security.PrivateKey

import scala.beans.BeanProperty

/**
  * Created by Zhou peiwen on 2017/2/23.
  */
class Certificate(val cert:Array[Byte],val privateKey:PrivateKey,val privacyLevel: PrivacyLevel.Priv ) {
/*  @BeanProperty
  var cert: Array[Byte] = _

  @BeanProperty
  var privateKey: PrivateKey = _

  @BeanProperty
  var privacyLevel: PrivacyLevel.Priv = _

  def this(cert:Array[Byte],privateKey:PrivateKey,privacyLevel: PrivacyLevel.Priv) = {
    this
    this.cert = cert
    this.privateKey = privateKey
    this.privacyLevel = privacyLevel
  }*/
}