package com.wanda.blockchain.fabric.sdk

import com.wanda.blockchain.fabric.sdk.events.EventHub

import scala.beans.BeanProperty
import scala.collection.mutable

/**
  * Created by zhou peiwen on 2017/2/11.
  */
object HFClient {


  def apply: HFClient = new HFClient()
  def newInstance = new HFClient()

}

class HFClient {

  @BeanProperty
  var userContext: User = _

  val chains = mutable.Map[String, Chain]()

  /**
    * Create new chain
    *
    * @param name
    * @return
    */
  def newChain(name: String): Chain = {
    val newChain = new Chain(name, this)
    chains += (name -> newChain)
    newChain
  }

  /**
    * create new peer
    * @param name
    * @return
    */
  def newPeer(name: String): Peer = {
    new Peer(name, null)
  }

  /**
    * Create new peer
    * @param name
    * @param pem
    * @return
    */
  def newPeer(name: String, pem: String): Peer = {
    new Peer(name, pem)
  }

  /**
    * Creaet new orderer
    * @param url
    * @return
    */
  def newOrderer(url: String): Orderer = {
    new Orderer(url, null, null)
  }

  /**
    * Get chain by name
    * @param name
    * @return
    */
  def getChain(name: String): Option[Chain] = {
    this.chains.get(name)
  }

  /**
    * Create new InstallProposalRequest
    * @return
    */
  def newInstallProposalRequest: InstallProposalRequest = new InstallProposalRequest

  /**
    * Create new InstantiateProposalRequest
    * @return
    */
  def newInstantiationProposalRequest:InstantiateProposalRequest = new InstantiateProposalRequest

  /**
    * Create new InvokeProposalRequest
    * @return
    */
  def newInvokeProposalRequest: InvokeProposalRequest = new InvokeProposalRequest

  /**
    * Create new QueryProposalRequest
    * @return
    */
  def newQueryProposalRequest: QueryProposalRequest = new QueryProposalRequest



  /**
    * Create event hub
    * @param eventHub
    * @return
    */
  def newEventHub(eventHub:String) = new EventHub(eventHub,null)
}