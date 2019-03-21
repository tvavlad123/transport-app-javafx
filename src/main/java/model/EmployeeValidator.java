package model;

public class EmployeeValidator implements Validator<Employee> {

    @Override
    public void validate(Employee entity) throws ValidationException {
        StringBuilder errors = new StringBuilder();
        if (entity.getEmployeeID() < 0)
            errors.append("Negative Id\n");
        if (entity.getFirstName().length() == 0)
            errors.append("First name cannot be empty\n");
        if (entity.getLastName().length() == 0)
            errors.append("Last name cannot be empty\n");
        if (entity.getUsername().length() == 0)
            errors.append("Username cannot be empty\n");
        if (entity.getPassword().length() == 0)
            errors.append("Password cannot be empty\n");
        if (entity.getOffice().length() == 0)
            errors.append("First name cannot be empty\n");
        if (errors.length() > 0)
            throw new ValidationException(errors.toString());
    }
}
