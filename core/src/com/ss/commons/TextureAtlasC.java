package com.ss.commons;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.ss.core.util.GAssetsManager;

public class TextureAtlasC {
  public static TextureAtlas uiAtlas;
  public static TextureAtlas AnimalsAtlas;
  public static TextureAtlas effectAtlas;

  public static void initAtlas(){

    uiAtlas       = GAssetsManager.getTextureAtlas("ui.atlas");
    AnimalsAtlas  = GAssetsManager.getTextureAtlas("animals.atlas");
    effectAtlas   = GAssetsManager.getTextureAtlas("effect.atlas");
  }
}
