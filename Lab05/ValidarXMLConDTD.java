import org.apache.xerces.parsers.SAXParser;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import java.io.IOException;
import java.util.Scanner;

public class ValidarXMLConDTD {

    public static void main(String[] args) {
        try {
            // Crear un parser SAX de Xerces
            SAXParser parser = new SAXParser();
            
            // Establecer un manejador por defecto para la validación
            parser.setFeature("http://xml.org/sax/features/validation", true);  // Activar validación
            // parser.setFeature("http://apache.org/xml/features/validation/dynamic", false); // Para deshabilitar validación dinámica
            
            // Parsear el archivo XML y validar con el DTD
            parser.setContentHandler(new DefaultHandler());
            // Preguntar por el nombre del archivo XML, pedir y almacenar el nombre del archivo XML, preguntar por consola
            // y almacenar el nombre del archivo XML, preguntar por consola 
            String nombreArchivo = "Ej01.xml"; // Aquí puedes cambiar el nombre del archivo XML si es necesario
            Scanner scanner = new Scanner(System.in);
            System.out.print("Introduce el nombre del archivo XML (Ej01.xml): ");
            nombreArchivo = scanner.nextLine();
            if (nombreArchivo.isEmpty()) {
                nombreArchivo = "Ej01PosibleSolucion.xml"; // Valor por defecto
            }
            scanner.close();
            parser.parse(nombreArchivo);  // Aquí se ejecuta la validación

            System.out.println("El archivo XML es válido según el DTD.");
        } catch (SAXException e) {
            System.out.println("Error de validación: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error de entrada/salida: " + e.getMessage());
        }
    }
}
