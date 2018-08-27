package ele.flow

import co.paralleluniverse.fibers.Suspendable
import ele.Commands
import ele.contract.AssetDocContact
import ele.dto.AssetDoc
import ele.state.AssetDocState
import net.corda.core.contracts.StateAndContract
import net.corda.core.flows.*
import net.corda.core.identity.Party
import net.corda.core.transactions.SignedTransaction
import net.corda.core.transactions.TransactionBuilder
import net.corda.core.utilities.unwrap
import java.text.SimpleDateFormat
import java.util.*

/**
 * 资产相关文档的交易
 * Created by lydon on 2018/8/24.
 */
object AssetDocFlow {

    /**
     * 增加一条资产文档
     */
    @InitiatingFlow
    @StartableByRPC
    class Create(private val assetDoc: AssetDoc) : FlowLogic<SignedTransaction>() {

        @Suspendable
        override fun call(): SignedTransaction {

            val myParty = ourIdentity
            val defaultNotary = serviceHub.networkMapCache.notaryIdentities[0]

            val txBuilder = TransactionBuilder(defaultNotary)

            // TODO 将java date util 转换为其他写法.
            val ourOutput = StateAndContract(AssetDocState(
                    assetDoc,
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()),
                    myParty), AssetDocContact.CONTRACT_ID)

            txBuilder.addCommand(Commands.Create(),myParty.owningKey)

            txBuilder.withItems(
                    ourOutput
            )

            txBuilder.verify(serviceHub)

            val signedTransaction = serviceHub.signInitialTransaction(txBuilder)

            return subFlow(FinalityFlow(signedTransaction))
        }

    }

    /**
     * buyer 购买一份资产
     *
     * @param docName 用于查询的唯一标识.
     * @param sellerParty
     */
    @InitiatingFlow
    @StartableByRPC
    class Initiator(private val docName: String, private val sellerParty: Party) : FlowLogic<String>() {

        @Suspendable
        override fun call(): String {
            if (docName.isEmpty()) throw IllegalArgumentException("docName can't be empty")
            val session = initiateFlow(sellerParty)
            logger.info("send docName : $docName")
            session.send(docName)

            // subflow 返回值
            val receivedMessage = session.receive<String>().unwrap { it -> it }
            logger.info("receivedMessage : $receivedMessage")
            return receivedMessage
        }
    }
}

object AssetDocSellerFlow {

    /**
     * 由卖家将数据推至买家节点
     */
    @InitiatedBy(AssetDocFlow.Initiator::class)
    class Responser(private val counterPartySession: FlowSession) : FlowLogic<Unit>() {

        @Suspendable
        override fun call() {
            val receivedDocName = counterPartySession.receive(String::class.java).unwrap { it -> it }
            logger.info("receivedDocName: $receivedDocName")

        }

    }

}