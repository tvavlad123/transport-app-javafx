package model;

public class Client {

    private int clientID;

    private String firstName;

    private String lastName;

    public Client(int clientID, String firstName, String lastName) {
        this.clientID = clientID;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
