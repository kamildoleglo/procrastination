package procrastination

import cakesolutions.kafka.{KafkaProducer, KafkaProducerRecord}
import org.apache.kafka.common.serialization.{ByteArraySerializer, StringSerializer}
import org.bytedeco.javacpp.opencv_core.Mat
import org.bytedeco.javacpp.{BytePointer, opencv_imgcodecs}
import org.bytedeco.javacv.{FFmpegFrameGrabber, OpenCVFrameConverter}


class VideoSubmitter(topic: String) {

    private val producer = KafkaProducer(
      KafkaProducer.Conf(
        bootstrapServers = "localhost:9092",
        keySerializer = new StringSerializer,
        valueSerializer = new ByteArraySerializer)
    )

    def processAndSend(filename: String): Unit = {
        val grabber = new FFmpegFrameGrabber(filename)
        grabber.start()
        var frame = grabber.grabImage()
        val converter = new OpenCVFrameConverter.ToMat()
        do {
          val mat = converter.convert(frame)
          submitFrame(mat)
          frame = grabber.grabImage()
        } while(frame != null)

        grabber.stop()
    }

    private def submitFrame(frame: Mat) = {
      val matOfByte = new BytePointer()
      opencv_imgcodecs.imencode(".png", frame, matOfByte)
      producer.send(KafkaProducerRecord(topic, Option("a"), matOfByte.getStringBytes))
    }

    def close(): Unit = producer.close()

}

