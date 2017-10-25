package com.taskail.mixion.steemitchat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.rocketchat.common.RocketChatException;
import com.rocketchat.common.listener.ConnectListener;
import com.rocketchat.common.network.Socket;
import com.rocketchat.core.RocketChatClient;
import com.rocketchat.core.callback.LoginCallback;
import com.rocketchat.core.model.Token;
import com.taskail.mixion.R;

/**Created by ed on 10/22/17.
 */

public class ChatLoginDialog extends DialogFragment implements ConnectListener {
    private static final String TAG = "ChatLoginDialog";

    private RocketChatClient rocketChatClient;
    private AppCompatEditText password, userName;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_chat_login, container, false);
        rocketChatClient = ((RocketChatApplication) getActivity().getApplicationContext()).getRocketChatAPI();
        password = view.findViewById(R.id.passwrd);
        userName = view.findViewById(R.id.user_name);
        sharedPreferences = getActivity().getSharedPreferences("mixion-app", Context.MODE_PRIVATE);
        rocketChatClient.setReconnectionStrategy(null);
        rocketChatClient.connect(this);

        Button loginBtn = view.findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener((View v) -> {

            if (!isConnected()){
                reconnect();
            } else {
                loginBtnClicked();

            }

        });
        return view;
    }

    private void loginBtnClicked(){
        if (!TextUtils.isEmpty(password.getText()) && !TextUtils.isEmpty(userName.getText())){
            String user = userName.getText().toString();
            String pswrd = password.getText().toString();
            rocketChatClient.login(user, pswrd, new LoginCallback() {
                @Override
                public void onLoginSuccess(Token token) {
                    Log.d(TAG, "onLoginSuccess: ");
                    handleLoginSuccess(user, pswrd, token);

                }
                @Override
                public void onError(RocketChatException error) {
                    handleLoginError(error);
                }
            });
        } else {
            Log.d(TAG, "loginBtnClicked: ");
            Toast.makeText(getActivity(), "Please Enter Your User Name and Password", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean isConnected(){
        return rocketChatClient.getWebsocketImpl().getSocket().getState() == Socket.State.CONNECTED;
    }

    private void reconnect(){
        Toast.makeText(getActivity(), "Connection Error, Reconnecting...", Toast.LENGTH_SHORT).show();
        rocketChatClient.getWebsocketImpl().getSocket().reconnect();
    }

    @UiThread
    private void handleLoginSuccess(String user, String password, Token token){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //editor.putString("chat-username", user);
        //editor.putString("chat-password", password);
        editor.putString("rocket-chat-token", token.toString());
        editor.apply();

    }

    @UiThread
    private void handleLoginError(RocketChatException error){
        Log.d(TAG, "onError: Error " + error.getMessage());
    }

    @Override
    public void onConnect(String sessionID) {

        Log.d(TAG, "onConnect: connected " + sessionID);

    }

    @Override
    public void onDisconnect(boolean closedByServer) {

    }

    @Override
    public void onConnectError(Throwable websocketException) {

        Log.d(TAG, "onConnectError: error " + websocketException.getMessage());
    }

    @Override
    public void onDestroy() {
        rocketChatClient.getWebsocketImpl().getConnectivityManager().unRegister(this);
        super.onDestroy();
    }
}
