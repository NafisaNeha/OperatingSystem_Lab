import java.util.*;

class Process {

  int processNumber, arrivalTime, cpuBurstTime, waitingTime, turnaroundTime, completionTime, cpuTimeLeft;

  Process(int processNumber, int arrivalTime, int cpuBurstTime) {
    this.processNumber = processNumber;
    this.arrivalTime = arrivalTime;
    this.cpuBurstTime = cpuBurstTime;
    this.cpuTimeLeft = cpuBurstTime;
  }
}

public class RoundRobin20200204049 {

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

    System.out.print("Enter the time quantum for Round Robin: ");
    int timeQuantum = inputScanner.nextInt();

    int currentTime = processes[0].arrivalTime;
    Queue<Process> readyQueue = new LinkedList<>();
    int processIndex = 0;

    System.out.println("\nGantt Chart:");
    System.out.print(currentTime);

    while (
      processIndex < numberOfProcesses &&
      processes[processIndex].arrivalTime <= currentTime
    ) {
      readyQueue.add(processes[processIndex]);
      processIndex++;
    }

    while (!readyQueue.isEmpty()) {
      Process process = readyQueue.poll();
      int executionTime = Math.min(timeQuantum, process.cpuTimeLeft);
      process.cpuTimeLeft -= executionTime;
      currentTime += executionTime;

      System.out.print(
        "-- " +
        (char) (START_CHAR + process.processNumber) +
        " --" +
        currentTime
      );

      while (
        processIndex < numberOfProcesses &&
        processes[processIndex].arrivalTime <= currentTime
      ) {
        Process nextProcess = processes[processIndex];
        // nextProcess.waitingTime += currentTime - nextProcess.arrivalTime;
        readyQueue.add(nextProcess);
        processIndex++;
      }

      if (process.cpuTimeLeft > 0) {
        readyQueue.add(process);
      } else {
        process.completionTime = currentTime;
        process.turnaroundTime = process.completionTime - process.arrivalTime;
        process.waitingTime = process.turnaroundTime - process.cpuBurstTime;
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
