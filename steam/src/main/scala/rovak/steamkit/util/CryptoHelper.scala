package rovak.steamkit.util

import org.bouncycastle.jce.provider.BouncyCastleProvider
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import java.nio.ByteBuffer
import java.security._
import java.util.Arrays
import java.util.zip.CRC32

object CryptoHelper {

  def SHAHash(input: Array[Byte]): Array[Byte] = {
    MessageDigest.getInstance("SHA-1").digest(input)
  }

  def SymmetricEncrypt(input: Array[Byte], key: Array[Byte]): Array[Byte] = {
      Security.addProvider(new BouncyCastleProvider())
      var cipher = Cipher.getInstance("AES/ECB/NoPadding", "BC")
      cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"))
      val iv = CryptoHelper.GenerateRandomBlock(16)
      val cryptedIv = cipher.doFinal(iv)
      cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC")
      cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv))
      val cipherText = cipher.doFinal(input)
      val output = Array.ofDim[Byte](cryptedIv.length + cipherText.length)
      System.arraycopy(cryptedIv, 0, output, 0, cryptedIv.length)
      System.arraycopy(cipherText, 0, output, cryptedIv.length, cipherText.length)
      output
  }

  def SymmetricDecrypt(input: Array[Byte], key: Array[Byte]): Array[Byte] = {
    try {
      Security.addProvider(new BouncyCastleProvider())
      var cipher = Cipher.getInstance("AES/ECB/NoPadding", "BC")
      var iv = Array.ofDim[Byte](16)
      val cryptedIv = Arrays.copyOfRange(input, 0, 16)
      var cipherText = Array.ofDim[Byte](input.length - cryptedIv.length)
      cipherText = Arrays.copyOfRange(input, cryptedIv.length, cryptedIv.length + cipherText.length)
      cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"))
      iv = cipher.doFinal(cryptedIv)
      cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC")
      cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv))
      return cipher.doFinal(cipherText)
    } catch {
      case e: Exception => e.printStackTrace()
    }
    Array.ofDim[Byte](0)
  }

  def CRCHash(input: Array[Byte]): Array[Byte] = {
    val crc = new CRC32()
    crc.update(input)
    val hash = crc.getValue
    val buffer = ByteBuffer.allocate(4)
    buffer.putInt(hash.toInt)
    val array = buffer.array()
    val output = Array.ofDim[Byte](array.length)
    for (i <- 0 until array.length) {
      output(array.length - 1 - i) = array(i)
    }
    output
  }

  def AdlerHash(input: Array[Byte]): Array[Byte] = {
    var a = 0
    var b = 0
    for (element <- input) {
      a = (a + element) % 65521
      b = (b + a) % 65521
    }
    val buffer = ByteBuffer.allocate(4)
    buffer.putInt(a | b << 16)
    buffer.array()
  }

  def GenerateRandomBlock(size: Int): Array[Byte] = {
    val block = Array.ofDim[Byte](size)
    val random = new SecureRandom()
    random.nextBytes(block)
    block
  }
}
