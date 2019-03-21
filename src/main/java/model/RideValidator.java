package model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class RideValidator implements Validator<Ride> {
    private final static String DATE_FORMAT = "yyyy-MM-dd";

    @Override
    public void validate(Ride entity) throws ValidationException {
        StringBuilder errors = new StringBuilder();
        if (entity.getRideID() < 0)
            errors.append("Negative Id\n");
        if (entity.getDestination().length() == 0)
            errors.append("Destination cannot be empty\n");
        if (!isDateValid(entity.getDate()))
            errors.append("Date cannot be empty\n");
        if (entity.getHour().length() == 0)
            errors.append("Hour cannot be empty\n");
        if (errors.length() > 0)
            throw new ValidationException(errors.toString());
    }

    public static boolean isDateValid(String date) {
        try {
            DateFormat df = new SimpleDateFormat(DATE_FORMAT);
            df.setLenient(false);
            df.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

}
