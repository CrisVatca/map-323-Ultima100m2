package ro.ubbcluj.map.repository.file;

import ro.ubbcluj.map.domain.Entity;
import ro.ubbcluj.map.domain.validators.Validator;
import ro.ubbcluj.map.repository.memory.InMemoryRepository;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E> {
    protected String fileName;

    public AbstractFileRepository(String fileName, Validator<E> validator) {
        super(validator);
        this.fileName = fileName;
        loadData();
    }

    private void loadData() {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                E e = extractEntity(Arrays.asList(line.split(";")));
                super.save(e);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private E saveData(E e) {
        if (e != null) {
            try (BufferedWriter br = new BufferedWriter(new FileWriter(fileName, false))) {
                for (E en : super.entities.values()) {
                    br.write(createEntityAsString(en));
                    br.newLine();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return e;
    }

    public void writeToFile(E entity) {
        FileWriter file = null;
        try {
            file = new FileWriter(this.fileName, true);
            file.write(createEntityAsString(entity));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            assert file != null;
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * extract entity  - template method design pattern
     * creates an entity of type E having a specified list of @code attributes
     *
     * @param attributes
     * @return an entity of type E
     */
    public abstract E extractEntity(List<String> attributes);
    ///Observatie-Sugestie: in locul metodei template extractEntity, puteti avea un factory pr crearea instantelor entity

    public abstract String createEntityAsString(E entity);

    @Override
    public E save(E entity) {
        E e = super.save(entity);
        if (e == null)
            writeToFile(entity);
        return e;
    }

    @Override
    public E delete(ID id) {
        E e = super.delete(id);
        return saveData(e);
    }

    @Override
    public E update(E entity) {
        E e = super.update(entity);
        return saveData(e);
    }

    @Override
    public Iterable<E> findAll() {

        return entities.values();
    }
}