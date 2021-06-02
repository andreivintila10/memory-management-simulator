package edu.mep.memory_management_simulation;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ProcessTest {

	private Process process;
	private int id;
	private int size;
	private int arrivalTime;
	private int burstTime;

	@Before
	public void setUp() throws Exception {
		id = 1;
		size = 100;
		arrivalTime = 0;
		burstTime = 150;

		process = new Process(id, size, arrivalTime, burstTime);
	}

	@Test
	public void testToString() {
		String expected = "Process [id=" + id + ", size=" + size + ", arrivalTime=" + arrivalTime + ", burstTime=" + burstTime + ", status=" + process.getStatus() + "]";
		assertEquals(process.toString(), expected);
	}

}
