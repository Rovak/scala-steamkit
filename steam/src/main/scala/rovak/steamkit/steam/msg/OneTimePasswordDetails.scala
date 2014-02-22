package rovak.steamkit.steam.msg

/**
 * The One-Time-Password details for this response.
 */
case class OneTimePasswordDetails(
  passwordType: Int,
  id: String,
  value: Int = 0,
  sharedSecret: Array[Byte],
  timeDrift: Int)

