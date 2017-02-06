name := "hf-sdk-scala"

version := "1.0"

scalaVersion := "2.11.5"

libraryDependencies ++= Seq(
  "io.grpc" % "grpc-netty" % "1.1.1",
  "io.grpc" % "grpc-protobuf" % "1.1.1",
  "io.grpc" % "grpc-stub" % "1.1.1",
  "io.grpc" % "grpc-auth" % "1.1.1",
  "com.google.protobuf" % "protobuf-java" % "3.2.0",
  "org.bouncycastle" % "bcpkix-jdk15on" % "1.56",
  "commons-cli" % "commons-cli" % "1.3.1",
  "org.apache.commons" % "commons-compress" % "1.13",
  "commons-io" % "commons-io" % "2.5",
  "org.apache.httpcomponents" % "httpclient" % "4.5.3",
  "org.glassfish" % "javax.json" % "1.0.4",
  "com.trueaccord.scalapb" % "scalapb-runtime-grpc_2.11" % "0.5.47",
  "io.netty" % "netty-tcnative-boringssl-static" % "1.1.33.Fork19",  // SSL support
  "javassist" % "javassist" % "3.12.1.GA"  // Improves Netty performance

)

PB.targets in Compile := Seq(
  scalapb.gen() -> (sourceManaged in Compile).value
)

// If you need scalapb/scalapb.proto or anything from
// google/protobuf/*.proto
libraryDependencies += "com.trueaccord.scalapb" %% "scalapb-runtime" % com.trueaccord.scalapb.compiler.Version.scalapbVersion % "protobuf"


/*PB.targets in Compile := Seq(
  scalapb.gen(grpc = true, flatPackage = true) -> (sourceManaged in Compile).value
)*/

/*import sbtprotobuf.{ProtobufPlugin=>PB}
PB.protobufSettings
unmanagedResourceDirectories in Compile += (sourceDirectory in PB.protobufConfig).value
javaSource in PB.protobufConfig := ((sourceDirectory in Compile).value / "java"/"generated")
compileOrder := CompileOrder.JavaThenScala*/

/*
sourceDirectory in PB.protobufConfig := baseDirectory.value / "src" / "main" / "proto"

protocOptions in PB.protobufConfig ++= Seq(
  "--plugin=protoc-gen-grpc-java=" + PATH_GRPC_JAVA_PLUGIN,
  "--grpc-java_out=" + baseDirectory.value +    "/target/src_managed/main/compiled_protobuf")*/
