package com.ss.gameLogic.config;

import com.badlogic.gdx.Gdx;
import com.ss.GMain;
import com.ss.core.util.GStage;
public class Config {
    public static float ScreenW = GStage.getWorldWidth();
    public static float ScreenH = GStage.getWorldHeight();
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
    public static float         paddingX            = 3;
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
    public static float         perArcY             = 0;
    public static float         DurMoveTile         = 5;
    public static int           MoveOut             = 150;
    public static int           LvPer               = GMain.prefs.getInteger("LvPre",1);
    public static int           Time                = 500;
    public static int           PercentTime         = 1000;
    public static int           MaxLvtime           = 400;
    public static boolean       isContinues         = false;
    public static int           ItSpHint            = GMain.prefs.getInteger("hint",2);
    public static int           ItSpShuffle         = GMain.prefs.getInteger("shuffle",2);
    public static int           ItSpBomb            = GMain.prefs.getInteger("bomb",200);
    public static int           RewardHint          = 1;
    public static int           RewardShuffle       = 1;
    public static int           RewardBomb          = 1;



}

