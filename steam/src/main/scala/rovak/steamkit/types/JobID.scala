package rovak.steamkit.types

import rovak.steamkit.util.stream.BinaryReader

object JobID {

  implicit def LongToJobId(number: Long) = JobID(number)
  implicit def JobIdToLong(job: JobID) = job.id

  /**
   * @return empty jobid
   */
  def invalid = JobID()

}

case class JobID(id: Long = BinaryReader.LongMaxValue) {

}
