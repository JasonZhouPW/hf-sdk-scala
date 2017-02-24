package com.wanda.blockchain.fabric.sdk

import javax.net.ssl.SSLException

import com.wanda.blockchain.fabric.sdk.util.SDKUtil._
import io.grpc.ManagedChannelBuilder
import io.grpc.netty.{GrpcSslContexts, NettyChannelBuilder}

import scala.beans.BeanProperty

/**
  * Created by Zhou peiwen on 2017/2/23.
  */
class EndPoint {

  @BeanProperty
  var addr:String = _

  @BeanProperty
  var port:Int = _


  @BeanProperty
  var channelBuilder:ManagedChannelBuilder[_] = _

  def this(url:String,pem:String) = {
    this
    val purl = parseGrpcUrl(url)
    val protocol = purl.getProperty("protocol")
    this.addr = purl.getProperty("host")
    this.port = purl.getProperty("port").toInt

    this.channelBuilder = protocol.toUpperCase match{
      case "GRPC" => ManagedChannelBuilder.forAddress(addr,port).usePlaintext(true)
      case "GRPCS" => if(isEmptyString(pem)){
        ManagedChannelBuilder.forAddress(addr,port)
      }else{
        try{
          val sslContext = GrpcSslContexts.forClient.trustManager(new java.io.File(pem)).build()
          NettyChannelBuilder.forAddress(addr,port).sslContext(sslContext)
        }catch{
          case e:SSLException => throw new RuntimeException(e)
        }
      }
      case _ => throw new RuntimeException(s"not support the protocol:$protocol")
    }
  }


}
