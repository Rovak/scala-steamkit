package org.rovak.steamclient.steam3

object MsgUtils {

  val ProtoMask = 0x80000000
  val EMsgMask = ~ProtoMask

  def ToMsg(msg: Int) = msg & EMsgMask

  def IsProtoBuf(msg: Int) = (msg & 0xffffffffL & ProtoMask) > 0
}
