package service;

import model.Client;
import model.ValidationException;
import repository.ClientJDBCRepository;

public class ClientService extends AbstractService<Integer, Client> {

    private ClientJDBCRepository clientJDBCRepository;

    public ClientService(ClientJDBCRepository clientJDBCRepository) {
        this.clientJDBCRepository = clientJDBCRepository;
    }

    public int filterByClientId(String firstName, String lastName) {
        return clientJDBCRepository.findByFirstAndLastName(firstName, lastName).getClientID();
    }

    @Override
    public void add(Client client) throws ValidationException {
        clientJDBCRepository.save(client);
    }

    @Override
    public Integer getSize() {
        return clientJDBCRepository.size();
    }
}
