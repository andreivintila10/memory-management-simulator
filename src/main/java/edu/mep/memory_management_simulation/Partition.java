package edu.mep.memory_management_simulation;

public class Partition {
	private int index;
	private long base;
	private long limit;
	private int currentProcess;
	private boolean inUse;

	public Partition(int index, long base, long limit, int currentProcess, boolean inUse) {
		this.index = index;
		this.base = base;
		this.limit = limit;
		this.currentProcess = currentProcess;
		this.inUse = inUse;
	}

	public long getSize() {
		return this.limit - this.base + 1;
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public long getBase() {
		return base;
	}

	public void setBase(long base) {
		this.base = base;
	}

	public long getLimit() {
		return limit;
	}

	public void setLimit(long limit) {
		this.limit = limit;
	}

	public int getCurrentProcess() {
		return currentProcess;
	}

	public void setCurrentProcess(int currentProcess) {
		this.currentProcess = currentProcess;
	}

	public boolean isInUse() {
		return inUse;
	}

	public void setInUse(boolean inUse) {
		this.inUse = inUse;
	}

	@Override
	public String toString() {
		return "Partition [index=" + index + ", base=" + base + ", limit=" + limit +
		       ", currentProcess=" + currentProcess + ", inUse=" + inUse + "]";
	}

}
