package edu.mep.memory_management_simulation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Simulation {
	private int memorySize = 3000;
	private int pageSize = 100;

	public ArrayList<Process> getProcesses() throws FileNotFoundException {
		String inputFile = "/home/andrei/eclipse-workspace/memory-management-simulator/src/main/java/edu/mep/memory_management_simulation/input-processes.txt";

		ArrayList<Process> processes = new ArrayList<Process>();
		Process process = null;
		int id, size, startTime, duration;

		File processesFile = new File(inputFile);
		Scanner sc = new Scanner(processesFile);
		int numberOfProcesses = sc.nextInt();
		for (int index = 0; index < numberOfProcesses; index++) {
			id = sc.nextInt();
			size = sc.nextInt();
			startTime = sc.nextInt();
			duration = sc.nextInt();
			process = new Process(id, size, startTime, duration);

			processes.add(process);
		}

		sc.close();

		return processes;
	}

	public static void main(String[] args) {
		Simulation simulation = new Simulation();
		Queue <Process> inputQ = new LinkedList<Process>();
		MemoryManager memoryManager = new MemoryManager(simulation.memorySize, simulation.pageSize);

		try {
			ArrayList<Process> processes = simulation.getProcesses();
			for (Process process : processes)
				System.out.println(process);

			System.out.println();

			int virtualClockTime = 0;
			while (virtualClockTime < 100000) {
				for (Process process : processes) {
					int exitTime = process.getStartTime() + process.getDuration();
					if (exitTime == virtualClockTime) {
						process.setTurnaroundTime(exitTime - process.getStartTime());
						System.out.println("Process " + process.getId() + " completes at t=" + virtualClockTime);
						memoryManager.deallocateMemory(process);
						memoryManager.printMemoryMap();
					}
				}

				for(Process process : processes) {
					if (process.getStartTime() == virtualClockTime) {
						inputQ.add(process);
						System.out.println("Process " + process.getId() + " arrives at t=" + virtualClockTime);
					}
				}

				Process removedProcess;
				while (inputQ.size() > 0) {
					removedProcess = inputQ.remove();
					memoryManager.allocateMemory(removedProcess);
					memoryManager.printMemoryMap();
				}

				virtualClockTime++;
			}
			
			double averageTurnaroundTime = 0.0;
			for (Process process : processes) {
				averageTurnaroundTime += process.getTurnaroundTime();
			}
			averageTurnaroundTime /= processes.size();
			System.out.println("Average turnaround time: " + averageTurnaroundTime);
		}

		catch (Exception e) {
			e.printStackTrace();
		}

	}

}