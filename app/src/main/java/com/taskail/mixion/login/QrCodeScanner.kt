package com.taskail.mixion.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.google.zxing.Result
import com.taskail.mixion.R
import com.taskail.mixion.postingKey
import me.dm7.barcodescanner.zxing.ZXingScannerView

/**
 *Created by ed on 2/17/18.
 */

class QrCodeScanner : Activity(), ZXingScannerView.ResultHandler {

    companion object {
        @JvmStatic fun newIntent(context: Context): Intent {
            return Intent(context, QrCodeScanner::class.java)
        }

        val QR_CODE_OK = 23
    }

    val TAG = javaClass.simpleName

    private lateinit var scannerView: ZXingScannerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scannerView = ZXingScannerView(this)
        setContentView(scannerView)
    }

    override fun onResume() {
        super.onResume()
        scannerView.setResultHandler(this)
        scannerView.startCamera()
    }

    override fun onPause() {
        super.onPause()
        scannerView.stopCamera()
    }

    override fun handleResult(result: Result?) {

        if (result != null ){
            if (isQr(result)){
                if (isWrongQrCode(result)){
                    // Wrong QR code was scanned
                    scannerView.resumeCameraPreview(this)
                    Toast.makeText(this, R.string.wrong_qr_code_explanation, Toast.LENGTH_SHORT).show()
                } else {
                    val intent = Intent().putExtra(postingKey, result.text)
                    setResult(QR_CODE_OK, intent)
                    finish()
                }
            }
        }
    }

    private fun isQr(result: Result): Boolean{
        return result.barcodeFormat.toString() == "QR_CODE"
    }

    private fun isWrongQrCode(result: Result): Boolean{
        return result.text == "http://"
    }
}