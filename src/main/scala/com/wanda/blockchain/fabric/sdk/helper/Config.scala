package com.wanda.blockchain.fabric.sdk.helper

import java.io.{File, FileInputStream}
import java.util.Properties

import org.apache.commons.logging.LogFactory

/**
  * Created by Zhou peiwen on 2017/2/23.
  */
object Config {

  private[this] val logger = LogFactory.getLog(this.getClass)

  private[this] val DEFAULT_CONFIG :String = "config.properties"
  private[this] val FABRIC_SDK_CONFIGURATION:String = "fabric.sdk.configuration"
  private[this] val SECURITY_LEVEL:String = "sdk.security_level"
  private[this] val HASH_ALGORITHM:String = "sdk.hash_algorithm"
  private[this] val CACERTS:String = "sdk.cacerts"
  private[this] val PROPOSAL_WAIT_TIME:String = "sdk.proposal.wait.time"

  private[this] val sdkProperties = new Properties

  {
    try{
      val loadFile = new File(System.getProperty(FABRIC_SDK_CONFIGURATION,DEFAULT_CONFIG)).getAbsoluteFile
      logger.debug(s"Loading configuaration from ${loadFile.toString} and it is present: ${loadFile.exists}")
      val configProps = new FileInputStream(loadFile)
      sdkProperties.load(configProps)
    }catch{
      case e:Exception => logger.warn(String.format("Failed to load any configuration from: %s. Using toolkit defaults", DEFAULT_CONFIG))
        logger.warn(s"exception:${e.getMessage}")
    }finally {
      // Default values
      defaultProperty(SECURITY_LEVEL, "256")
      defaultProperty(HASH_ALGORITHM, "SHA2")
      // TODO remove this once we have implemented MSP and get the peer certs from the channel
      defaultProperty(CACERTS, "/genesisblock/peercacert.pem")
      defaultProperty(PROPOSAL_WAIT_TIME, "12000")
    }
  }

  /**
    * get default property
    * @param key
    * @param value
    */
  private def defaultProperty(key :String,value:String):Unit = {
    val ret = System.getProperty(key)
    if(ret != null){
      sdkProperties.put(key,ret)
    }else if(null == sdkProperties.getProperty(key)){
      sdkProperties.put(key,value)
    }
  }

  /**
    * Return property
    * @param property
    * @return
    */
  private def getProperty(property:String):String = sdkProperties.getProperty(property)

  /**
    * Return security level
    * @return
    */
  def getSecurityLevel:Int = getProperty(SECURITY_LEVEL).toInt

  /**
    * Return hash algorithm
    * @return
    */
  def getHashAlgorithm:String = getProperty(HASH_ALGORITHM)

  /**
    * Return peer CA certs
    * @return
    */
  def getPeerCACerts:Array[String] = getProperty(CACERTS).split("'")

  /**
    * Return proposal wait time
    * @return
    */
  def getProposalWaitTime:Long = getProperty(PROPOSAL_WAIT_TIME).toLong

}


