package ele.dapp

import ele.dapp.flow.DistributorDTO
import ele.dapp.flow.DistributorFlow
import ele.dapp.flow.DistributorState
import net.corda.core.node.services.queryBy
import net.corda.core.utilities.getOrThrow
import net.corda.testing.node.MockNetwork
import net.corda.testing.node.StartedMockNode
import org.junit.After
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import java.time.LocalDateTime
import kotlin.test.assertEquals


@FixMethodOrder(MethodSorters.JVM)
class DistributorTests {
    lateinit var network: MockNetwork
    lateinit var a: StartedMockNode
    lateinit var b: StartedMockNode
    lateinit var c: StartedMockNode
    lateinit var d: StartedMockNode

    @Before
    fun setup() {
        network = MockNetwork(listOf("ele.dapp.flow"))
        a = network.createPartyNode()
        b = network.createPartyNode()
        c = network.createPartyNode()
        d = network.createPartyNode()

        listOf(a, b, c, d).forEach {
            it.registerInitiatedFlow(com.phoenix.d2d.ReceiveRegulatoryReportFlow::class.java)
        }
        network.runNetwork()
    }

    @After
    fun tearDown() {
        network.stopNodes()
    }

    @Test
    fun `flow records a transaction in both parties' transaction storages`() {

        val perA = DistributorDTO(1, "李玉栋", "220", "北京望京", "229900", LocalDateTime.now())
        val perB = DistributorDTO(2, "李玉栋", "2233", "北京望京", "2291900", LocalDateTime.now())

        val flow0 = DistributorFlow.Create(listOf(perA, perB).toTypedArray())
        val future0 = a.startFlow(flow0)
        network.runNetwork()
        val signedTx0 = future0.getOrThrow()
        for (node in listOf(a)) {
            val returnTx = node.services.validatedTransactions.getTransaction(signedTx0.id)
            assertEquals(signedTx0, returnTx)
            val states = node.services.vaultService.queryBy<DistributorState>().states.toString()
            println("--- $states")
        }

        val flow1 = DistributorFlow.Create(listOf(perA, perB).toTypedArray())
        val future1 = a.startFlow(flow1)
        network.runNetwork()
        val signedTx1 = future1.getOrThrow()
        for (node in listOf(a)) {
            assertEquals(signedTx1, node.services.validatedTransactions.getTransaction(signedTx1.id))
            val state = node.services.vaultService.queryBy<DistributorState>().states.toString()
            println("--- $state")
        }

    }

}