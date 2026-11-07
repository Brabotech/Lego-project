package nl.legostorage;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        UserStorage storage = new UserStorage();

        // Start de applicatie alleen als er verbinding met de database gemaakt kan worden.
        try {
            storage.init_connectie();
        } catch (SQLException | IllegalStateException exception) {
            System.err.println("Kan geen verbinding maken met legoDB: " + exception.getMessage());
            return;
        }

        // Houd de terminalinvoer open zolang het hoofdmenu actief is.
        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;

            // Toon na iedere actie opnieuw het menu totdat de gebruiker afsluit.
            while (running) {
                displayMenu();
                String choice = scanner.nextLine().trim();

                // Voer de gekozen gebruikersactie uit en meld databasefouten expliciet.
                try {
                    switch (choice) {
                        case "1" -> storage.display_all_users();
                        case "2" -> storage.add_user(scanner);
                        case "3" -> running = false;
                        default -> System.out.println("Ongeldige keuze. Kies 1, 2 of 3.");
                    }
                } catch (SQLException exception) {
                    System.err.println("Databasefout: " + exception.getMessage());
                }
            }
        } finally {
            // Sluit de databaseverbinding netjes af wanneer het programma stopt.
            try {
                storage.close();
            } catch (SQLException exception) {
                System.err.println("Kan de databaseverbinding niet sluiten: " + exception.getMessage());
            }
        }

        System.out.println("Programma afgesloten.");
    }

    // Toont de drie beschikbare menuopties.
    private static void displayMenu() {
        System.out.println();
        System.out.println("=== LEGO-opslagsysteem ===");
        System.out.println("1. display_all_users");
        System.out.println("2. add_user");
        System.out.println("3. exit");
        System.out.print("Kies een optie: ");
    }
}
