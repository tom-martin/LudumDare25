package com.heychinaski.ld25;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;

public class Game extends Canvas {
  private static final long serialVersionUID = 1L;
  
  Input input = new Input();
  Player player;
  List<Entity> entities;
  List<Platform> platforms;
  CollisionManager collisionManager = new CollisionManager(this);
  
  EntityTrackingCamera camera;
  
  boolean running = true;

  ImageManager imageManager;
  
  public Game() {
    setIgnoreRepaint(true);
    
    addKeyListener(new KeyListener() {
      
      @Override
      public void keyTyped(KeyEvent e) {}
      
      @Override
      public void keyReleased(KeyEvent e) {input.keyUp(e.getKeyCode());}
      
      @Override
      public void keyPressed(KeyEvent e) { input.keyDown(e.getKeyCode());}
    });
    
    addFocusListener(new FocusListener() {
      
      @Override
      public void focusLost(FocusEvent arg0) {
      }
      
      @Override
      public void focusGained(FocusEvent arg0) {
        
      }
    });
    
    addMouseListener(new MouseListener() {

      @Override
      public void mouseClicked(MouseEvent arg0) {
      }

      @Override
      public void mouseEntered(MouseEvent arg0) {
        
      }

      @Override
      public void mouseExited(MouseEvent arg0) {
        
      }

      @Override
      public void mousePressed(MouseEvent e) {
        input.setMouseDown(e.getButton());
      }

      @Override
      public void mouseReleased(MouseEvent e) {
        input.setMouseUp(e.getButton());
      }
      
    });
  }
  
  public void start() {
    createBufferStrategy(2);
    BufferStrategy strategy = getBufferStrategy();
    
    Graphics2D g;
    reset();
    
    long last = System.currentTimeMillis();
    while (running) {
      long now = System.currentTimeMillis();
      float tick = (float)(now - last) / 1000;
      last = now;
      
      if(input.isKeyDown(KeyEvent.VK_ESCAPE)) {
        System.exit(0);
      }
      
      g = (Graphics2D)strategy.getDrawGraphics();
      Graphics2D orig = g;
      g = (Graphics2D) g.create();
      g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
      
      g.setColor(Color.black);
      g.fillRect(0, 0, getWidth(), getHeight());
      
      for(int i = 0; i < entities.size(); i++) {
        entities.get(i).update(tick, this);
      }
      
      collisionManager.update(tick);
      
      for(int i = 0; i < entities.size(); i++) {
        entities.get(i).applyNext();
      }
      
      camera.update(tick, this);
      camera.look(g);
      
      for(int i = 0; i < platforms.size(); i++) {
        platforms.get(i).render(g);
      }
      
      player.render(g);
      
      g.dispose();
      strategy.show();
      
      try {
        Thread.sleep(10);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }    
  }
  
  public void reset() {
    entities = new ArrayList<Entity>();
    platforms = new ArrayList<Platform>();
    this.imageManager = new ImageManager(this, "ghost.png");
    
    player = new Player(imageManager.get("ghost.png"));
    camera = new EntityTrackingCamera(player, this);
    
    player.x = 0;
    player.y = 0;
    player.w = 32;
    player.h = 64;
    
    
    entities.add(player);
    
    addPlatform(0, 800, 10000, 200);
    
    for(int i = 0; i < 20; i++) {
      addPlatform(i * 200f, (float)Math.random()*800f, 100, 100);
    }
  }

  private void addPlatform(float x, float y, float w, float h) {
    Platform platform = new Platform();
    platform.nextX = x;
    platform.nextY = y;
    platform.w = w;
    platform.h = h;
    platforms.add(platform);
    entities.add(platform);
  }

}
