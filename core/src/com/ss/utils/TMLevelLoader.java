//package com.ss.utils;
//
//import com.badlogic.gdx.assets.loaders.FileHandleResolver;
//import com.badlogic.gdx.files.FileHandle;
//import com.badlogic.gdx.utils.Array;
//import com.badlogic.gdx.utils.Json;
//
//import java.util.HashMap;
//
//import game.model.Level;
//import game.model.LevelJson;
//import game.model.TileLevelData;
//import sdk.AbstractLoader;
//
//@SuppressWarnings("unused")
//public class TMLevelLoader extends AbstractLoader<Level> {
//  public TMLevelLoader(FileHandleResolver resolver) {
//    super(resolver);
//  }
//
//  @Override
//  public Level parse(String fileName, FileHandle file, Json json) {
//    LevelJson outputJson  = json.fromJson(LevelJson.class, file);
//    Level level           = new Level();
//    level.name            = file.nameWithoutExtension();
//    level.row             = outputJson.row;
//    level.col             = outputJson.col;
//    level.numPair         = outputJson.numPair;
//    level.items           = outputJson.items;
//    level.levelData       = loadLevelData(outputJson.levelData);
//    return level;
//  }
//
//  private Array<HashMap<String, TileLevelData>> loadLevelData(int[][][] list) {
//    Array<HashMap<String, TileLevelData>> result = new Array<>();
//    for (int[][] layer : list) {
//      HashMap<String, TileLevelData> data = new HashMap<>();
//      for (int i = 0; i < layer.length; i++) {
//        for (int j = 0; j < layer[i].length; j++) {
//          TileLevelData level = new TileLevelData();
//          level.row = i;
//          level.col = j;
//          level.value = layer[i][j];
//          data.put(i + "-" + j, level);
//        }
//      }
//      result.add(data);
//    }
//    return result;
//  }
//}