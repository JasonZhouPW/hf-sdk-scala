package com.wanda.blockchain.fabric.sdk.exception

/**
  * Created by Zhou peiwen on 2017/2/13.
  */
class EnrollmentException(msg:String,cause:Throwable) extends BaseException(msg:String,cause:Throwable){

  def this(msg:String) = this(msg,null)
}
