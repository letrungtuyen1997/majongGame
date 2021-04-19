package com.ss.gameLogic.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.ss.GMain;
import com.ss.commons.BitmapFontC;
import com.ss.commons.ShowTextField;
import com.ss.commons.TextureAtlasC;
import com.ss.core.action.exAction.GSimpleAction;
import com.ss.core.exSprite.GShapeSprite;
import com.ss.core.util.GLayer;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.effects.SoundEffect;
import com.ss.repository.HttpLeaderBoard;
import com.ss.utils.Utils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

public class LeaderBoard implements HttpLeaderBoard.GetLeaderBoard {
    public Group                group           = new Group();
    private Group               grScroll        = new Group();
    private GShapeSprite        blackOverlay;
    private Image               Loadding;
    private Table               table,tableScroll;
    private ScrollPane          scroll;
    private  Image              frmRank;
    private HttpLeaderBoard     httpLeaderBoard = new HttpLeaderBoard();
    ShowTextField tfs;
    Label myName;



    public LeaderBoard(){
        httpLeaderBoard.setIGetdata(this);
        GStage.addToLayer(GLayer.top,group);
        Image bg = GUI.createImage(TextureAtlasC.uiAtlas,"bg");
        bg.setSize(GStage.getWorldWidth(),GStage.getWorldHeight());
        group.addActor(bg);
        reanderListView();
//        AwaitData();
    }
    private void reanderListView(){
        frmRank = GUI.createImage(TextureAtlasC.LeaderAtlas,"frmRank");
        frmRank.setPosition(GStage.getWorldWidth()/2,GStage.getWorldHeight()/2, Align.center);
        group.addActor(frmRank);

        grScroll.setSize(frmRank.getWidth()*0.9f,frmRank.getHeight()*0.8f);
        grScroll.setPosition(frmRank.getX(Align.center),frmRank.getY(Align.center)+grScroll.getHeight()*0.08f,Align.center);
        Label title = new Label(GMain.locale.get("lbRank"),new Label.LabelStyle(BitmapFontC.Font_Button,null));
        title.setFontScale(1.2f);
        title.setAlignment(Align.center);
        GlyphLayout glTitle = new GlyphLayout(BitmapFontC.Font_Button,title.getText());
        title.setSize(glTitle.width*title.getFontScaleX(),glTitle.height*title.getFontScaleY());
        title.setPosition(frmRank.getX(Align.center),frmRank.getY()+title.getHeight()*1.3f,Align.center);
        group.addActor(title);

        Image frmMyRank = GUI.createImage(TextureAtlasC.LeaderAtlas,"frmMyRank");
        frmMyRank.setPosition(frmRank.getX(Align.center),frmRank.getY(Align.top)+frmMyRank.getHeight(),Align.center);
        group.addActor(frmMyRank);

        myName = new Label("",new Label.LabelStyle(BitmapFontC.Font_Button,null));
        myName.setText(""+GMain.prefs.getString("name"));

        Image icStar = GUI.createImage(TextureAtlasC.uiAtlas,"star");
        icStar.setPosition(frmMyRank.getX(Align.center)+icStar.getWidth()*2,frmMyRank.getY(Align.center),Align.center);
        group.addActor(icStar);

        Label Star = new Label(""+ Utils.compressCoin(GMain.prefs.getInteger("sumStar"),2),new Label.LabelStyle(BitmapFontC.Font_Button,null));
        Star.setFontScale(0.6f);
        GlyphLayout glStar = new GlyphLayout(BitmapFontC.Font_Button,Star.getText());
        Star.setSize(glStar.width*Star.getFontScaleX(),glStar.height*Star.getFontScaleY());
        Star.setPosition(icStar.getX(Align.right)+20,frmMyRank.getY(Align.center)-Star.getHeight()/2);
        Star.setAlignment(Align.left);
        group.addActor(Star);
        if(GMain.prefs.getString("name").equals("")){
            tfs = new ShowTextField(GMain.locale.get("lbTextFiled"),()->{
                myName.setText(""+GMain.prefs.getString("name"));
                tfs.gr.clear();
                tfs.gr.remove();
                long        id      = GMain.prefs.getLong("id");
                String      name    = GMain.prefs.getString("name");
                long        star    = (long)GMain.prefs.getInteger("sumStar");
                AwaitData();
                httpLeaderBoard.GetLeaderBoard(id,name,star);

            });

        }else {
            long        id      = GMain.prefs.getLong("id");
            String      name    = GMain.prefs.getString("name");
            long        star    = (long)GMain.prefs.getInteger("sumStar");
            AwaitData();
            httpLeaderBoard.GetLeaderBoard(id,name,star);
        }
        myName.setFontScale(0.6f);
        GlyphLayout glName = new GlyphLayout(BitmapFontC.Font_Button,myName.getText());
        myName.setSize(glName.width*myName.getFontScaleX(),glName.height*myName.getFontScaleY());
        myName.setPosition(frmMyRank.getX()+myName.getWidth()/2+10,frmMyRank.getY(Align.center),Align.center);
        myName.setAlignment(Align.left);
        group.addActor(myName);

        Image btnClose = GUI.createImage(TextureAtlasC.LeaderAtlas,"btnClose");
        btnClose.setPosition(frmRank.getX(Align.right)-btnClose.getWidth(),frmRank.getY()+btnClose.getHeight(),Align.center);
        group.addActor(btnClose);
        btnClose.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                SoundEffect.Play(SoundEffect.click);
                group.clear();
                group.remove();
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }

    public static void Swap(int  a, int b,Array<Utils.LeaderBoard> Str){
        Utils.LeaderBoard temp = new Utils.LeaderBoard();
        temp.setName(Str.get(a).getName());
        temp.setStar(Str.get(a).getStar());
        Str.get(a).setName(Str.get(b).getName());
        Str.get(a).setStar(Str.get(b).getStar());
        Str.get(b).setName(temp.getName());
        Str.get(b).setStar(temp.getStar());
//        Str[b] = temp;
    }

    public static void InterchangeSort(Array<Utils.LeaderBoard> a, int n){
        for (int i = 0; i < n - 1; i++)
            for (int j = i + 1; j < n; j++)
                if(a.get(i).getStar() < a.get(j).getStar())  //nếu có nghịch thế thì đổi chỗ
                    Swap(i, j,a);
    }

    private void Lisview(JsonValue data){
        Array<Utils.LeaderBoard> arrdata = new Array<>();
//        Utils.LeaderBoard [] arrdata;
        for (int i=0;i<data.size;i++){
            Utils.LeaderBoard s = new Utils.LeaderBoard();
            s.setName(data.get(i).get("name").asString());
            s.setStar(data.get(i).get("score").asInt());
            arrdata.add(s);
        }
        InterchangeSort(arrdata,arrdata.size);


        tableScroll = new Table();
        table = new Table();
        for (int i=0;i<arrdata.size;i++){
            String dataName = arrdata.get(i).getName();
            int    dataStar = arrdata.get(i).getStar();
            Group grT = new Group();
            grT.setSize(grScroll.getWidth(),80);
            tableScroll.row();
            Label name = new Label((i+1)+". "+dataName,new Label.LabelStyle(BitmapFontC.Font_Button,null));
            name.setFontScale(0.6f);
            GlyphLayout glName = new GlyphLayout(BitmapFontC.Font_Button,name.getText());
            name.setSize(glName.width*name.getFontScaleX(),glName.height*name.getFontScaleY());
            name.setPosition(grT.getX()+name.getWidth()/2,grT.getY(Align.center),Align.center);
            name.setAlignment(Align.left);
            grT.addActor(name);

            Image icStar = GUI.createImage(TextureAtlasC.uiAtlas,"star");
            icStar.setPosition(grT.getX(Align.center)+icStar.getWidth()*2,grT.getY(Align.center),Align.center);
            grT.addActor(icStar);

            Label Star = new Label(""+ Utils.compressCoin(dataStar,2),new Label.LabelStyle(BitmapFontC.Font_Button,null));
            Star.setFontScale(0.6f);
            GlyphLayout glStar = new GlyphLayout(BitmapFontC.Font_Button,Star.getText());
            Star.setSize(glStar.width*Star.getFontScaleX(),glStar.height*Star.getFontScaleY());
            Star.setPosition(icStar.getX(Align.right)+20,grT.getY(Align.center)-Star.getHeight()/2);
            Star.setAlignment(Align.left);
            grT.addActor(Star);
            grT.setScale(1,-1);
            grT.setOrigin(Align.center);
            tableScroll.add(grT);


        }

        tableScroll.align(Align.top);
        scroll = new ScrollPane(tableScroll);
        scroll.updateVisualScroll();
        table.setFillParent(true);
        table.add(scroll).fill().expand();
//        table.add(tableScroll);
        grScroll.addActor(table);
        grScroll.setScale(1,-1);
        grScroll.setOrigin(Align.center);
        group.addActor(grScroll);



    }

    private void AwaitData(){
        blackOverlay = new GShapeSprite();
        blackOverlay.createRectangle(true, 0,0, GStage.getWorldWidth()*2, GStage.getWorldHeight()*2);
        blackOverlay.setColor(0,0,0,0.5f);
        group.addActor(blackOverlay);
        ///////// loadding/////////
        Loadding = GUI.createImage(TextureAtlasC.LeaderAtlas,"loadding");
        Loadding.setOrigin(Align.center);
        Loadding.setPosition(GStage.getWorldWidth()/2,GStage.getWorldHeight()/2,Align.center);
        group.addActor(Loadding);
        aniload(Loadding);
//        System.out.println("here!!!");
    }

    void aniload(Image img){
        img.addAction(Actions.sequence(
                Actions.rotateBy(360,1f),
                GSimpleAction.simpleAction((d, a)->{
                    aniload(img);
                    return true;
                })
        ));
    }
    private void finishLoad(){
        Loadding.clear();
        Loadding.remove();
        blackOverlay.clear();
        blackOverlay.remove();
    }

    private void notice(float x, float y, String notice, Color color) {
        Group gr = new Group();
        GStage.addToLayer(GLayer.top, gr);
        GShapeSprite darkbg = new GShapeSprite();
        darkbg.createRectangle(true, 0, 0, GStage.getWorldWidth(), GStage.getWorldHeight());
        darkbg.setColor(0, 0, 0, 0.7f);
        group.addActor(darkbg);
        ////////// label ///////////
        Label lbnotice = new Label(notice, new Label.LabelStyle(BitmapFontC.font_white, color));
        lbnotice.setFontScale(0.6f);
        lbnotice.setAlignment(Align.center);
        lbnotice.setPosition(0, 0, Align.center);
        gr.addActor(lbnotice);
        gr.setPosition(x, y);
        gr.addAction(Actions.sequence(
                Actions.moveBy(0, -200, 1f),
                GSimpleAction.simpleAction((d, a) -> {
                    darkbg.clear();
                    darkbg.remove();
                    gr.clear();
                    gr.remove();
                    return true;
                })
        ));
    }

    @Override
    public void getLeaderBoard(JsonValue data) {
        finishLoad();
//        System.out.println("data: "+data);
        Lisview(data);
    }

    @Override
    public void Fail(String s) {
        finishLoad();
        notice(GStage.getWorldWidth()/2,GStage.getWorldHeight()/2,"lỗi mạng!!",Color.RED);

    }
}
