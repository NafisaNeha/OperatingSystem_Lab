class FirstProducer implements Runnable {

  private BoundedBuffer buffer;

  public FirstProducer(BoundedBuffer buffer) {
    this.buffer = buffer;
  }

  @Override
  public void run() {
    for (int i = 1; i <= 10; i++) {
      try {
        buffer.produce(i * 10);
        Thread.sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}

class SecondProducer implements Runnable {

  private BoundedBuffer buffer;

  public SecondProducer(BoundedBuffer buffer) {
    this.buffer = buffer;
  }

  @Override
  public void run() {
    for (int i = 1; i <= 10; i++) {
      try {
        buffer.produce(i * 100);
        Thread.sleep(200);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}

class FirstConsumer implements Runnable {

  private BoundedBuffer buffer;

  public FirstConsumer(BoundedBuffer buffer) {
    this.buffer = buffer;
  }

  @Override
  public void run() {
    for (int i = 1; i <= 10; i++) {
      try {
        buffer.consume();
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}

class SecondConsumer implements Runnable {

  private BoundedBuffer buffer;

  public SecondConsumer(BoundedBuffer buffer) {
    this.buffer = buffer;
  }

  @Override
  public void run() {
    for (int i = 1; i <= 10; i++) {
      try {
        buffer.consume();
        Thread.sleep(700);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}

public class ProducerConsumer {

  public static void main(String[] args) {
    BoundedBuffer buffer = new BoundedBuffer();
    FirstProducer firstProducer = new FirstProducer(buffer);
    SecondProducer secondProducer = new SecondProducer(buffer);
    FirstConsumer firstConsumer = new FirstConsumer(buffer);
    SecondConsumer secondConsumer = new SecondConsumer(buffer);

    Thread producer1 = new Thread(firstProducer);
    Thread consumer1 = new Thread(firstConsumer);
    Thread producer2 = new Thread(secondProducer);
    Thread consumer2 = new Thread(secondConsumer);

    producer1.start();
    consumer1.start();
    producer2.start();
    consumer2.start();
  }
}
