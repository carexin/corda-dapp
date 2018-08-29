package ele.api.controller

import org.springframework.web.bind.annotation.GetMapping
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

        return "this is an api test"
    }

}