package com.ss.commons;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.ss.GMain;

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
  public static TextureAtlas whell;

  public static void LoadAtlas(){

    GMain.getAssetManager().loadTextureAtlas("ui.atlas");
    GMain.getAssetManager().loadTextureAtlas("animals.atlas");
    GMain.getAssetManager().loadTextureAtlas("effect.atlas");
    GMain.getAssetManager().loadTextureAtlas("leaderboard.atlas");
    GMain.getAssetManager().loadTextureAtlas("bg.atlas");
    GMain.getAssetManager().loadTextureAtlas("number.atlas");
    GMain.getAssetManager().loadTextureAtlas("matchuoc.atlas");
    GMain.getAssetManager().loadTextureAtlas("vegetable.atlas");
    GMain.getAssetManager().loadTextureAtlas("bird.atlas");
    GMain.getAssetManager().loadTextureAtlas("food.atlas");
    GMain.getAssetManager().loadTextureAtlas("uiNotiny.atlas");
  }

  public static void InitAtlas(){

    uiAtlas         = GMain.getAssetManager().getTextureAtlas("ui.atlas");
    AnimalsAtlas    = GMain.getAssetManager().getTextureAtlas("animals.atlas");
    EffectAtlas     = GMain.getAssetManager().getTextureAtlas("effect.atlas");
    LeaderAtlas     = GMain.getAssetManager().getTextureAtlas("leaderboard.atlas");
    BgAtlas         = GMain.getAssetManager().getTextureAtlas("bg.atlas");
    NumberAtlas     = GMain.getAssetManager().getTextureAtlas("number.atlas");
    MatchuocAtlas   = GMain.getAssetManager().getTextureAtlas("matchuoc.atlas");
    Vegetable       = GMain.getAssetManager().getTextureAtlas("vegetable.atlas");
    Bird            = GMain.getAssetManager().getTextureAtlas("bird.atlas");
    Food            = GMain.getAssetManager().getTextureAtlas("food.atlas");
    uiNotiny        = GMain.getAssetManager().getTextureAtlas("uiNotiny.atlas");
  }
}
