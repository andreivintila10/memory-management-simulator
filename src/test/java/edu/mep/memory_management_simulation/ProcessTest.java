package edu.mep.memory_management_simulation;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ProcessTest {

	private Process process;
	private int id;
	private int size;
	private int startTime;
	private int duration;

	@Before
	public void setUp() throws Exception {
		id = 1;
		size = 100;
		startTime = 0;
		duration = 150;

		process = new Process(id, size, startTime, duration);
	}

	@Test
	public void testToString() {
		String expected = "Process [id=" + id + ", size=" + size + ", startTime=" + startTime + ", duration=" + duration + "]";
		assertEquals(process.toString(), expected);
	}

}
