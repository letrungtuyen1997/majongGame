package com.ss.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.ss.GMain;
import com.ss.PoolTile;
import com.ss.commons.BitmapFontC;
import com.ss.commons.LoadParticle;
import com.ss.commons.PaticleConvert;
import com.ss.commons.TextureAtlasC;
import com.ss.core.action.exAction.GSimpleAction;
import com.ss.core.util.GLayer;
import com.ss.core.util.GScreen;
import com.ss.core.util.GStage;
import com.ss.effects.SoundEffect;
import com.ss.gameLogic.config.Config;
import com.ss.gameLogic.objects.LevelData.LevelData;
import com.ss.utils.Utils;

public class LoadingScene extends GScreen {
    Group group = new Group();

    @Override
    public void dispose() {

    }

    @Override
    public void init() {
        GStage.addToLayer(GLayer.ui,group);
        loading();
        new PaticleConvert();

        Config.loadjson();
        Utils.initLoadData();
        LevelData.sortLevel();




    }
    static boolean firstShow = false;
    @Override
    public void show() {
        super.show();
            if(firstShow == false) {
                firstShow = true;
                GMain.platform.Onshow();
            }
    }

    float waitTime = 3;
    @Override
    public void run() {
//        GMain.getAssetManager().getAssetManager().getLoadedAssets()
//         System.out.println("run: "+  GMain.getAssetManager().getAssetManager().getProgress());
         waitTime-= Gdx.graphics.getDeltaTime();
         if(waitTime<=0) {
             if (!GMain.getAssetManager().isFinished()) {
//                 System.out.println("load");
                 GMain.getAssetManager().update();
//                 GMain.getAssetManager().getAssetManager().getProgress();
             }
             else {
                 BitmapFontC.LoadBitmapFont();
                 TextureAtlasC.LoadAtlas();
                 SoundEffect.initSound();
                 LoadParticle.init();
                 BitmapFontC.InitBitmapFont();
                 TextureAtlasC.InitAtlas();
                 //new PoolTile();
                 this.setScreen(new StartScene());
//                 this.setScreen(new WheelScene());
//                 System.out.println("chuyen");
             }
         }

    }
    void loading(){
        Texture img = new Texture("textureAtlas/bgload.png");
        img.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Image bg = new Image(new Texture("textureAtlas/bgload.png"));
        bg.setSize(GStage.getWorldWidth(), GStage.getWorldHeight());
        bg.setScale(1,-1);
        bg.setOrigin(Align.center);
        group.addActor(bg);
        Image load = new Image(new Texture("textureAtlas/loadding.png"));
        load.setOrigin(Align.center);
        load.setPosition(GStage.getWorldWidth()/2, GStage.getWorldHeight()/2+100, Align.center);
        group.addActor(load);
        aniload(load);
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
}
