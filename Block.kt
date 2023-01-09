package blockchain

data class Block(
    val blockID: String,
    var encryptedHash: String? = null,
    val previousHash: String,
    val timeMillis: Long,
    var nonce: Int = 0,
    val transactions: MutableList<Transaction>
) {
    fun toCsv(): String =
        "$blockID,$encryptedHash,$previousHash,$timeMillis,$nonce"

    fun addTransaction(txn: Transaction): Boolean {
        if (!txn.verifyTransaction()) {
            return false
        }
        transactions.add(txn)
        return true
    }

    fun computeHash(): String {
        val data = "$blockID$previousHash$timeMillis$nonce${hashOfAllTxnHashes()}"
        return Util.sha256(data)
    }

    fun mineBlock(difficulty: Int = 2) {
        println("Mining block $blockID ... it may take time depending on difficulty level")
        val zprefix = (1..difficulty).map { "0" }.joinToString("")
        do {
            val hash = computeHash()
            if (hash.startsWith(zprefix)) {
                encryptedHash = encryptHash(hash)
                break
            }
            else nonce++
        } while (nonce < Int.MAX_VALUE)
    }

    fun isMined(): Boolean =
        encryptedHash != null

    private fun hashOfAllTxnHashes(): String {
        val allTxnHashes = transactions.map { it.hash }.joinToString("")
        return Util.sha256(allTxnHashes)
    }

    private fun encryptHash(hash: String, des: DES = DES()): String {
        val hashParts = (0..3).map { i -> hash.substring(i*16, (i+1)*16) }
        val encrHashParts = hashParts.map { des.encrypt(it) }
        return encrHashParts.joinToString("")
    }

    fun isFull(): Boolean =
        transactions.size >= MAX_TXNS_PER_BLOCK

    companion object {
        val MAX_TXNS_PER_BLOCK = 100

        fun fromCsv(csv: String): Block {
            val (blockID, encryptedHash, previousHash, timeMillis, nonce) = csv.split(",")
            return Block(blockID, encryptedHash, previousHash, timeMillis.toLong(), nonce.toInt(), mutableListOf())
        }
    }
}

