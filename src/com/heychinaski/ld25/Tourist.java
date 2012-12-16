package com.heychinaski.ld25;

import static java.lang.Math.round;

import java.awt.Graphics2D;

public class Tourist extends PlatformingEntity {
  
  boolean scared = false;

  @Override
  public void stoodOn(Platform platform) {

  }

  @Override
  public void render(Graphics2D g, boolean dark) {
    g.fillRect(round(x - (w/2)), round(y - (h / 2)), round(w), round(h));
  }

}
