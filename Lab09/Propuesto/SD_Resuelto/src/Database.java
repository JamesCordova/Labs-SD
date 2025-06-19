import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @web https://www.jc-mouse.net/
 * @author Mouse
 */
public class Database {
    private final static String bd = "test_db";
    private final static String login = "root";
    private final static String password = "root";
    private final static String url = "jdbc:mysql://localhost/" + bd;

    public static Connection getConnection() {
        try {
            // Cargar el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, login, password);
            if (conn != null) {
                System.out.println("Conectado a la base de datos [" + bd + "]");
            }
            return conn;
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error al conectar a la base de datos [" + bd + "]");
            e.printStackTrace();
            return null;
        }
    }
}