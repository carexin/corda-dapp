package ele.dto

import net.corda.core.serialization.CordaSerializable

/**
 * Created by lydon on 2018/8/27.
 */
@CordaSerializable
data class AssetDoc(
        // 假设资产编号是业务上的唯一标识.
        val assetNo: String = "",//资产编号
        val assetName: String = "",   //资产名称
        val assetContent: String = "",  //资产明细
        val fromParty: String = "", //最初存在于那个节点
        val userCard: String = "" // 所属用户, 需要在api处添加进去. 查询均在api处做的, 可以做到用户数据权限的限制.
)