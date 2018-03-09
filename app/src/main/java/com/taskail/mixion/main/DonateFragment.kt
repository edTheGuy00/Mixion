package com.taskail.mixion.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.taskail.mixion.BaseFragment
import com.taskail.mixion.R
import com.taskail.mixion.utils.*
import kotlinx.android.synthetic.main.fragment_donate.*
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE

/**
 *Created by ed on 2/3/18.
 */

class DonateFragment : BaseFragment(){

    private val TAG = javaClass.simpleName
    companion object {
        @JvmStatic fun newInstance(): DonateFragment {
            return DonateFragment()
        }
    }

    private val payPalLink = "https://www.paypal.me/edgrrT"
    private val bitcoinAdress = "3PmgHaC8C2KzdG9cPVhMTnUmEcyTfzGUSd"
    private val etherAddress = "0xf76436e048b991A1eA046687c30CfB4C99Acc3bf"
    private val BTC = "Bitcoin Address"
    private val ETH = "Ether Address"

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?):
            View? {
        return inflater.inflate(R.layout.fragment_donate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        donateToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        donateContainer.fadeInAnimation()

        paypalImg.setOnClickListener {
            openPayPal(payPalLink)
        }

        bitcoinImg.setOnClickListener {
            showBitcoinDialog()
        }

        etherImg.setOnClickListener {
            showEtherDialog()
        }
    }

    override fun onBackPressed(): Boolean {
        closeFragment(donateContainer)
        return true
    }

    private fun openPayPal(link: String){

        val url = Uri.parse(link)
        val intent = Intent(Intent.ACTION_VIEW, url)

        if (intent.resolveActivity(activity?.packageManager) != null) {
            startActivity(intent)
        }else{
            Toast.makeText(activity, "No web browser installed", Toast.LENGTH_LONG).show()
        }
    }

    private fun showBitcoinDialog(){
        AlertDialog.Builder(context!!)
                .setTitle(R.string.donate_bitcoin)
                .setMessage(R.string.only_bitcoin)
                .setPositiveButton(R.string.copy_bitcoin_address) { _, _ ->
                    copyAddress(bitcoinAdress, BTC)
                }
                .show()
    }

    private fun showEtherDialog(){
        AlertDialog.Builder(context!!)
                .setTitle(R.string.donate_ether)
                .setMessage(R.string.only_ether)
                .setPositiveButton(R.string.copy_ether_address) { _, _ ->
                    copyAddress(etherAddress, ETH)
                }
                .show()
    }

    private fun copyAddress(address: String, coin: String){
        val clipboard = context?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(
                coin,
                address)
        clipboard.primaryClip = clip
        Toast.makeText(context, R.string.address_saved, Toast.LENGTH_SHORT).show()

    }
}