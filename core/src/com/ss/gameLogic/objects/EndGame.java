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
import com.ss.commons.BitmapFontC;
import com.ss.commons.ButtonC;
import com.ss.commons.TextureAtlasC;
import com.ss.commons.Tweens;
import com.ss.core.action.exAction.GScreenShakeAction;
import com.ss.core.action.exAction.GSimpleAction;
import com.ss.core.exSprite.GShapeSprite;
import com.ss.core.util.GLayer;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.gameLogic.config.Config;

public class EndGame {
  private Group    group       = new Group();
  private String   ribon,title;
  public EndGame(boolean isWin,int star){
    if(isWin){
      ribon = "ribonWin";
      title = "victory";
    }else {
      ribon="ribonFail";
      title = "fail";
    }
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

    Image ribbon = GUI.createImage(TextureAtlasC.uiAtlas,ribon);
    ribbon.setOrigin(Align.center);
    ribbon.setPosition(0,-popup.getHeight()/2+ribbon.getHeight()*0.15f,Align.center);
    group.addActor(ribbon);

    Image Title = GUI.createImage(TextureAtlasC.uiAtlas,title);
    Title.setPosition(ribbon.getX(Align.center),ribbon.getY(Align.center)-ribbon.getHeight()*0.2f,Align.center);
    group.addActor(Title);

    if(isWin){
      aniStar(star,()->{
        Image gift = GUI.createImage(TextureAtlasC.uiAtlas,"giftClose");
        gift.setOrigin(Align.center);
        gift.setPosition(0,gift.getHeight()/2,Align.center);
        group.addActor(gift);
        gift.setScale(0);
        gift.addAction(Actions.sequence(
                Actions.scaleTo(1,1,0.2f),
                GSimpleAction.simpleAction((d,a)->{
                  aniGift(gift);
                  return true;
                })
        ));
      });
      initButton(-popup.getWidth()*0.2f,popup.getHeight()*0.35f,TextureAtlasC.uiAtlas,"btnGreen","Next Level",BitmapFontC.font_white,0.3f,0.8f,group,new ClickListener(){
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
          return super.touchDown(event, x, y, pointer, button);
        }
      });

      initButton(popup.getWidth()*0.2f,popup.getHeight()*0.35f,TextureAtlasC.uiAtlas,"btnWatchAds","Open Gift",BitmapFontC.font_white,0.3f,0.8f,group,new ClickListener(){
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
          return super.touchDown(event, x, y, pointer, button);
        }
      });

    }else {
      for (int i=0;i<3;i++){
        int finalI = i;
        Tweens.setTimeout(group,0.4f*i,()->{
          Image ic = GUI.createImage(TextureAtlasC.uiAtlas,"star"+(finalI +1)+"Off");
          ic.setPosition(-ic.getWidth()*1.5f+ic.getWidth()* finalI,50,Align.topLeft);
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

      initButton(0,popup.getHeight()*0.3f,TextureAtlasC.uiAtlas,"btnGreen","Replay",BitmapFontC.font_white,0.3f,0.8f,group,new ClickListener(){
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
          return super.touchDown(event, x, y, pointer, button);
        }
      });
    }

  }
  private void aniStar(int star,Runnable runnable){
    Array<Image> arrIc = new Array<>();
    for (int i=0;i<3;i++){
      Image ic = GUI.createImage(TextureAtlasC.uiAtlas,"star"+(i+1)+"Off");
      ic.setPosition(-ic.getWidth()*1.5f+ic.getWidth()*i,0,Align.topLeft);
      group.addActor(ic);
      if(i==0)
        ic.setX(ic.getX()-10);
      if(i==2)
        ic.setX(ic.getX()+10);

      arrIc.add(ic);
    }

    for (int i=0;i<star;i++){
      int finalI = i;
      Tweens.setTimeout(group,0.5f*i,()->{
        Image icStar = GUI.createImage(TextureAtlasC.uiAtlas,"star"+(finalI+1)+"On");
        icStar.setScale(5);
        icStar.setOrigin(Align.center);
        icStar.setPosition(arrIc.get(finalI).getX(),arrIc.get(finalI).getY());
        group.addActor(icStar);
        icStar.addAction(Actions.sequence(
                Actions.scaleTo(1,1,0.2f),
                GScreenShakeAction.screenShake1(Config.duraShake,5,group),
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
    lbItSp.setPosition(btn.getX() + btn.getWidth() * 0.5f, btn.getY() + btn.getHeight() * 0.5f, Align.center);
    grbtn.addActor(lbItSp);
    grbtn.setSize(btn.getWidth(), btn.getHeight());
    grbtn.setPosition(x, y, Align.center);
    grbtn.addListener(event);
  }
}
