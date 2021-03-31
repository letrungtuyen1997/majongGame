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
      effectWin ef = new effectWin(effectWin.Match,0,0,1,group);
      arrEffMatch.add(ef);
      effectWin ef2 = new effectWin(effectWin.Select,0,0,0.85f,group);
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



  public void StartEff( float x, float y){
    effectWin ef          = getEfMatch();
    if(ef!=null ){
      ef.setPosition(x,y);
      ef.start();

    }
  }




  }
