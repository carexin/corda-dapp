package ele.database

import net.corda.client.rpc.CordaRPCClient
import net.corda.core.messaging.CordaRPCOps
import net.corda.core.utilities.NetworkHostAndPort
import org.springframework.stereotype.Component
import java.io.FileInputStream
import java.util.*

@Component
object Corda {

    fun getProxy(): CordaRPCOps {
        val properties = Properties()
        val propertiesFile = System.getProperty("user.dir") + "\\file.properties"

        val inputStream = FileInputStream(propertiesFile)
        properties.load(inputStream)

        val host = properties["corda.host"].toString()
        val username = properties["corda.username"].toString()
        val password = properties["corda.password"].toString()

        return CordaRPCClient(hostAndPort = NetworkHostAndPort.parse(host)).start(username = username, password = password).proxy
    }

}


