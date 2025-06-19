import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TiendaOnlineTransacciones {
    private static final String URL = "jdbc:mysql://localhost:3306/TiendaOnline";
    private static final String USER = "root";
    private static final String PASSWORD = "usuario";

    public static void main(String[] args) {
        Connection conn = null;
        PreparedStatement pstmtClientes = null;
        PreparedStatement pstmtPedidos = null;

        try {
            // Establecer la conexión
            conn = DriverManager.getConnection(URL, USER, PASSWORD);

            // Desactivar el modo de autocommit
            conn.setAutoCommit(false);

            // Insertar un cliente (ejemplo)
            String sqlClientes = "INSERT INTO Clientes (Nombre, Email, FechaRegistro) VALUES (?, ?, ?)";
            pstmtClientes = conn.prepareStatement(sqlClientes, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmtClientes.setString(1, "Juan Pérez");
            pstmtClientes.setString(2, "juan.perez@example.com");
            pstmtClientes.setDate(3, new java.sql.Date(System.currentTimeMillis()));
            pstmtClientes.executeUpdate();

            // Obtener el ID generado del cliente
            long clienteID = -1;
            try (java.sql.ResultSet generatedKeys = pstmtClientes.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    clienteID = generatedKeys.getLong(1);
                } else {
                    throw new SQLException("Fallo al obtener el ID del cliente.");
                }
            }

            // Insertar en la tabla Pedidos
            String sqlPedidos = "INSERT INTO Pedidos (ClienteID, FechaPedido, Total) VALUES (?, ?, ?)";
            pstmtPedidos = conn.prepareStatement(sqlPedidos);
            pstmtPedidos.setLong(1, clienteID); // Usar el ID del cliente insertado anteriormente
            pstmtPedidos.setDate(2, new java.sql.Date(System.currentTimeMillis()));
            pstmtPedidos.setBigDecimal(3, new java.math.BigDecimal("100.00"));
            pstmtPedidos.executeUpdate();

            // Hacer commit de la transacción
            conn.commit();
            System.out.println("Transacción completada exitosamente.");

        } catch (SQLException e) {
            // Manejo de excepciones y rollback en caso de error
            if (conn != null) {
                try {
                    System.err.print("Transacción fallida. Haciendo rollback...");
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            // Cerrar recursos
            if (pstmtClientes != null) {
                try {
                    pstmtClientes.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (pstmtPedidos != null) {
                try {
                    pstmtPedidos.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

