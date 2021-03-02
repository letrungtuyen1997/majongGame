package com.ss.commons;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.ss.core.util.GAssetsManager;

public class TextureAtlasC {
  public static TextureAtlas uiAtlas;
  public static TextureAtlas AnimalsAtlas;
  public static TextureAtlas MatchuocAtlas;
  public static TextureAtlas NumberAtlas;
  public static TextureAtlas EffectAtlas;
  public static TextureAtlas LeaderAtlas;
  public static TextureAtlas BgAtlas;
  public static TextureAtlas Vegetable;
  public static TextureAtlas Food;
  public static TextureAtlas Bird;
  public static TextureAtlas uiNotiny;

  public static void LoadAtlas(){

    GAssetsManager.loadTextureAtlas("ui.atlas");
    GAssetsManager.loadTextureAtlas("animals.atlas");
    GAssetsManager.loadTextureAtlas("effect.atlas");
    GAssetsManager.loadTextureAtlas("leaderboard.atlas");
    GAssetsManager.loadTextureAtlas("bg.atlas");
    GAssetsManager.loadTextureAtlas("number.atlas");
    GAssetsManager.loadTextureAtlas("matchuoc.atlas");
    GAssetsManager.loadTextureAtlas("vegetable.atlas");
    GAssetsManager.loadTextureAtlas("bird.atlas");
    GAssetsManager.loadTextureAtlas("food.atlas");
    GAssetsManager.loadTextureAtlas("uiNotiny.atlas");
  }

  public static void InitAtlas(){

    uiAtlas         = GAssetsManager.getTextureAtlas("ui.atlas");
    AnimalsAtlas    = GAssetsManager.getTextureAtlas("animals.atlas");
    EffectAtlas     = GAssetsManager.getTextureAtlas("effect.atlas");
    LeaderAtlas     = GAssetsManager.getTextureAtlas("leaderboard.atlas");
    BgAtlas         = GAssetsManager.getTextureAtlas("bg.atlas");
    NumberAtlas     = GAssetsManager.getTextureAtlas("number.atlas");
    MatchuocAtlas   = GAssetsManager.getTextureAtlas("matchuoc.atlas");
    Vegetable       = GAssetsManager.getTextureAtlas("vegetable.atlas");
    Bird            = GAssetsManager.getTextureAtlas("bird.atlas");
    Food            = GAssetsManager.getTextureAtlas("food.atlas");
    uiNotiny        = GAssetsManager.getTextureAtlas("uiNotiny.atlas");

  }
}
