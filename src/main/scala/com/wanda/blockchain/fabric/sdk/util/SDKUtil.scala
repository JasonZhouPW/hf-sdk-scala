package com.wanda.blockchain.fabric.sdk.util

import java.util.{Properties, UUID}

import io.netty.handler.codec.http.HttpResponse
import org.apache.http.HttpHost
import org.apache.http.auth.{AuthScope, UsernamePasswordCredentials}
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.protocol.HttpClientContext
import org.apache.http.entity.StringEntity
import org.apache.http.impl.auth.BasicScheme
import org.apache.http.impl.client.{BasicAuthCache, BasicCredentialsProvider, HttpClientBuilder}
import org.apache.http.util.EntityUtils

/**
  * Created by zhou peiwen on 2017/2/13.
  */
object SDKUtil {

  /**
    * Check whether string empty
    * @param s
    * @return
    */
  def isEmptyString(s:String):Boolean = s ==null || s.isEmpty

  def checkGrpcUrl(url:String):Boolean = {
    try{
      val props = parseGrpcUrl(url)
      true
    }catch{
      case _:Exception => false
    }
  }

  def parseGrpcUrl(url:String):Properties = {
    if(isEmptyString(url)) throw new RuntimeException("URL is empty")
    val props = new Properties
    val p = "([^:]+)[:]//([^:]+)[:]([0-9]+)".r
    val m = p.pattern.matcher(url)
    if(m.matches){
      props.setProperty("protocol",m.group(1))
      props.setProperty("host",m.group(2))
      props.setProperty("port",m.group(3))
      props
    }else{
      throw new RuntimeException("URL format error!")
    }
  }


  def httpPost(url:String,body:String,credentials: UsernamePasswordCredentials):String = {
    val provider = new BasicCredentialsProvider

    provider.setCredentials(AuthScope.ANY, credentials)

    val client = HttpClientBuilder.create.setDefaultCredentialsProvider(provider).build

    val httpPost = new HttpPost(url)

    val authCache = new BasicAuthCache

    val targetHost = new HttpHost(httpPost.getURI.getHost,httpPost.getURI.getPort)

    authCache.put(targetHost, new BasicScheme)

    val context = HttpClientContext.create
    context.setCredentialsProvider(provider)
    context.setAuthCache(authCache)

    httpPost.setEntity(new StringEntity(body))

    val response = client.execute(httpPost,context)
    val status = response.getStatusLine.getStatusCode

    val entity = response.getEntity

    val responseBody = if(entity!=null) EntityUtils.toString(entity) else null

    if(status >= 400){
      throw new Exception(s"Post request to $url failed with status code $status. response:$responseBody")
    }

    responseBody
  }

  def generateUUID = UUID.randomUUID.toString
}
