package controlescolar;

import java.sql.*;

public class Db {
    Connection con;
    
    public Db(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con=DriverManager.getConnection("jdbc:mysql://localhost:3306/ce","root","");
        } catch (Exception e){
            System.out.println("Error"+e.getMessage());
        }
    }
    public void mostrarEstudiantes(){
        Statement st;
        ResultSet rs;
        
        try{    
            st=con.createStatement();
            rs=st.executeQuery("Select id,nombre,apellido,matricula FROM alumnos");
            int id,matricula;
            String nombre,apellido;
            
            while(rs.next()){
                id=rs.getInt("id");
                nombre=rs.getString("nombre");
                apellido=rs.getString("apellido");
                matricula=rs.getInt("matricula");
                System.out.println("Id:"+id+" "+nombre+" "+apellido+" - "+matricula);
            }
            
        } catch (Exception e){
            System.out.println("Error al mostrar estudiantes: " + e.getMessage());
        }
    }
    public void insertarAlumno(String nombre, String apellido, int matricula){
        try{
            PreparedStatement stmt=con.prepareStatement("INSERT INTO alumnos (nombre, apellido, matricula) VALUES (?,?,?)");
            stmt.setString(1,nombre);
            stmt.setString(2,apellido);
            stmt.setInt(3,matricula);
            stmt.executeUpdate();
            System.out.println("Alumno insertado correctamente");
        } catch (SQLException e){
            System.out.println("Error al insertar alumno: "+e);
        }
    }
    public void editarAlumno(int id, String nombre, String apellido, int matricula){
        try{
            PreparedStatement stmt=con.prepareStatement("UPDATE alumnos SET nombre=?, apellido=?, matricula=? WHERE id=?");
            stmt.setString(1,nombre);
            stmt.setString(2,apellido);
            stmt.setInt(3,matricula);
            stmt.setInt(4,id);
            int filasAfectadas = stmt.executeUpdate();
            
            if(filasAfectadas > 0){
                System.out.println("Alumno editado correctamente");
            } else {
                System.out.println("No se encontró el alumno con ID: " + id);
            }
        } catch (SQLException e){
            System.out.println("Error al editar alumno: "+e);
        }
    }
    
    public void mostrarProfesores(){
        Statement st;
        ResultSet rs;
        
        try{    
            st=con.createStatement();
            rs=st.executeQuery("Select id,nombre,apellido,numero_empleado FROM profesores");
            int id,numero_empleado;
            String nombre,apellido;
            
            System.out.println("\n--- LISTA DE PROFESORES ---");
            while(rs.next()){
                id=rs.getInt("id");
                nombre=rs.getString("nombre");
                apellido=rs.getString("apellido");
                numero_empleado=rs.getInt("numero_empleado");
                System.out.println("Id:"+id+" "+nombre+" "+apellido+" - "+numero_empleado);
            }
            
        } catch (Exception e){
            System.out.println("Error al mostrar profesores: " + e.getMessage());
        }
    }
    
    public void editarProfesor(int id, String nombre, String apellido, int numero_empleado){
        try{
            PreparedStatement stmt=con.prepareStatement("UPDATE profesores SET nombre=?, apellido=?, numero_empleado=? WHERE id=?");
            stmt.setString(1,nombre);
            stmt.setString(2,apellido);
            stmt.setInt(3,numero_empleado);
            stmt.setInt(4,id);
            int filasAfectadas = stmt.executeUpdate();
            
            if(filasAfectadas > 0){
                System.out.println("Profesor editado correctamente");
            } else {
                System.out.println("No se encontró el profesor con ID: " + id);
            }
        } catch (SQLException e){
            System.out.println("Error al editar profesor: "+e);
        }
    }
    
    // Insertar profesor (NUEVA FUNCIÓN)
    public void insertarProfesor(String nombre, String apellido, int numero_empleado){
        try{
            PreparedStatement stmt=con.prepareStatement("INSERT INTO profesores (nombre, apellido, numero_empleado) VALUES (?,?,?)");
            stmt.setString(1,nombre);
            stmt.setString(2,apellido);
            stmt.setInt(3,numero_empleado);
            stmt.executeUpdate();
            System.out.println("Profesor insertado correctamente");
        } catch (SQLException e){
            System.out.println("Error al insertar profesor: "+e);
        }
    }
}