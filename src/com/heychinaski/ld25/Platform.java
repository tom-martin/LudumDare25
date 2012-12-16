package com.heychinaski.ld25;

import static java.lang.Math.round;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;

public class Platform extends Entity {
  
  Image image;
  Image darkImage;

  public Platform(Image image, Image darkImage) {
    super();
    this.image = image;
    this.darkImage = darkImage;
  }

  @Override
  public void update(float tick, Game game) {

  }

  @Override
  public void render(Graphics2D g, boolean dark) {
    Image i = dark ? darkImage : image;
    Graphics2D g2d = (Graphics2D) g.create();
    int startX = round(x - (w/2));
    int startY = round(y - (h/2));
    int endX = round(startX + w);
    int endY = round(startY + h);
    int imageWidth = i.getWidth(null);
    int imageHeight = i.getHeight(null);
    g2d.setClip(startX, startY, round(w), round(h));
    for(int currentY = startY; currentY < endY; currentY += imageHeight) {
      for(int currentX = startX; currentX < endX; currentX += imageWidth) {
        g2d.drawImage(i, currentX, currentY, null);
      }
    }
    
    g2d.dispose();
  }

  @Override
  public void collided(Entity with, float tick, Game game, Rectangle2D.Float boundsA, Rectangle2D.Float nextBoundsA, Rectangle2D.Float withBounds) {

  }

}
