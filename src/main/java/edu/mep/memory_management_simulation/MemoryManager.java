package edu.mep.memory_management_simulation;

import java.util.ArrayList;

public class MemoryManager {
	private int memorySize;
	private int pageSize;
	private MemoryMap memoryMap;

	public MemoryManager(int memorySize, int pageSize) {
		this.memorySize = memorySize;
		this.pageSize = pageSize;
		ArrayList<Partition> memorySpace = new ArrayList<Partition>();
		memorySpace.add(new Partition(0, 0, this.memorySize - 1, 0, false));
		memoryMap = new MemoryMap(memorySpace);
	}

	public boolean allocateMemory(Process process) {
		return memoryMap.allocateMemory(process.getId(), process.getSize(), this.pageSize);
	}

	public void deallocateMemory(Process process) {
		memoryMap.deallocateMemory(process.getId());
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