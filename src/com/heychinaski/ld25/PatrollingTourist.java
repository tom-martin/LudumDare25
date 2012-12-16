package com.heychinaski.ld25;

import static java.lang.Math.round;

import java.awt.Graphics2D;
import java.awt.Image;

public class PatrollingTourist extends Tourist {
  float left, right;
  int direction = 1;
  Image[] walkImages;
  boolean caughtPlayer = false;
  private Image[] darkImages;
  
  private Image[] scaredImages;
  
  private long scaredStart;
  
  static float WALK_SPEED = 30f;
  static float SCARED_WALK_SPEED = 100f;
  static float SCARE_JUMP_SPEED = GRAVITY_SPEED + 300f;
  
  public PatrollingTourist(Image walkImages[], Image darkImages[], Image scaredImages[], float left, float right) {
    this.left = left;
    this.right = right;
    this.walkImages = walkImages;
    this.darkImages = darkImages;
    this.scaredImages = scaredImages;
  }
  
  @Override
  public void update(float tick, Game game) {
    if(!caughtPlayer) {
      super.update(tick, game);
      
      long scaredDiff = System.currentTimeMillis() - scaredStart;
      if(scared && scaredDiff < SCARE_JUMP_SPEED) {
        nextY -= tick * Math.max(0, SCARE_JUMP_SPEED - scaredDiff);
      }
      
      if(game.dark && !scared) return;
      
      float speed = scared ? (Math.min(SCARED_WALK_SPEED, scaredDiff / 5)): WALK_SPEED;
      nextX = x + (direction * tick * speed);
      
      if(!scared) {
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
  }

  @Override
  public void render(Graphics2D g, boolean dark) {
    Image[] ic =  dark ? darkImages : walkImages;
    ic =  scared ? scaredImages : ic;
    
    Graphics2D g2 = (Graphics2D)g.create();
    g2.translate(round(x), round(y));
    g2.scale(direction, 1);
    
    int imageIndex = caughtPlayer ? 0 : (int)((System.currentTimeMillis() / 250) % ic.length);
    g2.drawImage(ic[imageIndex],  round(-w/2), round(-h / 2), null);
    g2.dispose();
  }
  
  public void successfulHit() {
    caughtPlayer = true;
  }
  
  public void scare() {
    if(!scared) {
      scared = true;
      scaredStart = System.currentTimeMillis();
    }
  }
}
