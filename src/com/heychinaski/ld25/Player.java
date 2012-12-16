package com.heychinaski.ld25;

import static java.lang.Math.round;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;

public class Player extends PlatformingEntity {
  
  static float WALK_SPEED = 100f;
  static float JUMP_SPEED = GRAVITY_SPEED + 600f;
  static float JUMP_X_SPEED = 150f;
  static float INITIAL_JUMP_FUEL = 350f;
  
  float jumpFuel = 0;
  
  int direction = 1;
  private int hidingIndex = -1;
  private Image[] hideImages;
  private Image[] imageCycle;
  private Image[] jumpCycle;
  private boolean jumping = false;
  
  boolean hit = false;
  
  public Player(Image[] imageCycle, Image[] jumpCycle, Image hideImages[]) {
    this.imageCycle = imageCycle; 
    this.jumpCycle = jumpCycle;
    this.hideImages = hideImages;
  }
  
  @Override
  public void update(float tick, Game game) {
    super.update(tick, game);
    
    if(hit) return;
    
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
      jumping = true;
      float cost = (jumpFuel / INITIAL_JUMP_FUEL);
      float jumpValue = Math.min((tick * JUMP_SPEED * cost), jumpFuel);
      nextY -= jumpValue;
      jumpFuel -= jumpValue;
      
      if(game.input.isKeyDown(KeyEvent.VK_LEFT)) nextX = x - (tick * JUMP_X_SPEED); 
      if(game.input.isKeyDown(KeyEvent.VK_RIGHT)) nextX = x + (tick * JUMP_X_SPEED);
    } else {
      jumping = false;
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
      Image[] images = jumping || hit ? jumpCycle : imageCycle;
      int imageIndex = (int)((System.currentTimeMillis() / 200) % images.length);
      g2.drawImage(images[imageIndex],  round(-w/2), round(-h / 2), null);
    } else {
      g2.drawImage(hideImages[hidingIndex],  round(-w/2), round(-(h / 2)), null);
    }
    g2.dispose();
  }
  
  @Override
  public void collided(Entity with, float tick, Game game, Rectangle2D.Float bounds, Rectangle2D.Float nextBounds, Rectangle2D.Float withBounds) {
    super.collided(with, tick, game, bounds, nextBounds, withBounds);
    if(with instanceof Flash && hidingIndex == -1) {
      hit = true;
      ((Flash)with).successfulHit();
    }
  }
  
  public void stoodOn(Platform platform) {
    jumpFuel = INITIAL_JUMP_FUEL;
  }
}
