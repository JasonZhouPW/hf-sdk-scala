package com.wanda.blockchain.fabric.sdk

import org.hyperledger.fabric.protos.common.configuration.ConfigurationItem.ConfigurationType.Peer

import scala.beans.BeanProperty
import scala.collection.mutable

/**
  * Created by zhou peiwen on 2017/2/11.
  */
object HFClient {


  def apply: HFClient = new HFClient()


}

class HFClient {

  @BeanProperty
  var userContext:User = _

  val chains = mutable.Map[String,Chain]()

  def newChain(name:String) = {
    val newChain = new Chain(name,this)
    chains += ("a"->newChain)
  }
}