package com.taskail.mixion.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.rocketchat.core.RocketChatClient;
import com.taskail.mixion.R;
import com.taskail.mixion.steemitchat.ChatLoginDialog;
import com.taskail.mixion.steemitchat.RocketChatApplication;
import com.taskail.mixion.steemitchat.RocketChatHelper;
import com.taskail.mixion.utils.FragmentLifecycle;

/**Created by ed on 9/30/17.
 */

public class ChatsFragment extends Fragment implements FragmentLifecycle {
    private static final String TAG = "ChatsFragment";

    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPref;
    private RocketChatClient rocketChatClient;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chats, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPref = getActivity().getSharedPreferences("mixion-app", Context.MODE_PRIVATE);
        rocketChatClient = ((RocketChatApplication)getActivity().getApplicationContext()).getRocketChatAPI();

        //checkCurrentUser(view);
    }

    private void checkCurrentUser(View v){
        //String username = sharedPref.getString("chat-username", null);
        //String password = sharedPref.getString("chat-password", null);
        String token = sharedPref.getString("rocket-chat-token", null);
        if (token != null){
            autoLogin(token);
        } else {
            initLoginDialog(v);
        }
    }

    private void initLoginDialog(View v){
        Button loginBtn = v.findViewById(R.id.loginBtn);
        loginBtn.setVisibility(View.VISIBLE);
        loginBtn.setOnClickListener((View view) -> {
            ChatLoginDialog loginDialog = new ChatLoginDialog();
            loginDialog.show(getActivity().getSupportFragmentManager(), "login");
        });
    }

    private void autoLogin(String token){

        Log.d(TAG, "autoLogin: " + token);
    }

    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment() {

    }
}
