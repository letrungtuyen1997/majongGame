package com.ss.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.ss.GMain;

import java.util.HashMap;


public final class Level {

  public Array<LevelJson> LevelData;

  public Level() {

  }



  private static Level[] defaultLevelData;

  public static int getMaxLevel() {
    return defaultLevelData.length;
  }




  public static Level getLevelData(int level) {
//    String leveldata = GMain.IPlat().GetConfigStringValue("leveldata_"+mode,"");
    String leveldata = "";
//    if (leveldata.equals(""))


    //todo get level from asset loader

    if (level >= 500)
      return GMain.getAssetManager().get(Level.class, 500 + "");
    //todo fire debug event [out of bound level]

    Level res = GMain.getAssetManager().get(Level.class,level + "");
    if (res == null)
      return GMain.getAssetManager().get(Level.class, "1");

    //todo fire debug event [null level]
    return res;

//    if (level > defaultLevelData.length)
//      level--;
//    return defaultLevelData[level - 1];


//    else {
//      try {
//        JsonReader json = new JsonReader();
//        JsonValue list = json.parse(leveldata);
//        Level[] levels = loadLevel(list);
//
//        defaultLevelData[mode] = levels;
//        return levels[level];
//      } catch (Exception e) {
//        return defaultLevelData[mode][level];
//      }
//    }
  }
}
