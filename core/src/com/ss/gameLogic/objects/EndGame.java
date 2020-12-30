package com.ss.gameLogic.objects;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.ss.commons.BitmapFontC;
import com.ss.commons.ButtonC;
import com.ss.commons.TextureAtlasC;
import com.ss.core.exSprite.GShapeSprite;
import com.ss.core.util.GLayer;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;

public class EndGame {
  private Group    group       = new Group();
  private String   ribon,title;
  public EndGame(boolean isWin){
    if(isWin){
      ribon = "ribonWin";
      title = "victory";
    }else {
      ribon="ribonFail";
      title = "fail";
    }
    GStage.addToLayer(GLayer.top,group);
    group.setPosition(GStage.getWorldWidth()/2,GStage.getWorldHeight()/2);
    GShapeSprite Gshape = new GShapeSprite();
    Gshape.createRectangle(true,-GStage.getWorldWidth()/2,-GStage.getWorldHeight()/2,GStage.getWorldWidth(),GStage.getWorldHeight());
    Gshape.setColor(0,0,0,0.6f);
    group.addActor(Gshape);
    Gshape.addListener(new ClickListener(){
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        group.clear();
        group.remove();
        return super.touchDown(event, x, y, pointer, button);
      }
    });
    group.setScale(0);
    group.addAction(Actions.scaleTo(1,1,0.5f, Interpolation.swingOut));

    Image popup = GUI.createImage(TextureAtlasC.uiAtlas,"popup");
    popup.setOrigin(Align.center);
    popup.setPosition(0,0, Align.center);
    group.addActor(popup);

    Image ribbon = GUI.createImage(TextureAtlasC.uiAtlas,ribon);
//    ribbon.setSize(ribbon.getWidth()*0.8f,ribbon.getHeight()*0.8f);
    ribbon.setOrigin(Align.center);
    ribbon.setPosition(0,-popup.getHeight()/2+ribbon.getHeight()*0.15f,Align.center);
    group.addActor(ribbon);

    Image Title = GUI.createImage(TextureAtlasC.uiAtlas,title);
    Title.setPosition(ribbon.getX(Align.center),ribbon.getY(Align.center)-ribbon.getHeight()*0.2f,Align.center);
    group.addActor(Title);

    if(isWin){
      ButtonC btnNextLv = new ButtonC(TextureAtlasC.uiAtlas,group,"btnGreen");
      btnNextLv.setScale(0.7f,0.7f);
      btnNextLv.setPosition(-btnNextLv.getSize().x/2,popup.getHeight()/2-btnNextLv.getSize().y/2,Align.center);
      btnNextLv.addBitmapFont(BitmapFontC.font_white);
      btnNextLv.addText("nextLv",0.7f);

      ButtonC btnWatch = new ButtonC(TextureAtlasC.uiAtlas,group,"btnWatchAds");
      btnWatch.setScale(0.7f,0.7f);
      btnWatch.setPosition(btnWatch.getSize().x/2,popup.getHeight()/2-btnWatch.getSize().y/2,Align.center);
      btnWatch.addBitmapFont(BitmapFontC.font_white);
      btnWatch.addText("open gift",0.4f);
    }

  }
}
