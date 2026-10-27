package ec.edu.taller;
import javax.swing.SwingUtilities;
/** Punto de entrada del programa: abre la ventana disenada en ExamenP1.form. */
public class InterfazImpresiones {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ExamenP1().setVisible(true));
    }
}

