package com.heychinaski.ld25;

import static java.lang.Math.round;

import java.awt.Graphics2D;
import java.awt.Image;

public class PatrollingTourist extends Tourist {
  float left, right;
  int direction = 1;
  Image[] walkImages;
  boolean caughtPlayer = false;
  
  static float WALK_SPEED = 30f;
  
  public PatrollingTourist(Image walkImages[], float left, float right) {
    this.left = left;
    this.right = right;
    this.walkImages = walkImages;
  }
  
  @Override
  public void update(float tick, Game game) {
    if(!caughtPlayer) {
      super.update(tick, game);
      nextX = x + (direction * tick * WALK_SPEED);
      
      if(nextX < left) {
        nextX = left;
        direction = 1;
      }
      if(nextX > right) {
        nextX = right;
        direction = -1;
      }
    }
  }

  @Override
  public void render(Graphics2D g) {
    Graphics2D g2 = (Graphics2D)g.create();
    g2.translate(round(x), round(y));
    g2.scale(direction, 1);
    
    int imageIndex = caughtPlayer ? 0 : (int)((System.currentTimeMillis() / 250) % walkImages.length);
    g2.drawImage(walkImages[imageIndex],  round(-w/2), round(-h / 2), null);
    g2.dispose();
  }
  
  public void successfulHit() {
    caughtPlayer = true;
  }
}
