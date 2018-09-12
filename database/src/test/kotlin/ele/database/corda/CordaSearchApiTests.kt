package ele.database.corda

import ele.dapp.flow.DistributorSchemaV1
import ele.dapp.flow.DistributorState
import ele.dapp.queryCriteria
import ele.database.getTestProxy
import net.corda.core.node.services.Vault
import net.corda.core.node.services.vault.*
import org.junit.Test

/**
 * 此测试不使用Corda.kt下的连接方式
 */
class CordaSearchApiTests {

    /**
     * 获取所有数据
     */
    @Test
    fun `get all DistributorState`() {

        val proxy = getTestProxy()
        // 查询所有Distributor所有数据
        val vault = proxy.vaultQuery(DistributorState::class.java).states
        println(vault)

        // val generalCriteria = QueryCriteria.VaultQueryCriteria()
        //val maxTime = DistributorState::data

        // proxy.vaultQueryByCriteria(queryCriteria,DistributorState::class.java)

    }

    @Test
    fun `get by clientName from distributor`() {
        val proxy = getTestProxy()
        // UNCONSUMED 查询未被消耗的.
        val genericCriteria = QueryCriteria.VaultQueryCriteria(Vault.StateStatus.UNCONSUMED)

        val results = builder {
            // 客户名称叫tom0的.
            val clientNameIndex = DistributorSchemaV1.Persistent::clientName.equal("tom1")

            val customQueryCriteria = QueryCriteria.VaultCustomQueryCriteria(clientNameIndex)

            val criteria = genericCriteria.and(customQueryCriteria)
            proxy.vaultQueryByCriteria(criteria, DistributorState::class.java)

        }
        println("""查询结果: ${results.states[0].state.data}""")
    }

    /**
     * 去corda节点中查询出最新的updateTime, 通过这个时间去mysql数据库中查询出需要重新导入的数据.
     *
     * 之所以这么做, 是因为如果说每次都获取前24小时的数据, 可能某次因为网络或者其他原因导致无法获取, 此时可能存在遗漏数据.
     */
    @Test
    fun `get max time of distributor`() {

        val criteria = QueryCriteria.VaultQueryCriteria(Vault.StateStatus.UNCONSUMED)
        val page = PageSpecification(1, 1)
        val sortAttribute = SortAttribute.Custom(DistributorSchemaV1.Persistent::class.java,"updateTime")
        val sort = Sort(setOf(Sort.SortColumn(sortAttribute,Sort.Direction.DESC)))

        val proxy = getTestProxy()
        val result = proxy.vaultQueryBy(criteria,page,sort,DistributorState::class.java)

        print("updateTIme 为最新的数据 : ${result.states[0].state.data}")

    }


}