package ele.dapp.schema

import net.corda.core.schemas.MappedSchema
import net.corda.core.schemas.PersistentState
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

/**
 * Created by lydon on 2018/8/28.
 */
object AssetTraceSchema

object AssetTraceSchemaV1 : MappedSchema(
        schemaFamily = AssetTraceSchema.javaClass,
        version = 1,
        mappedTypes = listOf(Persistent::class.java)) {

    @Entity
    @Table(name = "ele_asset_trace")
    class Persistent(
            @Column val tag: String = "",
            @Column val fromParty: String = "",
            @Column val index: Long,
            @Column val fromUserId: String = "",
            @Column val fromUserName: String = "",
            @Column val toParty: String = "",
            @Column val toUserName: String = ""
    ) : PersistentState()
}