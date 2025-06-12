import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {
	private static final String URL = "jdbc:mysql://localhost:3306/empresa";
	private static final String USER = "root";
	private static final String PASSWORD = "usuario";
	private Connection connection;

	public DatabaseConnection() {
		try {
			connection = DriverManager.getConnection(URL, USER, PASSWORD);
			System.out.println("Conexion a la base de datos establecida.");
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error al conectar a la base de datos.");
		}
	}

	public Connection getConnection() {
		return connection;
	}

	public void closeConnection() {
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
				System.out.println("Conexión a la base de datos cerrada.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error al cerrar la conexión a la base de datos.");
		}
	}

	// Métodos para Departamentos

	public void insertDepartment(String id, String name, String phone, String fax) {
		String query = "INSERT INTO Departamentos (IdDepartamento, Nombre, Telefono, Fax) VALUES (?, ?, ?, ?)";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, id);
			statement.setString(2, name);
			statement.setString(3, phone);
			statement.setString(4, fax);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error al insertar departamento.");
		}
	}

	public void deleteDepartment(String id) {
		String query = "DELETE FROM Departamentos WHERE IdDepartamento = ?";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, id);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error al eliminar departamento.");
		}
	}

	public void updateDepartment(String oldId, String newId, String name, String phone, String fax) {
		String query = "UPDATE Departamentos SET IdDepartamento = ?, Nombre = ?, Telefono = ?, Fax = ? WHERE IdDepartamento = ?";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, newId); // Usar el nuevo ID (puede ser el mismo que el antiguo)
			statement.setString(2, name);
			statement.setString(3, phone);
			statement.setString(4, fax);
			statement.setString(5, oldId); // Buscar por el ID antiguo en la base de datos
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error al actualizar departamento.");
		}
	}

	public List<String[]> getDepartments() {
		List<String[]> departments = new ArrayList<>();
		String query = "SELECT * FROM Departamentos";
		try (PreparedStatement statement = connection.prepareStatement(query);
				ResultSet resultSet = statement.executeQuery()) {
			while (resultSet.next()) {
				String id = resultSet.getString("IdDepartamento");
				String name = resultSet.getString("Nombre");
				String phone = resultSet.getString("Telefono");
				String fax = resultSet.getString("Fax");
				departments.add(new String[] { id, name, phone, fax });
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error al obtener Departamentos.");
		}
		return departments;
	}

	public boolean departmentExists(String idDepartamento) {
		String query = "SELECT COUNT(*) FROM Departamentos WHERE IdDepartamento = ?";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, idDepartamento);
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					return resultSet.getInt(1) > 0;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error al verificar si el departamento existe.");
		}
		return false;
	}

	// Métodos para Proyectos

	public void insertProject(String id, String name, String startDate, String endDate, String deptId) {
		String query = "INSERT INTO Proyectos (IdProyecto, Nombre, FechaInicio, FechaFin, IdDepartamento) VALUES (?, ?, ?, ?, ?)";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, id);
			statement.setString(2, name);
			statement.setString(3, startDate);
			statement.setString(4, endDate);
			statement.setString(5, deptId);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error al insertar proyecto.");
		}
	}

	public void deleteProject(String id) {
		String query = "DELETE FROM Proyectos WHERE IdProyecto = ?";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, id);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error al eliminar proyecto.");
		}
	}

	public void updateProject(String idProyecto, String nombre, String fechaInicio, String fechaFin,
			String idDepartamento) {
		String query = "UPDATE Proyectos SET Nombre=?, FechaInicio=?, FechaFin=?, IdDepartamento=? WHERE IdProyecto=?";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, nombre);
			statement.setDate(2, Date.valueOf(fechaInicio));
			statement.setDate(3, Date.valueOf(fechaFin));
			statement.setString(4, idDepartamento);
			statement.setString(5, idProyecto);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error al actualizar proyecto.");
		}
	}

	public List<String[]> getProjects() {
		List<String[]> projects = new ArrayList<>();
		String query = "SELECT * FROM Proyectos";
		try (PreparedStatement statement = connection.prepareStatement(query);
				ResultSet resultSet = statement.executeQuery()) {
			while (resultSet.next()) {
				String id = resultSet.getString("IdProyecto");
				String name = resultSet.getString("Nombre");
				String startDate = resultSet.getString("FechaInicio");
				String endDate = resultSet.getString("FechaFin");
				String deptId = resultSet.getString("IdDepartamento");
				projects.add(new String[] { id, name, startDate, endDate, deptId });
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error al obtener Proyectos.");
		}
		return projects;
	}

	public boolean projectExists(String idProyecto) {
		String query = "SELECT COUNT(*) FROM Proyectos WHERE IdProyecto = ?";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, idProyecto);
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					return resultSet.getInt(1) > 0;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error al verificar si el proyecto existe.");
		}
		return false;
	}

	public List<String> getProjectsByDepartment(String departmentId) {
		List<String> projects = new ArrayList<>();
		String query = "SELECT IdProyecto FROM Proyectos WHERE IdDepartamento = ?";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, departmentId);
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					String projectId = resultSet.getString("IdProyecto");
					projects.add(projectId);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error al obtener Proyectos por departamento.");
		}
		return projects;
	}
	
	
	// Metodos para las acciones de Ingenieros

	public List<String[]> getEngineers() {
		List<String[]> engineers = new ArrayList<>();
		String query = "SELECT * FROM Ingenieros";
		try (PreparedStatement statement = connection.prepareStatement(query);
				ResultSet resultSet = statement.executeQuery()) {
			while (resultSet.next()) {
				String id = resultSet.getString("IdIngeniero");
				String name = resultSet.getString("Nombre");
				String specialty = resultSet.getString("Especialidad");
				String post = resultSet.getString("Cargo");
				engineers.add(new String[] { id, name, specialty, post });
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error al obtener Ingenieros.");
		}
		return engineers;
	}

	public void insertEngineers(String id, String name, String post, String specialty) {
		String query = "INSERT INTO Ingenieros (IdIngeniero, Nombre, Especialidad, Cargo) VALUES (?, ?, ?, ?)";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, id);
			statement.setString(2, name);
			statement.setString(3, specialty);
			statement.setString(4, post);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error al insertar ingeniero.");
		}
	}

	public boolean engineerExists(String id) {
		String query = "SELECT COUNT(*) FROM Ingenieros WHERE IdIngeniero = ?";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, id);
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					return resultSet.getInt(1) > 0;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error al verificar si el ingeniero existe.");
		}
		return false;
	}

	public List<String> getAssignmentByEngineer(String id) {
		List<String> assignments = new ArrayList<>();
		String query = "SELECT FechaAsignacion FROM Asignaciones WHERE IdIngeniero = ?";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, id);
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					String assignmentDate = resultSet.getString("FechaAsignacion");
					assignments.add(assignmentDate);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error al obtener Proyectos por ingeniero.");
		}
		return assignments;
	}

	public void deleteEngineer(String id) {
		String query = "DELETE FROM Ingenieros WHERE IdIngeniero = ?";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, id);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error al eliminar ingeniero.");
		}
	}

	public void updateEngineer(String oldId, String newId, String name, String post, String specialty) {
		String query = "UPDATE Ingenieros SET IdIngeniero = ?, Nombre = ?, Especialidad = ?, Cargo = ? WHERE IdIngeniero = ?";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, newId); // Usar el nuevo ID (puede ser el mismo que el antiguo)
			statement.setString(2, name);
			statement.setString(3, specialty);
			statement.setString(4, post);
			statement.setString(5, oldId); // Buscar por el ID antiguo en la base de datos
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error al actualizar ingeniero.");
		}		
	}

	// Metodos para las acciones de Ingenieros	
	
	public List<String[]> getAssignments() {
		List<String[]> assignments = new ArrayList<>();
		String query = "SELECT * FROM Asignaciones";
		try (PreparedStatement statement = connection.prepareStatement(query);
				ResultSet resultSet = statement.executeQuery()) {
			while (resultSet.next()) {
				String idProyect = resultSet.getString("IdProyecto");
				String idEngineer = resultSet.getString("IdIngeniero");
				String fecha = resultSet.getString("FechaAsignacion");
				assignments.add(new String[] { idProyect, idEngineer, fecha });
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error al obtener Asignaciones.");
		}
		return assignments;
	}

	public void insertAssignments(String idProyect, String idEngineer, String fecha) {
		String query = "INSERT INTO Asignaciones (IdProyecto, IdIngeniero, FechaAsignacion) VALUES (?, ?, ?)";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, idProyect);
			statement.setString(2, idEngineer);
			statement.setString(3, fecha);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error al insertar asignacion.");
		}		
	}

	public void deleteAssignment(String id) {
		String query = "DELETE FROM Asignaciones WHERE IdProyecto = ?";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, id);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error al eliminar la asignación.");
		}		
	}

	public void updateAssignment(String oldId, String newId, String idEngineer, String fecha) {
		String query = "UPDATE Asignaciones SET IdProyecto = ?, IdIngeniero = ?, FechaAsignacion = ? WHERE IdProyecto = ?";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, newId); // Usar el nuevo ID (puede ser el mismo que el antiguo)
			statement.setString(2, idEngineer);
			statement.setString(3, fecha);
			statement.setString(4, oldId);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error al actualizar la asignacion.");
		}				
	}

	public boolean assignmentExists(String newId) {
		String query = "SELECT COUNT(*) FROM Asignaciones WHERE IdProyecto = ?";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, newId);
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					return resultSet.getInt(1) > 0;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error al verificar si la asignación existe.");
		}
		return false;
	}
}