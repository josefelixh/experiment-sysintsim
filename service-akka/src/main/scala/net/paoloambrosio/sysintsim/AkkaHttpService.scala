package net.paoloambrosio.sysintsim

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.http.Http
import akka.http.model.HttpResponse
import akka.http.model.StatusCodes._
import akka.http.server.Directives._
import akka.http.server.ExceptionHandler
import akka.pattern.after
import akka.pattern.ask
import akka.stream.FlowMaterializer
import akka.util.Timeout
import com.typesafe.config.{Config, ConfigFactory}
import net.paoloambrosio.sysintsim.slowdown.SlowdownProviderFactory
import org.jmxtrans.embedded.config.ConfigurationParser

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContextExecutor, Future}

trait Service {
  implicit val system: ActorSystem
  implicit def executor: ExecutionContextExecutor
  implicit val materializer: FlowMaterializer

  def config: Config
  val logger: LoggingAdapter

  lazy val slowdownActor = {
    val slowdownStrategy = config.getString("application.slowdown-strategy")
    val slowdownProvider = SlowdownProviderFactory.notThreadSafe(slowdownStrategy)
    system.actorOf(SlowdownActor.props(slowdownProvider))
  }

  implicit val timeout = Timeout(10 seconds)

  val routes = {
    get {
      complete {
        (slowdownActor ? "hello").mapTo[FiniteDuration].map { d =>
          after(d, using = system.scheduler)(Future("success"))
        }
      }
    }
  }

  implicit def exceptionHandler = ExceptionHandler {
    case t: Throwable => {
      logger.error(t, "Exception caught")
      complete(HttpResponse(InternalServerError, entity = "failure"))
    }
  }
}

object AkkaHttpService extends App with Service {
  override implicit val system = ActorSystem()
  override implicit val executor = system.dispatcher
  override implicit val materializer = FlowMaterializer()

  override val config = ConfigFactory.load()
  override val logger = Logging(system, getClass)

  new ConfigurationParser().newEmbeddedJmxTrans("classpath:jmxtrans.json").start()

  Http().bind(interface = config.getString("http.interface"), port = config.getInt("http.port")).startHandlingWith(routes)

}
