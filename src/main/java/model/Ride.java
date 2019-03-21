package model;


public class Ride {

    private int rideID;

    private String destination;

    private String date;

    private String hour;

    public Ride(int rideID, String destination, String date, String hour) {
        this.rideID = rideID;
        this.destination = destination;
        this.date = date;
        this.hour = hour;
    }

    public int getRideID() {
        return rideID;
    }

    public void setRideID(int rideID) {
        this.rideID = rideID;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }
}
