package com.wanda.blockchain.fabric.sdk

import java.io.{FileInputStream, FileNotFoundException, FileOutputStream, IOException}
import java.util.Properties

import org.apache.commons.logging.LogFactory

/**
  * Created by Zhou peiwen on 2017/2/24.
  */
class FileKeyValStore extends KeyValStore{

  private[this] var file:String = _
  private[this] val logger = LogFactory.getLog(this.getClass)

  def this(file:String) = {
    this
    this.file = file
  }
  /**
    * Get the value associated with name.
    *
    * @param name
    */
  override def getValue(name: String): String = {
    val properties = loadProperties
    properties.getProperty(name)
  }

  /**
    * Set the value associated with name.
    *
    * @param name
    * @param value
    */
  override def setValue(name: String, value: String): Unit = {
    val properties = loadProperties
    try{
      val output = new FileOutputStream(file)
      properties.setProperty(name,value)
      properties.store(output,"")
      output.close()
    }catch {
      case e:Exception => logger.warn(s"Could not save the keyValue store,reason:${e.getMessage}")
    }

  }

  /**
    * Load properties file
    * @return
    */
  private def loadProperties :Properties = {
    val properties = new Properties
    try{
      val input = new FileInputStream(file)
      properties.load(input)
      input.close
    }catch {
      case fe: FileNotFoundException => logger.warn(s"Could not find the file $file")
      case ioe:IOException => logger.warn(s"Could not load keyValue store from file:$file,reason:${ioe.getMessage}")
    }
    properties
  }
}
