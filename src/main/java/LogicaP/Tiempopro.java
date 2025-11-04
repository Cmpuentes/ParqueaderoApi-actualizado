package LogicaP;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Tiempopro {

    private SimpleDateFormat formatoHora = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
    private SimpleDateFormat formatoFecha = new SimpleDateFormat("dd-MM-yyyy");

    public String getFechacomp() {
        Calendar fecha = new GregorianCalendar(); // Se obtiene la fecha actual
        return formatoFecha.format(fecha.getTime());
    }

    public String getHoracomp() {
        Calendar fecha = new GregorianCalendar(); // Se obtiene la hora actual
        return formatoHora.format(fecha.getTime());
    }
}

