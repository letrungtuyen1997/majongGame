package com.ss.core.action.exAction;

import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class GPathAction extends Action {
  private CatmullRomSpline<Vector2> path;
  private float     speed;
  private float     current;
  private Vector2   out;
  private Vector2   der;
  private boolean   dir;

  public static GPathAction init(Vector2[] controlPoints, float speed, boolean dir) {
    GPathAction instance = Actions.action(GPathAction.class);
    instance.path = new CatmullRomSpline<>(controlPoints,false);
    instance.speed = speed;
    instance.current = 0;
    instance.out = new Vector2(0,0);
    instance.der = new Vector2(0,0);
    instance.dir = dir;
    return  instance;
  }

  @Override
  public boolean act(float delta) {
    path.derivativeAt(der, current);

//    current += (delta*speed/path.spanCount)/der.len();
    current += (delta*speed/path.spanCount);

    if (current >= 1)
      return true;

    path.valueAt(out, current);
    actor.setPosition(out.x, out.y);
    if (dir) {
      actor.setRotation(der.angle()+90);
    }

    return false;
  }
}