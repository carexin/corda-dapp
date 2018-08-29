package ele.dapp.state

import ele.dapp.schema.AssetTraceSchemaV1
import net.corda.core.identity.AbstractParty
import net.corda.core.identity.Party
import net.corda.core.schemas.MappedSchema
import net.corda.core.schemas.PersistentState
import net.corda.core.schemas.QueryableState

/**
 * 这样做也有个劣势, 交易方会获取到所有之前的交易记录.
 *
 * 每次都把此state消费掉, 生成新的, 查询的时候只查询未消费掉的.
 *
 * Created by lydon on 2018/8/28.
 */
data class AssetTraceState(val tag: String,
                           val index: Long,
                           val fromParty: Party,
                           val fromUserId: String,
                           val fromUserName: String,
                           val toParty: String,
                           val toUserName: String) : QueryableState{

    override val participants: List<AbstractParty>
        get() = listOf()

    override fun generateMappedObject(schema: MappedSchema): PersistentState {
        return when (schema) {
            AssetTraceSchemaV1 -> AssetTraceSchemaV1.Persistent(
                    fromParty = fromParty.toString(),
                    tag = tag,
                    index = index,
                    fromUserId = fromUserId,
                    fromUserName = fromUserName,
                    toParty = toParty,
                    toUserName = toUserName)
            else -> throw IllegalArgumentException("Unrecognised schema $schema")
        }
    }

    override fun supportedSchemas(): Iterable<MappedSchema> {
        return listOf(AssetTraceSchemaV1)
    }

}