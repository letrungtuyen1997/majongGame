package com.ss.gameLogic.objects;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.ss.commons.BitmapFontC;
import com.ss.commons.TextureAtlasC;
import com.ss.core.util.GClipGroup;
import com.ss.core.util.GLayer;
import com.ss.core.util.GLayerGroup;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.effects.SoundEffect;

public class Combo {
  private GLayerGroup group                   = new GLayerGroup();
  private Image           TimeBar;
  private GClipGroup clipGroup               = new GClipGroup();
  private TemporalAction  action;
  private Runnable        onComplete;
  private int             duration,count=1;
  private Label           labelCombo;
  private Group           grLabel;

  public Combo(float x, float y, int duration){
    GStage.addToLayer(GLayer.top,group);
    this.duration       = duration;
    group.setPosition(x,y, Align.center);
    TimeBar = GUI.createImage(TextureAtlasC.uiAtlas,"comboLine");
//    TimeBar.setSize(TimeBar.getWidth()*1.2f,TimeBar.getHeight()*1.2f);
    TimeBar.setOrigin(Align.center);
    this.clipGroup.addActor(this.TimeBar);
    clipGroup.setPosition(0,0);
    group.addActor(clipGroup);
    group.setVisible(false);
    //// label combo /////
    textCombo();

  }
  private void textCombo(){
    grLabel = new Group();
    labelCombo = new Label("",new Label.LabelStyle(BitmapFontC.font_white,null));
    labelCombo.setPosition(0,0,Align.center);
    labelCombo.setFontScale(0.7f);
    labelCombo.setAlignment(Align.center);
    grLabel.addActor(labelCombo);
    grLabel.setPosition(TimeBar.getX()+TimeBar.getWidth()/2,TimeBar.getY()+labelCombo.getPrefHeight()*1.1f,Align.center);
    grLabel.setOrigin(Align.center);
    group.addActor(grLabel);
  }
  private void actionUpCombo(int combo){

    grLabel.setScale(0);
    labelCombo.setText("combo X"+combo);
    grLabel.addAction(Actions.sequence(
            Actions.scaleTo(1.2f,1.2f,0.2f),
            Actions.scaleTo(0.9f,0.9f,0.2f),
            Actions.scaleTo(1f,1f,0.2f)
    ));

    //// effect /////////
//    ClassicScene.effAni.StartEffCombo(group.getX()+TimeBar.getWidth()/2,group.getY());
  }
  public void ActionScaleTime(){
    group.setVisible(true);
    this.action = new TemporalAction(duration, Interpolation.linear) {
      /* access modifiers changed from: protected */
      public void update(float f) {
        Combo.this.clipGroup.setClipArea( -(Combo.this.TimeBar.getWidth() * f),0, Combo.this.TimeBar.getWidth(), Combo.this.TimeBar.getHeight());

        if (f == 1.0f) {
//          BoardConfig.countcombo=0;
          group.setVisible(false);
        }
      }
    };
    group.addAction(this.action);
  }
  public void resetTime(){
    group.clearActions();
    ActionScaleTime();
  }
  public void upTime(int combo,int id){
    System.out.println("combo: " + combo);
//    if(combo>1)
//      SoundEffect.explode(combo-1);
//      if(id==2||id==6||id==8||id==18||id==23)
//        SoundEffect.noel();
//    else{
//      if(id==2||id==6||id==8||id==18||id==23)
//        SoundEffect.noel();
//      else
//        SoundEffect.Play(SoundEffect.match);
//    }
    if(combo==1){
      resetTime();
    }else if(combo>1){
      //label bug hrere null
      this.action.restart();
    }
    actionUpCombo(combo);

  }

  public void setPause(Boolean set){
    group.setPause(set);
  }


}
