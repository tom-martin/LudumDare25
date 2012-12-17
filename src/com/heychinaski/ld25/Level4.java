package com.heychinaski.ld25;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Level4 extends Level {
  
  {
    playerStart = new Point2D.Float(0, -32);
    
    platforms = new ArrayList<Rectangle2D.Float>();
    platforms.add(rect(-512, -320, -32, 256));
    platforms.add(rect(-32, 0, 704, 256));
    platforms.add(rect(-32, -416, 384, -48));
    platforms.add(rect(512, -224, 576, -48));
    platforms.add(rect(704, -416, 1280, 256));
    
    platforms.add(rect(384, -80, 480, -48));
    platforms.add(rect(416, -128, 512, -96));
    platforms.add(rect(384, -176, 480, -144));
    platforms.add(rect(416, -224, 512, -192));
    
    platforms.add(rect(384, -416, 704, -272));
    
    tourists = new ArrayList<Rectangle2D.Float>();
    tourists.add(rect(128, -32, 256, -32));
    tourists.add(rect(256, -32, 384, -32));
    tourists.add(rect(640, -32, 696, -32));
    
    lightSwitch = new Point2D.Float(544, -240);
    signs = new ArrayList<Sign>();
    
    furniture = new ArrayList<Point2D.Float>();
    furniture.add(new Point2D.Float(72, -32));
    furniture.add(new Point2D.Float(248, -32));
    furniture.add(new Point2D.Float(544, -32));
    furniture.add(new Point2D.Float(456, -256));
  }

  private Rectangle2D.Float rect(int left, int top, int right, int bottom) {
    return new Rectangle2D.Float(left, top, right - left, bottom - top);
  }

}
