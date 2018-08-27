package get.test

import ele.flow.TwoPartyFlow
import net.corda.client.rpc.CordaRPCClient
import net.corda.core.identity.CordaX500Name
import net.corda.core.messaging.CordaRPCOps
import net.corda.core.utilities.NetworkHostAndPort
import net.corda.core.utilities.getOrThrow
import org.junit.Test

/**
 * Created by lydon on 2018/8/23.
 */
class FlowTests {

    @Test
    fun startTrasactionRpc() {
        val host = "localhost:10004"
        val username = "user1"
        val password = "test"
        val proxys = loginToCordaNode(host, username, password)

        val a = proxys.networkMapSnapshot().map { it.legalIdentities[0].name.x500Principal.name }

        println(a)

        val party = proxys.wellKnownPartyFromX500Name(CordaX500Name.parse("O=PartyB,L=New York,C=US"))
        println(party)

        val createHandle = proxys.startFlowDynamic(TwoPartyFlow::class.java, party, "你好")

        println("rpc返回结果: ${createHandle.returnValue.getOrThrow()}")

    }

    fun loginToCordaNode(host: String, username: String, password: String): CordaRPCOps {
        val nodeAddress = NetworkHostAndPort.parse(host)
        val client = CordaRPCClient(nodeAddress)
        return client.start(username, password).proxy
    }
}