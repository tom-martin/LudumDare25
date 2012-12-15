package com.heychinaski.ld25;

import static java.lang.Math.round;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;

public class Player extends PlatformingEntity {
  
  static float WALK_SPEED = 600f;
  static float JUMP_SPEED = 1000f;
  static float JUMP_X_SPEED = 800f;
  static float INITIAL_JUMP_FUEL = 400f;
  
  float jumpFuel = 0;
  private Image image;
  
  int direction = 1;
  
  public Player(Image image) {
    this.image = image; 
  }
  
  @Override
  public void update(float tick, Game game) {
    super.update(tick, game);
    
    if(game.input.isKeyDown(KeyEvent.VK_LEFT)) direction = -1;
    if(game.input.isKeyDown(KeyEvent.VK_RIGHT)) direction = 1;
    
    if(game.input.isKeyDown(KeyEvent.VK_Z)) {
    } else if(game.input.isKeyDown(KeyEvent.VK_SPACE) && jumpFuel > 0) {
      float jumpValue = Math.min((tick * JUMP_SPEED), jumpFuel);
      nextY = y - jumpValue;
      jumpFuel -= jumpValue;
      
      if(game.input.isKeyDown(KeyEvent.VK_LEFT)) nextX = x - (tick * JUMP_X_SPEED); 
      if(game.input.isKeyDown(KeyEvent.VK_RIGHT)) nextX = x + (tick * JUMP_X_SPEED);
    } else {
      jumpFuel = 0;
      
      if(game.input.isKeyDown(KeyEvent.VK_LEFT)) nextX = x - (tick * WALK_SPEED); 
      if(game.input.isKeyDown(KeyEvent.VK_RIGHT)) nextX = x + (tick * WALK_SPEED);
    }
  }

  @Override
  public void render(Graphics2D g) {
    Graphics2D g2 = (Graphics2D)g.create();
    g2.translate(round(x), round(y));
    g2.scale(direction, 1);
    g2.drawImage(image,  round(-w/2), round(-h / 2), null);
    g2.dispose();
  }
  
  public void stoodOn(Platform platform) {
    jumpFuel = INITIAL_JUMP_FUEL;
  }
}
