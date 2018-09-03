package ele.dapp.flow

import co.paralleluniverse.fibers.Suspendable
import com.phoenix.d2d.FlowLogicX
import com.phoenix.d2d.defaultNotary
import net.corda.core.flows.InitiatingFlow
import net.corda.core.flows.StartableByRPC
import net.corda.core.serialization.CordaSerializable
import net.corda.core.transactions.SignedTransaction

/**
 * Created by lydon on 2018/9/3.
 */
object DistributorFlow {

    @InitiatingFlow
    @StartableByRPC
    class Create() : FlowLogicX<SignedTransaction>() {

        @Suspendable
        override fun call(): SignedTransaction {

            val txBuilder = transactionBuilder(defaultNotary)
            val signedTransaction = txBuilder.verify().sign()

            return signedTransaction
        }
    }
}

@CordaSerializable
data class DistributorDTO(
        val id: Int,
        val clientName: String,
        val clientNum: String,
        val address: String,
        val taxNum: String
)



