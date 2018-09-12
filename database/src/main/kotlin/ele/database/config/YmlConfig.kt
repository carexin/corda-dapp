package ele.database.config

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * Created by lydon on 2018/9/12.
 */
@ConfigurationProperties(prefix = "corda-node")
class YmlConfig {
    var host: String = ""
    var username: String = ""
    var password: String = ""
}
