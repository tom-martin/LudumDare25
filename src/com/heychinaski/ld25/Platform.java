package com.heychinaski.ld25;

import static java.lang.Math.round;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class Platform extends Entity {

  @Override
  public void update(float tick, Game game) {

  }

  @Override
  public void render(Graphics2D g) {
    g.setColor(Color.green);
    g.fillRect(round(x - (w/2)), round(y - (h / 2)), round(w), round(h));
  }

  @Override
  public void collided(Entity with, float tick, Game game, Rectangle2D.Float boundsA, Rectangle2D.Float nextBoundsA, Rectangle2D.Float withBounds) {

  }

}
