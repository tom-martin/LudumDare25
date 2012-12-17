package com.heychinaski.ld25;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Level3 extends Level {
  
  {
    playerStart = new Point2D.Float(0, -32);
    
    platforms = new ArrayList<Rectangle2D.Float>();
    platforms.add(rect(-512, -320, -32, 256));
    platforms.add(rect(-32, 0, 128, 256));
    platforms.add(rect(192, 0, 256, 32));
    platforms.add(rect(336, 0, 400, 32));
    platforms.add(rect(512, 0, 768, 32));
    platforms.add(rect(768, -320, 1280, 256));
    
    tourists = new ArrayList<Rectangle2D.Float>();
    tourists.add(rect(704, -32, 760, -32));
    tourists.add(rect(576, -32, 704, -32));
    
    lightSwitch = new Point2D.Float(544, -16);
    signs = new ArrayList<Sign>();
    
    furniture = new ArrayList<Point2D.Float>();
    furniture.add(new Point2D.Float(32, -32));
    furniture.add(new Point2D.Float(352, -32));
    furniture.add(new Point2D.Float(576, -32));
    furniture.add(new Point2D.Float(616, -32));
  }

  private Rectangle2D.Float rect(int left, int top, int right, int bottom) {
    return new Rectangle2D.Float(left, top, right - left, bottom - top);
  }

}
