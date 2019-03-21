package service;

import model.Employee;
import repository.EmployeeJDBCRepository;

public class EmployeeService extends AbstractService<Integer, Employee> {

    private EmployeeJDBCRepository employeeRepository;

    public EmployeeService(EmployeeJDBCRepository employeeRepository) {
        super(employeeRepository);
        this.employeeRepository = employeeRepository;
    }

    public boolean login(String username, String password) {
        return employeeRepository.login(username, password);
    }
}
