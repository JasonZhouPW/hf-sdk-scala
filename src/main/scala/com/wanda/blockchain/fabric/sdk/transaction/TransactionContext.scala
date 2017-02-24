package com.wanda.blockchain.fabric.sdk.transaction

import com.google.protobuf.ByteString
import com.wanda.blockchain.fabric.sdk.memberService.MemberServices
import com.wanda.blockchain.fabric.sdk.{Chain, User}
import com.wanda.blockchain.fabric.sdk.security.CryptoPrimitives
import com.wanda.blockchain.fabric.sdk.util.SDKUtil
import org.apache.commons.logging.LogFactory

import scala.beans.BeanProperty

/**
  * Created by Zhou peiwen on 2017/2/24.
  */
class TransactionContext {

  private[this] val log = LogFactory.getLog(this.getClass)

  private val nonce: ByteString = ByteString.copyFromUtf8(SDKUtil.generateUUID)

  @BeanProperty
  var cryptoPrimitives:CryptoPrimitives = _

  @BeanProperty
  var user:User = _

  @BeanProperty
  var chain:Chain = _

  @BeanProperty
  var memberServices:MemberServices = _

  @BeanProperty
  var txID:String = _

  var tcert:TCert = _

}
