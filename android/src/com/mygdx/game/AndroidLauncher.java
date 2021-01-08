package com.mygdx.game;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.ss.GMain;

//import com.crashlytics.android.Crashlytics;
//import com.google.firebase.iid.FirebaseInstanceId;
//import com.google.firebase.iid.InstanceIdResult;

public class AndroidLauncher extends AndroidApplication {

  JokerSdk sdk;
  @Override
  protected void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
    sdk = new JokerSdk(this);
    View libgdxview = initializeForView(new GMain(sdk));
    sdk.OnCreate(libgdxview);


  }

  @Override
  protected void onPause() {
    sdk.onPause();
    super.onPause();
  }

  @Override
  protected void onResume() {
    super.onResume();
    sdk.onResume();
  }

  @Override
  protected void onDestroy() {
    sdk.onDestroy();
    super.onDestroy();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    sdk.onActivityResult(requestCode, resultCode, data);
  }

}

