package ele.dapp

import net.corda.core.contracts.CommandData
import net.corda.core.contracts.ContractState
import net.corda.core.contracts.StateAndRef
import net.corda.core.flows.FlowLogic
import net.corda.core.node.services.vault.Builder
import net.corda.core.node.services.vault.CriteriaExpression
import net.corda.core.node.services.vault.QueryCriteria
import net.corda.core.node.services.vault.builder
import net.corda.core.schemas.PersistentState

/**
 * Created by lydon on 2018/8/27.
 */
interface Commands : CommandData {
    class Create : Commands
}

inline fun <reified T : ContractState> FlowLogic<*>.queryBy(criteria: QueryCriteria): List<StateAndRef<T>> {
    return serviceHub.vaultService.queryBy(T::class.java, criteria).states
}

inline fun queryCriteria(init: Builder.() -> CriteriaExpression<out PersistentState, Boolean>) = QueryCriteria
        .VaultCustomQueryCriteria(builder(init))