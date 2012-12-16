package com.heychinaski.ld25;

import static java.lang.Math.round;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D.Float;

public class Flash extends Entity {
  Image[] flashImages;
  PatrollingTourist tourist;
  
  long flashOffset = round(Math.random() * Long.MAX_VALUE);
  private boolean on;
  private Image hitImage;

  public Flash(Image[] flashImages, Image hitImage, PatrollingTourist tourist) {
    super();
    this.flashImages = flashImages;
    this.hitImage = hitImage;
    this.tourist = tourist;
  }

  @Override
  public void update(float tick, Game game) {
    nextX = tourist.nextX + (24 * tourist.direction);
    nextY = tourist.nextY;
  }

  @Override
  public void render(Graphics2D g, boolean dark) {
    if(dark) return;
    if(on ||
       (System.currentTimeMillis()+flashOffset) % 500 < 200) {
      Graphics2D g2 = (Graphics2D)g.create();
      g2.translate(round(x), round(y));
      g2.scale(tourist.direction, 1);
      
      Image img = on ? hitImage : flashImages[(int)((System.currentTimeMillis() / 20) % flashImages.length)]; 
      g2.drawImage(img,  round(-w/2), round(-h / 2), null);
      g2.dispose();
    }
  }

  @Override
  public void collided(Entity with, float tick, Game game, Float bounds,
      Float nextBounds, Float withBounds) {
    // TODO Auto-generated method stub

  }
  
  public void successfulHit() {
    on = true;
    tourist.successfulHit();
  }

}
