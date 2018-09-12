package ele.database

import ele.database.config.YmlConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling


@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(YmlConfig::class)
class DatabaseApplication

fun main(args: Array<String>) {
    runApplication<DatabaseApplication>(*args)
}
