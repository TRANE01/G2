package controlescolar;

import java.sql.*;

public class Db {
    Connection con;
    
  public Db(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con=DriverManager.getConnection("jdbc:mysql://localhost:3306/ce","root",""); //Aqui debo modificar my acceso mySQL
        } catch (Exception e){
            System.out.println("Error"+e.getMessage());
        }
    }
    
    public boolean existeMatricula(int matricula) {
        try {
            PreparedStatement st = con.prepareStatement("SELECT COUNT(*) FROM alumnos WHERE matricula = ?");
            st.setInt(1, matricula);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            System.out.println("Error al verificar matrícula: " + e.getMessage());
        }
        return false;
    }
    
    public void mostrarEstudiantes(){
        Statement st;
        ResultSet rs;
        
        try{    
            st=con.createStatement();
            rs=st.executeQuery("SELECT id,nombre,apellido,matricula FROM alumnos");
            int id,matricula;
            String nombre,apellido;
            
            while(rs.next()){
                id=rs.getInt("Id");
                nombre=rs.getString("Nombre");
                apellido=rs.getString("Apellido");
                matricula=rs.getInt("Matricula");
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
            stmt.executeUpdate();
        } catch (SQLException e){
            System.out.println("Error al editar alumno: "+e);
        }
    }
    
    public void editarAlumnoPorMatricula(int matricula, String nombre, String apellido, int nuevaMatricula){
        try{
            PreparedStatement stmt=con.prepareStatement("UPDATE alumnos SET nombre=?, apellido=?, matricula=? WHERE matricula=?");
            stmt.setString(1,nombre);
            stmt.setString(2,apellido);
            stmt.setInt(3,nuevaMatricula);
            stmt.setInt(4,matricula);
            int filasAfectadas = stmt.executeUpdate();
            System.out.println("Filas afectadas al editar: " + filasAfectadas);
        } catch (SQLException e){
            System.out.println("Error al editar alumno: "+e.getMessage());
        }
    }
    
    public boolean eliminarAlumno(int id) {
        try {
            PreparedStatement stmt = con.prepareStatement("DELETE FROM alumnos WHERE id = ?");
            stmt.setInt(1, id);
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar alumno: " + e.getMessage());
            return false;
        }
    }
    
    public void eliminarAlumnoPorMatricula(int matricula) {
        try {
            PreparedStatement stmt = con.prepareStatement("DELETE FROM alumnos WHERE matricula = ?");
            stmt.setInt(1, matricula);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al eliminar alumno: " + e.getMessage());
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
            
            while(rs.next()){
                id=rs.getInt("Id");
                nombre=rs.getString("Nombre");
                apellido=rs.getString("Apellido");
                numero_empleado=rs.getInt("Número de empleado");
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
            stmt.executeUpdate();
        } catch (SQLException e){
            System.out.println("Error al editar profesor: "+e);
        }
    }
    
    public void editarProfesorPorNumero(int numeroEmpleado, String nombre, String apellido, int nuevoNumero){
        try{
            PreparedStatement stmt=con.prepareStatement("UPDATE profesores SET nombre=?, apellido=?, numero_empleado=? WHERE numero_empleado=?");
            stmt.setString(1,nombre);
            stmt.setString(2,apellido);
            stmt.setInt(3,nuevoNumero);
            stmt.setInt(4,numeroEmpleado);
            stmt.executeUpdate();
        } catch (SQLException e){
            System.out.println("Error al editar profesor: "+e);
        }
    }
    
    public void insertarProfesor(String nombre, String apellido, int numero_empleado){
        try{
            PreparedStatement stmt=con.prepareStatement("INSERT INTO profesores (nombre, apellido, numero_empleado) VALUES (?,?,?)");
            stmt.setString(1,nombre);
            stmt.setString(2,apellido);
            stmt.setInt(3,numero_empleado);
            stmt.executeUpdate();
        } catch (SQLException e){
            System.out.println("Error al insertar profesor: "+e);
        }
    }
    
    public boolean eliminarProfesor(int id) {
        try {
            PreparedStatement stmt = con.prepareStatement("DELETE FROM profesores WHERE id = ?");
            stmt.setInt(1, id);
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar profesor: " + e.getMessage());
            return false;
        }
    }
    
    public void eliminarProfesorPorNumero(int numeroEmpleado) {
        try {
            PreparedStatement stmt = con.prepareStatement("DELETE FROM profesores WHERE numero_empleado = ?");
            stmt.setInt(1, numeroEmpleado);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al eliminar profesor: " + e.getMessage());
        }
    }
}