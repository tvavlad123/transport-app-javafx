package model;

public class Booking {

    private int bookingID;

    private int clientID;

    private int rideID;

    private int seatNo;

    public Booking() {
    }

    public Booking(int bookingID, int clientID, int rideID, int seatNo) {
        this.bookingID = bookingID;
        this.clientID = clientID;
        this.rideID = rideID;
        this.seatNo = seatNo;
    }

    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public int getRideID() {
        return rideID;
    }

    public void setRideID(int rideID) {
        this.rideID = rideID;
    }

    public int getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(int seatNo) {
        this.seatNo = seatNo;
    }
}
