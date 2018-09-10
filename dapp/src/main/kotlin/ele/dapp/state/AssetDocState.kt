package ele.dapp.state

import ele.dapp.dto.AssetDoc
import ele.dapp.schema.AssetSchemaV1
import net.corda.core.identity.AbstractParty
import net.corda.core.identity.Party
import net.corda.core.schemas.MappedSchema
import net.corda.core.schemas.PersistentState
import net.corda.core.schemas.QueryableState

/**
 * Created by lydon on 2018/8/27.
 */
class AssetDocState(val data: AssetDoc,
                    val updateTime: String,
                    private val fromParty: Party
) : QueryableState {
    override val participants: List<AbstractParty>
        get() = listOf(fromParty)

    override fun generateMappedObject(schema: MappedSchema): PersistentState {
        return when (schema) {
            AssetSchemaV1 -> AssetSchemaV1.Persistent(
                    fromParty = fromParty.toString(),
                    assetNo = data.assetNo,
                    assetName = data.assetName,
                    assetContent = data.assetContent
            )
            else -> throw IllegalArgumentException("unrecognised schema $schema")
        }
    }

    override fun supportedSchemas(): Iterable<MappedSchema> {
        return listOf(AssetSchemaV1)
    }

}