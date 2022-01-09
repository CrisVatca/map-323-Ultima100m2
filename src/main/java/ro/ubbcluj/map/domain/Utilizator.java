package ro.ubbcluj.map.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Utilizator extends Entity<Long> {
    private String firstName;
    private String lastName;
    private final List<Utilizator> friends = new ArrayList<>();
    private String userName;
    private String password;

    public Utilizator(String firstName, String lastName, String userName, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
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

    public String getUserName(){return userName;}

    public void setUserName(String userName){
        this.userName = userName;
    }

    public String getPassword(){return password;}

    public void setPassword(String password){
        this.password = password;
    }

    @Override
    public String toString() {
        StringBuilder stringOfFriends = new StringBuilder("[");
        for (Utilizator utilizator : friends) {
            String ut = "{" + utilizator.getId() + "; " + utilizator.firstName + "; " + utilizator.lastName + "; " +
                    utilizator.userName + "} ";
            stringOfFriends.append(ut);
        }
        return "Utilizator{" +
                "id = " + id + ", " +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", friends=" + stringOfFriends + ']' +
                ", userName=" + userName + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Utilizator)) return false;
        Utilizator that = (Utilizator) o;
        return getFirstName().equals(that.getFirstName()) &&
                getLastName().equals(that.getLastName()) &&
                getFriends().equals(that.getFriends()) &&
                getUserName().equals(that.getUserName()) &&
                getPassword().equals(that.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getFriends(), getUserName(), getPassword());
    }

    public void makeFriend(Utilizator utilizator) {
        this.friends.add(utilizator);
    }

    public void deleteOneFriend(Utilizator utilizator) {
        this.friends.remove(utilizator);
    }
}