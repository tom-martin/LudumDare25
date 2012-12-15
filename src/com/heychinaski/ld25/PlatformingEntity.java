package com.heychinaski.ld25;

import java.awt.geom.Rectangle2D;

public abstract class PlatformingEntity extends Entity {
  public static int GRAVITY_SPEED = 800;
  
  Rectangle2D.Float intersection = new Rectangle2D.Float();

  @Override
  public void update(float tick, Game game) {
    // Hack, use platforms
    if(y < game.getHeight()) {
      nextY += GRAVITY_SPEED * tick;
    } else {
      nextY = game.getHeight();
    }
  }
  
  @Override
  public void collided(Entity with, float tick, Game game, Rectangle2D.Float bounds, Rectangle2D.Float nextBounds, Rectangle2D.Float withBounds) {
    Rectangle2D.intersect(nextBounds, withBounds, intersection);
    if(with instanceof Platform) {
      if((bounds.getMaxY() > withBounds.getMinY() && bounds.getMaxY() < withBounds.getMaxY()) || 
          (bounds.getMinY() > withBounds.getMinY() && bounds.getMinY() < withBounds.getMaxY())) {
        if(x < nextX) {
          nextX -= intersection.width;
        } else if(x > nextX) {
          nextX += intersection.width;
        }
      } else {
        if(y < nextY) {
          nextY -= intersection.height;
          stoodOn((Platform)with);
        } else if(y > nextY) {
          nextY += intersection.height;
        }
      }
    }
  }
  
  public abstract void stoodOn(Platform platform);
}
