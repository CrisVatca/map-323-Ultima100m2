package ro.ubbcluj.map.domain.validators;

import ro.ubbcluj.map.domain.Cerere;

public class CerereValidator implements Validator<Cerere>{
    @Override
    public void validate(Cerere entity) throws ValidationException{
        if(entity.getId() == null){
            throw new ValidationException("Id-ul entitatii nu poate fi null!");
        }
        if(entity.getIdFrom() == null){
            throw new ValidationException("Id-ul utilizatorului de la care se primeste cererea trebuie sa existe!");
        }
        if(entity.getIdTo() == null){
            throw new ValidationException("Id-ul utilizatorului la care se trimite cererea trebuie sa existe!");
        }
        if(!"approved".equals(entity.getStatus()) && !"pending".equals(entity.getStatus()) &&
                !"rejected".equals(entity.getStatus())){
            throw new ValidationException("Statusul cererii poate fi doar una dintre [approved, pending, rejected]");
        }
    }
}
