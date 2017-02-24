package com.wanda.blockchain.fabric.sdk

import java.security.PrivateKey

/**
  * Created by Zhou peiwen on 2017/2/23.
  */
class Certificate(val cert:Array[Byte],val privateKey:PrivateKey,val privacyLevel:PrivacyLevel.Priv ) {

}
