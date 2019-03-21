package model;

public class SearchWrapper {

    private String clientName;

    private int seatNo;

    public SearchWrapper(String clientName, int seatNo) {
        this.clientName = clientName;
        this.seatNo = seatNo;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public int getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(int seatNo) {
        this.seatNo = seatNo;
    }
}
