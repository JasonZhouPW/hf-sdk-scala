package com.wanda.blockchain.fabric.sdk.util

/**
  * Created by zhou peiwen on 2017/2/13.
  */
object Test extends App{

  class Cons(val s:String,var i:Int){
    init()

    def init()=i=i+1
  }

  val c = new Cons("a",1)
  println(c.i)
}
