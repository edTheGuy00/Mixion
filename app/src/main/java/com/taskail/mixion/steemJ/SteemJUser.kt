package com.taskail.mixion.steemJ

import android.util.Log
import eu.bittrade.crypto.core.AddressFormatException
import eu.bittrade.libs.steemj.base.models.AccountName
import eu.bittrade.libs.steemj.configuration.SteemJConfig
import eu.bittrade.libs.steemj.enums.PrivateKeyType
import org.apache.commons.lang3.tuple.ImmutablePair
import java.util.ArrayList

/**
 *Created by ed on 3/14/18.
 *
 *
 */


val steemJConfig = SteemJConfig.getInstance()

fun setupSteemJUserSuccess(userName: String, postingKey: String): Boolean{
    val privateKeys: ArrayList<ImmutablePair<PrivateKeyType, String>> = ArrayList()
    privateKeys.add(ImmutablePair(PrivateKeyType.POSTING, postingKey))

    steemJConfig.defaultAccount = AccountName(userName)
    return try
    {
        steemJConfig.privateKeyStorage.addAccount(steemJConfig.defaultAccount, privateKeys)
        true

    } catch (e: AddressFormatException)
    {
        Log.e("SteemJ Login", "invalid Key")
        false
    }
}