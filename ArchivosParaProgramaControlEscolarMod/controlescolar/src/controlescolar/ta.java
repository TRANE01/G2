package controlescolar;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;

public class ta extends JPanel {
    private Db db;
    private vp mainFrame;
    private JTable table;
    private DefaultTableModel tableModel;
    
    private final Color COLOR_FONDO = new Color(45, 45, 45);
    private final Color COLOR_BOTONES = new Color(80, 80, 80);
    private final Color COLOR_TEXTO = Color.WHITE;
    private final Color COLOR_BORDE = new Color(120, 120, 120);
    private final Color COLOR_TABLA_FONDO = new Color(60, 60, 60);
    private final Color COLOR_TABLA_LINEAS = new Color(100, 100, 100);
    private final Color COLOR_HOVER = new Color(100, 100, 100);
    
    public ta(Db db, vp mainFrame) {
        this.db = db;
        this.mainFrame = mainFrame;
        initializeUI();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(COLOR_FONDO);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(COLOR_FONDO);
        
        JButton backBtn = createStyledButton("Regresar");
        JButton mostrarBtn = createStyledButton("Mostrar tabla");
        JButton editarBtn = createStyledButton("Editar alumno");
        JButton agregarBtn = createStyledButton("Agregar alumno");
        JButton eliminarBtn = createStyledButton("Eliminar alumno");
        
        backBtn.addActionListener(e -> mainFrame.showSelectionPanel());
        mostrarBtn.addActionListener(e -> mostrarTabla());
        editarBtn.addActionListener(e -> mostrarFormularioEditar());
        agregarBtn.addActionListener(e -> mostrarFormularioAgregar());
        eliminarBtn.addActionListener(e -> eliminarAlumno());
        
        buttonPanel.add(backBtn);
        buttonPanel.add(mostrarBtn);
        buttonPanel.add(editarBtn);
        buttonPanel.add(agregarBtn);
        buttonPanel.add(eliminarBtn);
        
        tableModel = new DefaultTableModel(new String[]{"ID", "Nombre", "Apellido", "Matrícula"}, 0);
        table = new JTable(tableModel);
        stylizeTable(table);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(COLOR_TABLA_FONDO);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDE, 1));
        
        add(buttonPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(COLOR_BOTONES);
        button.setForeground(COLOR_TEXTO);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(COLOR_BORDE, 1));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(COLOR_HOVER);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(COLOR_BOTONES);
            }
        });
        
        return button;
    }
    
    private void stylizeTable(JTable table) {
        table.setBackground(COLOR_TABLA_FONDO);
        table.setForeground(COLOR_TEXTO);
        table.setGridColor(COLOR_TABLA_LINEAS);
        table.setSelectionBackground(COLOR_HOVER);
        table.setSelectionForeground(Color.WHITE);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JTableHeader header = table.getTableHeader();
        header.setBackground(COLOR_BOTONES);
        header.setForeground(COLOR_TEXTO);
        header.setFont(new Font("Arial", Font.BOLD, 13));
    }
    
    private void mostrarTabla() {
        try {
            tableModel.setRowCount(0);
            
            Statement st = db.con.createStatement();
            ResultSet rs = st.executeQuery("SELECT id,nombre,apellido,matricula FROM alumnos");
            
            while(rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                int matricula = rs.getInt("matricula");
                
                tableModel.addRow(new Object[]{id, nombre, apellido, matricula});
            }
            
        } catch (Exception e) {
            showError("Error al mostrar estudiantes: " + e.getMessage());
        }
    }
    
    private void mostrarFormularioEditar() {
        String matriculaStr = JOptionPane.showInputDialog(this, "Ingrese la matrícula del alumno a editar:");
        if (matriculaStr == null || matriculaStr.trim().isEmpty()) return;
        
        try {
            int matricula = Integer.parseInt(matriculaStr);
            
            String[] datosActuales = obtenerDatosAlumnoPorMatricula(matricula);
            if (datosActuales == null) {
                showError("No se encontró la matrícula: " + matricula);
                return;
            }
            
            JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
            panel.setBackground(COLOR_FONDO);
            
            JTextField nombreField = new JTextField(datosActuales[0]);
            JTextField apellidoField = new JTextField(datosActuales[1]);
            JTextField matriculaField = new JTextField(datosActuales[2]);
            
            JLabel nombreLabel = new JLabel("Nombre:");
            nombreLabel.setForeground(COLOR_TEXTO);
            panel.add(nombreLabel);
            panel.add(nombreField);
            
            JLabel apellidoLabel = new JLabel("Apellido:");
            apellidoLabel.setForeground(COLOR_TEXTO);
            panel.add(apellidoLabel);
            panel.add(apellidoField);
            
            JLabel matriculaLabel = new JLabel("Matrícula:");
            matriculaLabel.setForeground(COLOR_TEXTO);
            panel.add(matriculaLabel);
            panel.add(matriculaField);
            
            int result = JOptionPane.showConfirmDialog(this, panel, 
                "Editar alumno - Matrícula: " + matricula, 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            
            if (result == JOptionPane.OK_OPTION) {
                String nuevoNombre = nombreField.getText();
                String nuevoApellido = apellidoField.getText();
                int nuevaMatricula = Integer.parseInt(matriculaField.getText());
                
                db.editarAlumnoPorMatricula(matricula, nuevoNombre, nuevoApellido, nuevaMatricula);
                mostrarTabla();
            }
            
        } catch (NumberFormatException e) {
            showError("Matrícula inválida");
        } catch (Exception e) {
            showError("Error: " + e.getMessage());
        }
    }
    
    private String[] obtenerDatosAlumnoPorMatricula(int matricula) {
        try {
            PreparedStatement st = db.con.prepareStatement("SELECT nombre, apellido, matricula FROM alumnos WHERE matricula = ?");
            st.setInt(1, matricula);
            ResultSet rs = st.executeQuery();
            
            if (rs.next()) {
                String nombre = rs.getString("Nombre");
                String apellido = rs.getString("Apellido");
                String matriculaStr = String.valueOf(rs.getInt("Matricula"));
                return new String[]{nombre, apellido, matriculaStr};
            }
        } catch (Exception e) {
            System.out.println("Error al obtener datos del alumno: " + e.getMessage());
        }
        return null;
    }
    
    private void mostrarFormularioAgregar() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.setBackground(COLOR_FONDO);
        
        JTextField nombreField = new JTextField();
        JTextField apellidoField = new JTextField();
        JTextField matriculaField = new JTextField();
        
        JLabel nombreLabel = new JLabel("Nombre:");
        nombreLabel.setForeground(COLOR_TEXTO);
        panel.add(nombreLabel);
        panel.add(nombreField);
        
        JLabel apellidoLabel = new JLabel("Apellido:");
        apellidoLabel.setForeground(COLOR_TEXTO);
        panel.add(apellidoLabel);
        panel.add(apellidoField);
        
        JLabel matriculaLabel = new JLabel("Matrícula:");
        matriculaLabel.setForeground(COLOR_TEXTO);
        panel.add(matriculaLabel);
        panel.add(matriculaField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, 
            "Agregar alumno", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                String nombre = nombreField.getText();
                String apellido = apellidoField.getText();
                int matricula = Integer.parseInt(matriculaField.getText());
                
                db.insertarAlumno(nombre, apellido, matricula);
                mostrarTabla();
            } catch (NumberFormatException e) {
                showError("La matrícula debe ser un número válido");
            }
        }
    }
    
    private void eliminarAlumno() {
        String matriculaStr = JOptionPane.showInputDialog(this, "Ingrese la matrícula del alumno a eliminar:");
        if (matriculaStr == null || matriculaStr.trim().isEmpty()) return;
        
        try {
            int matricula = Integer.parseInt(matriculaStr);
            db.eliminarAlumnoPorMatricula(matricula);
            mostrarTabla();
        } catch (NumberFormatException e) {
            showError("Matrícula inválida");
        }
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}