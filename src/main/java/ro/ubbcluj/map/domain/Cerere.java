package ro.ubbcluj.map.domain;

import java.time.LocalDateTime;

public class Cerere extends Entity<Long> {
    private String userNameFrom;
    private String userNameTo;
    private String status;
    private LocalDateTime date;


    public Cerere(String userNameFrom, String userNameTo, String status, LocalDateTime date) {
        this.userNameFrom = userNameFrom;
        this.userNameTo = userNameTo;
        this.status = status;
        this.date = date;
    }

    public String getUserNameFrom() {
        return userNameFrom;
    }

    public void setUserNameFrom(String userNameFrom) {
        this.userNameFrom = userNameFrom;
    }

    public String getUserNameTo() {
        return userNameTo;
    }

    public void setUserNameTo(String userNameTo) {
        this.userNameTo = userNameTo;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Cerere{" +
                "userNameFrom='" + userNameFrom + '\'' +
                ", userNameTo='" + userNameTo + '\'' +
                ", status='" + status + '\'' +
                ", date=" + date +
                ", id=" + id +
                '}';
    }
}
