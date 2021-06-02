package edu.mep.memory_management_simulation;

import java.util.ArrayList;
import java.util.Random;

public class MemoryManager {
	private long memorySize;
	private int pageSize;
	private MemoryMap memoryMap;

	public MemoryManager(long memorySize, int pageSize) {
		this.memorySize = memorySize;
		this.pageSize = pageSize;
		ArrayList<Partition> memorySpace = new ArrayList<Partition>();
		memorySpace.add(new Partition(0, 0, this.memorySize - 1, 0, false));
		memoryMap = new MemoryMap(memorySpace);
	}

	public boolean allocateMemory(Process process, PageFitting pageFitting) {
		return memoryMap.allocateMemory(process.getId(), process.getSize(), this.pageSize, pageFitting);
	}

	public void deallocateMemory(Process process) {
		memoryMap.deallocateMemory(process.getId());
	}

	public int pageReplacement(Process[] runningProcesses, Random randomPage) {
		return memoryMap.randomPageReplacement(runningProcesses, randomPage);
	}

	public void defragmentMemory() {
		memoryMap.defragmentMemory();
	}

	public boolean searchMemory(int processID) {
		return memoryMap.searchPartition(processID);
	}

	public void printMemoryMap() {
		System.out.println("Memory Map");
		System.out.print(memoryMap);
	}

	public MemoryMap getMemory() {
		return memoryMap;
	}

	public void setMemory(MemoryMap memory) {
		this.memoryMap = memory;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

}