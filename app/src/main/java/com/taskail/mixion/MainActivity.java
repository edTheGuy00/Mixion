package com.taskail.mixion;

import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.animation.AccelerateInterpolator;

import com.taskail.mixion.adapters.PagerAdapter;
import com.taskail.mixion.fragments.ChatsFragment;
import com.taskail.mixion.fragments.FeedFragment;
import com.taskail.mixion.fragments.AskSteemFragment;
import com.taskail.mixion.fragments.ProfileFragment;
import com.taskail.mixion.utils.BottomNavigationViewHelper;
import com.taskail.mixion.utils.BottomNavigationViewVisibility;
import com.taskail.mixion.utils.FragmentLifecycle;
import com.taskail.mixion.utils.LockableViewPager;

/**
 * The main entry point for Mixion
 */
public class MainActivity extends AppCompatActivity implements BottomNavigationViewVisibility {
    private static final String TAG = "MainActivity";

    private LockableViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private BottomNavigationView bottomNavigationView;

    private final int FEED_FRAGMENT = 0;
    private final int ASK_STEEM_FRAGMENT = 1;
    private final int CHAT_FRAGMENT = 2;
    private final int PROFILE_FRAGMENT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        viewPager = findViewById(R.id.container);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());

        setViewPager();
        startBottomNavView();

    }

    private void setViewPager(){

        pagerAdapter.addFragment(new FeedFragment());
        pagerAdapter.addFragment(new AskSteemFragment());
        pagerAdapter.addFragment(new ChatsFragment());
        pagerAdapter.addFragment(new ProfileFragment());

        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(FEED_FRAGMENT);
        viewPager.setSwipeable(false);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            int currentPosition = FEED_FRAGMENT;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int newPosition) {
                handleFragmentLifeCycle(currentPosition, newPosition);
                currentPosition = newPosition;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * Implements a manula lifecycle for each fragment.
     * @param currentPosition begins with MainActivity#FEED_FRAGMENT
     * @param newPosition updated position as the user navigates the fragments
     */
    private void handleFragmentLifeCycle(int currentPosition, int newPosition){

        FragmentLifecycle fragmentToHide = (FragmentLifecycle) pagerAdapter.getItem(currentPosition);
        fragmentToHide.onPauseFragment();

        FragmentLifecycle fragmentToShow = (FragmentLifecycle) pagerAdapter.getItem(newPosition);
        fragmentToShow.onResumeFragment();

    }

    private void startBottomNavView(){
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener((MenuItem item) -> {
            item.setChecked(true);
            switch (item.getItemId()) {
                case R.id.feed_icon:
                    viewPager.setCurrentItem(FEED_FRAGMENT, true);
                    break;
                case R.id.asksteem_icon:
                    viewPager.setCurrentItem(ASK_STEEM_FRAGMENT, true);
                    break;
                case R.id.chat_icon:
                    viewPager.setCurrentItem(CHAT_FRAGMENT);
                    break;
                case R.id.profile_icon:
                    viewPager.setCurrentItem(PROFILE_FRAGMENT, true);
                    break;
            }
            return false;
        });

    }

    /**
     * Hide and show the bottom navigation view
     */

    @Override
    public void hideBNV() {
        bottomNavigationView.animate().translationY(bottomNavigationView.getHeight())
                .setInterpolator(new AccelerateInterpolator(2)).start();
    }
    @Override
    public void showBNV() {
        bottomNavigationView.animate().translationY(0).setInterpolator(new AccelerateInterpolator(2));
    }
}
