package blockchain

import java.io.File

class BlockChain {
    val blocks: MutableList<Block> = mutableListOf()

    fun createBlock(blockID: String = Util.genUUID(), transactions: List<Transaction> = listOf()) {
        val prevHash = getLastTxn()?.hash ?: ""
        val timeMillis = System.currentTimeMillis()
        val block = Block(blockID, null, prevHash, timeMillis, transactions = transactions.toMutableList())
        blocks.add(block)
    }

    fun addTransaction(txn: Transaction): Boolean {
        if (blocks.isEmpty()) createBlock()
        blocks.last().let {
            if (it.isFull()) {
                it.mineBlock()
                createBlock()
            }
        }
        return blocks.last().addTransaction(txn)
    }

    fun getLastTxn(): Transaction? {
        if (blocks.isEmpty()) return null
        val blockWithTxns = blocks.findLast { it.transactions.isNotEmpty() }
        return blockWithTxns?.transactions?.last()
    }

    fun viewUser(userId: String) {
        val allTxns = blocks.flatMap { it.transactions }
        val userTxns = allTxns.filter { it.fromUser == userId || it.toUser == userId }
        println("${userTxns.size} transactions found for user $userId:")
        for (i in userTxns.indices) {
            println("${i.inc()}. ${userTxns[i]}")
        }
    }

    /**
     * Save block chain data to disk
     */
    fun persist(dir: String = "data") {
        File(dir).mkdirs()

        // write blocks file
        val blocksStr = blocks.map { it.toCsv() }.joinToString("\n")
        File("$dir/$blocksFileName").writeText(blocksStr)

        // write transactions of each block in a separate file, named by blockId
        blocks.forEach { block ->
            val txnsStr = block.transactions.map { txn -> txn.toCsv() }.joinToString("\n")
            File("$dir/${block.blockID}.csv").writeText(txnsStr)
        }
    }

    companion object {

        private val blocksFileName = "blocks.csv"

        /**
         * Restore block chain data from disk files
         */
        fun loadFrom(dir: String = "data"): BlockChain {
            // read blockIDs file
            val blocks = File("$dir/$blocksFileName").readLines()

            val blockChain = BlockChain()

            // read each block of transactions and populate the blockChain object
            blocks.forEach { blockCsv ->
                val block = Block.fromCsv(blockCsv)

                // read transactions file for this block
                val txnStrs = File("$dir/${block.blockID}.csv").readLines()
                val txns = txnStrs.map { txn -> Transaction.fromCsv(txn) }

                block.transactions.addAll(txns)
                blockChain.blocks.add(block)
            }
            return blockChain
        }

    }
}

