package controlescolar;

import javax.swing.*;
import java.awt.*;

public class vp extends JFrame {
    private Db db;
    private CardLayout seleccion;
    private JPanel mainPanel;
    
    private final Color COLOR_FONDO = new Color(45, 45, 45);
    private final Color COLOR_BOTONES = new Color(80, 80, 80);
    private final Color COLOR_TEXTO = Color.WHITE;
    private final Color COLOR_BORDE = new Color(120, 120, 120);
    
    public vp() {
        db = new Db();
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("CONTROL ESCOLAR");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
        
        seleccion = new CardLayout();
        mainPanel = new JPanel(seleccion);
        mainPanel.setBackground(COLOR_FONDO);
        
        mainPanel.add(createSelectionPanel(), "SELECTION");
        mainPanel.add(new ta(db, this), "ALUMNOS");
        mainPanel.add(new tp(db, this), "PROFESORES");
        
        add(mainPanel);
    }
    
    private JPanel createSelectionPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);
        
        JLabel titleLabel = new JLabel("CONTROL ESCOLAR", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(COLOR_TEXTO);
        panel.add(titleLabel, gbc);
        
        gbc.insets = new Insets(30, 0, 10, 0);
        panel.add(Box.createVerticalStrut(20), gbc);
        
        JButton alumnosBtn = createStyledButton("Alumnos");
        alumnosBtn.addActionListener(e -> showAlumnosPanel());
        
        JButton profesoresBtn = createStyledButton("Profesores");
        profesoresBtn.addActionListener(e -> showProfesoresPanel());
        
        gbc.insets = new Insets(15, 0, 15, 0);
        panel.add(alumnosBtn, gbc);
        panel.add(profesoresBtn, gbc);
        
        return panel;
    }
    
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(250, 50));
        button.setBackground(COLOR_BOTONES);
        button.setForeground(COLOR_TEXTO);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(COLOR_BORDE, 2));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(100, 100, 100));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(COLOR_BOTONES);
            }
        });
        
        return button;
    }
    
    public void showAlumnosPanel() {
        seleccion.show(mainPanel, "ALUMNOS");
    }
    
    public void showProfesoresPanel() {
        seleccion.show(mainPanel, "PROFESORES");
    }
    
    public void showSelectionPanel() {
        seleccion.show(mainPanel, "SELECTION");
    }
}