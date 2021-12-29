package ro.ubbcluj.map.domain.validators;

import ro.ubbcluj.map.domain.Prietenie;

public class PrietenieValidator implements Validator<Prietenie> {
    @Override
    public void validate(Prietenie entity) throws ValidationException {
        if (entity.getId() == null) {
            throw new ValidationException("Id-ul nu poate fi null.");
        }
        if (entity.getIdU() == null) {
            throw new ValidationException("Id-ul utilizatorului nu poate fi null");
        }
        if (entity.getIdP() == null) {
            throw new ValidationException("Id-ul prietenului nu poate fi null");
        }
    }
}