package com.taskail.mixion.steemitchat;

import android.app.Application;

import com.rocketchat.common.network.ReconnectionStrategy;
import com.rocketchat.common.utils.Logger;
import com.rocketchat.common.utils.Utils;
import com.rocketchat.core.RocketChatClient;

/**Created by ed on 10/22/17.
 */

public class RocketChatApplication extends Application {

    RocketChatClient rocketChatClient;

    private static String serverurl = "wss://steemit.chat/websocket";
    private static String baseUrl = "https://steemit.chat/api/v1/";


    public String token;

    @Override
    public void onCreate() {
        super.onCreate();
        rocketChatClient = new RocketChatClient.Builder()
                .websocketUrl(serverurl)
                .restBaseUrl(baseUrl)
                .logger(logger)
                .build();

        Utils.DOMAIN_NAME = "https://steemit.chat";

        rocketChatClient.setReconnectionStrategy(new ReconnectionStrategy(20, 3000));

    }

    public RocketChatClient getRocketChatAPI() {
        return rocketChatClient;
    }

    Logger logger = new Logger() {
        @Override
        public void info(String format, Object... args) {
            System.out.println(format + " " +  args);
        }

        @Override
        public void warning(String format, Object... args) {
            System.out.println(format + " " +  args);
        }

        @Override
        public void debug(String format, Object... args) {
            System.out.println(format + " " +  args);
        }
    };

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
