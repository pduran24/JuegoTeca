import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CsvDataService implements DataService<Juego> {

    private String archivo;

    CsvDataService(String csvFile) {
        this.archivo = csvFile;
    }

    @Override
    public List<Juego> findAll() {
        var salida = new ArrayList<Juego>();

        try (BufferedReader br = new BufferedReader(new FileReader(new File(archivo)))) {
            var contenido = br.lines();

            contenido.forEach(line -> {
                String[] lineArray = line.split(",");
                Juego juego = new Juego();
                juego.setId(Integer.parseInt(lineArray[0]));
                juego.setNombre(lineArray[1]);
                juego.setAño(Integer.parseInt(lineArray[2]));
                juego.setPlataforma(lineArray[3]);
                juego.setDesarrolladora(lineArray[5]);
                juego.setValoracion(Integer.parseInt(lineArray[6]));
                juego.setTipo(lineArray[7]);
                juego.setDescripcion(lineArray[8]);
                juego.setImagen(lineArray[9]);
                juego.setVideourl(lineArray[10]);
                juego.setOwnerId(Integer.parseInt(lineArray[11]));
                juego.setStatus(lineArray[12]);

                salida.add(juego);
            });

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return salida;

    }

}
