package com.heychinaski.ld25;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Level6 extends Level {
  
  {
    playerStart = new Point2D.Float(0, -32);
    
    platforms = new ArrayList<Rectangle2D.Float>();
    platforms.add(rect(-512, -320, -32, 256));
    platforms.add(rect(-32, 0, 128, 256));
    
    platforms.add(rect(192, 0, 256, 32));
    
    platforms.add(rect(352, 0, 416, 32));
    
    platforms.add(rect(480, -64, 544, -32));
    
    platforms.add(rect(608, -128, 736, 256));
    
    platforms.add(rect(768, 128, 832, 256));
    platforms.add(rect(832, -320, 1280, 256));
    
    tourists = new ArrayList<Rectangle2D.Float>();
    tourists.add(rect(192, -32, 256, -32));
    tourists.add(rect(352, -32, 416, -32));
    tourists.add(rect(608, -160, 672, -32));
    tourists.add(rect(480, -96, 544, -96));
    tourists.add(rect(768, -96, 831, -96));
    
    lightSwitch = new Point2D.Float(672, -144);
    signs = new ArrayList<Sign>();
    
    furniture = new ArrayList<Point2D.Float>();
    furniture.add(new Point2D.Float(16, -32));
    furniture.add(new Point2D.Float(96, -32));
    furniture.add(new Point2D.Float(378, -32));
    furniture.add(new Point2D.Float(688, -160));
    furniture.add(new Point2D.Float(784, 96));
  }

  private Rectangle2D.Float rect(int left, int top, int right, int bottom) {
    return new Rectangle2D.Float(left, top, right - left, bottom - top);
  }

}
