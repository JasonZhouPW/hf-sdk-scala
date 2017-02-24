package com.wanda.blockchain.fabric.sdk

import scala.beans.BeanProperty
import scala.collection.mutable

/**
  * Created by zhou peiwen on 2017/2/13.
  */
class RegistrationRequest {

  @BeanProperty
  var enrollmentID: String = _

  @BeanProperty
  var roles: List[String] = _

  @BeanProperty
  var affiliation: String = _

  @BeanProperty
  var group: String = _

  @BeanProperty
  var attrs: mutable.Map[String, String] = _


}
