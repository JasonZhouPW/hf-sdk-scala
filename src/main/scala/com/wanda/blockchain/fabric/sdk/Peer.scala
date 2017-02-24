package com.wanda.blockchain.fabric.sdk

import com.wanda.blockchain.fabric.sdk.exception.PeerException
import com.wanda.blockchain.fabric.sdk.util.SDKUtil
import org.apache.commons.logging.LogFactory
import org.hyperledger.fabric.protos.peer.proposal.SignedProposal
import org.hyperledger.fabric.protos.peer.proposal_response.ProposalResponse

import scala.beans.BeanProperty
import scala.concurrent.Future

/**
  * Created by zhou peiwen on 2017/2/13.
  */
class Peer {

  private val logger = LogFactory.getLog(this.getClass)

  @BeanProperty
  var name: String = _

  @BeanProperty
  var url: String = _

  @BeanProperty
  var chain: Chain = _

  @BeanProperty
  var endorserClient: EndorserClient = _

  def this(url: String, pem: String) = {
    this
    if (SDKUtil.checkGrpcUrl(url)) {
      this.url = url
      this.endorserClient = new EndorserClient(new EndPoint(url, pem).getChannelBuilder)
    } else {
      throw new PeerException("Bad peer url.")
    }

  }

  /**
    * send proposal to Chain
    *
    * @param proposal
    * @return
    */
  def sendProposal(proposal: SignedProposal): ProposalResponse = {
    checkSendProposal(proposal)
    logger.debug(s"peer.sendProposal:$proposal")
    endorserClient.sendProposal(proposal)
  }

  /**
    * send proposal to Chain async
    *
    * @param proposal
    * @return
    */
  def sendProposalAsync(proposal: SignedProposal): Future[ProposalResponse] = {
    checkSendProposal(proposal)
    logger.debug(s"peer.sendProposalAsync:$proposal")
    endorserClient.sendProposalAsync(proposal)
  }

  /**
    * check the proposal arguments
    *
    * @param proposal
    */
  private def checkSendProposal(proposal: SignedProposal): Unit = {
    require(proposal != null, "proposal is null")
    require(chain != null, "Chain is null")
    require(SDKUtil.checkGrpcUrl(url), "Bad peer url")
  }

  /**
    * remove the peer from the chain
    */
  def remove: Unit = {
    chain.peers = chain.peers.filter(_ != this)
  }

  override def equals(obj: scala.Any): Boolean = {
    obj match{
      case p:Peer if(p.name == this.name && p.url == this.url) => true
      case _ => false
    }
  }

  override def hashCode(): Int = this.getName.hashCode + this.getUrl.hashCode * 8

}
