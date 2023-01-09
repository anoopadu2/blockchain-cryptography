package blockchain

import java.math.BigInteger

/**
 * Zero Knowledge Proof
 */
class ZKP(val p: Int = 11, val g: Int = 2, val ntimes: Int = 50, private val x: Int) {
    private val bp = BigInteger.valueOf(p.toLong())
    private val bg = BigInteger.valueOf(g.toLong())

    val y: Int = bg.pow(x).mod(bp).toInt()

    val r = (0..(p-2)).random()

    val h: Int = bg.pow(r).mod(bp).toInt()

    fun s(b: Int): Int = (r + b * x) % (p - 1)

    fun verify(): Boolean {
        for (i in 1..ntimes) {
            if (!_verify()) return false
        }
        return true
    }

    private fun _verify(): Boolean {
        val b = (0..1).random()
        val left = bg.pow(s(b)).mod(bp).toInt()
        val y_b = if (b == 0) 1 else y
        val right = (h * y_b) % p
        //println("b=$b, r=$r, s(b)=${s(b)}, h=$h, y=$y, left=$left, right=$right")
        return left == right
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val zkp = ZKP(x=23098745)
            println("verify: ${zkp.verify()}")
        }
    }
}

