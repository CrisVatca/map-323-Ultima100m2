package ro.ubbcluj.map.domain.validators;

import ro.ubbcluj.map.domain.Cerere;

public class CerereValidator implements Validator<Cerere>{
    @Override
    public void validate(Cerere entity) throws ValidationException{
        if(entity.getId() == null){
            throw new ValidationException("Id-ul entitatii nu poate fi null!");
        }
        if(entity.getUserNameFrom() == null){
            throw new ValidationException("Username-ul utilizatorului de la care se primeste cererea trebuie sa existe!");
        }
        if(entity.getUserNameTo() == null){
            throw new ValidationException("Username-ul utilizatorului la care se trimite cererea trebuie sa existe!");
        }
        if(!"approved".equals(entity.getStatus()) && !"pending".equals(entity.getStatus()) &&
                !"rejected".equals(entity.getStatus())){
            throw new ValidationException("Statusul cererii poate fi doar una dintre [approved, pending, rejected]");
        }
    }
}
