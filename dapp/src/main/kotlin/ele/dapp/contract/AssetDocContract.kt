package ele.dapp.contract

import net.corda.core.contracts.Contract
import net.corda.core.transactions.LedgerTransaction

/**
 * Created by lydon on 2018/8/27.
 */
class AssetDocContract : Contract {

    companion object {
        @JvmStatic
        val CONTRACT_ID = "ele.dapp.contract.AssetDocContract"
    }

    // 合约, 用于校验交易
    override fun verify(tx: LedgerTransaction) {

    }

}