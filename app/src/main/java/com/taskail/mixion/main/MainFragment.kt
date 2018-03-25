package com.taskail.mixion.main

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.taskail.mixion.*
import com.taskail.mixion.data.SteemitRepository
import com.taskail.mixion.data.models.SteemDiscussion
import com.taskail.mixion.data.source.local.MixionDatabase
import com.taskail.mixion.ui.dialog.TagDialog
import com.taskail.mixion.feed.FeedFragment
import com.taskail.mixion.feed.FeedPresenter
import com.taskail.mixion.login.LoginActivity
import com.taskail.mixion.post.CreatePostActivity
import com.taskail.mixion.User
import com.taskail.mixion.data.source.local.Drafts
import com.taskail.mixion.fragment.BaseFragment
import com.taskail.mixion.search.SearchFragment
import com.taskail.mixion.search.SearchPresenter
import com.taskail.mixion.fragment.DonateFragment
import com.taskail.mixion.fragment.DraftsFragment
import com.taskail.mixion.profile.ProfileActivity
import com.taskail.mixion.steemdiscussion.loadDiscussionIntent
import com.taskail.mixion.steemdiscussion.openDiscussionIntent
import com.taskail.mixion.utils.getCallback
import cz.koto.keystorecompat.base.utility.runSinceLollipop
import io.reactivex.disposables.CompositeDisposable

/**
 *Created by ed on 1/19/18.
 */

var steemitRepository: SteemitRepository? = null

class MainFragment : Fragment(),
        BackPressedHandler,
        FeedFragment.Callback,
        BaseFragment.Callback{

    val TAG = "MainFragment"

    private val keystoreCompat by lazy { (activity?.application as MixionApplication).keyStoreCompat }

    interface Callback{
        fun onChildFragmentOpen()
        fun onChildFragmentClosed()
        fun getDatabase(): MixionDatabase?
        fun getMainToolbar(): Toolbar
        fun setToolbarTitle(title: String)
    }

    override fun onAccountRequested() {
        if (User.userIsLoggedIn){
            startActivity(ProfileActivity.myProfile(context!!))
        } else {
            startActivityForResult(LoginActivity.newIntent(context!!), ACTIVITY_REQUEST_LOGIN_TO_PROFILE)
        }
    }

    override fun requestToAddNewPost() {
        if (User.userIsLoggedIn){
            startActivityForResult(CreatePostActivity.newIntent(context!!), ACTIVITY_REQUEST_CREATE_NEW_POST)
        } else {
            startActivityForResult(LoginActivity.newIntent(context!!), ACTIVITY_REQUEST_LOGIN_TO_POST)
        }
    }

    override fun onDraftOpenRequested(draft: Drafts) {
        startActivity(CreatePostActivity.openDraft(context!!, draft))
    }

    override fun onDonateRequested() {
        openFragment(DonateFragment.newInstance())
    }

    override fun onDraftsRequested() {
        openFragment(DraftsFragment.newInstance())
    }

    override fun getDrawerToolbar(): Toolbar? {
        return getCallback()?.getMainToolbar()
    }

    override fun setToolbarTitle(title: String) {
        getCallback()?.setToolbarTitle(title)
    }

    override fun getDrawerContainer(): Int {
        return R.id.drawerContainer
    }
    private lateinit var remoteDisposable: CompositeDisposable
    private lateinit var localDisposable: CompositeDisposable
    private lateinit var steemJDisposable: CompositeDisposable
    private lateinit var askSteemDisposable: CompositeDisposable
    private lateinit var feedPresenter: FeedPresenter

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        createDisposables()

        return view
    }

    private fun createDisposables(){
        remoteDisposable = CompositeDisposable()
        localDisposable = CompositeDisposable()
        steemJDisposable = CompositeDisposable()
        askSteemDisposable = CompositeDisposable()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        if (keystoreCompat.hasSecretLoadable()){
//            getCredentials()
//            User.userIsLoggedIn = true
//        }

        val feedFragment = FeedFragment.getInstance().apply {
            feedPresenter = FeedPresenter(this, getRepository())
        }
        switchToFragment(feedFragment)
    }

    private fun switchToFragment(fragment: Fragment, addToBackStack: Boolean = false){
        val fm = this.childFragmentManager
        val currentFragment = fm.findFragmentById(R.id.container)
        val transaction = fm.beginTransaction()
        transaction.replace(R.id.childContainer, fragment)
        if (currentFragment != null && addToBackStack)
            transaction.addToBackStack(fragment.javaClass.name)
        transaction.commitAllowingStateLoss()
    }

    private fun getRepository(): SteemitRepository{
        return steemitRepository ?: getMixionRepository(
                remoteDisposable,
                localDisposable,
                getCallback()?.getDatabase()?.draftsDao()!!,
                getCallback()?.getDatabase()?.tagsDao()!!
        ).apply {
            steemitRepository = this
        }
    }

    override fun onTagDialogRequested() {
        TagDialog(context, getRepository(), {
            feedPresenter.getByTag(it)
        }).show()
    }

    private fun childFragment(): BaseFragment? {
        return childFragmentManager.findFragmentById(R.id.fragment_main_container) as BaseFragment?
    }

    private fun openFragment(fragToOpen: Fragment) {
        var fragment: Fragment? = childFragment()
        if (fragment == null){
            fragment = fragToOpen

            childFragmentManager
                    .beginTransaction()
                    .add(R.id.fragment_main_container, fragment)
                    .commitNow()

            getCallback()?.onChildFragmentOpen()
        }

    }

    override fun onSearchRequested() {

        openFragment(SearchFragment.newInstance().apply {
            SearchPresenter(this, getAskSteemRepo(askSteemDisposable))
        })
    }

    override fun onFragmentClosed() {
        childFragmentManager.beginTransaction().remove(childFragment()).commitNowAllowingStateLoss()
        getCallback()?.onChildFragmentClosed()
    }

    override fun onSearchResultSelected(author: String, permlink: String) {
        startActivity(loadDiscussionIntent(context!!, author, permlink))
    }

    override fun openDiscussionRequested(discussion: SteemDiscussion) {
        startActivity(openDiscussionIntent(context!!, discussion))
    }

    companion object {
        @JvmStatic fun newInstance(): MainFragment{
            val fragment = MainFragment()
            fragment.retainInstance = true
            return fragment
        }
    }

    override fun onDestroy() {
        //User.performLogout()
        clearDisposables()
        super.onDestroy()
    }

    private fun clearDisposables(){
        askSteemDisposable.clear()
        remoteDisposable.clear()
        localDisposable.clear()
        steemJDisposable.clear()
    }

    private fun getCallback(): Callback? {
        return getCallback(this, Callback::class.java)
    }

    override fun logoutUser() {
        User.performLogout()
        runSinceLollipop {
            keystoreCompat.clearCredentials()
            User.performLogout()
        }
    }

//    private fun getCredentials(){
//        runSinceLollipop {
//            keystoreCompat.loadSecretAsString({ decryptResults ->
//                decryptResults.split(';').let {
//                    User.storeUser(it[0], it[1])
//                }
//            }, {
//                Log.d("Error", it.message)
//            }, User.forceLockScreenFlag)
//        }
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode){
            ACTIVITY_REQUEST_LOGIN_TO_PROFILE -> {
                if (resultCode == LoginActivity.RESUlT_LOGIN_OK){
                    updateUiForLoggedInUser()
                }
            }
            ACTIVITY_REQUEST_LOGIN_TO_POST -> {
                if (resultCode == LoginActivity.RESUlT_LOGIN_OK) {
                    startActivity(CreatePostActivity.newIntent(context!!))
                }
            }
            ACTIVITY_REQUEST_CREATE_NEW_POST -> {
                if (resultCode == CreatePostActivity.POSTED_SUCCESSFULLY){
                    val permlink = data?.getStringExtra(myNewPermLink)
                    startActivity(loadDiscussionIntent(context!!, User.getUserName()!!, permlink!!))
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun updateUiForLoggedInUser(){
        feedPresenter.getMyFeed()
        feedPresenter.userStatus(loggedIn = true)
    }

    override fun onBackPressed(): Boolean {
        val fragment = childFragment()
        if (fragment != null && fragment.onBackPressed()) {
            return true
        }

        return false
    }
}
