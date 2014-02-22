package rovak.steamkit.steam.msg

import rovak.steamkit.steam.language.EResult

case class MachineAuthDetails(
  jobId: Long,
  result: EResult,
  bytesWritten: Int,
  offset: Int,
  filename: String,
  filesize: Int,
  lastError: Int,
  sentryFileHash: Array[Byte],
  oneTimePassword: OneTimePasswordDetails)