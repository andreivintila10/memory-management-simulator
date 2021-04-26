package edu.mep.memory_management_simulation;

public class Process {
	private int id;
	private int size;
	private int startTime;
	private int duration;
	private int turnaroundTime;

	public Process(int id, int size, int startTime, int duration) {
		this.id = id;
		this.size = size;
		this.startTime = startTime;
		this.duration = duration;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSize() {
		return this.size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getStartTime() {
		return this.startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public int getDuration() {
		return this.duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getTurnaroundTime() {
		return turnaroundTime;
	}

	public void setTurnaroundTime(int turnaroundTime) {
		this.turnaroundTime = turnaroundTime;
	}

	public String toString() {
		return "Process [id=" + this.id + ", size=" + this.size + ", startTime=" + this.startTime + ", duration=" + this.duration + "]";
	}

}
