package com.ss.gameLogic.objects;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.JsonValue;
import com.ss.GMain;
import com.ss.commons.BitmapFontC;
import com.ss.commons.ButtonC;
import com.ss.commons.TextureAtlasC;
import com.ss.commons.ToggleBtn;
import com.ss.core.exSprite.GShapeSprite;
import com.ss.core.util.GLayer;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.effects.SoundEffect;
import com.ss.scenes.gameScene;
import com.ss.utils.Utils;

import java.util.logging.Level;

public class Setting {
  private Group         group = new Group();
  private gameScene     gameScene;
  public Board         Board;
  public Setting(gameScene gameScene, Board board, String listLv[], int Level){
    this.gameScene  = gameScene;
    this.Board      = board;
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

    Image ribbon = GUI.createImage(TextureAtlasC.uiAtlas,"titlePause");
    ribbon.setSize(ribbon.getWidth()*0.8f,ribbon.getHeight()*0.8f);
    ribbon.setOrigin(Align.center);
    ribbon.setPosition(0,-popup.getHeight()/2+ribbon.getHeight()*0.8f,Align.center);
    group.addActor(ribbon);

    Image btnResume = GUI.createImage(TextureAtlasC.uiAtlas,"btnPlay");
    btnResume.setPosition(0,-btnResume.getHeight()/2,Align.center);
    group.addActor(btnResume);

    Image btnReplay = GUI.createImage(TextureAtlasC.uiAtlas,"btnReplay");
    btnReplay.setPosition(0,btnReplay.getHeight()/2,Align.center);
    group.addActor(btnReplay);
    btnReplay.addListener(new ClickListener(){
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        Board.dispose();
        group.clear();
        group.remove();
        Board = new Board(Level,listLv,gameScene);
        new Header(Board,Level,gameScene,listLv);
        return super.touchDown(event, x, y, pointer, button);
      }
    });

    Image btnHome = GUI.createImage(TextureAtlasC.uiAtlas,"home");
    btnHome.setPosition(-btnHome.getWidth(),popup.getHeight()/2-btnHome.getHeight()*0.7f,Align.center);
    group.addActor(btnHome);
    btnHome.addListener(new ClickListener(){
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        gameScene.setScreen(new gameScene());
        return super.touchDown(event, x, y, pointer, button);
      }
    });

    ToggleBtn toggleBtnMusic = new ToggleBtn(TextureAtlasC.uiAtlas,group, !SoundEffect.music,1,"onMusic","offMusic");
    toggleBtnMusic.setPosition(0,popup.getHeight()/2-toggleBtnMusic.getHeight()*0.7f,Align.center);

    ToggleBtn toggleBtnSound = new ToggleBtn(TextureAtlasC.uiAtlas,group,!SoundEffect.mute,2,"onSound","offSound");
    toggleBtnSound.setPosition(toggleBtnMusic.getWidth(),popup.getHeight()/2-toggleBtnSound.getHeight()*0.7f,Align.center);

    ButtonC btnExit = new ButtonC(TextureAtlasC.uiAtlas,group,"exit");
    btnExit.setPosition(popup.getWidth()/2-btnExit.getSize().x,-popup.getHeight()/2+btnExit.getSize().y,Align.center);
    btnExit.addTouchDown(()->{
      group.clear();
      group.remove();
    });





  }
}
