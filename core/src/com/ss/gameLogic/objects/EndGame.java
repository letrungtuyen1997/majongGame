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
import com.badlogic.gdx.utils.JsonValue;
import com.ss.GMain;
import com.ss.commons.BitmapFontC;
import com.ss.commons.TextureAtlasC;
import com.ss.commons.Tweens;
import com.ss.core.action.exAction.GScreenFlashAction;
import com.ss.core.action.exAction.GScreenShake2Action;
import com.ss.core.action.exAction.GScreenShake3Action;
import com.ss.core.action.exAction.GScreenShakeAction;
import com.ss.core.action.exAction.GSimpleAction;
import com.ss.core.exSprite.GShapeSprite;
import com.ss.core.util.GLayer;
import com.ss.core.util.GLayerGroup;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.effects.SoundEffect;
import com.ss.effects.effectWin;
import com.ss.gameLogic.config.Config;
import com.ss.scenes.GameScene;
import com.ss.scenes.StartScene;

public class EndGame {
  private Group    group       = new Group();
  private Group    gref        = new Group();
  private Group    grtop       = new Group();
  private Group    grTouch     = new Group();
  private Group    grNotice;
  private String   ribon,title,pop;
  private Group    grBoard;
  private Image    popup,gift,sclBar,icGift;
  private Label    lbProgress;
  private float    dura=3;
  private int      tic=0;
  private int      moment=0;
  private int      up=0;
  private int      count=0;
  public EndGame(boolean isWin, int star, JsonValue jsLV[], int lv, GameScene gameScene, Board board, Group grBoard, Group grTimer, GLayerGroup grCombo){
    this.grBoard = grBoard;
    GStage.addToLayer(GLayer.top,group);
    GStage.addToLayer(GLayer.top,gref);
    GStage.addToLayer(GLayer.top,grtop);
    if(isWin){
      ribon = "ribonWin";
      String id = GMain.locale.get("idLang");
//      if(id.equals("Vn")==false||id.equals("Vn")==false)
//        id="En";
      title = "victory"+id;
      pop   = "popup";
      SoundEffect.Play(SoundEffect.win);
      saveData(star,lv+1);

    }else {
      SoundEffect.Play(SoundEffect.lose);
      ribon="ribonFail";
      String id = GMain.locale.get("idLang");
//      if(id.equals("Vn")==false||id.equals("Vn")==false)
//        id="En";
      title = "fail"+id;
      pop   = "popup";
    }
    group.setPosition(GStage.getWorldWidth()/2,GStage.getWorldHeight()/2);
    gref.setPosition(GStage.getWorldWidth()/2,GStage.getWorldHeight()/2);
    grtop.setPosition(GStage.getWorldWidth()/2,GStage.getWorldHeight()/2);
    GShapeSprite Gshape = new GShapeSprite();
    Gshape.createRectangle(true,-GStage.getWorldWidth()/2,-GStage.getWorldHeight()/2,GStage.getWorldWidth(),GStage.getWorldHeight());
    Gshape.setColor(0,0,0,0.6f);
    group.addActor(Gshape);
    Gshape.addListener(new ClickListener(){
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//        group.clear();
//        group.remove();
        return super.touchDown(event, x, y, pointer, button);
      }
    });
    group.setScale(0);
    group.addAction(Actions.scaleTo(1,1,0.5f, Interpolation.swingOut));
    gref.setScale(0);
    gref.addAction(Actions.scaleTo(1,1,0.5f, Interpolation.swingOut));
    grtop.setScale(0);
    grtop.addAction(Actions.scaleTo(1,1,0.5f, Interpolation.swingOut));

    popup = GUI.createImage(TextureAtlasC.uiAtlas,pop);
    popup.setOrigin(Align.center);
    popup.setPosition(0,0, Align.center);
    group.addActor(popup);

    Image ribbon = GUI.createImage(TextureAtlasC.uiAtlas,ribon);
    ribbon.setOrigin(Align.center);
    ribbon.setPosition(0,-popup.getHeight()/2+ribbon.getHeight()/3,Align.center);
    group.addActor(ribbon);

    Image Title = GUI.createImage(TextureAtlasC.uiAtlas,title);
    Title.setPosition(ribbon.getX(Align.center),ribbon.getY(Align.center)-ribbon.getHeight()*0.3f,Align.center);
    group.addActor(Title);

    Tweens.setTimeout(group,0.1f,()->{
      if(isWin){
        aniStar(star,()->{
          effectWin ef = new effectWin(effectWin.Light,0,20,3,gref);
          ef.start();
          gift = GUI.createImage(TextureAtlasC.uiAtlas,"giftClose");
          gift.setOrigin(Align.center);
          gift.setPosition(0,gift.getHeight()*0.25f,Align.center);
          grtop.addActor(gift);
          gift.setScale(0);
          gift.addAction(Actions.sequence(
                  Actions.scaleTo(0.8f,0.8f,0.2f),
                  GSimpleAction.simpleAction((d,a)->{
                    aniGift(gift);
                    return true;
                  })
          ));
          effectWin ef1= new effectWin(effectWin.FireWork,0,0,2,grtop);
          ef1.start();
          updateProgress(star,()->{
            if(moment>=Config.targetGift){
              aniGift(icGift);
              effectWin ef2 = new effectWin(effectWin.Light,icGift.getX(Align.center),icGift.getY(Align.center),0.5f,gref);
              ef2.start();
              icGift.addListener(new ClickListener(){
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                  moment =  moment - Config.targetGift;
                  GMain.prefs.putInteger("momentGift",moment);
                  GMain.prefs.flush();
                  createPopAds(board,lv,jsLV,gameScene,grTimer,grCombo,()->{
                    board.dispose();
                    group.clear();
                    group.remove();
                    gref.clear();
                    gref.remove();
                    grtop.clear();
                    grtop.remove();
                    grTouch.clear();
                    grTouch.remove();
                    setReward();
                    Config.setSkin(lv+1);
                    new Board(lv+1,jsLV,gameScene,grBoard,grTimer,grCombo);
                  });
                  setReward();
                  return super.touchDown(event, x, y, pointer, button);
                }
              });
            }




          });
        });
        initButton(-popup.getWidth()*0.23f,popup.getHeight()*0.35f,TextureAtlasC.uiAtlas,"btnGreen",GMain.locale.get("btnNext"),BitmapFontC.Font_Title,0.5f,1,grtop,0,new ClickListener(){
          @Override
          public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            SoundEffect.Play(SoundEffect.click);
            board.dispose();
            group.clear();
            group.remove();
            gref.clear();
            gref.remove();
            grtop.clear();
            grtop.remove();
            if(jsLV.length<=lv+1){
              grNotice = new Notice(GMain.locale.get("noticeMaxLv"),0.6f,new ClickListener(){
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                  SoundEffect.Play(SoundEffect.click);
                  grNotice.clear();
                  grNotice.remove();
                  gameScene.setScreen(new StartScene());
                  return super.touchDown(event, x, y, pointer, button);
                }
              });
            }else {
              Config.setSkin(lv+1);
              new Board(lv+1,jsLV,gameScene,grBoard,grTimer,grCombo);
            }
            return super.touchDown(event, x, y, pointer, button);
          }
        });

        initButton(popup.getWidth()*0.23f,popup.getHeight()*0.35f,TextureAtlasC.uiAtlas,"btnWatchAds",GMain.locale.get("btnGift"),BitmapFontC.Font_Title,0.5f,1,grtop,23,new ClickListener(){
          @Override
          public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            SoundEffect.Play(SoundEffect.click);
            showAdsReward(board,lv+1,jsLV,gameScene,grTimer,grCombo);
            return super.touchDown(event, x, y, pointer, button);
          }
        });

      }else {
        for (int i=0;i<3;i++){
          int finalI = i;
          Tweens.setTimeout(group,0.4f*i,()->{
            Image ic = GUI.createImage(TextureAtlasC.uiAtlas,"star"+(finalI +1)+"Off");
            ic.setPosition(-ic.getWidth()*1.5f+ic.getWidth()* finalI,100,Align.topLeft);
            group.addActor(ic);
            if(finalI ==0)
              ic.setX(ic.getX()-10);
            if(finalI ==2)
              ic.setX(ic.getX()+10);
            ic.setScale(5);
            ic.setOrigin(Align.center);
            ic.addAction(Actions.scaleTo(1,1,0.2f));

          });
        }
        updateProgress(star,()->{
          if(moment>=Config.targetGift){
            aniGift(icGift);
            effectWin ef2 = new effectWin(effectWin.Light,icGift.getX(Align.center),icGift.getY(Align.center),0.5f,gref);
            ef2.start();
            icGift.addListener(new ClickListener(){
              @Override
              public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                moment =  moment - Config.targetGift;
                GMain.prefs.putInteger("momentGift",moment);
                GMain.prefs.flush();
                createPopAds(board,lv,jsLV,gameScene,grTimer,grCombo,()->{
                  updateProgressDec();
                  grTouch.clear();
                  grTouch.remove();
                  setReward();
                  if(moment<Config.targetGift){
                    icGift.setTouchable(Touchable.disabled);
                    icGift.clearActions();
                    ef2.remove();

                  }
                });
                setReward();
                return super.touchDown(event, x, y, pointer, button);
              }
            });
          }




        });

        initButton(0,popup.getHeight()*0.35f,TextureAtlasC.uiAtlas,"btnGreen",GMain.locale.get("btnRestart"),BitmapFontC.Font_Title,0.4f,1.2f,group,0,new ClickListener(){
          @Override
          public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            SoundEffect.Play(SoundEffect.click);
            board.dispose();
            group.clear();
            group.remove();
            gref.clear();
            gref.remove();
            grtop.clear();
            grtop.remove();
            Config.setSkin(lv);
            new Board(lv,jsLV,gameScene,grBoard,grTimer,grCombo);
            return super.touchDown(event, x, y, pointer, button);
          }
        });
      }
    });


    if(Config.countTimePlay%Config.countShowAds==0)
      GMain.platform.ShowFullscreen();
    createReward(GMain.prefs.getInteger("momentGift"),Config.targetGift);
    updatePref(star);

  }
  private void aniStar(int star,Runnable runnable){
    Array<Image> arrIc = new Array<>();
    for (int i=0;i<3;i++){
      Image ic = GUI.createImage(TextureAtlasC.uiAtlas,"star"+(i+1)+"Off");
      ic.setSize(ic.getWidth()*0.6f,ic.getHeight()*0.6f);
      ic.setOrigin(Align.center);
      ic.setPosition(-ic.getWidth()*1.5f+ic.getWidth()*i,-50,Align.topLeft);
      grtop.addActor(ic);
      if(i==0)
        ic.setX(ic.getX()-20);
      if(i==2)
        ic.setX(ic.getX()+20);

      arrIc.add(ic);
    }

    for (int i=0;i<star;i++){
      int finalI = i;
      Tweens.setTimeout(group,0.5f*i,()->{
        System.out.println("here!!"+finalI);
        Image icStar = GUI.createImage(TextureAtlasC.uiAtlas,"star"+(finalI+1)+"On");
        icStar.setSize(icStar.getWidth()*0.6f,icStar.getHeight()*0.6f);
        icStar.setScale(5);
        icStar.setOrigin(Align.center);
        icStar.setPosition(arrIc.get(finalI).getX(),arrIc.get(finalI).getY());
        grtop.addActor(icStar);
        icStar.addAction(Actions.sequence(
          Actions.scaleTo(1,1,0.2f),
                Actions.parallel(
                        GScreenShake3Action.screenShake(Config.DuraShake, 5, GLayer.top),
                        Actions.run(()->{
                          SoundEffect.Play(SoundEffect.getStar1+finalI);
                        })
                        ),
          GSimpleAction.simpleAction((d,a)->{
            if(finalI==star-1){
              group.addAction(Actions.run(runnable));
            }
            return true;
          })
        ));
      });
    }
  }
  private void aniGift(Image img){
    img.addAction(Actions.sequence(
            Actions.delay(0.5f),
            Actions.parallel(
              Actions.rotateBy(15,0.4f),
              Actions.sequence(
                Actions.moveBy(10,0,0.1f),
                Actions.moveBy(-20,0,0.1f),
                Actions.moveBy(20,0,0.1f),
                Actions.moveBy(-20,0,0.1f),
                Actions.moveBy(10,0,0.1f)
              )
            ),
            Actions.parallel(
              Actions.rotateBy(-30,0.4f),
              Actions.sequence(
                Actions.moveBy(10,0,0.1f),
                Actions.moveBy(-20,0,0.1f),
                Actions.moveBy(20,0,0.1f),
                Actions.moveBy(-20,0,0.1f),
                Actions.moveBy(10,0,0.1f)
              )
            ),
            Actions.parallel(
              Actions.rotateBy(15,0.4f),
              Actions.sequence(
                Actions.moveBy(10,0,0.1f),
                Actions.moveBy(-20,0,0.1f),
                Actions.moveBy(20,0,0.1f),
                Actions.moveBy(-20,0,0.1f),
                Actions.moveBy(10,0,0.1f)
              )
            ),
            GSimpleAction.simpleAction((d,a)->{
              aniGift(img);
              return true;
            })
    ));
  }
  private void aniOpenGift(Image img,Runnable callback){
    SoundEffect.Play(SoundEffect.open);
    Image giftOpen = GUI.createImage(TextureAtlasC.uiAtlas,"giftOpen");
    giftOpen.setPosition(img.getX(Align.center),img.getY(Align.center),Align.center);
    grtop.addActor(giftOpen);
    giftOpen.getColor().a=0;
    img.clearActions();
    img.setRotation(0);
    img.setOrigin(Align.center);
    img.addAction(Actions.sequence(
                    Actions.parallel(
                      GScreenShakeAction.screenShake1(3,5,img),
                      Actions.scaleTo(1.5f,1.5f,2)
                    ),
                    Actions.parallel(
                            Actions.alpha(0,1),
                            Actions.run(()->{
                              giftOpen.addAction(Actions.sequence(
                                      Actions.alpha(1,1),
                                      Actions.run(callback)
                              ));
                              return;
                            })
                    )
            )
          );

  }

  private void saveData(int star,int lv){
    int oldStar = GMain.prefs.getInteger("starLv"+lv);
    int LvPre   = GMain.prefs.getInteger("LvPre");
    System.out.println("lv: "+lv);

    System.out.println("oldStar: "+oldStar);
    System.out.println("lvPre: "+oldStar);
    if(oldStar<star){
      GMain.prefs.putInteger("starLv"+lv,star);
      GMain.prefs.flush();
    }
    if(LvPre<=lv){
      GMain.prefs.putInteger("LvPre",lv+1);
      Config.LvPer = lv+1;
      GMain.prefs.flush();
    }
    int starPre=0;
    for (int i=1;i<=Config.LvPer;i++){
       starPre+=GMain.prefs.getInteger("starLv"+i);
      GMain.prefs.putInteger("sumStar",starPre);
      GMain.prefs.flush();
    }

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
    GlyphLayout glItSp = new GlyphLayout(bit, lbItSp.getText());
    lbItSp.setSize(glItSp.width * lbItSp.getFontScaleX(), glItSp.height * lbItSp.getFontScaleY());
    lbItSp.setPosition(btn.getX() + btn.getWidth() * 0.5f+paddingX, btn.getY() + btn.getHeight() * 0.5f, Align.center);
    grbtn.addActor(lbItSp);
    grbtn.setSize(btn.getWidth(), btn.getHeight());
    grbtn.setPosition(x, y, Align.center);
    grbtn.addListener(event);
  }

  private void showAdsReward(Board board,int lv, JsonValue[] jsLV,GameScene gameScene,Group grTimer,GLayerGroup grCombo) {
    if (GMain.platform.isVideoRewardReady()) {
      GMain.platform.ShowVideoReward((succes) -> {
        if (succes) {
//          gr.clear();
//          gr.remove();
          aniOpenGift(gift,()->{
            System.out.println("mo qua");
            createPopAds(board,lv,jsLV,gameScene,grTimer,grCombo,()->{
              board.dispose();
              group.clear();
              group.remove();
              gref.clear();
              gref.remove();
              grtop.clear();
              grtop.remove();
              grTouch.clear();
              grTouch.remove();
              setReward();
              Config.setSkin(lv);
              new Board(lv,jsLV,gameScene,grBoard,grTimer,grCombo);
            });
          });
        } else {
//          gr.clear();
//          gr.remove();

        }
      });
    } else {
      grNotice = new Notice(GMain.locale.get("noticeAdsErr"),0.5f,new ClickListener(){
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
          SoundEffect.Play(SoundEffect.click);
//          gr.clear();
//          gr.remove();
          grNotice.clear();
          grNotice.remove();
          return super.touchDown(event, x, y, pointer, button);
        }
      });
    }
  }

  private void createPopAds(Board board,int lv, JsonValue[] jsLV,GameScene gameScene,Group grTimer,GLayerGroup grCombo,Runnable runnable) {
    grTouch = disabledTouch();
    SoundEffect.Play(SoundEffect.unlock);
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

    Label lbDesAds = new Label(GMain.locale.get("lbDesAds2"), new Label.LabelStyle(BitmapFontC.Font_Button, null));
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
    initButton(0, popup.getY(Align.top)-70 , TextureAtlasC.uiAtlas, "btnGreen", GMain.locale.get("btnGive"), BitmapFontC.Font_Title, 0.55f,1.2f, grAds,0, new ClickListener() {
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        SoundEffect.Play(SoundEffect.click);
        grAds.clear();
        grAds.remove();
        group.addAction(Actions.run(runnable));
        return super.touchDown(event, x, y, pointer, button);
      }
    });
  }

  private void setQuanItSp(String type,int num){
    if(type.equals("hint")){
      Config.ItSpHint+=num;
    }
    if(type.equals("shuffle")){
      Config.ItSpShuffle+=num;
    }
    if(type.equals("bomb")){
      Config.ItSpBomb+=num;
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
  private void createReward(int Moment, int target){
    moment=Moment;
    Image bar = GUI.createImage(TextureAtlasC.uiAtlas,"barGift");
    bar.setPosition(popup.getX(Align.center),popup.getY(Align.top)+bar.getHeight()*2,Align.center);
    grtop.addActor(bar);
    sclBar = GUI.createImage(TextureAtlasC.uiAtlas,"barSclGift");
    sclBar.setPosition(bar.getX(Align.center),bar.getY(Align.center),Align.center);
    grtop.addActor(sclBar);
    float scl=(float) moment/Config.targetGift;
    if(scl>1)
      scl=1;
    if(scl<=0)
      scl=0;
    sclBar.setScale(scl,1);
    icGift = GUI.createImage(TextureAtlasC.uiAtlas,"giftClose");
    icGift.setSize(icGift.getWidth()*0.3f,icGift.getHeight()*0.3f);
    icGift.setOrigin(Align.center);
    icGift.setPosition(bar.getX(Align.right),bar.getY(Align.center),Align.center);
    grtop.addActor(icGift);
    lbProgress = new Label(moment+"/"+target,new Label.LabelStyle(BitmapFontC.Font_brown_thin,null));
    lbProgress.setFontScale(0.4f);
    lbProgress.setAlignment(Align.center);
    GlyphLayout glProgress = new GlyphLayout(BitmapFontC.Font_brown_thin,lbProgress.getText());
    lbProgress.setSize(glProgress.width*lbProgress.getFontScaleX(),glProgress.height*lbProgress.getFontScaleY());
    lbProgress.setPosition(bar.getX(Align.center),bar.getY(Align.center),Align.center);
    grtop.addActor(lbProgress);
    Image icStar = GUI.createImage(TextureAtlasC.uiAtlas,"star");
    icStar.setSize(icStar.getWidth()*0.8f,icStar.getHeight()*0.8f);
    icStar.setOrigin(Align.center);
    icStar.setPosition(lbProgress.getX(Align.right)+icStar.getWidth(),lbProgress.getY(Align.center),Align.center);
    grtop.addActor(icStar);
//    updateProgress((float) moment/target);

  }
  private void updateProgress(int star,Runnable runnable){
    float scl=(float)count/Config.targetGift;
    if(scl>1)
      scl=1;
    if(scl<=0)
      scl=0;
    sclBar.addAction(Actions.scaleTo(scl,1,dura));
    sclBar.addAction(GSimpleAction.simpleAction((d,a)->{
      tic++;
      if(tic==((dura*60)/Config.targetGift) && up>0){
        tic=0;
        up-=1;
        moment+=1;
        lbProgress.setText(moment+"/"+Config.targetGift);
        System.out.println("up: "+up);

      }else if(up==0) {
        group.addAction(Actions.run(runnable));

        return true;
      }
      return false;
    }));
  }
  private void updateProgressDec(){
//    moment-=Config.targetGift;
    float scl=(float)moment/Config.targetGift;
    if(scl>1)
      scl=1;
    if(scl<=0)
      scl=0;
    sclBar.addAction(Actions.scaleTo(scl,1,dura));
    lbProgress.setText(moment+"/"+Config.targetGift);
  }
  private void updatePref(int star){
    count=moment+star;
    up=star;
    GMain.prefs.putInteger("momentGift",count);
    GMain.prefs.flush();
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

