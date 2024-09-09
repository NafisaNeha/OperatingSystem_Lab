import java.util.*;

class Process {

  int processNumber, arrivalTime, cpuBurstTime, remainingBurstTime, waitingTime, turnaroundTime, completionTime;
  boolean isFinished = false;

  Process(int processNumber, int arrivalTime, int cpuBurstTime) {
    this.processNumber = processNumber;
    this.arrivalTime = arrivalTime;
    this.cpuBurstTime = cpuBurstTime;
    this.remainingBurstTime = cpuBurstTime;
  }
}

public class ShortestRemainingTimeNext {

  private static final char START_CHAR = 'A';

  public static void main(String[] args) {
    Scanner inputScanner = new Scanner(System.in);
    System.out.print("Enter the number of processes: ");
    int numberOfProcesses = inputScanner.nextInt();

    Process[] processes = new Process[numberOfProcesses];
    System.out.println("Enter the CPU burst times and arrival times:");

    for (int i = 0; i < numberOfProcesses; i++) {
      System.out.print(
        "Enter CPU burst time and Arrival time for process " +
        (char) ('A' + i) +
        ": "
      );
      int cpuBurstTime = inputScanner.nextInt();
      int arrivalTime = inputScanner.nextInt();

      processes[i] = new Process(i, arrivalTime, cpuBurstTime);
    }

    Arrays.sort(processes, Comparator.comparingInt(p -> p.arrivalTime));

    PriorityQueue<Process> queue = new PriorityQueue<>(
      Comparator.comparingInt(p -> p.remainingBurstTime)
    );

    int currentTime = 0;
    int index = 0;
    int executionTime = 1;

    System.out.println("\nGantt Chart:");
    System.out.print(currentTime);

    while (
      index < numberOfProcesses && processes[index].arrivalTime <= currentTime
    ) {
      queue.add(processes[index]);
      index++;
    }

    while (!queue.isEmpty()) {
      Process currentProcess = queue.poll();
      currentProcess.remainingBurstTime -= executionTime;
      currentTime += executionTime;

      System.out.print(
        "-- " +
        (char) (START_CHAR + currentProcess.processNumber) +
        " --" +
        currentTime
      );

      while (
        index < numberOfProcesses && processes[index].arrivalTime <= currentTime
      ) {
        Process nextProcess = processes[index];
        queue.add(nextProcess);
        index++;
      }

      if (currentProcess.remainingBurstTime > 0) {
        queue.add(currentProcess);
      } else {
        currentProcess.completionTime = currentTime;
        currentProcess.turnaroundTime =
          currentProcess.completionTime - currentProcess.arrivalTime;
        currentProcess.waitingTime =
          currentProcess.turnaroundTime - currentProcess.cpuBurstTime;
      }
    }

    System.out.println();
    int totalWaitingTime = 0;
    int totalTurnaroundTime = 0;

    for (Process p : processes) {
      totalWaitingTime += p.waitingTime;
      totalTurnaroundTime += p.turnaroundTime;
      System.out.print(
        "\nProcess " +
        (char) (START_CHAR + p.processNumber) +
        " - Arrival Time: " +
        p.arrivalTime +
        ", CPU Burst Time: " +
        p.cpuBurstTime +
        ", Waiting Time: " +
        p.waitingTime +
        ", Turnaround Time: " +
        p.turnaroundTime
      );
    }

    double averageWaitingTime = (double) totalWaitingTime / numberOfProcesses;
    double averageTurnaroundTime = (double) totalTurnaroundTime /
    numberOfProcesses;

    System.out.println("\nTotal Waiting Time: " + totalWaitingTime);
    System.out.println("Average Waiting Time: " + averageWaitingTime);
    System.out.println("Total Turnaround Time: " + totalTurnaroundTime);
    System.out.println("Average Turnaround Time: " + averageTurnaroundTime);
  }
}
