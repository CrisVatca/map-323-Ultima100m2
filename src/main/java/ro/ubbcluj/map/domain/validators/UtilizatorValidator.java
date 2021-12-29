package ro.ubbcluj.map.domain.validators;

import ro.ubbcluj.map.domain.Utilizator;

public class UtilizatorValidator implements Validator<Utilizator> {
    @Override
    public void validate(Utilizator entity) throws ValidationException {
        if (entity.getFirstName() == null) {
            throw new ValidationException("Prenumele trebuie sa existe!");
        }
        if (entity.getLastName() == null) {
            throw new ValidationException("Numele trebuie sa existe!");
        }
        if (entity.getId() == null) {
            throw new ValidationException("Id-ul nu poate fi null!");
        }
    }
}

