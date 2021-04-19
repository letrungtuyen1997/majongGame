package com.ss.scenes;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ss.GMain;
import com.ss.commons.TextureAtlasC;
import com.ss.commons.Tweens;
import com.ss.core.util.GLayer;
import com.ss.core.util.GScreen;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.gameLogic.config.Config;
import com.ss.gameLogic.objects.Board;
import com.ss.gameLogic.objects.Effect;
import com.ss.gameLogic.objects.EndGame;
import com.ss.gameLogic.objects.SelectChapter;
import com.ss.gameLogic.objects.SelectLevel;


public class GameScene extends GScreen {

  private        Group                       group              = new Group();
  private        Board                       board;


  @Override
  public void dispose() {

  }

  @Override
  public void init() {
    GStage.addToLayer(GLayer.top,GMain.getGrEff());
    GMain.platform.ShowBanner(false);
    GStage.addToLayer(GLayer.map,group);
    Image bg = GUI.createImage(TextureAtlasC.uiAtlas,"bg");
    bg.setSize(GStage.getWorldWidth(),GStage.getWorldHeight());
    group.addActor(bg);
    if(Config.isContinues){
      int chapter = Config.LvPer/100;
      if(chapter==5)
        chapter=4;
      new SelectLevel(group,this, Config.LvPer,Config.isContinues,chapter);
      bg.clear();
      bg.remove();
    }
    else{
      new SelectChapter(group,this);
      bg.clear();
      bg.remove();
    }

  }

  @Override
  public void run() {

  }
}
