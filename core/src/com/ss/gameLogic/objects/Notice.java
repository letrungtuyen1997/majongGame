package com.ss.gameLogic.objects;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.ss.commons.BitmapFontC;
import com.ss.commons.TextureAtlasC;
import com.ss.core.exSprite.GShapeSprite;
import com.ss.core.util.GLayer;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;

public class Notice extends Group {
  Notice(String text,float sclTxt, ClickListener event){
//    thisoup this = new thisoup();
    GStage.addToLayer(GLayer.top,this);
    this.setPosition(GStage.getWorldWidth()/2,GStage.getWorldHeight()/2);

    GShapeSprite bg = new GShapeSprite();
    bg.createRectangle(true,-GStage.getWorldWidth()/2,-GStage.getWorldHeight()/2,GStage.getWorldWidth(),GStage.getWorldHeight());
    bg.setColor(0,0,0,0.7f);
    this.addActor(bg);

    Image popup = GUI.createImage(TextureAtlasC.uiAtlas,"popup");
    popup.setPosition(0,0, Align.center);
    this.addActor(popup);
    this.setScale(0);
    this.setOrigin(Align.center);
    this.addAction(Actions.scaleTo(1,1,0.5f, Interpolation.swingOut));

    Label notice = new Label("Notice",new Label.LabelStyle(BitmapFontC.Font_Title,null));
    notice.setFontScale(1.5f);
    notice.setAlignment(Align.center);
    notice.setPosition(0,-popup.getHeight()/2+notice.getHeight(),Align.center);
    this.addActor(notice);

    Label des = new Label(text,new Label.LabelStyle(BitmapFontC.Font_Button,null));
    des.setFontScale(sclTxt);
    des.setWrap(true);
    des.setWidth(popup.getWidth()*0.8f);
    des.setPosition(0,0,Align.center);
    des.setAlignment(Align.center);
    this.addActor(des);
    initButton(0,popup.getHeight()*0.3f,TextureAtlasC.uiAtlas,"btnGreen","ok",BitmapFontC.Font_Button,0.7f,1,this,event);
  }

  private void initButton(float x, float y, TextureAtlas atlas, String kind, String text, BitmapFont bit, float sclText, float sclbtn, Group gr, ClickListener event) {
    Group thisbtn = new Group();
    this.addActor(thisbtn);
    Image btn = GUI.createImage(atlas, kind);
    btn.setSize(btn.getWidth()*sclbtn,btn.getHeight()*sclbtn);
    btn.setOrigin(Align.center);
    thisbtn.addActor(btn);
    Label lbItSp = new Label(text, new Label.LabelStyle(bit, null));
    lbItSp.setFontScale(sclText);
    GlyphLayout glItSp = new GlyphLayout(bit, lbItSp.getText());
    lbItSp.setSize(glItSp.width * lbItSp.getFontScaleX(), glItSp.height * lbItSp.getFontScaleY());
    lbItSp.setPosition(btn.getX() + btn.getWidth() * 0.5f, btn.getY() + btn.getHeight() * 0.4f, Align.center);
    thisbtn.addActor(lbItSp);
    thisbtn.setSize(btn.getWidth(), btn.getHeight());
    thisbtn.setPosition(x, y, Align.center);
    thisbtn.addListener(event);
  }
}
