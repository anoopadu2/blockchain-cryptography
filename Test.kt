package blockchain

import java.io.File

object Test {
    fun populateBlockChain(nusers: Int = 500, ntxns: Int = 10000): BlockChain {
        val users = (1..nusers).map { userId(it) }
        val blockChain = BlockChain()

        var prevHash = ""
        for (i in (1..ntxns)) {
            val txn = genTxn(nusers, prevHash)
            println("$i. $txn")
            blockChain.addTransaction(txn)
            prevHash = txn.hash
        }

        return blockChain
    }

    private fun zeroPaddedNum(number: Int, minLen: Int): String {
        var s = "$number"
        while (s.length < minLen) s = "0$s"
        return s
    }

    private fun userId(number: Int) = "U${zeroPaddedNum(number, 3)}"

    private fun randAmount(): Double = (100..100000).random() / 100.0

    private fun pickUser(nusers: Int): String = userId((1..nusers).random())

    private var lastTime = System.currentTimeMillis() - 400L * 24 * 60 * 60 * 1000
    private fun nextTime() = lastTime + (1000..100000).random()

    private fun genTxn(nusers: Int, prevHash: String): Transaction = Transaction(
        txnId = Util.genUUID(),
        previousHash = prevHash,
        fromUser = pickUser(nusers),
        toUser = pickUser(nusers),
        timeMillis = nextTime(),
        amount = randAmount()
    ).apply { setHash() }

    @JvmStatic
    fun main(args: Array<String>) {
        val dataDir = "testdata"
        val dataExists = File("$dataDir/blocks.csv").exists()

        // if blockchain data already exists on disk, load it.
        // Otherwise, populate the chain with test data and persist it to disk
        val blockChain = if (dataExists) BlockChain.loadFrom(dataDir)
                else populateBlockChain().apply { persist(dataDir)  }

        // view all the transactions involving a particular user (fromUser or toUser in a Txn)
        blockChain.viewUser("U020")
    }
}