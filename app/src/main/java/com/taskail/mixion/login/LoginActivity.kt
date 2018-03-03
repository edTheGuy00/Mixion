package com.taskail.mixion.login

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.widget.Toast
import com.taskail.mixion.MixionApplication
import com.taskail.mixion.R
import com.taskail.mixion.activity.BaseActivity
import com.taskail.mixion.profile.User
import com.taskail.mixion.ui.TextInputValidator
import kotlinx.android.synthetic.main.activity_login.*
import android.app.AlertDialog
import android.support.design.widget.Snackbar
import android.view.View
import com.taskail.mixion.ACTIVITY_REQUEST_QR_CODE_SCANNER
import com.taskail.mixion.data.setupSteemJUserSuccess
import com.taskail.mixion.postingKey


/**
 *Created by ed on 2/3/18.
 */

class LoginActivity : BaseActivity() {

    companion object {
        @JvmStatic fun newIntent(context: Context): Intent{
            return Intent(context, LoginActivity::class.java)
        }

        val RESUlT_LOGIN_OK = 33
    }

    private val TAG = javaClass.simpleName

    private val REQUEST_CAMERA = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        qrCodeBtn.isEnabled = true

        TextInputValidator({
            loginBtn.isEnabled = it
        }, login_username_text, login_key_input)

        loginBtn.setOnClickListener {
            val user = login_username_text.editText?.text.toString()
            val key = login_key_input.editText?.text.toString()

            verifyUserCredentials(user, key)
        }

        qrCodeBtn.setOnClickListener {
            showQrInfoDialog()
        }
    }

    private fun verifyUserCredentials(user: String, key: String){
        if (setupSteemJUserSuccess(user, key)){
            storeAndSetUserCredentials(user, key)
        } else {
            Toast.makeText(this, R.string.invalid_posting_key, Toast.LENGTH_LONG).show()
        }
    }

    private fun storeAndSetUserCredentials(user: String, key: String){
        keystoreCompat.storeSecret("${user};${key}", {
            Log.e("KeyStoreCompat", "Storing credentials failed")
            Toast.makeText(this, R.string.storing_key_error, Toast.LENGTH_LONG).show()
        }, {
            loginUser(user, key)
            setResult(RESUlT_LOGIN_OK)
            finish()
        })
    }

    private fun showQrInfoDialog(){
        AlertDialog.Builder(this)
                .setTitle(R.string.steemit_qrcode_instructions_title)
                .setMessage(R.string.steemit_qrcode_instructions)
                .setPositiveButton(R.string.ok) { _, _ -> openQrCodeScanner() }
                .create()
                .show()
    }

    private fun openQrCodeScanner(){

        // Check if the Camera permission is already available.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED){

            requestCameraPermission()

        } else {
            // Camera permissions is already available.
            Log.i(TAG,
                    "CAMERA permission has already been granted. Opening Scanner.")

            startActivityForResult(QrCodeScanner.newIntent(this),
                    ACTIVITY_REQUEST_QR_CODE_SCANNER)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode){
            ACTIVITY_REQUEST_QR_CODE_SCANNER -> {
                if (resultCode == QrCodeScanner.QR_CODE_OK){
                    login_key_input.editText?.setText(data?.getStringExtra(postingKey))
                }
            }
        }
    }

    private fun requestCameraPermission(){
        Log.i(TAG, "CAMERA permission has NOT been granted. Requesting permission.")

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.CAMERA)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            Log.i(TAG,
                    "Displaying camera permission rationale to provide additional context.")
            Snackbar.make(loginMainLayout, R.string.permission_camera_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, View.OnClickListener {
                        ActivityCompat.requestPermissions(this@LoginActivity,
                                arrayOf(Manifest.permission.CAMERA),
                                REQUEST_CAMERA)
                    })
                    .show()
        } else {

            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),
                    REQUEST_CAMERA)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_CAMERA -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                    Log.i(TAG, "CAMERA permission has now been granted")

                    startActivity(QrCodeScanner.newIntent(this))

                } else {

                    Log.i(TAG, "CAMERA permission was NOT granted.");
                    Snackbar.make(loginMainLayout, R.string.permissions_not_granted,
                            Snackbar.LENGTH_SHORT).show();

                }
                return
            }

        // Add other 'when' lines to check for other
        // permissions this app might request.

            else -> {
                // Ignore all other requests.
            }
        }
    }

}