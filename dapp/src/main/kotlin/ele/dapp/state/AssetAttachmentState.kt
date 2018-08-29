package ele.dapp.state

import ele.dapp.schema.AssetAttachmentSchemaV1
import net.corda.core.identity.AbstractParty
import net.corda.core.identity.Party
import net.corda.core.schemas.MappedSchema
import net.corda.core.schemas.PersistentState
import net.corda.core.schemas.QueryableState

/**
 * 资产附件 state
 *
 * Created by lydon on 2018/8/29.
 */
class AssetAttachmentState(
        private val assetNo: String, // asset的唯一标识, 用于交易时查到此附件hash
        private val fileHash: String, // 整体的hash
        private val partFileHash: String, // 每个单独文件的hash
        private val fromParty: Party
) : QueryableState {

    override val participants: List<AbstractParty>
        get() = listOf(fromParty)

    override fun generateMappedObject(schema: MappedSchema): PersistentState {
        return when (schema) {
            AssetAttachmentSchemaV1 -> AssetAttachmentSchemaV1.Persistent(
                    assetNo = assetNo,
                    fileHash = fileHash,
                    partFileHash = partFileHash
            )
            else -> throw IllegalArgumentException("Unrecognised schema $schema")
        }
    }

    override fun supportedSchemas(): Iterable<MappedSchema> = listOf(AssetAttachmentSchemaV1)
}