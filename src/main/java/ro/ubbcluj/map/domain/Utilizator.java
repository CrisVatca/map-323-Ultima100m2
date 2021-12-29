package ro.ubbcluj.map.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Utilizator extends Entity<Long> {
    private String firstName;
    private String lastName;
    private final List<Utilizator> friends = new ArrayList<>();

    public Utilizator(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
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

    public List<Utilizator> getFriends() {
        return friends;
    }

    @Override
    public String toString() {
        StringBuilder stringOfFriends = new StringBuilder("[");
        for (Utilizator utilizator : friends) {
            String ut = "{" + utilizator.getId() + "; " + utilizator.firstName + "; " + utilizator.lastName + "} ";
            stringOfFriends.append(ut);
        }
        return "Utilizator{" +
                "id = " + id + ", " +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", friends=" + stringOfFriends + ']' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Utilizator)) return false;
        Utilizator that = (Utilizator) o;
        return getFirstName().equals(that.getFirstName()) &&
                getLastName().equals(that.getLastName()) &&
                getFriends().equals(that.getFriends());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getFriends());
    }

    public void makeFriend(Utilizator utilizator) {
        this.friends.add(utilizator);
    }

    public void deleteOneFriend(Utilizator utilizator) {
        this.friends.remove(utilizator);
    }
}