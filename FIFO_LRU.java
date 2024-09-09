import java.util.*;

public class FIFO_LRU {

  static int pageFaultsFIFO(int pages[], int capacity) {
    HashSet<Integer> s = new HashSet<>(capacity);
    Deque<Integer> indexes = new LinkedList<>();
    int pageFaults = 0;

    for (int i = 0; i < pages.length; ++i) {
      if (!s.contains(pages[i])) {
        if (s.size() < capacity) {
          s.add(pages[i]);
          pageFaults++;
          indexes.addLast(pages[i]);
        } else {
          int val = indexes.removeFirst();
          if (s.contains(val)) {
            s.remove(val);
          }
          s.add(pages[i]);
          indexes.addLast(pages[i]);
          pageFaults++;
        }
      }
    }

    return pageFaults;
  }

  static int pageFaultsLRU(int pages[], int capacity) {
    HashSet<Integer> s = new HashSet<>(capacity);
    HashMap<Integer, Integer> indexes = new HashMap<>();
    int pageFaults = 0;

    for (int i = 0; i < pages.length; ++i) {
      if (!s.contains(pages[i])) {
        if (s.size() < capacity) {
          s.add(pages[i]);

          pageFaults++;

          indexes.put(pages[i], i);
        } else {
          int lru = Integer.MAX_VALUE, val = Integer.MIN_VALUE;

          for (int page : s) {
            if (indexes.get(page) < lru) {
              lru = indexes.get(page);
              val = page;
            }
          }
          s.remove(val);
          indexes.remove(val);
          s.add(pages[i]);
          pageFaults++;

          indexes.put(pages[i], i);
        }
      }
    }

    return pageFaults;
  }

  public static void main(String args[]) {
    int pages[] = { 7, 0, 1, 2, 0, 3, 0, 4, 2, 3, 0, 3, 2 };
    int capacity = 3;

    System.out.println("FIFO Page Faults: " + pageFaultsFIFO(pages, capacity));
    System.out.println("LRU Page Faults: " + pageFaultsLRU(pages, capacity));
  }
}
