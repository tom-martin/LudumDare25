package com.heychinaski.ld25;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Level1 extends Level {
  
  {
    playerStart = new Point2D.Float(0, -32);
    
    platforms = new ArrayList<Rectangle2D.Float>();
    platforms.add(rect(-512, -320, -32, 256));
    platforms.add(rect(-32, 0, 640, 256));
    platforms.add(rect(128, -320, 320, -48));
    platforms.add(rect(448, -320, 1280, 256));
    
    tourists = new ArrayList<Rectangle2D.Float>();
    tourists.add(rect(128, -32, 320, -32));
    
    lightSwitch = new Point2D.Float(412, -16);
    signs = new ArrayList<Sign>();
    signs.add(new Sign(0, -16, new String[] {"USE THE LEFT AND RIGHT", "ARROW KEYS TO MOVE."}, null));
    signs.add(new Sign(64, -16, new String[] {"DON'T LET THE TOURISTS", "TAKE YOUR PICTURE!", "USE THE 'Z' KEY", "TO HIDE FROM THEM!"}, new String[]  {"SCARE ALL THE TOURISTS", "TO COMPLETE EACH LEVEL"}));
    signs.add(new Sign(352, -16, new String[] {"TOUCH THE SWITCH TO", "TURN OUT THE LIGHTS."}, new String[]  {"NOW YOU CAN SCARE", "AWAY THE TOURISTS!"}));
    
    furniture = new ArrayList<Point2D.Float>();
    furniture.add(new Point2D.Float(224, -32));
  }

  private Rectangle2D.Float rect(int left, int top, int right, int bottom) {
    return new Rectangle2D.Float(left, top, right - left, bottom - top);
  }

}
