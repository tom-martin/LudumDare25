package com.heychinaski.ld25;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Level5 extends Level {
  
  {
    playerStart = new Point2D.Float(0, -32);
    
    platforms = new ArrayList<Rectangle2D.Float>();
    platforms.add(rect(-512, -320, -32, 256));
    platforms.add(rect(-32, 0, 128, 256));
    
    platforms.add(rect(192, 0, 320, 32));
    
    platforms.add(rect(416, 0, 480, 32));
    
    platforms.add(rect(576, 0, 640, 32));
    
    platforms.add(rect(736, 0, 800, 32));
    platforms.add(rect(800, -320, 1280, 256));
    
    tourists = new ArrayList<Rectangle2D.Float>();
    tourists.add(rect(192, -32, 320, -32));
    tourists.add(rect(576, -32, 640, -32));
    
    lightSwitch = new Point2D.Float(768, -16);
    signs = new ArrayList<Sign>();
    
    furniture = new ArrayList<Point2D.Float>();
    furniture.add(new Point2D.Float(80, -32));
    furniture.add(new Point2D.Float(256, -32));
    furniture.add(new Point2D.Float(416, -32));
  }

  private Rectangle2D.Float rect(int left, int top, int right, int bottom) {
    return new Rectangle2D.Float(left, top, right - left, bottom - top);
  }

}
