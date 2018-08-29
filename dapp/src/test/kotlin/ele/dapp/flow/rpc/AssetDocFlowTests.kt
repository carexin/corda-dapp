package ele.dapp.flow.rpc

import com.google.common.collect.ImmutableList
import ele.dapp.dto.AssetDoc
import ele.dapp.flow.AssetDocFlow
import net.corda.core.utilities.getOrThrow
import net.corda.testing.node.MockNetwork
import net.corda.testing.node.StartedMockNode
import org.junit.After
import org.junit.Before
import org.junit.Test


/**
 *
 * Created by lydon on 2018/8/27.
 */
class AssetDocFlowTests {

    private lateinit var network: MockNetwork
    private lateinit var nodeA: StartedMockNode
    private lateinit var nodeB: StartedMockNode

    @Before
    fun setup() {
        network = MockNetwork(ImmutableList.of("ele.dapp.flow", "ele.dapp.schema", "ele.contract"))
        nodeA = network.createPartyNode(null)
        nodeB = network.createPartyNode(null)
        network.runNetwork()

    }

    @Test
    fun `flow records one assetDoc`() {
        val assetDoc = AssetDoc("ssxzzzfdaf", "this is a assetDoc", "this is content")
        val flow = AssetDocFlow.Create(assetDoc)
        val future = nodeA.startFlow(flow)
        network.runNetwork()
        val signedTx =future.getOrThrow()
        println(signedTx)
    }

    @After
    fun tearDown() {
        network.stopNodes()
    }

}