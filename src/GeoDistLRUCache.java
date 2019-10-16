import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.Hashtable;
import java.util.Set;

public class GeoDistLRUCache {

  private Hashtable<Point2D.Double, LRUCache> geoCache = new Hashtable<Point2D.Double, LRUCache>();

  GeoDistLRUCache(long globalExpiry, int globalCapacity, Point2D.Double[] locations) {
    for (Double location : locations) {
      geoCache.put(location, new LRUCache(globalCapacity, globalExpiry));
    }
  }

  public void put(int key, int value) {
    geoCache.forEach((k,v) -> {
      v.put(key, value);
    });
  }

  public int get(int key, Point2D.Double location) throws Exception {
    Set<Double> locs = geoCache.keySet();
    double minDist = java.lang.Double.MAX_VALUE;
    Point2D.Double nearestCache = location;
    for (Point2D.Double loc: locs) {
      if(location.distance(loc)<minDist) {
        nearestCache = loc;
        minDist = location.distance(loc);
      }
    }
    return geoCache.get(nearestCache).get(key);
  }
}
