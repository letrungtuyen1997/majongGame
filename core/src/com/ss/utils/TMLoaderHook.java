//package com.ss.utils;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.assets.AssetManager;
//import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
//import com.badlogic.gdx.files.FileHandle;
//import com.badlogic.gdx.utils.Array;
//import com.ss.interfaces.LoaderHook;
//
//
//@SuppressWarnings("unused")
//public class TMLoaderHook implements LoaderHook<Level> {
//  private final String          levelPath       = "level/";
//  private AssetManager am;
//  @Override
//  public void load(AssetManager am) {
//    am.setLoader(Level.class, new TMLevelLoader(new InternalFileHandleResolver()));
//
//    Array<String> res = new Array<>();
//    FileHandle dirHandle = Gdx.files.internal("level/");
//    for (FileHandle file : dirHandle.list()) {
//      if (!file.isDirectory() && !file.name().equals(".ds_store"))
//        am.load(levelPath + file.name(), Level.class, new AbstractLoader.AbstractParameter<>());
//    }
//  }
//
//  @Override
//  public void finish(AssetManager am) {
//    this.am = am;
//  }
//
//  @Override
//  public Level get(String key) {
//    return am.get(levelPath + key, Level.class);
//  }
//}