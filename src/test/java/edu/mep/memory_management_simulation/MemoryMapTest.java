package edu.mep.memory_management_simulation;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

public class MemoryMapTest {

	int pageSize = 100;
	
	Process process1 = new Process(2, 200, 100, 500);
	Process process2 = new Process(3, 250, 100, 500);
	Process process3 = new Process(4, 1000, 100, 500);
	Process process4 = new Process(5, 1001, 100, 500);

	Partition partition1 = new Partition(0, 0, 999, 0, false);
	Partition partition2 = new Partition(1, 1000, 1499, 1, true);

	MemoryMap memoryMap;

	@Before
	public void setUp() {
		ArrayList<Partition> partitions = new ArrayList<Partition>();
		Collections.addAll(partitions, partition1, partition2);
		memoryMap = new MemoryMap(partitions);
	}

	@Test
	public void testMemoryAllocation1() {
		boolean check1 = memoryMap.allocateMemory(process1.getId(), process1.getSize(), pageSize);
		assertEquals(check1, true);
		assertEquals(memoryMap.getMemorySpace().size(), 3);
	}

	@Test
	public void testMemoryAllocation2() {
		boolean check2 = memoryMap.allocateMemory(process2.getId(), process2.getSize(), pageSize);
		assertEquals(check2, true);
		assertEquals(memoryMap.getMemorySpace().size(), 3);
	}

	@Test
	public void testMemoryAllocation3() {
		boolean check3 = memoryMap.allocateMemory(process3.getId(), process3.getSize(), pageSize);
		assertEquals(check3, true);
		assertEquals(memoryMap.getMemorySpace().size(), 2);
	}

	@Test
	public void testMemoryAllocation4() {
		boolean check4 = memoryMap.allocateMemory(process4.getId(), process4.getSize(), pageSize);
		assertEquals(check4, false);
		assertEquals(memoryMap.getMemorySpace().size(), 2);
	}
	
	@Test
	public void testMemoryDeallocation1() {
		memoryMap.deallocateMemory(1);
		assertEquals(memoryMap.getMemorySpace().size(), 1);
	}

}
