package rovak.steamkit.util

import rovak.steamkit.steam.language.EMsg

object MsgUtil {

  private val ProtoMask = 0x80000000

  private val EMsgMask = ~MsgUtil.ProtoMask

  def getMsg(msg: Int): EMsg = EMsg.f(msg & MsgUtil.EMsgMask)

  def getGCMsg(msg: Int): Int = msg & MsgUtil.EMsgMask

  def getMsg(msg: EMsg): EMsg = MsgUtil.getMsg(msg.v())

  def isProtoBuf(msg: Int): Boolean = {
    (msg & 0xffffffffL & MsgUtil.ProtoMask) > 0
  }

  def isProtoBuf(msg: EMsg): Boolean = MsgUtil.isProtoBuf(msg.v())

  def makeMsg(msg: Int, protobuf: Boolean): Int = {
    if (protobuf) {
      return msg | MsgUtil.ProtoMask
    }
    msg
  }

  def makeGCMsg(msg: Int, protobuf: Boolean): Int = {
    if (protobuf) {
      return msg | MsgUtil.ProtoMask
    }
    msg
  }

  def makeGCMsg(msg: Int): Int = MsgUtil.makeGCMsg(msg, protobuf = false)

  def makeMsg(msg: Int): Int = MsgUtil.makeMsg(msg, protobuf = false)
}
