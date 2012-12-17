package com.heychinaski.ld25;


import static java.lang.Math.round;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D.Float;

public class Sign extends Entity {
  
  Font font = null;
  Image image = null;
  Image darkImage = null;
  String[] message;
  private long lastActivated;
  
  String[] darkMessage;
  
  public Sign(int x, int y, String[] message, String[] darkMessage) {
    nextX = x;
    nextY = y;
    
    w = 16;
    h = 32;
    
    this.message = message;
    this.darkMessage = darkMessage;
  }

  @Override
  public void update(float tick, Game game) {
    
  }

  @Override
  public void render(Graphics2D g, boolean dark) {
    if(dark) {
      g.drawImage(darkImage, round(x - (w / 2)), round(y - (h / 2)), null);
    } else {
      g.drawImage(image, round(x - (w / 2)), round(y - (h / 2)), null);
    }
  }
  
  public void renderMessage(Graphics2D g, Camera camera, boolean dark) {
    String[] ms = dark ? darkMessage : message;
    if(active() && ms != null) {
      int startX = round(camera.x - 100);
      int startY = round(camera.y - 50);
      for(int i = 0; i < ms.length; i++) {
        font.renderString(g, ms[i], startX, startY + (i * 10));  
      }
    }
  }

  boolean active() {
    return (System.currentTimeMillis() - lastActivated) < 1000;
  }

  @Override
  public void collided(Entity with, float tick, Game game, Float bounds,
      Float nextBounds, Float withBounds) {
    if(with instanceof Player) {
      lastActivated = System.currentTimeMillis();
    }
  }

}
