package com.ss.gameLogic.objects;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.ss.GMain;
import com.ss.commons.BitmapFontC;
import com.ss.commons.TextureAtlasC;
import com.ss.core.exSprite.GShapeSprite;
import com.ss.core.util.GLayer;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.gameLogic.config.Config;
import com.ss.scenes.GameScene;

public class Header {
  private Group           group           = new Group();
  private Group           grNotice        = new Group();
  private Image           frmLv;
  private Board           board;
  private int             Level;
  private GameScene       gameScene;
  private String          jvBoard[];
  private Group           grTouch;
  private Array<Integer>  arrItSp         = new Array<>();
  private Array<Label>    arrLbSp         = new Array<>();
  public Header(Board board, int Level, GameScene gameScene, String listLv[]){
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
    Image footer = GUI.createImage(TextureAtlasC.uiAtlas,"footer");
    footer.setPosition(GStage.getWorldWidth()/2,GStage.getWorldHeight()-footer.getHeight()/2,Align.center);
    group.addActor(footer);

//    Image btnHint = GUI.createImage(TextureAtlasC.uiAtlas,"btnHint");
//    btnHint.setPosition(footer.getX(Align.center)-btnHint.getWidth()*1.5f,footer.getY(Align.center), Align.center);
//    group.addActor(btnHint);
//    btnHint.addListener(new ClickListener(){
//      @Override
//      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//        board.hintBoard("hint");
//        return super.touchDown(event, x, y, pointer, button);
//      }
//    });
//
//    Image btnShuffle = GUI.createImage(TextureAtlasC.uiAtlas,"btnShuffle");
//    btnShuffle.setPosition(footer.getX(Align.center),footer.getY(Align.center), Align.center);
//    group.addActor(btnShuffle);
//    btnShuffle.addListener(new ClickListener(){
//      @Override
//      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//        grTouch = disabledTouch();
//        board.SkipHint();
//        board.shuffleBoard("shuffle",()->{
//          grTouch.clear();
//          grTouch.remove();
//          System.out.println("shuffle done");
//        });
//        return super.touchDown(event, x, y, pointer, button);
//      }
//    });
//
//    Image btnBomb = GUI.createImage(TextureAtlasC.uiAtlas,"btnBomb");
//    btnBomb.setPosition(footer.getX(Align.center)+btnBomb.getWidth()*1.5f,footer.getY(Align.center), Align.center);
//    group.addActor(btnBomb);
//    btnBomb.addListener(new ClickListener(){
//      @Override
//      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//        grTouch = disabledTouch();
//        board.autoMatch(()->{
//          grTouch.clear();
//          grTouch.remove();
//          System.out.println("finish auto match");
//        });
//        return super.touchDown(event, x, y, pointer, button);
//      }
//    });

    Group btnShuffle = initBtnSuport(footer.getX(Align.center),footer.getY(Align.center),TextureAtlasC.uiAtlas,"btnShuffle",""+ Config.ItSpShuffle,BitmapFontC.Font_Button,0.6f,1,group,new ClickListener(){
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        if(Config.ItSpShuffle>0){
          setQuanItSp("shuffle",-1);
          grTouch = disabledTouch();
          board.SkipHint();
          board.shuffleBoard("shuffle",()->{
            grTouch.clear();
            grTouch.remove();
            System.out.println("shuffle done");
          });
        }else {
          /// ads
          createPopAds();
        }

        return super.touchDown(event, x, y, pointer, button);
      }
    });

    Group btnHint = initBtnSuport(footer.getX(Align.center)-btnShuffle.getWidth()*1.5f,footer.getY(Align.center),TextureAtlasC.uiAtlas,"btnHint",""+Config.ItSpHint,BitmapFontC.Font_Button,0.6f,1,group,new ClickListener(){
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        if(Config.ItSpHint>0){
          setQuanItSp("hint",-1);
          board.hintBoard("hint");
        }else{
          /// ads
          createPopAds();
        }
        return super.touchDown(event, x, y, pointer, button);
      }
    });

    Group btnBomb = initBtnSuport(footer.getX(Align.center)+btnShuffle.getWidth()*1.5f,footer.getY(Align.center),TextureAtlasC.uiAtlas,"btnBomb",""+Config.ItSpBomb,BitmapFontC.Font_Button,0.6f,1,group,new ClickListener(){
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        if(Config.ItSpBomb > 0){
          setQuanItSp("bomb",-1);
          grTouch = disabledTouch();
          board.autoMatch(()->{
            grTouch.clear();
            grTouch.remove();
            System.out.println("finish auto match");
          });
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
    lbItSp.setPosition(btn.getX() + btn.getWidth()*0.9f, btn.getY() + btn.getHeight()*0.7f, Align.center);
    grbtn.addActor(lbItSp);
    grbtn.setSize(btn.getWidth(), btn.getHeight());
    grbtn.setPosition(x, y, Align.center);
    grbtn.addListener(event);
    //// array lb suport /////
    arrLbSp.add(lbItSp);
    return grbtn;
  }
  private void createBtnPause(){
    Image btnPause = GUI.createImage(TextureAtlasC.uiAtlas,"btnPause");
    btnPause.setPosition(GStage.getWorldWidth()-btnPause.getWidth()*0.7f,frmLv.getY()+btnPause.getHeight()/2,Align.center);
    group.addActor(btnPause);
    btnPause.addListener(new ClickListener(){
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        board.pauseTime(true);
        new Setting(gameScene,board,jvBoard,Level);
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

    Label lbTitle = new Label("Thong bao", new Label.LabelStyle(BitmapFontC.Font_Title, null));
    lbTitle.setFontScale(1.5f);
    lbTitle.setAlignment(Align.center);
    GlyphLayout Gltitle = new GlyphLayout(BitmapFontC.Font_Button, lbTitle.getText());
    lbTitle.setSize(Gltitle.width * lbTitle.getFontScaleX(), Gltitle.height * lbTitle.getFontScaleY());
    lbTitle.setPosition(0, popup.getY() + lbTitle.getHeight() * 0.5f, Align.center);
    grAds.addActor(lbTitle);

    Label lbDesAds = new Label("Xem quang cao de nhan", new Label.LabelStyle(BitmapFontC.Font_Button, null));
    lbDesAds.setFontScale(0.4f);
    GlyphLayout GlDesAds = new GlyphLayout(BitmapFontC.Font_Title, lbDesAds.getText());
    lbDesAds.setSize(GlDesAds.width * lbDesAds.getFontScaleX(), GlDesAds.height * lbDesAds.getFontScaleY());
    lbDesAds.setWidth(popup.getWidth() * 0.8f);
    lbDesAds.setAlignment(Align.center);
    lbDesAds.setPosition(0, -popup.getHeight()*0.15f, Align.bottom);
    grAds.addActor(lbDesAds);
    lbDesAds.setWrap(true);


    Image iconSwap = GUI.createImage(TextureAtlasC.uiAtlas, "btnShuffle");
    iconSwap.setOrigin(Align.center);
    iconSwap.setPosition(popup.getX()+popup.getWidth()/2-iconSwap.getWidth()*2, popup.getY()+popup.getHeight()*0.6f,Align.center);
    grAds.addActor(iconSwap);
    Label lbSwap = new Label("x" + Config.ItSpShuffle, new Label.LabelStyle(BitmapFontC.Font_Button, null));
    lbSwap.setFontScale(0.4f);
    GlyphLayout GlSwap = new GlyphLayout(BitmapFontC.Font_Button, lbSwap.getText());
    lbSwap.setSize(GlSwap.width * lbSwap.getFontScaleX(), GlSwap.height * lbSwap.getFontScaleY());
    lbSwap.setPosition(iconSwap.getX() + iconSwap.getWidth()/2 , iconSwap.getY() + iconSwap.getHeight()*1.2f,Align.center);
    grAds.addActor(lbSwap);

    Image iconHint = GUI.createImage(TextureAtlasC.uiAtlas, "btnHint");
    iconHint.setOrigin(Align.center);
    iconHint.setPosition(popup.getX()+popup.getWidth()/2, popup.getY()+popup.getHeight()*0.6f,Align.center);
    grAds.addActor(iconHint);
    Label lbHint = new Label("x" + Config.RewardHint, new Label.LabelStyle(BitmapFontC.Font_Button, null));
    lbHint.setFontScale(0.4f);
    GlyphLayout GlHint = new GlyphLayout(BitmapFontC.Font_Button, lbHint.getText());
    lbHint.setSize(GlHint.width * lbHint.getFontScaleX(), GlHint.height * lbHint.getFontScaleY());
    lbHint.setPosition(iconHint.getX() + iconHint.getWidth()/2 , iconHint.getY() + iconHint.getHeight()*1.2f,Align.center);
    grAds.addActor(lbHint);

    Image iconBomb = GUI.createImage(TextureAtlasC.uiAtlas, "btnBomb");
    iconBomb.setOrigin(Align.center);
    iconBomb.setPosition(popup.getX()+popup.getWidth()/2+iconBomb.getWidth()*2, popup.getY()+popup.getHeight()*0.6f,Align.center);
    grAds.addActor(iconBomb);
    Label lbThunder = new Label("x" + Config.RewardBomb, new Label.LabelStyle(BitmapFontC.Font_Button, null));
    lbThunder.setFontScale(0.4f);
    GlyphLayout GlThunder = new GlyphLayout(BitmapFontC.Font_Button, lbSwap.getText());
    lbThunder.setSize(GlThunder.width * lbThunder.getFontScaleX(), GlThunder.height * lbThunder.getFontScaleY());
    lbThunder.setPosition(iconBomb.getX() + iconBomb.getWidth()/2, iconBomb.getY() + iconBomb.getHeight()*1.2f,Align.center);
    grAds.addActor(lbThunder);
    initButton(-popup.getWidth() * 0.22f, popup.getY(Align.top)-70 , TextureAtlasC.uiAtlas, "btnGreen", "để sau", BitmapFontC.Font_Button, 0.4f,0.8f, grAds, new ClickListener() {
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        grAds.clear();
        grAds.remove();
        return super.touchDown(event, x, y, pointer, button);
      }
    });
    initButton(popup.getWidth() * 0.22f, popup.getY(Align.top)-70, TextureAtlasC.uiAtlas, "btnWatchAds","xem ads", BitmapFontC.Font_Button, 0.4f,0.8f, grAds, new ClickListener() {
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        showAdsReward(grAds);
        return super.touchDown(event, x, y, pointer, button);
      }
    });
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
      grNotice = new Notice("khong load duoc quang cao",0.5f,new ClickListener(){
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
          gr.clear();
          gr.remove();
          grNotice.clear();
          grNotice.remove();
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

}
