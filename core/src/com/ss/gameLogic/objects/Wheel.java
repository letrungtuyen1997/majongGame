package com.ss.gameLogic.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.ss.commons.TextureAtlasC;
import com.ss.core.util.GUI;
import com.ss.effects.SoundEffect;
import com.ss.gameLogic.config.Config;
import com.ss.gdx.NSound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
//import java.util.stream.Collectors;
//import java.util.stream.IntStream;

@SuppressWarnings("unused")
public class Wheel extends Group {
  public  static        TextureRegion                wheelTex;                                //texture vòng xoay
  public  static        TextureRegion                pointer;                                 //texture con trỏ xoay
  public  static        TextureRegion                wheelDot;                                //texture nút tròn
  public  static        TextureRegion                lightDot;                                //texture đèn nháy
//  public  static        TextureRegion                knife;                                   //texture dao
  public  static        TextureRegion                Skip;                                    //texture Skip
  public  static        Sound                        wheelTick;                               //âm thanh xoay
  public  static        BitmapFont                   wheelText;                               //font số lượng item
  public  static        List<WheelItem>              wheelItems          = new ArrayList<>(); //thông tin từ item
  public  static        int                          PARTITION           = 12;                //số phần chia, wheelItems.size() phải = PARTITION
  public  static        float                        TEXT_SPACE          = 6;                 //khoảng cách chữ
  public  static        boolean                      Y_DOWN              = true;              //tọa đồ y down hay y up
  private static        float                        WHEEL_ARC           = 30;                // bn độ trên 1 cung (360/PARTITION)
  public static         boolean                      USE_DRAG            = false;             // bn độ trên 1 cung (360/PARTITION)

  private static final  boolean                      RENDER_ITEM         = true;             //có vẽ icon item hay ko
  public static boolean                              RENDER_KNIFE        = false;             //có vẽ icon item hay ko
  private static final  boolean                      RENDER_POINTER      = false;             //có vẽ icon  hay ko
  private static final  boolean                      RENDER_LIGHTDOT     = true;             //có vẽ icon  hay ko
  public  static        float                        ITEM_SCALE          = 1;                 //scale của whell item
  public  static        float                        ITEM_FLOAT          = 0.6f;              //vẽ item gần hay xa tâm tròn
  public  static        float                        KNIFE_FLOAT         = 1.1f;              //vẽ item gần hay xa tâm tròn
  public  static        float                        SKIP_FLOAT          = 0.42f;              //vẽ item gần hay xa tâm tròn
  private static final  float                        DOT_FLOAT           = 0.98f;             //vẽ text số lượng gần hay xa tâm tròn
  private static final  boolean                      RENDER_TEXT         = true;              //có vẽ text số lượng hay ko
  private static final  boolean                      POLAR_TEXT_RENDER   = true;              //true: vẽ text phóng từ tâm, false: vẽ xung quanh rìa đường tròn
  private static final  float                        ANGULAR_TEXT_FLOAT  = 0.6f;             //font số lượng nằm gần hay xa tâm tròn, TH vẽ xoay vòng
  private static final  float                        POLAR_TEXT_FLOAT    = 0.44f;             //font số lượng nằm gần hay xa tâm tròn, TH vẽ phóng từ tâm
  private static final  HashMap<Integer, WheelItem>  wheelItemMap        = new HashMap<>();

  private static final  float                        FLASH_DURATION      = 0.4f;              // thời gian nhấp nháy đèn
  private static final  int                          VELOCITY_SCALE      = 10;                // scale vận tốc xoay, càng cao càng xoay nhanh
  private static final  int                          ROLLING_DURATION    = 5;                // thời gian xoay tính bằng giây
  private static final  int                          MIN_ANGULAR_VEL     = 2000;              // vận tốc xoay tốt thiểu
  private static final  int                          MAX_ANGULAR_VEL     = 4000;              // vận tốc xoay tối đa
  private static final  float                        POINTER_Y_OFFSET    = 1.85f;             // chỉnh vị trí cho con trỏ (cao thấp)
  private static final  int                          TOTAL_PERCENT       = 10000;
  public  static int                                 Outcome             = 19;
  private static        Wheel                        inst;

  private               Image                        wheel;
  public                Group                        wheelGroup;
  private               Group                        knifeGroup;
  private               Vector2                      cp;
  private               List<Vector2>                dotPos              = new ArrayList<>();
  private               float                        acc                 = 0;
  private               EventListener                listener;
  private               boolean                      muteDrawing         = false; //when errors detected
  public                boolean                      lock                = false;
  public                Batch                        batch               = new SpriteBatch();
  public                Array<Image>                 arrKnife            = new Array<>();
  public                Array<Float>                 arrRotation         = new Array<>();
  public                Array<Image>                 arrSkip             = new Array<>();
  public                boolean                      Delay               = true;
  public                float xk;
  public                float yk;
  public                float okx;
  public                float oky;
  public                float angK;
  public                int   indexSkip                                  =-1;

  public static Wheel inst() {
    if (inst == null) {
      inst = new Wheel();
      return inst;
    }
    return inst;
  }

  private void addSkipItem(Group group){
    //if(RENDER_KNIFE){

    List<Tuple<Vector2, WheelItem>>   items         = calcItemPosition3();
    for (int i=0;i<PARTITION;i++){

      Image Skip = new Image(Wheel.Skip);
      int iw          = Wheel.Skip.getRegionWidth();
      int ih          = Wheel.Skip.getRegionHeight();
      float ix        = items.get(i).position.x;
      float iy        = items.get(i).position.y;
      float ox        = iw/2;
      float oy        = ih/2;
      float scl       = 1;
      float ang       = i*WHEEL_ARC;

      Skip.setPosition(ix, iy);
      Skip.setScale(-1,1);
      Skip.setOrigin(ox, oy);
      Skip.setRotation(ang);
      Skip.setZIndex(0);
      group.addActor(Skip);
      arrSkip.add(Skip);
//      arrSkip.get(i).setName(""+wheelItems.get(i).pos);
      //System.out.println("get pos Skip: "+arrSkip.get(i).getName());
      Skip.setVisible(false);
      //batch.draw(items.get(i).wheelItem.tex2, ix, iy, ox, oy, iw, ih, scl,scl, ang);
      //}

    }

  }
  public void init() {
    try {
      if (!validateWheelSetting()) {
        muteDrawing = true;
        throw new IllegalStateException("wheel setting wrong");
      }

      wheelItemMap.clear();
      int totalPercent = 0;
      for (int i = 0; i < wheelItems.size(); i++)
        totalPercent += wheelItems.get(i).percent;
      //int totalPercent = wheelItems.stream().mapToInt(i -> i.percent).sum();
      if (totalPercent != TOTAL_PERCENT)
        throw new IllegalStateException("wheel percent inconsistency: " + totalPercent);

      for (WheelItem items : wheelItems)
        wheelItemMap.put(items.id, items);
      //wheelItems.forEach(it -> wheelItemMap.put(it.id, it));

      WHEEL_ARC = (float) 360 / PARTITION;
      cp = new Vector2(wheelTex.getRegionWidth()/2, wheelTex.getRegionHeight()/2);
      combine();
      wheelGroup = new Group();
      knifeGroup = new Group();
//      knifeGroup.setScale(-1);
//      knifeGroup.setOrigin(Align.center);
      wheel = new Image(new TextureRegionDrawable(wheelTex));
//      wheel.setScale(1,-1);
      wheel.setOrigin(wheel.getWidth()/2, wheel.getHeight()/2);
      if (USE_DRAG)
        wheel.addListener(WheelDragListener.inst());
      wheelGroup.addActor(wheel);
      wheelGroup.setScale(-1,-1);
//      wheelGroup.setOrigin(Align.center);
//      addKnifes(knifeGroup);
//      addSkipItem(knifeGroup);
      knifeGroup.setScale(1,-1);
      knifeGroup.setOrigin(Align.center);
      wheelGroup.addActor(knifeGroup);
      addActor(wheelGroup);
      // wheelGroup.setOrigin(Align.center);

      //wheelGroup.setPosition(0,0,Align.center);
      wheelGroup.setPosition(this.getX(), this.getY());
      wheelGroup.setSize(wheel.getWidth(), wheel.getHeight());
      wheelGroup.setOrigin(wheel.getWidth()/2, wheel.getHeight()/2);
      // wheel.setZIndex(20);

      knifeGroup.setPosition(this.getX(),this.getY());
      knifeGroup.setSize(wheelGroup.getWidth(),wheelGroup.getHeight());

      knifeGroup.setOrigin(wheel.getWidth()/2,wheel.getHeight()/2);
      //  wheel.setPosition(0,0, Align.center);

    }
    catch (Exception e) {
      muteDrawing = true;
      removeListener(WheelDragListener.inst());
      if (listener != null) {
        listener.error(e.getMessage());
      }
    }
  }


  public void setWheelListener(EventListener listener) {
    this.listener = listener;
  }

  private boolean validateWheelSetting() {
    return  !(PARTITION <= 0                    ||
            TEXT_SPACE <= 0                   ||
            wheelTex == null                  ||
            wheelText == null                 ||
            pointer == null                   ||
            wheelDot == null                  ||
            lightDot == null                  ||
//            knife    == null                  ||
            wheelTick == null                 ||
            wheelItems == null                ||
//            listener == null                  ||
            wheelItems.size() != PARTITION);
  }

  @Override
  public void act(float delta) {
    acc += delta;
    super.act(delta);
  }

  private static float r = 0;
  private static Vector2 pos = new Vector2();
  private static Vector2 posKnife = new Vector2();
  //List<Tuple<Vector2, WheelItem>>   itemsPos         = calcItemPosition();
  @Override
  public void draw(Batch batch, float parentAlpha) {
    super.draw(batch, parentAlpha);

    if (muteDrawing)
      return;

    float               scl               = getScaleX();

    //render light dot
    if (RENDER_LIGHTDOT) {
      for (int i = 0; i < PARTITION; i++) {


        if (acc < FLASH_DURATION) {
          if (i % 2 == 0) {
            float w = lightDot.getRegionWidth();
            float h = lightDot.getRegionHeight();
            float ox = w / 2f;
            float oy = h / 2f;
            pos.set(0, wheelTex.getRegionHeight() * DOT_FLOAT / 2f)
                    .rotate(wheelGroup.getRotation() + i * WHEEL_ARC + WHEEL_ARC / 2)
                    .add(cp).scl(scl).add(getX(), getY()).sub(w / 2, h / 2);
            batch.draw(lightDot, pos.x, pos.y, ox, oy, w, h, scl, scl, 0);
          }

        } else if (acc > FLASH_DURATION && acc < FLASH_DURATION * 2) {
          if (i % 2 == 1) {
            float w = lightDot.getRegionWidth();
            float h = lightDot.getRegionHeight();
            float ox = w / 2f;
            float oy = h / 2f;
            pos.set(0, wheelTex.getRegionHeight() * DOT_FLOAT / 2f)
                    .rotate(wheelGroup.getRotation() + i * WHEEL_ARC + WHEEL_ARC / 2)
                    .add(cp).scl(scl).add(getX(), getY()).sub(w / 2, h / 2);
            batch.draw(lightDot, pos.x, pos.y, ox, oy, w, h, scl, scl, 0);
          }
        } else {
          acc = 0;
        }
      }
    }

    if (RENDER_POINTER) {
      //render pointer
      float pX          = (cp.x - pointer.getRegionWidth()/2f)*scl + getX();
      float flipYShift  = Y_DOWN ? -cp.y*2 - pointer.getRegionHeight()*0.7f: 0;
      float pY          = (cp.y*POINTER_Y_OFFSET + flipYShift)*scl + getY();
      float oX          = (pointer.getRegionWidth()/2f)*scl;
      float oY          = (pointer.getRegionHeight()*0.7f)*scl;
      float pW          = pointer.getRegionWidth()*scl;
      float pH          = pointer.getRegionHeight()*scl;
      float d           = WheelDragListener.angularVel > 0 ? -1 : 1;
      float magnitude   = 1.2f;
      float angle       = Math.abs(wheelGroup.getRotation() % WHEEL_ARC);
      float sclY        = Y_DOWN ? -1 : 1;

      if (angle > WHEEL_ARC/4f && angle < WHEEL_ARC*3/4f)
        r = d * angle * magnitude;
      else
        r = r < 0 ? ++r > 0 ? 0 : r : --r < 0 ? 0 : r;                    //........!!!!

      batch.draw(pointer, pX, pY, oX, oY, pW, pH, 1, sclY, r);
    }
//    createKnife(0);

  }

  private List<Tuple<Vector2,WheelItem >> calcItemPosition() {
    Vector2 roller = Vector2.Zero.setAngle(0).set(0, wheelTex.getRegionHeight()*ITEM_FLOAT/2);
    System.out.println("check roller: "+roller);
    List<Tuple<Vector2, WheelItem>> result = new ArrayList<>();
    for (int i = 0; i < PARTITION; i++) {
      TextureRegion region = wheelItems.get(i)/*!*/.tex;
      Vector2 pos = roller.cpy().rotate(i*WHEEL_ARC).add(cp)
              .sub(region.getRegionWidth()/2, region.getRegionHeight()/2);
      result.add(new Tuple<>(pos, wheelItems.get(i)));
    }
    return result;

  }
  private List<Tuple<Vector2,WheelItem >> calcItemPosition3() {
    Vector2 roller = Vector2.Zero.setAngle(0).set(0, wheelTex.getRegionHeight()*SKIP_FLOAT/2);
    System.out.println("check roller: "+roller);
    List<Tuple<Vector2, WheelItem>> result = new ArrayList<>();
    for (int i = 0; i < PARTITION; i++) {
      TextureRegion region = Wheel.Skip;
      Vector2 pos = roller.cpy().rotate(i*WHEEL_ARC).add(cp)
              .sub(region.getRegionWidth()/2, region.getRegionHeight()/2);
      result.add(new Tuple<>(pos, wheelItems.get(i)));
    }
    return result;

  }


  private List<Vector2> calcDotPosition(float shiftRadian) {
    Vector2 roller = Vector2.Zero.setAngle(0).set(0, wheelTex.getRegionHeight()* Wheel.DOT_FLOAT /2);
    List<Vector2> result = new ArrayList<>();
    for (int i = 0; i < PARTITION; i++) {
      result.add(roller.cpy().rotate(i*WHEEL_ARC + shiftRadian)
              .add(cp).sub(wheelDot.getRegionWidth()/2, wheelDot.getRegionHeight()/2));
    }
    return result;
//    return IntStream.range(0, PARTITION)
//    .mapToObj(i -> roller.cpy().rotate(i*WHEEL_ARC + shiftRadian)
//      .add(cp).sub(wheelDot.getRegionWidth()/2, wheelDot.getRegionHeight()/2))
//    .collect(Collectors.toList());
  }

  //  static int sclKinfe=0;
  private void combine() {
    int                               h             = wheelTex.getRegionWidth();
    int                               w             = wheelTex.getRegionHeight();
    Matrix4                           projector     = new Matrix4();
//    Batch                             batch         = new SpriteBatch();
    List<Tuple<Vector2, WheelItem>>   items         = calcItemPosition();
    FrameBuffer                       fbo;

    projector.setToOrtho2D(0, 0, w, h);
    batch.setProjectionMatrix(projector);
    fbo     = new FrameBuffer(Pixmap.Format.RGBA8888, w, h, false);
    dotPos  = calcDotPosition(WHEEL_ARC/2);

    fbo.begin();batch.begin();

    batch.draw(wheelTex, 0, 0);




    //render qty text
    if (RENDER_TEXT) {
      for (int i = 0; i < PARTITION; i++) {
        AngularSpriteFont sprite;
        if (POLAR_TEXT_RENDER)
          sprite = new PolarSpriteFont(wheelItems.get(i)./*!*/qtyText, wheelText);
        else
          sprite = new AngularSpriteFont(wheelItems.get(i)./*!*/qtyText, wheelText);
        sprite.draw(batch,i*WHEEL_ARC);
      }
//      IntStream.range(0, PARTITION)
//      .forEach(i -> {
//        AngularSpriteFont sprite;
//        if (POLAR_TEXT_RENDER)
//          sprite = new PolarSpriteFont(wheelItems.get(i)./*!*/qtyText, wheelText);
//        else
//          sprite = new AngularSpriteFont(wheelItems.get(i)./*!*/qtyText, wheelText);
//        sprite.draw(batch,i*WHEEL_ARC);
//      });
    }

    //render items
    if(RENDER_ITEM) {
      for (int i = 0; i < PARTITION; i++) {
        int     iw        = wheelItems.get(i)./*!*/tex.getRegionWidth();
        int     ih        = wheelItems.get(i).tex.getRegionHeight();
        float   ix        = items.get(i).position.x;
        float   iy        = items.get(i).position.y;
        float   ox        = iw/2;
        float   oy        = ih/2;
        float   scl       = ITEM_SCALE;
        float   ang       = i*WHEEL_ARC;
        batch.draw(items.get(i).wheelItem.tex, ix, iy, ox, oy, iw, ih, -scl, scl, ang);


        //render dots
        if(RENDER_LIGHTDOT){
          iw    =  wheelDot.getRegionWidth();
          ih    =  wheelDot.getRegionHeight();
          ix    =  dotPos.get(i)./*!*/x;
          iy    =  dotPos.get(i).y;
          ox    =  iw/2;
          oy    =  ih/2;
          scl   =  1;
          ang   =  0;
          batch.draw(wheelDot, ix, iy, ox, oy, iw, ih, scl, scl, ang);
          System.out.println("render dot: ");
        }


      }
//      IntStream.range(0, PARTITION)
//      .forEach(i -> {
//        int     iw        = wheelItems.get(i)./*!*/tex.getRegionWidth();
//        int     ih        = wheelItems.get(i).tex.getRegionHeight();
//        float   ix        = items.get(i).position.x;
//        float   iy        = items.get(i).position.y;
//        float   ox        = iw/2;
//        float   oy        = ih/2;
//        float   scl       = ITEM_SCALE;
//        float   ang       = i*WHEEL_ARC;
//        batch.draw(items.get(i).wheelItem.tex, ix, iy, ox, oy, iw, ih, scl, scl, ang);
//
//        //render dots
//        iw    =  wheelDot.getRegionWidth();
//        ih    =  wheelDot.getRegionHeight();
//        ix    =  dotPos.get(i)./*!*/x;
//        iy    =  dotPos.get(i).y;
//        ox    =  iw/2;
//        oy    =  ih/2;
//        scl   =  1;
//        ang   =  0;
//        batch.draw(wheelDot, ix, iy, ox, oy, iw, ih, scl, scl, ang);
//      });
    }
    batch.end();fbo.end();batch.dispose();

    wheelTex = new TextureRegion(fbo.getColorBufferTexture());

    wheelTex.flip(false, !Y_DOWN);
  }
  public void rollWheel(float vel, int outcome) {
    if (outcome >= PARTITION) {
      if (listener != null)
        listener.error("outcome > partition");
    }
    else {
      if (!lock) {
        Wheel.inst().wheelGroup.addAction(WheelRollAction.inst()
                .init(vel, outcome));
        lock = true;
      }
    }
  }

  public void stopWheel(int outcome) {
    if(outcome<0){
      WheelRollAction.inst().stopRoll(outcome);
      return;
    }
//    Array<Integer> arrPos = new Array<>();
//    for (int i=0;i<wheelItems.size();i++){
//      if(outcome==wheelItems.get(i).id)
//        arrPos.add(i);
//    }
//    int test = (int)(Math.random()*arrPos.size);

//    int index = arrPos.get(test);
//    int index = getRandom(arrPos);
//    int index = getIndex(arrPos);
//    getIndex(arrPos);

        for(int i=0;i<wheelItems.size();i++){
          if(outcome==wheelItems.get(i).id){
            WheelRollAction.inst().stopRoll(i);
//            setVisibleKnife(outcome);
//            Outcome = wheelItems.get(i).pos;
//            System.out.println("check result: "+Outcome);
            return;
          }
        }
  }
  private int getRandom(Array<Integer> arr){
    int test = (int)(Math.random()*arr.size);
    int index = arr.get(test);

    for (Image Skip: arrSkip){
//      System.out.println("check skip:::::::: "+Skip.getName());
      if(Skip.isVisible()==false){
        if(index==Integer.parseInt(Skip.getName())){
          return index;
        }
      }
    }
    return -1;
  }
  private void getIndex(Array<Integer> arr){
    indexSkip = getRandom(arr);
    if(indexSkip==-1){
      getIndex(arr);
    }else {
      WheelRollAction.inst().stopRoll(indexSkip);
//      setVisibleKnife(wheelItems.get(indexSkip).pos);
      return;
    }
  }



  /************************************************************************************************/

  private static class WheelDragListener extends DragListener {
    private        Vector2            initialAngularVector  = new Vector2(0,0);
    private        Vector2            angularVector         = new Vector2(0 ,0);
    private        float              initialRotation       = 0;
    private        float              acc                   = 0;
    private        float              initialAngle          = 0;
    private        float              lastRotation          = 0;
    private static float              angularVel            = 0;
    private static WheelDragListener  inst;

    static WheelDragListener inst() {
      if (inst == null)
        inst = new WheelDragListener();
      return inst;
    }

    @Override
    public void dragStart(InputEvent event, float x, float y, int pointer) {
      acc = 0;
      angularVel = 0;
      initialRotation = initialAngularVector.set(x, y).sub(Wheel.inst().cp).angle();
      initialAngle = Wheel.inst().wheelGroup.getRotation();
      super.dragStart(event, x, y, pointer);
    }


    @Override
    public void drag(InputEvent event, float x, float y, int pointer) {
      float angle         = angularVector.set(x, y).sub(Wheel.inst().cp).angle();
      float rotation      = Wheel.inst().wheelGroup.getRotation() + angle - initialRotation;
      float fuzzyDegree   = 0.5f; //theta biến thiên hơn khoản này mới đảo dấu vận tốc xoay (giảm lag)

      if (Math.abs(rotation - lastRotation) > fuzzyDegree)
        angularVel = rotation - lastRotation;
      Wheel.inst().wheelGroup.setRotation(rotation);
      acc += Gdx.graphics.getDeltaTime();
      lastRotation = rotation;
      super.drag(event, x, y, pointer);
    }

    @Override
    public void dragStop(InputEvent event, float x, float y, int pointer) {
      if (Wheel.inst().listener != null && Wheel.inst().listener.start()) {
        try {
          int outcome     = callOutcome2();
          float theta     = Wheel.inst().wheelGroup.getRotation() - initialAngle;
          angularVel      = (theta/acc)*VELOCITY_SCALE;
          acc             = 0;
          float vel       = angularVel < MAX_ANGULAR_VEL ? angularVel : MAX_ANGULAR_VEL;

          if (Math.abs(angularVel) >= MIN_ANGULAR_VEL) {
            Wheel.inst().wheelGroup.removeListener(this);
            Wheel.inst().wheelGroup.addAction(WheelRollAction.inst()
                    .init(vel, outcome));
          }
        }
        catch (Exception e) {
          Wheel.inst().listener.error(e.getMessage());
        }
      }
      super.dragStop(event, x, y, pointer);
    }
    private int callOutcome2(){
      System.out.println("Wheel size:"+wheelItems.size());
      return wheelItems.get(Outcome).id;
    }



    private int calcOutcome() {
      Random rand = new Random();
      int rate = MathUtils.random(0, TOTAL_PERCENT - 1);
      int totalPer = 0;
      for (int i = 0; i < wheelItems.size(); i++) {
        totalPer += wheelItems.get(i).percent;
        if (rate < totalPer)
          return wheelItems.get(i).id;
      }
      throw new IllegalStateException("Wheel item percents inconsistency");
    }
  }

  /************************************************************************************************/

  public static class WheelRollAction extends TemporalAction {
    private         float              velocity              = 0;
    private         float              acc                   = 0;
    private         float              coefficient           = 1;
    private         float              initAngle             = 0;
    private         int                outcome               = 0;
    private static  WheelRollAction    inst;
    public boolean                     delay                 = true; //roll infinity and slow down when something happen;

    static WheelRollAction inst() {
      if (inst == null)
        inst = new WheelRollAction();
      return inst;
    }
    private void stopRoll(int outcome) {
      delay = false;
      acc = 0;
      ////// update by id ///////
      if(outcome<0){
        this.outcome = outcome;
      }else {
        this.outcome  = wheelItems.get(outcome).id;
      }
      //Todo: fix moc neo knife//
      this.initAngle = outcome * WHEEL_ARC;
//          this.initAngle = (int)(Math.random()*20) * WHEEL_ARC;
    }

    @Override
    protected void begin() {
      acc = 0;
      coefficient = velocity/WHEEL_ARC;
      this.setReverse(true);
      super.begin();
    }
    @Override
    public boolean act(float delta) {
      if (!delay){
//        System.out.println("check test!!1");
        return super.act(delta);

      }

      else {
        _update(delta);
        return false;
      }
    }

    protected void _update(float delta) { //infinity roll, waiting for signal
      this.acc += velocity;
//      this.acc += 1080;
      float _magicNumber = 5; //dont ask me why it just work T____T
      actor.setRotation(actor.getRotation() + delta*velocity/_magicNumber);
      System.out.println("get rotation: "+Wheel.inst().wheelGroup.getRotation());
      //calc playing sound
      float magicNumber = 2;
      if (Math.abs(acc /(VELOCITY_SCALE*coefficient)) >= magicNumber) {
//        SoundEffect.Playmusic(SoundEffect.wheel_sound);
//              Wheel.wheelTick.play(0.5f);
//            int comeout =;
//            System.out.println("get rotation wheel: "+(float)(actor.getRotation()/360));
        //System.out.println("get rotation wheel: "+Wheel.inst().wheelGroup.getRotation()/360);


        this.acc = 0;
      }
    }

    @Override
    protected void update(float percent) {
      //todo: fix veloccity whell
//      this.acc += velocity*(percent*percent*percent*percent*percent);
//      actor.setRotation(initAngle -(velocity*percent*percent*percent*percent*percent));
//      this.acc += velocity*percent;
//      Config.countAng+=percent;
      this.acc += (velocity*percent*(percent*0.5f));
      actor.setRotation(initAngle -(velocity*percent*(percent*0.5f)));
//      System.out.println("check: "+Math.abs(Math.round(actor.getRotation()))+"______"+Math.round(velocity-(initAngle*2)));
//      System.out.println("check: "+);
//      if(Config.countAng==initAngle){
//        System.out.println("action here");
////        Wheel.inst().setVisibleKnife(Wheel.inst().wheelItems.get(Wheel.inst().indexSkip).pos);
//      }
      if(Math.abs(Math.round(actor.getRotation()))==Math.round(velocity-(initAngle*2))){
        System.out.println("action here");

      }

      //Todo: cheat wheel.

//      float rotation = 0;
//      if((int)velocity/360>1)
//        rotation = (int)(velocity/360)*360;
//      else
//        rotation = 1080;
//      System.out.println("count ro: "+initAngle);
//      actor.setRotation(0 -((rotation-initAngle)*percent));
//      System.out.println("check percent: "+-((rotation-initAngle)*percent));

      //calc playing sound
      float magicNumber = 2;
      if (Math.abs(acc /(VELOCITY_SCALE*(coefficient/2))) >= magicNumber) {
//        SoundEffect.Playmusic(SoundEffect.wheel_sound);
//        Wheel.wheelTick.play();
        this.acc = 0;
      }
    }

    WheelRollAction init(float velocity, int outcome) {
      super.reset();
      this.velocity = velocity;
      this.outcome = outcome;
      setDuration((float)ROLLING_DURATION);
      this.initAngle = outcome * WHEEL_ARC;
      return this;

    }

    @Override
    protected void end() {
      if (Wheel.inst().listener != null) {
        WheelItem outputItem = wheelItemMap.get(outcome);
        if (outputItem != null)
          Wheel.inst().listener.end(outputItem);
        else
          Wheel.inst().listener.error("Wheel Item Inconsistency");
      }

      if (USE_DRAG) {
        Wheel.inst().wheelGroup.addListener(new WheelDragListener());
      }

      super.end();
    }
  }

  /************************************************************************************************/

  private static class AngularSpriteFont {
    List<Sprite>      composite;
    private           BitmapFont        font;
    private           String            text;

    private Sprite getGlyphSprite(char ch) {
      BitmapFont.Glyph glyph = font.getData().getGlyph(ch);
      Sprite s =  new Sprite(font.getRegion().getTexture(),
              glyph.srcX,glyph.srcY,glyph.width , glyph.height);

      s.setOrigin(glyph.width/2, glyph.height/2);
      return s;
    }

    AngularSpriteFont(String text, BitmapFont font) {
      this.text = text;
      this.font = font;
      this.composite = new ArrayList<>();
      make();
    }

    private void make() {
      for (char ch : text.toCharArray()) {
        composite.add(getGlyphSprite(ch));
      }
    }

    protected void draw(Batch batch, float angle) {
      int     w             = wheelTex.getRegionWidth();
      int     h             = wheelTex.getRegionHeight();
      Vector2 roller        = (new Vector2()).set(0, h * ANGULAR_TEXT_FLOAT /2);
      Vector2 cp            = Vector2 .Zero.set(w/2, h/2);
      float   align         = angle + ((composite.size() - 1)/2f)*TEXT_SPACE;

      for (Sprite sprite : composite) {
        Vector2 _pos = roller .cpy().rotate(align).add(cp)
                .sub(sprite.getRegionWidth()/2f, sprite.getRegionHeight()/2);
        sprite.setX(_pos.x);
        sprite.setY(_pos.y);
        sprite.setRotation(align);
//        sprite.setScale(-1,1);
        align -= TEXT_SPACE;
        sprite.draw(batch);
      }
    }
  }

  private static class PolarSpriteFont extends AngularSpriteFont {
    PolarSpriteFont(String text, BitmapFont font) {
      super(text, font);
    }

    @Override
    protected void draw(Batch batch, float angle) {
      int     w             = wheelTex.getRegionWidth();
      int     h             = wheelTex.getRegionHeight();
      Vector2 roller        = (new Vector2()).set(0, h * POLAR_TEXT_FLOAT /2);
      Vector2 cp            = Vector2 .Zero.set(w/2, h/2);
      int     idx           = 0;
      float   magicNumber   = 4.2f;

      for (Sprite sprite : composite) {
        Vector2 _pos = roller .cpy().add(0,idx++*TEXT_SPACE*magicNumber)
                .rotate(angle).add(cp)
                .sub(sprite.getRegionWidth()/2f, sprite.getRegionHeight()/2);
        sprite.setX(_pos.x);
        sprite.setY(_pos.y);
        sprite.setRotation(angle + 90);
        sprite.draw(batch);
      }
    }
  }

  /************************************************************************************************/

  public static class WheelItem {
    TextureRegion    tex;
//    TextureRegion    tex2;
    int              id;
    int              qty;
    String           qtyText;
    int              percent;


    public static WheelItem newInst(TextureRegion wheelItem,
                                    int id , int qty, String qtyText, int percent) {
      WheelItem r = new WheelItem();
      r.tex       = wheelItem;
//      r.tex2      = knife;
      r.id        = id;
      r.qtyText   = qtyText;
      r.qty       = qty;
      r.percent   = percent;
      return r;
    }

    public int getPercent() {
      return percent;
    }
    public TextureRegion getRegion(){return tex;}
//    public TextureRegion getRegion2(){return tex2;}

    public int getId() {
      return id;
    }

    public String getQtyText() {
      return qtyText;
    }

    public int getQty() {
      return qty;
    }
  }

  private static class Tuple<T,V> {
    T position;
    V wheelItem;

    Tuple(T pos, V item) {
      position  = pos;
      wheelItem = item;
    }
  }


  public interface EventListener {
    boolean start();
    void end(WheelItem item);
    void error(String msg);
  }
}