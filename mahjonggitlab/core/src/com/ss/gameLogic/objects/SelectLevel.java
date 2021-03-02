package com.ss.gameLogic.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.JsonValue;
import com.ss.GMain;
import com.ss.commons.BitmapFontC;
import com.ss.commons.TextureAtlasC;
import com.ss.core.util.GLayer;
import com.ss.core.util.GLayerGroup;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.effects.SoundEffect;
import com.ss.gameLogic.config.Config;
import com.ss.scenes.GameScene;
import com.ss.utils.Utils;

public class SelectLevel {
  private Group       grTable = new Group();
  private Group       grNotice;
  private ScrollPane  scroll;
  private Table       table,tableScroll;
  private JsonValue   arrStrLv[];
  private Label       title;
  private GameScene   gameScene;
  private Group       group;
  private Group       grBoard = new Group();
  private Group       grTimer = new Group();
  private GLayerGroup grCombo = new GLayerGroup();
  private Image       frmStar;
  private Label       lbStar;
  private boolean     isDrag = false;
  private int         s;
  private int         lvPer;
  private int         tmp=0;
  private int         chapter=0;
  private int         padRow=0;
  public  Image       bg,btnBack;


  public SelectLevel(Group group, GameScene gameScene, int lvPer,boolean isContinues,int chapter){
//    System.out.println("check lv Present: "+Config.LvPer);
    this.gameScene = gameScene;
    this.lvPer     = lvPer;
    this.group     = group;
    this.chapter   = chapter;
    setSkin();

    bg = GUI.createImage(TextureAtlasC.BgAtlas,Config.Bg);
    bg.setSize(GStage.getWorldWidth(),GStage.getWorldHeight());
    group.addActor(bg);
    grTable.setSize(GStage.getWorldWidth(),1000);
    grTable.setPosition(GStage.getWorldWidth()/2,GStage.getWorldHeight()/2, Align.center);
    group.addActor(grTable);

    GStage.addToLayer(GLayer.map,grBoard);
    grBoard.setSize(Config.TileW*8,Config.TileH*10);
    grBoard.setPosition(GStage.getWorldWidth()/2,GStage.getWorldHeight()/2,Align.center);
    grBoard.setVisible(false);
    grBoard.setScale(0.84f);
    grBoard.setOrigin(Align.center);

    GStage.addToLayer(GLayer.top,grTimer);
    GStage.addToLayer(GLayer.top,grCombo);

    title = new Label(GMain.locale.get("titleSelectLv"),new Label.LabelStyle(BitmapFontC.Font_Title,null));
    title.setPosition(GStage.getWorldWidth()/2,grTable.getY()-title.getHeight(),Align.center);
    group.addActor(title);

    btnBack = GUI.createImage(TextureAtlasC.uiNotiny,"btnBack");
    btnBack.setPosition(btnBack.getWidth()/2,title.getY(Align.center),Align.center);
    group.addActor(btnBack);
    btnBack.addListener(new ClickListener(){
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        SoundEffect.Play(SoundEffect.click);
        dispose();
        btnBack.clear();
        btnBack.remove();
        disposeBg();
        new SelectChapter(group,gameScene);
        return super.touchDown(event, x, y, pointer, button);
      }
    });
//    readData();
//    loadData();
    arrStrLv = Utils.getLv();

//    System.out.println("check arrStrLv: "+arrStrLv[0]);
    createFrmStar();
    if(isContinues){
      Config.isContinues=false;
      if(Utils.getLv().length<=Config.LvPer){
        grNotice = new Notice(GMain.locale.get("noticeMaxLv"),0.6f,new ClickListener(){
          @Override
          public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            SoundEffect.Play(SoundEffect.click);
            grNotice.clear();
            grNotice.remove();
//            gameScene.setScreen(new StartScene());
            renderListLv(arrStrLv);
            return super.touchDown(event, x, y, pointer, button);
          }
        });
      }else {
        dispose();
        new Board(lvPer-1, arrStrLv,gameScene,grBoard,grTimer,grCombo);
      }
    }else
      renderListLv(arrStrLv);

  }

  private void createFrmStar(){
    int starPre   = 0;
    int starTaget = Utils.getLv().length*3;
    frmStar = GUI.createImage(TextureAtlasC.uiAtlas,"frmStar");
    frmStar.setPosition(GStage.getWorldWidth()/2,grTable.getY(Align.top),Align.bottom);
    group.addActor(frmStar);
    for (int i=1;i<=Config.LvPer;i++){
      starPre+=GMain.prefs.getInteger("starLv"+i);
      GMain.prefs.putInteger("sumStar",starPre);
      GMain.prefs.flush();
    }
    lbStar = new Label(Utils.compressCoin(starPre,1)+"/"+Utils.compressCoin(starTaget,1),new Label.LabelStyle(BitmapFontC.Font_Button,null));
    lbStar.setFontScale(0.5f);
    GlyphLayout GlbStar = new GlyphLayout(BitmapFontC.Font_Button,lbStar.getText());
    lbStar.setSize(GlbStar.width*lbStar.getFontScaleX(),GlbStar.height*lbStar.getFontScaleY());
    lbStar.setAlignment(Align.right);
    lbStar.setPosition(frmStar.getX(Align.center)-lbStar.getWidth()*0.2f,frmStar.getY(Align.center),Align.center);
    group.addActor(lbStar);
  }

  private void readData(){
//    String StrLv = Gdx.files.internal("data/data.txt").readString();
//    arrStrLv = StrLv.split("\n");
//    InterchangeSort(arrStrLv, arrStrLv.length);
//    for (int i=0;i<ArrStrLv.length;i++){
//      String lv = ArrStrLv[i];
//      FileHandle fileLv = new FileHandle("level/"+(i+1));
//      fileLv.writeString(lv,false);
//    }
  }

  public static void Swap(int  a, int b,String Str[]){
    String temp = Str[a];
    Str[a] = Str[b];
    Str[b] = temp;
  }

  public static void InterchangeSort(String a[], int n){
    for (int i = 0; i < n - 1; i++)
      for (int j = i + 1; j < n; j++)
        if(a[i].length() > a[j].length())  //nếu có nghịch thế thì đổi chỗ
          Swap(i, j,a);
  }
private void setSkin(){
  if(chapter==0){
    Config.atlasSKin    = Config.atlasChapter(Config.atlasChapter1);
    Config.Cxl          = Config.cxlchapter1;
    Config.Bg           = Config.bgchapter1;

  }
  if(chapter==1){
    Config.atlasSKin    = Config.atlasChapter(Config.atlasChapter2);
    Config.Cxl          = Config.cxlchapter2;
    Config.Bg           = Config.bgchapter2;

  }
  if(chapter==2){
    Config.atlasSKin    = Config.atlasChapter(Config.atlasChapter3);
    Config.Cxl          = Config.cxlchapter3;
    Config.Bg           = Config.bgchapter3;

  }
  if(chapter==3){
    Config.atlasSKin    = Config.atlasChapter(Config.atlasChapter4);
    Config.Cxl          = Config.cxlchapter4;
    Config.Bg           = Config.bgchapter4;

  }
  if(chapter==4){
    Config.atlasSKin    = Config.atlasChapter(Config.atlasChapter5);
    Config.Cxl          = Config.cxlchapter5;
    Config.Bg           = Config.bgchapter5;

  }
//  System.out.println("check cucxilau: "+Config.Cxl);
}
  private void renderListLv(JsonValue listLv[]){
    int begin=0;
    int end =0;
    if(chapter==0){
      begin=0;
      end=99;
    }
    if(chapter==1){
      begin=99;
      end=199;
    }
    if(chapter==2){
      begin=199;
      end=299;
    }
    if(chapter==3){
      begin=299;
      end=399;
    }
    if(chapter==4){
      begin=399;
      end=listLv.length;
    }

    table = new Table();
    tableScroll = new Table();
    for (int i=begin ; i<end ; i++){
      Group grT = new Group();
//      grT.setTransform(false);
      Image frmStar = GUI.createImage(TextureAtlasC.uiAtlas,"starOff");
      grT.addActor(frmStar);

      Image Tile = GUI.createImage(TextureAtlasC.uiNotiny,"TileLv");
      Tile.setPosition(frmStar.getX(),frmStar.getY()+frmStar.getHeight()*1.1f);
      grT.addActor(Tile);

      Label lb = new Label(""+(i+1),new Label.LabelStyle(BitmapFontC.Font_Title,null));
      lb.setFontScale(0.8f);
      lb.setAlignment(Align.center);
      lb.setPosition(Tile.getX(Align.center),Tile.getY(Align.center),Align.center);
      grT.addActor(lb);

      Image iconLock = GUI.createImage(TextureAtlasC.uiAtlas,"iconlock");
      iconLock.setPosition(Tile.getX(Align.center),Tile.getY(Align.center),Align.center);
      grT.addActor(iconLock);
      iconLock.setVisible(false);

      grT.setSize(Tile.getWidth(),Tile.getHeight()+frmStar.getHeight()*1.1f);
      grT.setScale(1,-1);
      grT.setOrigin(Align.center);
      tableScroll.add(grT).center().pad(grT.getWidth()/2);
      padRow++;
      if(padRow%3==0)
        tableScroll.row();

      if(i> Config.LvPer-1){
        Tile.setColor(Color.DARK_GRAY);
        lb.setColor(Color.DARK_GRAY);
        iconLock.setVisible(true);
      }else {
        int star = GMain.prefs.getInteger("starLv"+(i+1));
        if(star!=0){
          Image icStar = GUI.createImage(TextureAtlasC.uiAtlas,star+"star");
          icStar.setPosition(frmStar.getX(Align.center),frmStar.getY(Align.center),Align.center);
          grT.addActor(icStar);

        }
      }

      /// event select level ////
      int finalI = i;
      grT.addListener(new ClickListener(){
        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
          super.touchUp(event, x, y, pointer, button);
          if(isDrag){
            isDrag=false;
          }else {
            if(iconLock.isVisible()==false){
              SoundEffect.Play(SoundEffect.click);
              dispose();
               new Board(finalI,listLv,gameScene,grBoard,grTimer,grCombo);
            }
          }
        }
      });
      grT.addListener(new DragListener(){
        @Override
        public void dragStart(InputEvent event, float x, float y, int pointer) {
          super.dragStart(event, x, y, pointer);
          isDrag=true;
        }
      });
    }
    tableScroll.align(Align.top);
    scroll = new ScrollPane(tableScroll);
    table.setFillParent(true);
    table.add(scroll).fill().expand();
    grTable.addActor(table);
    grTable.setScale(1,-1);
    grTable.setOrigin(Align.center);

    scroll.layout();
    int lv= lvPer-(lvPer/100)*100;
    lv-=1;
    int row=0;
    if(lv%3==0){
      tmp=0;
      row=(lv/3);
//      scroll.setScrollPercentY((float) ((float)row)/50);
      scroll.setScrollY(row*tableScroll.getRowHeight(0));
    }else {
      tmp++;
      int lvTmp=lv-tmp;
      if(lvTmp%3!=0)
        lvTmp=lv-(tmp*2);
      row=(lvTmp/3);
      scroll.setScrollY(row*tableScroll.getRowHeight(0));
//      scroll.setScrollPercentY((float) ((float)row)/50);
    }
//    System.out.println("check scroll bar hight: "+tableScroll.getRowHeight(0));
  }
  private void dispose(){
    grTable.clear();
    grTable.remove();
    title.clear();
    title.remove();
    frmStar.clear();
    frmStar.remove();
    lbStar.clear();
    lbStar.remove();
    btnBack.clear();
    btnBack.remove();

  }
  private void disposeBg(){
    bg.clear();
    bg.remove();
  }
//  private TextureAtlasC atlasSkin(){
//    if()
//  }


}
