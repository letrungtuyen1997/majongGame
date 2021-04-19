package com.ss.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
import com.ss.gameLogic.objects.Effect;
import com.ss.gameLogic.objects.LevelData.LevelData;
import com.ss.utils.Level;
import com.ss.utils.TMLoaderHook;
import com.ss.utils.Utils;

import java.lang.reflect.Array;

public class LoadingScene extends GScreen {
    Group group = new Group();
    private boolean isLoading=true;
    public  static Effect effect;
    private Image loadd,barr;
    private TextureRegion load,bar;

    @Override
    public void dispose() {

    }

    @Override
    public void init() {
        GStage.addToLayer(GLayer.ui,group);
//        GMain.getAsset().addHook(Level.class, new TMLoaderHook());
        GMain.getAssetManager().addHook(Level.class, new TMLoaderHook());
        GMain.getAssetManager().loadExtention();
        loading();
        new PaticleConvert();
        Config.loadjson();
//        Utils.initLoadData();
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
        if (isLoading) {
            if (GMain.getAssetManager().isFinished()) {
                isLoading   = false;
                BitmapFontC.LoadBitmapFont();
                TextureAtlasC.LoadAtlas();
                SoundEffect.initSound();
                LoadParticle.init();
                BitmapFontC.InitBitmapFont();
                TextureAtlasC.InitAtlas();
                effect = new Effect();
                this.setScreen(new StartScene());
            }
            else {
                float per = (GMain.getAssetManager().getProgress()/100)*bar.getRegionWidth();
                load.setRegionWidth((int) per);
                loadd.setWidth(load.getRegionWidth());
//                loadd.setHeight(load.getRegionHeight());
                System.out.println("check: "+GMain.getAssetManager().getProgress());
                GMain.getAssetManager().update();
                GMain.getAssetManager().cache();
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

        bar = new TextureRegion(new Texture("textureAtlas/bar.png"));
        bar.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        load = new TextureRegion(new Texture("textureAtlas/load.png"));
        load.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        load.setRegionX(0);

        barr = new Image(bar);
        barr.setPosition(GStage.getWorldWidth()/2,GStage.getWorldHeight()/2+100,Align.center);
        group.addActor(barr);

        loadd = new Image(load);
        loadd.setPosition(barr.getX(Align.center),barr.getY(Align.center),Align.center);
        loadd.setWidth(load.getRegionWidth());
        loadd.setHeight(load.getRegionHeight());
        group.addActor(loadd);


    }

}
