package com.ss.utils;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
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
    Json j = new Json();
    JsonValue value = new JsonReader().parse(file);
    Array<LevelJson> arrLevel = new Array<>();
    for (int i=0;i<value.size;i++ ){
      LevelJson lv = j.fromJson( LevelJson.class , value.get(i).toString());
      arrLevel.add(lv);
    }
//    JsonReader jsonReader = new JsonReader();
//    JsonValue value = jsonReader.parse(json.toString());
    step = (T) parse(fileName, file, arrLevel);
  }

  public abstract Level parse(String fileName, FileHandle file, Array<LevelJson> arrLv);

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