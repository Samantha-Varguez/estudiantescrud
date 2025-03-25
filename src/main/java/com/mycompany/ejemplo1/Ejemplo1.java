

package com.mycompany.ejemplo1;

import java.sql.*;
import java.util.Scanner;

public class Ejemplo1 {
    static final String url = "jdbc:postgresql://localhost:5432/universidad";
    static final String usuario = "postgres";
    static final String contraseña = "postgres"; 

    public static void main(String[] args) {
        Scanner escaner = new Scanner(System.in);
        while (true) {
            System.out.println("1. Agregar Estudiante\n2. Ver Estudiantes\n3. Actualizar Estudiante\n4. Eliminar Estudiante\n5. Salir");
            int opcion = escaner.nextInt();
            escaner.nextLine(); // Consumir nueva línea

            if (opcion == 1) {
                agregarEstudiante(escaner);
            } else if (opcion == 2) {
                verEstudiantes();
            } else if (opcion == 3) {
                actualizarEstudiante(escaner);
            } else if (opcion == 4) {
                eliminarEstudiante(escaner);
            } else if (opcion == 5) {
                break;
            } else {
                System.out.println("Opción no válida");
            }
        }
        escaner.close();
    }

    public static Connection obtenerConexion() {
        try {
            return DriverManager.getConnection(url, usuario, contraseña);
        } catch (SQLException e) {
            System.out.println("Error de conexión: " + e.getMessage());
            return null; 
        }
    }

    public static void agregarEstudiante(Scanner escaner) {
        System.out.print("Ingrese el nombre del estudiante: ");
        String nombre = escaner.nextLine();
        System.out.print("Ingrese la edad del estudiante: ");
        int edad = escaner.nextInt();
        escaner.nextLine(); // Consumir nueva línea

        Connection cnx = obtenerConexion();
        if (cnx != null) {
            try {
                Statement declaracion = cnx.createStatement();
                String sql = "INSERT INTO estudiantes (nombre, edad) VALUES ('" + nombre + "', " + edad + ")";
                declaracion.executeUpdate(sql); // 🔴 Inyección SQL
                System.out.println("Estudiante agregado.");
                declaracion.close();
                cnx.close();
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
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
                declaracion.executeUpdate(sql); // 🔴 Inyección SQL
                System.out.println("Estudiante eliminado.");
                declaracion.close();
                cnx.close();
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
