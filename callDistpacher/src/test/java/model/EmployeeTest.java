package model;

import call_center.Dispatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {
	@BeforeEach
	void setUp() {
		Employee.resetIdSequence();
		Call.resetIdSequence();
	}

	@Test
	void getEmployeeId() {
		Employee employee = new Employee(Employee.EmployeeType.OPERATOR);
		assertAll(
				() -> assertEquals(1, employee.getEmployeeId().intValue()),
				() -> {
					for (int i = 0; i < 8; i++)
						new Employee(Employee.EmployeeType.OPERATOR);
					Employee lastEmployee = new Employee(Employee.EmployeeType.OPERATOR);
					assertEquals(10, lastEmployee.getEmployeeId().intValue());
				});
	}

	@Test
	void setCall() {
		Employee employee = new Employee(Employee.EmployeeType.OPERATOR);
		employee.setCall(new Call());
		assertAll(
				() -> assertNotNull(employee.getCall()),
				() -> assertEquals(1, employee.getCall().getId().intValue()),
				() -> {
					for (int i = 0; i < 9; i++)
						employee.setCall(new Call());
					assertEquals(10, employee.getCall().getId().intValue());
				});
	}

	@Test
	void getCall() {
		Employee employee = new Employee(Employee.EmployeeType.OPERATOR);
		assertAll(
				() -> assertNull(employee.getCall()),
				() -> {
					employee.setCall(new Call());
					assertEquals(1, employee.getCall().getId().intValue());
				});
	}

	@Test
	void getType() {
		Dispatcher dispatcher = new Dispatcher();
		Employee employee = new Employee(dispatcher, Employee.EmployeeType.OPERATOR);
		assertEquals(1, employee.getType().getPriority().intValue());
	}

	@Test
	void setType() {
		Dispatcher dispatcher = new Dispatcher();
		Employee employee = new Employee(dispatcher, Employee.EmployeeType.OPERATOR);
		assertAll(
				() -> assertEquals(1, employee.getType().getPriority().intValue()),
				() -> {
					employee.setType(Employee.EmployeeType.SUPERVISOR);
					assertEquals(2, employee.getType().getPriority().intValue());
				},
				() -> {
					employee.setType(Employee.EmployeeType.DIRECTOR);
					assertEquals(3, employee.getType().getPriority().intValue());
				}
		);
	}

	@Test
	void attendCall() {
		Dispatcher dispatcher = new Dispatcher();
		Employee employee = new Employee(dispatcher, Employee.EmployeeType.OPERATOR);
		employee.attendCall(new Call());
		assertAll(
				// The call should finish after minimum 5 seconds
				() -> assertNotNull(employee.getCall()),
				() -> {
					try {

						// The call should be finish after maximum 10 seconds, then after 11
						// seconds the current call of the employee shouldn't exist
						Thread.sleep(11 * 1000L);
						assertNull(employee.getCall());
					} catch (InterruptedException e) {
						fail(e.getMessage());
						Thread.currentThread().interrupt();
					}
				}
		);
	}

	@Test
	void compareTo() {
		Employee employee = new Employee(Employee.EmployeeType.SUPERVISOR);
		assertAll(
				() -> assertEquals(1, employee.compareTo(new Employee(
						Employee.EmployeeType.OPERATOR))),
				() -> assertEquals(0, employee.compareTo(new Employee(
						Employee.EmployeeType.SUPERVISOR))),
				() -> assertEquals(-1, employee.compareTo(new Employee(
						Employee.EmployeeType.DIRECTOR
				)))
		);
	}
}