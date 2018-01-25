package com.taskail.mixion.data.source

import eu.bittrade.libs.steemj.SteemJ
import eu.bittrade.libs.steemj.base.models.AccountName
import eu.bittrade.libs.steemj.configuration.SteemJConfig
import eu.bittrade.libs.steemj.enums.PrivateKeyType
import org.apache.commons.lang3.tuple.ImmutablePair
import java.util.ArrayList

/**
 *Created by ed on 1/24/18.
 */
class SteemJClient(accountName : String,
                   activeKey : String,
                   postingKey: String) {

    private var steemJ: SteemJ

    init {
        val steemJConfig = SteemJConfig.getNewInstance()
        steemJConfig.defaultAccount = AccountName(accountName)

        steemJ = SteemJ()

        val privateKeys = ArrayList<ImmutablePair<PrivateKeyType, String>>()
        privateKeys.add(ImmutablePair(PrivateKeyType.POSTING, postingKey))
        privateKeys.add(ImmutablePair(PrivateKeyType.ACTIVE, activeKey))

        steemJConfig.privateKeyStorage.addAccount(steemJConfig.defaultAccount, privateKeys)
    }

    fun getSteemJ() : SteemJ{
        return steemJ
    }

    companion object {
        private var INSTANCE : SteemJ? = null

        /**
         * Returns the single instance of this class, creating one if necessary.
         *
         */

        @JvmStatic
        fun getSteemJInstance(accountName: String, activeKey: String, postingKey: String) : SteemJ{
            return INSTANCE ?: SteemJClient(accountName, activeKey, postingKey)
                    .getSteemJ().apply {
                INSTANCE = this
            }
        }
    }
}