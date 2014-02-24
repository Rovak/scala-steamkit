package org.rovak.steamclient.steam3.handlers

import org.rovak.steamclient.steam3.{Commands, SteamClient}
import rovak.steamkit.steam.{ClientMsgProtobuf, IPacketMsg}
import rovak.steamkit.steam.language.{EPersonaState, EMsg}
import steam.generated.SteammessagesClientserver._
import org.rovak.steamclient.steam3.SteamClient.SteamLogin
import akka.actor.Actor

trait BotHandler extends MessageHandlerStack {
  this: SteamClient =>

  import Commands._

  var personaState = Status("Bot", EPersonaState.Online)
  var queuedLogin: Option[SteamLogin] = None
  var isReadyToChangeStatus = false
  var isReadyToLogin = false

  /**
   * Change the status of the bot
   *
   * @param name Name
   * @param status new status
   */
  def setPersonaState(name: String, status: EPersonaState) = {
    val stateMsg = new ClientMsgProtobuf[CMsgClientChangeStatus.Builder](classOf[CMsgClientChangeStatus], EMsg.ClientChangeStatus)
    stateMsg.body.setPersonaState(status.v())
    stateMsg.body.setPlayerName(name)
    send(stateMsg)
  }

  override def internalHandleMessage(message: IPacketMsg) = {
    super.internalHandleMessage(message)
    message.msgType match {
      case EMsg.ClientNewLoginKey =>
        isReadyToChangeStatus = true
        setPersonaState(personaState.name, personaState.status)
      case EMsg.ChannelEncryptResult =>
        println("received login!")
        isReadyToLogin = true
        queuedLogin.map(x => self ! x)
      case _ =>
    }
  }


  def test: Receive = {
    // If we are not ready yet to change status then park the status so it will be picked up later
    case s @ Status(name, status) if !isReadyToChangeStatus =>
      personaState = s
    case Status(name, status) =>
      setPersonaState(name, status)
    case steamLogin: SteamLogin if isReadyToLogin =>
      println("logging in!")
      login(steamLogin)
    case steamLogin: SteamLogin =>
      println("queing login!")
      queuedLogin = Option(steamLogin)
  }

  abstract override def receive = test orElse super.receive
}