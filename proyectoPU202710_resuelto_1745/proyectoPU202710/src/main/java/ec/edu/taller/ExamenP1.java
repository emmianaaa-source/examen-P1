package ec.edu.taller;

import javax.swing.*;

import java.util.List;

/**

 * Clase enlazada (bound class) al formulario ExamenP1.form (bind to class: ec.edu.taller.ExamenP1).

 * El DISENO se hace en el GUI Designer de IntelliJ; aqui solo va la LOGICA de la ventana.

 */

public class ExamenP1 extends JFrame {

    // ---- Componentes creados en el .form (los nombres deben coincidir con "field name") ----

    private JPanel panelPrincipal;

    private JTabbedPane tabbedPane;

    // Pestana 1: REGISTRAR TRABAJO

    private JTextField txtCodigo;

    private JComboBox<String> cmbSolicitante;

    private JTextField txtDocumento;

    private JSpinner spnPrioridad;

    private JButton btnRegistrar;

    private JButton btnLimpiar;

    // Pestana 2: GESTIONAR TRABAJOS

    private JLabel lblValCodigo;

    private JLabel lblValSolicitante;

    private JLabel lblValDocumento;

    private JLabel lblValPrioridad;

    private JLabel lblValEstado;

    private JTextArea txaPendientes;

    private JLabel lblPendientes;

    private JLabel lblDisponibles;

    private JLabel lblCapacidad;

    private JButton btnProcesar;

    private JButton btnConsultar;

    private JButton btnListar;

    // UNA sola instancia del gestor durante toda la ejecucion

    private final GestorImpresiones gestor = new GestorImpresiones(10);
    private JPanel PanelPrincipal;
    private JTextField textField1;
    private JSpinner spinner1;

    public ExamenP1() {

        super("Centro de impresion universitario");

        setContentPane(panelPrincipal);          // el panel raiz del .form

        // Lo que el diseñador no configura bien, se ajusta aqui

        cmbSolicitante.setModel(new DefaultComboBoxModel<>(

                new String[]{"ESTUDIANTE", "DOCENTE", "ADMINISTRATIVO"}));

        spnPrioridad.setModel(new SpinnerNumberModel(1, 1, 10, 1));

        txaPendientes.setEditable(false);

        // Eventos de los botones

        btnRegistrar.addActionListener(e -> registrar());

        btnLimpiar.addActionListener(e -> limpiar());

        btnConsultar.addActionListener(e -> consultarSiguiente());

        btnProcesar.addActionListener(e -> procesarSiguiente());

        btnListar.addActionListener(e -> mostrarListado());

        cargarTrabajosQuemados();

        actualizarContadores();

        mostrarListado();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        pack();

        setLocationRelativeTo(null);

    }

    // ================== Acciones ==================

    private void registrar() {

        String codigo = txtCodigo.getText();

        String solicitante = (String) cmbSolicitante.getSelectedItem();

        String documento = txtDocumento.getText().trim();

        int prioridad = (Integer) spnPrioridad.getValue();

        TrabajoImpresion trabajo = new TrabajoImpresion(codigo, solicitante, documento, prioridad);

        if (gestor.registrarTrabajo(trabajo)) {

            JOptionPane.showMessageDialog(this, "Trabajo registrado:\n" + trabajo);

            limpiar();

            actualizarContadores();

            mostrarListado();

        } else {

            JOptionPane.showMessageDialog(this, motivoRechazo(codigo),

                    "Registro rechazado", JOptionPane.WARNING_MESSAGE);

        }

    }

    private String motivoRechazo(String codigo) {

        if (codigo == null || codigo.isBlank()) {

            return "El codigo no puede estar vacio.";

        }

        if (gestor.existeTrabajo(codigo)) {

            return "Ya existe un trabajo pendiente con el codigo " + codigo + ".";

        }

        if (gestor.consultarEspaciosDisponibles() == 0) {

            return "La cola esta llena (capacidad " + gestor.getCapacidadMaxima() + ").";

        }

        return "No se pudo registrar el trabajo.";

    }

    private void limpiar() {

        txtCodigo.setText("");

        cmbSolicitante.setSelectedIndex(0);

        txtDocumento.setText("");

        spnPrioridad.setValue(1);

        txtCodigo.requestFocus();

    }

    private void consultarSiguiente() {

        TrabajoImpresion siguiente = gestor.consultarSiguienteTrabajo();   // peek

        mostrarDetalle(siguiente);

        if (siguiente == null) {

            JOptionPane.showMessageDialog(this, "No hay trabajos pendientes.");

        }

    }

    private void procesarSiguiente() {

        TrabajoImpresion procesado = gestor.procesarSiguienteTrabajo();    // poll

        if (procesado == null) {

            JOptionPane.showMessageDialog(this, "La cola esta vacia: no hay nada que procesar.");

            return;

        }

        mostrarDetalle(procesado);

        actualizarContadores();

        mostrarListado();

        JOptionPane.showMessageDialog(this, "Trabajo procesado:\n" + procesado);

    }

    // ================== Actualizar la vista ==================

    private void mostrarDetalle(TrabajoImpresion t) {

        lblValCodigo.setText(t == null ? "-" : t.getCodigo());

        lblValSolicitante.setText(t == null ? "-" : t.getSolicitante());

        lblValDocumento.setText(t == null ? "-" : t.getDocumento());

        lblValPrioridad.setText(t == null ? "-" : String.valueOf(t.getPrioridad()));

        lblValEstado.setText(t == null ? "-" : t.getEstado());

    }

    private void mostrarListado() {

        List<TrabajoImpresion> lista = gestor.listarTrabajos();

        if (lista.isEmpty()) {

            txaPendientes.setText("(No hay trabajos pendientes)");

            return;

        }

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < lista.size(); i++) {

            sb.append(i + 1).append(". ").append(lista.get(i)).append('\n');

        }

        txaPendientes.setText(sb.toString());

    }

    private void actualizarContadores() {

        lblPendientes.setText("PENDIENTES: " + gestor.contarTrabajosPendientes());

        lblDisponibles.setText("DISPONIBLES: " + gestor.consultarEspaciosDisponibles());

        lblCapacidad.setText("CAPACIDAD: " + gestor.getCapacidadMaxima());

    }

    /** 8 trabajos "quemados". */

    private void cargarTrabajosQuemados() {

        gestor.registrarTrabajo(new TrabajoImpresion("IMP-001", "ESTUDIANTE", "Informe.pdf", 5));

        gestor.registrarTrabajo(new TrabajoImpresion("IMP-002", "DOCENTE", "Examen.pdf", 1));

        gestor.registrarTrabajo(new TrabajoImpresion("IMP-003", "ADMINISTRATIVO", "Acta.pdf", 3));

        gestor.registrarTrabajo(new TrabajoImpresion("IMP-004", "ESTUDIANTE", "Tesis.pdf", 7));

        gestor.registrarTrabajo(new TrabajoImpresion("IMP-005", "DOCENTE", "Silabo.pdf", 2));

        gestor.registrarTrabajo(new TrabajoImpresion("IMP-006", "ADMINISTRATIVO", "Oficio.pdf", 4));

        gestor.registrarTrabajo(new TrabajoImpresion("IMP-007", "ESTUDIANTE", "Deber.pdf", 9));

        gestor.registrarTrabajo(new TrabajoImpresion("IMP-008", "DOCENTE", "Rubrica.pdf", 6));

    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> new ExamenP1().setVisible(true));

    }

}
