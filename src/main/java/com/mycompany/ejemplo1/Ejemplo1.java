
package com.mycompany.ejemplo1;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.*;
import java.util.Scanner;

class BaseDeDatos {
    static final String url = "jdbc:postgresql://localhost:5432/universidad";
    static final String usuario = "postgres";
    static final String contraseña = "postgres"; 

    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(url, usuario, contraseña);
    }
}

public class Ejemplo1 {
    private static final Logger LOGGER = Logger.getLogger(Ejemplo1.class.getName());
    public static void main(String[] args) {
        Scanner escaner = new Scanner(System.in);
        while (true) {
            System.out.println("1. Agregar Estudiante\n2. Ver Estudiantes\n3. Actualizar Estudiante\n4. Eliminar Estudiante\n5. Salir");
            int opcion = escaner.nextInt();
            escaner.nextLine(); // Consumir nueva línea

            switch (opcion) {
                case 1:
                    agregarEstudiante(escaner);
                    break;
                case 2:
                    verEstudiantes();
                    break;
                case 3:
                    actualizarEstudiante(escaner);
                    break;
                case 4:
                    eliminarEstudiante(escaner);
                    break;
                case 5:
                    System.out.println("Saliendo...");
                    escaner.close();
                    return;
                default:
                    System.out.println("Opción no válida");
            }
        }
    }

    public static void agregarEstudiante(Scanner escaner) {
        System.out.print("Ingrese el nombre del estudiante: ");
        String nombre = escaner.nextLine();
        System.out.print("Ingrese la edad del estudiante: ");
        int edad = escaner.nextInt();
        escaner.nextLine(); 

        String consulta = "INSERT INTO estudiantes (nombre, edad) VALUES (?, ?)";

        try (Connection conexion = BaseDeDatos.obtenerConexion();
             PreparedStatement declaracion = conexion.prepareStatement(consulta)) {

            declaracion.setString(1, nombre);
            declaracion.setInt(2, edad);
            declaracion.executeUpdate();
            System.out.println("Estudiante agregado exitosamente.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al agregar estudiante: {0}", e.getMessage());
        }
    }

    public static void verEstudiantes() {
        Connection cnx = obtenerConexion();
        if (cnx != null) {
            try {
                Statement declaracion = cnx.createStatement();
                ResultSet resultado = declaracion.executeQuery("SELECT * FROM estudiantes");
                while (resultado.next()) {
                    System.out.println(resultado.getInt("id") + " - " + resultado.getString("nombre") + " - " + resultado.getInt("edad"));
                }
                declaracion.close();
                cnx.close(); 
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    public static void actualizarEstudiante(Scanner escaner) {
        System.out.print("Ingrese el ID del estudiante a actualizar: ");
        int id = escaner.nextInt();
        escaner.nextLine();
        System.out.print("Ingrese el nuevo nombre: ");
        String nombre = escaner.nextLine();
        System.out.print("Ingrese la nueva edad: ");
        int edad = escaner.nextInt();
        escaner.nextLine();

        Connection cnx = obtenerConexion();
        if (cnx != null) {
            try {
                Statement declaracion = cnx.createStatement();
                String sql = "UPDATE estudiantes SET nombre='" + nombre + "', edad=" + edad + " WHERE id=" + id;
                declaracion.executeUpdate(sql); 
                System.out.println("Estudiante actualizado.");
                declaracion.close();
                cnx.close();
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    public static void eliminarEstudiante(Scanner escaner) {
        System.out.print("Ingrese el ID del estudiante a eliminar: ");
        int id = escaner.nextInt();
        escaner.nextLine();

        Connection cnx = obtenerConexion();
        if (cnx != null) {
            try {
                Statement declaracion = cnx.createStatement();
                String sql = "DELETE FROM estudiantes WHERE id=" + id;
                declaracion.executeUpdate(sql); 
                System.out.println("Estudiante eliminado.");
                declaracion.close();
                cnx.close();
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
