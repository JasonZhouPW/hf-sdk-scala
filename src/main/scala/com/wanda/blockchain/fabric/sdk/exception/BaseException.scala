package com.wanda.blockchain.fabric.sdk.exception

/**
  * Created by dell on 2017/2/13.
  */
class BaseException(msg:String,cause:Throwable) extends Exception(msg:String,cause:Throwable)  {

  def this(msg:String) = this(msg,null)

}
