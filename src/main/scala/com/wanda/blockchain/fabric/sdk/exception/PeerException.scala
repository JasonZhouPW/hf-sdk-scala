package com.wanda.blockchain.fabric.sdk.exception

/**
  * Created by Zhou peiwen on 2017/2/23.
  */
class PeerException(message:String,cause:Exception) extends BaseException(message:String,cause:Throwable){
  def this(msg:String) = this(msg,null)
}
