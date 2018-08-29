package ele.dapp.flow

import co.paralleluniverse.fibers.Suspendable
import ele.dapp.Commands
import ele.dapp.contract.AssetDocContract
import ele.dapp.contract.AssetTraceStateContract
import ele.dapp.dto.AssetDoc
import ele.dapp.queryCriteria
import ele.dapp.schema.AssetSchemaV1
import ele.dapp.schema.AssetTraceSchemaV1
import ele.dapp.state.AssetDocState
import ele.dapp.state.AssetTraceState
import net.corda.core.contracts.Command
import net.corda.core.contracts.StateAndContract
import net.corda.core.flows.*
import net.corda.core.identity.Party
import net.corda.core.node.services.vault.Builder.equal
import net.corda.core.transactions.SignedTransaction
import net.corda.core.transactions.TransactionBuilder
import net.corda.core.utilities.unwrap
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * 计划实现:
 * 1. 资产有扩展表, 附件表
 * 2. 通过交易实现.
 *
 * 仍然是购买方向卖家发出购买资产的标识, 有卖家将AssetTraceState作为input
 *
 * Created by lydon on 2018/8/27.
 */
object AssetTraceFlow {

    /**
     * 添加资产信息
     */
    @InitiatingFlow
    @StartableByRPC
    class AddAsset(private val asset: AssetDoc) : FlowLogic<SignedTransaction>() {

        @Suspendable
        override fun call(): SignedTransaction {

            val myParty = ourIdentity
            val defaultNotary = serviceHub.networkMapCache.notaryIdentities[0]

            val txBuilder = TransactionBuilder(defaultNotary)

            //将附件带过去.
            //txBuilder.addAttachment(SecureHash.parse("this is file hashcode"))

            txBuilder.withItems(
                    StateAndContract(
                            AssetTraceState(asset.assetNo, 1, myParty, asset.userCardId, asset.userCardName, "", ""),
                            AssetTraceStateContract.CONTRACT_ID
                    ),
                    StateAndContract(
                            AssetDocState(
                                    asset,
                                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                                    myParty),
                            AssetDocContract.CONTRACT_ID
                    ),
                    Command(Commands.Create(), myParty.owningKey)
            )

            txBuilder.verify(serviceHub)
            val signedTransaction = serviceHub.signInitialTransaction(txBuilder)
            return subFlow(FinalityFlow(signedTransaction))
        }
    }

    /**
     * 卖方获取资产数据
     */
    @InitiatingFlow
    @StartableByRPC
    class GetAsset(private val assetNo: String, private val sellerParty: Party) : FlowLogic<String>() {
        override fun call(): String {

            val session = initiateFlow(sellerParty)
            // 发送的内容可以进行封装Message
            // 交易由买家完成.
            // [send源代码了解下.]
            session.send(assetNo)

            // 返回值 目前可以写成String, 交易的唯一标识之类的.
            return session.receive<String>().unwrap { it -> it }
        }
    }

    class Responser(private val counterPartySession: FlowSession) : FlowLogic<String>() {
        override fun call(): String {
            val assetNo = counterPartySession.receive<String>().unwrap { it -> it }

            // 查询出对应此assetNo的最新的traceAsset
            val assetStates = queryCriteria { AssetTraceSchemaV1.Persistent::tag.equal(assetNo) }

            // 查出附件
            val txBuilder = TransactionBuilder(ourIdentity)
            return ""
        }

    }

}