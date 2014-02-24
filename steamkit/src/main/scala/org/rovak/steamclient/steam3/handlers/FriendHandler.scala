package org.rovak.steamclient.steam3.handlers

import org.rovak.steamclient.steam3._
import steam.SteamId
import com.google.protobuf.ByteString
import rovak.steamkit.steam.{ClientMsgProtobuf, IPacketMsg}
import rovak.steamkit.steam.language.{EChatEntryType, EMsg}
import steam.generated.SteammessagesClientserver._

/**
 * Friend Commands like chat and friend requests
 */
trait FriendHandler extends SteamClient {

  import Commands._

  override def internalHandleMessage(message: IPacketMsg) = {
    message.msgType match {
      case _ => super.internalHandleMessage(message)
    }
  }

  override def receive = super.receive orElse {
    case chatmessage @ SendChatMessage(steamid, message) => sendChatMessage(steamid, message)
  }

  /**
   * Sends a chatmessage
   *
   * @param steamId SteamId to which to send the message
   * @param message Text to send
   */
  def sendChatMessage(steamId: SteamId, message: String) {
    val chatMsg = new ClientMsgProtobuf[CMsgClientFriendMsg.Builder](classOf[CMsgClientFriendMsg], EMsg.ClientFriendMsg)
    chatMsg.body.setSteamid(steamId)
    chatMsg.body.setChatEntryType(EChatEntryType.ChatMsg.v())
    chatMsg.body.setMessage(ByteString.copyFromUtf8(message))
    send(chatMsg)
  }
}
