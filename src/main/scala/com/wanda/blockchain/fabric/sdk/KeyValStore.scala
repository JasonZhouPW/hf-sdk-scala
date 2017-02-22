package com.wanda.blockchain.fabric.sdk

/**
  * Created by zhou peiwen on 2017/2/13.
  */
trait KeyValStore {

  /**
    * Get the value associated with name.
    *
    * @param name
    */
  def getValue(name:String):String

  /**
    * Set the value associated with name.
    *
    * @param name
    * @param value
    */
  def setValue(name:String,value:String):String

}
