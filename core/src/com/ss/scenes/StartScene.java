package com.ss.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.TimeUtils;
import com.ss.GMain;
import com.ss.commons.BitmapFontC;
import com.ss.commons.TextureAtlasC;
import com.ss.core.action.exAction.GArcMoveByAction;
import com.ss.core.action.exAction.GArcMoveToAction;
import com.ss.core.action.exAction.GPathAction;
import com.ss.core.action.exAction.GSimpleAction;
import com.ss.core.action.exAction.PathAction;
import com.ss.core.exSprite.GShapeSprite;
import com.ss.core.transitions.GTransitionFade;
import com.ss.core.transitions.GTransitionRotationScale;
import com.ss.core.transitions.GTransitionSlide;
import com.ss.core.util.GLayer;
import com.ss.core.util.GScreen;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.effects.SoundEffect;
import com.ss.gameLogic.config.Config;
import com.ss.gameLogic.objects.CrossPanel;
import com.ss.gameLogic.objects.EndGame;
import com.ss.gameLogic.objects.LeaderBoard;
import com.ss.utils.Utils;
import com.sun.nio.sctp.HandlerResult;
import com.sun.nio.sctp.Notification;
import com.sun.nio.sctp.NotificationHandler;

public class StartScene extends GScreen {
  private Group         group     = new Group();
  private Group         btnStart,btnLevel;
  @Override
  public void dispose() {

  }

  @Override
  public void init() {
    GStage.addToLayer(GLayer.top,group);
    if(!SoundEffect.music)
      SoundEffect.Playmusic();
    createBg();

  }

  @Override
  public void run() {

  }

  private void createBg(){
    GMain.platform.ShowBanner(true);
    Image bg = GUI.createImage(TextureAtlasC.uiAtlas,"bg");
    bg.setSize(GStage.getWorldWidth(),GStage.getWorldHeight());
    group.addActor(bg);

    Image logo = GUI.createImage(TextureAtlasC.uiNotiny,"logo");
    logo.setOrigin(Align.center);
    logo.setPosition(GStage.getWorldWidth()/2,logo.getHeight()*0.6f, Align.center);
    group.addActor(logo);
    aniLogo(logo);

    btnStart = initButton(GStage.getWorldWidth()/2,GStage.getWorldHeight()/2,TextureAtlasC.uiAtlas,"btnBrown",GMain.locale.get("btnPlay"), BitmapFontC.Font_Button,0.9f,1,group,new ClickListener(){
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        SoundEffect.Play(SoundEffect.click);
        Config.isContinues = true;
        setScreen(new GameScene(), GTransitionFade.init(1));
        return super.touchDown(event, x, y, pointer, button);
      }
    });

    btnLevel = initButton(GStage.getWorldWidth()/2,btnStart.getY()+btnStart.getHeight()*2,TextureAtlasC.uiAtlas,"btnBrown",GMain.locale.get("btnLevel"),BitmapFontC.Font_Button,0.9f,1,group,new ClickListener(){
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        SoundEffect.Play(SoundEffect.click);
        Config.isContinues = false;
        setScreen(new GameScene(), GTransitionRotationScale.init(0.5f,1));
//        new EndGame(true,3,null,1,null,null,null,null,null);

        return super.touchDown(event, x, y, pointer, button);
      }
    });

    Table table = new Table();
    table.defaults().pad(10);
    table.setFillParent(true);

    Image btnRank = GUI.createImage(TextureAtlasC.uiNotiny,"btnRank");
//    Image btnGameOther = GUI.createImage(TextureAtlasC.uiNotiny,"btnGameOther");

    Button btnMusic = GUI.creatButton(TextureAtlasC.uiNotiny.findRegion("onMusic"));
    btnMusic.getStyle().checked = new TextureRegionDrawable(TextureAtlasC.uiNotiny.findRegion("offMusic"));
    btnMusic.setSize(btnRank.getWidth(),btnRank.getHeight());

    Button btnSound = GUI.creatButton(TextureAtlasC.uiNotiny.findRegion("onSound"));
    btnSound.getStyle().checked = new TextureRegionDrawable(TextureAtlasC.uiNotiny.findRegion("offSound"));
    btnSound.setSize(btnRank.getWidth(),btnRank.getHeight());


    table.add(btnRank).width(btnRank.getWidth()).height(btnRank.getHeight());
//    table.add(btnGameOther).width(btnGameOther.getWidth()).height(btnGameOther.getHeight());
    table.add(btnMusic).width(btnMusic.getWidth()).height(btnMusic.getHeight());
    table.add(btnSound).width(btnSound.getWidth()).height(btnSound.getHeight());
    group.addActor(table);
    table.setPosition(GStage.getWorldWidth()/2,GStage.getWorldHeight()-100);
    ////// event btn /////
    eventBtn(btnRank,()->{
      new LeaderBoard();


    });
//    eventBtn(btnGameOther,()->{
//      new CrossPanel();
//
//    });
    btnMusic.setChecked(SoundEffect.music);
    btnMusic.addListener(new ClickListener(){
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        SoundEffect.Play(SoundEffect.click);
        SoundEffect.music = !SoundEffect.music;
        btnMusic.setChecked(!SoundEffect.music);
        if(!SoundEffect.music)
          SoundEffect.Playmusic();
        else
          SoundEffect.Pausemusic();
        return super.touchDown(event, x, y, pointer, button);
      }
    });
    btnSound.setChecked(SoundEffect.mute);
    btnSound.addListener(new ClickListener(){
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        SoundEffect.Play(SoundEffect.click);
        SoundEffect.mute = !SoundEffect.mute;
        btnSound.setChecked(!SoundEffect.mute);
        return super.touchDown(event, x, y, pointer, button);
      }
    });


  }
  private void eventBtn(Image img,Runnable runnable){
    img.setOrigin(Align.center);
    img.addListener(new ClickListener(){
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        SoundEffect.Play(SoundEffect.click);
        img.addAction(Actions.sequence(
                Actions.scaleTo(0.9f,0.9f,0.1f),
                Actions.scaleTo(1,1,0.1f),
                Actions.run(runnable)
        ));
        return super.touchDown(event, x, y, pointer, button);
      }
    });
  }

  private Group initButton(float x, float y, TextureAtlas atlas, String kind, String text, BitmapFont bit, float sclText, float sclbtn, Group gr, ClickListener event) {
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
    return grbtn;
  }

  private void aniLogo(Image img){
    img.addAction(Actions.sequence(
            Actions.scaleTo(1.1f,0.9f,1f),
            Actions.scaleTo(1f,1f,1f),
            Actions.delay(1),
            GSimpleAction.simpleAction((d,a)->{
              aniLogo(img);
              return true;
            })
    ));
  }



}
