package ele.database

import org.junit.Test
import java.io.FileInputStream
import java.util.*

class PropertiesTests{

    @Test
    fun `read Properties`(){
        val properties = Properties()
        val propertiesFile = System.getProperty("user.dir") + "\\file.properties"

        val inputStream = FileInputStream(propertiesFile)
        properties.load(inputStream)

        properties.forEach{(k, v) -> println("key = $k, value = $v")}

        println("根据键获取值: host: ${properties["corda.host"]}")

    }
}