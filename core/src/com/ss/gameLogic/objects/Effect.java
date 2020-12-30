package com.ss.gameLogic.objects;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.ss.core.util.GLayer;
import com.ss.core.util.GStage;
import com.ss.effects.effectWin;

public class Effect {
  private Group group = new Group();
  private Array<effectWin> arrEffMatch           = new Array<>();
  private Array<effectWin> arrEffSelect          = new Array<>();
  private int              idTemp                = 0;


  public Effect(){
    GStage.addToLayer(GLayer.top,group);
    initEff();
  }

  public void initEff(){
    for (int i=0;i<20;i++){
      effectWin ef = new effectWin(effectWin.Match,0,0,group);
      arrEffMatch.add(ef);
      effectWin ef2 = new effectWin(effectWin.Select,0,0,group);
      arrEffSelect.add(ef2);
    }
  }
  public effectWin getEfMatch(){
    if(arrEffMatch != null && arrEffMatch.size != 0){
      for (int i=0;i<arrEffMatch.size;i++){
        if(!arrEffMatch.get(i).isAlive){
          return arrEffMatch.get(i);
        }
      }
    }
    return null;
  }

  public effectWin getEfSelect(){
    if(arrEffSelect != null && arrEffSelect.size != 0){
      for (int i=0;i<arrEffSelect.size;i++){
        if(!arrEffSelect.get(i).isAlive){
          return arrEffSelect.get(i);
        }
      }
    }
    return null;
  }

  public void StartEff( float x, float y){
    effectWin ef          = getEfMatch();
    if(ef!=null ){
      ef.setPosition(x,y);
      ef.start();

    }
  }
  public int StartEffSelect(float x, float y){
    effectWin ef         = getEfSelect();
    if(ef!=null ){
      ef.setPosition(x,y);
      ef.start();
      idTemp = arrEffSelect.indexOf(ef,true);
      return idTemp;
    }
    return -1;
  }


  public void FreeEfSelect(int id){
    if(id==-1)
      return;
    System.out.println("size :"+arrEffSelect.size);
    System.out.println("size-idtemp :"+idTemp);

    if(id<arrEffSelect.size && arrEffSelect!=null){
      arrEffSelect.get(id).remove();
      arrEffSelect.get(id).isAlive = false;
    }
  }

  public void FreeAllEfSelect(){
    for(int i = 0; i < arrEffSelect.size; i++) {
      if(arrEffSelect.get(i).isAlive){
        FreeEfSelect(i);
      }
    }
  }

  }
