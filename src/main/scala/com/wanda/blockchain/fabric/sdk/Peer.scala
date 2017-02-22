package com.wanda.blockchain.fabric.sdk

import scala.beans.BeanProperty

/**
  * Created by zhou peiwen on 2017/2/13.
  */
class Peer {

  @BeanProperty
  var name:String = _

  @BeanProperty
  var url:String = _

  @BeanProperty
  var chain:Chain = _

}
