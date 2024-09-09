package readerwriter;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class ReaderWriter {

  public static void main(String[] args) {
    SharedResource sharedResource = new SharedResource();

    Thread reader1 = new Thread(new Reader(sharedResource));
    Thread reader2 = new Thread(new Reader(sharedResource));
    Thread writer1 = new Thread(new Writer(sharedResource));
    Thread writer2 = new Thread(new Writer(sharedResource));

    reader1.start();
    writer1.start();
    writer2.start();
    reader2.start();
  }
}

class SharedResource {

  private int[] data = { 10, 20, 30, 40, 50, 60 };
  private Semaphore mutex = new Semaphore(1);
  private Semaphore db = new Semaphore(1);
  private int readersCount = 0;

  public void readData() {
    String name = Thread.currentThread().getName();
    char id = name.charAt(name.length() - 1);
    try {
      mutex.acquire();
      readersCount++;
      if (db.availablePermits() == 0 && readersCount == 0) System.out.println(
        "Reader " + id + ": Waiting"
      );

      if (readersCount == 1) {
        db.acquire();
        printBuffer();
      }
      mutex.release();

      Random rand = new Random();
      int value = rand.nextInt(5);
      System.out.println("Reader " + id + ": Reading " + data[value]);

      mutex.acquire();
      readersCount--;
      System.out.println("Reader " + id + ": Leaves");
      if (readersCount == 0) {
        db.release();
        Thread.sleep(rand.nextInt(1500));
      }
      mutex.release();
    } catch (InterruptedException e) {}
  }

  public void writeData() {
    String name = Thread.currentThread().getName();
    char id = name.charAt(name.length() - 1);
    try {
      if (db.availablePermits() == 0) System.out.println(
        "Writer " + id + ": Waiting"
      );
      db.acquire();
      Random rand = new Random();
      int value = rand.nextInt(5);
      int newValue = rand.nextInt(50);
      data[value] = newValue;
      Thread.sleep(rand.nextInt(100));
      System.out.println("Writer " + id + ": Writes in the system " + newValue);
      printBuffer();
      db.release();
      System.out.println("Writer " + id + ": Leaves");
    } catch (InterruptedException e) {
      System.out.println("Writer " + id + ": Waiting");
    }
  }

  void printBuffer() {
    System.out.print("Data in Buffer: ");
    for (int i = 0; i < 6; i++) System.out.print(data[i] + " ");
    System.out.println("");
  }
}

class Reader implements Runnable {

  private SharedResource sharedResource;

  public Reader(SharedResource sharedResource) {
    this.sharedResource = sharedResource;
  }

  @Override
  public void run() {
    while (true) {
      sharedResource.readData();
    }
  }
}

class Writer implements Runnable {

  private SharedResource sharedResource;

  public Writer(SharedResource sharedResource) {
    this.sharedResource = sharedResource;
  }

  @Override
  public void run() {
    while (true) {
      sharedResource.writeData();
    }
  }
}
