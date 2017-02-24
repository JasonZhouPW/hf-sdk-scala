package com.wanda.blockchain.fabric.sdk

import com.wanda.blockchain.fabric.sdk.events.EventHub
import com.wanda.blockchain.fabric.sdk.memberService.{MemberServices, MemberServicesFabricCAImpl}
import com.wanda.blockchain.fabric.sdk.security.CryptoPrimitives
import com.wanda.blockchain.fabric.sdk.util.SDKUtil._
import org.apache.commons.logging.LogFactory
import org.hyperledger.fabric.protos.peer.proposal_response.ProposalResponse

import scala.beans.BeanProperty
import scala.collection.mutable

/**
  * Created by zhou peiwen on 2017/2/11.
  */
class Chain(val name: String, val client: HFClient) {

  private[this] val logger = LogFactory.getLog(this.getClass)
  /*  //Name of the chain
    val name: String = name*/

  //Peers infos
  @BeanProperty
  var peers: List[Peer] = Nil

  //Security flag
  @BeanProperty
  var securityEnabled = true

  //User catch of this chain
  //TODO: use guava catch instead
  @BeanProperty
  var members = mutable.Map[String, User]()

  //tcerts to get in each batch
  @BeanProperty
  val tcertBatchSize = 200

  //The registrar that registers and enrolls new members/users
  @BeanProperty
  var registrar: User = _

  //the member service used for this chain
  private var memberServices: MemberServices = _

  //The kv store uesd for this chain
  @BeanProperty
  var keyValueStore: KeyValStore = _

  //Is in dev mode or network mode
  @BeanProperty
  var devMode: Boolean = false

  // If in prefetch mode, we prefetch tcerts from member services to help performance
  @BeanProperty
  var preFetchMode = false


  // Temporary variables to control how long to wait for deploy and invoke to complete before
  // emitting events.  This will be removed when the SDK is able to receive events from the
  @BeanProperty
  var deployWaitTime = 20

  @BeanProperty
  var invokeWaitTime = 5

  // The crypto primitives object
  @BeanProperty
  var cryptoPrimitives: CryptoPrimitives = _

  //The Orderers
  @BeanProperty
  var orderers: List[Orderer] = Nil

  //The client of this chain
  /*  @BeanProperty
    var client:HFClient = _*/

  //Chain initialized flag
  @BeanProperty
  var isInitialized = false

  //event hubs
  @BeanProperty
  var eventHubs: List[EventHub] = Nil

  /**
    * Get Chain name
    *
    * @return chain name
    **/
  def getName = this.name

  /**
    * Add a peer to chain
    *
    * @param peer
    * @return Chain
    */
  def addPeer(peer: Peer): Chain = {
    require(peer != null, "Peer is null!")
    require(!isEmptyString(peer.name), "Peer name is null")
    require(checkGrpcUrl(peer.getUrl), "Peer url is invalid")

    peer.setChain(this)
    this.peers ::= peer
    this
  }

  /**
    * add orderer to chain
    *
    * @param orderer
    * @return
    */
  def addOrderer(orderer: Orderer): Chain = {
    require(orderer != null, "Orderer is null")
    require(checkGrpcUrl(orderer.url))

    orderer.chain = this
    this.orderers ::= orderer
    this
  }

  /**
    * add eventhub to chain
    *
    * @param eventHub
    * @return
    */
  def addEventHub(eventHub: EventHub): Chain = {
    require(eventHub != null, "event hub")
    require(checkGrpcUrl(eventHub.url))

    //TODO add chainEventQue th eventHUb
    eventHubs ::= eventHub
    this
  }

  def setMemberServiceUrl(url: String, pem: String = null): Unit = {
    this.setMemberServices(new MemberServicesFabricCAImpl(url, pem))
  }

  def getMemberServices = this.memberServices

  def setMemberServices(memberServices: MemberServices): Unit = {
    this.memberServices = memberServices
    memberServices match {
      case m: MemberServicesFabricCAImpl => this.cryptoPrimitives = m.getCrypto
      case _ => //do nothing
    }
  }

  def isSecurityEnable: Boolean = this.memberServices != null

  def isPreFetchMode: Boolean = this.preFetchMode

  def isDevMode: Boolean = this.devMode

  /**
    * initialize the chain
    *
    * @return
    */
  def initialize: Chain = {
    require(peers.size > 0, "Chain need at least one peer.")
    require(!isEmptyString(name), "Chain initialized with null or empty name.")
    require(client != null, "Chain initialized with no client.")
    //    require(this.
    logger.info("initializing.... not implemented....")
    null
  }

  /**
    * Enroll a user which has already been registered
    *
    * @param name
    * @param secret
    * @return
    */
  def enroll(name: String, secret: String): User = {
    getMember(name) match {
      case Some(user) => val enrollment = if (!user.isEnrolled) user.enroll(secret)
      else user.getEnrollment
        members += (name -> user)
        user
      case None => null
    }
  }


  def sendInstallProposal(request: InstallProposalRequest, peers: List[Peer]): List[ProposalResponse] = {
    require(request != null, "sendInstallProposal request is null")
    require(peers != null, "sendInstallProposal peers is null.")
    require(!peers.isEmpty,"sendInstallProposal peers to send to is empty")
    require(isInitialized,"sendInstallProposal on chain not initialized")

    val transactionContext = new TransactionContext

  }

  /**
    * Get the user by name
    *
    * @param name
    * @return
    */
  def getMember(name: String): Option[User] = {
    if (null == keyValueStore) {
      None
    } else if (null == memberServices) {
      None
    } else {
      members.get(name) match {
        case Some(user) => Some(user)
        case _ => val user = new User(name, this)
          user.restoreState
          Some(user)
      }

    }
  }

}
