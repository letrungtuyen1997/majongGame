package com.ss.commons;

import com.ss.core.exSprite.particle.GParticleSystem;

public class LoadParticle {

  public static void init(){
    new GParticleSystem("timeFire","effect.atlas",1,1);
  }
}


