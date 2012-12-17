package com.heychinaski.ld25;

import static java.lang.Math.round;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D.Float;

public class Furniture extends Entity {
  
  Image image;
  Image darkImage;
  
  public Furniture(Image[] images, Image[] darkImages) {
    int index = (int)(Math.random() * images.length);
    image = images[index];
    darkImage = darkImages[index];
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

  @Override
  public void collided(Entity with, float tick, Game game, Float bounds,
      Float nextBounds, Float withBounds) {

  }

}
