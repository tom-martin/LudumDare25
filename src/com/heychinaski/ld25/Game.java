package com.heychinaski.ld25;

import static java.lang.Math.random;
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
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Game extends Canvas {
  private static final long serialVersionUID = 1L;

  private static final int MAX_LEVEL_INDEX = 5;
  
  Input input = new Input();
  Player player;
  List<Entity> entities;
  List<Platform> platforms;
  List<Tourist> tourists;
  List<Flash> flashes;
  List<Sign> signs;
  CollisionManager collisionManager = new CollisionManager(this);
  Font font;
  
  int levelIndex = 0; 
  
  BackgroundTile bgTile;
  
  float biggestTick = 0;
  
  EntityTrackingCamera camera;
  
  boolean running = true;

  ImageManager imageManager;

  boolean dark = false;
  
  long finishTime = -1;

  private LightSwitch lightSwitch;

  private long levelStart;

  private boolean showingTitle = false;

  private long timeToPlayScaredSound = -1;

  private Clip musicClip;
  
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
    playMusic();
    
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
      
      if(now - levelStart < 2000) {
        ensureNotDark();
      } else {
        showingTitle = false;
      }
      if(now - levelStart < 200) input.keyUp(KeyEvent.VK_SPACE);
      
      if(showingTitle) {
        input.keyUp(KeyEvent.VK_SPACE);
        input.keyUp(KeyEvent.VK_LEFT);
        input.keyUp(KeyEvent.VK_RIGHT);
        input.keyUp(KeyEvent.VK_Z);
      }
      
      
      if(input.isKeyDown(KeyEvent.VK_ESCAPE)) {
        System.exit(0);
      }
      
      if(input.isKeyDown(KeyEvent.VK_M)) {
        if(musicClip.isRunning()) {
          musicClip.stop();
        } else {
          playMusic();
        }
        input.keyUp(KeyEvent.VK_M);
      }
      
      for(int i = 0; i < entities.size(); i++) {
        entities.get(i).update(tick, this);
      }
      
      collisionManager.update(tick);
      
      for(int i = 0; i < entities.size(); i++) {
        entities.get(i).applyNext();
      }
      
      boolean allTouristsScared = true;
      for(int i = 0; i < tourists.size(); i++) {
        Tourist t = tourists.get(i);
        if(!t.scared) {
          allTouristsScared = false;
          break;
        }
      }
      
      camera.update(tick, this);
      
      g = (Graphics2D)strategy.getDrawGraphics();
      g = (Graphics2D) g.create();
      g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
      
      g.setColor(Color.black);
      g.fillRect(0, 0, getWidth(), getHeight());
      
      camera.look(g);
      
      bgTile.render(round(camera.x-300), round(camera.y-100), round(camera.x+300), round(camera.y+200), g, dark);
      
      for(int i = 0; i < platforms.size(); i++) {
        platforms.get(i).render(g, dark);
      }
      
      for(int i = 0; i < signs.size(); i++) {
        signs.get(i).render(g, dark );
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
      
      if(showingTitle) {
        int startX = round(camera.x - 100);
        int startY = round(camera.y - 50);
        font.renderString(g, "TOURIST TRAP", startX, startY);
      } else if(!allTouristsScared) {
        for(int i = signs.size() - 1; i >= 0; i--) {
          Sign s = signs.get(i);
          if(s.active()) {
            s.renderMessage(g, camera, dark);
            break;
          }
        }
      } else {
        int startX = round(camera.x - 100);
        int startY = round(camera.y - 50);
        
        if(levelIndex < MAX_LEVEL_INDEX) {
          font.renderString(g, "LEVEL COMPLETE!", startX, startY + (10));
          font.renderString(g, "PRESS SPACE TO", startX, startY + (20));
          font.renderString(g, "CONTINUE.", startX, startY + (30));
        } else {
          font.renderString(g, "GAME COMPLETE!", startX, startY + (10));
          font.renderString(g, "PRESS SPACE TO", startX, startY + (20));
          font.renderString(g, "PLAY AGAIN!", startX, startY + (30));
        }
      }
      
      g.dispose();
      strategy.show();
      
      if(player.hit && (System.currentTimeMillis() - player.hitTime) > 2000 ) {
        reset();
      }

      if(allTouristsScared && finishTime == -1) {
        finishTime = System.currentTimeMillis();
      }
      
      if(allTouristsScared && (System.currentTimeMillis() - finishTime) > 500 && input.isKeyDown(KeyEvent.VK_SPACE)) {
        levelIndex++;
        if(levelIndex > MAX_LEVEL_INDEX) {
          levelIndex = 0;
        }
        reset();
      }
      
      if(timeToPlayScaredSound > 0 && System.currentTimeMillis() > timeToPlayScaredSound) {
        playScaredSound();
      }
      
      try {
        Thread.sleep(5);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }    
  }
  
  public void reset() {
    if(levelIndex == 0) showingTitle = true;
    levelStart = System.currentTimeMillis();
    entities = new ArrayList<Entity>();
    platforms = new ArrayList<Platform>();
    tourists = new ArrayList<Tourist>();
    flashes = new ArrayList<Flash>();
    signs = new ArrayList<Sign>();
    
    ensureNotDark();
    finishTime = -1;
    
    Level level = newLevelForIndex();
    
    // TODO end screen.
    if(level == null) System.exit(0);
    
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
                                               "switch_dark.png",
                                               "font.png",
                                               "sign.png",
                                               "change.png");
    
    bgTile = new BackgroundTile(imageManager.get("wallpaper.png"), imageManager.get("wallpaper2.png"), imageManager.get("wallpaper_dark.png"), imageManager.get("wallpaper_dark.png"));
    
    player = new Player(new Image[]{imageManager.get("ghost1.png"), imageManager.get("ghost2.png"), imageManager.get("ghost3.png"), imageManager.get("ghost2.png")},
                        new Image[]{imageManager.get("ghost_jump.png")},
                        new Image[]{imageManager.get("clock.png"), imageManager.get("flower.png"), imageManager.get("hatstand.png")},
                        new Image[]{imageManager.get("ghost1_dark.png"), imageManager.get("ghost2_dark.png"), imageManager.get("ghost3_dark.png"), imageManager.get("ghost2_dark.png")},
                        new Image[]{imageManager.get("ghost_jump_dark.png")},
                        imageManager.get("change.png"));
    camera = new EntityTrackingCamera(player, this);
    
    font = new Font(imageManager.get("font.png"));
    
    Point2D.Float playerLoc = level.playerStart;
    player.x = playerLoc.x;
    player.nextY = playerLoc.y;
    player.w = 16;
    player.h = 16;
    
    List<Rectangle2D.Float> levelPlatforms = level.platforms;
    for(int i = 0; i < levelPlatforms.size(); i++) {
      Rectangle2D.Float r = levelPlatforms.get(i);
      addPlatform(r.x + (r.width / 2), r.y + (r.height / 2), r.width, r.height);  
    }
    
    List<Rectangle2D.Float> levelTourists = level.tourists;
    for(int i = 0; i < levelTourists.size(); i++) {
      Rectangle2D.Float r = levelTourists.get(i);
      addTourist((float)r.getCenterX(), r.y, r.x, (float)r.getMaxX());
    }
    
    for(int i = 0; i < level.signs.size(); i++) {
      Sign sign = level.signs.get(i);
      Sign newSign = new Sign(round(sign.nextX), round(sign.nextY), sign.message, sign.darkMessage);
      newSign.font = font;
      newSign.image = imageManager.get("sign.png");
      signs.add(newSign);
      entities.add(newSign);
    }
    
    entities.add(player);
    
    for(int i = 0; i < entities.size(); i++) {
      entities.get(i).applyNext();
    }
    
    setLightSwitch(level.lightSwitch.x, level.lightSwitch.y);
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
      playSwitchSound();
      dark = true;
    }
  }
  
  public void ensureNotDark() {
    if(dark) {
      dark = false;
    }
  }
  
  private Level newLevelForIndex() {
    switch(levelIndex) {
     case 0: return new Level1();
     case 1: return new Level2();
     case 2: return new Level3();
     case 3: return new Level4();
     case 4: return new Level5();
     case 5: return new Level6();
     default: return null;
    }
  }
  
  public synchronized void playSwitchSound() {
    playSound("/switch.wav");
  }
  
  public synchronized void playJumpSound() {
    playSound("/jump" + randomInt(3) + ".wav");
  }
  
  public synchronized void playHideSound() {
    playSound("/hide" + randomInt(3) + ".wav");
  }
  
  public synchronized void playScareSound() {
    playSound("/scare" + randomInt(3) + ".wav");
  }
  
  public synchronized void playScaredSoundDelayed() {
    timeToPlayScaredSound = System.currentTimeMillis() + 200;
  }
  
  public synchronized void playScaredSound() {
    playSound("/scared" + randomInt(3) + ".wav");
    timeToPlayScaredSound = -1;
  }
  
  public synchronized void playClickSound() {
    playSound("/click.wav");
  }
  
  public synchronized void playMusic() {
    musicClip = playSound("/music1.wav", true);
  }
  
  private void playSound(String soundLoc) {
    playSound(soundLoc, false);
  }
  
  private Clip playSound(String soundLoc, boolean loop) {
    try {
      Clip clip = AudioSystem.getClip();
      AudioInputStream inputStream = AudioSystem.getAudioInputStream(Game.this.getClass().getResourceAsStream(soundLoc));
      clip.open(inputStream);
      if(loop) clip.loop(Clip.LOOP_CONTINUOUSLY);
      clip.start(); 
      
      return clip;
    } catch (Exception e) {
      System.err.println(e.getMessage());
      return null;
    }
  }
  
  public static int randomInt(int max) { return (int)(random() * max);}

}
