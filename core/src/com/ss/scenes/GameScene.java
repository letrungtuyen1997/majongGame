package com.ss.scenes;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ss.commons.TextureAtlasC;
import com.ss.core.util.GLayer;
import com.ss.core.util.GScreen;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.gameLogic.config.Config;
import com.ss.gameLogic.objects.Board;
import com.ss.gameLogic.objects.Effect;
import com.ss.gameLogic.objects.EndGame;
import com.ss.gameLogic.objects.SelectLevel;


public class GameScene extends GScreen {

  private        Group                       group              = new Group();
  private        Board                       board;
  public  static Effect                       effect;


  @Override
  public void dispose() {

  }

  @Override
  public void init() {
    effect = new Effect();
    GStage.addToLayer(GLayer.map,group);
    Image bg = GUI.createImage(TextureAtlasC.uiAtlas,"bg");
    bg.setSize(GStage.getWorldWidth(),GStage.getWorldHeight());
    group.addActor(bg);

    new SelectLevel(group,this, Config.LvPer,Config.isContinues);
    bg.addListener(new ClickListener(){
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//        new EndGame(false,3);
        System.out.println("click!!");
//        effectWin ef =new effectWin(1,GStage.getWorldWidth()/2,100,group);
//        ef.start();
//        ef.addAction(Actions.moveTo(GStage.getWorldWidth(),GStage.getWorldHeight(),2f));
        return super.touchDown(event, x, y, pointer, button);
      }
    });


  }

  @Override
  public void run() {

  }
}
