package com.ss.gameLogic.objects.setupEff;

import com.ss.effects.SoundEffect;
import com.ss.interfaces.EffectMatch;
import com.ss.scenes.LoadingScene;

public class EffectV1 implements EffectMatch {
  private int count =0;
  @Override
  public void startEff(int id, float x, float y, float scl) {
      SoundEffect.Play(SoundEffect.match);
//    LoadingScene.effect.StartEff2(x,y,scl);

  }
}
