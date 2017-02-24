# hf-sdk-scala
fabric-sdk-scala



### Protobuf bug

there is a protobuf compile error using scalaPB to compile the proto files into scala files

in Column.scala line 165, modify the code like below

```
@SerialVersionUID(0L)
case class String(value: java.lang.String) extends org.hyperledger.fabric.protos.table.Column.Value {
  override def isString: Boolean = true
  override def string: scala.Option[java.lang.String] = Some(value)
  override def number: Int = 1
}
```

