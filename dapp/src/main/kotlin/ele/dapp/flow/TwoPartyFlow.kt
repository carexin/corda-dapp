package ele.dapp.flow

import co.paralleluniverse.fibers.Suspendable
import net.corda.core.flows.*
import net.corda.core.identity.Party
import net.corda.core.utilities.unwrap

/**
 * Created by lydon on 2018/8/24.
 */
@InitiatingFlow
@StartableByRPC
class TwoPartyFlow(private val counterparty: Party, private val message: String) : FlowLogic<String>() {

    @Suspendable
    override fun call(): String {
        val session = initiateFlow(counterparty)

        logger.info("准备发出的数据: $message")
        session.send(message)

        val receivedNumber = session.receive<String>().unwrap { it -> it }
        logger.info("接收到的数据: $receivedNumber")
        return receivedNumber
    }
}

@InitiatedBy(TwoPartyFlow::class)
class Responser(private val counterPartySession: FlowSession) : FlowLogic<Unit>() {

    @Suspendable
    override fun call() {

        val receivedInt = counterPartySession.receive(String::class.java).unwrap { it ->
            it
        }
        logger.info("获取到的数据: $receivedInt")

        counterPartySession.send("收到")
        logger.info("发回给对方的数据 ${receivedInt + 1}")
    }


}