package com.heychinaski.ld25;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
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
  List<Tourist> tourists;
  List<Flash> flashes;
  CollisionManager collisionManager = new CollisionManager(this);
  
  float biggestTick = 0;
  
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
      if(tick > biggestTick) {
        biggestTick = tick;
        System.out.println("Biggest"+(((float)1) / biggestTick));
      }
      if(System.currentTimeMillis()%100==0) {
        System.out.println(((float)1) / tick);
      }
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
      
      for(int i = 0; i < flashes.size(); i++) {
        flashes.get(i).render(g);
      }
      
      for(int i = 0; i < tourists.size(); i++) {
        tourists.get(i).render(g);
      }
      
      if(player.hit) player.render(g);
      
      
      g.dispose();
      strategy.show();
      
      if(player.hit) {
        try {
          Thread.sleep(500);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        reset();
      }
      
      try {
        Thread.sleep(5);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }    
  }
  
  public void reset() {
    entities = new ArrayList<Entity>();
    platforms = new ArrayList<Platform>();
    tourists = new ArrayList<Tourist>();
    flashes = new ArrayList<Flash>();
    
    this.imageManager = new ImageManager(this, "ghost1.png", 
                                               "ghost2.png", 
                                               "ghost3.png", 
                                               "ghost_jump.png", 
                                               "clock.png", 
                                               "flower.png", 
                                               "hatstand.png", 
                                               "tourist1.png", 
                                               "tourist2.png",
                                               "brick.png",
                                               "flash1.png",
                                               "flash2.png",
                                               "flash3.png",
                                               "flash4.png",
                                               "flash5.png");
    
    player = new Player(new Image[]{imageManager.get("ghost1.png"), imageManager.get("ghost2.png"), imageManager.get("ghost3.png"), imageManager.get("ghost2.png")},
                        new Image[]{imageManager.get("ghost_jump.png")},
                        new Image[]{imageManager.get("clock.png"), imageManager.get("flower.png"), imageManager.get("hatstand.png")});
    camera = new EntityTrackingCamera(player, this);
    
    player.x = 0;
    player.nextY = 600;
    player.w = 16;
    player.h = 32;
    
    
    addPlatform(0, 800, 10000, 200);
    
    for(int i = 0; i < 20; i++) {
      addPlatform(i * 100f, ((float)Math.random()*300f) + 300f, 64, 64);
    }
    
    for(int i = 1; i < 5; i++) {
      float x = i * 200f;
      addTourist(x, 0f, x - 100, x + 100);
    }
    
    entities.add(player);
    
    for(int i = 0; i < entities.size(); i++) {
      entities.get(i).applyNext();
    }
  }

  private void addPlatform(float x, float y, float w, float h) {
    Platform platform = new Platform(imageManager.get("brick.png"));
    platform.nextX = x;
    platform.nextY = y;
    platform.w = w;
    platform.h = h;
    platforms.add(platform);
    entities.add(platform);
  }
  
  private void addTourist(float x, float y, float left, float right) {
    PatrollingTourist t = new PatrollingTourist(new Image[] {imageManager.get("tourist1.png"), imageManager.get("tourist2.png")}, left, right);
    t.nextX = x;
    t.nextY = y;
    t.w = 16;
    t.h = 32;
    tourists.add(t);
    entities.add(t);
    addFlash(t);
  }
  
  private void addFlash(PatrollingTourist tourist) {
    Flash f = new Flash(new Image[]{imageManager.get("flash1.png"),
                                    imageManager.get("flash2.png"),
                                    imageManager.get("flash3.png"), 
                                    imageManager.get("flash4.png"), 
                                    imageManager.get("flash5.png"),
                                    imageManager.get("flash4.png"),
                                    imageManager.get("flash3.png"), 
                                    imageManager.get("flash2.png"),}, imageManager.get("flash5.png"), tourist);
    f.nextX = tourist.nextX;
    f.nextY = tourist.nextY;
    f.w = 32;
    f.h = 32;
    flashes.add(f);
    entities.add(f);
  }

}
