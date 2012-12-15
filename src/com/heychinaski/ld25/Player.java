package com.heychinaski.ld25;

import static java.lang.Math.round;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;

public class Player extends PlatformingEntity {
  
  static float WALK_SPEED = 200f;
  static float JUMP_SPEED = GRAVITY_SPEED + 1200f;
  static float JUMP_X_SPEED = 300f;
  static float INITIAL_JUMP_FUEL = 700f;
  
  float jumpFuel = 0;
  private Image image;
  
  int direction = 1;
  private int hidingIndex = -1;
  private Image[] hideImages;
  
  public Player(Image image, Image hideImages[]) {
    this.image = image; 
    this.hideImages = hideImages;
  }
  
  @Override
  public void update(float tick, Game game) {
    super.update(tick, game);
    
    if(game.input.isKeyDown(KeyEvent.VK_LEFT)) direction = -1;
    if(game.input.isKeyDown(KeyEvent.VK_RIGHT)) direction = 1;
    
    if(game.input.isKeyDown(KeyEvent.VK_Z)) {
      if(hidingIndex  == -1) {
        hidingIndex = (int)(Math.random() * hideImages.length);
      }
      
      return;
    }
    
    hidingIndex = -1;
    
    if(game.input.isKeyDown(KeyEvent.VK_SPACE) && jumpFuel > 0) {
      float cost = (jumpFuel / INITIAL_JUMP_FUEL);
      float jumpValue = Math.min((tick * JUMP_SPEED * cost), jumpFuel);
      nextY -= jumpValue;
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
    if(hidingIndex == -1) {
      g2.scale(direction, 1);
      float wobbleY = (int)((System.currentTimeMillis() % 1600) / 100);
      if(wobbleY > 8) wobbleY = 16 - wobbleY;
      
      g2.drawImage(image,  round(-w/2), round(wobbleY -(h / 2)), null);
    } else {
      g2.drawImage(hideImages[hidingIndex],  round(-w/2), round(-(h / 2)), null);
    }
    g2.dispose();
  }
  
  public void stoodOn(Platform platform) {
    jumpFuel = INITIAL_JUMP_FUEL;
  }
}
