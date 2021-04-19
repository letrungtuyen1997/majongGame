package com.ss.interfaces;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;

@SuppressWarnings("unused")
public interface LoaderHook<T> {
  void load(AssetManager loader);
  void finish(AssetManager loader);
  T get(String key);
  Array<T> getList(String key);
}