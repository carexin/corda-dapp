package ele.contract

import net.corda.core.contracts.Contract
import net.corda.core.transactions.LedgerTransaction

/**
 * Created by lydon on 2018/8/27.
 */
class AssetDocContact : Contract {

    companion object {
        @JvmStatic
        val CONTRACT_ID = "ele.contract.AssetDocContact"
    }

    // 合约, 用于校验交易
    override fun verify(tx: LedgerTransaction) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}