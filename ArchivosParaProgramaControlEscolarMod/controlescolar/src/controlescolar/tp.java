package controlescolar;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;

public class tp extends JPanel {
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
    
    public tp(Db db, vp mainFrame) {
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
        JButton editarBtn = createStyledButton("Editar profesor");
        JButton agregarBtn = createStyledButton("Agregar profesor");
        JButton eliminarBtn = createStyledButton("Eliminar profesor");
        
        backBtn.addActionListener(e -> mainFrame.showSelectionPanel());
        mostrarBtn.addActionListener(e -> mostrarTabla());
        editarBtn.addActionListener(e -> mostrarFormularioEditar());
        agregarBtn.addActionListener(e -> mostrarFormularioAgregar());
        eliminarBtn.addActionListener(e -> eliminarProfesor());
        
        buttonPanel.add(backBtn);
        buttonPanel.add(mostrarBtn);
        buttonPanel.add(editarBtn);
        buttonPanel.add(agregarBtn);
        buttonPanel.add(eliminarBtn);
        
        tableModel = new DefaultTableModel(new String[]{"ID", "Nombre", "Apellido", "Número de empleado"}, 0);
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
            ResultSet rs = st.executeQuery("SELECT id,nombre,apellido,numero_empleado FROM profesores");
            
            while(rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                int numeroEmpleado = rs.getInt("numero_empleado");
                
                tableModel.addRow(new Object[]{id, nombre, apellido, numeroEmpleado});
            }
            
        } catch (Exception e) {
            showError("Error al mostrar profesores: " + e.getMessage());
        }
    }
    
    private void mostrarFormularioEditar() {
        String numeroStr = JOptionPane.showInputDialog(this, "Ingrese el número de empleado del profesor a editar:");
        if (numeroStr == null || numeroStr.trim().isEmpty()) return;
        
        try {
            int numeroEmpleado = Integer.parseInt(numeroStr);
            
            String[] datosActuales = obtenerDatosProfesorPorNumero(numeroEmpleado);
            if (datosActuales == null) {
                showError("No se encontró el número de empleado: " + numeroEmpleado);
                return;
            }
            
            JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
            panel.setBackground(COLOR_FONDO);
            
            JTextField nombreField = new JTextField(datosActuales[0]);
            JTextField apellidoField = new JTextField(datosActuales[1]);
            JTextField numeroField = new JTextField(datosActuales[2]);
            
            JLabel nombreLabel = new JLabel("Nombre:");
            nombreLabel.setForeground(COLOR_TEXTO);
            panel.add(nombreLabel);
            panel.add(nombreField);
            
            JLabel apellidoLabel = new JLabel("Apellido:");
            apellidoLabel.setForeground(COLOR_TEXTO);
            panel.add(apellidoLabel);
            panel.add(apellidoField);
            
            JLabel numeroLabel = new JLabel("Número de empleado:");
            numeroLabel.setForeground(COLOR_TEXTO);
            panel.add(numeroLabel);
            panel.add(numeroField);
            
            int result = JOptionPane.showConfirmDialog(this, panel, 
                "Editar profesor - Número: " + numeroEmpleado, 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            
            if (result == JOptionPane.OK_OPTION) {
                String nuevoNombre = nombreField.getText();
                String nuevoApellido = apellidoField.getText();
                int nuevoNumero = Integer.parseInt(numeroField.getText());
                
                db.editarProfesorPorNumero(numeroEmpleado, nuevoNombre, nuevoApellido, nuevoNumero);
                mostrarTabla();
            }
            
        } catch (NumberFormatException e) {
            showError("Número de empleado inválido");
        } catch (Exception e) {
            showError("Error: " + e.getMessage());
        }
    }
    
    private String[] obtenerDatosProfesorPorNumero(int numeroEmpleado) {
        try {
            PreparedStatement st = db.con.prepareStatement("SELECT nombre, apellido, numero_empleado FROM profesores WHERE numero_empleado = ?");
            st.setInt(1, numeroEmpleado);
            ResultSet rs = st.executeQuery();
            
            if (rs.next()) {
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String numeroStr = String.valueOf(rs.getInt("numero_empleado"));
                return new String[]{nombre, apellido, numeroStr};
            }
        } catch (Exception e) {
            System.out.println("Error al obtener datos del profesor: " + e.getMessage());
        }
        return null;
    }
    
    private void mostrarFormularioAgregar() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.setBackground(COLOR_FONDO);
        
        JTextField nombreField = new JTextField();
        JTextField apellidoField = new JTextField();
        JTextField numeroField = new JTextField();
        
        JLabel nombreLabel = new JLabel("Nombre:");
        nombreLabel.setForeground(COLOR_TEXTO);
        panel.add(nombreLabel);
        panel.add(nombreField);
        
        JLabel apellidoLabel = new JLabel("Apellido:");
        apellidoLabel.setForeground(COLOR_TEXTO);
        panel.add(apellidoLabel);
        panel.add(apellidoField);
        
        JLabel numeroLabel = new JLabel("Número de empleado:");
        numeroLabel.setForeground(COLOR_TEXTO);
        panel.add(numeroLabel);
        panel.add(numeroField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, 
            "Agregar profesor", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                String nombre = nombreField.getText();
                String apellido = apellidoField.getText();
                int numeroEmpleado = Integer.parseInt(numeroField.getText());
                
                db.insertarProfesor(nombre, apellido, numeroEmpleado);
                mostrarTabla();
            } catch (NumberFormatException e) {
                showError("Número de empleado inválido");
            }
        }
    }
    
    private void eliminarProfesor() {
        String numeroStr = JOptionPane.showInputDialog(this, "Ingrese el número de empleado del profesor a eliminar:");
        if (numeroStr == null || numeroStr.trim().isEmpty()) return;
        
        try {
            int numeroEmpleado = Integer.parseInt(numeroStr);
            db.eliminarProfesorPorNumero(numeroEmpleado);
            mostrarTabla();
        } catch (NumberFormatException e) {
            showError("Número de empleado inválido");
        }
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}