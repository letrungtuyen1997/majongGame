package com.ss.gameLogic.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.ss.commons.TextureAtlasC;
import com.ss.core.exSprite.particle.GParticleSprite;
import com.ss.core.exSprite.particle.GParticleSystem;
import com.ss.core.util.GClipGroup;
import com.ss.core.util.GLayerGroup;
import com.ss.core.util.GUI;

public class Timer extends GLayerGroup {
  private Image                     frmTime,TimeBar;
  private GClipGroup                clipGroup               = new GClipGroup();
  private TemporalAction            action;
  private Runnable                  onComplete;
  private int                       duration,resDura;
  private Group                     Greff;
  private GParticleSprite           ef;
  private Array<Image>              arrStar                 = new Array<>();

  public Timer(float x, float y, int duration){
    this.duration       = duration;
    this.resDura        = duration;
    this.setPosition(x,y,Align.center);
    frmTime = GUI.createImage(TextureAtlasC.uiAtlas,"barframe");
    frmTime.setOrigin(Align.center);
    frmTime.setPosition(0,0,Align.center);
    addActor(frmTime);
    TimeBar = GUI.createImage(TextureAtlasC.uiAtlas,"bar");
    TimeBar.setPosition(frmTime.getX(Align.center),frmTime.getY(Align.center));
    TimeBar.setOrigin(Align.center);
    this.clipGroup.addActor(this.TimeBar);
    clipGroup.setPosition(frmTime.getX(Align.center)-TimeBar.getWidth()/2,frmTime.getY());
    clipGroup.debug();
    this.addActor(clipGroup);
    for (int i=0;i<3;i++){
      Image star = GUI.createImage(TextureAtlasC.uiAtlas,"star");
      if(i==0)
        star.setPosition(frmTime.getX()+frmTime.getWidth()*0.2f,frmTime.getY(Align.center),Align.center);
      if(i==1)
        star.setPosition(frmTime.getX()+frmTime.getWidth()*0.4f,frmTime.getY(Align.center),Align.center);
      if (i==2)
        star.setPosition(frmTime.getX()+frmTime.getWidth()*0.6f,frmTime.getY(Align.center),Align.center);
      this.addActor(star);
      arrStar.add(star);
    }



  }
  public void start(Runnable callback){
    if(ef != null) {
      ef.free();
      this.clearActions();
    }
//    System.out.println("let go!");
    this.onComplete     = callback;
    Effect();
    ActionScaleTime();
  }

  public void setTime(int time){
    this.duration =time;
    this.resDura  =this.duration;
  }
  public void ActionScaleTime(){
    this.action = new TemporalAction(duration, Interpolation.linear) {
      /* access modifiers changed from: protected */
      public void update(float f) {
        Timer.this.clipGroup.setClipArea( -(Timer.this.TimeBar.getWidth() * f),0, Timer.this.TimeBar.getWidth(), Timer.this.TimeBar.getHeight());
        ef.setPosition(clipGroup.getX()+(TimeBar.getWidth()*0.99f)-TimeBar.getWidth()*f,frmTime.getY()+frmTime.getHeight()*0.5f);
        resDura =  duration-(int)(duration*f);
        checkStar(ef.getX());
//        System.out.println("resTime: "+((int)(duration*f)));
        if (f == 1.0f) {
          Timer.this.onComplete.run();
          ef.free();
        }
      }
    };
    addAction(this.action);
  }
  private void checkStar(float x){
    for (Image img : arrStar){
      if(x<img.getX() && img.getColor().equals(Color.DARK_GRAY)==false){
        img.setColor(Color.DARK_GRAY);
      }
    }
  }
  public void resetTime(){
    ef.free();
    this.clearActions();
    Effect();
    ActionScaleTime();
  }

  public int getResTime(){
    return resDura;
  }

  public String getResTimeStr(){
    int minute = resDura/60;
    int second = resDura%60;
    return minute+":"+second;
  }
  public void setPause(Boolean set){
    this.setPause(set);
  }

  private void Effect(){
    ef = GParticleSystem.getGParticleSystem("timeFire").create(this, frmTime.getX()+TimeBar.getWidth(),frmTime.getY()+TimeBar.getHeight()/2);
    ef.setScale(2.5f,2.3f);
  }
  public void setActionTime(float res){
    this.action.setTime(res);
  }
  public int getStar(){
    int count=0;
    for (Image img : arrStar){
      if(img.getColor().equals(Color.DARK_GRAY)==false)
        count++;
    }
    return count;
  }
  public void setPosition(float x, float y){
    this.setPosition(x,y,Align.center);

  }
}
