package procrastination

object Procrastination extends App {
  //av_set_log_level(AV_LOG_QUIET) ?

  def sendVideo(): Unit = {
    val submitter = new VideoSubmitter("test2")
    submitter.processAndSend("/home/kamil/Documents/scala_lab/procrastination/src/main/scala/video.mp4")

  }

  def run(): Unit = {
      val a = ConsumerSelfManaged()
  }

  //sendVideo()
  run()
}
