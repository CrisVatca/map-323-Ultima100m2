package ro.ubbcluj.map.ui;

import ro.ubbcluj.map.domain.Message;
import ro.ubbcluj.map.domain.Utilizator;
import ro.ubbcluj.map.domain.validators.ValidationException;
import ro.ubbcluj.map.service.Service;

import java.security.KeyException;
import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class UI {
    private final Service service;

    public UI(Service service) {
        this.service = service;
    }

    private void saveUI() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Dati prenumele utilizatorului de adaugat: ");
        String firstName = sc.nextLine();
        System.out.println("Dati numele utilizatorului de adaugat: ");
        String lastName = sc.nextLine();
        System.out.println("Dati username-ul utilizatorului de adaugat: ");
        String userName = sc.nextLine();
        System.out.println("Dati o parola pentru username: ");
        String password = sc.nextLine();
        this.service.addUser(firstName, lastName, userName, password);
        System.out.println("Utilizatorul a fost adaugat!");
    }

    private void deleteUI() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Dati id-ul utilizatorului de sters: ");
            Long id = sc.nextLong();
            this.service.deleteUser(id);
            System.out.println("Utilizatorul a fost sters!");
        } catch (IllegalArgumentException | NullPointerException e) {
            System.out.println(e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("Format incorect al datelor!");
        }
    }

    private void updateUI() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Dati id-ul utilizatorului de modificat: ");
            Long id = Long.parseLong(sc.nextLine());
            System.out.println("Dati noul prenume al utilizatorului: ");
            String firstName = sc.nextLine();
            System.out.println("Dati noul nume al utilizatorului: ");
            String lastName = sc.nextLine();
            System.out.println("Dati noul username al utilizatorului: ");
            String userName = sc.nextLine();
            System.out.println("Dati noua parola a utilizatorului: ");
            String password = sc.nextLine();
            this.service.updateUser(id, firstName, lastName, userName, password);
            System.out.println("Utilizatorul a fost modificat!");
        } catch (IllegalArgumentException | NullPointerException e) {
            System.out.println(e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("Format incorect al datelor!");
        }
    }

    private void addFriendUI() {
        printAllUI();
        Scanner sc = new Scanner(System.in);
        try {
            System.out.println("Dati id-ul primului utilizator: ");
            Long id1 = sc.nextLong();
            System.out.println("Dati id-ul celui de al doilea utilizator: ");
            Long id2 = sc.nextLong();
            LocalDateTime datenow = LocalDateTime.now();
            this.service.addFriend(id1, id2, datenow);
            System.out.println("Prietenia a fost creata!");
        } catch (IllegalArgumentException | NullPointerException | ValidationException | KeyException e) {
            System.out.println(e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("Format incorect al datelor!");
        }
    }

    private void deleteFriendUI() {
        printAllUI();
        Scanner sc = new Scanner(System.in);
        try {
            System.out.println("Dati id-ul primului utilizator: ");
            Long id1 = sc.nextLong();
            System.out.println("Dati id-ul celui de al doilea utilizator: ");
            Long id2 = sc.nextLong();
            this.service.deleteFriend(id1, id2);
            System.out.println("Prietenia a fost stearsa!");
        } catch (IllegalArgumentException | NullPointerException e) {
            System.out.println(e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("Format incorect al datelor!");
        }
    }

    private void getNrOfConnectedComponentsUI() {
        int nr = this.service.getNrOfConnectedComponents();
        System.out.println("Numarul de comunitati este: " + nr);
    }

    private void getLargestConnectedComponentUI() {
        System.out.println("Cea mai sociabila comunitate este: \n");
        List<Utilizator> largestConnectedComponent = this.service.getLargestConnectedComponent();
        for (Utilizator utilizator : largestConnectedComponent) {
            System.out.println(utilizator);
        }
    }

    private void invitatieUI() {
        System.out.println("Selectati optiunea:\n1.Trimite o cerere de prietenie\n2.Accepta/Refuza o cerere de prietenie");
        this.service.getCereri().forEach(System.out::println);

        System.out.println("\n\nOptiunea:");
        Scanner sc = new Scanner(System.in);
        Integer optiune = sc.nextInt();
        try {
            if (optiune == 1) {
                System.out.println("De la utilizatorul cu id-ul:");
                Long idFrom = sc.nextLong();
                System.out.println("La utilizatorul cu id-ul:");
                Long idTo = sc.nextLong();
                this.service.trimiteCerere(idFrom, idTo);
            } else if (optiune == 2) {
                System.out.println("De la utilizatorul cu id-ul:");
                Long idFrom = sc.nextLong();
                System.out.println("La utilizatorul cu id-ul:");
                Long idTo = sc.nextLong();
                System.out.println("1.Accepta\n2.Refuza");
                Integer optiune2 = sc.nextInt();
                if (optiune2 == 1)
                    this.service.raspundereCerere(idFrom, idTo, true);
                else if (optiune2 == 2)
                    this.service.raspundereCerere(idFrom, idTo, false);

            }
        } catch (KeyException | ValidationException e) {
            System.out.println(e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("Format incorect al datelor!");
        }
    }

    public void prieteniiUnuiUtilizatorUI() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Dati id-ul utilizatorului pentru care doriti prietenii:");
            Long id = sc.nextLong();
            Map<Utilizator, LocalDateTime> prieteniiUnuiUtilizator = this.service.prieteniiUnuiUtilizator(id);
            for (Utilizator u : prieteniiUnuiUtilizator.keySet())
                System.out.println(u.getLastName() + " | " + u.getFirstName() + " | " + prieteniiUnuiUtilizator.get(u).toLocalDate());
        } catch (IllegalArgumentException | NullPointerException e) {
            System.out.println(e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("Format incorect al datelor!");
        }
    }

    public void prieteniiUnuiUtilizatorPerLunaUI() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Dati id-ul utilizatorului pentru care doriti prietenii:");
            Long id = sc.nextLong();
            System.out.println("Dati luna(1-12) pentru care se vor afisa prietenii:");
            int luna = sc.nextInt();
            Map<Utilizator, LocalDateTime> prieteniiUnuiUtilizator = this.service.prieteniiUnuiUtilizatorPerLuna(id, luna);
            for (Utilizator u : prieteniiUnuiUtilizator.keySet())
                System.out.println(u.getLastName() + " | " + u.getFirstName() + " | " + prieteniiUnuiUtilizator.get(u).toLocalDate());
        } catch (IllegalArgumentException | NullPointerException e) {
            System.out.println(e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("Format incorect al datelor!");
        }
    }

    private void printAllUI() {
        Iterable<Utilizator> users = this.service.getUsers();
        users.forEach(System.out::println);
    }

    private void addMessageUI() {
        printAllUI();
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Id-ul utilizatorului from: ");
            Long idFrom = Long.parseLong(sc.nextLine());
            System.out.println("Id-ul utilizatorului to: ");
            Long idTo = Long.parseLong(sc.nextLine());
            System.out.println("Mesajul:");
            String mesaj = sc.nextLine();
            LocalDateTime dateTime = LocalDateTime.now();
            System.out.println("Id-ul mesajului reply: ");
            Long idReply = sc.nextLong();
            this.service.addMessage(idFrom, idTo, mesaj, dateTime, idReply);
            System.out.println("Mesajul a fost adaugat!");
        } catch (ValidationException ve) {
            System.out.println(ve.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("Format incorect al datelor!");
        }
    }

    private void deleteMessageUI() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Dati id-ul mesajului de sters: ");
            Long id = sc.nextLong();
            this.service.deleteMessage(id);
            System.out.println("Mesajul a fost sters!");
        } catch (IllegalArgumentException | NullPointerException e) {
            System.out.println(e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("Format incorect al datelor!");
        }
    }

    private void allMessagesUI() {
        service.getAllMessages().forEach(System.out::println);
    }

    public void conversatiiUI() {
        Scanner S = new Scanner(System.in);
        try {
            System.out.println("id1=: ");
            Long id1;
            id1 = S.nextLong();
            System.out.println("id2=: ");
            Long id2;
            id2 = S.nextLong();
            if (service.getById(id1) == null || service.getById(id2) == null)
                throw new NullPointerException("Utilizatorul trebuie sa existe!");
            Utilizator u1 = service.getById(id1);
            Utilizator u2 = service.getById(id2);

            List<Message> lista = service.conversatii(u1, u2);

            for (Message message : lista) {
                System.out.println("Utilizator from: " + message.getFrom() + " : " + message.getMessage());
            }

        } catch (ValidationException | NullPointerException e) {
            System.out.println(e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("Format incorect al datelor!");
        }
    }

    private void menuPrint() {
        System.out.println("Selectati optiunea: ");
        System.out.println("1. Adaugare utilizator");
        System.out.println("2. Stergere utilizator");
        System.out.println("3. Update utilizator");
        System.out.println("4. Afisare utilizatori");
        System.out.println("5. Adaugare prieten");
        System.out.println("6. Stergere prieten");
        System.out.println("7. Adaugare mesaj");
        System.out.println("8. Stergere mesaj");
        System.out.println("9. Afisare mesaje");
        System.out.println("10. Afisarea prieteniilor unui utilizator dat");
        System.out.println("11. Afisarea prieteniilor unui utilizator dat create intr-o anumita luna");
        System.out.println("12. Determinarea numarului de comunitati");
        System.out.println("13. Determinarea celei mai sociabile comunitati");
        System.out.println("14. Mesajele dintre doi utilizatori");
        System.out.println("15. Trimiterea unei cereri de prietenie sau raspunderea la una");
        System.out.println("0. Iesire");
        System.out.println("-----------------------");
    }

    public void menu() {
        Scanner sc = new Scanner(System.in);
        boolean loop = true;
        while (loop) {
            menuPrint();
            int option = sc.nextInt();
            if (option == 1) {
                saveUI();
            } else if (option == 2) {
                deleteUI();
            } else if (option == 3) {
                updateUI();
            } else if (option == 4) {
                printAllUI();
            } else if (option == 5) {
                addFriendUI();
            } else if (option == 6) {
                deleteFriendUI();
            } else if (option == 7) {
                addMessageUI();
            } else if (option == 8) {
                deleteMessageUI();
            } else if (option == 9) {
                allMessagesUI();
            } else if (option == 10) {
                prieteniiUnuiUtilizatorUI();
            } else if (option == 11) {
                prieteniiUnuiUtilizatorPerLunaUI();
            } else if (option == 12) {
                getNrOfConnectedComponentsUI();
            } else if (option == 13) {
                getLargestConnectedComponentUI();
            } else if (option == 14) {
                conversatiiUI();
            } else if (option == 15) {
                invitatieUI();
            } else if (option == 0) {
                loop = false;
            } else {
                System.out.println("Optiune inexistenta! Reincercati!");
            }
        }
    }

}
