package com.ss.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.ss.commons.TextureAtlasC;



public class effectWin extends Actor{
    ////////// file handel/////
    FileHandle match = Gdx.files.internal("particle/match");
    FileHandle select = Gdx.files.internal("particle/select");
//    FileHandle lightyelow = Gdx.files.internal("particle/lightYellow");
//    FileHandle tree = Gdx.files.internal("particle/tree");
//    FileHandle merge = Gdx.files.internal("particle/merge");
//    FileHandle test = Gdx.files.internal("particle2/cu-lu");
    //////// index handel ///////
    public static int Match = 1;
    public static int Select = 2;
    public static int Tree = 3;
    public static int Merge = 4;
    public ParticleEffect effect;
    public ParticleEffectPool effectPool;
    public ParticleEffectPool.PooledEffect pooledEffect;
    private Actor parent = this.parent;
    private Group group;
    private Array<Sprite> arrSprite= new Array<>();
    public boolean isAlive = false;

    public effectWin(int id, float f, float f2, Group group) {

        this.group = group;
        this.effect = new ParticleEffect();
        this.effectPool = new ParticleEffectPool(effect,0,100);
        this.pooledEffect = effectPool.obtain();
        setX(f);
        setY(f2);
            if(id==Match) {
                this.effect.load(match, TextureAtlasC.effectAtlas);
                for (int i = 0; i < this.effect.getEmitters().size; i++) {
                    ((ParticleEmitter) this.effect.getEmitters().get(i)).flipY();
                    ((ParticleEmitter) this.effect.getEmitters().get(i)).setFlip(true, false);
                }
                this.effect.scaleEffect(1f);
            }else if(id==Select) {
                this.effect.load(select, TextureAtlasC.effectAtlas);
                for (int i = 0; i < this.effect.getEmitters().size; i++) {
                    ((ParticleEmitter) this.effect.getEmitters().get(i)).flipY();
                    ((ParticleEmitter) this.effect.getEmitters().get(i)).setFlip(true, false);
                }
                this.effect.scaleEffect(1f);
            }
        this.effect.setPosition(f, f2);
    }
    public void resetSprites(){
       // for (int i = 0; i < this.effect.getEmitters().size; i++) {
            this.effect.getEmitters().get(0).getSprites().clear();
            this.effect.getEmitters().get(0).getSprites().addAll(arrSprite);

       // }
    }
    public void changeSprites(int id){
        resetSprites();
//        this.effect.getEmitters().get(0).getSprites().swap(0,(id-1));
//        System.out.println("check: "+this.effect.getEmitters().get(0).getSprites().size);
        if(this.effect.getEmitters().get(0).getSprites()!=null)
            this.effect.getEmitters().get(0).getSprites().swap(0,(id-1));

    }
    public void changeEffect(int id){

    }
    public void SetPosition(float x,float y){
        this.effect.setPosition(x,y);
    }

    @Override
    public boolean remove() {
        if(pooledEffect!=null)
            pooledEffect.free();
        return super.remove();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        this.effect.setPosition(getX(), getY());
        this.effect.update(delta);
        if(this.effect.isComplete()){
            remove();
            isAlive=false;
        }
    }



    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (!this.effect.isComplete()) {
            this.effect.draw(batch);
            return;
        }
        this.effect.dispose();
    }


    public void setScale(float ratio){
        this.effect.scaleEffect(ratio);
    }

    public void setScale(float ratioX, float ratioY){
        this.effect.scaleEffect(ratioX, ratioY);
    }

    public void start() {
        isAlive =true;
        this.group.addActor(this);
        this.effect.start();
    }

}
