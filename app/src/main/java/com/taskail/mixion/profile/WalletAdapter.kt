package com.taskail.mixion.profile

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.taskail.mixion.R
import com.taskail.mixion.data.models.local.Wallet
import kotlinx.android.synthetic.main.item_steem_wallet.view.*

/**
 *Created by ed on 3/7/18.
 */

class WalletAdapter(wallet: List<Wallet>): RecyclerView.Adapter<WalletAdapter.WalletViewHolder>(){


    var wallet: List<Wallet> = wallet

        set(items) {
            field = items
            notifyDataSetChanged()
        }

    class WalletViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun setItems(wallet: Wallet) {
            with(itemView){
                title.text = wallet.item
                description.text = wallet.description
                amount.text = wallet.amount
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalletViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_steem_wallet,
                        parent, false)
        return WalletAdapter.WalletViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return wallet.size
    }

    override fun onBindViewHolder(holder: WalletViewHolder, position: Int) {
        holder.setItems(wallet[position])
    }
}