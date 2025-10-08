public class Session {
    public static Juego juegosSeleccionados = null;
    public static String idioma = "es";
    public static String usuarioAutenticado = null;

    public static boolean isAutenticado() {
        return usuarioAutenticado != null;
    }

    public static void cerrarSesion() {
        usuarioAutenticado = null;
    }
}