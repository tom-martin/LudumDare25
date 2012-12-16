package com.heychinaski.ld25;

import java.awt.Graphics2D;

public abstract class Camera extends Entity {

  Game game;
  
  float zoom = 4;

  public Camera(Game game) {
    this.game = game;
  }

  public void look(Graphics2D g) {
    g.scale(zoom, zoom);
    g.translate((game.getWidth() / (2 * zoom))-x, (game.getHeight() / (2 * zoom))-y);
  }
  
}
