package blockchain

import java.util.*

data class Transaction(
    val txnId: String,
    var hash: String = "",
    val previousHash: String,
    val fromUser: String,
    val toUser: String,
    val timeMillis: Long,
    val amount: Double
) {
    fun toCsv(): String = "$txnId,$hash,$previousHash,$fromUser,$toUser,$timeMillis,$amount"

    fun time(): String = Date(timeMillis).toString()

    override fun toString(): String =
        "txnId=$txnId, time=${time()}, from=$fromUser, to=$toUser, amount=%.2f, hash=$hash, previousHash=$previousHash".format(amount)

    fun computeHash(): String {
        val data = "$txnId,$previousHash,$fromUser,$toUser,$timeMillis,$amount"
        return Util.sha256(data)
    }

    fun setHash() {
        hash = computeHash()
    }

    val MIN_TXN_AMOUNT = 0.5

    fun verifyTransaction(): Boolean {
        // verify txn hash
        if (computeHash() != hash) return false

        // check amount
        if (amount < MIN_TXN_AMOUNT) return false

        // verify ZKP
        val attr = fromUser.hashCode()
        val zkp = ZKP(x=attr)
        return zkp.verify()
    }

    companion object {
        fun fromCsv(row: String): Transaction {
            val cols = row.split(",")
            val (txnId, hash, prevHash, fromUser, toUser) = cols.take(5)
            val (timeMillisStr, amountStr) = cols.drop(5)
            return Transaction(txnId, hash, prevHash, fromUser, toUser, timeMillisStr.toLong(), amountStr.toDouble())
        }
    }
}

