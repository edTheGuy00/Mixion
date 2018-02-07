package com.taskail.mixion.main

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.zsmb.materialdrawerkt.builders.drawer
import co.zsmb.materialdrawerkt.draweritems.badgeable.primaryItem
import co.zsmb.materialdrawerkt.draweritems.badgeable.secondaryItem
import co.zsmb.materialdrawerkt.draweritems.sectionHeader
import com.mikepenz.fontawesome_typeface_library.FontAwesome
import com.mikepenz.materialdrawer.Drawer
import com.taskail.mixion.ACTIVITY_REQUEST_LOGIN
import com.taskail.mixion.BackPressedHandler
import com.taskail.mixion.MixionApplication
import com.taskail.mixion.R
import com.taskail.mixion.data.source.remote.AskSteemRepository
import com.taskail.mixion.data.SteemitRepository
import com.taskail.mixion.data.models.SteemDiscussion
import com.taskail.mixion.data.source.local.LocalDataSource
import com.taskail.mixion.data.source.local.MixionDatabase
import com.taskail.mixion.data.source.remote.*
import com.taskail.mixion.dialog.TagDialog
import com.taskail.mixion.feed.FeedFragment
import com.taskail.mixion.feed.FeedPresenter
import com.taskail.mixion.login.LoginActivity
import com.taskail.mixion.profile.ProfileFragment
import com.taskail.mixion.profile.User
import com.taskail.mixion.search.SearchFragment
import com.taskail.mixion.search.SearchPresenter
import com.taskail.mixion.steemJ.SteemJAPI
import com.taskail.mixion.steemdiscussion.loadDiscussionIntent
import com.taskail.mixion.steemdiscussion.openDiscussionIntent
import com.taskail.mixion.utils.getCallback
import com.taskail.mixion.utils.hideBottomNavigationView
import com.taskail.mixion.utils.showBottomNavigationView
import cz.koto.keystorecompat.base.utility.runSinceLollipop
import eu.bittrade.libs.steemj.base.models.AccountName
import eu.bittrade.libs.steemj.configuration.SteemJConfig
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_main.*

/**
 *Created by ed on 1/19/18.
 */

var steemitRepository: SteemitRepository? = null
var steemJAPI: SteemJAPI? = null

class MainFragment : Fragment(),
        BackPressedHandler,
        FeedFragment.Callback,
        SearchFragment.Callback {

    val TAG = "MainFragment"

    private val keystoreCompat by lazy { (activity?.application as MixionApplication).keyStoreCompat }

    interface Callback{
        fun onSearchOpen()
        fun onSearchClosed()
        fun getDatabase(): MixionDatabase?
        fun getMainToolbar(): Toolbar
        fun setToolbarTitle(title: String)
    }

    override fun onAccountRequested() {
        if (User.userIsLoggedIn){
            // start user profile
        } else {
            startActivityForResult(LoginActivity.newIntent(context!!), ACTIVITY_REQUEST_LOGIN)
        }
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
    private var remoteDisposable = CompositeDisposable()
    private var localDisposable = CompositeDisposable()
    private var steemJDisposable = CompositeDisposable()
    private var steemitAPI: SteemAPI? = null
    private var askSteemApi: AskSteemApi? = null
    private var askSteemDisposable: CompositeDisposable? = null
    private val FEED_FRAGMENT = 0
    private lateinit var feedPresenter: FeedPresenter

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (keystoreCompat.hasSecretLoadable()){
            getCredentials()
            User.userIsLoggedIn = true
        }

        val feedFragment = FeedFragment.getInstance().apply {
            feedPresenter = FeedPresenter(this, getRepository())
        }
        switchToFragment(feedFragment)
    }

    private fun switchToFragment(fragment: Fragment, addToBackStack: Boolean = false){
        val fm = this.childFragmentManager
        val currentFragment = fm.findFragmentById(R.id.container)
        val transaction = fm.beginTransaction()
        transaction.replace(R.id.container, fragment)
        if (currentFragment != null && addToBackStack)
            transaction.addToBackStack(fragment.javaClass.name)
        transaction.commitAllowingStateLoss()
    }

    private fun getRepository(): SteemitRepository{
        return steemitRepository ?: SteemitRepository.getInstance(createRemoteRepo(), createLocalRepo()).apply {
            steemitRepository = this
        }
    }

    private fun createRemoteRepo() : RemoteDataSource {
        return RemoteDataSource.getInstance(remoteDisposable, createSteemApi())
    }

    private fun createLocalRepo(): LocalDataSource {
        return LocalDataSource.getInstance(getCallback()?.getDatabase()?.tagsDao()!!, localDisposable)
    }

    private fun createSteemApi() : SteemAPI {
        return steemitAPI ?: getRetrofitClient(baseUrl).create(SteemAPI::class.java).apply {
            steemitAPI = this
        }
    }

    private fun getAskSteemRepo() : AskSteemRepository {
        return AskSteemRepository.getInstance(getAskSteemDisposable(), createAskSteemApi())
    }

    private fun getAskSteemDisposable(): CompositeDisposable{
        return askSteemDisposable ?: CompositeDisposable()
    }

    private fun createAskSteemApi() : AskSteemApi {
        return askSteemApi ?: getRetrofitClient(askSteemUrl).create(AskSteemApi::class.java).apply {
            askSteemApi = this
        }
    }

    override fun onTagDialogRequested() {
        TagDialog(context, getRepository(), {
            feedPresenter.getByTag(it)
        }).show()
    }

    private fun searchFragment(): SearchFragment? {
        return childFragmentManager.findFragmentById(R.id.fragment_main_container) as SearchFragment?
    }

    override fun onSearchRequested() {
        var fragment: Fragment? = searchFragment()
        if (fragment == null) {
            fragment = SearchFragment.newInstance().apply {
                SearchPresenter(this, getAskSteemRepo())
            }
            childFragmentManager
                    .beginTransaction()
                    .add(R.id.fragment_main_container, fragment)
                    .commitNow()
        }

        getCallback()?.onSearchOpen()

    }

    override fun onSearchClosed() {
        childFragmentManager.beginTransaction().remove(searchFragment()).commitNowAllowingStateLoss()
        getCallback()?.onSearchClosed()
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
        remoteDisposable.dispose()
        localDisposable.dispose()
        User.performLogout()
        super.onDestroy()
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

    private fun getCredentials(){
        runSinceLollipop {
            keystoreCompat.loadSecretAsString({ decryptResults ->
                decryptResults.split(';').let {
                    User.storeUser(it[0], it[1])
                }
            }, {
                Log.d("Error", it.message)
            }, User.forceLockScreenFlag)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode){
            ACTIVITY_REQUEST_LOGIN -> {
                if (resultCode == LoginActivity.RESUlT_LOGIN_OK){
                    updateUiForLoggedInUser()
                }
            } else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun updateUiForLoggedInUser(){
        feedPresenter.getMyFeed()
        feedPresenter.userStatus(loggedIn = true)
    }

    override fun onBackPressed(): Boolean {
        val searchFragment = searchFragment()
        if (searchFragment != null && searchFragment.onBackPressed()) {
            return true
        }

        return false
    }
}
