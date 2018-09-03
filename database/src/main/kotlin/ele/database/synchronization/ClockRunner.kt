package ele.database.synchronization

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime

/**
 * Created by lydon on 2018/9/3.
 */
@Component
class ClockRunner{


    /**
     * 每五秒执行一次.
     */
    @Scheduled(cron = "0/5 * * * * ?")
    fun timerToNow(){
        println("time: ${LocalDateTime.now()}")
    }

}