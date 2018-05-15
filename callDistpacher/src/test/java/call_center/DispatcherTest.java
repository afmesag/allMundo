package call_center;

import model.Call;
import model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;


class DispatcherTest {

	private Dispatcher dispatcher;

	@BeforeEach
	void setUp() {
		dispatcher = new Dispatcher();
		Employee.resetIdSequence();
		Call.resetIdSequence();

	}

	private void populateEmployees(Integer numEmployees) {
		List<Employee> employees = new ArrayList<>();
		for (int i = 0; i < numEmployees; i++) {
			Integer randomType = new Random().nextInt(3);
			Employee.EmployeeType type;
			switch (randomType) {
				case 0:
					type = Employee.EmployeeType.OPERATOR;
					break;
				case 1:
					type = Employee.EmployeeType.SUPERVISOR;
					break;
				case 2:
					type = Employee.EmployeeType.DIRECTOR;
					break;
				default:
					type = Employee.EmployeeType.OPERATOR;
			}
			employees.add(new Employee(dispatcher, type));
		}
		dispatcher.addEmployees(employees);
	}

	private void populateCalls(Integer numCalls) {
		List<Call> calls = new ArrayList<>();
		for (int i = 0; i < numCalls; i++) {
			calls.add(new Call());
		}
		dispatcher.addCalls(calls);
	}

	@Test
	void test10Calls() {
		populateEmployees(5);
		populateCalls(10);
		dispatcher.start();
		assertAll(
				() -> {
					try {
						// For end the 10 calls for 5 employees, the minimum time to end all the calls should be
						// 10 seconds, then after 5 seconds there should be waiting calls in the queue
						Thread.sleep(5 * 1000L);
						assertTrue(!dispatcher.getCalls().isEmpty());
					} catch (InterruptedException e) {
						fail(e.getMessage());
						Thread.currentThread().interrupt();
					}
				},
				() -> {
					try {
						// To end all the calls, the maximum time should be 20 seconds, then
						// after 21 seconds more there shouldn't be waiting calls and all the
						// employees should be free
						Thread.sleep(21 * 1000L);
						assertTrue(dispatcher.getCalls().isEmpty());
					} catch (InterruptedException e) {
						fail(e.getMessage());
						Thread.currentThread().interrupt();
					}
				},
				() -> assertEquals(5, dispatcher.getEmployees().size()));

	}

	@Test
	void test20Calls() {
		populateEmployees(5);
		populateCalls(20);
		dispatcher.start();
		assertAll(
				() -> {
					try {
						// For end the 20 calls for 5 employees, the minimum time to end all the
						// calls should be 20 seconds, then after 10 seconds there should be
						// waiting calls in the queue
						Thread.sleep(10 * 1000L);
						assertTrue(!dispatcher.getCalls().isEmpty());
					} catch (InterruptedException e) {
						fail(e.getMessage());
						Thread.currentThread().interrupt();
					}
				},
				() -> {
					try {
						// To end all the calls, the maximum time should be 40 seconds, then
						// after 31 seconds more there shouldn't be waiting calls and all the
						// employees should be free
						Thread.sleep(31 * 1000L);
						assertTrue(dispatcher.getCalls().isEmpty());
					} catch (InterruptedException e) {
						fail(e.getMessage());
						Thread.currentThread().interrupt();
					}
				},
				() -> assertEquals(5, dispatcher.getEmployees().size()));

	}


	@Test
	void addEmployee() {
		populateEmployees(9);
		dispatcher.addEmployee(new Employee(dispatcher, Employee.EmployeeType.OPERATOR));
		assertEquals(10, dispatcher.getEmployees().size());
	}

	@Test
	void addAvailableEmployee() {
		populateEmployees(5);
		assertAll(
				() -> assertEquals(5, dispatcher.getEmployees().size()),
				() -> {
					dispatcher.addAvailableEmployee(new Employee(dispatcher,
							Employee.EmployeeType.OPERATOR));
					dispatcher.addAvailableEmployee(new Employee(dispatcher,
							Employee.EmployeeType.DIRECTOR));
					assertEquals(7, dispatcher.getEmployees().size());
				});
	}

	@Test
	void getCalls() {
		populateCalls(5);
		assertEquals(5, dispatcher.getCalls().size());
	}

	@Test
	void addCalls() {
		populateCalls(9);
		List<Call> calls = new ArrayList<>();
		for (int i = 0; i < 6; i++)
			calls.add(new Call());
		dispatcher.addCalls(calls);
		assertEquals(15, dispatcher.getCalls().size());
	}

	@Test
	void getEmployees() {
		populateEmployees(10);
		assertEquals(10, dispatcher.getEmployees().size());
	}

	@Test
	void addEmployees() {
		populateEmployees(9);
		List<Employee> employees = new ArrayList<>();
		for (int i = 0; i < 6; i++) {
			employees.add(new Employee(dispatcher, Employee.EmployeeType.OPERATOR));
		}
		dispatcher.addEmployees(employees);
		assertEquals(15, dispatcher.getEmployees().size());
	}
}