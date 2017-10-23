package com.taskail.mixion.steemitchat;

import android.os.Bundle;
import android.support.annotation.Nullable;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_chat_login, container, false);
        rocketChatClient = RocketChatHelper.myRocketClient();
        password = view.findViewById(R.id.passwrd);
        userName = view.findViewById(R.id.user_name);
        Button loginBtn = view.findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener((View v) -> {
            loginBtnClicked();
        });

        rocketChatClient.setReconnectionStrategy(null);
        rocketChatClient.connect(this);
        return view;
    }

    private void loginBtnClicked(){
        if (!TextUtils.isEmpty(password.getText()) && !TextUtils.isEmpty(userName.getText())){
            rocketChatClient.login(userName.getText().toString(), password.getText().toString(), new LoginCallback() {
                @Override
                public void onLoginSuccess(Token token) {


                }

                @Override
                public void onError(RocketChatException error) {

                }
            });
        } else {
            Toast.makeText(getActivity(), "Please Enter Your User Name and Password", Toast.LENGTH_SHORT).show();
        }


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

    }

    @Override
    public void onDestroy() {
        rocketChatClient.getWebsocketImpl().getConnectivityManager().unRegister(this);
        super.onDestroy();
    }
}
