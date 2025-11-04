package Presentacion;

import Datos.TotalesSalidaDTO;
import DatosP.Dingresop;
import DatosP.Dsalidap;
import DatosP.SessionData;
import DatosP.SessionManager;
import DatosP.SessionStorage;
import Diseños.Estado_Abonos;
import Diseños.Estilo_tablas;
import Diseños.TableActionCellEditor;
import Diseños.TableActionCellRender;
import Diseños.TablecActionEvent;
import ImpresionP.ImprimirArqueo;
import Impresionp.CierreTurno;
import Logica.AbonoService;
import Logica.ApiSalidaService;
import Logica.ConfiguradorApi;
import Logica.IngresoService;
import Logica.SalidaService;
import LogicaP.AplicarIcono;
import LogicaP.Fzonap;
import LogicaP.LoginService;
import LogicaP.Tiempopro;
import ReporteP.SalidaTurnoReporteDTO;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.PrintException;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class Jmenuprin extends javax.swing.JFrame {

    private Jinicioturnop loginDialog;

    private Timer sessionTimer;
    private Timer salidaTimer;

    public static Boolean SesionIniciada = false;
    private static final long serialVersionUID = 1L;
    public static String idcliente = "";
    private static boolean turnoRecuperadoP = false;
    public static Boolean sesionIniciadaP = false;
    private LoguinDeAdminp Javanzadop;
    private RegitrarPrepago registroPrepago;
    private Jsalidap salidaFormularioP;
    private Jabonosp abonosFormulariop;
    private Jfinturnop salidaturnoP;
    private Jingresop ingresoFormularioP;
    public static Fzonap fcnP;
    private JWindow ayudaWindow;
    private int numeroTurno = 0;
    NumberFormat formatoMiles = NumberFormat.getNumberInstance(Locale.US);

    @SuppressWarnings("LeakingThisInConstructor")
    public Jmenuprin() throws Exception {

        initComponents();

        validarSesionInicial();
        iniciarTimerSesion();
        iniciarTimerIngresos();

        mostrarTiempo();
        AplicarIcono.icono(this, "/Imagenes/GN_Grande.png");
        setLocationRelativeTo(null);
        inhabilitar();

        mostrarsalida();
        iniciarTimerSalidas();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0, 0, screenSize.width, screenSize.height);
        setVisible(true);
//      iniciarMonitoreoSesion();
//      iniciarMonitoreoSesionP();
        configurarTabla();
        actualizarTablas();
        //iniciarTurno();

    }

    //FUNCIÓN PARA VALIDAR LA SESIÓN INICIAL
    private void validarSesionInicial() {
        // obtener local

        SessionData local = SessionStorage.loadSession();

        log("validarSesionInicial - token=" + (local == null ? "null" : local.getToken()));

        if (local == null) {
            limpiarSesion();
            abrirLogin();
            return;
        }

        // lanzar worker para consultar en background
        new javax.swing.SwingWorker<java.util.Optional<SessionData>, Void>() {
            @Override
            protected java.util.Optional<SessionData> doInBackground() {
                // consulta remota
                return LoginService.checkSessionAndGetSession(local.getToken());
            }

            @Override
            protected void done() {
                try {
                    java.util.Optional<SessionData> remoteOpt = get();
                    if (remoteOpt.isPresent()) {
                        SessionData remote = remoteOpt.get();
                        // sincronizar en memoria y archivo
                        SessionManager.getInstance().setSessionData(remote);
                        SessionStorage.saveSession(remote);
                        actualizarLabels(remote);
                    } else {
                        limpiarSesion();
                        abrirLogin();
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    // si hay error al consultar, puedes mostrar login o confiar en local (tu decisión)
                    limpiarSesion();
                    abrirLogin();
                }
            }
        }.execute();
    }

    //TIMER PARA LA CONSULTA DE SESION
    private void iniciarTimerSesion() {
        final int intervalo = 180_000; // 3 minutos en milisegundos
        sessionTimer = new javax.swing.Timer(intervalo, e -> {
            SessionData local = SessionManager.getInstance().getSessionData();
            if (local == null) {
                System.out.println("TIMER >> No hay sesión local en memoria");
                return;
            }

            System.out.println("TIMER >> Validando sesión con token: " + local.getToken());

            new javax.swing.SwingWorker<java.util.Optional<SessionData>, Void>() {
                @Override
                protected java.util.Optional<SessionData> doInBackground() {
                    return LoginService.checkSessionAndGetSession(local.getToken());
                }

                @Override
                protected void done() {
                    try {
                        java.util.Optional<SessionData> remoteOpt = get();
                        if (remoteOpt.isPresent()) {
                            // session válida: actualizar si hay cambios
                            SessionData remote = remoteOpt.get();
                            System.out.println("TIMER >> Sesión válida en servidor: " + remote.getNombreCompleto());
                            SessionManager.getInstance().setSessionData(remote);
                            SessionStorage.saveSession(remote);
                            actualizarLabels(remote);
                        } else {
                            // sesión invalidada en el servidor
                            System.out.println("TIMER >> Sesión inválida en servidor. Limpiando...");
                            limpiarSesion();
                            abrirLogin();
                        }
                    } catch (InterruptedException | ExecutionException ex) {
                        System.out.println("TIMER >> Error validando sesión: " + ex.getMessage());
                        // en caso de fallo, puedes ignorar y esperar al próximo tick
                    }
                }
            }.execute();
        });
        sessionTimer.start();
    }

    //FUNCIÓN PARA ACTUALIZAR LOS LABELS CON LOS DATOS DE LA SESIÓN
    public void actualizarLabels(SessionData session) {
        lblempleadoP.setText(session.getNombreCompleto());
        lblturnosP.setText(session.getTurno());
        lblnumeroturnoP.setText(String.valueOf(session.getNumeroTurno()));
        lblfechaP.setText(session.getFechaInicio());
        lblestadoP.setText("Activo");
    }

    //FUNCIÓN PARA LIMPIAR LOS DATOS DE SESIÓN DE LOS LABELS
    private void limpiarSesion() {
        SessionStorage.clearSession();
        lblempleadoP.setText("");
        lblturnosP.setText("");
        lblnumeroturnoP.setText("");
        lblfechaP.setText("");
        lblestadoP.setText("Finalizado");
    }

    //FUNCIÓN PARA ABRIR EL FORMULARIO DE LOGIN
    private void abrirLogin() {
        if (loginDialog == null || !loginDialog.isDisplayable()) {
            loginDialog = new Jinicioturnop(this);
            loginDialog.setVisible(true);
            loginDialog.setLocationRelativeTo(null); // ✅ Centrar en la pantalla
            loginDialog.setAlwaysOnTop(true);
        } else {
            loginDialog.toFront();
        }
    }

    //FUNCIÓN PARA PRUEBAS CON LOGS
    private void log(String msg) {
        System.out.println(java.time.LocalDateTime.now() + " SESSION-LOG: " + msg);
    }

//    private void iniciarTurno() throws Exception {
//        Jfinturnop Turnosalida = new Jfinturnop();
//        Turnosalida.setVisible(true);
//        Turnosalida.toFront();
//        Turnosalida.setExtendedState(JFrame.NORMAL);
//    }
    void ocultar_columnas() {
        int[] columnasOcultas = {0, 1, 7, 10, 11};
        Estilo_tablas.ocultarColumnas(tablalistado, columnasOcultas);
    }

    void ocultar_columnasSalidaP() {
        int[] columnasOcultas = {0, 7, 8, 9};
        Estilo_tablas.ocultarColumnas(TablaSalida, columnasOcultas);
    }

    void ocultar_columnasPrepagos() {
        int[] columnasOcultas = {0, 9, 10, 11, 12};
        Estilo_tablas.ocultarColumnas(tablalistadoPrepagos, columnasOcultas);

    }

    //FUNCIÓN PARA COLOCAR LA FECHA
    private void mostrarTiempo() {
        Tiempopro time = new Tiempopro();
        System.out.println("TIME: " + time);
        lblfechaP.setText(time.getFechacomp() + " " + time.getHoracomp());
    }

    //MIGRADO Y "FUNCIONANDO"
    void mostrarabono(String buscar) {
        try {
            DefaultTableModel modelo;
            AbonoService func = new AbonoService();
            modelo = func.mostrar(buscar);

            tablalistadoPrepagos.setModel(modelo);
            tablalistadoPrepagos.getColumnModel().getColumn(8).setCellRenderer(new Estado_Abonos());
            ocultar_columnasPrepagos();
            lblregistroprepagos.setText("Total Registros " + Integer.toString(func.totalregistros));

        } catch (Exception e) {
            JOptionPane.showConfirmDialog(rootPane, e);
        }
    }
//OJO AQUÍ PA PONER LA TABLA DE SALIDA

    private void actualizarTablas() throws Exception {
        mostrar();
        //mostrarsalida();
        mostrarabono("");

    }

    public static int idusuario;

    static void inhabilitar() {
        lblidinicioturnoP.setVisible(false);
    }

    // Métodos para actualizar los JLabel
    public static void actualizarFechaP(String fecha) {
        lblfechaP.setText(fecha);
    }

    public static void actualizarTurnoP(String turno) {
        lblturnosP.setText(turno);
    }

    public static void actualizarEmpleadoP(String empleado) {
        lblempleadoP.setText(empleado);
    }

    public static void actualizarEstadoP(String estado) {
        lblestadoP.setText(estado);
    }

    public static void actualizarnnumero_turnoP(String numero_turno) {
        lblnumeroturnoP.setText(numero_turno);
    }

    private void configurarTabla() {
        Estilo_tablas.configurarTabla(tablalistado);
        Estilo_tablas.configurarTabla(TablaSalida);
        Estilo_tablas.configurarTabla(tablalistadoPrepagos);
        Estilo_tablas.configurarTabla(tablalistadoPrepagos);
    }

    // Timer para refrescar ingresos
    private javax.swing.Timer ingresosTimer;

    //TIMER DE INGRSOS PARA MOSTRAR EN LA TABLA
    private void iniciarTimerIngresos() {
        final int intervalo = 120_000; // 3 minutos en milisegundos
        ingresosTimer = new javax.swing.Timer(intervalo, e -> {
            System.out.println("TIMER >> Actualizando tabla de ingresos...");
            mostrar();
        });
        ingresosTimer.start();
    }

// Si quieres detenerlo
    private void detenerTimerIngresos() {
        if (ingresosTimer != null && ingresosTimer.isRunning()) {
            ingresosTimer.stop();
        }
    }

    //MOSTAR LOS VALORES EN LA TABLA DE JMENUPRIN
    //MIGRADO, FUNCIONANDO Y CON TIMER
    private void mostrar() {
        try {

            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            URL url = new URL(baseUrl + "/api/ingresos/escritorio");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accep", "application/json");

            if (con.getResponseCode() == 200) {
                StringBuilder response;
                try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                    response = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                }

                Gson gson = new Gson();
                java.lang.reflect.Type listType = new TypeToken<List<Dingresop>>() {
                }.getType();
                List<Dingresop> ingresos = gson.fromJson(response.toString(), listType);

                DefaultTableModel model = new DefaultTableModel();
                model.addColumn("ID");
                model.addColumn("Turno");
                model.addColumn("Placa");
                model.addColumn("Fecha Ingreso");
                model.addColumn("Tipo Vehículo");
                model.addColumn("Tipo Servicio");
                model.addColumn("Cliente");
                model.addColumn("Zona");
                model.addColumn("Observaciones");
                model.addColumn("Estado");
                model.addColumn("Número Turno");
                model.addColumn("Empleado");

                for (Dingresop d : ingresos) {
                    model.addRow(new Object[]{
                        d.getIdingreso(),
                        d.getTurno(),
                        d.getPlaca(),
                        d.getFechaingreso(),
                        d.getTipovehiculo(),
                        d.getTiposervicio(),
                        d.getCliente(),
                        d.getZona(),
                        d.getObservaciones(),
                        d.getEstado(),
                        d.getNumeroturno(),
                        d.getEmpleado()
                    });
                }
                tablalistado.setModel(model);
                ocultar_columnas();
                lblregistros.setText("Total Registros: " + ingresos.size());
            } else {
                JOptionPane.showMessageDialog(this, "Error al obtener ingresos: " + con.getResponseCode());
            }

        } catch (JsonSyntaxException | HeadlessException | IOException e) {
            JOptionPane.showMessageDialog(this, "Error de la tabla ingreso del Jmenuprin: " + e.getMessage());
        }

    }

    //TIMER PARA LA TABLA DE SALIDA DE JMENUPRIN
    private void iniciarTimerSalidas() {
        int delay = 0;               // empieza de inmediato
        int interval = 120000;       // cada 3 minutos (en milisegundos)

        salidaTimer = new Timer(interval, e -> {
            try {
                mostrarsalida();
            } catch (Exception ex) {
                Logger.getLogger(Jmenuprin.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        salidaTimer.setInitialDelay(delay);
        salidaTimer.start();
    }

    //Por si se quiere cerrar
    private void detenerTimerSalidas() {
        if (salidaTimer != null && salidaTimer.isRunning()) {
            salidaTimer.stop();
        }
    }

    //MOSTRAR DATOS EN LA TABLA SALIDA
    private void mostrarsalida() throws Exception {
        try {
            DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("ID");
            modelo.addColumn("placa");
            modelo.addColumn("tipovehiculo");
            modelo.addColumn("tiposervicio");
            modelo.addColumn("cliente");
            modelo.addColumn("fechaentreda");
            modelo.addColumn("fechasalida");
            modelo.addColumn("valor");
            modelo.addColumn("descuento");
            modelo.addColumn("subtotal");
            modelo.addColumn("efectivo");
            modelo.addColumn("tarjeta");
            modelo.addColumn("transferencia");
            modelo.addColumn("total");
            modelo.addColumn("Acción");

            SalidaService apiservice = new SalidaService();
            List<Dsalidap> salidas = apiservice.obtenerListaSalidas(); // ← Nueva función sugerida

            for (Dsalidap s : salidas) {
                modelo.addRow(new Object[]{
                    s.getIdsalida(),
                    s.getPlaca(),
                    s.getTipovehiculo(),
                    s.getTiposervicio(),
                    s.getCliente(),
                    s.getFechaentrada(),
                    s.getFechasalida(),
                    s.getValor(),
                    s.getDescuento(),
                    s.getSubtotal(),
                    s.getEfectivo(),
                    s.getTarjeta(),
                    s.getTransferencia(),
                    s.getTotal(),
                    "Editar"
                });
            }

            TablaSalida.setModel(modelo);
            //ocultar_columnasSalidaP();

            TablecActionEvent event = new TablecActionEvent() {
                @Override
                public void onEdit(int row) {
                    if (row >= 0) {
                        DefaultTableModel model = (DefaultTableModel) TablaSalida.getModel();

                        int idSalida = Integer.parseInt(model.getValueAt(row, 0).toString());

                        String efectivo = model.getValueAt(row, 1).toString();
                        String tarjeta = model.getValueAt(row, 2).toString();
                        String transferencia = model.getValueAt(row, 3).toString();
                        String total = model.getValueAt(row, 4).toString();

//                    Fsalidap func = new Fsalidap();
                        boolean actualizado = apiservice.editarSalida(idSalida, efectivo, tarjeta, transferencia, total);

                        if (actualizado) {
                            JOptionPane.showMessageDialog(null, "EDITADO CON ÉXITO",
                                    "Confirmación", JOptionPane.INFORMATION_MESSAGE, icono("/Imagenes/check.png", 40, 40));
                        } else {
                            JOptionPane.showMessageDialog(null, "Error al actualizar la salida.", "Error", JOptionPane.ERROR_MESSAGE);
                        }

                        try {
                            mostrarsalida();
                        } catch (Exception ex) {
                            Logger.getLogger(Jmenuprin.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }

                @Override
                public void onDelete(int row) {
                    // no implementado
                }

                @Override
                public void onView(int row) {
                    System.out.println("VISTO: " + row);
                }
            };

            TablaSalida.getColumnModel().getColumn(14).setCellRenderer(new TableActionCellRender());
            TablaSalida.getColumnModel().getColumn(14).setCellEditor(new TableActionCellEditor(event));

            TableColumn columna = TablaSalida.getColumnModel().getColumn(14);
            columna.setPreferredWidth(100);
            columna.setMinWidth(100);
            columna.setMaxWidth(100);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "Error al cargar salidas: " + e.getMessage());
        }

        mostrarTotalesSalida();
    }

    private void mostrarTotalesSalida() throws Exception {

        SalidaService api = new SalidaService();
        int turno = numeroTurno;
        System.out.println("Numero de turno taride desde la función nueva: " + turno);
        TotalesSalidaDTO dto = SalidaService.obtenerTotalesSalida();

        if (dto != null) {
            int efectivo = dto.getEfectivo();
            int tarjeta = dto.getTarjeta();
            int transferencia = dto.getTransferencia();
            int suma = efectivo + tarjeta + transferencia;

            TxtTotalEfectivossa.setText(formatoMiles.format(efectivo));
            TxtTotalTarjetassa.setText(formatoMiles.format(tarjeta));
            TxtTotalTransferenciassa.setText(formatoMiles.format(transferencia));
            TxtTotal.setText(formatoMiles.format(suma));
        } else {
            JOptionPane.showMessageDialog(null, "No se pudieron obtener los totales de salida");
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel16 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        lblfechaP = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        lblestadoP = new javax.swing.JLabel();
        lblempleadoP = new javax.swing.JLabel();
        lblturnosP = new javax.swing.JLabel();
        lblnumeroturnoP = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        btnregistrovehiculo = new javax.swing.JButton();
        btnsalidap = new javax.swing.JButton();
        btnentregaturnop = new javax.swing.JButton();
        btnabonos2 = new javax.swing.JButton();
        btavanzado = new javax.swing.JButton();
        Copia_actura = new javax.swing.JButton();
        Copia_Turno = new javax.swing.JButton();
        Copia_Turno1 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        EscritorioP = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablalistado = new javax.swing.JTable();
        lblregistros = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tablalistadoPrepagos = new javax.swing.JTable();
        lblregistroprepagos = new javax.swing.JLabel();
        jToggleButtonverde1 = new javax.swing.JToggleButton();
        jToggleButtonamarillo1 = new javax.swing.JToggleButton();
        jToggleButtonrojo1 = new javax.swing.JToggleButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TablaSalida = new javax.swing.JTable();
        jLabel33 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        TxtTotalEfectivossa = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        TxtTotalTarjetassa = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        TxtTotalTransferenciassa = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        TxtTotal = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        lblidinicioturnoP = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));

        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/GN_Pequeño.png"))); // NOI18N

        jLabel14.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jLabel14.setText("Fecha:");

        lblfechaP.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblfechaP.setText("fecha");

        jLabel13.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jLabel13.setText("Turno:");

        jLabel12.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jLabel12.setText("Empleado:");

        jLabel11.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jLabel11.setText("Estado:");

        lblestadoP.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblestadoP.setText("estado");

        lblempleadoP.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblempleadoP.setText("nombre");

        lblturnosP.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblturnosP.setText("turno");

        lblnumeroturnoP.setText("numero");

        jPanel1.setBackground(new java.awt.Color(51, 153, 255));

        btnregistrovehiculo.setBackground(new java.awt.Color(51, 153, 255));
        btnregistrovehiculo.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        btnregistrovehiculo.setForeground(new java.awt.Color(255, 255, 255));
        btnregistrovehiculo.setText("INGRESO VEHICULO");
        btnregistrovehiculo.setActionCommand("     REGISTRO");
        btnregistrovehiculo.setBorder(null);
        btnregistrovehiculo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnregistrovehiculo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnregistrovehiculoActionPerformed(evt);
            }
        });

        btnsalidap.setBackground(new java.awt.Color(51, 153, 255));
        btnsalidap.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        btnsalidap.setForeground(new java.awt.Color(255, 255, 255));
        btnsalidap.setText("SALIDA VEHICULO");
        btnsalidap.setActionCommand("     REGISTRO");
        btnsalidap.setBorder(null);
        btnsalidap.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnsalidap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsalidapActionPerformed(evt);
            }
        });

        btnentregaturnop.setBackground(new java.awt.Color(51, 153, 255));
        btnentregaturnop.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        btnentregaturnop.setForeground(new java.awt.Color(255, 255, 255));
        btnentregaturnop.setText("ENTREGA TURNO");
        btnentregaturnop.setActionCommand("     REGISTRO");
        btnentregaturnop.setBorder(null);
        btnentregaturnop.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnentregaturnop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnentregaturnopActionPerformed(evt);
            }
        });

        btnabonos2.setBackground(new java.awt.Color(51, 153, 255));
        btnabonos2.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        btnabonos2.setForeground(new java.awt.Color(255, 255, 255));
        btnabonos2.setText("ABONOS");
        btnabonos2.setActionCommand("     REGISTRO");
        btnabonos2.setBorder(null);
        btnabonos2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnabonos2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnabonos2ActionPerformed(evt);
            }
        });

        btavanzado.setBackground(new java.awt.Color(51, 153, 255));
        btavanzado.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        btavanzado.setForeground(new java.awt.Color(255, 255, 255));
        btavanzado.setText("OPCIONES ADMIN");
        btavanzado.setActionCommand("     REGISTRO");
        btavanzado.setBorder(null);
        btavanzado.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btavanzado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btavanzadoActionPerformed(evt);
            }
        });

        Copia_actura.setBackground(new java.awt.Color(51, 153, 255));
        Copia_actura.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        Copia_actura.setForeground(new java.awt.Color(255, 255, 255));
        Copia_actura.setText("COPIA FACTURA");
        Copia_actura.setActionCommand("     REGISTRO");
        Copia_actura.setBorder(null);
        Copia_actura.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        Copia_actura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Copia_acturaActionPerformed(evt);
            }
        });

        Copia_Turno.setBackground(new java.awt.Color(51, 153, 255));
        Copia_Turno.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        Copia_Turno.setForeground(new java.awt.Color(255, 255, 255));
        Copia_Turno.setText("COPIA TURNO");
        Copia_Turno.setActionCommand("     REGISTRO");
        Copia_Turno.setBorder(null);
        Copia_Turno.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        Copia_Turno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Copia_TurnoActionPerformed(evt);
            }
        });

        Copia_Turno1.setBackground(new java.awt.Color(51, 153, 255));
        Copia_Turno1.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        Copia_Turno1.setForeground(new java.awt.Color(255, 255, 255));
        Copia_Turno1.setText("REGITRAR PREPAGO");
        Copia_Turno1.setActionCommand("     REGISTRO");
        Copia_Turno1.setBorder(null);
        Copia_Turno1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        Copia_Turno1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Copia_Turno1ActionPerformed(evt);
            }
        });

        tablalistado.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        tablalistado.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7", "Title 8", "Title 9", "Title 10", "Title 11", "Title 12", "Title 13", "Title 14", "Title 15", "Title 16"
            }
        ));
        tablalistado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablalistadoMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tablalistado);

        lblregistros.setText("Total Registros:");

        jButton2.setBackground(new java.awt.Color(204, 204, 204));
        jButton2.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/impresora.png"))); // NOI18N
        jButton2.setText("Imprimir");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(204, 204, 204));
        jButton5.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Excel.png"))); // NOI18N
        jButton5.setText("Excel");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout EscritorioPLayout = new javax.swing.GroupLayout(EscritorioP);
        EscritorioP.setLayout(EscritorioPLayout);
        EscritorioPLayout.setHorizontalGroup(
            EscritorioPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EscritorioPLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(EscritorioPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 991, Short.MAX_VALUE)
                    .addGroup(EscritorioPLayout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(lblregistros)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2)
                        .addGap(18, 18, 18)
                        .addComponent(jButton5)))
                .addContainerGap())
        );
        EscritorioPLayout.setVerticalGroup(
            EscritorioPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EscritorioPLayout.createSequentialGroup()
                .addGroup(EscritorioPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(EscritorioPLayout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(lblregistros))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, EscritorioPLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(EscritorioPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton2)
                            .addComponent(jButton5))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 389, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("INGRESOS", EscritorioP);

        tablalistadoPrepagos.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        tablalistadoPrepagos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7", "Title 8", "Title 9"
            }
        ));
        tablalistadoPrepagos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablalistadoPrepagosMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tablalistadoPrepagos);

        lblregistroprepagos.setText("Total Registros:");

        jToggleButtonverde1.setBackground(new java.awt.Color(144, 238, 144));
        jToggleButtonverde1.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        jToggleButtonverde1.setText("SUFICIENTE SALDO");
        jToggleButtonverde1.setBorder(null);

        jToggleButtonamarillo1.setBackground(new java.awt.Color(255, 255, 102));
        jToggleButtonamarillo1.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        jToggleButtonamarillo1.setText("POCO SALDO");
        jToggleButtonamarillo1.setBorder(null);

        jToggleButtonrojo1.setBackground(new java.awt.Color(255, 99, 71));
        jToggleButtonrojo1.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        jToggleButtonrojo1.setText("DEFICIENTE ");
        jToggleButtonrojo1.setBorder(null);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(lblregistroprepagos, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jToggleButtonverde1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jToggleButtonamarillo1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jToggleButtonrojo1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane4))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblregistroprepagos)
                    .addComponent(jToggleButtonverde1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jToggleButtonamarillo1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jToggleButtonrojo1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 389, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("SALDOS PREPAGOS", jPanel11);

        TablaSalida.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(TablaSalida);

        jLabel33.setBackground(new java.awt.Color(51, 153, 255));
        jLabel33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/actualizar.png"))); // NOI18N
        jLabel33.setToolTipText("");
        jLabel33.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel33.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel33.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel33.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jLabel33.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel33MouseClicked(evt);
            }
        });

        jLabel37.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel37.setText("Total Efectivo:");

        TxtTotalEfectivossa.setEditable(false);
        TxtTotalEfectivossa.setBackground(new java.awt.Color(204, 255, 255));
        TxtTotalEfectivossa.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N

        jLabel36.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel36.setText("Total Tarjetas:");

        TxtTotalTarjetassa.setEditable(false);
        TxtTotalTarjetassa.setBackground(new java.awt.Color(204, 255, 255));
        TxtTotalTarjetassa.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N

        jLabel35.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel35.setText("Total Transferencias:");

        TxtTotalTransferenciassa.setEditable(false);
        TxtTotalTransferenciassa.setBackground(new java.awt.Color(204, 255, 255));
        TxtTotalTransferenciassa.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N

        jLabel34.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel34.setText("Total:");

        TxtTotal.setEditable(false);
        TxtTotal.setBackground(new java.awt.Color(204, 255, 255));
        TxtTotal.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 991, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel33)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(TxtTotalEfectivossa, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel36)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(TxtTotalTarjetassa, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel35)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(TxtTotalTransferenciassa, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel34)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(TxtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel37)
                        .addComponent(TxtTotalEfectivossa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel36)
                        .addComponent(TxtTotalTarjetassa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel35)
                        .addComponent(TxtTotalTransferenciassa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel34)
                        .addComponent(TxtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel33))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(41, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("SALIDAS", jPanel3);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btavanzado, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnabonos2, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnentregaturnop, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnregistrovehiculo, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnsalidap, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(Copia_actura, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Copia_Turno, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(Copia_Turno1, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnregistrovehiculo, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnsalidap, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnentregaturnop, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnabonos2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btavanzado, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Copia_actura, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Copia_Turno, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Copia_Turno1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        jLabel15.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jLabel15.setText("N°turno:");

        lblidinicioturnoP.setText("ID_Inicioturno");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13)
                            .addComponent(jLabel14))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblturnosP, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(lblfechaP, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(65, 65, 65))
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(lblestadoP, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jLabel15)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                        .addComponent(lblnumeroturnoP, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(lblempleadoP, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addComponent(lblidinicioturnoP)
                        .addGap(606, 606, 606))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(lblturnosP, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblempleadoP, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(11, 11, 11)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel11)
                                .addComponent(lblestadoP)
                                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblnumeroturnoP, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblidinicioturnoP))))
                    .addComponent(jLabel17)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblfechaP)
                        .addComponent(jLabel14)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnregistrovehiculoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnregistrovehiculoActionPerformed
        // Si la sesión está iniciada, procede a mostrar el formulario

        if (ingresoFormularioP == null || !ingresoFormularioP.isVisible()) {
            // Crear una nueva instancia si no está abierto
            ingresoFormularioP = new Jingresop(); // Usando Singleton
            ingresoFormularioP.setVisible(true);
        } else {
            // Enfocar el formulario si ya está abierto
            ingresoFormularioP.setExtendedState(JFrame.NORMAL); // Restaurar si está minimizado
            ingresoFormularioP.toFront(); // Traer al frente
            ingresoFormularioP.requestFocus(); // Solicitar foco
        }
        Jingresop.txtempleado.setText(lblempleadoP.getText());
        Jingresop.lblturnos.setText(lblturnosP.getText());
        Jingresop.txtnumeroturno.setText(lblnumeroturnoP.getText());


    }//GEN-LAST:event_btnregistrovehiculoActionPerformed

    private void btnsalidapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsalidapActionPerformed

        if (salidaFormularioP == null || !salidaFormularioP.isVisible()) {
            // Si no está abierto, crea una nueva instancia o usa el Singleton
            salidaFormularioP = new Jsalidap();
            salidaFormularioP.setVisible(true);
        } else {
            // Si ya está abierto, enfócalo
            salidaFormularioP.setExtendedState(JFrame.NORMAL); // Restaurar si está minimizado
            salidaFormularioP.toFront(); // Traer al frente
            salidaFormularioP.requestFocus(); // Solicitar foco
        }
        Jsalidap.txtempleadosalida.setText(lblempleadoP.getText());
        Jsalidap.lblturno.setText(lblturnosP.getText());
        Jsalidap.txtturnosalida.setText(lblnumeroturnoP.getText());
    }//GEN-LAST:event_btnsalidapActionPerformed

    private void btnentregaturnopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnentregaturnopActionPerformed
        detenerTimerSalidas();
        detenerTimerIngresos();
        Jfinturnop Turnosalida = null;
        try {
            Turnosalida = new Jfinturnop();
        } catch (Exception ex) {
            Logger.getLogger(Jmenuprin.class.getName()).log(Level.SEVERE, null, ex);
        }
        Turnosalida.setVisible(true);
        Turnosalida.toFront();
        Turnosalida.setExtendedState(JFrame.NORMAL);

    }//GEN-LAST:event_btnentregaturnopActionPerformed

    private void tablalistadoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablalistadoMouseClicked

    }//GEN-LAST:event_tablalistadoMouseClicked

    private void btavanzadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btavanzadoActionPerformed
        // TODO add your handling code here:
        detenerTimerIngresos();
        Javanzadop = new LoguinDeAdminp();
        Javanzadop.toFront();
        Javanzadop.setVisible(true);
        sesionIniciadaP = true;
    }//GEN-LAST:event_btavanzadoActionPerformed

    private void tablalistadoPrepagosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablalistadoPrepagosMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tablalistadoPrepagosMouseClicked

    private void btnabonos2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnabonos2ActionPerformed
        // TODO add your handling code here:

        if (abonosFormulariop == null || !abonosFormulariop.isVisible()) {
            // Si no está abierto, crea una nueva instancia o usa el Singleton
            abonosFormulariop = new Jabonosp();
            abonosFormulariop.setVisible(true);
        } else {
            // Si ya está abierto, enfócalo
            abonosFormulariop.setExtendedState(JFrame.NORMAL); // Restaurar si está minimizado
            abonosFormulariop.toFront(); // Traer al frente
            abonosFormulariop.requestFocus(); // Solicitar foco
        }
        Jabonosp.txtempleado.setText(lblempleadoP.getText());
        Jabonosp.txtnumero_turno.setText(lblnumeroturnoP.getText());
        Jabonosp.lblturno.setText(lblturnosP.getText());
    }//GEN-LAST:event_btnabonos2ActionPerformed

    //MIGRADO Y PROBANDO FUNCIONAMIENTO
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        try {
            // Crear un diálogo de carga con barra de progreso
            JDialog loadingDialog = new JDialog((Frame) null, "Imprimiendo...", true);
            JPanel panel = new JPanel(new BorderLayout());

            // Icono personalizado (reemplaza con tu ruta y ajusta el tamaño si deseas)
            ImageIcon iconoLogo = new ImageIcon(new ImageIcon(getClass().getResource("/Imagenes/impresora.png"))
                    .getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
            JLabel iconLabel = new JLabel(iconoLogo, JLabel.CENTER);

            // Texto y barra de progreso
            JLabel lblTexto = new JLabel("Generando impresión...", JLabel.CENTER);
            JProgressBar barra = new JProgressBar();
            barra.setIndeterminate(true);

            panel.add(iconLabel, BorderLayout.NORTH);
            panel.add(lblTexto, BorderLayout.CENTER);
            panel.add(barra, BorderLayout.SOUTH);
            loadingDialog.getContentPane().add(panel);
            loadingDialog.setSize(300, 150);
            loadingDialog.setLocationRelativeTo(null);

            // Ejecutar impresión en un hilo separado
            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() throws Exception {
                    ImprimirArqueo lista = new ImprimirArqueo();
                    lista.ImprimirArqueo();
                    return null;
                }

                @Override
                protected void done() {
                    loadingDialog.dispose();
                    JOptionPane.showMessageDialog(null, "Impresión completada correctamente", "Confirmación",
                            JOptionPane.INFORMATION_MESSAGE, iconoLogo);
                }
            };

            worker.execute();
            loadingDialog.setVisible(true);

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(null, "Ocurrió un error durante la impresión:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_jButton2ActionPerformed

    //MIGRADO Y PROBANDO FUNCIONAMIENTO
    private void Copia_acturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Copia_acturaActionPerformed
        // TODO add your handling code here:
        detenerTimerIngresos();
        detenerTimerSalidas();
        JcopiaFacturSalida CopiaF = new JcopiaFacturSalida();
        CopiaF.setVisible(true);
        CopiaF.toFront();

    }//GEN-LAST:event_Copia_acturaActionPerformed

    public Icon icono(String path, int width, int heigth) {
        Icon img = new ImageIcon(new ImageIcon(getClass().getResource(path)).getImage().getScaledInstance(width, heigth, java.awt.Image.SCALE_SMOOTH));
        return img;
    }

    private void jLabel33MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel33MouseClicked
        // TODO add your handling code here:

        JDialog dialog = new JDialog();
        dialog.setUndecorated(true); // Sin bordes ni botones
        dialog.setSize(250, 120);
        dialog.setLocationRelativeTo(null); // Centrar pantalla
        dialog.setLayout(new BorderLayout());

        // Crear etiqueta con imagen
        JLabel label = new JLabel(new ImageIcon(getClass().getResource("/Imagenes/check.png")), JLabel.CENTER);

        // Barra de progreso
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true); // Modo de carga infinita
        progressBar.setPreferredSize(new Dimension(200, 15));

        // Agregar componentes al diálogo
        dialog.add(label, BorderLayout.CENTER);
        dialog.add(progressBar, BorderLayout.SOUTH);

        // Cerrar después de 2 segundos
        Timer timer = new Timer(2000, e -> dialog.dispose());
        timer.setRepeats(false);
        timer.start();
        try {
            //mostrarsalida();
        } catch (Exception ex) {
            Logger.getLogger(Jmenuprin.class.getName()).log(Level.SEVERE, null, ex);
        }

        dialog.setVisible(true);
    }//GEN-LAST:event_jLabel33MouseClicked

    private void Copia_TurnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Copia_TurnoActionPerformed
        int confirmacion = JOptionPane.showConfirmDialog(
                rootPane,
                "¿Deseas imprimir Cierre de turno?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        );
        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                CierreTurno imprimir = new CierreTurno();
                imprimir.cierreTurno();
            } catch (IOException ex) {
                Logger.getLogger(Jfinturnop.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            // ✅ 1. Llamar API y obtener DTO
            ApiSalidaService api = new ApiSalidaService();
            SalidaTurnoReporteDTO dto = api.obtenerCierreDesdeAPI();

            if (dto == null) {
                JOptionPane.showMessageDialog(null, "No hay cierre disponible");
                return;
            }

            // ✅ 2. Convertir DTO a DataSource
            List<SalidaTurnoReporteDTO> lista = new ArrayList<>();
            lista.add(dto);
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(lista);
            // Parámetros adicionales (ejemplo: firma/logo)
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("Turno", dto.getTurno());
            // ✅ Usamos InputStream porque el parámetro está declarado como java.io.InputStream
            InputStream Gesnnova = getClass().getResourceAsStream("/Imagenes/GN_Pequeño.png");
            if (Gesnnova != null) {
                parametros.put("Gesnnova", Gesnnova);
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró la imagen de Gesnnova en los recursos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
            // ✅ Usamos InputStream porque el parámetro está declarado como java.io.InputStream
            InputStream Combugas = getClass().getResourceAsStream("/Imagenes//Iconocombugas-trans.png");
            if (Gesnnova != null) {
                parametros.put("Combugas", Combugas);
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró la imagen de Combugas en los recursos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
            // ✅ 3. Cargar reporte
            String reportePath = new File("").getAbsolutePath() + "/src/main/java/ReporteP/Cierre_Turno.jrxml";
            JasperReport report = JasperCompileManager.compileReport(reportePath);

            // ✅ 5. Llenar el reporte con el DTO (NO BD)
            JasperPrint print = JasperFillManager.fillReport(report, parametros, dataSource);

            // ✅ 6. FILECHOOSER
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Guardar Reporte");
            fileChooser.setSelectedFile(new File(dto.getEmpleado().replace("/", "-") + ".pdf"));

            int userSelection = fileChooser.showSaveDialog(null);

            if (userSelection == JFileChooser.APPROVE_OPTION) {

                File archivoSeleccionado = fileChooser.getSelectedFile();
                String rutaSalida = archivoSeleccionado.getAbsolutePath();

                if (!rutaSalida.toLowerCase().endsWith(".pdf")) {
                    rutaSalida += ".pdf";
                }

                JasperExportManager.exportReportToPdfFile(print, rutaSalida);

                File archivo = new File(rutaSalida);
                if (archivo.exists()) {
                    Desktop.getDesktop().open(archivo);
                }

                JOptionPane.showMessageDialog(null, "Reporte generado en:\n" + rutaSalida);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }//GEN-LAST:event_Copia_TurnoActionPerformed

    //MIGRADO CON APIREPORTEPREPAGO
    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        IngresoService reporte = new IngresoService();
        reporte.exportarExcel(tablalistado);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void Copia_Turno1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Copia_Turno1ActionPerformed
        // TODO add your handling code here:

        if (registroPrepago == null || !registroPrepago.isVisible()) {
            // Crear una nueva instancia si no está abierto
            registroPrepago = new RegitrarPrepago(); // Usando Singleton
            registroPrepago.setVisible(true);
        } else {
            // Enfocar el formulario si ya está abierto
            registroPrepago.setExtendedState(JFrame.NORMAL); // Restaurar si está minimizado
            registroPrepago.toFront(); // Traer al frente
            registroPrepago.requestFocus(); // Solicitar foco
        }
    }//GEN-LAST:event_Copia_Turno1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(() -> {
            try {
                new Jmenuprin().setVisible(true);
            } catch (Exception ex) {
                Logger.getLogger(Jmenuprin.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Copia_Turno;
    private javax.swing.JButton Copia_Turno1;
    private javax.swing.JButton Copia_actura;
    public static javax.swing.JPanel EscritorioP;
    private javax.swing.JTable TablaSalida;
    private javax.swing.JTextField TxtTotal;
    private javax.swing.JTextField TxtTotalEfectivossa;
    private javax.swing.JTextField TxtTotalTarjetassa;
    private javax.swing.JTextField TxtTotalTransferenciassa;
    private javax.swing.JButton btavanzado;
    private javax.swing.JButton btnabonos2;
    private javax.swing.JButton btnentregaturnop;
    private javax.swing.JButton btnregistrovehiculo;
    private javax.swing.JButton btnsalidap;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToggleButton jToggleButtonamarillo1;
    private javax.swing.JToggleButton jToggleButtonrojo1;
    private javax.swing.JToggleButton jToggleButtonverde1;
    private static javax.swing.JLabel lblempleadoP;
    private static javax.swing.JLabel lblestadoP;
    public static javax.swing.JLabel lblfechaP;
    public static javax.swing.JLabel lblidinicioturnoP;
    private static javax.swing.JLabel lblnumeroturnoP;
    private javax.swing.JLabel lblregistroprepagos;
    private javax.swing.JLabel lblregistros;
    private static javax.swing.JLabel lblturnosP;
    private javax.swing.JTable tablalistado;
    private javax.swing.JTable tablalistadoPrepagos;
    // End of variables declaration//GEN-END:variables
}
