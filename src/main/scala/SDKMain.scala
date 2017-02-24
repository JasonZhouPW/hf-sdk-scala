import scala.collection.mutable

/**
  * Created by zhou peiwen on 2017/2/10.
  */


object SDKMain extends App{

//  println("hello fabric")
//  val map = mutable.Map[String,String]()
//
//  map += ("aa"->"1")
//
//  println(map)

  var list = List(1,2,3,4,5)
  list = list.filter(_ !=2)

  println(list)

  println(TestObj.hello)
  TestObj.hi
}

object TestObj{
  {
    println("initial...")
  }
  def hello = "hello"
  def hi = {
    println("hi")
  }


}