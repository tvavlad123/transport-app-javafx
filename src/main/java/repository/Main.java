package repository;

import model.Client;
import model.Employee;
import model.Ride;
import model.ValidationException;
import service.ClientService;


public class Main {

    public static void main(String[] args) {
        DBUtils dbUtils = new DBUtils("db.properties");
        RideJDBCRepository rideRepository = new RideJDBCRepository(dbUtils);
        ClientJDBCRepository clientJDBCRepository = new ClientJDBCRepository(dbUtils);
        ClientService clientService = new ClientService(clientJDBCRepository);
        System.out.println(clientService.getSize());

    }
}
