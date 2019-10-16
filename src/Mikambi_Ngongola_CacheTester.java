import java.awt.geom.Point2D;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

public class Mikambi_Ngongola_CacheTester {

  public static void main(String[] args) throws InterruptedException {
    /*
    Locations are represented by point on a grid.
    This point is the latitude and longitude.
    Point2D is used for distance calculations
     */
    Point2D.Double[] locations = new Point2D.Double[]{
        new Point2D.Double(1.0,1.0), //USA
        new Point2D.Double(3.4,5.1), //Canada
        new Point2D.Double(2.0,1.1), //Hong Kong
        new Point2D.Double(6.0,1.0), //China
        new Point2D.Double(4.0,4.2) //France
    };

    GeoDistLRUCache hardCache = new GeoDistLRUCache(10,4,locations);

    for (int i = 0; i < 3; i++) {
      Thread thread = new ClientThread(hardCache);
      thread.start();
    }
    TimeUnit.SECONDS.sleep(2);
  }

  public static class ClientThread extends Thread {

    private GeoDistLRUCache cache;
    private Point2D.Double myLocation;

    ClientThread(GeoDistLRUCache cache) {
      this.cache = cache;
      this.myLocation = new Point2D.Double(Math.random()*10,Math.random()*10);
    }

    @Override
    public void run() {
      try {
        for (int i=0; i<10; i++) {
          cache.put(i,i);
        }
        System.out.println("-------"+Thread.currentThread().getName()+" My Location: "+myLocation+"-------");
        for (int i=0; i<10; i++) {
          print(i);
        }
        System.out.println("-------"+Thread.currentThread().getName()+" Waiting for expiry-------");
        TimeUnit.SECONDS.sleep(20);
        for (int i=0; i<10; i++) {
          print(i);
        }
      } catch (InterruptedException ie) {
        ie.printStackTrace();
      }
    }

    private void print(int i) {
      try {
        System.out.println(Thread.currentThread().getName() + ": key-" + i + " value="
            + cache.get(i, myLocation));
      } catch (NoSuchElementException e) {
        System.out.println(Thread.currentThread().getName() + ": " + e.getMessage());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
