package ro.ubbcluj.map.repository.db;

import ro.ubbcluj.map.domain.Cerere;
import ro.ubbcluj.map.domain.validators.Validator;
import ro.ubbcluj.map.repository.Repository;

import java.sql.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CerereDbRepository implements Repository<Long, Cerere> {
    private final String url;
    private final String username;
    private final String password;
    private final Validator<Cerere> validator;

    public CerereDbRepository(String url, String username, String password, Validator<Cerere> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public Cerere findOne(Long aLong) {
        String sql = "select * from cerere where id=?";

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
    public Iterable<Cerere> findAll() {
        Set<Cerere> cereri = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from cerere");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long idFrom = resultSet.getLong("idfrom");
                Long idTo = resultSet.getLong("idto");
                String status = resultSet.getString("status");

                Cerere cerere = new Cerere(idFrom, idTo, status);
                cerere.setId(id);
                cereri.add(cerere);
            }
            return cereri;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cereri;
    }

    @Override
    public Cerere save(Cerere entity) {
        String sql = "insert into cerere (idFrom, idTo, status) values (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, entity.getIdFrom());
            ps.setLong(2, entity.getIdTo());
            ps.setString(3, entity.getStatus());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Cerere delete(Long aLong) {
        String sql = "delete from cerere where id=?";

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
    public Cerere update(Cerere entity) {
        String sql = "update cerere set status=? where id=?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, entity.getStatus());
            ps.setLong(2, entity.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<Long, Cerere> getEntities() {
        Map<Long, Cerere> entities = new HashMap<>();

        for (Cerere c : this.findAll())
            entities.put(c.getId(), c);

        return entities;
    }

    @Override
    public Cerere getEntity(Cerere one) {
        return one;
    }
}
