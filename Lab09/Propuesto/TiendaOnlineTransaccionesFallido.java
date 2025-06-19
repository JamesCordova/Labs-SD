import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TiendaOnlineTransaccionesFallido {
    private static final String URL = "jdbc:mysql://localhost:3306/TiendaOnline";
    private static final String USER = "root";
    private static final String PASSWORD = "usuario";

    public static void main(String[] args) {
        Connection conn = null;
        PreparedStatement pstmtPedidos = null;
        PreparedStatement pstmtDetallesPedido = null;

        try {
            // Establecer la conexión
            conn = DriverManager.getConnection(URL, USER, PASSWORD);

            // Desactivar el modo de autocommit
            conn.setAutoCommit(false);

            // Transacción 1: Insertar en la tabla Pedidos
            String sqlPedidos1 = "INSERT INTO Pedidos (ClienteID, FechaPedido, Total) VALUES (?, ?, ?)";
            pstmtPedidos = conn.prepareStatement(sqlPedidos1, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmtPedidos.setInt(1, 1); // ClienteID
            pstmtPedidos.setDate(2, new java.sql.Date(System.currentTimeMillis())); // FechaPedido
            pstmtPedidos.setBigDecimal(3, new java.math.BigDecimal("100.00")); // Total
            pstmtPedidos.executeUpdate();

            // Obtener el ID generado del primer pedido
            long pedidoID1 = -1;
            try (java.sql.ResultSet generatedKeys = pstmtPedidos.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    pedidoID1 = generatedKeys.getLong(1);
                } else {
                    throw new SQLException("Fallo al obtener el ID del primer pedido.");
                }
            }

            // Transacción 2: Insertar en la tabla DetallesPedido (exitosa)
            String sqlDetallesPedido1 = "INSERT INTO DetallesPedido (PedidoID, ProductoID, Cantidad, Precio) VALUES (?, ?, ?, ?)";
            pstmtDetallesPedido = conn.prepareStatement(sqlDetallesPedido1);
            pstmtDetallesPedido.setLong(1, pedidoID1); // PedidoID
            pstmtDetallesPedido.setInt(2, 1); // ProductoID
            pstmtDetallesPedido.setInt(3, 2); // Cantidad
            pstmtDetallesPedido.setBigDecimal(4, new java.math.BigDecimal("50.00")); // Precio
            pstmtDetallesPedido.executeUpdate();

            // Transacción 3: Insertar en la tabla Pedidos nuevamente (exitosa)
            String sqlPedidos2 = "INSERT INTO Pedidos (ClienteID, FechaPedido, Total) VALUES (?, ?, ?)";
            pstmtPedidos = conn.prepareStatement(sqlPedidos2, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmtPedidos.setInt(1, 2); // ClienteID
            pstmtPedidos.setDate(2, new java.sql.Date(System.currentTimeMillis())); // FechaPedido
            pstmtPedidos.setBigDecimal(3, new java.math.BigDecimal("150.00")); // Total
            pstmtPedidos.executeUpdate();

            // Obtener el ID generado del segundo pedido
            long pedidoID2 = -1;
            try (java.sql.ResultSet generatedKeys = pstmtPedidos.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    pedidoID2 = generatedKeys.getLong(1);
                } else {
                    throw new SQLException("Fallo al obtener el ID del segundo pedido.");
                }
            }

            // Transacción 4: Intentar insertar en DetallesPedido con un PedidoID inexistente (fallida)
            String sqlDetallesPedido2 = "INSERT INTO DetallesPedido (PedidoID, ProductoID, Cantidad, Precio) VALUES (?, ?, ?, ?)";
            pstmtDetallesPedido = conn.prepareStatement(sqlDetallesPedido2);
            pstmtDetallesPedido.setLong(1, pedidoID1 + pedidoID2); // Intentamos usar un PedidoID inexistente
            pstmtDetallesPedido.setInt(2, 2); // ProductoID
            pstmtDetallesPedido.setInt(3, 3); // Cantidad
            pstmtDetallesPedido.setBigDecimal(4, new java.math.BigDecimal("75.00")); // Precio
            pstmtDetallesPedido.executeUpdate();

            conn.commit(); // Esto no debería ejecutarse si hay una excepción anterior

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
            if (pstmtPedidos != null) {
                try {
                    pstmtPedidos.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (pstmtDetallesPedido != null) {
                try {
                    pstmtDetallesPedido.close();
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


