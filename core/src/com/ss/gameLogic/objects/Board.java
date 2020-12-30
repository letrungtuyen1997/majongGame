package com.ss.gameLogic.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.ss.commons.BitmapFontC;
import com.ss.commons.Tweens;
import com.ss.core.action.exAction.GSimpleAction;
import com.ss.core.util.GLayer;
import com.ss.core.util.GStage;
import com.ss.gameLogic.config.Config;
import com.ss.scenes.gameScene;
import com.ss.utils.Utils;

public class Board {
  private Array<Tile> arrTile;
  private Array<Tile>                 arrTileBoard       = new Array<>();
  private Group                       group              = new Group();
  private Array<Tile>                 arrCompare         = new Array<>();
  private Array<Integer>              arrTileShuffle     = new Array<>();
  private Array<Tile>                 arrTileHint        = new Array<>();
  private Array<Tile>                 arrTileautoMatch   = new Array<>();
  private Array<Tile>                 arrTileCanHint     = new Array<>();
  private Label                       lbShuffle;
  private int                         s;
  private gameScene                   gameScene;
  private int                         count              = 0;
  private int                         Level              = 0;

  public Board(int lv, String Lv[], gameScene gameScene){
    this.gameScene = gameScene;
    this.Level     = lv;
    GStage.addToLayer(GLayer.map,group);
    group.setSize(Config.TileW*8,Config.TileH*10);
    group.setPosition(GStage.getWorldWidth()/2-group.getWidth()/2,GStage.getWorldHeight()/2-group.getHeight()/2);
//    group.debug();
    createBoard(Utils.GetJsV(Lv[lv]));
    setLockTile();
    shuffleBoard("begin",()->{
      System.out.println("begin done!");
    });
    showfps();


  }
  private void createBoard(JsonValue Arrjv ){
    int index=0;
    for (JsonValue jv : Arrjv){
      Tile t = new Tile(group,this);
      t.setRowCol(jv.get("row").asInt(),jv.get("col").asInt());
      t.setLayer2(jv.get("layer").asInt());
      t.setPos(jv.get("x").asFloat(),jv.get("y").asFloat(),jv.get("kind").asInt(),true,index);
      t.createID(jv.get("id").asInt());
      arrTileBoard.add(t);
      index++;
    }
  }
  public void setLockTile(){
    setDefault();
    for (int i=0;i<arrTileBoard.size;i++){
      if(setLock(arrTileBoard.get(i))){
        arrTileBoard.get(i).setColor(Color.DARK_GRAY);
      }
      System.out.println("check: "+arrTileBoard.get(i).getRowCol().x+"___"+arrTileBoard.get(i).getRowCol().y+"__"+arrTileBoard.get(i).getKind());
    }

  }
  public void setDefault(){
    System.out.println("arr board tile when setColor: "+arrTileBoard.size );
    for (int i=0;i<arrTileBoard.size;i++){
       arrTileBoard.get(i).setColor(Color.WHITE);

    }

  }

  private boolean setLock(Tile tile){
    if(checkLayer(tile)==1 || checkLock(tile)==1)
      return true;
    return false;
  }
  private int checkLock(Tile t){
    int row   = (int)t.getRowCol().x;
    int col   = (int)t.getRowCol().y;
    int Layer = t.getLayer();
    //Label TH1:
    if(findTile(row,col-1,Layer,1)!=null && findTile(row,col+1,Layer,1)!=null && t.getKind()==1 )
      return 1;
    if(findTile(row,col-1,Layer,2)!=null && findTile(row,col+1,Layer,2)!=null && t.getKind()==2 )
      return 1;
    if(findTile(row,col-1,Layer,4)!=null && findTile(row,col+1,Layer,4)!=null && t.getKind()==4 )
      return 1;
    if(findTile(row,col-1,Layer,5)!=null && findTile(row,col+1,Layer,5)!=null && t.getKind()==5 )
      return 1;

    //Label TH2:
    if(findTile(row-1,col-1,Layer,4)!=null && findTile(row-1,col+1,Layer,4)!=null && t.getKind()==1 )
      return 1;
    if(findTile(row-1,col-1,Layer,5)!=null && findTile(row-1,col+1,Layer,5)!=null && t.getKind()==2 )
      return 1;
    if(findTile(row,col-1,Layer,1)!=null && findTile(row,col+1,Layer,1)!=null && t.getKind()==4 )
      return 1;
    if(findTile(row,col-1,Layer,2)!=null && findTile(row,col+1,Layer,2)!=null && t.getKind()==5 )
      return 1;

    //Label TH3:
    if(findTile(row+1,col-1,Layer,2)!=null && findTile(row+1,col+1,Layer,2)!=null && t.getKind()==5 )
      return 1;
    if(findTile(row+1,col-1,Layer,1)!=null && findTile(row+1,col+1,Layer,1)!=null && t.getKind()==4 )
      return 1;
    if(findTile(row,col-1,Layer,4)!=null && findTile(row,col+1,Layer,4)!=null && t.getKind()==1 )
      return 1;
    if(findTile(row,col-1,Layer,5)!=null && findTile(row,col+1,Layer,5)!=null && t.getKind()==2 )
      return 1;

    //Label TH4:
    if(findTile(row-1,col-1,Layer,4)!=null && findTile(row,col+1,Layer,4)!=null && t.getKind()==1 )
      return 1;
    if(findTile(row-1,col-1,Layer,5)!=null && findTile(row,col+1,Layer,5)!=null && t.getKind()==2 )
      return 1;
    if(findTile(row,col-1,Layer,1)!=null && findTile(row+1,col+1,Layer,1)!=null && t.getKind()==4 )
      return 1;
    if(findTile(row,col-1,Layer,2)!=null && findTile(row+1,col+1,Layer,2)!=null && t.getKind()==5 )
      return 1;

    //Label TH5:
    if(findTile(row,col-1,Layer,4)!=null && findTile(row-1,col+1,Layer,4)!=null && t.getKind()==1 )
      return 1;
    if(findTile(row,col-1,Layer,5)!=null && findTile(row-1,col+1,Layer,5)!=null && t.getKind()==2 )
      return 1;
    if(findTile(row+1,col-1,Layer,1)!=null && findTile(row,col+1,Layer,1)!=null && t.getKind()==4 )
      return 1;
    if(findTile(row+1,col-1,Layer,2)!=null && findTile(row,col+1,Layer,2)!=null && t.getKind()==5 )
      return 1;

    //Label TH6:
    if(findTile(row,col-1,Layer,1)!=null && findTile(row,col+1,Layer,4)!=null && t.getKind()==1 )
      return 1;
    if(findTile(row,col-1,Layer,2)!=null && findTile(row,col+1,Layer,5)!=null && t.getKind()==2 )
      return 1;
    if(findTile(row,col-1,Layer,4)!=null && findTile(row+1,col+1,Layer,1)!=null && t.getKind()==4 )
      return 1;
    if(findTile(row,col-1,Layer,5)!=null && findTile(row+1,col+1,Layer,2)!=null && t.getKind()==5 )
      return 1;

    //Label TH7:
    if(findTile(row,col-1,Layer,1)!=null && findTile(row-1,col+1,Layer,4)!=null && t.getKind()==1 )
      return 1;
    if(findTile(row,col-1,Layer,2)!=null && findTile(row-1,col+1,Layer,5)!=null && t.getKind()==2 )
      return 1;
    if(findTile(row,col-1,Layer,4)!=null && findTile(row,col+1,Layer,1)!=null && t.getKind()==4 )
      return 1;
    if(findTile(row,col-1,Layer,5)!=null && findTile(row,col+1,Layer,2)!=null && t.getKind()==5 )
      return 1;

    //Label TH8:
    if(findTile(row-1,col-1,Layer,4)!=null && findTile(row,col+1,Layer,1)!=null && t.getKind()==1 )
      return 1;
    if(findTile(row-1,col-1,Layer,5)!=null && findTile(row,col+1,Layer,2)!=null && t.getKind()==2 )
      return 1;
    if(findTile(row,col-1,Layer,1)!=null && findTile(row,col+1,Layer,4)!=null && t.getKind()==4 )
      return 1;
    if(findTile(row,col-1,Layer,2)!=null && findTile(row,col+1,Layer,5)!=null && t.getKind()==5 )
      return 1;

    //Label TH9:
    if(findTile(row,col-1,Layer,4)!=null && findTile(row,col+1,Layer,1)!=null && t.getKind()==1 )
      return 1;
    if(findTile(row,col-1,Layer,5)!=null && findTile(row,col+1,Layer,2)!=null && t.getKind()==2 )
      return 1;
    if(findTile(row+1,col-1,Layer,1)!=null && findTile(row,col+1,Layer,4)!=null && t.getKind()==4 )
      return 1;
    if(findTile(row+1,col-1,Layer,2)!=null && findTile(row,col+1,Layer,5)!=null && t.getKind()==5 )
      return 1;
    return 0;
  }
  public Array<Tile> getLock(Tile t){
    int row   = (int)t.getRowCol().x;
    int col   = (int)t.getRowCol().y;
    int Layer = t.getLayer();
    Array<Tile> arr = new Array<>();
    //Label TH1:
    if(findTile(row,col-1,Layer,1)!=null && findTile(row,col+1,Layer,1)!=null && t.getKind()==1 ){
      arr.add(findTile(row,col-1,Layer,1),findTile(row,col+1,Layer,1),t);
      return arr;
    }
    if(findTile(row,col-1,Layer,2)!=null && findTile(row,col+1,Layer,2)!=null && t.getKind()==2 ){
      arr.add(findTile(row,col-1,Layer,2),findTile(row,col+1,Layer,2),t);
      return arr;
    }
    if(findTile(row,col-1,Layer,4)!=null && findTile(row,col+1,Layer,4)!=null && t.getKind()==4 ){
      arr.add(findTile(row,col-1,Layer,4),findTile(row,col+1,Layer,4),t);
      return arr;
    }
    if(findTile(row,col-1,Layer,5)!=null && findTile(row,col+1,Layer,5)!=null && t.getKind()==5 ){
      arr.add(findTile(row,col-1,Layer,5),findTile(row,col+1,Layer,5),t);
      return arr;
    }

    //Label TH2:
    if(findTile(row-1,col-1,Layer,4)!=null && findTile(row-1,col+1,Layer,4)!=null && t.getKind()==1 ){
      arr.add(findTile(row-1,col-1,Layer,4),findTile(row-1,col+1,Layer,4),t);
      return arr;
    }
    if(findTile(row-1,col-1,Layer,5)!=null && findTile(row-1,col+1,Layer,5)!=null && t.getKind()==2 ){
      arr.add(findTile(row-1,col-1,Layer,5),findTile(row-1,col+1,Layer,5),t);
      return arr;
    }
    if(findTile(row,col-1,Layer,1)!=null && findTile(row,col+1,Layer,1)!=null && t.getKind()==4 ){
      arr.add(findTile(row,col-1,Layer,1),findTile(row,col+1,Layer,1),t);
      return arr;
    }
    if(findTile(row,col-1,Layer,2)!=null && findTile(row,col+1,Layer,2)!=null && t.getKind()==5 ){
      arr.add(findTile(row,col-1,Layer,2),findTile(row,col+1,Layer,2),t);
      return arr;
    }

    //Label TH3:
    if(findTile(row+1,col-1,Layer,2)!=null && findTile(row+1,col+1,Layer,2)!=null && t.getKind()==5 ){
      arr.add(findTile(row+1,col-1,Layer,2),findTile(row+1,col+1,Layer,2),t);
      return arr;
    }
    if(findTile(row+1,col-1,Layer,1)!=null && findTile(row+1,col+1,Layer,1)!=null && t.getKind()==4 ){
      arr.add(findTile(row+1,col-1,Layer,1),findTile(row+1,col+1,Layer,1),t);
      return arr;
    }
    if(findTile(row,col-1,Layer,4)!=null && findTile(row,col+1,Layer,4)!=null && t.getKind()==1 ){
      arr.add(findTile(row,col-1,Layer,4),findTile(row,col+1,Layer,4),t);
      return arr;
    }
    if(findTile(row,col-1,Layer,5)!=null && findTile(row,col+1,Layer,5)!=null && t.getKind()==2 ){
      arr.add(findTile(row,col-1,Layer,5),findTile(row,col+1,Layer,5),t);
      return arr;
    }

    //Label TH4:
    if(findTile(row-1,col-1,Layer,4)!=null && findTile(row,col+1,Layer,4)!=null && t.getKind()==1 ){
      arr.add(findTile(row-1,col-1,Layer,4),findTile(row,col+1,Layer,4),t);
      return arr;
    }
    if(findTile(row-1,col-1,Layer,5)!=null && findTile(row,col+1,Layer,5)!=null && t.getKind()==2 ){
      arr.add(findTile(row-1,col-1,Layer,5),findTile(row,col+1,Layer,5),t);
      return arr;
    }
    if(findTile(row,col-1,Layer,1)!=null && findTile(row+1,col+1,Layer,1)!=null && t.getKind()==4 ){
      arr.add(findTile(row,col-1,Layer,1),findTile(row+1,col+1,Layer,1),t);
      return arr;
    }
    if(findTile(row,col-1,Layer,2)!=null && findTile(row+1,col+1,Layer,2)!=null && t.getKind()==5 ){
      arr.add(findTile(row,col-1,Layer,2),findTile(row+1,col+1,Layer,2),t);
      return arr;
    }

    //Label TH5:
    if(findTile(row,col-1,Layer,4)!=null && findTile(row-1,col+1,Layer,4)!=null && t.getKind()==1 ){
      arr.add(findTile(row,col-1,Layer,4),findTile(row-1,col+1,Layer,4),t);
      return arr;
    }
    if(findTile(row,col-1,Layer,5)!=null && findTile(row-1,col+1,Layer,5)!=null && t.getKind()==2 ){
      arr.add(findTile(row,col-1,Layer,5),findTile(row-1,col+1,Layer,5),t);
      return arr;
    }
    if(findTile(row+1,col-1,Layer,1)!=null && findTile(row,col+1,Layer,1)!=null && t.getKind()==4 ){
      arr.add(findTile(row+1,col-1,Layer,1),findTile(row,col+1,Layer,1),t);
      return arr;
    }
    if(findTile(row+1,col-1,Layer,2)!=null && findTile(row,col+1,Layer,2)!=null && t.getKind()==5 ){
      arr.add(findTile(row+1,col-1,Layer,2),findTile(row,col+1,Layer,2),t);
      return arr;
    }

    //Label TH6:
    if(findTile(row,col-1,Layer,1)!=null && findTile(row,col+1,Layer,4)!=null && t.getKind()==1 ){
      arr.add(findTile(row,col-1,Layer,1),findTile(row,col+1,Layer,4),t);
      return arr;
    }
    if(findTile(row,col-1,Layer,2)!=null && findTile(row,col+1,Layer,5)!=null && t.getKind()==2 ){
      arr.add(findTile(row,col-1,Layer,2),findTile(row,col+1,Layer,5),t);
      return arr;
    }
    if(findTile(row,col-1,Layer,4)!=null && findTile(row+1,col+1,Layer,1)!=null && t.getKind()==4 ){
      arr.add(findTile(row,col-1,Layer,4),findTile(row+1,col+1,Layer,1),t);
      return arr;
    }
    if(findTile(row,col-1,Layer,5)!=null && findTile(row+1,col+1,Layer,2)!=null && t.getKind()==5 ){
      arr.add(findTile(row,col-1,Layer,5),findTile(row+1,col+1,Layer,2),t);
      return arr;
    }

    //Label TH7:
    if(findTile(row,col-1,Layer,1)!=null && findTile(row-1,col+1,Layer,4)!=null && t.getKind()==1 ){
      arr.add(findTile(row,col-1,Layer,1),findTile(row-1,col+1,Layer,4),t);
      return arr;
    }
    if(findTile(row,col-1,Layer,2)!=null && findTile(row-1,col+1,Layer,5)!=null && t.getKind()==2 ){
      arr.add(findTile(row,col-1,Layer,2),findTile(row-1,col+1,Layer,5),t);
      return arr;
    }
    if(findTile(row,col-1,Layer,4)!=null && findTile(row,col+1,Layer,1)!=null && t.getKind()==4 ){
      arr.add(findTile(row,col-1,Layer,4),findTile(row,col+1,Layer,1),t);
      return arr;
    }
    if(findTile(row,col-1,Layer,5)!=null && findTile(row,col+1,Layer,2)!=null && t.getKind()==5 ){
      arr.add(findTile(row,col-1,Layer,5),findTile(row,col+1,Layer,2),t);
      return arr;
    }

    //Label TH8:
    if(findTile(row-1,col-1,Layer,4)!=null && findTile(row,col+1,Layer,1)!=null && t.getKind()==1 ){
      arr.add(findTile(row-1,col-1,Layer,4), findTile(row,col+1,Layer,1),t);
      return arr;
    }
    if(findTile(row-1,col-1,Layer,5)!=null && findTile(row,col+1,Layer,2)!=null && t.getKind()==2 ){
      arr.add(findTile(row-1,col-1,Layer,1), findTile(row,col+1,Layer,2),t);
      return arr;
    }
    if(findTile(row,col-1,Layer,1)!=null && findTile(row,col+1,Layer,4)!=null && t.getKind()==4 ){
      arr.add(findTile(row,col-1,Layer,1), findTile(row,col+1,Layer,4),t);
      return arr;
    }
    if(findTile(row,col-1,Layer,2)!=null && findTile(row,col+1,Layer,5)!=null && t.getKind()==5 ){
      arr.add(findTile(row,col-1,Layer,2), findTile(row,col+1,Layer,5),t);
      return arr;
    }

    //Label TH9:
    if(findTile(row,col-1,Layer,4)!=null && findTile(row,col+1,Layer,1)!=null && t.getKind()==1 ){
      arr.add(findTile(row,col-1,Layer,4), findTile(row,col+1,Layer,1),t);
      return arr;
    }
    if(findTile(row,col-1,Layer,5)!=null && findTile(row,col+1,Layer,2)!=null && t.getKind()==2 ){
      arr.add(findTile(row,col-1,Layer,5),findTile(row,col+1,Layer,2),t);
      return arr;
    }
    if(findTile(row+1,col-1,Layer,1)!=null && findTile(row,col+1,Layer,4)!=null && t.getKind()==4 ){
      arr.add(findTile(row+1,col-1,Layer,1), findTile(row,col+1,Layer,4),t);
      return arr;
    }
    if(findTile(row+1,col-1,Layer,2)!=null && findTile(row,col+1,Layer,5)!=null && t.getKind()==5 ){
      arr.add(findTile(row+1,col-1,Layer,2),findTile(row,col+1,Layer,5),t);
      return arr;
    }
    return null;
  }
  public int checkLayer(Tile t){
    int row   = (int)t.getRowCol().x;
    int col   = (int)t.getRowCol().y;
    int Layer = t.getLayer()+1;
    if((findTile(row,col,Layer,1)!=null
            ||findTile(row,col,Layer,2)!=null|| findTile(row,col,Layer,4)!=null
            ||findTile(row,col,Layer,5)!=null|| findTile(row,col+1,Layer,1)!=null
            ||findTile(row,col+1,Layer,4)!=null||findTile(row+1,col+1,Layer,1)!=null
            ||findTile(row+1,col,Layer,1)!=null||findTile(row+1,col,Layer,2)!=null)  && t.getKind()==5){
      return 1;
    }
    if((findTile(row,col,Layer,1)!=null
            ||findTile(row,col,Layer,2)!=null|| findTile(row,col,Layer,4)!=null
            ||findTile(row,col,Layer,5)!=null|| findTile(row-1,col-1,Layer,5)!=null
            ||findTile(row-1,col,Layer,4)!=null||findTile(row-1,col,Layer,5)!=null
            ||findTile(row,col-1,Layer,5)!=null ||findTile(row,col-1,Layer,2)!=null)&& t.getKind()==1){
      return 1;
    }
    if((findTile(row,col,Layer,1)!=null
            ||findTile(row,col,Layer,2)!=null|| findTile(row,col,Layer,4)!=null
            ||findTile(row,col,Layer,5)!=null|| findTile(row-1,col,Layer,4)!=null
            ||findTile(row-1,col,Layer,5)!=null||findTile(row-1,col+1,Layer,4)!=null
            ||findTile(row,col+1,Layer,1)!=null||findTile(row,col+1,Layer,4)!=null) && t.getKind()==2){
      return 1;
    }
    if((findTile(row,col,Layer,1)!=null
            ||findTile(row,col,Layer,2)!=null|| findTile(row,col,Layer,4)!=null
            ||findTile(row,col,Layer,5)!=null|| findTile(row,col-1,Layer,2)!=null
            ||findTile(row,col-1,Layer,5)!=null||findTile(row+1,col,Layer,1)!=null
            ||findTile(row+1,col,Layer,2)!=null||findTile(row+1,col-1,Layer,2)!=null) && t.getKind()==4){
      return 1;
    }
    return 0;
  }
  public Array<Tile> getLockLayer(Tile t){
    int row   = (int)t.getRowCol().x;
    int col   = (int)t.getRowCol().y;
    int Layer = t.getLayer()+1;
    Array<Tile> arr = new Array<>();
    if((findTile(row,col,Layer,1)!=null
            ||findTile(row,col,Layer,2)!=null|| findTile(row,col,Layer,4)!=null
            ||findTile(row,col,Layer,5)!=null|| findTile(row,col+1,Layer,1)!=null
            ||findTile(row,col+1,Layer,4)!=null||findTile(row+1,col+1,Layer,1)!=null
            ||findTile(row+1,col,Layer,1)!=null||findTile(row+1,col,Layer,2)!=null)  && t.getKind()==5){

      arr.add(findTile(row,col,Layer,1));
      arr.add(findTile(row,col,Layer,2));
      arr.add(findTile(row,col,Layer,4));
      arr.add(findTile(row,col,Layer,5));
      arr.add(findTile(row,col+1,Layer,1));
      arr.add(findTile(row,col+1,Layer,4));
      arr.add(findTile(row+1,col+1,Layer,1));
      arr.add(findTile(row+1,col,Layer,1));
      arr.add(findTile(row+1,col,Layer,2));
      arr.add(t);
      return arr;
    }
    if((findTile(row,col,Layer,1)!=null
            ||findTile(row,col,Layer,2)!=null|| findTile(row,col,Layer,4)!=null
            ||findTile(row,col,Layer,5)!=null|| findTile(row-1,col-1,Layer,5)!=null
            ||findTile(row-1,col,Layer,4)!=null||findTile(row-1,col,Layer,5)!=null
            ||findTile(row,col-1,Layer,5)!=null ||findTile(row,col-1,Layer,2)!=null) && t.getKind()==1){
      arr.add(findTile(row,col,Layer,1));
      arr.add(findTile(row,col,Layer,2));
      arr.add(findTile(row,col,Layer,4));
      arr.add(findTile(row,col,Layer,5));
      arr.add(findTile(row-1,col-1,Layer,5));
      arr.add(findTile(row-1,col,Layer,4));
      arr.add(findTile(row-1,col,Layer,5));
      arr.add(findTile(row,col-1,Layer,5));
      arr.add(findTile(row,col-1,Layer,2));
      arr.add(t);
      return arr;
    }
    if((findTile(row,col,Layer,1)!=null
            ||findTile(row,col,Layer,2)!=null|| findTile(row,col,Layer,4)!=null
            ||findTile(row,col,Layer,5)!=null|| findTile(row-1,col,Layer,4)!=null
            ||findTile(row-1,col,Layer,5)!=null||findTile(row-1,col+1,Layer,4)!=null
            ||findTile(row,col+1,Layer,1)!=null||findTile(row,col+1,Layer,4)!=null )&& t.getKind()==2){
      arr.add(findTile(row,col,Layer,1));
      arr.add(findTile(row,col,Layer,2));
      arr.add(findTile(row,col,Layer,4));
      arr.add(findTile(row,col,Layer,5));
      arr.add(findTile(row-1,col,Layer,4));
      arr.add(findTile(row-1,col,Layer,5));
      arr.add(findTile(row-1,col+1,Layer,4));
      arr.add(findTile(row,col+1,Layer,1));
      arr.add(findTile(row,col+1,Layer,4));
      arr.add(t);
      return arr;
    }
    if((findTile(row,col,Layer,1)!=null
            ||findTile(row,col,Layer,2)!=null|| findTile(row,col,Layer,4)!=null
            ||findTile(row,col,Layer,5)!=null|| findTile(row,col-1,Layer,2)!=null
            ||findTile(row,col-1,Layer,5)!=null||findTile(row+1,col,Layer,1)!=null
            ||findTile(row+1,col,Layer,2)!=null||findTile(row+1,col-1,Layer,2)!=null) && t.getKind()==4){

      arr.add(findTile(row,col,Layer,1));
      arr.add(findTile(row,col,Layer,2));
      arr.add(findTile(row,col,Layer,4));
      arr.add(findTile(row,col,Layer,5));
      arr.add(findTile(row,col-1,Layer,2));
      arr.add(findTile(row,col-1,Layer,5));
      arr.add(findTile(row+1,col,Layer,1));
      arr.add(findTile(row+1,col,Layer,2));
      arr.add(findTile(row+1,col-1,Layer,2));
      arr.add(t);
      return arr;
    }
    return null;
  }
  public void checkMatch(Tile t){
    if(arrCompare.size==0){
      arrCompare.add(t);
    }else if(arrCompare.size==1){
      int row   = (int)arrCompare.get(0).getRowCol().x;
      int col   = (int)arrCompare.get(0).getRowCol().y;
      int id    = (int)arrCompare.get(0).getId();
      int kind  = (int)arrCompare.get(0).getKind();
      if(row==(int)t.getRowCol().x && col == (int)t.getRowCol().y && id == t.getId() && kind== t.getKind()){
        System.out.println("trùng ");
        if(com.ss.scenes.gameScene.effect!=null)
          com.ss.scenes.gameScene.effect.FreeAllEfSelect();
//        arrCompare.get(0).select(false);
        arrCompare.clear();
      }else {
        System.out.println("them tile mới!!");
        arrCompare.add(t);
      }
    }

    if(arrCompare.size==2){
      System.out.println("compare here!!");
      if(arrCompare.get(0).getId()==arrCompare.get(1).getId()){
        Tile t1 = arrCompare.get(0);
        Tile t2 = arrCompare.get(1);
        matchTile(t1,t2);
      }else {
        arrCompare.get(0).select(false);
        arrCompare.removeIndex(0);
//        arrCompare.get(1).select(false);
//        arrCompare.clear();
      }
    }
  }
  private void matchTile(Tile t1, Tile t2){
    if(com.ss.scenes.gameScene.effect!=null)
      com.ss.scenes.gameScene.effect.FreeAllEfSelect();
    float x1 = t1.getXY().x+t1.gr.getWidth()/2;
    float y1 = t1.getXY().y+t1.gr.getHeight()/2;
    float x2 = t2.getXY().x+t2.gr.getWidth()/2;
    float y2 = t2.getXY().y+t2.gr.getHeight()/2;
    float moveY1 = Math.abs(y1-y2)/2;
    if(y1>y2)
      moveY1*=-1;
    float moveY2 = Math.abs(y1-y2)/2;
    if(y2>y1)
      moveY2*=-1;

    float moveOutX1 = Math.abs(x1-x2)/2-t1.gr.getWidth()/2;
    float moveX1 = 200;
    if(x1<x2){
      moveX1*=-1;
      moveOutX1*=-1;
    }
    float moveOutX2 = Math.abs(x1-x2)/2-t1.gr.getWidth()/2;
    float moveX2 = 200;
    if(x2<x1){
      moveX2*=-1;
      moveOutX2*=-1;
    }
    if (x1==x2)
      moveX1*=-1;

    removeTile((int)t1.getRowCol().x,(int)t1.getRowCol().y,t1.getKind(),t1.getId(),t1.getLayer());
    removeTile((int)t2.getRowCol().x,(int)t2.getRowCol().y,t2.getKind(),t2.getId(),t2.getLayer());
    arrCompare.clear();
    setLockTile();
    t1.setZindex(1000);
    t2.setZindex(1000);
    t1.gr.addAction(Actions.sequence(
            Actions.moveBy(moveX1,moveY1,0.2f,Interpolation.circleOut),
            Actions.moveBy(-(moveX1+moveOutX1),0,0.2f,Interpolation.swingOut),
            Actions.run(()->{
              t1.dispose();
            })
    ));
    t2.gr.addAction(Actions.sequence(
            Actions.moveBy(moveX2,moveY2,0.2f,Interpolation.circleOut),
            Actions.moveBy(-(moveX2+moveOutX2),0,0.2f,Interpolation.swingOut),
            Actions.run(()->{
              t2.dispose();
            })
    ));
    hintBoard("check before match");
    if(arrTileHint.size<2 ){
      if(arrTileBoard.size>2){
        shuffleBoard("shuffle before match",()->{
          System.out.println("finish!!");
        });
      }else if(arrTileBoard.size==2){
        System.out.println("out of move!!!");
      }else {
        System.out.println("win game!!");
      }

    }
  }

  public void autoMatch(){
    count++;
    if(count> Config.quanAutomatch){
      count=0;
      return;
    }
    SkipHint();
    shuffleBoard("automatch",()->{
      if(arrTileHint.size>=2){
        Tile t1= arrTileHint.get(0);
        Tile t2= arrTileHint.get(1);
        matchTile(t1,t2);
        System.out.println("match");
      }
      if(arrTileBoard.size<=2)
        return;
      Tweens.setTimeout(group,0.5f,()->{
        autoMatch();
      });

    });
  }

  private void removeTile(int row, int col,int kind,int id,int layer){
    for (Tile t : arrTileBoard){
      if(t.getRowCol().x==row && t.getRowCol().y==col && t.getKind()==kind && t.getId()==id && t.getLayer() == layer){
        arrTileBoard.removeIndex(arrTileBoard.indexOf(t,true));
      }
    }

  }


  //Label: tim lien ket khoa tile
  private Tile findTile(int row, int col, int Layer, int kind){
    for (Tile tt : arrTileBoard){
      if(row==tt.getRowCol().x && col == tt.getRowCol().y && Layer ==tt.getLayer() && kind== tt.getKind() )
        return tt;
    }
    return null;
  }


  public void hintBoard(String type){
    arrTileHint.clear();
    arrTileCanHint.clear();
    for (Tile t : arrTileBoard){
      if(t.block.getColor().equals(Color.WHITE))
        arrTileCanHint.add(t);
    }
    int counthint=0;
    for (int i=0;i<arrTileCanHint.size; i++){
      for (int j=i+1 ;j<arrTileCanHint.size;j++){
        if(arrTileCanHint.get(i).getId()==arrTileCanHint.get(j).getId()){
          counthint++;
          arrTileHint.add(arrTileCanHint.get(j));
        }
      }
      if(counthint>0){
        arrTileHint.add(arrTileCanHint.get(i));
        break;
      }else {
        arrTileHint.clear();
      }

    }
    System.out.println("get size arrhint: "+ arrTileHint.size);
    int hint =0;
    for (Tile t: arrTileHint){
      hint++;
      if(hint>2)
        return;
      if(type.equals("hint")){
        t.select(true);
        t.ActionHint();
      }

    }

  }
  public void SkipHint(){
    for (Tile t : arrTileHint)
      t.select(false);
  }
  public void createlbShuffle(){
    lbShuffle = new Label("dang shuffle!!!",new Label.LabelStyle(BitmapFontC.FontAlert,null));
    lbShuffle.setPosition(GStage.getWorldWidth()/2,GStage.getWorldHeight()/2, Align.center);
    group.addActor(lbShuffle);
  }
  public void shuffleBoard(String type,Runnable runnable){
    arrTileShuffle.clear();
    for (Tile t : arrTileBoard){
      arrTileShuffle.add(t.getId());
    }
    arrTileShuffle.shuffle();
    if(arrTileShuffle.size==arrTileBoard.size){
      for (Tile t : arrTileBoard){
        t.changeId(arrTileShuffle.get(arrTileBoard.indexOf(t,true)));
      }
      setLockTile();
    }
    hintBoard(type);
    if(arrTileHint.size>=2){
//      lbShuffle.remove();
      group.addAction(Actions.run(runnable));
      return;
    }else {
      shuffleBoard(type,runnable);
    }

  }
  public void dispose(){
    group.clear();
    group.remove();
  }

  private void showfps(){
    s= Gdx.graphics.getFramesPerSecond();
    Label fps = new Label("fps: "+s,new Label.LabelStyle(BitmapFontC.font_white,null));
    fps.setFontScale(0.5f);
    fps.setPosition(100,GStage.getWorldHeight()-100);
    group.addActor(fps);
    group.addAction(GSimpleAction.simpleAction((d, a)->{
      s= Gdx.graphics.getFramesPerSecond();
      fps.setText("fps: "+s);
      return false;
    }));
  }

}
