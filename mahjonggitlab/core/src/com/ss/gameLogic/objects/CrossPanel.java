package com.ss.gameLogic.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.ss.GMain;
import com.ss.commons.BitmapFontC;
import com.ss.commons.TextureAtlasC;
import com.ss.core.action.exAction.GSimpleAction;
import com.ss.core.exSprite.GShapeSprite;
import com.ss.core.util.GLayer;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.gameLogic.config.C;
import com.ss.gameLogic.config.Config;
import com.ss.gameLogic.objects.crossPanel.HTTPAssetLoader;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.math.Interpolation.linear;
import static com.badlogic.gdx.math.Interpolation.swingOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;


public class CrossPanel {
        private Group group = new Group();
        private Group grBtn = new Group();
        final GShapeSprite WhiteOverLay = new GShapeSprite();
        private Image box;

    public CrossPanel() {
//        SoundEffect.Play(SoundEffect.panel_out);
            GStage.addToLayer(GLayer.top,group);
            WhiteOverLay.createRectangle(true,-GStage.getWorldWidth()/2,-GStage.getWorldHeight()/2, GStage.getWorldWidth()*2, GStage.getWorldHeight()*2);
            WhiteOverLay.setColor(0,0,0,0.7f);
            group.addActor(WhiteOverLay);
//            this.blackScreen = blackScreen;

            Image bgCross = GUI.createImage(TextureAtlasC.uiAtlas, "popup");
            group.setSize(bgCross.getWidth(), bgCross.getHeight());
            group.setOrigin(Align.center);
            group.setPosition(GStage.getWorldWidth()/2 - group.getWidth()/2,
                    GStage.getWorldHeight()/2 - group.getHeight()/2);
            group.addActor(bgCross);

            group.getColor().a = 0f;
            group.setScale(0);
            group.addAction(Actions.parallel(
                    Actions.scaleTo(1,1,0.5f, swingOut),
                    Actions.alpha(1,0.5f)
            ));
//            Image lb = GUI.createImage(TextureAtlasC.uiAtlas,text);
//            lb.setPosition(bgCross.getX()+bgCross.getWidth()/2,bgCross.getY()+bgCross.getHeight()*0.17f,Align.center);
//            group.addActor(lb);
            Label lbTitle = new Label(GMain.locale.get("otherGame"),new Label.LabelStyle(BitmapFontC.Font_Button,null));
            lbTitle.setFontScale(1);
            lbTitle.setAlignment(Align.center);
            lbTitle.setPosition(bgCross.getX()+bgCross.getWidth()/2,bgCross.getY()+bgCross.getHeight()*0.1f,Align.center);
            group.addActor(lbTitle);

            List<Group> lsBoxGames = new ArrayList<>();
            for (int i=0; i<4; i++) {
                Group gBox  = new Group();
                box   = GUI.createImage(TextureAtlasC.uiAtlas, "stroke");
                gBox.setSize(box.getWidth(), box.getHeight());
                gBox.addActor(box);

                if (i <= 1)
                    gBox.setPosition(bgCross.getX()+bgCross.getWidth()/2 - gBox.getWidth()-25 + (box.getWidth() + 55)*i, bgCross.getY() + bgCross.getHeight()*0.3f);
                else
                    gBox.setPosition(bgCross.getX()+bgCross.getWidth()/2 - gBox.getWidth()-25 + (box.getWidth() + 55)*(3-i),
                            bgCross.getY() +  bgCross.getHeight()*0.62f);

                lsBoxGames.add(gBox);
            }

            ArrayList<HTTPAssetLoader.LoadItem> loadItems = new ArrayList<>();
            JsonReader jReader = new JsonReader();
            JsonValue jValue  = jReader.parse(Config.OTHER_GAME_STRING);

            for (JsonValue v : jValue)
                loadItems.add(HTTPAssetLoader.LoadItem.newInst(
                        v.get("id").asInt(),
                        v.get("url").asString(),
                        v.get("display_name").asString(),
                        v.get("android_store_uri").asString(),
                        v.get("ios_store_uri").asString(),
                        v.get("fi_store_uri").asString()
                ));

            HTTPAssetLoader.inst().init(new HTTPAssetLoader.Listener() {

                @Override
                public void finish(ArrayList<HTTPAssetLoader.LoadItem> loadedItems) {

                    for (HTTPAssetLoader.LoadItem item : loadedItems){

                        Group gIcon = lsBoxGames.get(loadedItems.indexOf(item));
                        Image actor = new Image(new TextureRegionDrawable(item.getItemTexture()));
                        actor.setOrigin(Align.center);
                        actor.setSize(box.getWidth()*0.9f, box.getHeight()*0.9f);
                        actor.setScale(1f, -1f);
                        actor.setOrigin(Align.center);
                        actor.setPosition(gIcon.getWidth()/2 - actor.getWidth()/2, gIcon.getHeight()/2-actor.getHeight()/2);
                        gIcon.addActor(actor);
                        gIcon.getChildren().get(0).setZIndex(1000);

                        Label lbName = new Label(item.getDisplayName(), new Label.LabelStyle(BitmapFontC.font_white, null));
                        lbName.setFontScale(.5f);

                        lbName.setWrap(true);
                        lbName.setWidth(gIcon.getWidth());
                        lbName.setAlignment(Align.center);
                        lbName.setPosition(actor.getX() + actor.getWidth()/2 - lbName.getWidth()/2,
                              actor.getY() + actor.getHeight()+ lbName.getHeight()/2 );
                        gIcon.addActor(lbName);
                        group.addActor(gIcon);

                        gIcon.addListener(new ClickListener() {

                            @Override
                            public void clicked(InputEvent event, float x, float y) {
                                super.clicked(event, x, y);
                                Gdx.net.openURI(item.getAndroidStoreURI());
                            }
                        });

                    }
                }

                @Override
                public void error(Throwable e) {
                    e.printStackTrace();
                }
            }, loadItems);
            group.addActor(grBtn);
            btn(grBtn,bgCross.getX() + bgCross.getWidth()/2 , bgCross.getHeight()*1.1f,"btnGreen", GMain.locale.get("btnOk"));

//            Image btnX = GUI.createImage(TextureAtlasC.CrossPanel, "btnGreen");
//            btnX.setOrigin(Align.center);
//            btnX.setPosition(bgCross.getX() + bgCross.getWidth()/2 , bgCross.getHeight()*1.1f,Align.center);
//            group.addActor(btnX);

            grBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);

                    grBtn.setTouchable(Touchable.disabled);
                    animEscape(grBtn);

                }
            });

        }

        private void animEscape(Group btn) {
//            blackScreen.remove();
            btn.setTouchable(Touchable.enabled);
            group.addAction(
                    sequence(
                            parallel(
                                    scaleTo(0f, 0f, .75f, linear),
                                    alpha(0f, .2f, linear)
                            ),
                            GSimpleAction.simpleAction((d, a)->{
                                group.remove();
                                return true;
                            })
                    )
            );
        }
    private void btn(Group grbtn,float x,float y,String type,String lb){
        Image btn = GUI.createImage(TextureAtlasC.uiAtlas,type);
        btn.setPosition(0,0);
        grbtn.addActor(btn);
        Label lbbtn = new Label(lb,new Label.LabelStyle(BitmapFontC.Font_Title,null));
        lbbtn.setFontScale(0.7f);
        lbbtn.setOrigin(Align.center);
        lbbtn.setAlignment(Align.center);
        lbbtn.setPosition(btn.getX()+btn.getWidth()/2,btn.getY()+btn.getHeight()/2,Align.center);
        grbtn.addActor(lbbtn);
        grbtn.setWidth(btn.getWidth());
        grbtn.setHeight(btn.getHeight());
        grbtn.setOrigin(Align.center);
        grbtn.setPosition(x,y,Align.center);
    }

}

