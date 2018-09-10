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
 * Created by lydon on 2018/9/3.
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
        val distributor = distributorMapper.findById(1)
        println("从数据库中查出的数据: $distributor")

        val proxy = Corda.getProxy("localhost:10007", "user1", "test")

        val createHandle = proxy.startFlowDynamic(DistributorFlow.Create::class.java,
                arrayOf(distributor))

        println("corda rpc返回结果: ${createHandle.returnValue.getOrThrow()}")

    }

}