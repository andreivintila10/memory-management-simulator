package edu.mep.memory_management_simulation;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class MemoryMapTest {

	int pageSize = 100;

	ArrayList<Partition> partitions;
	Process process1;
	Process process2;
	Process process3;
	Process process4;

	Partition partition1;
	Partition partition2;
	Partition partition3;

	MemoryMap memoryMap;

	@Before
	public void setUp() {
		process1 = new Process(2, 200, 100, 500);
		process2 = new Process(3, 250, 100, 500);
		process3 = new Process(4, 1000, 100, 500);
		process4 = new Process(5, 1001, 100, 500);

		partition1 = new Partition(0, 0, 999, 0, false);
		partition2 = new Partition(1, 1000, 1499, 1, true);

		partitions = new ArrayList<Partition>();
		partitions.add(partition1);
		partitions.add(partition2);
		memoryMap = new MemoryMap(partitions);
	}

	@Test
	public void testMemoryAllocation1() {
		boolean check1 = memoryMap.allocateMemory(process1.getId(), process1.getSize(), pageSize, PageFitting.FIRST);
		assertEquals(check1, true);
		assertEquals(memoryMap.getMemorySpace().size(), 3);
	}

	@Test
	public void testMemoryAllocation2() {
		boolean check2 = memoryMap.allocateMemory(process2.getId(), process2.getSize(), pageSize, PageFitting.FIRST);
		assertEquals(check2, true);
		assertEquals(memoryMap.getMemorySpace().size(), 3);
	}

	@Test
	public void testMemoryAllocation3() {
		boolean check3 = memoryMap.allocateMemory(process3.getId(), process3.getSize(), pageSize, PageFitting.FIRST);
		assertEquals(check3, true);
		assertEquals(memoryMap.getMemorySpace().size(), 2);
	}

	@Test
	public void testMemoryAllocation4() {
		boolean check4 = memoryMap.allocateMemory(process4.getId(), process4.getSize(), pageSize, PageFitting.FIRST);
		assertEquals(check4, false);
		assertEquals(memoryMap.getMemorySpace().size(), 2);
	}

	@Test
	public void testMemoryDeallocation1() {
		memoryMap.deallocateMemory(1);
		assertEquals(memoryMap.getMemorySpace().size(), 1);
	}

	@Test
	public void testMemoryDefragmentation() {
		partition3 = new Partition(2, 1500, 2999, 0, false);
		partitions.add(partition3);
		memoryMap.setMemorySpace(partitions);

		memoryMap.defragmentMemory();

		assertEquals(memoryMap.getMemorySpace().size(), 2);

		assertEquals(memoryMap.getMemorySpace().get(0).getIndex(), 0);
		assertEquals(memoryMap.getMemorySpace().get(0).getBase(), 0);
		assertEquals(memoryMap.getMemorySpace().get(0).getLimit(), 499);
		assertEquals(memoryMap.getMemorySpace().get(0).getSize(), 500);
		assertTrue(memoryMap.getMemorySpace().get(0).isInUse());

		assertEquals(memoryMap.getMemorySpace().get(1).getIndex(), 1);
		assertEquals(memoryMap.getMemorySpace().get(1).getBase(), 500);
		assertEquals(memoryMap.getMemorySpace().get(1).getLimit(), 2999);
		assertEquals(memoryMap.getMemorySpace().get(1).getSize(), 2500);
		assertFalse(memoryMap.getMemorySpace().get(1).isInUse());
	}

}
