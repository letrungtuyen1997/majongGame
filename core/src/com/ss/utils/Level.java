//package com.ss.utils;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.files.FileHandle;
//import com.badlogic.gdx.utils.Array;
//import com.badlogic.gdx.utils.Json;
//import com.badlogic.gdx.utils.JsonWriter;
//
//import java.util.HashMap;
//
//
//public final class Level {
//
//  public String name;
//  public int row;
//  public int col;
//  public int numPair;
//  public Array<HashMap<String, TileLevelData>> levelData;
////  public Array<ItemLevelJson> items;
//
//  public Level() {
//
//  }
//
//  /**
//   * @param type      is NONE | SLIDE | PARTITION | SHIFT | ROTATE.
//   * @param direction is VERTICAL | HORIZONTAL
//   * @param anchor    is ANCHOR_START | ROTATE_RIGHT.
//   */
//
//  private Level(int row, int col, int numPair, int time, int direction, int type, int anchor) {
//    this.row = row;
//    this.col = col;
//    this.numPair = numPair;
//  }
//
//  private static Level[] defaultLevelData;
//
//  public static int getMaxLevel() {
//    return defaultLevelData.length;
//  }
//
//
//
//
//  public static Level getLevelData(int level) {
////    String leveldata = GMain.IPlat().GetConfigStringValue("leveldata_"+mode,"");
//    String leveldata = "";
////    if (leveldata.equals(""))
//
//
//    //todo get level from asset loader
//
//    if (level >= model.composite.Level.maxLevel)
//      return JK.asset().get(game.model.Level.class, model.composite.Level.maxLevel + "");
//    //todo fire debug event [out of bound level]
//
//    game.model.Level res = JK.asset().get(game.model.Level.class, level + "");
//    if (res == null)
//      return JK.asset().get(game.model.Level.class, "1");
//
//    //todo fire debug event [null level]
//    return res;
//
////    if (level > defaultLevelData.length)
////      level--;
////    return defaultLevelData[level - 1];
//
//
////    else {
////      try {
////        JsonReader json = new JsonReader();
////        JsonValue list = json.parse(leveldata);
////        Level[] levels = loadLevel(list);
////
////        defaultLevelData[mode] = levels;
////        return levels[level];
////      } catch (Exception e) {
////        return defaultLevelData[mode][level];
////      }
////    }
//  }
//}
