# LegoStorageSystem

Een Java-terminalapplicatie voor het beheren van gebruikers in de MariaDB-database
`legoDB`.

## Benodigdheden

- Java 25 (LTS) of hoger
- Maven
- MariaDB-server

## Database voorbereiden

Start MariaDB en maak de database en tabel aan met een account dat databases en
tabellen mag aanmaken:

```sh
sudo service mariadb start
sudo mariadb < src/main/resources/schema.sql
```

Het schema maakt `legoDB` en de tabel `users` aan. De kolom `userID` wordt
automatisch gevuld.

Maak daarna een applicatieaccount aan. Vervang `kies-een-sterk-wachtwoord` door
een eigen wachtwoord:

```sh
sudo mariadb
```

```sql
CREATE USER 'lego_app'@'127.0.0.1' IDENTIFIED BY 'kies-een-sterk-wachtwoord';
GRANT SELECT, INSERT ON legoDB.* TO 'lego_app'@'127.0.0.1';
```

## Databaseverbinding instellen

De applicatie gebruikt deze omgevingsvariabelen:

- `LEGO_DB_USER`: MariaDB-gebruikersnaam
- `LEGO_DB_PASSWORD`: MariaDB-wachtwoord
- `LEGO_DB_URL`: optioneel; standaard is
  `jdbc:mysql://127.0.0.1:3306/legoDB?serverTimezone=UTC`

Stel de waarden in je eigen terminal in; plaats wachtwoorden niet in de broncode:

```sh
export LEGO_DB_USER='lego_app'
export LEGO_DB_PASSWORD='jouw-mariadb-wachtwoord'
```

## Starten

```sh
mvn compile exec:java
```

Het menu biedt de opties `display_all_users`, `add_user` en `exit`.