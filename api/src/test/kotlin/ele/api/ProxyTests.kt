package ele.api

import ele.api.corda.Corda
import org.junit.Test

/**
 * Created by lydon on 2018/8/29.
 */
class ProxyTests{

    @Test
    fun `get proxy from node`(){
        val proxy = Corda.getProxy("localhost:10007","user1","test")
        println(proxy.nodeInfo())
    }
}