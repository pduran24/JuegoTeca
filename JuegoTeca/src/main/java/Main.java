public class Main {
    public static void main(String[] args) {


        DataService ds = new CsvDataService("juegos.csv");

        (new Principal(ds)).start();

    }
}
