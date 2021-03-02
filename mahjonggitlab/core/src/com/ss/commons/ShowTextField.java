package com.ss.commons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;
import com.ss.GMain;
import com.ss.core.action.exAction.GSimpleAction;
import com.ss.core.exSprite.GShapeSprite;
import com.ss.core.util.GLayer;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;


public class ShowTextField {
    public  TextField textField;
    public  Group       gr          = new Group();
    public  Group       ChilGr      = new Group();
    private Runnable    callback;
    public ShowTextField(String title,Runnable callback){
        this.callback = callback;
        ShowTextField(title);
    }

    public  void ShowTextField(String title){
        GStage.addToLayer(GLayer.top,gr);
        GShapeSprite darkbg = new GShapeSprite();
        darkbg.createRectangle(true,0,0,GStage.getWorldWidth(),GStage.getWorldHeight());
        darkbg.setColor(0,0,0,0.8f);
        gr.addActor(darkbg);
       TextField.TextFieldStyle tfs = new TextField.TextFieldStyle();
        tfs.font = BitmapFontC.Font_brown_thin;
        tfs.fontColor = Color.WHITE;
        tfs.background = new TextureRegionDrawable(TextureAtlasC.LeaderAtlas.findRegion("textbox"));
        tfs.background.setLeftWidth(tfs.background.getLeftWidth() +10);
        tfs.background.setRightWidth(tfs.background.getRightWidth() +15);
        tfs.cursor = new TextureRegionDrawable(TextureAtlasC.LeaderAtlas.findRegion("cursor"));
        /////////// textfield megaID/////////
        Label lb = new Label(title,new Label.LabelStyle(BitmapFontC.font_white,null));
        lb.setFontScale(1f);
        GlyphLayout lGlb = new GlyphLayout(BitmapFontC.font_white,lb.getText());
        lb.setSize(lGlb.width*lb.getFontScaleX(),lGlb.height*lb.getFontScaleY());
        lb.setAlignment(Align.center);
        lb.setPosition(GStage.getWorldWidth()/2,GStage.getWorldHeight()/3,Align.center);
        gr.addActor(lb);
        textField = new TextField("", tfs);
        textField.setWidth(tfs.background.getMinWidth());
        textField.setHeight(tfs.background.getMinHeight());
        textField.getStyle().font.getData().setScale(0.8f);
        textField.setAlignment(Align.center);
        ChilGr.addActor(textField);
        ChilGr.setSize(textField.getWidth(),textField.getHeight());
        ChilGr.setOrigin(Align.center);
        ChilGr.setPosition(GStage.getWorldWidth()/2,lb.getY()+lb.getHeight()*2f+ChilGr.getHeight()/2,Align.center);
        gr.addActor(ChilGr);
        textField.setTextFieldFilter((textField, c) -> Character.toString(c).matches("^[a-zA-Z0-9 _ÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ]"));
        TextField.OnscreenKeyboard keyboard = visible -> {
            textField.setTouchable(Touchable.disabled);
            Gdx.input.getTextInput(new Input.TextInputListener() {
                @Override
                public void input(String text) {
                    textField.setText(text);
                    textField.setTouchable(Touchable.enabled);
                }

                @Override
                public void canceled() {
                    textField.setTouchable(Touchable.enabled);
                }
            }, "", textField.getText(), "");

        };
        textField.setOnscreenKeyboard(keyboard);

        initButton(GStage.getWorldWidth()/2,ChilGr.getY(Align.top)+100,TextureAtlasC.uiAtlas,"btnGreen", GMain.locale.get("btnOk"),BitmapFontC.Font_Title,0.5f,1,gr,new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (textField.getText().equals("")) {
                    notice(GStage.getWorldWidth()/2,GStage.getWorldHeight()/2,GMain.locale.get("lbNameNull"),Color.RED);
                }else if(textField.getText().length()>12) {
                    notice(GStage.getWorldWidth()/2,GStage.getWorldHeight()/2,GMain.locale.get("lbNameFull"),Color.RED);
                }else {
                    gr.addAction(Actions.run(callback));
                    GMain.prefs.putString("name",textField.getText());
                    GMain.prefs.putLong("id", TimeUtils.millis());
                    GMain.prefs.flush();

                }
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
    private void notice(float x, float y, String notice, Color color) {
        Group gr = new Group();
        GStage.addToLayer(GLayer.top, gr);
        GShapeSprite darkbg = new GShapeSprite();
        darkbg.createRectangle(true, -GStage.getWorldWidth()/2, -GStage.getWorldHeight()/2, GStage.getWorldWidth(), GStage.getWorldHeight()*1.5f);
        darkbg.setColor(0, 0, 0, 0.7f);
        gr.addActor(darkbg);
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
}
