package ele.database

import net.corda.client.rpc.CordaRPCClient
import net.corda.core.messaging.CordaRPCOps
import net.corda.core.utilities.NetworkHostAndPort


fun getTestProxy(): CordaRPCOps =
        CordaRPCClient(NetworkHostAndPort.parse("localhost:10004")).start("user1", "test").proxy