package ro.ubbcluj.map.repository.memory;

import ro.ubbcluj.map.domain.Entity;
import ro.ubbcluj.map.domain.validators.ValidationException;
import ro.ubbcluj.map.domain.validators.Validator;
import ro.ubbcluj.map.repository.Repository;

import java.util.HashMap;
import java.util.Map;

public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {

    private Validator<E> validator;
    protected Map<ID, E> entities;

    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        entities = new HashMap<ID, E>();
    }

    @Override
    public Map<ID, E> getEntities() {
        return this.entities;
    }

    @Override
    public E findOne(ID id) {
        if (id == null)
            throw new IllegalArgumentException("id must be not null");
        return entities.get(id);
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public E save(E entity) {
        if (entity == null)
            throw new IllegalArgumentException("entity must be not null");
        try {
            validator.validate(entity);
        } catch (ValidationException e) {
            throw e;
        }
        if (entities.get(entity.getId()) != null) {
            return entity;
        } else entities.put(entity.getId(), entity);
        return null;
    }

    @Override
    public E delete(ID id) {
        E entity = entities.get(id);
        if (entity == null) {
            throw new IllegalArgumentException("deleted entity doesn't exist");
        }

        return entities.remove(id);
    }

    @Override
    public E update(E entity) {

        if (entity == null)
            throw new IllegalArgumentException("entity must be not null!");
        validator.validate(entity);

        entities.put(entity.getId(), entity);

        if (entities.get(entity.getId()) != null) {
            entities.put(entity.getId(), entity);
            return null;
        }
        return entity;

    }

    @Override
    public E getEntity(E entity) {
        return entities.get(entity.getId());
    }

}