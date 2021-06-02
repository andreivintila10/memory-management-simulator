package edu.mep.memory_management_simulation;

import java.util.Queue;

public class Event {
	private int id;
	private EventType eventType;
	private int systemTime;
	private int processID;
	private String memoryMap;
	private Queue<Process> readyQ;
	private Queue<Process> blockedQ;

	// For PROCESS_ARRIVES and PROCESS_UNLOADED from memory
	public Event(EventType eventType, int systemTime, int processID, String memoryMap, Queue<Process> blockedQ) {
		this.eventType = eventType;
		this.systemTime = systemTime;
		this.memoryMap = memoryMap;
		this.processID = processID;
		this.blockedQ = blockedQ;
	}

	// For PROCESS_TERMINATES, PROCESS_LOADED
	public Event(EventType eventType, int systemTime, int processID, Queue<Process> readyQ, Queue<Process> blockedQ) {
		this.eventType = eventType;
		this.systemTime = systemTime;
		this.processID = processID;
		this.readyQ = readyQ;
		this.blockedQ = blockedQ;
	}

	// For PROCESS_NOT_FIT
	public Event(EventType eventType, int systemTime, int processID) {
		this.eventType = eventType;
		this.systemTime = systemTime;
		this.processID = processID;
	}

	public String toString() {
		return "";
	}

	public String queueToJSON(Queue<Process> queue) {
		String jsonString = "[";
		while (queue.size() > 1) {
			jsonString += queue.remove().getId() + ",";
		}

		if (queue.size() > 0)
			jsonString += queue.remove().getId();

		jsonString += "]";

		return jsonString;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String toJSON() {
		switch (eventType) {
			case PROCESS_ARRIVES:
			case PROCESS_UNLOADED:
				return "{\"id\":" + this.id + ",\"event\":\"" + this.eventType + "\",\"systemTime\":" + this.systemTime + ",\"processID\":" + this.processID + ",\"memoryMap\":" + this.memoryMap + ",\"blockedQ\":" + queueToJSON(this.blockedQ) + "}";
			case PROCESS_TERMINATES:
			case PROCESS_LOADED:
				return "{\"id\":" + this.id + ",\"event\":\"" + this.eventType + "\",\"systemTime\":" + this.systemTime + ",\"processID\":" + this.processID + ",\"readyQ\":" + queueToJSON(this.readyQ) + ",\"blockedQ\":" + queueToJSON(this.blockedQ) + "}";
			case PROCESS_NOT_FIT:
				return "{\"id\":" + this.id + ",\"event\":\"" + this.eventType + "\",\"systemTime\":" + this.systemTime + ",\"processID\":" + this.processID + "}";
			default:
				return "{\"id\":" + this.id + ",\"event\":\"" + this.eventType + "\",\"systemTime\":" + this.systemTime + ",\"processID\":" + this.processID + ",\"memoryMap\":" + this.memoryMap + ",\"readyQ\":" + queueToJSON(this.readyQ) + ",\"blockedQ\":" + queueToJSON(this.blockedQ) + "}";
		}
	}
}
