package com.ss.commons;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.ss.core.util.GAssetsManager;

public class BitmapFontC {
  public static BitmapFont font_white;
  public static BitmapFont Font_brown_thin;
  public static BitmapFont Font_Button;
  public static BitmapFont Font_Title;

  public static void LoadBitmapFont(){
  GAssetsManager.loadBitmapFont("font.fnt");
  GAssetsManager.loadBitmapFont("font_brown_thin.fnt");
  GAssetsManager.loadBitmapFont("font_mahjong.fnt");
  GAssetsManager.loadBitmapFont("font_mahjong_gradient.fnt");
  }
  public static void InitBitmapFont(){
    font_white      = GAssetsManager.getBitmapFont("font.fnt");
    Font_brown_thin = GAssetsManager.getBitmapFont("font_brown_thin.fnt");
    Font_Button     = GAssetsManager.getBitmapFont("font_mahjong.fnt");
    Font_Title      = GAssetsManager.getBitmapFont("font_mahjong_gradient.fnt");
  }
}
