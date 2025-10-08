import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class Principal extends JFrame {
    private JFrame frame;
    private JTable table1;
    private JPanel panel1;
    private JScrollPane scrollPanel;
    private JButton btnLogin;
    private JButton btnAgregarJuego;
    private DataService dataService;
    private ArrayList<Juego> juegos = new ArrayList<>();
    private DefaultTableModel modelo;

    public Principal(DataService ds) {
        dataService = ds;
        this.setTitle("Lista de Juegos");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Crear panel principal con BorderLayout
        JPanel panelPrincipal = new JPanel(new BorderLayout());

        // Panel superior para los botones
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnLogin = new JButton("Iniciar Sesión");
        btnAgregarJuego = new JButton("Añadir Videojuego");

        // Inicialmente el botón de agregar está deshabilitado
        btnAgregarJuego.setEnabled(false);

        panelSuperior.add(btnLogin);
        panelSuperior.add(btnAgregarJuego);

        // Agregar panel1 (que contiene la tabla) al centro
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        panelPrincipal.add(panel1, BorderLayout.CENTER);

        this.setContentPane(panelPrincipal);
        this.setLocationRelativeTo(null);
        this.setSize(900, 600);
        this.setResizable(true);

        // Configurar tabla
        modelo = new DefaultTableModel();
        modelo.addColumn("Id");
        modelo.addColumn("Nombre");
        modelo.addColumn("Descripción");
        table1.setModel(modelo);

        cargarJuegos();

        table1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table1.getSelectionModel().addListSelectionListener((e) -> {
            if (!e.getValueIsAdjusting() && table1.getSelectedRow() >= 0) {
                System.out.println("seleccionado: " + table1.getSelectedRow());
                Juego juego = juegos.get(table1.getSelectedRow());

                Session.juegosSeleccionados = juego;
                (new Detalle()).setVisible(true);
            }
        });

        // Eventos de botones
        btnLogin.addActionListener(e -> mostrarLogin());
        btnAgregarJuego.addActionListener(e -> mostrarAgregarJuego());

        actualizarEstadoBotones();
    }

    private void cargarJuegos() {
        // Limpiar la tabla
        modelo.setRowCount(0);

        // Cargar juegos desde el servicio
        juegos = new ArrayList<>(dataService.findAll());
        juegos.forEach((j) -> {
            var fila = new Object[]{j.getId(), j.getNombre(), j.getDescripcion()};
            modelo.addRow(fila);
        });
    }

    private void mostrarLogin() {
        if (Session.isAutenticado()) {
            // Si ya está autenticado, ofrecer cerrar sesión
            int opcion = JOptionPane.showConfirmDialog(
                    this,
                    "Ya has iniciado sesión como " + Session.usuarioAutenticado +
                            "\n¿Deseas cerrar sesión?",
                    "Sesión activa",
                    JOptionPane.YES_NO_OPTION
            );

            if (opcion == JOptionPane.YES_OPTION) {
                Session.cerrarSesion();
                actualizarEstadoBotones();
                JOptionPane.showMessageDialog(
                        this,
                        "Sesión cerrada correctamente",
                        "Cerrar Sesión",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        } else {
            // Mostrar diálogo de login
            LoginDialog loginDialog = new LoginDialog(this);
            loginDialog.setVisible(true);

            if (loginDialog.isLoginExitoso()) {
                actualizarEstadoBotones();
            }
        }
    }

    private void mostrarAgregarJuego() {
        if (!Session.isAutenticado()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debes iniciar sesión para añadir videojuegos",
                    "Acceso denegado",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        AgregarJuegoDialog agregarDialog = new AgregarJuegoDialog(this);
        agregarDialog.setVisible(true);

        Juego nuevoJuego = agregarDialog.getJuegoCreado();
        if (nuevoJuego != null) {
            // Generar un ID para el nuevo juego
            int maxId = juegos.stream()
                    .mapToInt(Juego::getId)
                    .max()
                    .orElse(0);
            nuevoJuego.setId(maxId + 1);

            // Agregar a la lista
            juegos.add(nuevoJuego);

            // Agregar a la tabla
            var fila = new Object[]{
                    nuevoJuego.getId(),
                    nuevoJuego.getNombre(),
                    nuevoJuego.getDescripcion()
            };
            modelo.addRow(fila);

            JOptionPane.showMessageDialog(
                    this,
                    "Videojuego añadido correctamente a la lista\n" +
                            "(Nota: Los cambios no se guardarán en el archivo CSV)",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    private void actualizarEstadoBotones() {
        if (Session.isAutenticado()) {
            btnLogin.setText("Cerrar Sesión (" + Session.usuarioAutenticado + ")");
            btnAgregarJuego.setEnabled(true);
        } else {
            btnLogin.setText("Iniciar Sesión");
            btnAgregarJuego.setEnabled(false);
        }
    }

    public void start() {
        this.setVisible(true);
    }
}
