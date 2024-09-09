import java.util.Scanner;

public class BankersAlgorithm {

  private int processCount;
  private int resourceCount;
  private int[][] max;
  private int[][] allocation;
  private int[][] need;
  private int[] available;

  public BankersAlgorithm(int processCount, int resourceCount) {
    this.processCount = processCount;
    this.resourceCount = resourceCount;
    max = new int[processCount][resourceCount];
    allocation = new int[processCount][resourceCount];
    need = new int[processCount][resourceCount];
    available = new int[resourceCount];
  }

  public void initializeData() {
    Scanner scanner = new Scanner(System.in);

    // Input maximum resource need for each process
    System.out.println("Enter the maximum resource need for each process:");
    for (int i = 0; i < processCount; i++) {
      for (int j = 0; j < resourceCount; j++) {
        max[i][j] = scanner.nextInt();
      }
    }

    // Input resource allocation for each process
    System.out.println("Enter the resource allocation for each process:");
    for (int i = 0; i < processCount; i++) {
      for (int j = 0; j < resourceCount; j++) {
        allocation[i][j] = scanner.nextInt();
        // Calculate initial need matrix
        need[i][j] = max[i][j] - allocation[i][j];
      }
    }

    // Input available resources
    System.out.println("Enter the available resources:");
    for (int j = 0; j < resourceCount; j++) {
      available[j] = scanner.nextInt();
    }
  }

  public boolean isSafeState() {
    boolean[] finish = new boolean[processCount];
    int[] work = new int[resourceCount];

    // Initialize work matrix with available resources
    System.arraycopy(available, 0, work, 0, resourceCount);

    // Initialize finish array
    for (int i = 0; i < processCount; i++) {
      finish[i] = false;
    }

    // Find an unmarked process that can be satisfied with current available resources
    for (int i = 0; i < processCount; i++) {
      if (!finish[i] && canAllocateResources(i, work)) {
        // Mark the process as finished and release allocated resources
        finish[i] = true;
        for (int j = 0; j < resourceCount; j++) {
          work[j] += allocation[i][j];
        }
        // Restart the process iteration
        i = -1;
      }
    }

    // If all processes are marked as finished, the system is in a safe state
    for (boolean aFinish : finish) {
      if (!aFinish) {
        return false;
      }
    }
    return true;
  }

  private boolean canAllocateResources(int process, int[] work) {
    for (int j = 0; j < resourceCount; j++) {
      if (need[process][j] > work[j]) {
        return false;
      }
    }
    return true;
  }

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    // Input the number of processes and resources
    System.out.println("Enter the number of processes:");
    int processCount = scanner.nextInt();

    System.out.println("Enter the number of resources:");
    int resourceCount = scanner.nextInt();

    BankersAlgorithm bankersAlgorithm = new BankersAlgorithm(
      processCount,
      resourceCount
    );
    bankersAlgorithm.initializeData();

    // Check if the system is in a safe state
    if (bankersAlgorithm.isSafeState()) {
      System.out.println("The system is in a safe state.");
    } else {
      System.out.println("The system is NOT in a safe state.");
    }
  }
}
