package com.ss.gameLogic.config;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.ss.GMain;
import com.ss.commons.TextureAtlasC;
import com.ss.core.util.GStage;
import com.ss.utils.Utils;

public class Config {
    public static float ScreenW = GStage.getWorldWidth();
    public static float ScreenH = GStage.getWorldHeight();
    public static int           targetGift          = 15;
    public static boolean       checkConnet         = true;
    public static boolean       checkWheel          = false;
    public static String        megaID              ="10001";
    public static String        token               ="";
    public static int           veloccity           =3;
    public static String        uri                 = Gdx.files.internal("uri/uri.txt").readString();
    public static boolean       remoteEffect        = Boolean.parseBoolean(Gdx.files.internal("uri/effect.txt").readString());
    public static int           condi_merge         = Integer.parseInt(Gdx.files.internal("uri/condi_merge.txt").readString());
    public static int           row                 = 10;
    public static int           col                 = 10;
    public static int           maxShuffle          = 20;
    public static float         paddingX            = 0;
    public static float         paddingY            = 3;
    public static float         DuraMove            = 0.5f;
    public static float         ShakeX              =5;
    public static float         DuraShake           =0.2f;
    public static float         DuraHint            =0.2f;
    public static float         DuraSelect          =0.1f;
    public static int           QuanAutomatch       = 5;
    public static float         TileW               = 93;
    public static float         TileH               = 120;
    public static float         PerArcX             = 1;
    public static float         PerArcY             = 0;
    public static float         DurMoveTile         = 5;
    public static int           MoveOut             = 150;
    public static int           LvPer               = GMain.prefs.getInteger("LvPre",1);
    public static int           Time                = 500;
    public static int           PercentTime         = 1000;
    public static int           MaxLvtime           = 400;
    public static boolean       isContinues         = false;
    public static int           ItSpHint            = GMain.prefs.getInteger("hint",1);
    public static int           ItSpShuffle         = GMain.prefs.getInteger("shuffle",1);
    public static int           ItSpBomb            = GMain.prefs.getInteger("bomb",1);
    public static int           RewardHint          = 1;
    public static int           RewardShuffle       = 1;
    public static int           RewardBomb          = 1;
    public static int           Combo               = 0;
    public static float         TimeCombo           = 1.5f;
    public static float         PaddingAdsBaner     = 125f;
    public static int           countShowAds        = 3;
    public static int           countTimePlay       = 0;
    public static int           rowLv               = 4;
    public static int           colLv               = 3;
    public static int           SORT_GROUP_LEVEL    = 5;
    public static int           MaxLevel            = 500;
    public static int           numTileLv           = rowLv*colLv;
    public static TextureAtlas  atlasSKin           = TextureAtlasC.MatchuocAtlas;
    public static String        Cxl                 = "cucxilau1";
    public static String        Bg                  = "bg1";
    public static String        OTHER_GAME_STRING   =GMain.platform.GetConfigStringValue("crosspanel", Gdx.files.internal("data/other_games.json").readString());
    public static String        LEVELS_WEIGHT_STRING= GMain.platform.GetConfigStringValue("levels_weight", Gdx.files.internal("data/levels_weight.json").readString());
    public static int           atlasChapter1       =GMain.platform.GetConfigIntValue("atlaschapter1",1);
    public static int           atlasChapter2       =GMain.platform.GetConfigIntValue("atlaschapter2",2);
    public static int           atlasChapter3       =GMain.platform.GetConfigIntValue("atlaschapter3",3);
    public static int           atlasChapter4       =GMain.platform.GetConfigIntValue("atlaschapter4",4);
    public static int           atlasChapter5       =GMain.platform.GetConfigIntValue("atlaschapter5",5);
    public static String        cxlchapter1         =GMain.platform.GetConfigStringValue("cxlchapter1","cucxilau");
    public static String        cxlchapter2         =GMain.platform.GetConfigStringValue("cxlchapter2","cucxilau");
    public static String        cxlchapter3         =GMain.platform.GetConfigStringValue("cxlchapter3","cucxilau");
    public static String        cxlchapter4         =GMain.platform.GetConfigStringValue("cxlchapter4","cucxilau");
    public static String        cxlchapter5         =GMain.platform.GetConfigStringValue("cxlchapter5","cucxilau1");
    public static String        bgchapter1          =GMain.platform.GetConfigStringValue("bgchapter1","bg1");
    public static String        bgchapter2          =GMain.platform.GetConfigStringValue("bgchapter2","bg2");
    public static String        bgchapter3          =GMain.platform.GetConfigStringValue("bgchapter3","bg3");
    public static String        bgchapter4          =GMain.platform.GetConfigStringValue("bgchapter4","bg4");
    public static String        bgchapter5          =GMain.platform.GetConfigStringValue("bgchapter5","bg5");

    public static TextureAtlas  atlasChapter(int atlas){
        if(atlas==1)
            return TextureAtlasC.NumberAtlas;
        if(atlas==2)
            return TextureAtlasC.Food;
        if(atlas==3)
            return TextureAtlasC.Vegetable;
        if(atlas==4)
            return TextureAtlasC.Bird;
        if(atlas==5)
            return TextureAtlasC.MatchuocAtlas;
        return TextureAtlasC.MatchuocAtlas;
    }
    public static enum NameEvent{
        START,
        COMPLETE,
        FAIL
    }
    public static void setSkin(int level){
        level+=1;
//        System.out.println("check skin here lv: "+level+"----"+(level/500));
        if(level>0 &&level<100){
            Config.atlasSKin    = Config.atlasChapter(Config.atlasChapter1);
            Config.Cxl          = Config.cxlchapter1;
            Config.Bg           = Config.bgchapter1;

        }
        if(level>=100 &&level<200){
            Config.atlasSKin    = Config.atlasChapter(Config.atlasChapter2);
            Config.Cxl          = Config.cxlchapter2;
            Config.Bg           = Config.bgchapter2;
//            System.out.println("chuyen skin 2");

        }
        if(level>=200 &&level<300){
            Config.atlasSKin    = Config.atlasChapter(Config.atlasChapter3);
            Config.Cxl          = Config.cxlchapter3;
            Config.Bg           = Config.bgchapter3;

        }
        if(level>=300 &&level<400){
            Config.atlasSKin    = Config.atlasChapter(Config.atlasChapter4);
            Config.Cxl          = Config.cxlchapter4;
            Config.Bg           = Config.bgchapter4;

        }
        if(level>=400 &&level<Config.MaxLevel){
            Config.atlasSKin    = Config.atlasChapter(Config.atlasChapter5);
            Config.Cxl          = Config.cxlchapter5;
            Config.Bg           = Config.bgchapter5;

        }
//  System.out.println("check cucxilau: "+Config.Cxl);
    }






    public static void loadjson(){
        FileHandle js = Gdx.files.internal("data/ConfigData.json");
        String jsonStr = js.readString();
        String jv2 =GMain.platform.GetConfigStringValue("config",jsonStr);
//        System.out.println("log: "+jv2);
        JsonReader json = new JsonReader();
        JsonValue jv = null;
        try {
            jv = json.parse(jv2);
//            System.out.println("log:"+jv.get("paddingX").asInt());
        }catch (Exception e){
            jv = json.parse(jsonStr);
        }
        paddingX            = jv.get("paddingX").asInt();
        paddingY            = jv.get("paddingY").asFloat();
        DuraMove            = jv.get("DuraMove").asFloat();
        DuraShake           = jv.get("DuraShake").asFloat();
        DuraHint            = jv.get("DuraHint").asFloat();
        DuraSelect          = jv.get("DuraSelect").asFloat();
        QuanAutomatch       = jv.get("QuanAutoMatch").asInt();
        PerArcX             = jv.get("PerArcX").asFloat();
        PerArcY             = jv.get("PerArcY").asFloat();
        DurMoveTile         = jv.get("DurMoveTile").asLong();
        MoveOut             = jv.get("MoveOut").asInt();
        Time                = jv.get("Time").asInt();
        PercentTime         = jv.get("PercentTime").asInt();
        MaxLvtime           = jv.get("MaxLvTime").asInt();
        ItSpHint            = GMain.prefs.getInteger("hint",jv.get("ItSpHint").asInt());
        ItSpShuffle         = GMain.prefs.getInteger("shuffle",jv.get("ItSpShuffle").asInt());
        ItSpBomb            = GMain.prefs.getInteger("bomb",jv.get("ItSpBomb").asInt());
        RewardHint          = jv.get("RewardHint").asInt();
        RewardShuffle       = jv.get("RewardShuffle").asInt();
        RewardBomb          = jv.get("RewardBomb").asInt();
        TimeCombo           = jv.get("TimeCombo").asFloat();
        countShowAds        = jv.get("countShowAds").asInt();
        SORT_GROUP_LEVEL    = jv.get("sort_group_level").asInt();
        targetGift          = jv.get("targetGift").asInt();

    }

}

