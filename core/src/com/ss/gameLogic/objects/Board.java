package com.ss.gameLogic.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.ss.GMain;
import com.ss.commons.BitmapFontC;
import com.ss.commons.Tweens;
import com.ss.core.action.exAction.GScreenShake2Action;
import com.ss.core.action.exAction.GSimpleAction;
import com.ss.core.util.GLayerGroup;
import com.ss.core.util.GStage;
import com.ss.effects.SoundEffect;
import com.ss.gameLogic.config.Config;
import com.ss.gameLogic.objects.LevelData.LevelData;
import com.ss.scenes.GameScene;

public class Board {
  private Array<Tile> arrTile;
  private Array<Tile>                 arrTileBoard       = new Array<>();
  private Group                       group;
  private Group                       grTimer;
  private GLayerGroup                 grCombo;
  private Array<Tile>                 arrCompare         = new Array<>();
  private Array<Integer>              arrTileShuffle     = new Array<>();
  private Array<Tile>                 arrTileHint        = new Array<>();
  private Array<Tile>                 arrTileCanHint     = new Array<>();
  private Label                       lbShuffle;
  private int                         s;
  private GameScene gameScene;
  private int                         count              = 0;
  private int level = 0;
  private JsonValue                   jsLV[];
  private Group                       grNotice;
  private Timer                       timer;
  private int                         NumEnd             = 4;
  private Combo                       combo;
  public  boolean                     isUseItem          = true;
  private Header                      header;
  private LevelData.LevelDto          levelDto;


  public Board(int lv, JsonValue[] levels, GameScene gameScene, Group group, Group grTimer, GLayerGroup grCombo){
    Config.countTimePlay++;
    GMain.platform.ShowBanner(true);
    this.gameScene = gameScene;
    this.level = lv;
    this.jsLV      = levels;
    this.group     = group;
    this.grTimer   = grTimer;
    this.grCombo   = grCombo;
    group.setVisible(true);
    timer = new Timer(GStage.getWorldWidth()/2,40,TimeLevel(lv+1));
//    grTimer.addActor(timer);
    grTimer.addActor(timer);
    timer.start(()->{
//      System.out.println("out Time");
      grNotice = new Notice(GMain.locale.get("noticeOutTime"),0.5f,new ClickListener(){
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
          grNotice.clear();
          grNotice.remove();
          new EndGame(false,0,jsLV, level,gameScene,Board.this,group,grTimer,grCombo);
          TrackingEventLv(Config.NameEvent.FAIL);
          return super.touchDown(event, x, y, pointer, button);
        }
      });
    });

    combo = new Combo(GStage.getWorldWidth()/2,60,Config.TimeCombo,grCombo);

    header = new Header(this,lv,gameScene,levels,group,grTimer,grCombo);

    levelDto = LevelData.getLevel(lv+1);
    createBoard(levels[levelDto.getFile()-1]);
    setLockTile();
    shuffleBoard("begin",()->{
//      System.out.println("begin done!");
    });
    //Todo: tracking event start
    TrackingEventLv(Config.NameEvent.START);
  }
  private void createBoard(JsonValue Arrjv ){
    int index=0;
    for (JsonValue jv : Arrjv){
      Tile t = new Tile(group,this);
      t.setRowCol(jv.get("row").asInt(),jv.get("col").asInt());
      t.setLayer2(jv.get("layer").asInt());
      t.setPos(jv.get("x").asFloat(),jv.get("y").asFloat(),jv.get("kind").asInt(),true,index,jv.get("id").asInt());
//      t.createID(jv.get("id").asInt());
      arrTileBoard.add(t);
      index++;
    }
  }
  public void setLockTile(){
    setDefault();
    for (int i=0;i<arrTileBoard.size;i++){
      if(setLock(arrTileBoard.get(i))){
        arrTileBoard.get(i).setColor(new Color(30/255f,30/255f,30/255f,255/255f));
      }
//      System.out.println("check: "+arrTileBoard.get(i).getRowCol().x+"___"+arrTileBoard.get(i).getRowCol().y+"__"+arrTileBoard.get(i).getKind());
    }

  }
  public void setDefault(){
    for (int i=0;i<arrTileBoard.size;i++){
       arrTileBoard.get(i).setColor(Color.WHITE);
    }

  }

  private boolean setLock(Tile tile){
    if(tile!=null){
      if(checkLayer(tile)==1 || checkLock(tile)==1)
        return true;
    }
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
        if(GameScene.effect!=null)
//          GameScene.effect.FreeAllEfSelect();
        arrCompare.get(0).select(false);
        arrCompare.clear();
      }else {
//        System.out.println("them tile mới!!");
        arrCompare.add(t);
      }
    }

    if(arrCompare.size==2){
//      System.out.println("compare here!!");
      if(arrCompare.get(0).getId()==arrCompare.get(1).getId()){
        Config.Combo++;
        Tile t1 = arrCompare.get(0);
        Tile t2 = arrCompare.get(1);
        matchTile(t1,t2,"",Config.Combo);
      }else {
        arrCompare.get(0).select(false);
        arrCompare.removeIndex(0);
//        SoundEffect.Play(SoundEffect.unmatch);
//        arrCompare.get(1).select(false);
//        arrCompare.clear();
      }
    }
  }
  private void matchTile(Tile t1, Tile t2,String type,int combo){

    Upcombo(combo);
    float x1 = t1.getXY().x;
    float y1 = t1.getXY().y;
    float x2 = t2.getXY().x;
    float y2 = t2.getXY().y;
    float WTile = t1.getWidth();
    float HTile = t1.getHeight();
    float moveY1 = Math.abs(y1-y2)/2;
    float perArcX = Config.PerArcX;
    float perArcY = Config.PerArcY;
    if(y1>y2)
      moveY1*=-1;
    float moveY2 = Math.abs(y1-y2)/2;
    if(y2>y1)
      moveY2*=-1;

    float moveOutX1 = Math.abs(x1-x2)/2-t1.getWidth()*0.4f;
    float moveX1 = Config.MoveOut;
    if(x1<x2){
      moveX1*=-1;
      moveOutX1*=-1;
      t1.setZindex(1001);
      t2.setZindex(1000);
    }
    float moveOutX2 = Math.abs(x1-x2)/2-t1.getWidth()*0.4f;
    float moveX2 = Config.MoveOut;
    if(x2<x1){
      moveX2*=-1;
      moveOutX2*=-1;
      t1.setZindex(1000);
      t2.setZindex(1001);
    }
    if (x1==x2){
      moveX1*=-1;
      moveOutX1+=WTile;
//      moveOutX2-=WTile;
      perArcY=0;
      t1.setZindex(1000);
      t2.setZindex(1001);
    }
    removeTile(t1);
    removeTile(t2);
    arrCompare.clear();
    setLockTile();
//    t1.setZindex(1000);
//    t2.setZindex(1000);

    Array<Vector2> arrVecT1 = new Array<>();
    arrVecT1.add(new Vector2(x1,y1));
    arrVecT1.add(new Vector2(x1,y1));
    arrVecT1.add(new Vector2(x1+moveX1*perArcX,y1+moveY1*perArcY));
    arrVecT1.add(new Vector2(x1+moveX1,y1+moveY1));
    arrVecT1.add(new Vector2((x1+moveX1)-(moveX1+moveOutX1),y1+moveY1));
    arrVecT1.add(new Vector2((x1+moveX1)-(moveX1+moveOutX1),y1+moveY1));

    Array<Vector2> arrVecT2 = new Array<>();
    arrVecT2.add(new Vector2(x2,y2));
    arrVecT2.add(new Vector2(x2,y2));
    arrVecT2.add(new Vector2(x2+moveX2*perArcX,y2+moveY2*perArcY));
    arrVecT2.add(new Vector2(x2+moveX2,y2+moveY2));
    arrVecT2.add(new Vector2((x2+moveX2)-(moveX2+moveOutX2),y2+moveY2));
    arrVecT2.add(new Vector2((x2+moveX2)-(moveX2+moveOutX2),y2+moveY2));

    t1.ActionMoveArc(arrVecT1,Config.DurMoveTile,false,()->{
      if(type.equals("bomb")){
        group.addAction(GScreenShake2Action.screenShake1(0.1f,4,group));
      }
      SoundEffect.Play(SoundEffect.match);
      if(combo>1)
        SoundEffect.explode(combo-1);
      t1.dispose();
    });

    t2.ActionMoveArc(arrVecT2,Config.DurMoveTile,false,()->{
      t2.dispose();
    });
    checkUseSkills();

    hintBoard("check before match");
    if(arrTileHint.size<2 ){
      if(!checkEndGame() && arrTileBoard.size!=0){
        shuffleBoard("shuffle before match",()->{
//          System.out.println("finish!!");
        });
      }else if(checkEndGame() && arrTileBoard.size!=0){
          pauseTime(true);
//        System.out.println("out of move!!!");
          Tweens.setTimeout(group,1,()->  {
            grNotice = new Notice(GMain.locale.get("noticeOutMove"),0.5f,new ClickListener(){
              @Override
              public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                grNotice.clear();
                grNotice.remove();
                new EndGame(false,0,jsLV, level,gameScene,Board.this,group,grTimer,grCombo);
                TrackingEventLv(Config.NameEvent.FAIL);
                return super.touchDown(event, x, y, pointer, button);
              }
            });
          });
      }else if(arrTileBoard.size==0) {
        pauseTime(true);
//        System.out.println("win game!!");
        Tweens.setTimeout(group,1,()->{
          new EndGame(true,timer.getStar(),jsLV, level,gameScene,this,group,grTimer,grCombo);
          TrackingEventLv(Config.NameEvent.COMPLETE);
        });
      }

    }
  }
  public void autoMatch(Runnable runnable){
    count++;
    if(count> Config.QuanAutomatch ||checkEndGame()){
      if(checkEndGame())
        isUseItem=false;
      count=0;
      group.addAction(Actions.run(runnable));
      return;
    }
    SkipHint();
    hintBoard("hint");
    if(arrTileHint.size>=2){
      Tile t1= arrTileHint.get(0);
      Tile t2= arrTileHint.get(1);
      Tweens.setTimeout(group,0.3f,()->{
        Config.Combo++;
        matchTile(t1,t2,"bomb",Config.Combo);
      });
//      System.out.println("match");
    }else {
      shuffleBoard("hint",()->{
        if(arrTileHint.size>=2){
          Tile t1= arrTileHint.get(0);
          Tile t2= arrTileHint.get(1);
          Config.Combo++;
          matchTile(t1,t2,"bomb",Config.Combo);
//          System.out.println("match");
        }
      });
    }
    Tweens.setTimeout(group,0.5f,()->{
      autoMatch(runnable);
    });

  }

  private void removeTile(Tile t){
    arrTileBoard.removeValue(t, true);
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
    SkipHint();
    arrTileHint.clear();
    arrTileCanHint.clear();
    for (Tile t : arrTileBoard){
      if(t.getColor().equals(Color.WHITE))
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
//    System.out.println("get size arrhint: "+ arrTileHint.size);
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

  public void shuffleBoard(String type,Runnable runnable){
    arrTileShuffle.clear();
    for (Tile t : arrTileBoard){
      arrTileShuffle.add(t.getId());
    }
    arrTileShuffle.shuffle();
    if(arrTileShuffle.size==arrTileBoard.size){
      for (Tile t : arrTileBoard){
        if(t!=null){
          t.changeId(arrTileShuffle.get(arrTileBoard.indexOf(t,true)));
        }
      }
      setLockTile();
    }
    hintBoard(type);
    if(arrTileHint.size>=2){
      group.addAction(Actions.run(runnable));
      return;
    }else {
      shuffleBoard(type,runnable);
    }

  }
  public void dispose(){
    SkipHint();
    grTimer.clear();
    combo.dispose();
    header.dispose();
    group.clear();


  }

  private void showfps(){
    s= Gdx.graphics.getFramesPerSecond();
    Label fps = new Label("fps: "+s,new Label.LabelStyle(BitmapFontC.font_white,null));
    fps.setFontScale(0.5f);
    fps.setPosition(20,GStage.getWorldHeight()-200);
    group.addActor(fps);
    group.addAction(GSimpleAction.simpleAction((d, a)->{
      s= Gdx.graphics.getFramesPerSecond();
      fps.setText("fps: "+s);
      return false;
    }));
  }
  public void pauseTime(boolean set){
    timer.setPause(set);
  }


  private int TimeLevel(int lv){
    int time= Config.Time;
    time = (int)(time - ((float)lv/Config.PercentTime)*time);
    if(lv>=Config.MaxLvtime){
      time=time-(Config.MaxLvtime /Config.PercentTime)*time;
    }
//    System.out.println("time level: "+time);
    return time;
  }
  private boolean checkEndGame(){
    int count=0;
    for (Tile t : arrTileBoard){
      if(t.getColor().equals(Color.WHITE))
        count++;
    }
    if(count<2)
      return true;
    return false;
  }
  public void Upcombo(int c){
    combo.upTime(c);
  }
  public void setPosTimer(float x,float y){
    timer.setPosition(x,y);
  }
  public void setPosCombo(float x, float y){
    combo.setPositon(x,y);
  }
  private void TrackingEventLv(Config.NameEvent select){
//    System.out.println("check name : "+ select);
    String name = "";
    if(select==Config.NameEvent.START)
      name ="level_start_"+(level +1)+"_"+levelDto.getFile();
    if(select==Config.NameEvent.COMPLETE)
      name ="level_complete_"+(level +1)+"_"+levelDto.getFile();
    if(select==Config.NameEvent.FAIL)
      name ="level_fail_"+(level +1)+"_"+levelDto.getFile();

//    System.out.println("tracking : "+name);
    GMain.platform.TrackCustomEvent(name);
  }
  public void checkUseSkills(){
    if(arrTileBoard.size!=0)
      isUseItem=true;
    else
      isUseItem=false;
  }




}
