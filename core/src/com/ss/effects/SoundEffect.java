package com.ss.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.ss.GMain;
import com.ss.core.util.GAssetsManager;
import com.ss.core.util.GSound;
import com.ss.gdx.NSound;

/* renamed from: com.ss.effect.SoundEffect */
public class SoundEffect {
  public static int MAX_COMMON = 38;
  public static Music bgSound = null;
  public static Music bgSound2 = null;
  public static Music bgSound3 = null;
  public static Sound[] commons = null;
//  public static GSound[] commons = null;
  public static boolean music = false;
  public static boolean mute = false;

  private static Sound[] explode;
  public static int bg            = 1;
  public static int click         = 2;
  public static int clickTile     = 3;
  public static int match         = 4;
  public static int unmatch       = 5;
  public static int win           = 6;
  public static int getStar1      = 7;
  public static int getStar2      = 8;
  public static int getStar3      = 9;
  public static int lose          = 10;
  public static int drop          = 11;
  public static int hint          = 12;
  public static int shuffle       = 13;
  public static int unlock        = 14;
  public static int open        = 15;



  public static void initSound() {
    explode = new Sound[6];
    commons = new Sound[MAX_COMMON];
    commons[click] = GMain.getAssetManager().getSound("click.mp3");
    commons[clickTile] = GMain.getAssetManager().getSound("clickTile.mp3");
    commons[match] = GMain.getAssetManager().getSound("match.mp3");
    commons[unmatch] = GMain.getAssetManager().getSound("unmatch.mp3");
    commons[win] = GMain.getAssetManager().getSound("finishwindow.mp3");
    commons[getStar1] = GMain.getAssetManager().getSound("getstar1.mp3");
    commons[getStar2] = GMain.getAssetManager().getSound("getstar2.mp3");
    commons[getStar3] = GMain.getAssetManager().getSound("getstar3.mp3");
    commons[lose] = GMain.getAssetManager().getSound("lose.mp3");
    commons[drop] = GMain.getAssetManager().getSound("drop.mp3");
    commons[hint] = GMain.getAssetManager().getSound("hint.mp3");
    commons[shuffle] = GMain.getAssetManager().getSound("shuffle.mp3");
    commons[unlock] = GMain.getAssetManager().getSound("unlock2.mp3");
    commons[open] = GMain.getAssetManager().getSound("openGift.mp3");
////        commons[coins] = GMain.getAssetManager().getSound("Coin.mp3");
////        commons[coins].setVolume(2,5);
    bgSound = GMain.getAssetManager().getMusic("bg.mp3");
    for (int i = 0; i < explode.length; i++)
      explode[i] = GMain.getAssetManager().getSound("e" + (i+1) + ".mp3");

  }

  public static void explode(int level) {
    if(!mute)
      explode[(level > 5) ? 5 : level].play();
  }
  public static long Play(int i) {
    long id = -1;
    if (!mute) {
      id = commons[i].play();
      commons[i].setVolume(id,0.5f);
    }
    return id;
  }



  public static void Playmusic() {
    bgSound.play();
    bgSound.setLooping(true);
  }

  public static void Stopmusic(){
    bgSound.stop();
  }
  public static void Pausemusic(){
    bgSound.pause();
  }
}
