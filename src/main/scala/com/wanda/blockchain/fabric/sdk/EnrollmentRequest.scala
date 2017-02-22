package com.wanda.blockchain.fabric.sdk

import scala.beans.BeanProperty

/**
  * Created by zhou peiwen on 2017/2/13.
  */
class EnrollmentRequest {

  //The enrollment ID
  @BeanProperty
  var enrollmentID:String = _

  //The enrollment secret(password)
  @BeanProperty
  var enrollmentSecret:String = _

}
