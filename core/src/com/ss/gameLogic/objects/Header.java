package com.ss.gameLogic.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.JsonValue;
import com.ss.commons.BitmapFontC;
import com.ss.commons.TextureAtlasC;
import com.ss.core.action.exAction.GSimpleAction;
import com.ss.core.util.GLayer;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.scenes.gameScene;

public class Header {
  private Group       group   = new Group();
  private Image       frmLv;
  private Board       board;
  private int         Level;
  private gameScene   gameScene;
  private String      jvBoard[];
  public Header(Board board, int Level, gameScene gameScene, String listLv[]){
    this.board      = board;
    this.Level      = Level;
    this.gameScene  = gameScene;
    this.jvBoard    = listLv;
    GStage.addToLayer(GLayer.top,group);
    createFrmLevel(Level+1);
    createbtnSuport(board);
    createBtnPause();


  }
  private void createFrmLevel(int lv){
    frmLv = GUI.createImage(TextureAtlasC.uiAtlas,"frmLevel");
    frmLv.setPosition(frmLv.getWidth()*0.7f,frmLv.getHeight()*1.2f,Align.center);
    group.addActor(frmLv);

    Label lbLv = new Label(""+lv,new Label.LabelStyle(BitmapFontC.FontAlert,null));
    lbLv.setFontScale(0.7f);
    lbLv.setAlignment(Align.center);
    lbLv.setPosition(frmLv.getX(Align.center),frmLv.getY(Align.center)-10,Align.center);
    group.addActor(lbLv);

    Label lbLvTitle = new Label("Level",new Label.LabelStyle(BitmapFontC.FontAlert,null));
    lbLvTitle.setFontScale(0.5f);
    GlyphLayout GlLvTitle = new GlyphLayout(BitmapFontC.FontAlert,lbLvTitle.getText());
    lbLvTitle.setSize(GlLvTitle.width * lbLvTitle.getFontScaleX(),GlLvTitle.height * lbLvTitle.getFontScaleY());
    lbLvTitle.setPosition(frmLv.getX(Align.center),frmLv.getY()-lbLvTitle.getHeight(),Align.center);
    group.addActor(lbLvTitle);
  }

  private void createbtnSuport(Board board){
    Image btnShuffle = GUI.createImage(TextureAtlasC.uiAtlas,"btnShuffle");
    btnShuffle.setPosition(GStage.getWorldWidth()/2+btnShuffle.getWidth(),GStage.getWorldHeight()-btnShuffle.getHeight()/2, Align.center);
    group.addActor(btnShuffle);
    btnShuffle.addListener(new ClickListener(){
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        board.SkipHint();
        board.shuffleBoard("shuffle",()->{
          System.out.println("shuffle done");
        });
        return super.touchDown(event, x, y, pointer, button);
      }
    });

    Image btnHint = GUI.createImage(TextureAtlasC.uiAtlas,"btnHint");
    btnHint.setPosition(GStage.getWorldWidth()/2-btnHint.getWidth(),GStage.getWorldHeight()-btnHint.getHeight()/2, Align.center);
    group.addActor(btnHint);
    btnHint.addListener(new ClickListener(){
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        board.hintBoard("hint");
        return super.touchDown(event, x, y, pointer, button);
      }
    });
  }

  private void createBtnPause(){
    Image btnPause = GUI.createImage(TextureAtlasC.uiAtlas,"btnPause");
    btnPause.setPosition(GStage.getWorldWidth()-btnPause.getWidth()*0.7f,frmLv.getY()+btnPause.getHeight()/2,Align.center);
    group.addActor(btnPause);
    btnPause.addListener(new ClickListener(){
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        new Setting(gameScene,board,jvBoard,Level);
        return super.touchDown(event, x, y, pointer, button);
      }
    });
  }
}
