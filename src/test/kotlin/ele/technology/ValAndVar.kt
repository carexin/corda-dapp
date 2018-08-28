package ele.technology

import org.junit.Test

/**
 * Created by lydon on 2018/8/28.
 */

class ValAndVar {

    data class TestData(val address: String, val username: String = "donnie")

    @Test
    fun `val and var test`() {

        // 可以这么做, 在对象中的初始化呢?
        val a: String
        a = "1000"

        // 构造方法中如果没有给定默认值, 再掉用构造方法时必须传入此参数的值.
        //val testData = TestData(username = "aa")

        // val 变量, 被初始化为null, 仍然不可以再次赋值. 和java中的final类似, 是栈中的值不变.

        // plus是返回新的值. number是无法被改编的.
        // 在java中所有的包装类都是final修饰的
        val number : Int = 1000
        number.plus(10)

        println(number)




    }


}