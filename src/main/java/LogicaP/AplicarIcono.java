
package LogicaP;

import java.awt.Window;
import javax.swing.ImageIcon;
import javax.swing.JFrame;


public class AplicarIcono {
    public static void icono(Window window, String rutaIcono) {
        window.setIconImage(new ImageIcon(window.getClass().getResource(rutaIcono)).getImage());
    }
}
