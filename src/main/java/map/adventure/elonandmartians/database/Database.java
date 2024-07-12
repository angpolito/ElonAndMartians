/**
 * @author APS Dev (Angelo Polito, Paolo Scicutella, Simone Pugliese)
 */

package map.adventure.elonandmartians.database;

import map.adventure.elonandmartians.game.Engine;
import map.adventure.elonandmartians.thread.Time;
import map.adventure.elonandmartians.swing.App;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * La classe Database gestisce la connessione al database, la creazione della tabella e il suo aggiornamento.
 */
public class Database {
    private static Connection conn;
    public static final String DB_URL = "jdbc:h2:./src/main/java/map/adventure/elonandmartians/database/leaderboard_db";
    public static final String USER = "sa";
    public static final String PASSWORD = "";

    /**
     * Crea la tabella SCORE se non è stata precedentemente creata.
     *
     * @throws SQLException in caso di errore nella creazione della tabella
     */
    public static void createTable() throws SQLException {
        Connection conn = null;
        Statement stm = null;

        try {
            final String createTable = "CREATE TABLE IF NOT EXISTS SCORE (ID INT PRIMARY KEY, USERNAME VARCHAR(50), "
                    + "TIME VARCHAR(10))";
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);

            stm = conn.createStatement();
            stm.executeUpdate(createTable);
        } catch (SQLException e) {
            throw new RuntimeException("Database creation failed: " + e.getMessage(), e);
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException e) {
                    System.err.println("Failed to close Statement: " + e.getMessage());
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Failed to close Connection: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Aggiorna il database con il punteggio attuale.
     *
     * @throws SQLException in caso di errore nell'aggiornamento del database
     */
    public static void updateDatabase() throws SQLException {
        Connection conn = null;
        Statement stm = null;

        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);

            String time = Time.formatTime(Time.getElapsedTime());
            String username = App.getInstance().inputPlayerName();

            if (username.equals("")) {
                username = "Guest";
            }

            int id = generateID();
            final String insertScore = "INSERT INTO SCORE (ID, USERNAME, TIME) VALUES ('" + id + "', '" + username
                    + "', '" + time + "')";
            stm = conn.createStatement();
            stm.executeUpdate(insertScore);
        } catch (SQLException e) {
            throw new RuntimeException("Database update failed: " + e.getMessage(), e);
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException e) {
                    System.err.println("Failed to close Statement: " + e.getMessage());
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Failed to close Connection: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Restituisce la classifica dei 10 migliori punteggi.
     *
     * @return una stringa con la classifica dei migliori punteggi
     */
    public static String showDatabase() {
        Statement stm = null;
        StringBuilder msg = new StringBuilder();
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            stm = conn.createStatement();
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet tables = metaData.getTables(null, null, "SCORE", null);
            if (tables.next()) {
                ResultSet rs = stm.executeQuery("SELECT * FROM SCORE ORDER BY TIME");
                int num = 1;
                while (rs.next() && num < 11) {
                    msg.append(num + " | " + rs.getString(2) + "\t "
                            + rs.getString(3) + "\n");
                    num++;
                }
                rs.close();
            } else {
                msg.append("Non sono stati registrati ancora vincitori. Prova ad entrare nella"
                        + " classifica ufficiale!");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database update failed: " + e.getMessage(), e);
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException e) {
                    System.err.println("Failed to close Statement: " + e.getMessage());
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Failed to close Connection: " + e.getMessage());
                }
            }
        }
        return msg.toString();
    }

    /**
     * Genera un ID univoco per ogni inserimento nel database.
     *
     * @return un ID univoco per il nuovo punteggio
     */
    public static int generateID() {
        Statement stm = null;
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            stm = conn.createStatement();
            String query = "SELECT MAX(ID) AS MAX_ID FROM SCORE";
            ResultSet rs = stm.executeQuery(query);

            if (rs.next()) {
                int maxId = rs.getInt("MAX_ID");
                return maxId + 1;
            }
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException("Database update failed: " + e.getMessage(), e);
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException e) {
                    System.err.println("Failed to close Statement: " + e.getMessage());
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Failed to close Connection: " + e.getMessage());
                }
            }
        }
        return 1;
    }
}
