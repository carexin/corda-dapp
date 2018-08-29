package ele.dapp.flow.rpc

import ele.dapp.dto.AssetDoc
import ele.dapp.flow.AssetDocFlow
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
        val host = "localhost:10007"
        val username = "user1"
        val password = "test"
        val proxys = loginToCordaNode(host, username, password)

        val a = proxys.networkMapSnapshot().map { it.legalIdentities[0].name.x500Principal.name }

        println(a)

        val party = proxys.wellKnownPartyFromX500Name(CordaX500Name.parse("O=PartyA,L=London,C=GB"))
        println(party)

        val createHandle = proxys.startFlowDynamic(AssetDocFlow.Initiator::class.java,"bian_hao", party)

        println("rpc返回结果: ${createHandle.returnValue.getOrThrow()}")

    }

    @Test
    fun `add asset`(){
        val host = "localhost:10004"
        val username = "user1"
        val password = "test"
        val proxys = loginToCordaNode(host, username, password)

        val createHandle = proxys.startFlowDynamic(AssetDocFlow.Create::class.java,
               AssetDoc ("bian_hao","name_xxxxxxxxxx", "content_123214325352433242") )

        println("rpc返回结果: ${createHandle.returnValue.getOrThrow()}")
    }

    fun loginToCordaNode(host: String, username: String, password: String): CordaRPCOps {
        val nodeAddress = NetworkHostAndPort.parse(host)
        val client = CordaRPCClient(nodeAddress)
        return client.start(username, password).proxy
    }
}