import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AuthService {
    private Properties properties;
    private static AuthService instance;

    private AuthService() {
        properties = new Properties();
        cargarPropiedades();
    }

    public static AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }

    private void cargarPropiedades() {
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("login.properties")) {

            if (input == null) {
                System.out.println("No se pudo encontrar login.properties");
                return;
            }

            properties.load(input);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public boolean autenticar(String usuario, String password) {
        String usuarioValido = properties.getProperty("user.name");
        String passwordValido = properties.getProperty("user.password");

        return usuario != null && password != null &&
                usuario.equals(usuarioValido) && password.equals(passwordValido);
    }
}
