package procrastination

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import cakesolutions.kafka.KafkaConsumer
import cakesolutions.kafka.akka.KafkaConsumerActor.{Confirm, Subscribe}
import cakesolutions.kafka.akka.{ConsumerRecords, Extractor, KafkaConsumerActor}
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.common.serialization.{ByteArrayDeserializer, StringDeserializer}
import org.bytedeco.javacpp.opencv_core.Mat
import org.bytedeco.javacpp.opencv_imgcodecs
import org.bytedeco.javacv.{CanvasFrame, OpenCVFrameConverter}

import scala.concurrent.duration._

/**
  * Simple Kafka Consumer using ManualPartition subscription mode, subscribing to topic: 'topic1'.
  *
  * Kafka bootstrap server can be provided as an environment variable: -DKAFKA=127.0.0.1:9092 (default).
  */

object ConsumerSelfManaged {

  /*
   * Starts an ActorSystem and instantiates the below Actor that subscribes and
   * consumes from the configured KafkaConsumerActor.
   */
  def apply(): ActorRef = {
    val consumerConf = KafkaConsumer.Conf(
      new StringDeserializer,
      new ByteArrayDeserializer,
      groupId = "gid",
      enableAutoCommit = false,
      autoOffsetReset = OffsetResetStrategy.EARLIEST)


    val actorConf = KafkaConsumerActor.Conf(1.seconds, 3.seconds)

    val system = ActorSystem()
    system.actorOf(Props(new ConsumerSelfManaged(consumerConf, actorConf)))
  }
}

class ConsumerSelfManaged(
                           kafkaConfig: KafkaConsumer.Conf[String, Array[Byte]],
                           actorConfig: KafkaConsumerActor.Conf) extends Actor with ActorLogging {

  val recordsExt: Extractor[Any, ConsumerRecords[String, Array[Byte]]] = ConsumerRecords.extractor[String, Array[Byte]]

  val consumer: ActorRef = context.actorOf(
    KafkaConsumerActor.props(kafkaConfig, actorConfig, self)
  )

  consumer ! Subscribe.AutoPartition(List("test2"))

  def receive: Receive = {

    // Records from Kafka
    case recordsExt(records) =>
      processRecords(records)
      sender() ! Confirm(records.offsets)
    case _ => log.info("Unknown message")
  }

  val converter = new OpenCVFrameConverter.ToMat()
  val canvas = new CanvasFrame("Video")
  canvas.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE)
  canvas.setCanvasSize(720, 480)

  private def processRecords(records: ConsumerRecords[String, Array[Byte]]): Unit = {
    records.values.foreach {
      value =>
        val mat = new Mat(value, false)
        val mat2 = opencv_imgcodecs.imdecode(mat, 1)
        val img = converter.convert(mat2)
        canvas.showImage(img)
    }
  }

}