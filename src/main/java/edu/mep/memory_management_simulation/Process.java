package edu.mep.memory_management_simulation;

public class Process {
	private int id;
	private long size;
	private int arrivalTime;
	private int burstTime;
	private int cpuTime;
	private int waitingTime;
	private int deviceTime;
	private int turnaroundTime;
	private int enterReadyQTime;
	private int waitTime;
	private int enterBlockedQTime;
	private int responseTime;
	private Status status;

	public Process(int id, long size, int arrivalTime, int burstTime) {
		this.id = id;
		this.size = size;
		this.arrivalTime = arrivalTime;
		this.burstTime = burstTime;
		this.cpuTime = 0;
		this.waitingTime = (int) Math.ceil(size / 500000.0);
		this.deviceTime = 0;
		this.turnaroundTime = 0;
		this.waitTime = 0;
		this.responseTime = 0;
		this.status = Status.NEW;
	}

	public void incrementDeviceTime() {
		this.deviceTime++;
	}

	public void incrementCpuTime() {
		this.cpuTime++;
	}

	public void resetDeviceTime() {
		this.deviceTime = 0;
	}

	public boolean isWaiting() {
		return this.waitingTime == this.deviceTime;
	}

	public boolean hasTerminated() {
		return this.burstTime == this.cpuTime;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getSize() {
		return this.size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public int getArrivalTime() {
		return this.arrivalTime;
	}

	public void setArrivalTime(int arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public int getBurstTime() {
		return this.burstTime;
	}

	public void setBurstTime(int burstTime) {
		this.burstTime = burstTime;
	}

	public int getCpuTime() {
		return cpuTime;
	}

	public void setCpuTime(int cpuTime) {
		this.cpuTime = cpuTime;
	}

	public int getWaitingTime() {
		return waitingTime;
	}

	public void setWaitingTime(int waitingTime) {
		this.waitingTime = waitingTime;
	}

	public int getDeviceTime() {
		return deviceTime;
	}

	public void setDeviceTime(int deviceTime) {
		this.deviceTime = deviceTime;
	}

	public int getTurnaroundTime() {
		return turnaroundTime;
	}

	public void setTurnaroundTime(int turnaroundTime) {
		this.turnaroundTime = turnaroundTime;
	}

	public int getEnterReadyQTime() {
		return enterReadyQTime;
	}

	public void setEnterReadyQTime(int enterReadyQTime) {
		this.enterReadyQTime = enterReadyQTime;
	}

	public int getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(int waitTime) {
		this.waitTime = waitTime;
	}

	public int getEnterBlockedQTime() {
		return enterBlockedQTime;
	}

	public void setEnterBlockedQTime(int enterBlockedQTime) {
		this.enterBlockedQTime = enterBlockedQTime;
	}

	public int getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(int responseTime) {
		this.responseTime = responseTime;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Status getStatus() {
		return this.status;
	}

	public String toString() {
		return "Process [id=" + this.id + ", size=" + this.size + ", arrivalTime=" + this.arrivalTime + ", burstTime=" + this.burstTime + ", status=" + this.status + "]";
	}

}
