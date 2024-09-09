import java.util.*;

class Process {

  int processNumber, arrivalTime, cpuBurstTime, waitingTime, turnaroundTime, completionTime;
  boolean isFinished = false;

  Process(int processNumber, int arrivalTime, int cpuBurstTime) {
    this.processNumber = processNumber;
    this.arrivalTime = arrivalTime;
    this.cpuBurstTime = cpuBurstTime;
  }
}

public class MaxWaitingTimeFirst {

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

    for (int i = 0; i < numberOfProcesses - 1; i++) {
      for (int j = 0; j < numberOfProcesses - i - 1; j++) {
        if (processes[j].arrivalTime > processes[j + 1].arrivalTime) {
          Process temp = processes[j];
          processes[j] = processes[j + 1];
          processes[j + 1] = temp;
        }
      }
    }

    PriorityQueue<Process> queue = new PriorityQueue<>((p1, p2) -> {
      if (p1.waitingTime != p2.waitingTime) {
        return p2.waitingTime - p1.waitingTime;
      } else if (p1.cpuBurstTime != p2.cpuBurstTime) {
        return p2.cpuBurstTime - p1.cpuBurstTime;
      } else {
        return p1.processNumber - p2.processNumber;
      }
    });

    int currentTime = processes[0].arrivalTime;
    int index = 0;

    System.out.println("\nGantt Chart:");
    System.out.print(currentTime);

    while (!queue.isEmpty() || index < numberOfProcesses) {
      while (
        index < numberOfProcesses && processes[index].arrivalTime <= currentTime
      ) {
        processes[index].waitingTime =
          currentTime - processes[index].arrivalTime;
        queue.add(processes[index]);
        index++;
      }

      if (!queue.isEmpty()) {
        Process process = queue.poll();
        process.isFinished = true;
        currentTime += process.cpuBurstTime;
        process.completionTime = currentTime;
        process.turnaroundTime = process.completionTime - process.arrivalTime;
        process.waitingTime = process.turnaroundTime - process.cpuBurstTime;

        System.out.print(
          " --" + (char) ('A' + process.processNumber) + "-- " + currentTime
        );
      } else {
        currentTime = processes[index].arrivalTime;
      }
    }

    int totalWaitingTime = 0;
    int totalTurnaroundTime = 0;

    for (Process p : processes) {
      totalWaitingTime += p.waitingTime;
      totalTurnaroundTime += p.turnaroundTime;
      System.out.print(
        "\nProcess " +
        (char) ('A' + p.processNumber) +
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
