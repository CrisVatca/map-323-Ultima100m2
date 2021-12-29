package ro.ubbcluj.map.repository.file;

import ro.ubbcluj.map.domain.Utilizator;
import ro.ubbcluj.map.domain.validators.Validator;

import java.util.List;

public class UtilizatorFileRepository extends AbstractFileRepository<Long, Utilizator> {

    private static Long nextId = 0L;

    public UtilizatorFileRepository(String fileName, Validator<Utilizator> validator) {
        super(fileName, validator);
        nextId++;
    }

    @Override
    public Utilizator extractEntity(List<String> attributes) {
        long currentId = Long.parseLong(attributes.get(0));

        if (currentId > nextId) {
            nextId = currentId;
        }
        Utilizator u = new Utilizator(attributes.get(1), attributes.get(2));
        u.setId(Long.parseLong(attributes.get(0)));
        return u;
    }

    @Override
    public String createEntityAsString(Utilizator entity) {
        return entity.getId().toString() + ";" + entity.getFirstName() + ";" + entity.getLastName() + '\n';
    }

    @Override
    public Utilizator save(Utilizator entity) {
        entity.setId(nextId);
        nextId++;
        return super.save(entity);
    }
}