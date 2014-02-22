package rovak.steamkit.steam.msg

case class LogOnDetails(
  username: String,
  password: String,

  /**
   * Gets or sets the Steam Guard auth code used to login. This is the code sent to the user's email.
   */
  authCode: String = "",

  /**
   * Gets or sets the sentry file hash for this logon attempt, or null if no sentry file is available.
   */
  sentryFileHash: Option[Array[Byte]] = None,

  /**
   * Gets or sets the account instance. 1 for the PC instance or 2 for the Console (PS3) instance.
   */
  accountInstance: Int = 1,

  /**
   * Gets or sets a value indicating whether to request the Steam2 ticket.
   * This is an optional request only needed for Steam2 content downloads.
   */
  requestSteam2Ticket: Boolean = false)
