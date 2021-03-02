package com.ss.gameLogic.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.ss.GMain;
import com.ss.commons.BitmapFontC;
import com.ss.commons.TextureAtlasC;
import com.ss.core.util.GLayer;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.effects.SoundEffect;
import com.ss.gameLogic.config.Config;
import com.ss.scenes.GameScene;
import com.ss.scenes.StartScene;

public class SelectChapter {
    private VerticalGroup grV           = new VerticalGroup();
    private Group         groupTitle    = new Group();
    private Group         group;
    private Label         title;
    private Array<Label> arrLbBtn       = new Array<>();
    private Array<Image> arrBtn         = new Array<>();
    private Array<Image> arrIcLock      = new Array<>();
    private Array<Image> arrLbNew       = new Array<>();
    private Group        grGame;
    private GameScene    gameScene;
    private Image        bg;

    public SelectChapter(Group grGame, GameScene gameScene){
        this.grGame     = grGame;
        this.gameScene  = gameScene;
        GStage.addToLayer(GLayer.top,grV);
        GStage.addToLayer(GLayer.top,groupTitle);
        bg = GUI.createImage(TextureAtlasC.uiAtlas,"bg");
        bg.setSize(GStage.getWorldWidth(),GStage.getWorldHeight());
        grGame.addActor(bg);
        renderList();
        setLock();

        title = new Label(GMain.locale.get("lbTitleChapter"),new Label.LabelStyle(BitmapFontC.Font_Title,null));
        title.setPosition(GStage.getWorldWidth()/2,title.getHeight()*3,Align.center);
        groupTitle.addActor(title);

        Image btnBack = GUI.createImage(TextureAtlasC.uiNotiny,"btnBack");
        btnBack.setPosition(btnBack.getWidth()/2,title.getY(Align.center),Align.center);
        groupTitle.addActor(btnBack);
        btnBack.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                SoundEffect.Play(SoundEffect.click);
                dispose();
                btnBack.clear();
                btnBack.remove();
                gameScene.setScreen(new StartScene());
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }
    private void renderList(){
        for (int i=0;i<5;i++){
            int finalI = i;
            group = initButton(0,0,TextureAtlasC.uiAtlas,"btnLv",GMain.locale.get("lbChapter")+(i+1), BitmapFontC.Font_Title,0.7f,1,grV,new ClickListener(){
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    SoundEffect.Play(SoundEffect.click);
                    if(arrIcLock.get(finalI).isVisible()==false){
                        dispose();
//                        setSkin(finalI);
                        new SelectLevel(grGame,gameScene, Config.LvPer,Config.isContinues, finalI);
                    }
                    return super.touchDown(event, x, y, pointer, button);
                }
            });
            group.setScaleY(-1);
            group.setOrigin(Align.center);

        }
        grV.setSize(GStage.getWorldWidth(),((group.getHeight()+20)*5f));
        grV.setScaleY(-1);
        grV.setOrigin(Align.center);
        grV.setPosition(GStage.getWorldWidth()/2,GStage.getWorldHeight()/2, Align.center);
        grV.space(20);

    }
    private Group initButton(float x, float y, TextureAtlas atlas, String kind, String text, BitmapFont bit, float sclText, float sclbtn, Group gr, ClickListener event) {
        Group grbtn = new Group();
        gr.addActor(grbtn);
        Image btn = GUI.createImage(atlas, kind);
        btn.setSize(btn.getWidth()*sclbtn,btn.getHeight()*sclbtn);
        btn.setOrigin(Align.center);
        grbtn.addActor(btn);
        btn.setColor(Color.DARK_GRAY);
        Label lbItSp = new Label(text, new Label.LabelStyle(bit, null));
        lbItSp.setFontScale(sclText);
        GlyphLayout glItSp = new GlyphLayout(bit, lbItSp.getText());
        lbItSp.setSize(glItSp.width * lbItSp.getFontScaleX(), glItSp.height * lbItSp.getFontScaleY());
        lbItSp.setPosition(btn.getX() + btn.getWidth() * 0.5f, btn.getY() + btn.getHeight() * 0.45f, Align.center);
        grbtn.addActor(lbItSp);
        lbItSp.setColor(Color.DARK_GRAY);
        Image icLock = GUI.createImage(TextureAtlasC.uiAtlas,"iconlock");
        icLock.setPosition(btn.getX(Align.center),btn.getY(Align.center),Align.center);
        grbtn.addActor(icLock);
        String kindNew = "new_vi";

        if(GMain.locale.get("idLang").equals("En"))
            kindNew = "new_en";
        Image lbNew = GUI.createImage(TextureAtlasC.uiAtlas,kindNew);
        lbNew.setPosition(btn.getX(Align.right)-lbNew.getWidth()/2-5,btn.getY()+lbNew.getHeight()/2,Align.center);
        grbtn.addActor(lbNew);
        lbNew.setVisible(false);

        grbtn.setSize(btn.getWidth(), btn.getHeight());
        grbtn.setPosition(x, y, Align.center);
        grbtn.addListener(event);

        ///// array add /////
        arrBtn.add(btn);
        arrIcLock.add(icLock);
        arrLbBtn.add(lbItSp);
        arrLbNew.add(lbNew);
        return grbtn;
    }
    private void setLock(){
        int lv =Config.LvPer;
//        int lv =500;
        int size = (lv/100);
        if(size>=arrBtn.size)
            size=arrBtn.size-1;
        for (int i=0;i<=size;i++){
            arrBtn.get(i).setColor(Color.WHITE);
            arrLbBtn.get(i).setColor(Color.WHITE);
            arrIcLock.get(i).setVisible(false);
        }
        if(lv==100||lv==200||lv==300||lv==400||lv==1){
            arrLbNew.get((lv/100)).setVisible(true);
        }
    }

    private void dispose(){
        grV.clear();
        grV.remove();
        groupTitle.clear();
        groupTitle.remove();
        arrLbNew.clear();
        arrIcLock.clear();
        arrBtn.clear();
        arrLbNew.clear();
        bg.clear();
        bg.remove();
    }
}
