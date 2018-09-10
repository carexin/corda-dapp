package ele.api.controller

import ele.api.corda.Corda
import ele.dapp.dto.AssetDoc
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Created by lydon on 2018/8/29.
 */
@RestController
@RequestMapping("/api")
class TestController {

    @GetMapping("/test")
    fun test(): String {

        val proxy = Corda.getProxy("localhost:10007", "user1", "test")
        return "this is an api test"
    }

    /**
     * 保存数据
     */
    @PostMapping("/addData")
    fun addData(assetDoc: AssetDoc): String {
        val proxy = Corda.getProxy("localhost:10007", "user1", "test")
        return ""
    }

}