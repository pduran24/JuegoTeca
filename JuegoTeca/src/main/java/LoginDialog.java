import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginDialog extends JDialog {
    private JPanel contentPane;
    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnCancelar;
    private boolean loginExitoso = false;

    public LoginDialog(JFrame parent) {
        super(parent, "Iniciar Sesión", true);

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
        JPanel panelCampos = new JPanel(new GridLayout(2, 2, 10, 10));

        JLabel lblUsuario = new JLabel("Usuario:");
        txtUsuario = new JTextField(15);

        JLabel lblPassword = new JLabel("Contraseña:");
        txtPassword = new JPasswordField(15);

        panelCampos.add(lblUsuario);
        panelCampos.add(txtUsuario);
        panelCampos.add(lblPassword);
        panelCampos.add(txtPassword);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnLogin = new JButton("Iniciar Sesión");
        btnCancelar = new JButton("Cancelar");

        panelBotones.add(btnLogin);
        panelBotones.add(btnCancelar);

        contentPane.add(panelCampos, BorderLayout.CENTER);
        contentPane.add(panelBotones, BorderLayout.SOUTH);

        // Eventos
        btnLogin.addActionListener(e -> realizarLogin());
        btnCancelar.addActionListener(e -> dispose());

        // Enter en password ejecuta login
        txtPassword.addActionListener(e -> realizarLogin());

        // ESC cierra el diálogo
        contentPane.registerKeyboardAction(
                e -> dispose(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
        );
    }

    private void realizarLogin() {
        String usuario = txtUsuario.getText();
        String password = new String(txtPassword.getPassword());

        if (usuario.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Por favor, ingrese usuario y contraseña",
                    "Campos vacíos",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        AuthService authService = AuthService.getInstance();

        if (authService.autenticar(usuario, password)) {
            loginExitoso = true;
            Session.usuarioAutenticado = usuario;
            JOptionPane.showMessageDialog(
                    this,
                    "¡Bienvenido " + usuario + "!",
                    "Login exitoso",
                    JOptionPane.INFORMATION_MESSAGE
            );
            dispose();
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Usuario o contraseña incorrectos",
                    "Error de autenticación",
                    JOptionPane.ERROR_MESSAGE
            );
            txtPassword.setText("");
            txtPassword.requestFocus();
        }
    }

    public boolean isLoginExitoso() {
        return loginExitoso;
    }
}
