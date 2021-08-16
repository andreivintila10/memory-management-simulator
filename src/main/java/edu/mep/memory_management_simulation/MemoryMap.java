package edu.mep.memory_management_simulation;

import java.util.ArrayList;
import java.util.Random;

public class MemoryMap {
	private ArrayList<Partition> memorySpace;

	public MemoryMap(ArrayList<Partition> memorySpace) {
		this.memorySpace = memorySpace;
	}

	public int FirstFit(long requiredSize) {
		for (Partition partition : memorySpace)
			if (!partition.isInUse() && partition.getSize() >= requiredSize)
				return partition.getIndex();

		return -1;
	}

	public int BestFit(long requiredSize) {
		long minSize = 16670486529L;
		int minSizeIndex = -1;
		long currentPartitionSize;
		for (Partition partition : memorySpace) {
			currentPartitionSize = partition.getSize();
			if (!partition.isInUse() && currentPartitionSize >= requiredSize && currentPartitionSize < minSize) {
				minSize = currentPartitionSize;
				minSizeIndex = partition.getIndex();
			}
		}

		return minSizeIndex;
	}

	public int WorstFit(long requiredSize) {
		long maxSize = 0;
		int maxSizeIndex = -1;
		long currentPartitionSize;
		for (Partition partition : memorySpace) {
			currentPartitionSize = partition.getSize();
			if (!partition.isInUse() && currentPartitionSize >= requiredSize && currentPartitionSize > maxSize) {
				maxSize = currentPartitionSize;
				maxSizeIndex = partition.getIndex();
			}
		}

		return maxSizeIndex;
	}

	public boolean allocateMemory(int processId, long requiredSize, int pageSize, PageFitting pageFitting) {
		int partitionIndex;

		switch (pageFitting) {
			case FIRST:
				partitionIndex = FirstFit(requiredSize);
				break;

			case BEST:
				partitionIndex = BestFit(requiredSize);
				break;

			case WORST:
				partitionIndex = WorstFit(requiredSize);
				break;

			default:
				partitionIndex = FirstFit(requiredSize);
				break;
		}

		if (partitionIndex >= 0) {
			Partition partition = memorySpace.get(partitionIndex);
			long partitionSize = partition.getSize();
			if (partitionSize - requiredSize < pageSize) {
				partition.setCurrentProcess(processId);
				partition.setInUse(true);
			} else {
				long newPartitionLimit = partition.getLimit();

				long pagesRequired = (int) Math.ceil((double) requiredSize / pageSize);
				long partitionLimit = partition.getBase() + pagesRequired * pageSize - 1;

				partition.setLimit(partitionLimit);
				partition.setCurrentProcess(processId);
				partition.setInUse(true);

				int newPartitionIndex = partitionIndex + 1;
				long newPartitionBase = partitionLimit + 1;
				int newPartitionCurrentProcess = 0;
				boolean newPartitionInUse = false;
				Partition newPartition = new Partition(newPartitionIndex, newPartitionBase, newPartitionLimit,
				                                       newPartitionCurrentProcess, newPartitionInUse);
				for (int index = newPartitionIndex; index < memorySpace.size(); index++) {
					Partition tempPartition = memorySpace.get(index);
					tempPartition.setIndex(index + 1);
					memorySpace.set(index, tempPartition);
				}

				memorySpace.set(partitionIndex, partition);
				memorySpace.add(newPartitionIndex, newPartition);
			}

			return true;
		}
		return false;
	}

	public int randomPageReplacement(Process[] runningProcesses, Random randomPage) {
		int noOfPartitions = memorySpace.size();
		Partition currentPartition;
		int randomValue, currentProcess;
		boolean ok;

		do {
			ok = true;
			randomValue = randomPage.nextInt(noOfPartitions);
			currentPartition = memorySpace.get(randomValue);
			currentProcess = currentPartition.getCurrentProcess();

			for (int index = 0; index < runningProcesses.length; index++)
				if (runningProcesses[index].getId() == currentProcess) {
					ok = false;
					break;
				}

		} while (!currentPartition.isInUse() || !ok);

		return currentProcess;
	}

	public void deallocateMemory(int processId) {
		int partitionIndex = 0;
		for (Partition partition : memorySpace)
			if (partition.getCurrentProcess() == processId) {
				partitionIndex = partition.getIndex();
				break;
			}

		Partition currentPartition = memorySpace.get(partitionIndex);
		currentPartition.setCurrentProcess(0);
		currentPartition.setInUse(false);

		if (partitionIndex > 0) {
			Partition prevPartition = memorySpace.get(partitionIndex - 1);
			if (!prevPartition.isInUse()) {
				prevPartition.setLimit(currentPartition.getLimit());
				for (int index = partitionIndex; index < memorySpace.size(); index++) {
					Partition tempPartition = memorySpace.get(index);
					tempPartition.setIndex(index - 1);
					memorySpace.set(index, tempPartition);
				}

				memorySpace.remove(partitionIndex);
				partitionIndex--;
				currentPartition = prevPartition;
			}
		}

		if (partitionIndex < memorySpace.size() - 1) {
			Partition nextPartition = memorySpace.get(partitionIndex + 1);
			if (!nextPartition.isInUse()) {
				nextPartition.setBase(currentPartition.getBase());
				for (int index = partitionIndex; index < memorySpace.size(); index++) {
					Partition tempPartition = memorySpace.get(index);
					tempPartition.setIndex(index - 1);
					memorySpace.set(index, tempPartition);
				}

				memorySpace.remove(partitionIndex);
			}
		}
	}

	public void defragmentMemory() {
		Partition currentPartition, tempPartition = null;
		long currentPartitionSize;
		int noOfPartitions = memorySpace.size();
		int index = 0;

		while (index < memorySpace.size()) {
			currentPartition = memorySpace.get(index);
			if (!currentPartition.isInUse() && index < noOfPartitions - 1) {
				currentPartitionSize = currentPartition.getSize();

				for (int index2 = index + 1; index2 < noOfPartitions; index2++) {
					tempPartition = memorySpace.get(index2);
					tempPartition.setIndex(index2 - 1);
					tempPartition.setBase(tempPartition.getBase() - currentPartitionSize);
					tempPartition.setLimit(tempPartition.getLimit() - currentPartitionSize);
					memorySpace.set(index2 - 1, tempPartition);
				}

				currentPartition.setIndex(noOfPartitions - 1);
				currentPartition.setBase(tempPartition.getLimit() + 1);
				currentPartition.setLimit(currentPartition.getBase() + currentPartitionSize - 1);
				memorySpace.set(noOfPartitions - 1, currentPartition);

				Partition secondToLastPartition = memorySpace.get(noOfPartitions - 2);
				Partition lastPartition = memorySpace.get(noOfPartitions - 1);
				if (!secondToLastPartition.isInUse() && !lastPartition.isInUse()) {
					int newIndex = noOfPartitions - 2;
					long newBase = secondToLastPartition.getBase();
					long newLimit = lastPartition.getLimit();

					Partition newPartition = new Partition(newIndex, newBase, newLimit, 0, false);
					memorySpace.set(newIndex, newPartition);
					memorySpace.remove(noOfPartitions - 1);

					noOfPartitions = memorySpace.size();
				}
			}

			index++;
		}
	}

	public boolean searchPartition(int processID) {
		for (Partition partition : memorySpace)
			if (partition.isInUse() && partition.getCurrentProcess() == processID)
				return true;

		return false;
	}

	public ArrayList<Partition> getMemorySpace() {
		return memorySpace;
	}

	public void setMemorySpace(ArrayList<Partition> memorySpace) {
		this.memorySpace = memorySpace;
	}

	public String toJSON() {
		String jsonString = "[";
		for (Partition partition : memorySpace) {
			jsonString += "{\"index\":" + partition.getIndex() + ",\"base\":" + partition.getBase() + ",\"limit\":" +
			              partition.getLimit() + ",\"size\":" + partition.getSize() + ",\"process\":" +
			              partition.getCurrentProcess() + ",\"inUse\":" + partition.isInUse() + "},";
		}

		jsonString = jsonString.substring(0, jsonString.length() - 1);
		jsonString += "]";

		return jsonString;
	}

	public String toString() {
		String output = "";
		for (Partition partition : memorySpace) {
			output += "  " + partition.getBase() + "-" + partition.getLimit();
			if (partition.isInUse())
				output += ": Process " + partition.getCurrentProcess() + "\n";
			else
				output += ": Free frame(s)\n";
		}

		return output + "\n";
	}

}
