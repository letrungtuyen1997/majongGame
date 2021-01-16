package com.ss.gameLogic.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.ss.commons.TextureAtlasC;
import com.ss.core.action.exAction.GPathAction;
import com.ss.core.action.exAction.GScreenShakeAction;
import com.ss.core.action.exAction.GSimpleAction;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.effects.SoundEffect;
import com.ss.gameLogic.config.Config;
import com.ss.scenes.GameScene;

public class TileRefactor extends Actor {
  public TextureRegion    block;
  private Board           board;
  private int             Layer       = 0;
  private int             row,col;
  public  float           x,y;
  public  int             id;
  public  int             kind;
  private TextureRegion   IdAni;
  private Array<Integer>  arrActionShark = new Array<>();
  private Group           group;
  private int             idEff   =0;


  public TileRefactor(Group group, Board board){
    this.board  = board;
    this.group  = group;
    arrActionShark.add(1);
    arrActionShark.add(-1);



  }
  public void createID(int Id){
    this.id = Id;
    IdAni = TextureAtlasC.AnimalsAtlas.findRegion(""+id);
//    IdAni.setOrigin(Align.center);
//    IdAni.setPosition(block.getX(Align.center),block.getY(Align.center),Align.center);
//    group.addActor(this);

//    addevent();
  }

  public void setPos(float x ,float y,int kind,boolean move,int index,int Id){
//    block = SelectLevel.poolImage.getTileFree();
    this.id = Id;
    block = TextureAtlasC.AnimalsAtlas.findRegion("cucxilau");
    IdAni = TextureAtlasC.AnimalsAtlas.findRegion(""+id);
    setSize(block.getRegionWidth(), block.getRegionHeight());
    group.addActor(this);
    this.kind = kind;
    if(move){

      this.setPosition(GStage.getWorldWidth()/2,-GStage.getWorldHeight()/2-group.getHeight()/2);
      this.addAction(Actions.sequence(
              Actions.delay(0.01f*index),
              Actions.moveTo(x-this.getWidth()-((Layer-1)* Config.paddingX)+this.getWidth()/2,y-this.getHeight()/2-((Layer-1)* Config.paddingY),Config.DuraMove,Interpolation.swingOut),
              GSimpleAction.simpleAction((d,a)->{
                this.x=this.getX();
                this.y=this.getY();
//                IdAni.setPosition(block.getX(Align.center),block.getY(Align.center),Align.center);
                return true;
              })
      ));

    }else {
      this.setPosition(x-this.getWidth()*0.6f,y-this.getHeight()/2);
      this.setPosition(this.getX(Align.center),this.getY(Align.center),Align.center);
      this.x=this.getX();
      this.y=this.getY();
      this.kind = kind;
//      if(board!=null){
//        addevent();
//      }
    }

  }
  public int getKind(){return kind;}
  public void setLayer(Array<Boolean> arrLayer){
    int dem=0;
    for (Boolean b: arrLayer){
      if(b)
        dem++;
    }
    Layer = dem;
  }
  public void setLayer2(int layer){
    Layer = layer;
  }
  public int getLayer(){
    return Layer;
  }
  public void setRowCol(int r,int c){
    row=r;
    col=c;
  }
  public Vector2 getRowCol(){
    return new Vector2(row,col);
  }
  public Vector2 getXY(){
    return new Vector2(x,y);
  }
  public void setZindex(int set){
    this.setZIndex(set);
  }
  public int getId(){return id;}
  public void setColor(Color set){
    this.setColor(set);
//    IdAni.setColor(set);
  }
  public void select(boolean set){
    if(set==true){
      if(GameScene.effect!=null)
        idEff = GameScene.effect.StartEffSelect(effPos().x-2,effPos().y);
    }else {
      if(GameScene.effect!=null)
        GameScene.effect.FreeEfSelect(idEff);
    }
  }
  public boolean checkLock(){
    if(this.getColor()==Color.DARK_GRAY)
      return true;
    return false;
  }
  public void ActionSelectLock(int s){
    this.addAction(Actions.sequence(
            GScreenShakeAction.screenShake1(Config.DuraShake,5,this)
    ));
//    IdAni.addAction(Actions.sequence(
//            GScreenShakeAction.screenShake1(Config.DuraShake,5,IdAni)
//    ));
  }
  private void ActionSelect(){
    this.addAction(Actions.sequence(
            Actions.scaleTo(1,0.9f,Config.DuraSelect),
            Actions.scaleTo(1,1f,Config.DuraSelect)
    ));
//    IdAni.addAction(Actions.sequence(
//            Actions.scaleTo(1,0.9f,Config.DuraSelect),
//            Actions.scaleTo(1,1f,Config.DuraSelect)
//    ));
  }
  public void ActionHint(){
    this.addAction(Actions.sequence(
            Actions.rotateBy(-10,Config.DuraHint),
            Actions.rotateBy(10,Config.DuraHint)
    ));
//    IdAni.addAction(Actions.sequence(
//            Actions.rotateBy(-10,Config.DuraHint),
//            Actions.rotateBy(10,Config.DuraHint)
//    ));
  }
  private Vector2 effPos(){
    return new Vector2(this.getParent().localToStageCoordinates(new Vector2(this.getX(Align.center),this.getY(Align.center))));
  }
  public void dispose(){
    if(GameScene.effect!=null){
      GameScene.effect.StartEff(effPos().x,effPos().y);
    }
    this.clear();
    this.remove();
//    block.clear();
//    block.remove();
//    SelectLevel.poolImage.dispose(block);
//    SelectLevel.poolImage.disposeAni(IdAni,id);

//    IdAni.clear();
//    IdAni.remove();
//    OverLay.clear();
//    OverLay.remove();
  }
  public void changeId(int Id){
//    IdAni.clear();
//    IdAni.remove();
    createID(Id);
//    block.addAction(Actions.sequence(
//            Actions.moveTo(GStage.getWorldWidth()/2,GStage.getWorldHeight()/2,0.5f),
//            GSimpleAction.simpleAction((d,a)->{
//              return true;
//            }),
//            Actions.moveTo(x,y,0.5f)
//    ));
//    IdAni.addAction(Actions.sequence(
//            Actions.moveTo(GStage.getWorldWidth()/2,GStage.getWorldHeight()/2,0.5f),
//            GSimpleAction.simpleAction((d,a)->{
//              createID(Id);
//              return true;
//            }),
//            Actions.moveTo(x,y,0.5f)
//    ));
  }

  private void addevent(){
    this.addListener(new ClickListener(){
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        SoundEffect.Play(SoundEffect.clickTile);
        select(true);
//        System.out.println("check tile click: "+getRowCol().x+"__"+getRowCol().y+"__"+getKind()+"__"+getLayer());
        Array<TileRefactor> arrTile = board.getLock(TileRefactor.this);
        Array<TileRefactor> arrTile2 = board.getLockLayer(TileRefactor.this);
        if(arrTile!=null && arrTile.size!=0){
          select(false);
          for (TileRefactor t : arrTile){
              t.ActionSelectLock(arrActionShark.get((int)(Math.random()*arrActionShark.size)));
          }

        }else if(board.checkLayer(TileRefactor.this)==1) {
          select(false);
          Array<TileRefactor> arr = new Array<>();
//          System.out.println("check size arrTile2: "+arrTile2.size);
//          System.out.println("check this tile: "+getRowCol().x+"_"+getRowCol().y+"_"+getKind()+"_"+getLayer());
          for (int i=0;i<arrTile2.size;i++){
            System.out.println(arrTile2.get(i));
            if(arrTile2.get(i)!=null){
              arr.add(arrTile2.get(i));
            }
          }
//          System.out.println("check size arrTile2: "+arrTile2.size);
          if(arr.size>=2){
            for (TileRefactor t : arr){
//              System.out.println("tile orthe layer: "+t.getRowCol().x+"_"+t.getRowCol().y+"_"+t.getKind()+"_"+t.getLayer());
                t.ActionSelectLock(arrActionShark.get((int)(Math.random()*arrActionShark.size)));
            }
          }
        }else {
          ActionSelect();
          board.checkMatch(TileRefactor.this);
        }
        return super.touchDown(event, x, y, pointer, button);
      }
    });
  }

  public void ActionMoveArc(Array<Vector2> arrVec, float dur, boolean dir,Runnable runnable){
//    IdAni.setTouchable(Touchable.disabled);
    this.addAction(Actions.sequence(
            GPathAction.init(arrVec.toArray(Vector2.class),dur,dir),
            Actions.run(()->{
            })
    ));
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    super.draw(batch, parentAlpha);
    batch.draw(block, 0, 0, block.getRegionWidth()/2f, block.getRegionHeight()/2f, block.getRegionWidth(), block.getRegionHeight(), 1, 1, 1);
    batch.draw(IdAni, 0, 0, block.getRegionWidth()/2f, block.getRegionHeight()/2f, block.getRegionWidth(), block.getRegionHeight(), 1, 1, 1);

  }

}
