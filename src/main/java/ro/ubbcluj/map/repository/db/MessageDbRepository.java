package ro.ubbcluj.map.repository.db;

import ro.ubbcluj.map.domain.Message;
import ro.ubbcluj.map.domain.Utilizator;
import ro.ubbcluj.map.domain.validators.Validator;
import ro.ubbcluj.map.repository.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageDbRepository implements Repository<Long, Message> {
    private String url;
    private String username;
    private String password;
    private Repository<Long, Utilizator> repoUtilizatori;
    private Validator<Message> validator;

    public MessageDbRepository(String url, String username, String password, Validator<Message> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public Message findOne(Long aLong) {

        String sql = "select * from messages where id = ?";
        Message mess = null;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long from = resultSet.getLong("from_user");
                Long to = resultSet.getLong("to_user");

                String message = resultSet.getString("message");
                Timestamp timestamp = resultSet.getTimestamp("data");
                LocalDateTime data = timestamp.toLocalDateTime();

                Long reply = resultSet.getLong("reply");

                mess = new Message(from, to, message, data, reply);
                mess.setId(id);
                return mess;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mess;
    }

    @Override
    public Iterable<Message> findAll() {
        List<Message> messages = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement
                     ("SELECT * from messages");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");

                Long from = resultSet.getLong("from_user");
                Long to = resultSet.getLong("to_user");

                String message = resultSet.getString("message");
                Timestamp timestamp = resultSet.getTimestamp("data");
                LocalDateTime data = timestamp.toLocalDateTime();

                Long reply = resultSet.getLong("reply");

                Message mess = new Message(from, to, message, data, reply);
                mess.setId(id);
                messages.add(mess);
            }
            return messages;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    @Override
    public Message save(Message entity) {
        String sql = "insert into messages (from_user, to_user, message, data, reply) values (?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, entity.getFrom());

            ps.setLong(2, entity.getTo());
            ps.setString(3, entity.getMessage());
            ps.setObject(4, entity.getData());

            if (entity.getReply() != null && entity.getReply() != 0)
                ps.setLong(5, entity.getReply());
            else ps.setLong(5, 0);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Message delete(Long aLong) {
        String sql = "delete from messages where id=?";

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
    public Message update(Message entity) {
        String sql = "update messages set from_user = ?, to_user = ?,message = ?, data = ? where id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, entity.getFrom());
            ps.setLong(2, entity.getTo());
            ps.setString(3, entity.getMessage());
            ps.setString(4, entity.getData().toString());

            ps.setLong(5, entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public Map<Long, Message> getEntities() {
        Map<Long, Message> entities = new HashMap<>();

        for (Message m : this.findAll())
            entities.put(m.getId(), m);

        return entities;
    }

    @Override
    public Message getEntity(Message one) {
        return one;
    }
}
