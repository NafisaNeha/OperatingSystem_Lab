import java.util.*;

class Process {

  int processNumber, arrivalTime, cpuBurstTime, waitingTime, turnaroundTime, completionTime;

  Process(int processNumber, int arrivalTime, int cpuBurstTime) {
    this.processNumber = processNumber;
    this.arrivalTime = arrivalTime;
    this.cpuBurstTime = cpuBurstTime;
  }
}

public class FirstComeFirstServe {

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

    int currentTime = processes[0].arrivalTime;
    System.out.println("\nGantt Chart:");

    System.out.print(currentTime);
    for (int i = 0; i < numberOfProcesses; i++) {
      int previousTime = currentTime;
      currentTime += processes[i].cpuBurstTime;

      for (int j = 0; j < currentTime - previousTime; j++) {
        System.out.print(
          "--" + (char) ('A' + processes[i].processNumber) + "--"
        );
      }
      System.out.print(currentTime);

      processes[i].completionTime =
        Math.max(currentTime, processes[i].arrivalTime);
      processes[i].turnaroundTime =
        processes[i].completionTime - processes[i].arrivalTime;
      processes[i].waitingTime =
        processes[i].turnaroundTime - processes[i].cpuBurstTime;
    }

    System.out.println();
    int totalWaitingTime = 0;
    int totalTurnaroundTime = 0;

    for (int i = 0; i < numberOfProcesses; i++) {
      totalTurnaroundTime += processes[i].turnaroundTime;
      totalWaitingTime += processes[i].waitingTime;
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
