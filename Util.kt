package blockchain

import java.security.MessageDigest
import java.util.*

object Util {
    fun sha256(input: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val bytes = md.digest(input.toByteArray(Charsets.UTF_8))

        fun byteToHex(byte: Int): String {
            val b1 = byte shr 4
            val b2 = byte - b1 * 16
            return (b1.toString(16) + b2.toString(16))
        }

        return bytes.map { byteToHex(it.toUByte().toInt()) }.joinToString("")
    }

    fun genUUID(): String = UUID.randomUUID().toString()

    @JvmStatic
    fun main(args: Array<String>) {
        println(sha256("Hello World!!"))
    }
}
