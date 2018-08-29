package ele.api.corda

import net.corda.client.rpc.CordaRPCClient
import net.corda.core.messaging.CordaRPCOps
import net.corda.core.utilities.NetworkHostAndPort

/**
val host = "localhost:10007"
val username = "user1"
val password = "test"
val proxys = loginToCordaNode(host, username, password)
 */
object Corda {
    fun getProxy(host: String, username: String, password: String): CordaRPCOps {
        val nodeAddress = NetworkHostAndPort.parse(host)
        val client = CordaRPCClient(nodeAddress)
        return client.start(username, password).proxy
    }
}


