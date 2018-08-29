package ele.dapp.schema

import net.corda.core.schemas.MappedSchema
import net.corda.core.schemas.PersistentState
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

/**
 * Created by lydon on 2018/8/27.
 */
object AssetSchema

object AssetSchemaV1 : MappedSchema(schemaFamily = AssetSchema.javaClass,
        version = 1,
        mappedTypes = listOf(Persistent::class.java)) {

    @Entity
    @Table(name = "ele_asset_doc")
    class Persistent(
            @Column val fromParty: String = "",
            @Column val assetNo: String = "",
            @Column val assetName: String = "",
            @Column val assetContent: String = ""
    ) : PersistentState()
}