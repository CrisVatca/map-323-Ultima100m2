package ro.ubbcluj.map.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Prietenie extends Entity<Long> {
    private Long idU;
    private Long idP;
    private LocalDateTime date;

    public Prietenie(Long idU, Long idP, LocalDateTime date) {
        this.idU = idU;
        this.idP = idP;
        this.date = date;
    }

    public Long getIdU() {
        return idU;
    }

    public Long getIdP() {
        return idP;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setIdU(Long idU) {
        this.idU = idU;
    }

    public void setIdP(Long idP) {
        this.idP = idP;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Prietenie{" +
                "id prietenie=" + id +
                ", idU=" + idU +
                ", idP=" + idP +
                ", date=" + date +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prietenie prietenie = (Prietenie) o;
        return Objects.equals(idU, prietenie.idU) && Objects.equals(idP, prietenie.idP) &&
                Objects.equals(date, prietenie.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idU, idP, date);
    }
}

