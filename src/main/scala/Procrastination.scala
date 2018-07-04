package scala

object Procrastination extends App {
  //val canvas = new CanvasFrame("Video")
  //canvas.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE)
  //av_set_log_level(AV_LOG_QUIET) ?

  def sendVideo(): Unit = {
    val submitter = new FrameSubmitter("test")
    submitter.processAndSend("/home/kamil/Documents/scala_lab/procrastination/src/main/scala/video.mp4")

  }

  def run(): Unit = {
      val a = ConsumerSelfManaged()
  }

  sendVideo()
  run()
}
