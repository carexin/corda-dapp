package ele.dapp.flow

import co.paralleluniverse.fibers.Suspendable
import com.phoenix.d2d.*
import net.corda.core.contracts.Contract
import net.corda.core.flows.InitiatingFlow
import net.corda.core.flows.StartableByRPC
import net.corda.core.identity.AbstractParty
import net.corda.core.identity.Party
import net.corda.core.schemas.MappedSchema
import net.corda.core.schemas.PersistentState
import net.corda.core.schemas.QueryableState
import net.corda.core.serialization.CordaSerializable
import net.corda.core.transactions.LedgerTransaction
import net.corda.core.transactions.SignedTransaction
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

/**
 * flow Distributor 经销商
 */
object DistributorFlow {

    @InitiatingFlow
    @StartableByRPC
    class Create(private val dtoArray: Array<DistributorDTO>) : FlowLogicX<SignedTransaction>() {

        @Suspendable
        override fun call(): SignedTransaction {
            val txBuilder = transactionBuilder(defaultNotary)

            dtoArray.forEach {
                val criteria = queryCriteria { DistributorSchemaV1.Persistent::clientNum.equal(it.clientNum) }
                val hasState = queryBy<DistributorState>(criteria)
                logger.info(it.toString())
                val outputState = DistributorState(
                        address = it.address,
                        taxNum = it.taxNum,
                        clientNum = it.clientNum,
                        clientName = it.clientName,
                        fromParty = myParty,
                        updateTime = it.updateTime,
                        distributorId = it.id)

                if (hasState.isEmpty()) {
                    txBuilder.withItems(outputState + DistributorContract.contractId)
                } else {
                    txBuilder.withItems(hasState.last(), outputState + DistributorContract.contractId)
                }
            }

            txBuilder.withItems(Commands.createCommand(listOf(myParty)))

            val signedTx = txBuilder.verify().sign()
            return notariseAndRecord(signedTx)
        }
    }
}

/**
 * contract
 */
class DistributorContract : Contract {

    companion object {
        @JvmStatic
        val contractId = "ele.dapp.flow.DistributorContract"
    }

    override fun verify(tx: LedgerTransaction) {}
}

/**
 * DTO
 *
 * mybatis赋值的时候需要有一个默认的构造方法.
 *
 */
@CordaSerializable
data class DistributorDTO(
        val id: Int,
        val clientName: String,
        val clientNum: String,
        val address: String,
        val taxNum: String,
        val updateTime: LocalDateTime
) {
    constructor() : this(1, "", "", "", "", LocalDateTime.now())
}

/**
 * schema
 */
object DistributorSchema

object DistributorSchemaV1 : MappedSchema(schemaFamily = DistributorSchema.javaClass,
        version = 1,
        mappedTypes = listOf(Persistent::class.java)
) {
    //默认值必须要有, 否则会报错.
    @Table(name = "m_distributor")
    @Entity
    class Persistent(
            @Column val distributorId: Int = Int.MIN_VALUE,
            @Column val fromParty: String = "",
            @Column val clientName: String = "",
            @Column val clientNum: String = "",
            @Column val address: String = "",
            @Column val taxNum: String = "",
            @Column val updateTime: LocalDateTime = LocalDateTime.MIN
    ) : PersistentState()
}

/**
 * State
 */
data class DistributorState(val distributorId: Int,
                            val address: String,
                            val clientName: String,
                            val clientNum: String,
                            val taxNum: String,
                            val updateTime: LocalDateTime,
                            val fromParty: Party) : QueryableState {
    override val participants: List<AbstractParty>
        get() = listOf(fromParty)

    override fun generateMappedObject(schema: MappedSchema): PersistentState = DistributorSchemaV1.Persistent(

            distributorId = distributorId,
            fromParty = fromParty.toString(),
            address = address,
            clientName = clientName,
            clientNum = clientNum,
            taxNum = taxNum,
            updateTime = updateTime
    )

    override fun supportedSchemas(): Iterable<MappedSchema> = listOf(DistributorSchemaV1)
}


