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

public class MainActivity extends AppCompatActivity implements BottomNavigationViewVisibility {
    private static final String TAG = "MainActivity";

    private LockableViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private BottomNavigationView bottomNavigationView;

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
        viewPager.setCurrentItem(0);
        viewPager.setSwipeable(false);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            int currentPosition = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int newPosition) {

                FragmentLifecycle fragmentToHide = (FragmentLifecycle) pagerAdapter.getItem(currentPosition);
                fragmentToHide.onPauseFragment();

                FragmentLifecycle fragmentToShow = (FragmentLifecycle) pagerAdapter.getItem(newPosition);
                fragmentToShow.onResumeFragment();

                currentPosition = newPosition;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void startBottomNavView(){
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener((MenuItem item) -> {
            item.setChecked(true);
            switch (item.getItemId()) {
                case R.id.feed_icon:
                    viewPager.setCurrentItem(0, true);
                    break;
                case R.id.asksteem_icon:
                    viewPager.setCurrentItem(1, true);
                    break;
                case R.id.chat_icon:
                    viewPager.setCurrentItem(2);
                    break;
                case R.id.profile_icon:
                    viewPager.setCurrentItem(3, true);
                    break;
            }

            return false;
        });

    }

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
