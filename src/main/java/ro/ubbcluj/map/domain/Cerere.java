package ro.ubbcluj.map.domain;

public class Cerere extends Entity<Long> {
    private final Long idFrom;
    private final Long idTo;
    private String status;


    public Cerere(Long idFrom, Long idTo, String status) {
        this.idFrom = idFrom;
        this.idTo = idTo;
        this.status = status;
    }


    public Long getIdFrom() {
        return idFrom;
    }

    public Long getIdTo() {
        return idTo;
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
                "id=" + id +
                ", idFrom=" + idFrom +
                ", idTo=" + idTo +
                ", status=" + status +
                '}';
    }
}
