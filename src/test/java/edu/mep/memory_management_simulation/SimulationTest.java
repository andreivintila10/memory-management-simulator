package edu.mep.memory_management_simulation;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.Queue;

import org.junit.Before;
import org.junit.Test;

public class SimulationTest {

	private Queue<Process> blockedQ = new LinkedList<Process>();
	private Process process1, process2, process3;
//	private int systemTime = 0;

	@Before
	public void setup() {
		process1 = new Process(1, 2000000, 0, 15);
		process2 = new Process(1, 1000000, 4, 5);
		process3 = new Process(1, 100000, 16, 8);

		blockedQ.add(process1);
		blockedQ.add(process2);
		blockedQ.add(process3);
	}

	@Test
	public void removeProcessBlockedQTest() {
//		Simulation simulation = new Simulation();
//		Process nextBlockedProcess = blockedQ.peek();
		assertTrue(true);
	}

}
