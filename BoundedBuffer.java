import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedBuffer {

  private static final int BUFFER_SIZE = 5;
  private Integer[] buffer = new Integer[BUFFER_SIZE];

  private int in = 0, out = 0;
  private ReentrantLock lock = new ReentrantLock();
  private Condition notFull = lock.newCondition();
  private Condition notEmpty = lock.newCondition();

  public void produce(int data) throws InterruptedException {
    lock.lock();
    try {
      while (countEmpty() == 0) {
        notFull.await();
      }

      buffer[in] = data;
      in = (in + 1) % BUFFER_SIZE;

      System.out.println(
        "Producer: Data " + data + " is inserted in the buffer"
      );
      System.out.println("Empty space: " + countEmpty());
      System.out.println("Full space: " + (BUFFER_SIZE - countEmpty()));

      notEmpty.signalAll();
    } finally {
      lock.unlock();
    }
  }

  public void consume() throws InterruptedException {
    lock.lock();
    try {
      while (countEmpty() == BUFFER_SIZE) {
        notEmpty.await();
      }

      int data = buffer[out];
      buffer[out] = null;
      out = (out + 1) % BUFFER_SIZE;

      System.out.println(
        "Consumer: Consumed Data " + data + " from the buffer"
      );
      System.out.println("Empty space: " + countEmpty());
      System.out.println("Full space: " + (BUFFER_SIZE - countEmpty()));

      notFull.signalAll();
    } finally {
      lock.unlock();
    }
  }

  int countEmpty() {
    int count = 0;
    for (int i = 0; i < BUFFER_SIZE; i++) {
      if (buffer[i] == null) {
        count++;
      }
    }
    return count;
  }
}
