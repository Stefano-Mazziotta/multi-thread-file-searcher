package multiThreadFileSearcher;

/*
 * FileSearcher
 * Armar una aplicación que permita buscar un archivo particular dentro del sistema de archivos del sistema operativo.
 * Opcionalmente, podria especificar la carpeta donde quiero realizar la busqueda.
 * La idea es que la busqueda se realice con multiples hilos y que el que haya encontrado el archivo pedido lo reporte por pantalla.
 */

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java Main <fileName> [startPath]");
            System.exit(1);
        }

        String fileName = args[0];
        String startPath = args.length >= 2 ? args[1] : System.getProperty("user.dir");

        FileSearcher searcher = new FileSearcher(fileName, startPath);
        searcher.startSearch();
    }
}
