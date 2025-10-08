import lombok.Data;

@Data
public class Juego {
    private Integer id;
    private String nombre;
    private Integer año;
    private String plataforma;
    private String desarrolladora;
    private Integer valoracion;
    private String tipo;
    private String descripcion;
    private String imagen;
    private String videourl;
    private Integer ownerId;
    private String status;

    public Juego(){

    }

}
