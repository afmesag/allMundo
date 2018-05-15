package call_center;

import model.Call;
import model.Employee;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class Dispatcher extends Thread {
	private static final Logger LOGGER = LogManager.getLogger("Dispatcher");
	private PriorityBlockingQueue<Employee> employees;
	private BlockingQueue<Call> calls;

	public Dispatcher() {
		this.employees = new PriorityBlockingQueue<>();
		this.calls = new LinkedBlockingQueue<>();
	}

	private void receiveCall(Call call) {
		try {
			LOGGER.info("Call #" + call.getId() + " received");
			this.calls.put(call);
		} catch (InterruptedException ex) {
			LOGGER.error(ex.getMessage());
			Thread.currentThread().interrupt();
		}
	}

	void addEmployee(Employee employee) {
		this.employees.add(employee);
	}

	public void addAvailableEmployee(Employee employee) {
		Integer currentId = employee.getEmployeeId();
		Employee.EmployeeType type = employee.getType();
		this.employees.add(new Employee(currentId, this, type));

	}

	BlockingQueue<Call> getCalls() {
		return calls;
	}

	void addCalls(List<Call> calls) {
		for (Call call : calls) {
			this.receiveCall(call);
		}
	}

	PriorityBlockingQueue<Employee> getEmployees() {
		return employees;
	}

	void addEmployees(List<Employee> employees) {
		this.employees.addAll(employees);
	}

	@Override
	public void run() {
		try {
			while (!calls.isEmpty()) {
				Call currentCall = this.calls.take();
				Employee currentEmployee = this.employees.take();
				currentEmployee.attendCall(currentCall);
			}
		} catch (InterruptedException ex) {
			LOGGER.error(ex.getMessage());
			Thread.currentThread().interrupt();
		}
	}
}
