package ele.dapp.schema

import net.corda.core.schemas.MappedSchema
import net.corda.core.schemas.PersistentState
import javax.persistence.Column

/**
 * Created by lydon on 2018/8/29.
 */
object AssetAttachmentSchema

object AssetAttachmentSchemaV1:MappedSchema(
        schemaFamily = AssetAttachmentSchema.javaClass,
        version = 1,
        mappedTypes = listOf(Persistent::class.java)){

    /**
    val assetNo: String, // asset的唯一标识, 用于交易时查到此附件hash
    val fileHash: String, // 整体的hash
    val partFileHash: String // 每个单独文件的hash
     */
    class Persistent(
            @Column val assetNo:String = "",
            @Column val fileHash:String = "",
            @Column val partFileHash:String=""

    ):PersistentState()
}