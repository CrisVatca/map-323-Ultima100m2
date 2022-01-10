package ro.ubbcluj.map.service;

import ro.ubbcluj.map.domain.*;
import ro.ubbcluj.map.domain.validators.ValidationException;
import ro.ubbcluj.map.repository.Repository;
import ro.ubbcluj.map.utils.Graph;
import java.security.KeyException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static ro.ubbcluj.map.utils.MD5.getMd5;

public class Service {
    private final Repository<Long, Utilizator> repoUtilizatori;
    private final Repository<Long, Prietenie> repoPrietenie;
    private final Repository<Long, Message> repoMessage;
    private final Repository<Long, Cerere> repoCerere;

    public Service(Repository<Long, Utilizator> repoUtilizatori, Repository<Long, Prietenie> repoPrietenie,
                   Repository<Long, Message> repoMessage, Repository<Long, Cerere> repoCerere) {
        this.repoUtilizatori = repoUtilizatori;
        this.repoPrietenie = repoPrietenie;
        this.repoMessage = repoMessage;
        this.repoCerere = repoCerere;
    }

    public void addUser(String firstName, String lastName, String userName, String password) {
        Utilizator utilizator = new Utilizator(firstName, lastName, userName, getMd5(password));
        this.repoUtilizatori.save(utilizator);
    }

    public void deleteUser(Long id) {
        try {
            Utilizator u = this.repoUtilizatori.findOne(id);
            if (u == null)
                throw new NullPointerException("Utilizatorul nu poate fi sters deoarece nu exista!");
            this.repoUtilizatori.delete(id);
            List<Long> l = new ArrayList<>();
            for (Prietenie p : this.repoPrietenie.findAll()) {
                if (id.equals(p.getIdU())) {
                    l.add(p.getId());
                }

                if (id.equals(p.getIdP())) {
                    l.add(p.getId());
                }
            }
            for (long i : l)
                this.repoPrietenie.delete(i);
        } catch (NullPointerException e) {
            throw e;
        }
    }

    public void updateUser(Long id, String firstName, String lastName, String userName, String password) {
        try {
            Utilizator u = this.repoUtilizatori.findOne(id);
            if (u == null)
                throw new NullPointerException("Utilizatorul nu poate fi modificat deoarece acesta nu exista");
            Utilizator user = new Utilizator(firstName, lastName, userName, password);
            user.setId(id);
            this.repoUtilizatori.update(user);
        } catch (NullPointerException e) {
            throw e;
        }
    }

    public List<Utilizator> getUsers() {

        Iterable<Utilizator> utilizatori = this.repoUtilizatori.findAll();
        Iterable<Prietenie> prietenii = this.repoPrietenie.findAll();

        for (Prietenie p : prietenii) {
            for (Utilizator u : utilizatori) {
                if (u.getId().equals(p.getIdU()))
                    for (Utilizator u2 : utilizatori) {
                        if (u2.getId().equals(p.getIdP())) {
                            Iterable<Utilizator> uFriends = u.getFriends();
                            boolean ok = true;
                            for (Utilizator prieten : uFriends)
                                if (prieten.getId().equals(u2.getId())) {
                                    ok = false;
                                    break;
                                }
                            if (!ok)
                                continue;
                            u.makeFriend(u2);
                            u2.makeFriend(u);
                        }
                    }
            }
        }
        List<Utilizator> utilizatorList = new ArrayList<>();
        for (Utilizator u : utilizatori)
            utilizatorList.add(u);

        return utilizatorList;

    }


    public void addFriend(Long id1, Long id2, LocalDateTime date) throws KeyException {
        try {
            for (Prietenie prietenie : repoPrietenie.findAll()) {
                if ((prietenie.getIdP().equals(id1) && prietenie.getIdU().equals(id2)) || (prietenie.getIdP().equals(id2)
                        && prietenie.getIdU().equals(id1))) {
                    throw new KeyException("Prietenia exista deja!");
                }
            }
            if (!Objects.equals(id1, id2)) {
                Utilizator utilizator1 = this.repoUtilizatori.findOne(id1);
                Utilizator utilizator2 = this.repoUtilizatori.findOne(id2);
                if (utilizator1 == null || utilizator2 == null)
                    throw new NullPointerException("Utilizatorul trebuie sa existe pentru a se crea o prietenie!");
                Prietenie p = new Prietenie(id1, id2, date);
                this.repoPrietenie.save(p);
            }
        } catch (NullPointerException | ValidationException | KeyException e) {
            throw e;
        }
    }

    public void deleteFriend(Long id1, Long id2) {
        try {
            Utilizator u1 = this.repoUtilizatori.findOne(id1);
            Utilizator u2 = this.repoUtilizatori.findOne(id2);
            if (u1 == null || u2 == null)
                throw new NullPointerException("Utilizatorul trebuie sa existe pentru a sterge o prietenie!");
            u1.deleteOneFriend(u2);
            u2.deleteOneFriend(u1);
            Iterable<Prietenie> prietenie = this.repoPrietenie.findAll();
            Long id = 0L;
            for (Prietenie p : prietenie)
                if (id1.equals(p.getIdU())) {
                    if (id2.equals(p.getIdP())) {
                        id = p.getId();
                        break;
                    }
                }
            if (id != 0L)
                this.repoPrietenie.delete(id);
            else
                throw new NullPointerException("Prietenia nu a fost gasita");
        } catch (NullPointerException e) {
            throw e;
        }
    }

    public Utilizator getUserAfterUserName(String username){
        for(Utilizator u:getUsers()){
            if(u.getUserName().equals(username)){
                return u;
            }
        }
        return null;
    }

    public boolean existaCererea(Cerere cerere){
        for(Cerere c:getCereri()){
            if(c.getUserNameFrom().equals(cerere.getUserNameFrom()) && c.getUserNameTo().equals(cerere.getUserNameTo()) ||
                    c.getUserNameFrom().equals(cerere.getUserNameTo()) && c.getUserNameTo().equals(cerere.getUserNameFrom())) {
                if (!Objects.equals(c.getStatus(), "rejected")) {
                    return true;
                }
            }
        }
        return false;
    }

    public void trimiteCerere(String userNameFrom, String userNameTo) throws KeyException {

        for (Cerere cerere : this.repoCerere.findAll())
            if ((cerere.getUserNameFrom().equals(userNameFrom) && cerere.getUserNameTo().equals(userNameTo) && !Objects.equals(cerere.getStatus(), "rejected"))
                    || (cerere.getUserNameFrom().equals(userNameTo) && cerere.getUserNameTo().equals(userNameFrom) && !Objects.equals(cerere.getStatus(), "rejected")))
                throw new KeyException("Cererea de prietenie exista deja!");

        Utilizator utilizator1 = getUserAfterUserName(userNameFrom);
        Utilizator utilizator2 = getUserAfterUserName(userNameTo);
        if (utilizator1 == null || utilizator2 == null)
            throw new NullPointerException("Utilizatorii trebuie sa existe!");

        LocalDateTime datenow = LocalDateTime.now();

        Cerere cerere = new Cerere(userNameFrom, userNameTo, "pending",datenow);
        this.repoCerere.save(cerere);
    }

    public void raspundereCerere(String userNameFrom, String userNameTo, boolean accepted) throws KeyException {

        Cerere cererePrietenie = null;

        for (Cerere cerere : this.repoCerere.findAll())
            if (cerere.getUserNameFrom().equals(userNameFrom) && cerere.getUserNameTo().equals(userNameTo))
                cererePrietenie = cerere;

        if (cererePrietenie == null)
            throw new NullPointerException("Cererea nu exista!");

        if (accepted) {
            cererePrietenie.setStatus("approved");
            Utilizator u1 = getUserAfterUserName(userNameFrom);
            Utilizator u2 = getUserAfterUserName(userNameTo);
            addFriend(u1.getId(), u2.getId(), LocalDateTime.now());
        } else
            cererePrietenie.setStatus("rejected");

        this.repoCerere.update(cererePrietenie);
    }

    public void deleteCerere(Long id){
        this.repoCerere.delete(id);
    }

    public Iterable<Cerere> getCereri() {
        return this.repoCerere.findAll();
    }

    public int getNrOfConnectedComponents() {
        Graph graph = new Graph(getUsers());
        return graph.getNrOfConnectedComponents();
    }

    public List<Utilizator> getLargestConnectedComponent() {
        Graph graph = new Graph(getUsers());
        return graph.getLargestConnectedComponent();
    }

    public Map<Utilizator, LocalDateTime> prieteniiUnuiUtilizator(Long id) {
        try {
            Utilizator u = this.repoUtilizatori.findOne(id);
            List<Prietenie> prietenii = new ArrayList<>();
            this.repoPrietenie.findAll().forEach(prietenii::add);

            if (u == null)
                throw new NullPointerException("Utilizatorul trebuie sa existe!");

            Predicate<Prietenie> filterByUtilizator = prietenie -> u.getId().equals(prietenie.getIdU()) || u.getId().equals(prietenie.getIdP());

            return prietenii.stream()
                    .filter(filterByUtilizator)
                    .map(prietenie -> {
                        Long idPrieten = prietenie.getIdU() + prietenie.getIdP() - u.getId();
                        return new AbstractMap.SimpleEntry<Utilizator, LocalDateTime>(getById(idPrieten), prietenie.getDate());
                    })
                    .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
        } catch (NullPointerException e) {
            throw e;
        }
    }

    public Map<Utilizator, LocalDateTime> prieteniiUnuiUtilizatorPerLuna(Long id, int luna) {
        try {
            Utilizator u = this.repoUtilizatori.findOne(id);
            List<Prietenie> prietenii = new ArrayList<>();
            this.repoPrietenie.findAll().forEach(prietenii::add);

            if (u == null)
                throw new NullPointerException("Utilizatorul trebuie sa existe!");

            Predicate<Prietenie> filterByUtilizator = prietenie -> u.getId().equals(prietenie.getIdU()) || u.getId().equals(prietenie.getIdP());
            Predicate<Prietenie> filterByLuna = prietenie -> prietenie.getDate().toLocalDate().getMonthValue() == luna;

            return prietenii.stream()
                    .filter(filterByUtilizator)
                    .filter(filterByLuna)
                    .map(prietenie -> {
                        Long idPrieten = prietenie.getIdU() + prietenie.getIdP() - u.getId();
                        return new AbstractMap.SimpleEntry<Utilizator, LocalDateTime>(getById(idPrieten), prietenie.getDate());
                    })
                    .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
        } catch (NullPointerException e) {
            throw e;
        }
    }


    public Utilizator getById(Long x) {
        return this.repoUtilizatori.getEntity(this.repoUtilizatori.findOne(x));
    }

    public void addMessage(Long from, Long to, String message, LocalDateTime data, Long reply) {
        try {
            Utilizator u1 = repoUtilizatori.findOne(from);
            Utilizator u2 = repoUtilizatori.findOne(to);
            if (u1 == null)
                throw new NullPointerException("Mesajul nu poate fi adaugat deoarece utilizatorul de la from nu exista");
            if (u2 == null)
                throw new NullPointerException("Mesajul nu poate fi adaugat deoarece utilizatorul de la to nu exista");
            Message message1 = new Message(from, to, message, data, reply);
            repoMessage.save(message1);
        } catch (NullPointerException e) {
            throw e;
        }
    }

    public void deleteMessage(Long id) throws NullPointerException {
        Message m = repoMessage.findOne(id);
        if (m == null)
            throw new NullPointerException("Mesajul nu poate fi sters deoarece mesajul nu exista!");
        repoMessage.delete(id);
    }

    public Iterable<Message> getAllMessages() {
        return repoMessage.findAll();
    }

    public List<Message> conversatii(Utilizator u1, Utilizator u2) {
        List<Message> list = new ArrayList<>();
        for (Message message : repoMessage.findAll())
            list.add(message);

        List<Message> listaConversatii = list.stream()
                .sorted(Comparator.comparing(Message::getData))
                .filter(mesaj -> {
                    return ((u1.getId().equals(mesaj.getFrom()) && mesaj.getTo().equals(u2.getId())) ||
                            (u2.getId().equals(mesaj.getFrom()) && mesaj.getTo().equals(u1.getId())));
                })
                .collect(Collectors.toList());
        return listaConversatii;
    }

    public List<MessageGui> getMessagesOf(Utilizator utilizator){
        Iterable<Message> messages = getAllMessages();
        List<Message> messageList = new ArrayList<>();
        messages.forEach(messageList::add);

        messageList = messageList.stream()
                .filter(m-> (m.getFrom().equals(utilizator.getId()) || m.getTo().equals(utilizator.getId())))
                .collect(Collectors.toList());
        return parseMessageToMessageGui(messageList);
    }

    private List<MessageGui> parseMessageToMessageGui(List<Message> messageList) {
        List<MessageGui> messageGuiList = new ArrayList<>();
        messageList.forEach(m->messageGuiList.add(new MessageGui(m.getId(),
                this.repoUtilizatori.findOne(m.getFrom()).getUserName(),
                this.repoUtilizatori.findOne(m.getTo()).getUserName(),
                m.getMessage(),m.getData(),m.getReply())));
        return messageGuiList;
    }
}
