package com.heychinaski.ld25;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D.Float;

import static java.lang.Math.round;

public class LightSwitch extends Entity {

  private Image image;
  private Image darkImage;

  public LightSwitch(Image image, Image darkImage) {
    super();
    this.image = image;
    this.darkImage = darkImage;
  }

  @Override
  public void update(float tick, Game game) {
    // TODO Auto-generated method stub

  }

  @Override
  public void render(Graphics2D g, boolean dark) {
    g.drawImage(dark ? darkImage : image, round(x-(w/2)), round(y-(h/2)), null);    
  }

  @Override
  public void collided(Entity with, float tick, Game game, Float bounds,
      Float nextBounds, Float withBounds) {

  }

}
