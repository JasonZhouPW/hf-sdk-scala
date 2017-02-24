package com.wanda.blockchain.fabric.test

import java.io.File

import com.wanda.blockchain.fabric.sdk.{FileKeyValStore, HFClient}

/**
  * Created by Zhou peiwen on 2017/2/24.
  */
object Test extends App{

  val CHAIN_CODE_NAME = "example_cc.go"
  val CHAIN_CODE_PATH = "github.com/example.cc"
  val CHAIN_CODE_VERSION = "1.0"

  val CHAIN_NAME = "myc1"

  val PEER_LOCATIONS = List("grpc://localhost:7051")
  val ORDERER_LOCATIONS = List("grpc://localhost:7052")
  val EVENTHUB_LOCATIONS = List("grpc://localhost:7053")
  val FABRIC_CA_SERVICES_LOCATION = "http://10.15.190.203:7054"

  setup

  def setup:Unit = {
    val client = HFClient.newInstance
    constructChain(client)
    val chain = client.getChain(CHAIN_NAME).get
    chain.setInvokeWaitTime(1000)
    chain.setDeployWaitTime(12000)

    chain.setMemberServiceUrl(FABRIC_CA_SERVICES_LOCATION)
    val fileStore = new File(System.getProperty("user.home")+"/test.properties")
    if(fileStore.exists)fileStore.delete

    chain.setKeyValueStore(new FileKeyValStore(fileStore.getAbsolutePath))
    chain.enroll("admin","adminpw")

    chain.initialize

    val peers = chain.getPeers
    val orderers = chain.getOrderers

    println("creating install proposal...")

    val installProposalRequest = client.newInstallProposalRequest
    installProposalRequest.setChaincodeName(CHAIN_CODE_NAME)
    installProposalRequest.setChaincodePath(CHAIN_CODE_PATH)
    installProposalRequest.setChaincodeVersion(CHAIN_CODE_VERSION)

    val resonses = chain.send

  }

  def constructChain(client:HFClient):Unit = {
    val newChain = client.newChain(CHAIN_NAME)

    PEER_LOCATIONS.foreach(p =>{
      val peer = client.newPeer(p)
      peer.setName("peer1")
      newChain.addPeer(peer)})

    ORDERER_LOCATIONS.foreach(p => {
      val orderer = client.newOrderer(p)
      newChain.addOrderer(orderer)
    })

    EVENTHUB_LOCATIONS.foreach(p=>{
      val eh = client.newEventHub(p)
      newChain.addEventHub(eh)
    })
  }
}
