import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class LRUCache {

  private final ConcurrentHashMap<Integer, Node> cache = new ConcurrentHashMap<Integer, Node>();
  private int count, capacity;
  private final long expireTime;
  private Node head, tail;

  class Node {
    int key;
    int value;
    LocalDateTime lastAccess = LocalDateTime.now();
    Node prev;
    Node next;
  }

  //Cleanup thread runs to remove expired nodes
  final class Cleanup implements Runnable {
    private final LRUCache instance;

    Cleanup(LRUCache instance) {
      this.instance = instance;
    }

    @Override
    public void run() {
      while (true) {
        try {
          TimeUnit.SECONDS.sleep(1);
          instance.cleanup();
        } catch (InterruptedException ie) {
          ie.printStackTrace();
        }
      }
    }
  }

  private void addNode(Node node) {
    node.prev = head;
    node.next = head.next;

    head.next.prev = node;
    head.next = node;
  }

  private void removeNode(Node node) {
    Node prev = node.prev;
    Node next = node.next;

    prev.next = next;
    next.prev = prev;
  }

  private void addToHead(Node node) {
    removeNode(node);
    addNode(node);
  }

  private Node removeTail() {
    Node pop = tail.prev;
    removeNode(pop);
    return pop;
  }

  public LRUCache(int capacity, long expireTime) {
    count = 0;
    this.capacity = capacity;
    this.expireTime = expireTime;

    head = new Node();
    head.prev = null;

    tail = new Node();
    tail.next = null;

    head.next = tail;
    tail.prev = head;

    Thread cleanupThread = new Thread(new Cleanup(this));
    cleanupThread.setDaemon(true);
    cleanupThread.start();
  }

  public int get(int key) throws Exception {
    Node node = cache.get(key);
    if (node == null) {
      throw new NoSuchElementException("Key " + key + " not found");
    }

    node.lastAccess = LocalDateTime.now();
    addToHead(node);
    return node.value;
  }

  public void put(int key, int value) {
    Node node = cache.get(key);
    if(node != null) {
      node.value = value;
      addToHead(node);
    } else {
      Node nodeToAdd = new Node();
      nodeToAdd.key = key;
      nodeToAdd.value = value;

      cache.put(key, nodeToAdd);
      addNode(nodeToAdd);
      ++count;

      if(count>capacity) {
        Node tail = removeTail();
        cache.remove(tail.key);
        --count;
      }
    }
  }

  //Test method for printing what is currently in the cache
  public void printCache() {
    Node current = head;
    while (current != null) {
      System.out.println("Key: "+ current.key + " Value: " + current.value);
      current = current.next;
    }
  }

  private void cleanup() {
    Iterator<Integer> keys = cache.keySet().iterator();
    LocalDateTime now = LocalDateTime.now();

    while(keys.hasNext()) {
      Node node = cache.get(keys.next());
      synchronized (cache) {
        if(node != null) {
          LocalDateTime lastAccess = node.lastAccess;
          long timeElapsed = ChronoUnit.SECONDS.between(lastAccess, now);
          if(timeElapsed > expireTime) {
            removeNode(node);
            cache.remove(node.key);
            Thread.yield();
          }
        }
      }
    }
  }
}
