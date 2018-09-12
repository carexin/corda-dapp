package ele.database

import ele.dapp.flow.DistributorFlow
import ele.database.mapper.DistributorMapper
import net.corda.core.utilities.getOrThrow
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import javax.annotation.Resource

/**
 * 测试从mysql数据库中获取数据, 存入corda节点.
 */
@RunWith(SpringRunner::class)
@SpringBootTest
class DatabaseApplicationTests {

    @Resource
    lateinit var distributorMapper: DistributorMapper

    @Test
    fun test() {
        // 批量数据
        //val a = distributorMapper.listDistributor()

        // mybatis 从目标数据库查询数据 此example为单条数据测试.
        val distributor = distributorMapper.findById(20)
        println("从数据库中查出的数据: $distributor")

        val proxy = Corda.getProxy()

        val createHandle = proxy.startFlowDynamic(DistributorFlow.Create::class.java,
                arrayOf(distributor))

        println("corda rpc返回结果: ${createHandle.returnValue.getOrThrow()}")

    }

    /**
     * 在数据库中查询出数据
     */
    @Test
    fun `get from mysql use mybatis`() {

        val distributorDTOList = distributorMapper.listDistributor()
        println(distributorDTOList)
    }

}