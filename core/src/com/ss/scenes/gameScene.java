package com.ss.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.ss.commons.BitmapFontC;
import com.ss.commons.TextureAtlasC;
import com.ss.core.action.exAction.GSimpleAction;
import com.ss.core.util.GLayer;
import com.ss.core.util.GScreen;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.effects.effectWin;
import com.ss.gameLogic.objects.Board;
import com.ss.gameLogic.objects.Effect;
import com.ss.gameLogic.objects.EndGame;
import com.ss.gameLogic.objects.SelectLevel;
import com.ss.gameLogic.objects.Tile;
import com.ss.gameLogic.objects.Tile;
import com.ss.utils.Utils;


public class gameScene extends GScreen {

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

    new SelectLevel(group,this);
    bg.addListener(new ClickListener(){
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        System.out.println("click!!");
        effectWin ef =new effectWin(1,GStage.getWorldWidth()/2,100,group);
        ef.start();
        ef.addAction(Actions.moveTo(GStage.getWorldWidth(),GStage.getWorldHeight(),2f));
        return super.touchDown(event, x, y, pointer, button);
      }
    });
    new EndGame(true);


//    board = new Board();
//    showfps();
//    Image btnShuffle = GUI.createImage(TextureAtlasC.uiAtlas,"btnShuffle");
//    btnShuffle.setPosition(GStage.getWorldWidth()/2+btnShuffle.getWidth(),btnShuffle.getHeight()/2, Align.center);
//    group.addActor(btnShuffle);
//    btnShuffle.addListener(new ClickListener(){
//      @Override
//      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//        board.createlbShuffle();
//        board.SkipHint();
//        board.shuffleBoard();
//        return super.touchDown(event, x, y, pointer, button);
//      }
//    });
//
//    Image btnHint = GUI.createImage(TextureAtlasC.uiAtlas,"btnHint");
//    btnHint.setPosition(GStage.getWorldWidth()/2-btnHint.getWidth(),btnHint.getHeight()/2, Align.center);
//    group.addActor(btnHint);
//    btnHint.addListener(new ClickListener(){
//      @Override
//      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//        board.hintBoard();
//        return super.touchDown(event, x, y, pointer, button);
//      }
//    });

//    createLv(11,11,11,11,11,11,11);

  }

  @Override
  public void run() {

  }
}
