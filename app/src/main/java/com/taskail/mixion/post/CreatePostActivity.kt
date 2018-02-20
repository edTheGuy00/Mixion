package com.taskail.mixion.post

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.taskail.mixion.R
import com.taskail.mixion.activity.BaseActivity
import com.taskail.mixion.myNewPermLink
import com.taskail.mixion.profile.User
import com.taskail.mixion.data.RxSteemJ
import com.taskail.mixion.data.setupSteemJUserSuccess
import com.taskail.mixion.utils.hideSoftKeyboard
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_create_post.*

/**
 *Created by ed on 2/7/18.
 */
class CreatePostActivity : BaseActivity() {

    companion object {
        @JvmStatic fun newIntent(context: Context): Intent {
            return Intent(context, CreatePostActivity::class.java)
        }

        val POSTED_SUCCESSFULLY = 23
    }

    val TAG = javaClass.simpleName

    private lateinit var fragment: EditPostFragment
    private lateinit var disposable: CompositeDisposable
    private lateinit var steemJ: RxSteemJ
    private lateinit var progressBar: ProgressBar
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeAsUpIndicator(getDrawable(R.drawable.ic_arrow_back))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        progressBar = submit_progress_bar
        updateProgressBar(false, true, 0)
        
//        createPostTitle.setOnFocusChangeListener { view, b ->  }

        disposable = CompositeDisposable()
        steemJ = RxSteemJ(disposable)
        if (setupSteemJUserSuccess(User.getUserName()!!, User.getUserKey()!!)) {
            steemJ.connecToSteemit()
        } else {
            Log.wtf(TAG, "hmmm...")
            finish()
        }
        fragment = supportFragmentManager.findFragmentById(R.id.postBodyContainer) as EditPostFragment
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_new_post, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.action_post -> if (!isLoading) checkAndPost()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkAndPost(){
        if (getPostTitle().isNotEmpty()){

            if (fragment.getBody().isNotEmpty()) {

                if (fragment.getTags().isNotEmpty()) {
                    setLoadingUi()
                    postIt(fragment.getBody(), getPostTitle(), fragment.getTags())

                } else {
                    Toast.makeText(this, R.string.no_tags_error, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, R.string.blank_body_error, Toast.LENGTH_SHORT).show()
            }

        } else {
            Toast.makeText(this, R.string.blank_title_error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun postIt(body: String, title: String, tags: Array<String>){
        disposable.add(steemJ.sendPost(title, body, tags)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError{
                    Toast.makeText(this, R.string.something_wrong, Toast.LENGTH_SHORT).show()
                }
                .subscribe{
                    postedSuccessfully(it)
                })

    }

    private fun setLoadingUi(){
        postingMainLayout.hideSoftKeyboard()
        updateProgressBar(true, true, 0)
        displaySnackBar(R.string.submitting_post)
        isLoading = true
    }

    /**
     * Update the state of the main progress bar that is shown inside the ActionBar of the activity.
     * @param visible Whether the progress bar is visible.
     * @param indeterminate Whether the progress bar is indeterminate.
     * @param value Value of the progress bar (may be between 0 and 10000). Ignored if the
     *              progress bar is indeterminate.
     */
    private fun updateProgressBar(visible: Boolean, indeterminate: Boolean, value: Int) {
        progressBar.isIndeterminate = indeterminate
        if (!indeterminate) {
            progressBar.progress = value
        }
        progressBar.visibility = if (visible) View.VISIBLE else View.GONE
    }

    private fun postedSuccessfully(permLink: String){
        val intent = Intent().putExtra(myNewPermLink, permLink)
        setResult(POSTED_SUCCESSFULLY, intent)
        finish()
    }

    private fun getPostTitle(): String{
        return createPostTitle.text.toString()
    }

    private fun displaySnackBar(msg: Int){
        Snackbar.make(postingMainLayout, msg,
                Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        disposable.clear()
        super.onDestroy()
    }
}