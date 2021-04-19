package com.ss.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.platform.IPlatform;
import com.ss.GMain;

import java.util.Date;

public class Utils {
    public static String result ="";
    public static JsonValue data[];
    public static void initLoadData(){
       data = loadData();
    }
    public static JsonValue[] getLv(){
      if(data.length==0)
        initLoadData();
      else
        return data;
      return null;
    }
    private Utils(){

    }
    public static JsonValue GetJsV(String s){
        JsonReader JReader= new JsonReader();
        return JReader.parse(s);
    }
    public static class LeaderBoard{
      public String name;

      public String getName() {
        return name;
      }

      public void setName(String name) {
        this.name = name;
      }

      public int getStar() {
        return star;
      }

      public void setStar(int star) {
        this.star = star;
      }

      public int    star;
    }


    public static class JsonLevel{
//        @Serialization
        public int row;

//        @Serialization
        public int col;

//        @Serialization
        public int layer;

//        @Serialization
        public float x;

//        @Serialization
        public float y;

//        @Serialization
        public int kind;

//        @Serialization
        public int id;

        public String name;


        public String getName() {
          return name;
        }

        public void setName(String name) {
          this.name = name;
        }

        public int getRow() {
            return row;
        }

        public void setRow(int row) {
            this.row = row;
        }

        public int getCol() {
            return col;
        }

        public void setCol(int col) {
            this.col = col;
        }

        public int getLayer() {
            return layer;
        }

        public void setLayer(int layer) {
            this.layer = layer;
        }

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }

        public int getKind() {
            return kind;
        }

        public void setKind(int kind) {
            this.kind = kind;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }


    }
    public static String compressCoin(long num, int numOf){
        String str = "0";
        String dv = "";
        int ratio = 0;
        double x = 0;

        if(num >= 1000000000){
            ratio = 1000000000;
            dv = "B";
        }
        else if(num >= 1000000){
            ratio = 1000000;
            dv = "M";
        }
        else if(num >= 1000){
            ratio = 1000;
            dv = "K";
        }
        else {
            ratio = 1;
            dv = "";
        }
        x = (double)num/ratio;
        x = Math.floor(x*Math.pow(10, numOf))/Math.pow(10, numOf);
        str = x + dv;


        String strTemp = str.substring(str.length() - 2, str.length());
        if(strTemp.equals(".0")){
            str = str.substring(0, str.length() - 2);
        }
        else {
            strTemp = str.substring(str.length() - 3, str.length()-1);
            if(strTemp.equals(".0")){
                str = str.substring(0, str.length()-3);
                str += dv;
            }
        }

        return str;
    }
    public static String[] readData(){

        String StrLv = Gdx.files.internal("data/data.txt").readString();
       return StrLv.split("\n");
    }
  public static JsonValue[] loadData() {
    FileHandle files = Gdx.files.internal("level/");
    return loadLevel(files.list((file, s) -> {
      String lowercaseName = s.toLowerCase();
      if (lowercaseName.endsWith(".ds_store"))
        return false;
      return true;
    }));
  }
  public static JsonValue[] loadLevel(FileHandle[] list) {
    if (list == null)
      return new JsonValue[0];
    Array<JsonValue> result = new Array<>();

    for (int i = 0; i < list.length; i++) {
//      JsonValue levelJV = json.parse(list[i]);
      JsonValue levelJV = GetJsV(list[i].readString());
      result.add(levelJV);
      result.get(i).setName(list[i].nameWithoutExtension());
    }

    result.sort((a, b) -> Integer.parseInt(a.name) - Integer.parseInt(b.name));
    return result.toArray(JsonValue.class);
  }


}
