import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AgregarJuegoDialog extends JDialog {
    private JPanel contentPane;
    private JTextField txtNombre;
    private JTextField txtAnio;
    private JTextField txtPlataforma;
    private JTextField txtDesarrolladora;
    private JTextField txtValoracion;
    private JTextField txtTipo;
    private JTextArea txtDescripcion;
    private JButton btnGuardar;
    private JButton btnCancelar;
    private Juego juegoCreado;

    public AgregarJuegoDialog(JFrame parent) {
        super(parent, "Añadir Videojuego", true);

        initComponents();

        setContentPane(contentPane);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(parent);
        setResizable(false);
    }

    private void initComponents() {
        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(10, 10));
        contentPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel central con los campos
        JPanel panelCampos = new JPanel(new GridLayout(7, 2, 10, 10));

        panelCampos.add(new JLabel("Nombre:"));
        txtNombre = new JTextField(20);
        panelCampos.add(txtNombre);

        panelCampos.add(new JLabel("Año:"));
        txtAnio = new JTextField(20);
        panelCampos.add(txtAnio);

        panelCampos.add(new JLabel("Plataforma:"));
        txtPlataforma = new JTextField(20);
        panelCampos.add(txtPlataforma);

        panelCampos.add(new JLabel("Desarrolladora:"));
        txtDesarrolladora = new JTextField(20);
        panelCampos.add(txtDesarrolladora);

        panelCampos.add(new JLabel("Valoración (1-10):"));
        txtValoracion = new JTextField(20);
        panelCampos.add(txtValoracion);

        panelCampos.add(new JLabel("Tipo:"));
        txtTipo = new JTextField(20);
        panelCampos.add(txtTipo);

        panelCampos.add(new JLabel("Descripción:"));
        txtDescripcion = new JTextArea(3, 20);
        JScrollPane scrollDescripcion = new JScrollPane(txtDescripcion);
        panelCampos.add(scrollDescripcion);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        contentPane.add(panelCampos, BorderLayout.CENTER);
        contentPane.add(panelBotones, BorderLayout.SOUTH);

        // Eventos
        btnGuardar.addActionListener(e -> guardarJuego());
        btnCancelar.addActionListener(e -> dispose());

        // ESC cierra el diálogo
        contentPane.registerKeyboardAction(
                e -> dispose(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
        );
    }

    private void guardarJuego() {
        // Validar campos obligatorios
        if (txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "El nombre es obligatorio",
                    "Campo requerido",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            // Crear el juego
            juegoCreado = new Juego();
            juegoCreado.setNombre(txtNombre.getText().trim());

            // Año
            if (!txtAnio.getText().trim().isEmpty()) {
                juegoCreado.setAño(Integer.parseInt(txtAnio.getText().trim()));
            }

            juegoCreado.setPlataforma(txtPlataforma.getText().trim());
            juegoCreado.setDesarrolladora(txtDesarrolladora.getText().trim());

            // Valoración
            if (!txtValoracion.getText().trim().isEmpty()) {
                int valoracion = Integer.parseInt(txtValoracion.getText().trim());
                if (valoracion < 1 || valoracion > 10) {
                    throw new IllegalArgumentException("La valoración debe estar entre 1 y 10");
                }
                juegoCreado.setValoracion(valoracion);
            }

            juegoCreado.setTipo(txtTipo.getText().trim());
            juegoCreado.setDescripcion(txtDescripcion.getText().trim());
            juegoCreado.setStatus("Activo");
            juegoCreado.setOwnerId(1); // ID por defecto

            JOptionPane.showMessageDialog(
                    this,
                    "Juego añadido correctamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "El año y la valoración deben ser números válidos",
                    "Error de formato",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error de validación",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public Juego getJuegoCreado() {
        return juegoCreado;
    }
}