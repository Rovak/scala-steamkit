import akka.actor.ActorSystem
import akka.actor.Props
import akka.testkit.TestKit
import org.rovak.steamclient.steam3.SteamClient.ConnectToServer
import org.rovak.steamclient.steam3.SteamClient
import org.scalatest.WordSpecLike
import org.scalatest.Matchers
import org.scalatest.BeforeAndAfterAll
import akka.testkit.ImplicitSender

import scala.concurrent.duration._
import steam.Server

class SteamConnectSpec(_system: ActorSystem) extends TestKit(_system)
  with ImplicitSender
  with WordSpecLike
  with Matchers
  with BeforeAndAfterAll {

  def this() = this(ActorSystem("Client"))

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "A client must" must {

    "properly login" in {
      val bot = system.actorOf(Props[SteamClient])
      bot ! ConnectToServer(Server("72.165.61.185", 27017))
    }

  }
}