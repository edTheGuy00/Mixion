package com.taskail.mixion.profile

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.taskail.mixion.R
import com.taskail.mixion.data.models.local.Wallet
import kotlinx.android.synthetic.main.fragment_wallet.*


/**
 *Created by ed on 3/6/18.
 */
class WalletFragment: Fragment(), ProfileContract.WalletView {

    override lateinit var presenter: ProfileContract.Presenter

    var userWallet = mutableListOf<Wallet>()

    lateinit var adapter: WalletAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_wallet, container, false)

        adapter = WalletAdapter(userWallet)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        walletRecycler.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(activity)
        walletRecycler.layoutManager = linearLayoutManager
        walletRecycler.itemAnimator = DefaultItemAnimator()
        walletRecycler.adapter = adapter

    }

    override fun setWallet() {
        adapter.notifyDataSetChanged()
    }

    override fun setSteemBalance(balance: String?) {
        if (balance != null) {
            val wallet = Wallet(
                    resources.getString(R.string.steem),
                    resources.getString(R.string.steem_description),
                    balance)

            userWallet.add(wallet)
        }
    }

    override fun setSteemDollars(dollars: String?) {
        if (dollars != null) {
            val wallet = Wallet(
                    resources.getString(R.string.steem_dollars),
                    resources.getString(R.string.steem_dollars_description),
                    dollars)

            userWallet.add(wallet)
        }
    }

    override fun setSavingsBalance(balance: String?) {
        if (balance != null) {
            val wallet = Wallet(
                    resources.getString(R.string.savings),
                    resources.getString(R.string.savings_description),
                    balance)

            userWallet.add(wallet)
        }
    }

}