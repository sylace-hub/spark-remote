import com.typesafe.config._

object ZeConf {
  val env = if (System.getenv("SCALA_ENV") == null) "local-env" else "luster-env"

  val conf = ConfigFactory.load()
  def apply() = conf.getConfig(env)
}