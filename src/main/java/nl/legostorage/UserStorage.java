package nl.legostorage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class UserStorage {
    private static final String DEFAULT_DATABASE_URL =
            "jdbc:mysql://127.0.0.1:3306/legoDB?serverTimezone=UTC";

    private Connection connection;

    // Maakt verbinding met de MariaDB-database met instellingen uit omgevingsvariabelen.
    public void init_connectie() throws SQLException {
        String databaseUrl = environmentOrDefault("LEGO_DB_URL", DEFAULT_DATABASE_URL);
        String username = requiredEnvironmentVariable("LEGO_DB_USER");
        String password = requiredEnvironmentVariable("LEGO_DB_PASSWORD");

        connection = DriverManager.getConnection(databaseUrl, username, password);
    }

    // Haalt alle gebruikers op en toont de opgeslagen gegevens in de terminal.
    public void display_all_users() throws SQLException {
        String query = """
                SELECT userID, first_name, last_name, address, address_nr,
                       postal_code, city, country, telefoonnr, email_address
                FROM users
                ORDER BY userID
                """;

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet results = statement.executeQuery()) {
            System.out.printf("%-8s %-18s %-18s %-24s %-10s %-14s %-18s %-18s %-18s %-30s%n",
                    "userID", "Voornaam", "Achternaam", "Adres", "Nr.", "Postcode",
                    "Plaats", "Land", "Telefoon", "E-mailadres");
            System.out.println("-".repeat(180));

            while (results.next()) {
                System.out.printf("%-8d %-18s %-18s %-24s %-10s %-14s %-18s %-18s %-18s %-30s%n",
                        results.getInt("userID"),
                        results.getString("first_name"),
                        results.getString("last_name"),
                        results.getString("address"),
                        results.getString("address_nr"),
                        results.getString("postal_code"),
                        results.getString("city"),
                        results.getString("country"),
                        results.getString("telefoonnr"),
                        results.getString("email_address"));
            }
        }
    }

    // Vraagt de gebruikersgegevens op en voegt ze met een prepared statement toe.
    public void add_user(Scanner scanner) throws SQLException {
        String query = """
                INSERT INTO users
                    (first_name, last_name, address, address_nr, postal_code,
                     city, country, telefoonnr, email_address)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, readRequiredValue(scanner, "Voornaam"));
            statement.setString(2, readRequiredValue(scanner, "Achternaam"));
            statement.setString(3, readRequiredValue(scanner, "Adres"));
            statement.setString(4, readRequiredValue(scanner, "Huisnummer"));
            statement.setString(5, readRequiredValue(scanner, "Postcode"));
            statement.setString(6, readRequiredValue(scanner, "Plaats"));
            statement.setString(7, readRequiredValue(scanner, "Land"));
            statement.setString(8, readRequiredValue(scanner, "Telefoonnummer"));
            statement.setString(9, readRequiredValue(scanner, "E-mailadres"));

            int insertedRows = statement.executeUpdate();
            if (insertedRows != 1) {
                throw new SQLException("De gebruiker is niet toegevoegd.");
            }
        }

        System.out.println("Gebruiker succesvol toegevoegd.");
    }

    // Sluit een eerder geopende databaseverbinding, indien aanwezig.
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    // Leest een niet-lege invoerwaarde; lege waarden worden opnieuw gevraagd.
    private String readRequiredValue(Scanner scanner, String label) {
        while (true) {
            System.out.print(label + ": ");
            String value = scanner.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("Dit veld is verplicht. Probeer het opnieuw.");
        }
    }

    // Geeft een ingestelde omgevingsvariabele terug of gebruikt de standaardwaarde.
    private String environmentOrDefault(String name, String defaultValue) {
        String value = System.getenv(name);
        return value == null || value.isBlank() ? defaultValue : value;
    }

    // Vereist een omgevingsvariabele en geeft een duidelijke fout als deze ontbreekt.
    private String requiredEnvironmentVariable(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Omgevingsvariabele " + name + " ontbreekt. Stel deze in en start opnieuw.");
        }
        return value;
    }
}
