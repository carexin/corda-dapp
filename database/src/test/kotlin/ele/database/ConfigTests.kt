package ele.database

import ele.database.config.YmlConfig
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import javax.annotation.Resource

/**
 * 测试从yml配置文件中获取数据.
 */
@RunWith(SpringRunner::class)
@SpringBootTest
class ConfigTests {

    @Resource
    lateinit var ymlConfig: YmlConfig

    @Test
    fun getProperties() {
        println("host: ${ymlConfig.host}")
    }

}