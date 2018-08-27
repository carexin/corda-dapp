package ele.flow

import ele.dto.AssetDoc
import net.corda.core.flows.InitiatingFlow
import net.corda.core.flows.StartableByRPC

/**
 * 计划实现:
 * 1. 资产有扩展表, 附件表
 * 2. 通过交易实现.
 * Created by lydon on 2018/8/27.
 */
object AssetTraceFlow{

    /**
     * 添加资产信息
     */
    @InitiatingFlow
    @StartableByRPC
    class AddAsset(val asset: AssetDoc){

    }
}