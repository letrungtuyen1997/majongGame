package com.ss.gameLogic.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Align;
import com.ss.commons.BitmapFontC;
import com.ss.commons.TextureAtlasC;
import com.ss.core.action.exAction.GSimpleAction;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.scenes.gameScene;
import com.ss.utils.Utils;

public class SelectLevel {
  private Group      grTable = new Group();
  private ScrollPane scroll;
  private Table      table,tableScroll;
  private String     ArrStrLv[];
  private Label      title;
  private gameScene  gameScene;
  private Group      group;
  private boolean    isDrag = false;
  private int        s;
  private int        lvPer;

  public SelectLevel(Group group,gameScene gameScene,int lvPer){
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

    renderListLv(ArrStrLv);
//    showfps();




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

      grT.setSize(Tile.getWidth(),Tile.getHeight()+frmStar.getHeight()*1.1f);
      grT.setScale(1,-1);
      grT.setOrigin(Align.center);
      tableScroll.add(grT).center().pad(grT.getWidth()/2);
      int dem = i+1;
      if(dem%3==0)
        tableScroll.row();

      /// event select level ////
      int finalI = i;
      grT.addListener(new ClickListener(){
        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
          super.touchUp(event, x, y, pointer, button);
          if(isDrag){
            isDrag=false;
          }else {
            dispose();
            Board board = new Board(finalI,listLv/*,Utils.GetJsV(listLv[finalI])*/,gameScene);
            new Header(board,finalI,gameScene,listLv);
//            createbtn(board);
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
    scroll.setScrollPercentY((float) ((float)lvPer/2.5f)/10);
  }
  private void dispose(){
    grTable.clear();
    grTable.remove();
    title.clear();
    title.remove();
  }
  private void createbtn(Board board){
    Image btnShuffle = GUI.createImage(TextureAtlasC.uiAtlas,"btnShuffle");
    btnShuffle.setPosition(GStage.getWorldWidth()/2+btnShuffle.getWidth(),btnShuffle.getHeight()/2, Align.center);
    group.addActor(btnShuffle);
    btnShuffle.addListener(new ClickListener(){
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//        board.createlbShuffle();
        board.SkipHint();
        board.shuffleBoard("shuffle",()->{
          System.out.println("shuffle done");
        });
        return super.touchDown(event, x, y, pointer, button);
      }
    });

    Image btnHint = GUI.createImage(TextureAtlasC.uiAtlas,"btnHint");
    btnHint.setPosition(GStage.getWorldWidth()/2-btnHint.getWidth(),btnHint.getHeight()/2, Align.center);
    group.addActor(btnHint);
    btnHint.addListener(new ClickListener(){
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        board.shuffleBoard("hint",()->{
          System.out.println("hint done");
        });
//        board.createlbShuffle();
//        board.autoMatch();
        return super.touchDown(event, x, y, pointer, button);
      }
    });

    Image btnBack = GUI.createImage(TextureAtlasC.uiAtlas,"btnReplay");
    btnBack.setPosition(GStage.getWorldWidth()/2,GStage.getWorldHeight()-btnBack.getHeight()/2,Align.center);
    group.addActor(btnBack);
    btnBack.addListener(new ClickListener(){
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        group.clear();
        group.remove();
        gameScene.setScreen(new gameScene());
        return super.touchDown(event, x, y, pointer, button);
      }
    });
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
