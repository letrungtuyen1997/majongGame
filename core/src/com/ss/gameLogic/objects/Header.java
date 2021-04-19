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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.ss.GMain;
import com.ss.commons.BitmapFontC;
import com.ss.commons.TextureAtlasC;
import com.ss.core.exSprite.GShapeSprite;
import com.ss.core.util.GLayer;
import com.ss.core.util.GLayerGroup;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.effects.SoundEffect;
import com.ss.gameLogic.config.Config;
import com.ss.scenes.GameScene;

public class Header {
  private Group           group           = new Group();
  private Group           grNotice        = new Group();
  private Group           grBoard;
  private Group           grTimer;
  private GLayerGroup     grCombo;
  private Image           btnPause;
  private Label           lbLvTitle;
  private Board           board;
  private int             Level;
  private GameScene       gameScene;
  private JsonValue       jvBoard[];
  private Group           grTouch;
  private Array<Integer>  arrItSp         = new Array<>();
  private Array<Label>    arrLbSp         = new Array<>();
  public Header(Board board, int Level, GameScene gameScene, Group grBoard, Group grTimer, GLayerGroup grCombo){
    this.board      = board;
    this.Level      = Level;
    this.gameScene  = gameScene;
    this.grBoard    = grBoard;
    this.grTimer    = grTimer;
    this.grCombo    = grCombo;
    GStage.addToLayer(GLayer.top,group);
    createBtnPause();
    createFrmLevel(Level);
    createbtnSuport(board);
    board.setPosTimer(GStage.getWorldWidth()/2,btnPause.getY(Align.center));
    board.setPosCombo(GStage.getWorldWidth()/2,btnPause.getY(Align.top));

  }
  private void createFrmLevel(int lv){
    lbLvTitle = new Label(GMain.locale.get("lbLevel"),new Label.LabelStyle(BitmapFontC.Font_Title,null));
    lbLvTitle.setFontScale(0.9f);
    GlyphLayout GlLvTitle = new GlyphLayout(BitmapFontC.Font_Title,lbLvTitle.getText());
    lbLvTitle.setSize(GlLvTitle.width * lbLvTitle.getFontScaleX(),GlLvTitle.height * lbLvTitle.getFontScaleY());
    lbLvTitle.setPosition(lbLvTitle.getX(Align.center)+20,btnPause.getY()+lbLvTitle.getHeight()*0.52f,Align.center);
    group.addActor(lbLvTitle);
    if(GlLvTitle.width>150)
    {
      lbLvTitle.setFontScale((float)150/GlLvTitle.width);
      lbLvTitle.setAlignment(Align.center);
    }

    Label lbLv = new Label(""+lv,new Label.LabelStyle(BitmapFontC.Font_Title,null));
    lbLv.setFontScale(0.8f);
    lbLv.setAlignment(Align.center);
    lbLv.setPosition(lbLvTitle.getX(Align.center),lbLvTitle.getY()+lbLvTitle.getHeight()*1.7f,Align.center);
    group.addActor(lbLv);
  }

  private void createbtnSuport(Board board){
    Image footer = GUI.createImage(TextureAtlasC.uiAtlas,"footer");
    footer.setPosition(GStage.getWorldWidth()/2,GStage.getWorldHeight()-footer.getHeight()/2,Align.center);
    group.addActor(footer);


    Group btnShuffle = initBtnSuport(footer.getX(Align.center),footer.getY(Align.center),TextureAtlasC.uiAtlas,"btnShuffle",""+ Config.ItSpShuffle,BitmapFontC.Font_Button,0.4f,1,group,new ClickListener(){
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        SoundEffect.Play(SoundEffect.shuffle);
        if(Config.ItSpShuffle>0 ){
          if(board.isUseItem ==true) {
            setQuanItSp("shuffle",-1);
            grTouch = disabledTouch();
            board.SkipHint();
            board.shuffleBoard("shuffle",()->{
              grTouch.clear();
              grTouch.remove();
//            System.out.println("shuffle done");
            });
          }
        }else {
          /// ads
          createPopAds();
        }

        return super.touchDown(event, x, y, pointer, button);
      }
    });

    Group btnHint = initBtnSuport(footer.getX(Align.center)-btnShuffle.getWidth()*1.5f,footer.getY(Align.center),TextureAtlasC.uiAtlas,"btnHint",""+Config.ItSpHint,BitmapFontC.Font_Button,0.4f,1,group,new ClickListener(){
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        SoundEffect.Play(SoundEffect.hint);
        if(Config.ItSpHint>0 ){
          if(board.isUseItem ==true){
            setQuanItSp("hint",-1);
            board.hintBoard("hint");
          }

        }else{
          /// ads
          createPopAds();
        }
        return super.touchDown(event, x, y, pointer, button);
      }
    });

    Group btnBomb = initBtnSuport(footer.getX(Align.center)+btnShuffle.getWidth()*1.5f,footer.getY(Align.center),TextureAtlasC.uiAtlas,"btnBomb",""+Config.ItSpBomb,BitmapFontC.Font_Button,0.4f,1,group,new ClickListener(){
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        SoundEffect.Play(SoundEffect.click);
        if(Config.ItSpBomb > 0 ){
          if(board.isUseItem==true){
            setQuanItSp("bomb",-1);
            grTouch = disabledTouch();
            board.autoMatch(()->{
              grTouch.clear();
              grTouch.remove();
//              System.out.println("finish auto match");
            });
          }else {
            grTouch = disabledTouch();
//            grNotice = new Notice(GMain.locale.get("cantUseItem"),0.4f,new ClickListener(){
//              @Override
//              public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                grNotice.clear();
//                grNotice.remove();
//                grTouch.clear();
//                grTouch.remove();
//                return super.touchDown(event, x, y, pointer, button);
//              }
//            });
          }
        }else {
          /// ads
          createPopAds();

        }

        return super.touchDown(event, x, y, pointer, button);
      }
    });
  }
  private void setQuanItSp(String type,int num){
    if(type.equals("hint")){
      Config.ItSpHint+=num;
      if(arrLbSp.get(1)!=null){
        arrLbSp.get(1).setText(""+Config.ItSpHint);
      }
    }
    if(type.equals("shuffle")){
      Config.ItSpShuffle+=num;
      if(arrLbSp.get(0)!=null){
        arrLbSp.get(0).setText(""+Config.ItSpShuffle);
      }
    }
    if(type.equals("bomb")){
      Config.ItSpBomb+=num;
      if(arrLbSp.get(2)!=null){
        arrLbSp.get(2).setText(""+Config.ItSpBomb);
      }
    }
    ///// save data//////
    GMain.prefs.putInteger("hint",Config.ItSpHint);
    GMain.prefs.putInteger("shuffle",Config.ItSpShuffle);
    GMain.prefs.putInteger("bomb",Config.ItSpBomb);
    GMain.prefs.flush();

  }
  private void setReward(){
    setQuanItSp("hint",Config.RewardHint);
    setQuanItSp("shuffle",Config.RewardShuffle);
    setQuanItSp("bomb",Config.RewardBomb);

  }

  private Group initBtnSuport(float x, float y, TextureAtlas atlas, String kind, String text, BitmapFont bit, float sclText, float sclbtn, Group gr, ClickListener event) {
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
    lbItSp.setPosition(btn.getX() + btn.getWidth()*0.9f, btn.getY() + btn.getHeight()*0.8f, Align.center);
    grbtn.addActor(lbItSp);
    grbtn.setSize(btn.getWidth(), btn.getHeight());
    grbtn.setPosition(x, y, Align.center);
    grbtn.addListener(event);
    //// array lb suport /////
    arrLbSp.add(lbItSp);
    return grbtn;
  }
  private void createBtnPause(){
    btnPause = GUI.createImage(TextureAtlasC.uiAtlas,"btnPause");
    btnPause.setPosition(GStage.getWorldWidth()-btnPause.getWidth()*0.7f,btnPause.getHeight()*0.5f+Config.PaddingAdsBaner,Align.center);
    group.addActor(btnPause);
    btnPause.addListener(new ClickListener(){
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        SoundEffect.Play(SoundEffect.click);
        board.pauseTime(true);

        new Setting(gameScene,board,jvBoard,Level,grBoard,grTimer,grCombo);
        return super.touchDown(event, x, y, pointer, button);
      }
    });
  }
  private void createPopAds() {
    Group grAds = new Group();
    GStage.addToLayer(GLayer.top, grAds);
    grAds.setPosition(GStage.getWorldWidth() / 2, GStage.getWorldHeight() / 2);
    GShapeSprite Gshape = new GShapeSprite();
    Gshape.createRectangle(true, -GStage.getWorldWidth() / 2, -GStage.getWorldHeight() / 2, GStage.getWorldWidth(), GStage.getWorldHeight());
    Gshape.setColor(0, 0, 0, 0.7f);
    grAds.addActor(Gshape);
    grAds.setScale(0);
    grAds.addAction(Actions.scaleTo(1, 1, 0.5f, Interpolation.swingOut));
    Image popup = GUI.createImage(TextureAtlasC.uiAtlas, "popup");
    popup.setPosition(0, 0, Align.center);
    grAds.addActor(popup);

    Label lbTitle = new Label(GMain.locale.get("lbTitle"), new Label.LabelStyle(BitmapFontC.Font_Button, null));
    lbTitle.setFontScale(1.1f);
    lbTitle.setAlignment(Align.center);
    GlyphLayout Gltitle = new GlyphLayout(BitmapFontC.Font_Button, lbTitle.getText());
    lbTitle.setSize(Gltitle.width * lbTitle.getFontScaleX(), Gltitle.height * lbTitle.getFontScaleY());
    lbTitle.setPosition(0, popup.getY() + lbTitle.getHeight() * 1f, Align.center);
    grAds.addActor(lbTitle);

    Label lbDesAds = new Label(GMain.locale.get("lbDesAds"), new Label.LabelStyle(BitmapFontC.Font_Button, null));
    lbDesAds.setFontScale(0.6f);
    GlyphLayout GlDesAds = new GlyphLayout(BitmapFontC.Font_Title, lbDesAds.getText());
    lbDesAds.setSize(GlDesAds.width * lbDesAds.getFontScaleX(), GlDesAds.height * lbDesAds.getFontScaleY());
    lbDesAds.setWidth(popup.getWidth() * 0.8f);
    lbDesAds.setAlignment(Align.center);
    lbDesAds.setPosition(0, -popup.getHeight()*0.15f, Align.bottom);
    grAds.addActor(lbDesAds);
    lbDesAds.setWrap(true);


    Image iconSwap = GUI.createImage(TextureAtlasC.uiAtlas, "btnShuffle");
    iconSwap.setOrigin(Align.center);
    iconSwap.setPosition(popup.getX()+popup.getWidth()/2-iconSwap.getWidth()*2, popup.getY()+popup.getHeight()*0.55f,Align.center);
    grAds.addActor(iconSwap);
    Label lbSwap = new Label("x" + Config.RewardShuffle, new Label.LabelStyle(BitmapFontC.Font_Button, null));
    lbSwap.setFontScale(0.8f);
    GlyphLayout GlSwap = new GlyphLayout(BitmapFontC.Font_Button, lbSwap.getText());
    lbSwap.setSize(GlSwap.width * lbSwap.getFontScaleX(), GlSwap.height * lbSwap.getFontScaleY());
    lbSwap.setPosition(iconSwap.getX() + iconSwap.getWidth()/2 , iconSwap.getY() + iconSwap.getHeight()*1.3f,Align.center);
    grAds.addActor(lbSwap);

    Image iconHint = GUI.createImage(TextureAtlasC.uiAtlas, "btnHint");
    iconHint.setOrigin(Align.center);
    iconHint.setPosition(popup.getX()+popup.getWidth()/2, popup.getY()+popup.getHeight()*0.55f,Align.center);
    grAds.addActor(iconHint);
    Label lbHint = new Label("x" + Config.RewardHint, new Label.LabelStyle(BitmapFontC.Font_Button, null));
    lbHint.setFontScale(0.8f);
    GlyphLayout GlHint = new GlyphLayout(BitmapFontC.Font_Button, lbHint.getText());
    lbHint.setSize(GlHint.width * lbHint.getFontScaleX(), GlHint.height * lbHint.getFontScaleY());
    lbHint.setPosition(iconHint.getX() + iconHint.getWidth()/2 , iconHint.getY() + iconHint.getHeight()*1.3f,Align.center);
    grAds.addActor(lbHint);

    Image iconBomb = GUI.createImage(TextureAtlasC.uiAtlas, "btnBomb");
    iconBomb.setOrigin(Align.center);
    iconBomb.setPosition(popup.getX()+popup.getWidth()/2+iconBomb.getWidth()*2, popup.getY()+popup.getHeight()*0.55f,Align.center);
    grAds.addActor(iconBomb);
    Label lbThunder = new Label("x" + Config.RewardBomb, new Label.LabelStyle(BitmapFontC.Font_Button, null));
    lbThunder.setFontScale(0.8f);
    GlyphLayout GlThunder = new GlyphLayout(BitmapFontC.Font_Button, lbSwap.getText());
    lbThunder.setSize(GlThunder.width * lbThunder.getFontScaleX(), GlThunder.height * lbThunder.getFontScaleY());
    lbThunder.setPosition(iconBomb.getX() + iconBomb.getWidth()/2, iconBomb.getY() + iconBomb.getHeight()*1.3f,Align.center);
    grAds.addActor(lbThunder);
    initButton(-popup.getWidth() * 0.22f, popup.getY(Align.top)-70 , TextureAtlasC.uiAtlas, "btnGreen", GMain.locale.get("late"), BitmapFontC.Font_Title, 0.5f,1, grAds,0, new ClickListener() {
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        SoundEffect.Play(SoundEffect.click);
        grAds.clear();
        grAds.remove();
        return super.touchDown(event, x, y, pointer, button);
      }
    });
    initButton(popup.getWidth() * 0.22f, popup.getY(Align.top)-70, TextureAtlasC.uiAtlas, "btnWatchAds",GMain.locale.get("watch"), BitmapFontC.Font_Title, 0.5f,1, grAds,23, new ClickListener() {
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        SoundEffect.Play(SoundEffect.click);
        showAdsReward(grAds);
        return super.touchDown(event, x, y, pointer, button);
      }
    });
  }
  private void initButton(float x, float y, TextureAtlas atlas, String kind, String text, BitmapFont bit, float sclText, float sclbtn, Group gr,float paddingX, ClickListener event) {
    Group grbtn = new Group();
    gr.addActor(grbtn);
    Image btn = GUI.createImage(atlas, kind);
    btn.setSize(btn.getWidth()*sclbtn,btn.getHeight()*sclbtn);
    btn.setOrigin(Align.center);
    grbtn.addActor(btn);
    Label lbItSp = new Label(text, new Label.LabelStyle(bit, null));
    lbItSp.setFontScale(sclText);
    lbItSp.setAlignment(Align.center);
    GlyphLayout glItSp = new GlyphLayout(bit, lbItSp.getText());
    lbItSp.setSize(glItSp.width * lbItSp.getFontScaleX(), glItSp.height * lbItSp.getFontScaleY());
    lbItSp.setPosition(btn.getX() + btn.getWidth() * 0.5f+paddingX, btn.getY() + btn.getHeight() * 0.45f, Align.center);
    grbtn.addActor(lbItSp);
    grbtn.setSize(btn.getWidth(), btn.getHeight());
//    if(glItSp.width>(grbtn.getWidth()*0.8f)){
//      float scl = (grbtn.getWidth()*0.8f)/glItSp.width;
//      System.out.println("check scl: "+scl);
//      lbItSp.setFontScale(scl);
//      lbItSp.setAlignment(Align.center);
//    }
    grbtn.setPosition(x, y, Align.center);
    grbtn.addListener(event);
  }
  private void showAdsReward(Group gr) {
    if (GMain.platform.isVideoRewardReady()) {
      GMain.platform.ShowVideoReward((succes) -> {
        if (succes) {
          gr.clear();
          gr.remove();
          setReward();
        } else {
          gr.clear();
          gr.remove();
        }
      });
    } else {
      grTouch =disabledTouch();
      grNotice = new Notice(GMain.locale.get("noticeAdsErr"),0.5f,new ClickListener(){
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
          SoundEffect.Play(SoundEffect.click);
          gr.clear();
          gr.remove();
          grNotice.clear();
          grNotice.remove();
          grTouch.clear();
          grTouch.remove();
          return super.touchDown(event, x, y, pointer, button);
        }
      });
    }
  }
  private Group disabledTouch(){
    Group gr = new Group();
    GStage.addToLayer(GLayer.top,gr);
    GShapeSprite bg = new GShapeSprite();
    bg.createRectangle(true,0,0,GStage.getWorldWidth(),GStage.getWorldHeight());
    bg.setColor(0,0,0,0);
    gr.addActor(bg);
    return gr;
  }
  public void dispose(){
    group.clear();
    group.remove();
    if(grTouch!=null)
    {
      grTouch.clear();
      grTouch.remove();
    }
//    System.out.println("delete header");
  }

}
