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
import com.taskail.mixion.*
import com.taskail.mixion.activity.BaseActivity
import com.taskail.mixion.data.source.local.Drafts
import com.taskail.mixion.main.steemitRepository
import com.taskail.mixion.steemJ.*
import com.taskail.mixion.utils.hideSoftKeyboard
import kotlinx.android.synthetic.main.activity_create_post.*

/**
 *Created by ed on 2/7/18.
 */
class CreatePostActivity : BaseActivity() {

    companion object {
        @JvmStatic fun newIntent(context: Context): Intent {
            return Intent(context, CreatePostActivity::class.java)
        }

        fun openDraft(context: Context,
                      draft: Drafts):
                Intent {
            return Intent(context, CreatePostActivity::class.java)
                    .putExtra(openDraftIntent, draft)

        }

        const val POSTED_SUCCESSFULLY = 23
    }

    val TAG = javaClass.simpleName

    private var posted = false
    private var saved = false
    private var isFromDraft = false

    private lateinit var draft: Drafts

    private lateinit var fragment: EditPostFragment
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


        if (!confirmUserLoggedIn())
        {
            if (keystoreCompat.hasSecretLoadable()) {
                redoLogIn()
            } else {
                Log.e(TAG, "No Key or user Found")
                // A User Who isn't logged in shouldn't be here
                finish()
            }
        }
        fragment = supportFragmentManager
                .findFragmentById(R.id.postBodyContainer)
                as EditPostFragment

        if (intent.hasExtra(openDraftIntent)) {
            handleIntent(intent)
        }
    }

    private fun handleIntent(intent: Intent) {
        val draft = intent.extras[openDraftIntent]
        if (draft != null) {
            setupDraft(draft as Drafts)
            this.draft = draft
            isFromDraft = true
        }

    }

    private fun setupDraft(draft: Drafts) {
        setPostTitle(draft.title)
        fragment.setBody(draft.body)
        fragment.setTags(draft.tags.toMutableList())
    }

    override fun onResume() {
        super.onResume()
        if (User.userIsLoggedIn) {
            RxSteemJManager.registerSteemJUser(CREATE_POST_STEEMJ_USER)
        }
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

    private fun checkAndSave(){
        if (getPostTitle().isNotEmpty()){

            if (fragment.getBody().isNotEmpty()) {

                if (fragment.getTags().isNotEmpty()) {

                    if (isFromDraft){
                        draft.title = getPostTitle()
                        draft.body = fragment.getBody()
                        draft.tags = getListOfTags()
                        saveDraft(draft)
                    } else {
                        val draft = Drafts(getPostTitle(), fragment.getBody(), getListOfTags())

                        saveDraft(draft)
                    }

                } else {
                    if (isFromDraft){
                        draft.title = getPostTitle()
                        draft.body = fragment.getBody()
                        draft.tags = emptyList()
                        saveDraft(draft)
                    } else {
                        val draft = Drafts(getPostTitle(), fragment.getBody(), emptyList())
                        saveDraft(draft)
                    }
                }

            }

            // do nothing

        }
    }

    private fun getListOfTags(): List<String> {

        var list = emptyList<String>()

        for (tag in fragment.getTags()) {

            list = listOf(tag)
        }

        return list
    }

    private fun postIt(body: String, title: String, tags: Array<String>){
        RxSteemJManager.createPost(title, body, tags, object : SteemJCallback.CreatePostCallBack {
            override fun onSuccess(permLink: String) {
                postedSuccessfully(permLink)
            }

            override fun onError(e: Throwable) {
                posted = false
                Toast.makeText(this@CreatePostActivity,
                        "Unable to post, Saving to drafts",
                        Toast.LENGTH_SHORT)
                        .show()

                if (isFromDraft){
                    draft.title = title
                    draft.body = body
                    draft.tags = tags.toList()
                    saveDraft(draft)
                } else {
                    val draft = Drafts(title, body, getListOfTags())
                    saveDraft(draft)
                }

                finish()
            }

        })
    }

    private fun saveDraft(draft: Drafts) {

        if (isFromDraft) {
            steemitRepository?.localRepository?.updateDraft(draft)

        } else {
            steemitRepository?.localRepository?.saveDraft(draft)
        }

        saved = true
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
        posted = true
        val intent = Intent().putExtra(myNewPermLink, permLink)
        setResult(POSTED_SUCCESSFULLY, intent)
        finish()
    }

    private fun getPostTitle(): String{
        return createPostTitle.text.toString()
    }

    private fun setPostTitle(title: String) {
        createPostTitle.setText(title)
    }

    private fun displaySnackBar(msg: Int){
        Snackbar.make(postingMainLayout, msg,
                Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        if (User.userIsLoggedIn) {
            RxSteemJManager.deregisterSteemJUser(CREATE_POST_STEEMJ_USER)
        }
        if (!posted && !saved) {
            checkAndSave()
            Toast.makeText(this@CreatePostActivity,
                    "Saving to drafts",
                    Toast.LENGTH_SHORT)
                    .show()
        }

        super.onDestroy()
    }
}