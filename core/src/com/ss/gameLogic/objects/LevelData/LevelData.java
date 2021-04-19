package com.ss.gameLogic.objects.LevelData;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.ss.GMain;
import com.ss.gameLogic.config.Config;
import com.ss.utils.Utils;

public class LevelData { //dùng để render level list, query level (theo score)
  public static final int nChapter = 5;
  public static final int levelPerChapter = 100;
  private static IntMap<Array<LevelDto>> sortedLevels;
  private static IntMap<IntMap<LevelDto>> levelMap;

  public static class LevelDto {
    int file;           //id: [1,2,3,4,5,6,..,200] ứng với tên file
    int chapterId;      // chapterId
    int weight;         //score lấy từ firebase để sort

    private LevelDto() {
    }

    private static LevelDto of(int id, int chapterId, int weight) {
      LevelDto dto = new LevelDto();
      dto.file = id;
      dto.chapterId = chapterId;
      dto.weight = weight;
      return dto;
    }

    @Override
    public String toString() {
      return "LevelDto{" +
          "file=" + file +
          ", chapterId=" + chapterId +
          ", score=" + weight +
          '}';
    }

    public int getFile() {
      return file;
    }

    public int getChapterId() {
      return chapterId;
    }


  }

  static {
    sortedLevels = new IntMap<>();
    levelMap = new IntMap<>();


//    for (int chapterId = 1; chapterId <= nChapter; chapterId++) {
//      IntMap<LevelDto> levels = new IntMap<>();
//      for (int levelId = 1; levelId <= levelPerChapter; levelId++) {
//        int id = levelId + (chapterId - 1) * levelPerChapter;
//        levels.put(levelId, LevelDto.of(id, chapterId, id));
//      }
//      levelMap.put(chapterId, levels);
//    }
//
    int lvPerChapter = GMain.platform.GetConfigIntValue("levelPerChapter", levelPerChapter);

    JsonReader jReader = new JsonReader();
//    String level_weight =Gdx.files.internal("data/levels_weight.json").readString();
//    String level_weight =GMain.platform.GetConfigStringValue("levels_weight", Gdx.files.internal("data/levels_weight.json").readString());
    String level_weight = Config.LEVELS_WEIGHT_STRING;
//    System.out.println("check config: "+ Utils.GetJsV(level_weight));

    JsonValue jValue = jReader.parse(level_weight);

    int numChapter = ((jValue.size - 1) / lvPerChapter) + 1;

    for (int chapterId = 1; chapterId <= numChapter; chapterId++) {
      IntMap<LevelDto> levels = new IntMap<>();
      for (int levelId = 1; levelId <= lvPerChapter; levelId++) {
        int id = levelId + (chapterId - 1) * lvPerChapter;
        int weight = jValue.getInt(id + "");
        levels.put(levelId, LevelDto.of(id, chapterId, weight));
      }
      levelMap.put(chapterId, levels);
    }
  }

  public static Array<LevelDto> levelsOfChapter(int chapterId) {
    return sortedLevels.get(chapterId);
  }

  public static int getCountLevel(int chapterId) {
    Array<LevelDto> levels = levelsOfChapter(chapterId);
    return levels.size;
  }

  public static void sortLevel() { //scoreMap: levelId -> score
    //sort level theo score (cao nhất lên đầu), sort cục bộ từng chapter, ko sort liên chapter
    int sort_group_level = Config.SORT_GROUP_LEVEL;
    for (IntMap.Entry<IntMap<LevelDto>> entry : levelMap.entries()) {
      Array<LevelDto> sortLevels = new Array<>();
      Array<LevelDto> sortByGroup = new Array<>();
      int i = 1;
      for (LevelDto levelDto : entry.value.values()) {
        sortByGroup.add(LevelDto.of(levelDto.file, levelDto.chapterId, levelDto.weight));
        if (i >= sort_group_level) {
          sortByGroup.sort((a, b) -> a.weight - b.weight);
          sortLevels.addAll(sortByGroup);
          sortByGroup = new Array<>();
          i = 0;
        }
        i++;
      }
      sortedLevels.put(entry.key, sortLevels);
    }
  }
  public static LevelDto getLevel(int curLevel) {
//    curLevel+=1;
    int chapterId = (curLevel - 1) / levelPerChapter + 1;
    int level = (curLevel - 1) % levelPerChapter;
    Array<LevelDto> levelInChapter = sortedLevels.get(chapterId);
    return levelInChapter.get(level);
  }

  public static int getChapterSize() {
    return levelMap.size;
  }
}