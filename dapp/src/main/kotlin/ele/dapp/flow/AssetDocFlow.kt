package ele.dapp.flow

import co.paralleluniverse.fibers.Suspendable
import ele.dapp.Commands
import ele.dapp.contract.AssetDocContract
import ele.dapp.dto.AssetDoc
import ele.dapp.queryBy
import ele.dapp.queryCriteria
import ele.dapp.schema.AssetSchemaV1
import ele.dapp.state.AssetDocState
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
 *
 * 实现思路:
 * a添加资产, 之后广播出去, b通过资产的唯一标识向a所要资产信息(基于subflow), 相当于b拿到数据持久化到本地.
 *
 * 好处是只会从a中拿到最新的数据, 不会把连带的历史信息拿过来.
 *
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

            val ourOutput = StateAndContract(AssetDocState(
                    assetDoc,
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()),
                    myParty), AssetDocContract.CONTRACT_ID)

            txBuilder.addCommand(Commands.Create(), myParty.owningKey)

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
     * @param assetNo 用于查询的唯一标识.
     * @param sellerParty
     */
    @InitiatingFlow
    @StartableByRPC
    class Initiator(private val assetNo: String, private val sellerParty: Party) : FlowLogic<String>() {

        @Suspendable
        override fun call(): String {
            if (assetNo.isEmpty()) throw IllegalArgumentException("docName can't be empty")
            val session = initiateFlow(sellerParty)
            logger.info("send docName : $assetNo")

            session.send(assetNo)

            // subflow 返回值
            val assetDoc = session.receive<AssetDoc>().unwrap { it -> it }
            logger.info("receivedMessage : $assetDoc")

            /*----------------------------------------------------*/
            val myParty = ourIdentity
            val defaultNotary = serviceHub.networkMapCache.notaryIdentities[0]

            val txBuilder = TransactionBuilder(defaultNotary)

            val ourOutput = StateAndContract(AssetDocState(
                    assetDoc,
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()),
                    myParty), AssetDocContract.CONTRACT_ID)

            txBuilder.addCommand(Commands.Create(), myParty.owningKey)

            txBuilder.withItems(
                    ourOutput
            )

            txBuilder.verify(serviceHub)

            val signedTransaction = serviceHub.signInitialTransaction(txBuilder)
            subFlow(FinalityFlow(signedTransaction))

            // 返回资产名称
            return assetDoc.assetName
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
            val assetNo = counterPartySession.receive(String::class.java).unwrap { it -> it }

            // 构建查询
            val permissionCriteria = queryCriteria { AssetSchemaV1.Persistent::assetNo.equal(assetNo) }
            val states = queryBy<AssetDocState>(permissionCriteria)

            if (states.isEmpty()) throw IllegalArgumentException("can't find AssetDocState by assetNo = $assetNo")

            val assetDoc = states[0].state.data.data

            logger.info("receivedDocName: $assetNo")
            counterPartySession.send(assetDoc)
        }
    }
}