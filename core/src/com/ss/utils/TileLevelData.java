package com.ss.utils;

import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

public class TileLevelData {
  public int row;
  public int col;
  public int value;

  public static Array<HashMap<String, TileLevelData>> loadLevelData(int[][][] list) {
    Array<HashMap<String, TileLevelData>> result = new Array<>();
    for (int[][] layer : list) {
      HashMap<String, TileLevelData> data = new HashMap<>();
      for (int i = 0; i < layer.length; i++) {
        for (int j = 0; j < layer[i].length; j++) {
          TileLevelData level = new TileLevelData();
          level.row = i;
          level.col = j;
          level.value = layer[i][j];
          data.put(i + "-" + j, level);
        }
      }
      result.add(data);
    }
    return result;
  }
}
