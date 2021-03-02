package com.ss.gameLogic.objects.LevelData;

import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.ss.GMain;
import com.ss.gameLogic.config.Config;

public class ScoreData {
  static final IntMap<Integer> level2Weight; // levelId -> score

  static {
    level2Weight = new IntMap<>();
//    for (int i = 1; i <= 200; i++)
//      level2Weight.put(i,0);

    int lvPerChapter = GMain.platform.GetConfigIntValue("levelPerChapter", LevelData.levelPerChapter);

    JsonReader jReader = new JsonReader();
    JsonValue jValue = jReader.parse(Config.LEVELS_WEIGHT_STRING);

    int numChapter = ((jValue.size - 1) / lvPerChapter) + 1;

    for (int i = 1; i <= jValue.size; i++)
      level2Weight.put(i,jValue.getInt(i + ""));

  }

  public static IntMap<Integer> getLevel2Weight(){
    //level2Weight = get firebase
//    LevelData.sortLevel(level2Weight);
    return level2Weight;
  }

}