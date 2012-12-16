package com.heychinaski.ld25;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

public abstract class Level {
  Point2D.Float playerStart;
  List<Rectangle2D.Float> platforms;
  List<Rectangle2D.Float> tourists;
  Point2D.Float lightSwitch;
  List<Sign> signs;
}
