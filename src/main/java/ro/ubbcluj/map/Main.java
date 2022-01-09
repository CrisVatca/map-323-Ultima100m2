package ro.ubbcluj.map;


import ro.ubbcluj.map.domain.Cerere;
import ro.ubbcluj.map.domain.Message;
import ro.ubbcluj.map.domain.Prietenie;
import ro.ubbcluj.map.domain.Utilizator;
import ro.ubbcluj.map.domain.validators.CerereValidator;
import ro.ubbcluj.map.domain.validators.MessageValidator;
import ro.ubbcluj.map.domain.validators.PrietenieValidator;
import ro.ubbcluj.map.domain.validators.UtilizatorValidator;
import ro.ubbcluj.map.repository.Repository;
import ro.ubbcluj.map.repository.db.CerereDbRepository;
import ro.ubbcluj.map.repository.db.MessageDbRepository;
import ro.ubbcluj.map.repository.db.PrietenieDbRepository;
import ro.ubbcluj.map.repository.db.UtilizatorDbRepository;
import ro.ubbcluj.map.service.Service;
import ro.ubbcluj.map.ui.UI;

public class Main {
    public static void main(String[] args) {
        UtilizatorValidator validator = new UtilizatorValidator();
        PrietenieValidator validator1 = new PrietenieValidator();
        MessageValidator validator2 = new MessageValidator();
        CerereValidator validator3 = new CerereValidator();

        Repository<Long, Cerere> repoCerere = new CerereDbRepository("jdbc:postgresql://localhost:5432/db", "postgres", "compunere", validator3);
        Repository<Long, Utilizator> repoUtilizatori = new UtilizatorDbRepository("jdbc:postgresql://localhost:5432/db", "postgres", "compunere", validator);
        Repository<Long, Prietenie> repoPrietenie = new PrietenieDbRepository("jdbc:postgresql://localhost:5432/db", "postgres", "compunere", validator1);
        Repository<Long, Message> repoMessage = new MessageDbRepository("jdbc:postgresql://localhost:5432/db", "postgres", "compunere", validator2);
        //Repository<Long, Utilizator> repoUtilizatori = new UtilizatorFileRepository("data/users.csv", new UtilizatorValidator());
        //Repository<Long, Prietenie> repoPrietenie = new PrietenieFileRepository("data/friendships.csv", new PrietenieValidator());
        Service service = new Service(repoUtilizatori, repoPrietenie, repoMessage, repoCerere);
        UI ui = new UI(service);
        ui.menu();
    }
}
