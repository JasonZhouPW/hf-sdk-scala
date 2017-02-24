package com.wanda.blockchain.fabric.sdk

import java.util.concurrent.TimeUnit

import com.wanda.blockchain.fabric.sdk.exception.PeerException
import io.grpc.{ManagedChannel, ManagedChannelBuilder, StatusRuntimeException}
import org.apache.commons.logging.LogFactory
import org.hyperledger.fabric.protos.peer.peer.EndorserGrpc
import org.hyperledger.fabric.protos.peer.proposal.SignedProposal
import org.hyperledger.fabric.protos.peer.proposal_response.ProposalResponse

import scala.beans.BeanProperty
import scala.concurrent.Future

/**
  * Created by Zhou peiwen on 2017/2/23.
  */
class EndorserClient {

  private[this] val logger = LogFactory.getLog(this.getClass)
  @BeanProperty
  var channel:ManagedChannel =_

  @BeanProperty
  var blockingStub :EndorserGrpc.EndorserBlockingStub = _

  @BeanProperty
  var asyncStub:EndorserGrpc.EndorserStub = _

  //for scalaPB, it doesn't create the futureStub,just use the Future[] to do async call

  def this(channelBuilder: ManagedChannelBuilder[_]) = {
    this
    this.channel = channelBuilder.build
  }

  /**
    * Shutdown
    * @return
    */
  def shutDown = channel.shutdown.awaitTermination(5 ,TimeUnit.SECONDS)

  /**
    * send proposal via grpc
    * @param proposal
    * @return
    */
  def sendProposal(proposal: SignedProposal):ProposalResponse = {
    try{
      blockingStub.processProposal(proposal)
    }catch{
      case sre :StatusRuntimeException => logger.warn(s"RPC failed:${sre.getStatus}")
        throw new PeerException("Send proposal failed",sre)
      case _@e => logger.error(s"unexpect exception:${e.getMessage}")
        throw new PeerException("Send proposal failed")
    }
  }

  def sendProposalAsync(proposal: SignedProposal):Future[ProposalResponse] = {
    asyncStub.processProposal(proposal)
  }

  override def finalize(): Unit = {
    try{
      shutDown
    }catch{
      case _ => logger.debug("Failed to shutdown the Peer client")
    }
  }

}
