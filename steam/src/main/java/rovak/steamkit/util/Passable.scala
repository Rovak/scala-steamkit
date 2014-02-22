package rovak.steamkit.util

class Passable[T](var value: T) {
  def getValue = value
  def setValue(newValue: T) = {
    value = newValue
  }
}