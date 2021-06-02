package edu.mep.memory_management_simulation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class GenerateProcesses {
	private static final int seed = 4215;
	private static final int size_of_MB = 1048576;

	public static void main(String[] args) throws IOException {
		String inputFile = "/home/andrei/eclipse-workspace/memory-management-simulator/src/main/java/edu/mep/memory_management_simulation/input-processes-generated.txt";
		File processesFile = new File(inputFile);
		FileWriter fw = new FileWriter(processesFile);

		Random random = new Random();
		random.setSeed(seed);

		int noOfProcesses = 1000;
//		int noOfProcessesRange = (int) (noOfProcessesOffset * 0.1);

		int arrivalTimeRange = 200000;
//		int arrivalTimeRange = arrivalTimeRangeInit + (int) (arrivalTimeRangeInit * 0.1);

		int burstTimeOffset = 2;
		int burstTimeRange = 5000;
//		int burstTimeRange = burstTimeRangeInit - burstTimeOffset + (int) ((burstTimeRangeInit - burstTimeOffset) * 0.1);

		int sizeOffset = size_of_MB * 2;
		int sizeRange = size_of_MB * 2000;
//		int sizeRange = sizeRangeInit + (int) ((sizeRangeInit - sizeOffset) * 0.01);

//		int noOfProcesses = noOfProcessesOffset + random.nextInt(noOfProcessesRange);
		int arrivalTime, burstTime, size;

		Process process;
		ArrayList<Process> processes = new ArrayList<Process>();
		fw.write(noOfProcesses + "\n");
		System.out.println("Number of processes: " + noOfProcesses);

		int noOfSmallProcesses = 0;
		int noOfMediumProcesses = 0;
		int noOfLargeProcesses = 0;

		int sizeCategoryRange = (int) (sizeRange - sizeOffset) / 3;
		int limitSmallMedium = sizeCategoryRange + sizeOffset;
		int limitMediumLarge = sizeCategoryRange * 2 + sizeOffset;

		for (int index = 0; index < noOfProcesses; index++) {
			size = sizeOffset + random.nextInt(sizeRange);
			arrivalTime = random.nextInt(arrivalTimeRange);
			burstTime = burstTimeOffset + random.nextInt(burstTimeRange);

			process = new Process(index + 1, size, arrivalTime, burstTime);
			processes.add(process);

			// "Small Process" < limitSmallMedium <= "Medium Process" < limitMediumLarge <= "Large Process"
			if (size <= limitSmallMedium)
				noOfSmallProcesses++;
			else if (size > limitMediumLarge)
				noOfLargeProcesses++;
			else
				noOfMediumProcesses++;

			fw.write((index + 1) + "\n");
			fw.write(size + "\n");
			fw.write(arrivalTime + " " + burstTime + "\n");
			System.out.println(process);
		}
		fw.close();

		System.out.println();
		System.out.printf("Numarul de procese mici [%dMB, %dMB]: %d\n", (int) (sizeOffset / size_of_MB), (int) ((limitSmallMedium - 1) / size_of_MB), noOfSmallProcesses);
		System.out.printf("Numarul de procese medii [%dMB, %dMB]: %d\n", (int) (limitSmallMedium / size_of_MB), (int) ((limitMediumLarge - 1) / size_of_MB), noOfMediumProcesses);
		System.out.printf("Numarul de procese mari [%dMB, %dMB]: %d\n", (int) (limitMediumLarge / size_of_MB), (int) ((sizeRange - 1) / size_of_MB), noOfLargeProcesses);
	}
}
