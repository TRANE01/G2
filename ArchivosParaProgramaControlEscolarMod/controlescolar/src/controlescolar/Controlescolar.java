package controlescolar;

import javax.swing.*;

public class Controlescolar {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new vp().setVisible(true);
            }
        });
    }
}