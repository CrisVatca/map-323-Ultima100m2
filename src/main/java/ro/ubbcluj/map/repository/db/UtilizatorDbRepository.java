package ro.ubbcluj.map.repository.db;

import ro.ubbcluj.map.domain.Utilizator;
import ro.ubbcluj.map.domain.validators.Validator;
import ro.ubbcluj.map.repository.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UtilizatorDbRepository implements Repository<Long, Utilizator> {
    private String url;
    private String username;
    private String password;
    private Validator<Utilizator> validator;

    public UtilizatorDbRepository(String url, String username, String password, Validator<Utilizator> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public Map<Long, Utilizator> getEntities() {
        Map<Long, Utilizator> entities = new HashMap<>();

        for (Utilizator u : this.findAll())
            entities.put(u.getId(), u);

        return entities;
    }

    @Override
    public Utilizator findOne(Long aLong) {
        String sql = "select * from utilizatori where id=?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, aLong);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");

                Utilizator utilizator = new Utilizator(firstName, lastName);
                utilizator.setId(id);
                return utilizator;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Utilizator> findAll() {
        List<Utilizator> users = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from utilizatori");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");

                Utilizator utilizator = new Utilizator(firstName, lastName);
                utilizator.setId(id);

                users.add(getEntity(utilizator));
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public Utilizator getEntity(Utilizator utilizator) {
        if (utilizator == null)
            return utilizator;
        Long id = utilizator.getId();
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sql2 = "SELECT idu FROM prietenie WHERE idp = ?";
            PreparedStatement statement2 = connection.prepareStatement(sql2);
            statement2.setLong(1, id);
            ResultSet resultSet2 = statement2.executeQuery();

            while (resultSet2.next()) {
                Utilizator utilizator2 = findOne(resultSet2.getLong(1));
                utilizator2.setId(resultSet2.getLong(1));
                utilizator.makeFriend(utilizator2);
            }

            String sql3 = "SELECT idp FROM prietenie WHERE idu = ?";
            PreparedStatement statement3 = connection.prepareStatement(sql3);
            statement3.setLong(1, id);
            ResultSet resultSet3 = statement3.executeQuery();

            while (resultSet3.next()) {
                Utilizator utilizator3 = findOne(resultSet3.getLong(1));
                utilizator3.setId(resultSet3.getLong(1));
                utilizator.makeFriend(utilizator3);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return utilizator;
    }

    @Override
    public Utilizator save(Utilizator entity) {

        String sql = "insert into utilizatori (first_name, last_name) values (?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Utilizator delete(Long aLong) {
        String sql = "delete from utilizatori where id=?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, aLong);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Utilizator update(Utilizator entity) {
        String sql = "update utilizatori set first_name = ?, last_name = ? where id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());
            ps.setLong(3, entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entity;
    }
}
