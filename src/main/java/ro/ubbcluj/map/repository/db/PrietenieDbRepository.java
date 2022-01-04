package ro.ubbcluj.map.repository.db;

import ro.ubbcluj.map.domain.Prietenie;
import ro.ubbcluj.map.domain.validators.ValidationException;
import ro.ubbcluj.map.domain.validators.Validator;
import ro.ubbcluj.map.repository.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrietenieDbRepository implements Repository<Long, Prietenie> {
    private final String url;
    private final String username;
    private final String password;
    private final Validator<Prietenie> validator;

    public PrietenieDbRepository(String url, String username, String password, Validator<Prietenie> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public Map<Long, Prietenie> getEntities() {
        Map<Long, Prietenie> entities = new HashMap<>();

        for (Prietenie p : this.findAll())
            entities.put(p.getId(), p);

        return entities;
    }

    @Override
    public Prietenie findOne(Long id) {
        String sql = "SELECT * from prietenie WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            Long idu = resultSet.getLong("idu");
            Long idp = resultSet.getLong("idp");
            Long Id = resultSet.getLong("id");

            Timestamp timestamp = resultSet.getTimestamp("date");
            LocalDateTime date = timestamp.toLocalDateTime();

            Prietenie prietenie = new Prietenie(idu, idp, date);
            prietenie.setId(Id);
            return prietenie;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Prietenie> findAll() {
        List<Prietenie> prietenii = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from prietenie");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Long idu = resultSet.getLong("idu");
                Long idp = resultSet.getLong("idp");
                Long id = resultSet.getLong("id");
                Timestamp timestamp = resultSet.getTimestamp("date");
                LocalDateTime date = timestamp.toLocalDateTime();

                Prietenie prietenie = new Prietenie(idu, idp, date);
                prietenie.setId(id);
                prietenii.add(prietenie);
            }
            return prietenii;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prietenii;
    }

    @Override
    public Prietenie save(Prietenie entity) {

        Iterable<Prietenie> prietenii = findAll();
        for (Prietenie p : prietenii) {
            if (entity.getIdP().equals(p.getIdP()) && entity.getIdU().equals(p.getIdU())) {
                throw new ValidationException("Prietenia deja exista!");
            }
            if (entity.getIdU().equals(p.getIdP()) && entity.getIdP().equals(p.getIdU())) {
                throw new ValidationException("Prietenia deja exista!");
            }
        }

        String sql = "insert into prietenie (idu, idp, date) values (?,?,?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, entity.getIdU());
            ps.setLong(2, entity.getIdP());
            ps.setTimestamp(3, Timestamp.valueOf(entity.getDate()));

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Prietenie delete(Long id) {
        String sql = "delete from prietenie where id=?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Prietenie update(Prietenie entity) {
        String sql = "update prietenie set idu = ?, idp = ?, date = ? where id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, entity.getIdU());
            ps.setLong(2, entity.getIdP());
            ps.setTimestamp(3, Timestamp.valueOf(entity.getDate()));
            ps.setLong(4, entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public Prietenie getEntity(Prietenie p) {
        return p;
    }
}

