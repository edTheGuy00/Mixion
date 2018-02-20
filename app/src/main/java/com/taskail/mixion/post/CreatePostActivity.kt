package com.taskail.mixion.post

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ProgressBar
import android.widget.Toast
import com.taskail.mixion.R
import com.taskail.mixion.activity.BaseActivity
import com.taskail.mixion.myNewPermLink
import com.taskail.mixion.profile.User
import com.taskail.mixion.data.RxSteemJ
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

    private lateinit var fragment: EditPostFragment
    private lateinit var disposable: CompositeDisposable
    private lateinit var steemJ: RxSteemJ
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeAsUpIndicator(getDrawable(R.drawable.ic_arrow_back))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        
//        createPostTitle.setOnFocusChangeListener { view, b ->  }

        disposable = CompositeDisposable()
        steemJ = RxSteemJ(disposable)
        steemJ.setupPostingUser(User.getUserName()!!, User.getUserKey()!!)
        steemJ.connecToSteemit()
        fragment = supportFragmentManager.findFragmentById(R.id.postBodyContainer) as EditPostFragment
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_new_post, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.action_post -> checkAndPost()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkAndPost(){
        if (getPostTitle().isNotEmpty()){

            if (fragment.getBody().isNotEmpty()) {

                if (fragment.getTags().isNotEmpty()) {
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

    private fun postedSuccessfully(permLink: String){
        val intent = Intent().putExtra(myNewPermLink, permLink)
        setResult(POSTED_SUCCESSFULLY, intent)
        finish()
    }

    private fun getPostTitle(): String{
        return createPostTitle.text.toString()
    }

    override fun onDestroy() {
        disposable.clear()
        super.onDestroy()
    }
}