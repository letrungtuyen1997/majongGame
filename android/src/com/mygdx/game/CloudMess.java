package com.mygdx.game;

import com.google.firebase.messaging.RemoteMessage;

import de.golfgl.gdxpushmessages.FcmMessageHandler;

public class CloudMess extends FcmMessageHandler {
  public void onMessageReceived(RemoteMessage remoteMessage) {
    callPushMessageListenerOnMessageArrived("");
  }

}
