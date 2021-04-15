package com.ss.interfaces;

import com.badlogic.gdx.assets.AssetManager;

@SuppressWarnings("unused")
public interface LoaderHook<T> {
  void load(AssetManager loader);
  void finish(AssetManager loader);
  T get(String key);
}