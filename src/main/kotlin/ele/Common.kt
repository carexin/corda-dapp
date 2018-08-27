package ele

import net.corda.core.contracts.Command
import net.corda.core.contracts.CommandData
import net.corda.core.contracts.ContractState
import net.corda.core.identity.AbstractParty

/**
 * Created by lydon on 2018/8/27.
 */
interface Commands : CommandData {
    class Create : Commands
    class Update : Commands

    companion object {
        inline fun createCommand(participants: List<AbstractParty>)
                = Command(Create(), participants.map { it.owningKey })

        inline fun updateCommand(participants: List<AbstractParty>)
                = Command(Update(), participants.map { it.owningKey })

        inline fun createCommand(state: ContractState)
                = createCommand(state.participants)

        inline fun updateCommand(state: ContractState)
                = updateCommand(state.participants)
    }
}