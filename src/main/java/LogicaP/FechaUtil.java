
package LogicaP;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 *
 * @author Carlos Mario
 */
public class FechaUtil {
    
    public static String generarFechaActual() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a", Locale.ENGLISH);
        return LocalDateTime.now().format(formatter);
    }  
}
