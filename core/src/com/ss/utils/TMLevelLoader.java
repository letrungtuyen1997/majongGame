package com.ss.utils;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

@SuppressWarnings("unused")
public class TMLevelLoader extends AbstractLoader<Level> {
  public TMLevelLoader(FileHandleResolver resolver) {
    super(resolver);
  }
  @Override
  public Level parse(String fileName, FileHandle file, Array<LevelJson> arrLv) {
    Level level = new Level();
    level.LevelData = arrLv;
    return level;
  }

}