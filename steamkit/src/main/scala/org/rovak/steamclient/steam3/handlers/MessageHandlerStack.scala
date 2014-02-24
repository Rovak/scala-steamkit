package org.rovak.steamclient.steam3.handlers

import akka.actor.Actor
import rovak.steamkit.steam.IPacketMsg
import org.rovak.steamclient.steam3.SteamClient

trait MessageHandlerStack extends Actor {

  def wrappedHandleMessage(message: IPacketMsg)
  def wrappedReceive: Receive

  def receive = {
    case x => if (wrappedReceive.isDefinedAt(x)) wrappedReceive(x) else unhandled(x)
  }

  def internalHandleMessage(message: IPacketMsg) = {
    wrappedHandleMessage(message)
  }
}
