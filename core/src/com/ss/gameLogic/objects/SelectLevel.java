package com.ss.gameLogic.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Align;
import com.ss.GMain;
import com.ss.commons.BitmapFontC;
import com.ss.commons.TextureAtlasC;
import com.ss.core.action.exAction.GSimpleAction;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.gameLogic.config.Config;
import com.ss.scenes.GameScene;

public class SelectLevel {
  private Group      grTable = new Group();
  private ScrollPane scroll;
  private Table      table,tableScroll;
  private String     ArrStrLv[];
  private Label      title;
  private GameScene gameScene;
  private Group      group;
  private boolean    isDrag = false;
  private int        s;
  private int        lvPer;

  public SelectLevel(Group group, GameScene gameScene, int lvPer,boolean isContinues){
    System.out.println("check lv Present: "+Config.LvPer);
    this.gameScene = gameScene;
    this.lvPer     = lvPer;
    this.group = group;
    grTable.setSize(GStage.getWorldWidth(),1000);
    grTable.setPosition(GStage.getWorldWidth()/2,GStage.getWorldHeight()/2, Align.center);
    group.addActor(grTable);

    title = new Label("Select Level",new Label.LabelStyle(BitmapFontC.FontAlert,null));
    title.setPosition(GStage.getWorldWidth()/2,grTable.getY()-100,Align.center);
    group.addActor(title);
    readData();

    if(isContinues){
      dispose();
      Board board = new Board(lvPer-1,ArrStrLv,gameScene);
      new Header(board,lvPer-1,gameScene,ArrStrLv);
    }else
      renderListLv(ArrStrLv);

  }
  private void readData(){
    String StrLv = Gdx.files.internal("data/data.txt").readString();
    ArrStrLv     = StrLv.split("\n");}

  private void renderListLv(String listLv[]){


    table = new Table();
    tableScroll = new Table();
    for (int i=0 ; i<listLv.length ; i++){
      Group grT = new Group();
      Image frmStar = GUI.createImage(TextureAtlasC.uiAtlas,"starOff");
      grT.addActor(frmStar);

      Image Tile = GUI.createImage(TextureAtlasC.uiAtlas,"TileLv");
      Tile.setPosition(frmStar.getX(),frmStar.getY()+frmStar.getHeight()*1.1f);
      grT.addActor(Tile);

      Label lb = new Label(""+(i+1),new Label.LabelStyle(BitmapFontC.FontAlert,null));
      lb.setFontScale(0.8f);
      lb.setAlignment(Align.center);
      lb.setPosition(Tile.getX(Align.center),Tile.getY(Align.center)-10,Align.center);
      grT.addActor(lb);


      Image iconLock = GUI.createImage(TextureAtlasC.uiAtlas,"iconlock");
      iconLock.setPosition(Tile.getX(Align.center),Tile.getY(Align.center),Align.center);
      grT.addActor(iconLock);
      iconLock.setVisible(false);


      grT.setSize(Tile.getWidth(),Tile.getHeight()+frmStar.getHeight()*1.1f);
      grT.setScale(1,-1);
      grT.setOrigin(Align.center);
      tableScroll.add(grT).center().pad(grT.getWidth()/2);
      int dem = i+1;
      if(dem%3==0)
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
              dispose();
              Board board = new Board(finalI,listLv,gameScene);
              new Header(board,finalI,gameScene,listLv);
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
    int lv= lvPer;
    lv-=1;
    int row=0;
    if(lv%3==0){
      row=(lv/3);
      scroll.setScrollPercentY((float) ((float)row)/10);
    }
  }
  private void dispose(){
    grTable.clear();
    grTable.remove();
    title.clear();
    title.remove();
  }
  private void showfps(){
    s= Gdx.graphics.getFramesPerSecond();
    Label fps = new Label("fps: "+s,new Label.LabelStyle(BitmapFontC.font_white,null));
    fps.setPosition(100,100);
    group.addActor(fps);
    group.addAction(GSimpleAction.simpleAction((d, a)->{
      s= Gdx.graphics.getFramesPerSecond();
      fps.setText("fps: "+s);
      return false;
    }));
  }
}
