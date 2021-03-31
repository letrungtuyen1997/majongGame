package com.ss.commons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.I18NBundle;
import com.ss.GMain;
import com.ss.core.util.GAssetsManager;

import java.util.Locale;
import java.util.MissingResourceException;

public class BitmapFontC {
  public static BitmapFont font_white;
  public static BitmapFont Font_brown_thin;
  public static BitmapFont Font_Button;
  public static BitmapFont Font_Title;

  public static void LoadBitmapFont(){
    GMain.getAssetManager().loadBitmapFont("font.fnt");
    GMain.getAssetManager().loadBitmapFont("font_brown_thin.fnt");
    GMain.getAssetManager().loadBitmapFont("font_mahjong.fnt");
    GMain.getAssetManager().loadBitmapFont("font_mahjong_gradient.fnt");
    ////// font kr
    GMain.getAssetManager().loadBitmapFont("font_kr.fnt");
    GMain.getAssetManager().loadBitmapFont("font_brown_thin_kr.fnt");
    GMain.getAssetManager().loadBitmapFont("font_mahjong_kr.fnt");
    GMain.getAssetManager().loadBitmapFont("font_mahjong_gradient_kr.fnt");

  }
  public static void InitBitmapFont(){

    if(GMain.locale.get("idLang").equals("Kr")){
      font_white      = GMain.getAssetManager().getBitmapFont("font_kr.fnt");
      Font_brown_thin = GMain.getAssetManager().getBitmapFont("font_brown_thin_kr.fnt");
      Font_Button     = GMain.getAssetManager().getBitmapFont("font_mahjong_kr.fnt");
      Font_Title      = GMain.getAssetManager().getBitmapFont("font_mahjong_gradient_kr.fnt");
    }else {
      font_white      = GMain.getAssetManager().getBitmapFont("font.fnt");
      Font_brown_thin = GMain.getAssetManager().getBitmapFont("font_brown_thin.fnt");
      Font_Button     = GMain.getAssetManager().getBitmapFont("font_mahjong.fnt");
      Font_Title      = GMain.getAssetManager().getBitmapFont("font_mahjong_gradient.fnt");
    }
  }
   
}
