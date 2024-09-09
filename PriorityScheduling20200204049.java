import java.util.*;

class Process {

  int processNumber, arrivalTime, cpuBurstTime, waitingTime, turnaroundTime, completionTime, priority, originalCpuBurstTime;

  Process(int processNumber, int arrivalTime, int cpuBurstTime, int priority) {
    this.processNumber = processNumber;
    this.arrivalTime = arrivalTime;
    this.cpuBurstTime = cpuBurstTime;
    this.priority = priority;
  }
}

public class PriorityScheduling20200204049 {

  private static final char START_CHAR = 'A';

  public static void main(String[] args) {
    Scanner inputScanner = new Scanner(System.in);
    System.out.print("Enter the number of processes: ");
    int numberOfProcesses = inputScanner.nextInt();

    Process[] processes = new Process[numberOfProcesses];
    System.out.println(
      "Enter the CPU burst times, arrival times, and priorities:"
    );

    for (int i = 0; i < numberOfProcesses; i++) {
      System.out.printf(
        "Enter CPU burst time, Arrival time, and Priority for process %c: ",
        START_CHAR + i
      );
      int cpuBurstTime = inputScanner.nextInt();
      int arrivalTime = inputScanner.nextInt();
      int priority = inputScanner.nextInt();

      processes[i] = new Process(i, arrivalTime, cpuBurstTime, priority);
    }

    Arrays.sort(processes, Comparator.comparingInt(p -> p.arrivalTime));

    nonPreemptivePriorityScheduling(Arrays.copyOf(processes, processes.length));

    preemptivePriorityScheduling(Arrays.copyOf(processes, processes.length));
  }

  // Non-Preemptive Priority Scheduling
  private static void nonPreemptivePriorityScheduling(Process[] processes) {
    PriorityQueue<Process> queue = new PriorityQueue<>(
      Comparator.comparingInt(p -> p.priority)
    );
    List<Process> completedProcesses = new ArrayList<>();

    int currentTime = 0;
    int index = 0;

    System.out.println("\nNon-Preemptive Gantt Chart:");
    System.out.print(currentTime);

    while (!queue.isEmpty() || index < processes.length) {
      while (
        index < processes.length && processes[index].arrivalTime <= currentTime
      ) {
        queue.add(processes[index]);
        index++;
      }

      if (!queue.isEmpty()) {
        Process currentProcess = queue.poll();
        int burstTime = currentProcess.cpuBurstTime;

        currentTime += burstTime;
        currentProcess.completionTime = currentTime;
        currentProcess.turnaroundTime =
          currentProcess.completionTime - currentProcess.arrivalTime;
        currentProcess.waitingTime =
          currentProcess.turnaroundTime - currentProcess.cpuBurstTime;

        for (int j = 0; j < burstTime; j++) {
          System.out.print(
            "-- " +
            (char) ('A' + currentProcess.processNumber) +
            " --" +
            currentTime
          );
        }

        completedProcesses.add(currentProcess);
      } else {
        currentTime = processes[index].arrivalTime;
      }
    }

    System.out.println();
    printResults(completedProcesses, "Non-Preemptive");
  }

  private static void preemptivePriorityScheduling(Process[] processes) {
    List<Process> completedProcesses = new ArrayList<>();
    PriorityQueue<Process> queue = new PriorityQueue<>(
      Comparator.comparingInt(p -> p.priority)
    );

    int currentTime = 0;
    int index = 0;

    System.out.println("\nPreemptive Gantt Chart:");
    System.out.print(currentTime);

    while (index < processes.length || !queue.isEmpty()) {
      while (
        index < processes.length && processes[index].arrivalTime <= currentTime
      ) {
        processes[index].originalCpuBurstTime = processes[index].cpuBurstTime;
        queue.add(processes[index]);
        index++;
      }

      if (!queue.isEmpty()) {
        Process currentProcess = queue.poll();
        int burstTime = currentProcess.cpuBurstTime;
        if (index < processes.length) {
          burstTime =
            Math.min(burstTime, processes[index].arrivalTime - currentTime);
        }

        currentTime += burstTime;
        currentProcess.cpuBurstTime -= burstTime;

        for (int j = 0; j < burstTime; j++) {
          System.out.print(
            "-- " +
            (char) ('A' + currentProcess.processNumber) +
            " --" +
            currentTime
          );
        }

        if (currentProcess.cpuBurstTime > 0) {
          queue.add(currentProcess);
        } else {
          currentProcess.completionTime = currentTime;
          currentProcess.turnaroundTime =
            currentProcess.completionTime - currentProcess.arrivalTime;
          currentProcess.waitingTime =
            currentProcess.turnaroundTime - currentProcess.originalCpuBurstTime;
          completedProcesses.add(currentProcess);
        }
      } else if (index < processes.length) {
        currentTime = processes[index].arrivalTime;
      }
    }

    System.out.println();
    printResults(completedProcesses, "Preemptive");
  }

  private static void printResults(List<Process> processes, String type) {
    System.out.println(type + " Priority Scheduling:");
    int totalWaitingTime = 0;
    int totalTurnaroundTime = 0;

    for (Process process : processes) {
      totalWaitingTime += process.waitingTime;
      totalTurnaroundTime += process.turnaroundTime;
      System.out.printf(
        "\nProcess %c - Arrival Time: %d, CPU Burst Time: %d, Waiting Time: %d, Turnaround Time: %d",
        START_CHAR + process.processNumber,
        process.arrivalTime,
        process.originalCpuBurstTime,
        process.waitingTime,
        process.turnaroundTime
      );
    }

    double averageWaitingTime = (double) totalWaitingTime / processes.size();
    double averageTurnaroundTime = (double) totalTurnaroundTime /
    processes.size();

    System.out.println("\nTotal Waiting Time: " + totalWaitingTime);
    System.out.println("Average Waiting Time: " + averageWaitingTime);
    System.out.println("Total Turnaround Time: " + totalTurnaroundTime);
    System.out.println("Average Turnaround Time: " + averageTurnaroundTime);
  }
}
