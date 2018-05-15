package call_center;

import model.Call;
import model.Employee;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class Dispatcher extends Thread {
	private static final Logger LOGGER = LogManager.getLogger("Dispatcher");
	/**
	 * The employee that will be polled will have the lower priority in the queue
	 */
	private PriorityBlockingQueue<Employee> employees;
	/**
	 * Acts as a normal queue (FIFO)
	 */
	private LinkedBlockingQueue<Call> calls;

	public Dispatcher() {
		this.employees = new PriorityBlockingQueue<>();
		this.calls = new LinkedBlockingQueue<>();
	}

	/**
	 * Adds a call to the calls queue
	 *
	 * @param call Call to be stored
	 */
	private void receiveCall(Call call) {
		try {
			LOGGER.info("Call #" + call.getId() + " received");
			this.calls.put(call);
		} catch (InterruptedException ex) {
			LOGGER.error(ex.getMessage());
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * Adds an employee to the employees queue
	 *
	 * @param employee Employee to be stored
	 */
	void addEmployee(Employee employee) {
		this.employees.add(employee);
	}

	/**
	 * After an employee has ended a call, he should be re-added to the employees queue
	 * The same employee couldn't be added, then create a "new" employee with the same
	 * information of the given employee
	 *
	 * @param employee Employee to be re-added to the employees queue
	 */
	public void addAvailableEmployee(Employee employee) {
		Integer currentId = employee.getEmployeeId();
		Employee.EmployeeType type = employee.getType();
		this.employees.add(new Employee(currentId, this, type));

	}

	LinkedBlockingQueue<Call> getCalls() {
		return calls;
	}

	PriorityBlockingQueue<Employee> getEmployees() {
		return employees;
	}

	/**
	 * Adds a list of calls to the calls queue
	 *
	 * @param calls {@link List} of {@link Call} to be added to the calls queue
	 */
	void addCalls(List<Call> calls) {
		for (Call call : calls) {
			this.receiveCall(call);
		}
	}

	/**
	 * Add a list of employees to the employees queue
	 *
	 * @param employees {@link List} of {@link Employee} to be added to the employees queue
	 */
	void addEmployees(List<Employee> employees) {
		this.employees.addAll(employees);
	}

	/**
	 * Thread run method, it runs until there is no calls in the calls queue, i.e. all the calls
	 * were processed
	 */
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
