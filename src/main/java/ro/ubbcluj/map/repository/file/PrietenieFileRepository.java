package ro.ubbcluj.map.repository.file;

import ro.ubbcluj.map.domain.Prietenie;
import ro.ubbcluj.map.domain.validators.Validator;

import java.time.LocalDateTime;
import java.util.List;

public class PrietenieFileRepository extends AbstractFileRepository<Long, Prietenie> {

    private static Long nextId = 0L;

    public PrietenieFileRepository(String fileName, Validator<Prietenie> validator) {
        super(fileName, validator);
        nextId++;
    }

    @Override
    public Prietenie extractEntity(List<String> attributes) {
        long currentId = Long.parseLong(attributes.get(0));
        System.out.println(attributes);
        if (currentId > nextId) {
            nextId = currentId;
        }
        long id1 = Long.parseLong(attributes.get(1));
        long id2 = Long.parseLong(attributes.get(2));
        LocalDateTime date = LocalDateTime.parse(attributes.get(3));
        Prietenie p = new Prietenie(id1, id2, date);
        p.setId(Long.parseLong(attributes.get(0)));
        return p;
    }


    @Override
    public Prietenie save(Prietenie entity) {
        entity.setId(nextId);
        nextId++;
        return super.save(entity);
    }

    @Override
    public String createEntityAsString(Prietenie entity) {
        return entity.getId().toString() + ";" + entity.getIdU().toString() + ";" + entity.getIdP().toString() +
                ";" + entity.getDate().toString() + '\n';
    }

}
