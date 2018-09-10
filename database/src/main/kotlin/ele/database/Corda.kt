package ele.database

import net.corda.client.rpc.CordaRPCClient
import net.corda.core.messaging.CordaRPCOps
import net.corda.core.utilities.NetworkHostAndPort

object Corda {
    fun getProxy(host: String, username: String, password: String): CordaRPCOps {
        val nodeAddress = NetworkHostAndPort.parse(host)
        val client = CordaRPCClient(nodeAddress)
        return client.start(username, password).proxy
    }
}


