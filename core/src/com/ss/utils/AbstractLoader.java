package com.ss.utils;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

//todo good old engineering stuff :)
public abstract class AbstractLoader<T> extends AsynchronousAssetLoader<T, AbstractLoader.AbstractParameter<T>> {
  Json json  = new Json();
  T     step  = null;

  public AbstractLoader(FileHandleResolver resolver) {
    super(resolver);
    json.setTypeName(null);
    json.setUsePrototypes(false);
    json.setIgnoreUnknownFields(true);
    json.setOutputType(JsonWriter.OutputType.json);
  }

  @Override
  public void loadAsync(AssetManager manager, String fileName, FileHandle file, AbstractParameter parameter) {
    step = parse(fileName, file, json);
  }

  public abstract T parse(String fileName, FileHandle file, Json json);

  @Override
  public T loadSync(AssetManager manager, String fileName, FileHandle file, AbstractParameter parameter) {
    T res =  step;
    step = null;
    return res;
  }

  @SuppressWarnings("rawtypes")
  @Override
  public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, AbstractParameter parameter) {
    return null;
  }

  public static class AbstractParameter<T> extends AssetLoaderParameters<T> {

  }
}