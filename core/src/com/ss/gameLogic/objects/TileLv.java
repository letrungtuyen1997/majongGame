package com.ss.gameLogic.objects;

import com.badlogic.gdx.graphics.Color;
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
import com.ss.core.action.exAction.GSimpleAction;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.effects.SoundEffect;
import com.ss.gameLogic.config.Config;
import com.ss.utils.Utils;

public class TileLv {
    private Group grT = new Group();
    private Image iconLock,frmStar,Tile;
    private SelectLevel selectLevel;
    private int Level;
    private boolean alive = false;
    Label lb;
    private Array<Image> arrStar= new Array<>();
    TileLv(Group group, int j, int i, float paddingX, float paddingY, int Level,int S,SelectLevel selectLevel){
        group.setTransform(false);
        grT.setTransform(false);
        this.selectLevel = selectLevel;
        this.Level = Level;
        if(Level<= Utils.getLv().length){
             frmStar = GUI.createImage(TextureAtlasC.uiAtlas,"starOff");
            grT.addActor(frmStar);

             Tile = GUI.createImage(TextureAtlasC.uiAtlas,"TileLv");
            Tile.setPosition(frmStar.getX(),frmStar.getY()+frmStar.getHeight()*1.1f);
            grT.addActor(Tile);

            lb = new Label(""+Level,new Label.LabelStyle(BitmapFontC.Font_Button,null));
            lb.setFontScale(0.8f);
            lb.setAlignment(Align.center);
            lb.setPosition(Tile.getX(Align.center),Tile.getY(Align.center)-10,Align.center);
            grT.addActor(lb);


            iconLock = GUI.createImage(TextureAtlasC.uiAtlas,"iconlock");
            iconLock.setPosition(Tile.getX(Align.center),Tile.getY(Align.center),Align.center);
            grT.addActor(iconLock);
            iconLock.setVisible(false);


            grT.setSize(Tile.getWidth(),Tile.getHeight()+frmStar.getHeight()*1.1f);
//        grT.setScale(1,-1);
            grT.setOrigin(Align.center);
            group.addActor(grT);
//            grT.setPosition((GStage.getWorldWidth()/2-((grT.getWidth()*paddingX)*((float)Config.colLv/2))+grT.getWidth()*0.25f)+(grT.getWidth()*paddingX)*j+(GStage.getWorldWidth()*S),GStage.getWorldHeight()*0.5f-((grT.getHeight()*paddingY)*(float) Config.rowLv/2-(grT.getHeight()*0.25f))+(grT.getHeight()*paddingY)*i);
//            grT.addAction(Actions.sequence(
//                    Actions.delay(0.1f*i),
//                    Actions.moveBy(-GStage.getWorldWidth()*S,0,0.5f, Interpolation.swingOut),
//                    GSimpleAction.simpleAction((d, a)->{
//                    addevent();
//                        return true;
//                    })
//            ));

//            if(Level> Config.LvPer){
//                Tile.setColor(Color.DARK_GRAY);
//                lb.setColor(Color.DARK_GRAY);
//                iconLock.setVisible(true);
//            }else {
//                int star = GMain.prefs.getInteger("starLv"+(Level));
//                if(star!=0){
//                    Image icStar = GUI.createImage(TextureAtlasC.uiAtlas,star+"star");
//                    icStar.setPosition(frmStar.getX(Align.center),frmStar.getY(Align.center),Align.center);
//                    grT.addActor(icStar);
//
//                }
//            }
//            if(Level%Config.numTileLv==0||Level==Utils.readData().length){
//                selectLevel.SetVisibleBtn(true);
//            }
            for (int ii=1;ii<4;ii++){
                Image icStar = GUI.createImage(TextureAtlasC.uiAtlas,ii+"star");
                icStar.setPosition(frmStar.getX(Align.center),frmStar.getY(Align.center),Align.center);
                grT.addActor(icStar);
                arrStar.add(icStar);
                icStar.setVisible(false);
            }
            setVisible(false);

        }

    }
    public void setVisible(boolean set){
        grT.setVisible(set);
    }
    public void SetPosition(int i,int j,float paddingX,float paddingY,int S){
        setVisible(true);
//        grT.setPosition((GStage.getWorldWidth()/2-((grT.getWidth()*paddingX)*((float)Config.colLv/2))+grT.getWidth()*0.25f)+(grT.getWidth()*paddingX)*j+(GStage.getWorldWidth()*S),GStage.getWorldHeight()*0.5f-((grT.getHeight()*paddingY)*(float) Config.rowLv/2-(grT.getHeight()*0.25f))+(grT.getHeight()*paddingY)*i);
        grT.setPosition((GStage.getWorldWidth()/2-((grT.getWidth()*paddingX)*((float)Config.colLv/2))+grT.getWidth()*0.25f)+(grT.getWidth()*paddingX)*j,GStage.getWorldHeight()*0.5f-((grT.getHeight()*paddingY)*(float) Config.rowLv/2-(grT.getHeight()*0.25f))+(grT.getHeight()*paddingY)*i);
//        grT.addAction(Actions.sequence(
//                Actions.delay(0.1f*i),
//                Actions.moveBy(-GStage.getWorldWidth()*S,0,0.5f, Interpolation.swingOut),
//                GSimpleAction.simpleAction((d, a)->{
                    addevent();
//                    return true;
//                })
//        ));
    }
    public void setLevel(int level){
        Level = level;
        lb.setText(""+Level);

        if(Level> Config.LvPer){
            Tile.setColor(Color.DARK_GRAY);
            lb.setColor(Color.DARK_GRAY);
            iconLock.setVisible(true);
        }else {
            int star = GMain.prefs.getInteger("starLv"+(Level));
            if(star!=0){
               arrStar.get(star-1).setVisible(true);
            }
        }
        if(Level%Config.numTileLv==0||Level==Utils.getLv().length){
//            selectLevel.SetVisibleBtn(true);
        }
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
    public boolean getAlive(){
        return alive;
    }

    private void addevent(){
        grT.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                SoundEffect.Play(SoundEffect.click);
                grT.setTouchable(Touchable.disabled);
                grT.addAction(Actions.sequence(
                        Actions.scaleTo(0.9f,0.9f,0.1f),
                        Actions.scaleTo(1f,1f,0.1f),
                        GSimpleAction.simpleAction((d,a)->{
                            grT.setTouchable(Touchable.enabled);
                            if(iconLock.isVisible()==false){
//                                selectLevel.dispose();
//                                selectLevel.PlayGame(Level-1);
                            }
                            return true;
                        })
                ));
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }
    public void dispose(int S){
//        grT.clear();
//        grT.remove();
//        grT.addAction(Actions.sequence(
//                Actions.moveBy(-GStage.getWorldWidth()*S,0,0.2f, Interpolation.linear),
//                GSimpleAction.simpleAction((d,a)->{
                    grT.setPosition(0,0);
                    setAlive(false);
                    setVisible(false);
                    iconLock.setVisible(false);
                    Tile.setColor(Color.WHITE);
                    lb.setColor(Color.WHITE);
                    for (Image img: arrStar)
                        img.setVisible(false);


//                    grT.clear();
//                    grT.remove();
//                    return true;
//                })
//        ));
    }
}
