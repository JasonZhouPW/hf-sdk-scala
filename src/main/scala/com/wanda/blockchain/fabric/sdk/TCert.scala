package com.wanda.blockchain.fabric.sdk

import java.security.PrivateKey

/**
  * Created by Zhou peiwen on 2017/2/24.
  */
class TCert extends Certificate{

  def this(publickey:Array[Byte],privateKey: PrivateKey) = {
    super(publickey,privateKey,PrivacyLevel.Anonymous)

  }
}
