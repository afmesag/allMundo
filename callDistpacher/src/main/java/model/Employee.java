package model;

import call_center.Dispatcher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicInteger;

public class Employee extends Thread implements Comparable<Employee> {
	private static final AtomicInteger ID_GENERATOR = new AtomicInteger(1);
	private static final Logger LOGGER = LogManager.getLogger("Employee");

	private Integer employeeId;
	private Call currentCall;
	private EmployeeType type;
	private Dispatcher dispatcher;

	Employee(EmployeeType type) {
		this.employeeId = ID_GENERATOR.getAndIncrement();
		this.type = type;
	}

	public Employee(Dispatcher dispatcher, EmployeeType type) {
		if (this.employeeId == null)
			this.employeeId = ID_GENERATOR.getAndIncrement();
		this.type = type;
		this.dispatcher = dispatcher;
	}

	public Employee(Integer employeeId, Dispatcher dispatcher, EmployeeType type) {
		this(dispatcher, type);
		this.employeeId = employeeId;
	}

	public static void resetIdSequence() {
		ID_GENERATOR.set(1);
	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	Call getCall() {
		return currentCall;
	}

	void setCall(Call call) {
		this.currentCall = call;
	}

	public EmployeeType getType() {
		return type;
	}

	void setType(EmployeeType type) {
		this.type = type;
	}

	public void attendCall(Call call) {
		this.currentCall = call;
		this.start();
	}

	public int compareTo(Employee employee) {
		if (this.type.priority > employee.getType().getPriority())
			return 1;
		else if (this.type.priority < employee.getType().getPriority())
			return -1;
		else return 0;
	}

	@Override
	public void run() {
		Integer callDuration = this.currentCall.getDuration();
		Integer callId = this.currentCall.getId();
		LOGGER.info("Call #" + callId + " has started | Duration: " +
				callDuration + " | Gotten by a " + this.type.description + "#" + this.employeeId);
		try {
			Thread.sleep(callDuration * 1000L);
			this.currentCall = null;
			LOGGER.info("Call #" + callId + " has ended | " + this.type.description + "#" +
					employeeId + " is available");
			this.dispatcher.addAvailableEmployee(this);
		} catch (InterruptedException ex) {
			LOGGER.error("Error in call #" + callId);
			LOGGER.error(ex.getMessage());
			Thread.currentThread().interrupt();
		}
	}

	public enum EmployeeType {
		OPERATOR(1, "Operator"),
		SUPERVISOR(2, "Supervisor"),
		DIRECTOR(3, "Director");
		private Integer priority;
		private String description;

		EmployeeType(Integer priority, String description) {
			this.priority = priority;
			this.description = description;
		}

		public Integer getPriority() {
			return priority;
		}

		public String getDescription() {
			return description;
		}
	}
}
