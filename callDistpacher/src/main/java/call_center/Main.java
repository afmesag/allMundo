package call_center;

import model.Call;
import model.Employee;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

	public static void main(String[] args) {
		Dispatcher dispatcher = new Dispatcher();
		List<Employee> employees = new ArrayList<>();
		List<Call> calls = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			Integer randomType = new Random().nextInt(3);
			switch (randomType) {
				case 0:
					employees.add(new Employee(dispatcher, Employee.EmployeeType.OPERATOR));
					break;
				case 1:
					employees.add(new Employee(dispatcher, Employee.EmployeeType.SUPERVISOR));
					break;
				case 2:
					employees.add(new Employee(dispatcher, Employee.EmployeeType.DIRECTOR));
					break;
				default:
					employees.add(new Employee(dispatcher, Employee.EmployeeType.OPERATOR));
			}
		}
		for (int i = 0; i < 10; i++) {
			calls.add(new Call());
		}
		dispatcher.addEmployees(employees);
		dispatcher.addCalls(calls);
		dispatcher.start();
	}
}
