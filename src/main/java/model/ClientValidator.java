package model;

public class ClientValidator implements Validator<Client> {

    @Override
    public void validate(Client entity) throws ValidationException {
        StringBuilder errors = new StringBuilder();
        if (entity.getClientID() < 0)
            errors.append("Negative Id\n");
        if (entity.getFirstName().length() == 0)
            errors.append("First name cannot be empty\n");
        if (entity.getLastName().length() == 0)
            errors.append("Last name cannot be empty\n");
        if (errors.length() > 0)
            throw new ValidationException(errors.toString());
    }
}
