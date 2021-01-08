package com.ss.gameLogic.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.ss.commons.TextureAtlasC;
import com.ss.core.action.exAction.GScreenShakeAction;
import com.ss.core.action.exAction.GSimpleAction;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.gameLogic.config.Config;
import com.ss.scenes.GameScene;

public class Tile {
  public Image            block,OverLay;
  private Board           board;
  private int             Layer       = 0;
  private int             row,col;
  public  float           x,y;
  public  int             id;
  public  int             kind;
  private Image           IdAni;
  public Group            gr = new Group();
  private Array<Integer>  arrActionShark = new Array<>();
  private Group           group;
  private int             idEff   =0;

  public Tile(Group group, Board board){
    this.board  = board;
    this.group  = group;
    gr.setOrigin(Align.center);
    group.addActor(gr);
    arrActionShark.add(1);
    arrActionShark.add(-1);
    if(board!=null){
      addevent();
    }


  }
  public void createID(int Id){
    this.id = Id;
    IdAni = GUI.createImage(TextureAtlasC.AnimalsAtlas,""+id);
    IdAni.setPosition(block.getX(Align.center),block.getY(Align.center),Align.center);
    gr.addActor(IdAni);
  }

  public void setPos(float x ,float y,int kind,boolean move,int index){
    block = GUI.createImage(TextureAtlasC.uiAtlas,"cucxilau");
    gr.setSize(block.getWidth(),block.getHeight());
    gr.setOrigin(Align.center);
    gr.addActor(block);
    this.kind = kind;
    if(move==true){
      gr.setPosition(GStage.getWorldWidth()/2,-GStage.getWorldHeight()/2-group.getHeight()/2);
      gr.addAction(Actions.sequence(
              Actions.delay(0.01f*index),
              Actions.moveTo(x-block.getWidth()-((Layer-1)* Config.paddingX)+gr.getWidth()/2,y-block.getHeight()/2-((Layer-1)* Config.paddingY),Config.DuraMove,Interpolation.swingOut),
              GSimpleAction.simpleAction((d,a)->{
                this.x=gr.getX();
                this.y=gr.getY();
                return true;
              })
      ));
    }else {
      gr.setPosition(x-block.getWidth()*0.6f,y-block.getHeight()/2);
      this.x=gr.getX();
      this.y=gr.getY();
      this.kind = kind;
//      gr.debug();
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
    System.out.println("check Layer: "+Layer);
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
    gr.setZIndex(set);
  }
  public int getId(){return id;}
  public void setColor(Color set){
    block.setColor(set);
    IdAni.setColor(set);
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
    if(block.getColor()==Color.DARK_GRAY)
      return true;
    return false;
  }
  public void ActionSelectLock(int s){
    gr.addAction(Actions.sequence(
            GScreenShakeAction.screenShake1(Config.DuraShake,5,gr)
    ));
  }
  private void ActionSelect(){
    gr.addAction(Actions.sequence(
            Actions.scaleTo(1,0.9f,Config.DuraSelect),
            Actions.scaleTo(1,1f,Config.DuraSelect)
    ));
  }
  public void ActionHint(){
    gr.addAction(Actions.sequence(
            Actions.rotateBy(-10,Config.DuraHint),
            Actions.rotateBy(10,Config.DuraHint)
    ));
  }
  private Vector2 effPos(){
    return new Vector2(gr.getParent().localToStageCoordinates(new Vector2(gr.getX(Align.center),gr.getY(Align.center))));
  }
  public void dispose(){
    if(GameScene.effect!=null){
      GameScene.effect.StartEff(effPos().x,effPos().y);
    }
    gr.clear();
    gr.remove();
  }
  public void changeId(int Id){
    IdAni.clear();
    IdAni.remove();
    createID(Id);
////    gr.addAction(Actions.sequence(
////            Actions.moveTo(GStage.getWorldWidth()/2,GStage.getWorldHeight()/2,0.5f),
////            GSimpleAction.simpleAction((d,a)->{
////              this.id = Id;
////              IdLb.setText(""+id);
////              return true;
////            }),
////            Actions.moveTo(x,y,0.5f)
////    ));
//    this.id = Id;
//    IdLb.setText(""+id);
  }

  private void addevent(){
    gr.addListener(new ClickListener(){
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//        if(gameScene.effect!=null)
//          idEff = gameScene.effect.StartEffSelect(gr.getX(Align.right)-5,gr.getY(Align.top));
        select(true);
        System.out.println("check tile click: "+getRowCol().x+"__"+getRowCol().y+"__"+getKind()+"__"+getLayer());
        Array<Tile> arrTile = board.getLock(Tile.this);
        Array<Tile> arrTile2 = board.getLockLayer(Tile.this);
        if(arrTile!=null && arrTile.size!=0){
//          if(gameScene.effect!=null)
//            gameScene.effect.FreeEfSelect(idEff);
          select(false);
          for (Tile t : arrTile){
              t.ActionSelectLock(arrActionShark.get((int)(Math.random()*arrActionShark.size)));
          }

        }else if(board.checkLayer(Tile.this)==1) {
//          if(gameScene.effect!=null)
//            gameScene.effect.FreeEfSelect(idEff);
          select(false);
          Array<Tile> arr = new Array<>();
          System.out.println("check size arrTile2: "+arrTile2.size);
          System.out.println("check this tile: "+getRowCol().x+"_"+getRowCol().y+"_"+getKind()+"_"+getLayer());
          for (int i=0;i<arrTile2.size;i++){
            System.out.println(arrTile2.get(i));
            if(arrTile2.get(i)!=null){
              arr.add(arrTile2.get(i));
            }
          }
          System.out.println("check size arrTile2: "+arrTile2.size);
          if(arr.size>=2){
            for (Tile t : arr){
              System.out.println("tile orthe layer: "+t.getRowCol().x+"_"+t.getRowCol().y+"_"+t.getKind()+"_"+t.getLayer());
                t.ActionSelectLock(arrActionShark.get((int)(Math.random()*arrActionShark.size)));
            }
          }
        }else {
          ActionSelect();
          board.checkMatch(Tile.this);
        }
        return super.touchDown(event, x, y, pointer, button);
      }
    });
  }


}
