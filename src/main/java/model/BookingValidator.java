package model;

public class BookingValidator implements Validator<Booking> {

    @Override
    public void validate(Booking entity) throws ValidationException {
        StringBuilder errors = new StringBuilder();
        if (entity.getBookingID() < 0)
            errors.append("Negative Id\n");
        if (entity.getClientID() < 0)
            errors.append("Client id cannot be negative\n");
        if (entity.getRideID() < 0)
            errors.append("Ride id cannot be negative\n");
        if (entity.getSeatNo() < 0)
            errors.append("Seat number must be positive\n");
        if (errors.length() > 0)
            throw new ValidationException(errors.toString());
    }
}
