package ele.dto

import net.corda.core.serialization.CordaSerializable

/**
 * Created by lydon on 2018/8/27.
 */
@CordaSerializable
data class AssetDoc(
        val assetNo: String = "",//资产编号
        val assetName: String = "",   //资产名称
        val assetContent: String = ""  //资产明细
)