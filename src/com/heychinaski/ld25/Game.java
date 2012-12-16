package com.heychinaski.ld25;

import static java.lang.Math.round;

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
  
  BackgroundTile bgTile;
  
  float biggestTick = 0;
  
  EntityTrackingCamera camera;
  
  boolean running = true;

  ImageManager imageManager;

  boolean dark = false;

  private LightSwitch lightSwitch;
  
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
      
      bgTile.render(round(camera.x-300), round(camera.y-100), round(camera.x+300), round(camera.y+200), g, dark);
      
      for(int i = 0; i < platforms.size(); i++) {
        platforms.get(i).render(g, dark);
      }
      lightSwitch.render(g, dark);
      player.render(g, dark);
      
      for(int i = 0; i < flashes.size(); i++) {
        flashes.get(i).render(g, dark );
      }
      
      for(int i = 0; i < tourists.size(); i++) {
        tourists.get(i).render(g, dark);
      }
      
      if(player.hit || dark) player.render(g, dark);
      
      for(int i = 0; i < tourists.size(); i++) {
        Tourist t = tourists.get(i);
        if(t.scared) t.render(g, dark);
      }
      
      
      g.dispose();
      strategy.show();
      
      if(player.hit && (System.currentTimeMillis() - player.hitTime) > 2000 ) {
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
                                               "ghost1_dark.png", 
                                               "ghost2_dark.png", 
                                               "ghost3_dark.png",
                                               "ghost_jump.png",
                                               "ghost_jump_dark.png",
                                               "clock.png", 
                                               "flower.png", 
                                               "hatstand.png", 
                                               "tourist1.png", 
                                               "tourist2.png",
                                               "tourist1_dark.png",
                                               "tourist2_dark.png",
                                               "tourist3_dark.png",
                                               "tourist_scared1.png",
                                               "tourist_scared2.png",
                                               "brick.png",
                                               "brick_dark.png",
                                               "flash1.png",
                                               "flash2.png",
                                               "flash3.png",
                                               "flash4.png",
                                               "flash5.png",
                                               "wallpaper.png",
                                               "wallpaper2.png",
                                               "wallpaper_dark.png",
                                               "switch.png",
                                               "switch_dark.png");
    
    bgTile = new BackgroundTile(imageManager.get("wallpaper.png"), imageManager.get("wallpaper2.png"), imageManager.get("wallpaper_dark.png"), imageManager.get("wallpaper_dark.png"));
    
    player = new Player(new Image[]{imageManager.get("ghost1.png"), imageManager.get("ghost2.png"), imageManager.get("ghost3.png"), imageManager.get("ghost2.png")},
                        new Image[]{imageManager.get("ghost_jump.png")},
                        new Image[]{imageManager.get("clock.png"), imageManager.get("flower.png"), imageManager.get("hatstand.png")},
                        new Image[]{imageManager.get("ghost1_dark.png"), imageManager.get("ghost2_dark.png"), imageManager.get("ghost3_dark.png"), imageManager.get("ghost2_dark.png")},
                        new Image[]{imageManager.get("ghost_jump_dark.png")});
    camera = new EntityTrackingCamera(player, this);
    
    player.x = 0;
    player.nextY = 600;
    player.w = 16;
    player.h = 16;
    
    
//    addPlatform(0, 896, 10000, 128);
    
    float y = 800f;
    addPlatform(0, y, 64, 64);
    for(int i = 1; i < 20; i++) {
      y +=((float)Math.random()*128f) - 64;
      y = Math.min(y, 768);
      addPlatform(i * 128, y, 64, 64);
    }
    
    for(int i = 1; i < 5; i++) {
      float x = i * 256f;
      addTourist(x, 0f, x - 32, x + 32);
    }
    
    entities.add(player);
    
    for(int i = 0; i < entities.size(); i++) {
      entities.get(i).applyNext();
    }
    
    setLightSwitch(128, 736);
    ensureNotDark();
  }

  private void addPlatform(float x, float y, float w, float h) {
    Platform platform = new Platform(imageManager.get("brick.png"), imageManager.get("brick_dark.png"));
    platform.nextX = x;
    platform.nextY = y;
    platform.w = w;
    platform.h = h;
    platforms.add(platform);
    entities.add(platform);
  }
  
  private void addTourist(float x, float y, float left, float right) {
    PatrollingTourist t = new PatrollingTourist(new Image[] {imageManager.get("tourist1.png"), imageManager.get("tourist2.png")},
                                                new Image[] {imageManager.get("tourist1_dark.png"), imageManager.get("tourist2_dark.png"), imageManager.get("tourist3_dark.png"), imageManager.get("tourist2_dark.png")}, 
                                                new Image[] {imageManager.get("tourist_scared1.png"), imageManager.get("tourist_scared2.png")},
                                                left, right);
    t.nextX = x;
    t.nextY = y;
    t.w = 16;
    t.h = 32;
    tourists.add(t);
    entities.add(t);
    addFlash(t);
  }
  
  private void setLightSwitch(float x, float y) {
    lightSwitch = new LightSwitch(imageManager.get("switch.png"), imageManager.get("switch_dark.png"));
    lightSwitch.nextX = x;
    lightSwitch.nextY = y;
    lightSwitch.w = 16;
    lightSwitch.h = 32;
    entities.add(lightSwitch);
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

  public void ensureDark() {
    if(!dark) {
      dark = true;
    }
  }
  
  public void ensureNotDark() {
    if(dark) {
      dark = false;
    }
  }

}
