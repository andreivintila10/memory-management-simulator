package edu.mep.memory_management_simulation;

import java.util.ArrayList;

public class MemoryMap {
	private ArrayList<Partition> memorySpace;

	public MemoryMap(ArrayList<Partition> memorySpace) {
		this.memorySpace = memorySpace;
	}

	public int findSpace(int requiredSize) {
		for (Partition partition : memorySpace)
			if (!partition.isInUse() && partition.getSize() >= requiredSize)
				return partition.getIndex();

		return -1;
	}

	public boolean allocateMemory(int processId, int requiredSize, int pageSize) {
		int partitionIndex = findSpace(requiredSize);
		if (partitionIndex >= 0) {
			Partition partition = memorySpace.get(partitionIndex);
			int partitionSize = partition.getSize();
			if (partitionSize - requiredSize < pageSize) {
				partition.setCurrentProcess(processId);
				partition.setInUse(true);
			}
			else {
				int newPartitionLimit = partition.getLimit();

				int pagesRequired = (int) Math.ceil((double) requiredSize / pageSize);
				int partitionLimit = partition.getBase() + pagesRequired * pageSize - 1;

				partition.setLimit(partitionLimit);
				partition.setCurrentProcess(processId);
				partition.setInUse(true);

				int newPartitionIndex = partitionIndex + 1;
				int newPartitionBase = partitionLimit + 1;
				int newPartitionCurrentProcess = 0;
				boolean newPartitionInUse = false;
				Partition newPartition = new Partition(newPartitionIndex, newPartitionBase, newPartitionLimit, newPartitionCurrentProcess, newPartitionInUse);
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
	
	public void deallocateMemory(int processId) {
		int partitionIndex = 0;
		for (Partition partition : memorySpace) {
			if (partition.getCurrentProcess() == processId) {
				
				partitionIndex = partition.getIndex();
				break;
			}
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

	public ArrayList<Partition> getMemorySpace() {
		return memorySpace;
	}

	public void setMemorySpace(ArrayList<Partition> memorySpace) {
		this.memorySpace = memorySpace;
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