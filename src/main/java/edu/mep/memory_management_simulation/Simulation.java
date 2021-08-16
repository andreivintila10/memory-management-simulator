package edu.mep.memory_management_simulation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

public class Simulation {
	private final long memorySize = 16670486528L;
	private final int pageSize = 4096;
	private final int timeSlice = 2;
	private final int noOfParallelProcesses = 8;
	private final PageFitting pageFitting = PageFitting.FIRST;

	private int notFitted = 0;

	public ArrayList<Process> getProcesses() throws FileNotFoundException {
		String inputFile = "/home/andrei/eclipse-workspace/memory-management-simulator/src/main/java/edu/mep/" +
		                   "memory_management_simulation/input-processes-generated.txt";

		ArrayList<Process> processes = new ArrayList<Process>();
		Process process = null;
		int id, size, arrivalTime, burstTime;

		File processesFile = new File(inputFile);
		Scanner sc = new Scanner(processesFile);
		int numberOfProcesses = sc.nextInt();
		for (int index = 0; index < numberOfProcesses; index++) {
			id = sc.nextInt();
			size = sc.nextInt();
			arrivalTime = sc.nextInt();
			burstTime = sc.nextInt();
			process = new Process(id, size, arrivalTime, burstTime);

			processes.add(process);
		}

		sc.close();

		return processes;
	}

	public ArrayList<Process> getArrivingProcesses(ArrayList<Process> processes, int systemTime) {
		ArrayList<Process> newProcesses = new ArrayList<Process>();
		for (Process process : processes)
			if (process.getArrivalTime() == systemTime)
				newProcesses.add(process);

		return newProcesses;
	}

	public Process getUnblockedProcess(Queue<Process> blockedQ) {
		Process blockedProcess = blockedQ.peek();
		if (blockedProcess == null)
			return null;

		if (blockedProcess.isWaiting()) {
			blockedProcess.incrementDeviceTime();
			return null;
		}

		blockedQ.remove();
		if (blockedProcess.getStatus() != Status.TERMINATED) {
			blockedProcess.setStatus(Status.READY);
			blockedProcess.resetDeviceTime();
		}

		return blockedProcess;
	}

	public String eventsToJSON(ArrayList<Event> events) {
		String jsonString = "var events = JSON.parse('[";
		for (Event event : events) {
			jsonString += event.toJSON() + ",";
		}
		jsonString = jsonString.substring(0, jsonString.length() - 1);
		jsonString += "]');";

		return jsonString;
	}

	public void saveSimulationHistory(ArrayList<Event> events) throws IOException {
		String outputFile = "/home/andrei/eclipse-workspace/memory-management-simulator/src/main/java/edu/mep/" +
		                    "memory_management_simulation/web/js/simulation-history.js";

		File processesFile = new File(outputFile);
		FileWriter fw = new FileWriter(processesFile);
		fw.write(eventsToJSON(events));
		fw.close();
	}

	public static void main(String[] args) {
		ArrayList<Event> events = new ArrayList<Event>();
		Event event;

		Simulation simulation = new Simulation();
		MemoryManager memoryManager = new MemoryManager(simulation.memorySize, simulation.pageSize);

		Queue<Process> blockedQ = new LinkedList<Process>();
		Queue<Process> readyQ = new LinkedList<Process>();

		Process[] cpuProcesses = new Process[simulation.noOfParallelProcesses];
		int[] currentTimeSlices = new int[simulation.noOfParallelProcesses];

		Random randomPage = new Random();
		randomPage.setSeed(172);

		try {
			ArrayList<Process> processes = simulation.getProcesses();
			for (Process process : processes)
				System.out.println(process);

			System.out.println();

			int systemTime = 0;
			while (systemTime < 200000) {
				for (Process process : simulation.getArrivingProcesses(processes, systemTime)) {
					System.out.println("Process " + process.getId() + " arrives at t=" + systemTime);
					if (memoryManager.allocateMemory(process, simulation.pageFitting)) {
						memoryManager.printMemoryMap();
						process.setEnterBlockedQTime(systemTime);
						blockedQ.add(process);

						event = new Event(EventType.PROCESS_ARRIVES, systemTime, process.getId(),
						                  memoryManager.getMemory().toJSON(),
						                  (Queue<Process>) new LinkedList<Process>(blockedQ));
						events.add(event);
					} else {
						System.out.println("Process " + process.getId() + " cannot be fitted into memory");
						event = new Event(EventType.PROCESS_NOT_FIT, systemTime, process.getId());
						events.add(event);
						simulation.notFitted++;
					}
				}

				Process unblockedProcess = simulation.getUnblockedProcess(blockedQ);
				if (unblockedProcess != null) {
					unblockedProcess.setResponseTime(unblockedProcess.getResponseTime() + systemTime -
					                                 unblockedProcess.getEnterBlockedQTime());

					if (unblockedProcess.getStatus() == Status.READY) {
						unblockedProcess.setEnterReadyQTime(systemTime);
						readyQ.add(unblockedProcess);
						System.out.println("Process " + unblockedProcess.getId() + " enters ready queue at t=" + systemTime);
						event = new Event(EventType.PROCESS_LOADED, systemTime, unblockedProcess.getId(),
						                  (Queue<Process>) new LinkedList<Process>(readyQ),
						                  (Queue<Process>) new LinkedList<Process>(blockedQ));

						events.add(event);
					} else if (unblockedProcess.getStatus() == Status.TERMINATED) {
						System.out.println("Process " + unblockedProcess.getId() + " freed from memory at t=" + systemTime);
						memoryManager.deallocateMemory(unblockedProcess);
						memoryManager.printMemoryMap();
						event = new Event(EventType.PROCESS_UNLOADED, systemTime, unblockedProcess.getId(),
						                  memoryManager.getMemory().toJSON(),
						                  (Queue<Process>) new LinkedList<Process>(blockedQ));

						events.add(event);
					}
				}

				for (int core = 0; core < simulation.noOfParallelProcesses; core++) {
					if (currentTimeSlices[core] == 0) {
						if (readyQ.size() > 0) {
							Process cpuProcess = readyQ.remove();
							cpuProcess.setStatus(Status.RUNNING);
							cpuProcesses[core] = cpuProcess;
						} else
							continue;
					}

					if (cpuProcesses[core].getStatus() == Status.RUNNING) {
						cpuProcesses[core].incrementCpuTime();
						if (cpuProcesses[core].hasTerminated()) {
							cpuProcesses[core].setStatus(Status.TERMINATED);
							cpuProcesses[core].setTurnaroundTime(systemTime - cpuProcesses[core].getArrivalTime());
							cpuProcesses[core].setEnterBlockedQTime(systemTime);
							cpuProcesses[core].setWaitTime(systemTime - cpuProcesses[core].getEnterReadyQTime() -
							                               cpuProcesses[core].getBurstTime());

							blockedQ.add(cpuProcesses[core]);
							System.out.println("Process " + cpuProcesses[core].getId() + " terminates at t=" + systemTime);
							event = new Event(EventType.PROCESS_TERMINATES, systemTime, cpuProcesses[core].getId(),
							                  (Queue<Process>) new LinkedList<Process>(readyQ),
							                  (Queue<Process>) new LinkedList<Process>(blockedQ));

							events.add(event);

							currentTimeSlices[core] = 0;
							continue;
						}
					}

					if (currentTimeSlices[core] == simulation.timeSlice - 1) {
						readyQ.add(cpuProcesses[core]);
						currentTimeSlices[core] = 0;
						continue;
					}

					currentTimeSlices[core]++;
				}

				systemTime++;
			}

			simulation.saveSimulationHistory(events);

			int index = 0;
			for (Event event1 : events) {
				event1.setId(index);
				index++;
			}

			int noOfProcesses = 0;
			double averageTurnaroundTime = 0.0;
			double averageWaitTime = 0.0;
			double averageResponseTime = 0.0;
			for (Process process : processes)
				if (process.getCpuTime() > 0) {
					noOfProcesses++;
					averageTurnaroundTime += process.getTurnaroundTime();
					averageWaitTime += process.getWaitTime();
					averageResponseTime += process.getResponseTime();
				}

			averageTurnaroundTime /= noOfProcesses;
			averageWaitTime /= noOfProcesses;
			averageResponseTime /= (noOfProcesses * 2);
			System.out.printf("Average turnaround time: %.2f ms\n", averageTurnaroundTime);
			System.out.printf("Average wait time: %.2f ms\n", averageWaitTime);
			System.out.printf("Average response time: %.2f ms\n", averageResponseTime);
			System.out.printf("Number of pages not fitted in memory(%s): %d\n", simulation.pageFitting, simulation.notFitted);
		}

		catch (Exception e) {
			e.printStackTrace();
		}

	}

}
