package com.ss;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.ss.commons.TextureAtlasC;

public class PoolTile {
    public static Array<TextureRegion> arrBlock = new Array<>();
    public static Array<Boolean>      arrBlockFree = new Array<>();
    public static Array<Array<TextureRegion>> arrAni = new Array<>();
    public static Array<Array<Boolean>>      arrAniFree = new Array<>();

    public PoolTile(){
        initArrTile();
    }
    public static void initArrTile(){
        for (int i=0;i< 47;i++){
            Array<TextureRegion>  arr = new Array<>();
            Array<Boolean>        arr2 = new Array<>();
            for (int j=0;j<20;j++){
                TextureRegion block = TextureAtlasC.uiAtlas.findRegion("cucxilau");
                TextureRegion ani = TextureAtlasC.AnimalsAtlas.findRegion(""+i);
                arrBlock.add(block);
                arr.add(ani);
                arrBlockFree.add(true);
                arr2.add(true);
            }
            arrAni.add(arr);
            arrAniFree.add(arr2);
        }
    }
    public static TextureRegion getBlock(){
        for (Boolean b: arrBlockFree){
            if(b){
                int index = arrBlockFree.indexOf(b,true);
                arrBlockFree.set(index,false);
                return arrBlock.get(index);

            }
        }
        return TextureAtlasC.uiAtlas.findRegion("cucxilau");
    }
    public static void FreeBlock(TextureRegion block){
        int index = arrBlock.indexOf(block,true);
        arrBlockFree.set(index,true);
    }

    public static TextureRegion getAni(int id){
       for (Boolean b: arrAniFree.get(id)){
           if(b){
               int index = arrAniFree.get(id).indexOf(b,true);
               arrAniFree.get(id).set(index,false);
               return arrAni.get(id).get(index);
           }
       }
        return TextureAtlasC.AnimalsAtlas.findRegion(""+id);
    }

    public static void FreeAni(TextureRegion ani,int id){
        int index = arrAni.get(id).indexOf(ani,true);
        arrAniFree.get(id).set(index,true);
    }

}
