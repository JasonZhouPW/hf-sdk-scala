package com.wanda.blockchain.fabric.sdk

import com.wanda.blockchain.fabric.sdk.helper.Config
import org.hyperledger.fabric.protos.peer.chaincode.ChaincodeID


/**
  * Created by Zhou peiwen on 2017/2/23.
  */
trait TransactionRequest {

  //Local chain code path to deploy
  protected var chaincodePath: String = _

  //Chaincode name identifier
  protected var chaincodeName: String = _

  //Chaincode version
  protected var chaincodeVersion: String = _

  // The chaincode ID as provided by the 'submitted' event emitted by a TransactionContext
  private var chainCodeID:ChaincodeID = _

  //The name of function to invoke
  protected var fcn: String = _

  //The args to pass to chaincode invocation
  protected var args: List[String] = _

  //Chaincode language
  private var userCert: Certificate = _

  //Chaincode language
  protected var chaincodeLanguage: ChaincodeLangType.LangType = ChaincodeLangType.GO_LANG

  // The timeout for a single proposal request to endorser in milliseconds
  protected var proposalWaitTime: Long = Config.getProposalWaitTime

  def getChaincodePath: String = Option(this.chaincodePath).getOrElse("")

  def setChaincodePath(chaincodePath: String): TransactionRequest = {
    this.chaincodePath = chaincodePath
    this
  }

  def getChaincodeName: String = this.chaincodeName

  def setChaincodeName(chaincodeName: String): TransactionRequest = {
    this.chaincodeName = chaincodeName
    this
  }

  def getChaincodeVersion: String = this.chaincodeVersion

  def setChaincodeVersion(version: String): TransactionRequest = {
    this.chaincodeVersion = version
    this
  }

  def getChaincodeID: ChaincodeID = this.chainCodeID

  def setChaincodeID(chaincodeID: ChaincodeID): Unit = {
    this.chainCodeID = chaincodeID
    this.chaincodeName = chaincodeID.name
    this.chaincodePath = chaincodeID.path
    this.chaincodeVersion = chaincodeID.version
  }

  def getFcn: String = this.fcn

  def setFcn(fcn: String): TransactionRequest = {
    this.fcn = fcn
    this
  }

  def getArgs: List[String] = this.args

  def setArgs(args: List[String]): TransactionRequest = {
    this.args = args
    this
  }

  def getUserCert: Certificate = this.userCert

  def setUserCert(userCert: Certificate): Unit = this.userCert = userCert

  def getChaincodeLanguage: ChaincodeLangType.LangType = this.chaincodeLanguage

  def setChaincodeLanguage(lang: ChaincodeLangType.LangType): Unit = this.chaincodeLanguage = lang

  def getProposalWaitTime: Long = this.proposalWaitTime

  def setProposalWaitTime(time: Long) = this.proposalWaitTime = time

}

/***
  * Chain code lang type
  *
  * only support go and java currently
  */
object ChaincodeLangType extends Enumeration {
  type LangType = Value
  val GO_LANG, JAVA = Value
}
