import model.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import repository.*;

import java.util.Random;


public class DBTest {
    private EmployeeJDBCRepository employeeJDBCRepository;
    private BookingJDBCRepository bookingJDBCRepository;
    private ClientJDBCRepository clientJDBCRepository;
    private RideJDBCRepository rideJDBCRepository;

    @Before
    public void initialize() {
        DBUtils dbUtils = new DBUtils("db.properties");
        employeeJDBCRepository = new EmployeeJDBCRepository(dbUtils);
        bookingJDBCRepository = new BookingJDBCRepository(dbUtils);
        clientJDBCRepository = new ClientJDBCRepository(dbUtils);
        rideJDBCRepository = new RideJDBCRepository(dbUtils);
    }

    @Test
    public void testClientDBRepository() {
        Integer currentSize = clientJDBCRepository.size();
        Client client = new Client(currentSize + 1, "Test", "Tester");
        try {
            clientJDBCRepository.save(client);
        } catch (ValidationException e) {
            Assert.fail();
        }
        Assert.assertEquals(clientJDBCRepository.size(), currentSize + 1);
        Client updateClient = new Client(currentSize + 1, "UpdateTest", "UpdateTester");
        try {
            clientJDBCRepository.update(client.getClientID(), updateClient);
        } catch (ValidationException e) {
            Assert.fail();
        }
        Client found = clientJDBCRepository.findOne(client.getClientID());
        Assert.assertEquals(client.getFirstName(), found.getFirstName());
        clientJDBCRepository.findOne(updateClient.getClientID());

        clientJDBCRepository.delete(updateClient.getClientID());
        Assert.assertEquals(clientJDBCRepository.size(), (int) currentSize);
    }

    @Test
    public void testEmployeeDBRepository() {
        Integer currentSize = employeeJDBCRepository.size();
        Random random = new Random();
        Employee employee = new Employee(currentSize + 1,
                "Test", "Tester", Double.toString(random.nextDouble()), "test123", "Cluj");

        try {
            employeeJDBCRepository.save(employee);
        } catch (ValidationException e) {
            Assert.fail();
        }
        Assert.assertEquals(employeeJDBCRepository.size(), currentSize + 1);
        Employee updateEmployee = new Employee(currentSize + 1, "UpdateTest", "UpdateTester",
                Double.toString(random.nextDouble()), "testpass", "TestOffice");
        try {
            employeeJDBCRepository.update(employee.getEmployeeID(), updateEmployee);
        } catch (ValidationException e) {
            Assert.fail();
        }
        Employee found = employeeJDBCRepository.findOne(employee.getEmployeeID());
        Assert.assertEquals(updateEmployee.getFirstName(), found.getFirstName());
        employeeJDBCRepository.findOne(employee.getEmployeeID());

        employeeJDBCRepository.delete(updateEmployee.getEmployeeID());
        Assert.assertEquals(employeeJDBCRepository.size(), (int) currentSize);
    }

    @Test
    public void testRideDBRepository() {
        Integer currentSize = rideJDBCRepository.size();
        Random random = new Random();
        Ride ride = new Ride(currentSize + 1, "TestCancun", "2019-10-10", "08:20");

        try {
            rideJDBCRepository.save(ride);
        } catch (ValidationException e) {
            Assert.fail();
        }
        Assert.assertEquals(rideJDBCRepository.size(), currentSize + 1);
        Ride updateRide = new Ride(currentSize + 1, "TestHawaii", "2020-07-08", "11:20");
        try {
            rideJDBCRepository.update(ride.getRideID(), updateRide);
        } catch (ValidationException e) {
            Assert.fail();
        }
        Ride found = rideJDBCRepository.findOne(ride.getRideID());
        Assert.assertEquals(updateRide.getDestination(), found.getDestination());
        rideJDBCRepository.findOne(ride.getRideID());

        rideJDBCRepository.delete(updateRide.getRideID());
        Assert.assertEquals(rideJDBCRepository.size(), (int) currentSize);
    }

    @Test
    public void testBookingDBRepository() {
        Integer currentSize = bookingJDBCRepository.size();
        Random random = new Random();
        Booking booking = new Booking(currentSize + 1, 1, 1, 1);

        try {
            bookingJDBCRepository.save(booking);
        } catch (ValidationException e) {
            Assert.fail();
        }
        Assert.assertEquals(bookingJDBCRepository.size(), currentSize + 1);
        Booking updateBooking = new Booking(currentSize + 1, 1, 1, 2);
        try {
            bookingJDBCRepository.update(booking.getBookingID(), updateBooking);
        } catch (ValidationException e) {
            Assert.fail();
        }
        Booking found = bookingJDBCRepository.findOne(booking.getBookingID());
        Assert.assertEquals(updateBooking.getSeatNo(), found.getSeatNo());
        bookingJDBCRepository.findOne(booking.getBookingID());

        bookingJDBCRepository.delete(updateBooking.getBookingID());
        Assert.assertEquals(bookingJDBCRepository.size(), (int) currentSize);
    }

}
