package com.ss.gameLogic.objects;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
import com.ss.core.util.GLayerGroup;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.effects.SoundEffect;
import com.ss.gameLogic.config.Config;
import com.ss.scenes.GameScene;
import com.ss.scenes.StartScene;

public class Setting {
  private Group         group = new Group();
  private GameScene gameScene;
  public Board         Board;
  public Setting(GameScene gameScene, Board board, JsonValue listLv[], int Level, Group grBoard, Group grTimer, GLayerGroup grCombo){
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
        board.pauseTime(false);
        return super.touchDown(event, x, y, pointer, button);
      }
    });
    group.setScale(0);
    group.addAction(Actions.scaleTo(1,1,0.5f, Interpolation.swingOut));

    Image popup = GUI.createImage(TextureAtlasC.uiAtlas,"popup");
    popup.setOrigin(Align.center);
    popup.setPosition(0,0, Align.center);
    group.addActor(popup);

    Label title = new Label(GMain.locale.get("lbPause"),new Label.LabelStyle(BitmapFontC.Font_Button,null));
    title.setFontScale(1.4f);
    GlyphLayout glTitle = new GlyphLayout(BitmapFontC.Font_Button,title.getText());
    title.setSize(glTitle.width*title.getFontScaleX(),glTitle.height*title.getFontScaleY());
    title.setAlignment(Align.center);
    title.setPosition(0,-popup.getHeight()/2+title.getHeight()*0.85f,Align.center);
    group.addActor(title);

    initButton(0,-50,TextureAtlasC.uiAtlas,"btnGreen",GMain.locale.get("btnResume"),BitmapFontC.Font_Title,0.5f,1,group,new ClickListener(){
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        SoundEffect.Play(SoundEffect.click);
        group.clear();
        group.remove();
        board.pauseTime(false);
        return super.touchDown(event, x, y, pointer, button);
      }
    });

    initButton(0,50,TextureAtlasC.uiAtlas,"btnGreen",GMain.locale.get("btnRestart"),BitmapFontC.Font_Title,0.5f,1,group,new ClickListener(){
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        SoundEffect.Play(SoundEffect.click);
        Board.dispose();
        group.clear();
        group.remove();
        new Board(Level,gameScene,grBoard,grTimer,grCombo);
        return super.touchDown(event, x, y, pointer, button);
      }
    });

    Image btnHome = GUI.createImage(TextureAtlasC.uiAtlas,"home");
    btnHome.setPosition(-btnHome.getWidth()*1.5f,popup.getHeight()/2-btnHome.getHeight(),Align.center);
    group.addActor(btnHome);
    btnHome.addListener(new ClickListener(){
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        SoundEffect.Play(SoundEffect.click);
        Board.dispose();
        gameScene.setScreen(new StartScene());
        return super.touchDown(event, x, y, pointer, button);
      }
    });

    ToggleBtn toggleBtnMusic = new ToggleBtn(TextureAtlasC.uiNotiny,group, !SoundEffect.music,1,"onMusic","offMusic");
    toggleBtnMusic.setPosition(0,popup.getHeight()/2-toggleBtnMusic.getHeight(),Align.center);

    ToggleBtn toggleBtnSound = new ToggleBtn(TextureAtlasC.uiNotiny,group,!SoundEffect.mute,2,"onSound","offSound");
    toggleBtnSound.setPosition(toggleBtnMusic.getWidth()*1.5f,popup.getHeight()/2-toggleBtnSound.getHeight(),Align.center);

    ButtonC btnExit = new ButtonC(TextureAtlasC.uiAtlas,group,"exit");
    btnExit.setPosition(popup.getWidth()/2-btnExit.getSize().x,-popup.getHeight()/2+btnExit.getSize().y,Align.center);
    btnExit.addTouchDown(()->{
      SoundEffect.Play(SoundEffect.click);
      group.clear();
      group.remove();
      board.pauseTime(false);
    });

    /// show ads fullScene
    if(Config.countTimePlay%Config.countShowAds==0)
      GMain.platform.ShowFullscreen();
  }
  private void initButton(float x, float y, TextureAtlas atlas, String kind, String text, BitmapFont bit, float sclText, float sclbtn, Group gr, ClickListener event) {
    Group grbtn = new Group();
    gr.addActor(grbtn);
    Image btn = GUI.createImage(atlas, kind);
    btn.setSize(btn.getWidth()*sclbtn,btn.getHeight()*sclbtn);
    btn.setOrigin(Align.center);
    grbtn.addActor(btn);
    Label lbItSp = new Label(text, new Label.LabelStyle(bit, null));
    lbItSp.setFontScale(sclText);
    GlyphLayout glItSp = new GlyphLayout(bit, lbItSp.getText());
    lbItSp.setSize(glItSp.width * lbItSp.getFontScaleX(), glItSp.height * lbItSp.getFontScaleY());
    lbItSp.setPosition(btn.getX() + btn.getWidth() * 0.5f, btn.getY() + btn.getHeight() * 0.45f, Align.center);
    grbtn.addActor(lbItSp);
    grbtn.setSize(btn.getWidth(), btn.getHeight());
    grbtn.setPosition(x, y, Align.center);
    grbtn.addListener(event);
  }
}
