package Presentacion;

import Datos.BloqueosAdministrativoDTO;
import Datos.EmpleadoAdminDTO;
import Datos.TarifasAdminResDTO;
import Datos.ZonaAdminDTO;
import EstilosExcelPDF.Metodo_Excel_xlsx;
import Diseños.Estilo_tablas;
import Logica.ApiSalidaService;
import LogicaP.AplicarIcono;
import Logica.BloqueoAdminService;
import Logica.CopiaCierreTurnoService;
import Logica.EmpleadoService;
import Logica.ReportePrepagoService;
import Logica.TarifasAdminService;
import Logica.ZonaService;
import LogicaP.InformePorFecha;
import ReporteP.SalidaTurnoReporteDTO;
import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class Javanzadop extends javax.swing.JFrame {

    private static final long serialVersionUID = 1L;

    public Javanzadop() {
        initComponents();
        AplicarIcono.icono(this, "/Imagenes/GN_Grande.png");
        mostrarp("");
        mostrarzona("");
        mostrart("");
        mostrarb("");
        mostrarBQ("");
        mostrarprepagos("");
        mostrarCopia("");
        inhabilitar();
        habilitar();
        Metodo_Excel_xlsx reportefecha = new Metodo_Excel_xlsx(); // Inicializamos el servicio
        reportefecha.llenarcboturno(cboturno1);
        reportefecha.llenarcboturno(cboturno2);
        reportefecha.llenarcboturno(cboturno3);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        configurarTabla();
        FiltrarBusqueda();

    }

    private void FiltrarBusqueda() {
        DocumentListener filtroListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                aplicarFiltro();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                aplicarFiltro();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                aplicarFiltro();
            }
        };

        txtBuscar1.getDocument().addDocumentListener(filtroListener);
        txtBuscar2.getDocument().addDocumentListener(filtroListener);
        txtBuscar3.getDocument().addDocumentListener(filtroListener);

    }

    private void aplicarFiltro() {
        @SuppressWarnings("unchecked")
        TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) TablaPrepagos.getRowSorter();

        if (sorter != null) {
            String texto1 = txtBuscar1.getText().trim();
            String texto2 = txtBuscar2.getText().trim();
            String texto3 = txtBuscar3.getText().trim();

            // Crear filtro para combinar las tres búsquedas
            RowFilter<DefaultTableModel, Object> filtro1 = RowFilter.regexFilter("(?i)" + texto1);
            RowFilter<DefaultTableModel, Object> filtro2 = RowFilter.regexFilter("(?i)" + texto2);
            RowFilter<DefaultTableModel, Object> filtro3 = RowFilter.regexFilter("(?i)" + texto3);

            // Aplicar los filtros combinados (coincide con cualquiera de los tres)
            List<RowFilter<DefaultTableModel, Object>> filtros = Arrays.asList(filtro1, filtro2, filtro3);
            sorter.setRowFilter(RowFilter.andFilter(filtros));
        }
    }

//MIGRADO Y PROBANDO FUNCIONAMIENTO
    private void mostrarBQ(String buscar) {
        try {
            DefaultTableModel modelo;
            BloqueoAdminService func = new BloqueoAdminService();
            modelo = func.mostrar(buscar);

            tablalistadoBQ.setModel(modelo);
            ocultar_columnas();
            lblregistrosBQ.setText("Total Registros " + Integer.toString(func.totalregistros));

        } catch (Exception e) {
            JOptionPane.showConfirmDialog(rootPane, e);
        }
    }

    private String accion = "guardar";

    public void imprimirFilaSeleccionada(JTable tabla) throws IOException {
        int fila = tablalistadoCopiaTurno.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Debe seleccionar una fila para imprimir.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // ✅ Numero de turno (para la API)
            String numeroTurno = tablalistadoCopiaTurno.getValueAt(fila, 2).toString();
            System.out.println("Número de turno: " + numeroTurno);

            // ✅ Nombre del turno (para el PDF)
            String nombreTurno = tablalistadoCopiaTurno.getValueAt(fila, 1).toString();
            System.out.println("Nombre del turno: " + nombreTurno);

            // ✅ Consumir API usando solo el número
            ApiSalidaService apiService = new ApiSalidaService();
            List<SalidaTurnoReporteDTO> lista = apiService.getCopiaCierreTurno(numeroTurno);

            if (lista.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No se encontró información del turno.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // ✅ Limpiar el nombre del turno para usarlo como nombre de archivo
            String nombreLimpio = nombreTurno
                    .replace(" ", "_")
                    .replace("/", "-")
                    .replace("\\", "-")
                    .replace(":", "-");

            String nombrePDF = nombreLimpio + ".pdf";

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(lista);

            Map<String, Object> parametros = new HashMap<>();
            parametros.put("turno", nombreTurno);

            // ✅ Usamos InputStream porque el parámetro está declarado como java.io.InputStream
            InputStream Gesnnova = getClass().getResourceAsStream("/Imagenes/GN_Pequeño.png");
            if (Gesnnova != null) {
                parametros.put("Gesnnova", Gesnnova);
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró la imagen de Gesnnova en los recursos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
            // ✅ Usamos InputStream porque el parámetro está declarado como java.io.InputStream
            InputStream Combugas = getClass().getResourceAsStream("/Imagenes/Iconocombugas-trans.png");
            if (Gesnnova != null) {
                parametros.put("Combugas", Combugas);
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró la imagen de Combugas en los recursos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }

            String rutaReporte = new File("").getAbsolutePath() + "/src/main/java/ReporteP/Copia_Cierre_Turno.jrxml";
            JasperReport reporte = JasperCompileManager.compileReport(rutaReporte);

            JasperPrint print = JasperFillManager.fillReport(reporte, parametros, dataSource);

            // ✅ Selector para guardar con el nombre del turno
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Guardar Reporte");
            fileChooser.setSelectedFile(new File(nombrePDF));

            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {

                String rutaSalida = fileChooser.getSelectedFile().getAbsolutePath();

                JasperExportManager.exportReportToPdfFile(print, rutaSalida);

                File archivo = new File(rutaSalida);
                if (archivo.exists()) {
                    Desktop.getDesktop().open(archivo);
                }

                JOptionPane.showMessageDialog(null, "Reporte generado con éxito");
            }

        } catch (HeadlessException | IOException | JRException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al imprimir: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    void ocultar_columnas() {

        int[] columnasOcultas = {0, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        Estilo_tablas.ocultarColumnas(tablalistadoP, columnasOcultas);

        int[] columnasOcultasB = {0};
        Estilo_tablas.ocultarColumnas(tablalistadob, columnasOcultasB);

        int[] columnasOcultasBQ = {0};
        Estilo_tablas.ocultarColumnas(tablalistadoBQ, columnasOcultasBQ);

        int[] columnasOcultasZONA = {0};
        Estilo_tablas.ocultarColumnas(tablalistadozona, columnasOcultasZONA);

        int[] columnasOcultasTF = {0};
        Estilo_tablas.ocultarColumnas(tablalistadot, columnasOcultasTF);

    }

    //MIGRADO Y PROBANDO FUNCIONAMIENTO (SI LOS ESTÁ MOSTRANDO)
    private void mostrarb(String buscar) {
        try {
            DefaultTableModel modelo;
            BloqueoAdminService func = new BloqueoAdminService();
            modelo = func.mostrar(buscar);

            tablalistadob.setModel(modelo);
            ocultar_columnas();
            lblregistrosb.setText("Total Registros " + Integer.toString(func.totalregistros));

        } catch (Exception e) {
            JOptionPane.showConfirmDialog(rootPane, e);
        }
    }

    private void configurarTabla() {
        Estilo_tablas.configurarTabla(tablalistadoBQ);
        Estilo_tablas.configurarTabla(tablalistadoP);
        Estilo_tablas.configurarTabla(tablalistadob);

        Estilo_tablas.configurarTabla(tablalistadoinfo);
        Estilo_tablas.configurarTabla(tablalistador);
        Estilo_tablas.configurarTabla(tablalistadot);
        Estilo_tablas.configurarTabla(tablalistadozona);
    }

    private void inhabilitar() {
        lbleps.setVisible(false);
        lblarl.setVisible(false);
        txteps.setVisible(false);
        txtarl.setVisible(false);
        txtidempleado.setVisible(false);
        txtidzona.setVisible(false);
        txtidtarifas.setVisible(false);
        txtidbloqueo.setVisible(false);
        lblacceso.setVisible(false);
        lblarl.setVisible(false);
        lbleps.setVisible(false);
        txtarl.setVisible(false);
        txteps.setVisible(false);
        txtidzona.setVisible(false);
//        txtidcliente.setVisible(false);

    }

    private void habilitar() {
        txtidempleado.setText("");
        txtnombres.setText("");
        txtapellidos.setText("");
        txtdocumento.setText("");
        txtdireccion.setText("");
        txttelefono.setText("");
        txtemail.setText("");
        txtpais.setText("");
        txtciudad.setText("");
        txtlogin.setText("");
        txtpassword.setText("");
        cboestado.setEnabled(true);
        txteps.setText("");
        txtarl.setText("");
        txtfechaentrada.setText("d-m-Y");
        txtfechasalida.setText("d-m-Y");
        txtfecha_turno.setText("'01-01-2025'");
        txttipovehiculo.setText("");
        txtprecio12h.setText("");
        txtdescuentorecibo.setText("");
        txtpreciohoras.setText("");
        txttelefono.setText("");

    }

    //MIGRADO Y PROBANDO FUNCIONAMIENTO
    private void mostrarp(String buscar) {
        try {
            DefaultTableModel modelo;
            EmpleadoService func = new EmpleadoService();
            modelo = func.mostrarp(buscar);

            tablalistadoP.setModel(modelo);
            ocultar_columnas();
            lbltotalregistrop.setText("Total Registros " + Integer.toString(func.totalregistros));

        } catch (Exception e) {
            JOptionPane.showConfirmDialog(rootPane, e);
        }
    }

    //MIGRADO Y PROBANDO FUNCIONAMIENTO
    private void mostrarzona(String buscar) {
        try {
            DefaultTableModel modelo;
            ZonaService func = new ZonaService();
            modelo = func.mostrar(buscar);

            tablalistadozona.setModel(modelo);
            lblregistrozona.setText("Total Registros " + Integer.toString(func.totalregistros));

        } catch (Exception e) {
            JOptionPane.showConfirmDialog(rootPane, e);
        }
    }

    //MIGRADO Y PROBANDO FUNCIONAMIENTO
    private void mostrart(String buscar) {
        try {
            DefaultTableModel modelo;
            TarifasAdminService func = new TarifasAdminService();
            modelo = func.mostrar(buscar);

            tablalistadot.setModel(modelo);
            lbltotalregistro.setText("Total Registros " + Integer.toString(func.totalregistros));

        } catch (Exception e) {
            JOptionPane.showConfirmDialog(rootPane, e);
        }
    }

    //MIGRADO Y PROBANDO FUNCIONAMIENTO
    private void mostrarprepagos(String buscar) {
        try {
            DefaultTableModel modelo;
            ReportePrepagoService func = new ReportePrepagoService();
            modelo = func.exportarReportePrepagos(TablaPrepagos);

            TablaPrepagos.setModel(modelo);
            ocultar_columnas();

        } catch (Exception e) {
            JOptionPane.showConfirmDialog(rootPane, e);
        }
    }

    //MIGRADO Y PROBANDO FUNCIONAMIENTO
    private void mostrarCopia(String buscar) {
        try {
            DefaultTableModel modelo;
            CopiaCierreTurnoService func = new CopiaCierreTurnoService();
            modelo = func.mostrar(buscar);

            tablalistadoCopiaTurno.setModel(modelo);

        } catch (Exception e) {
            JOptionPane.showConfirmDialog(rootPane, e);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel6 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        cbotipo_bloqueo = new javax.swing.JComboBox<>();
        txtbloqueo = new javax.swing.JTextField();
        txtidbloqueo = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        btnguardarb = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablalistadob = new javax.swing.JTable();
        btneliminarb = new javax.swing.JButton();
        lblregistrosb = new javax.swing.JLabel();
        btnnuevob = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel7 = new javax.swing.JPanel();
        cboestado = new javax.swing.JComboBox();
        jLabel15 = new javax.swing.JLabel();
        txtidempleado = new javax.swing.JTextField();
        jptabla_empleado = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        txtbuscar = new javax.swing.JTextField();
        btnbuscar1 = new javax.swing.JButton();
        btneliminar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablalistadoP = new javax.swing.JTable();
        lbltotalregistrop = new javax.swing.JLabel();
        txtlogin = new javax.swing.JTextField();
        txtpassword = new javax.swing.JPasswordField();
        jLabel1 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        cboacceso = new javax.swing.JComboBox();
        txtnombres = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtapellidos = new javax.swing.JTextField();
        txtdocumento = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        comtipodocumento = new javax.swing.JComboBox<>();
        txtdireccion = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txttelefono = new javax.swing.JTextField();
        txtemail = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtpais = new javax.swing.JTextField();
        txtciudad = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txteps = new javax.swing.JTextField();
        txtarl = new javax.swing.JTextField();
        lblarl = new javax.swing.JLabel();
        lbleps = new javax.swing.JLabel();
        btnGuardarp = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tablalistadot = new javax.swing.JTable();
        lbltotalregistro = new javax.swing.JLabel();
        btneliminart = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txtdescuentorecibo = new javax.swing.JTextField();
        txtidtarifas = new javax.swing.JTextField();
        btnguardart = new javax.swing.JButton();
        btnnuevot = new javax.swing.JButton();
        txtprecio12h = new javax.swing.JTextField();
        txttipovehiculo = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txtpreciohoras = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        txttiposervicio = new javax.swing.JTextField();
        jPanel18 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        cboturno1 = new javax.swing.JComboBox<>();
        cboturno2 = new javax.swing.JComboBox<>();
        cboturno3 = new javax.swing.JComboBox<>();
        jScrollPane8 = new javax.swing.JScrollPane();
        tablalistador = new javax.swing.JTable();
        btnconsultar = new javax.swing.JButton();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        btnBuscarReporte2 = new javax.swing.JButton();
        jLabel41 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        tablalistadoinfo = new javax.swing.JTable();
        txtfechaentrada = new javax.swing.JTextField();
        txtfechasalida = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        BTNnuevo = new javax.swing.JButton();
        btnExcel = new javax.swing.JButton();
        btnConsultar = new javax.swing.JButton();
        btnBuscarReporte1 = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        txtfecha_turno = new javax.swing.JTextField();
        jPanel17 = new javax.swing.JPanel();
        txtnumerozona = new javax.swing.JTextField();
        cboestadozona = new javax.swing.JComboBox<>();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        btnguardarz = new javax.swing.JButton();
        btnnuevoz = new javax.swing.JButton();
        txtidzona = new javax.swing.JTextField();
        cbocalle = new javax.swing.JComboBox<>();
        lblregistrozona = new javax.swing.JLabel();
        lblacceso = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tablalistadozona = new javax.swing.JTable();
        jPanel12 = new javax.swing.JPanel();
        jLabel42 = new javax.swing.JLabel();
        cbotipo_bloqueo1 = new javax.swing.JComboBox<>();
        txtbloqueo1 = new javax.swing.JTextField();
        txtidbloqueo1 = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        btnguardarBQ = new javax.swing.JButton();
        btneliminar1 = new javax.swing.JButton();
        lblregistrosBQ = new javax.swing.JLabel();
        btnnuevoBQ = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        tablalistadoBQ = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        TablaPrepagos = new javax.swing.JTable();
        txtBuscar3 = new javax.swing.JTextField();
        jLabel45 = new javax.swing.JLabel();
        btnExcel1 = new javax.swing.JButton();
        jLabel46 = new javax.swing.JLabel();
        txtBuscar1 = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        txtBuscar2 = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel44 = new javax.swing.JLabel();
        txtTurnos = new javax.swing.JTextField();
        jScrollPane9 = new javax.swing.JScrollPane();
        tablalistadoCopiaTurno = new javax.swing.JTable();
        jButton5 = new javax.swing.JButton();
        btnImprimir = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();

        jLabel35.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jLabel35.setText("Tipo Bloqueo:");

        cbotipo_bloqueo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccionar", "Eliminar", "Modificar", "Otros", " ", " " }));

        jLabel36.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jLabel36.setText("Codigo:");

        btnguardarb.setBackground(new java.awt.Color(102, 102, 102));
        btnguardarb.setForeground(new java.awt.Color(255, 255, 255));
        btnguardarb.setText("Guardar");
        btnguardarb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnguardarbActionPerformed(evt);
            }
        });

        jScrollPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane1MouseClicked(evt);
            }
        });

        tablalistadob.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        tablalistadob.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3"
            }
        ));
        tablalistadob.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablalistadobMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablalistadob);

        btneliminarb.setBackground(new java.awt.Color(204, 204, 204));
        btneliminarb.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        btneliminarb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/eliminar.png"))); // NOI18N
        btneliminarb.setText("Eliminar");
        btneliminarb.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btneliminarb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btneliminarbActionPerformed(evt);
            }
        });

        lblregistrosb.setText("Registros");

        btnnuevob.setBackground(new java.awt.Color(102, 102, 102));
        btnnuevob.setForeground(new java.awt.Color(255, 255, 255));
        btnnuevob.setText("Nuevo");
        btnnuevob.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnnuevobActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel35)
                            .addComponent(jLabel36))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtidbloqueo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbotipo_bloqueo, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtbloqueo, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnnuevob)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnguardarb)
                        .addGap(30, 30, 30)))
                .addGap(39, 39, 39)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addComponent(lblregistrosb)
                        .addGap(114, 114, 114)
                        .addComponent(btneliminarb))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(442, Short.MAX_VALUE))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btneliminarb, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cbotipo_bloqueo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel35)
                        .addComponent(lblregistrosb)))
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtbloqueo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel36))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtidbloqueo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(80, 80, 80)
                        .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnnuevob, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnguardarb, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(34, Short.MAX_VALUE))
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(24, Short.MAX_VALUE))))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 249, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));

        jTabbedPane1.setBackground(new java.awt.Color(102, 204, 255));

        cboestado.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        cboestado.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Activo", "Finalizado" }));

        jLabel15.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel15.setText("Estado:");

        txtidempleado.setText("IDP");
        txtidempleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtidempleadoActionPerformed(evt);
            }
        });

        jptabla_empleado.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel17.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jLabel17.setText("Buscar:");

        txtbuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtbuscarActionPerformed(evt);
            }
        });

        btnbuscar1.setBackground(new java.awt.Color(204, 204, 204));
        btnbuscar1.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        btnbuscar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/buscar.png"))); // NOI18N
        btnbuscar1.setText("Buscar");
        btnbuscar1.setBorder(null);
        btnbuscar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbuscar1ActionPerformed(evt);
            }
        });

        btneliminar.setBackground(new java.awt.Color(204, 204, 204));
        btneliminar.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        btneliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/eliminar.png"))); // NOI18N
        btneliminar.setText("Eliminar");
        btneliminar.setBorder(null);
        btneliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btneliminarActionPerformed(evt);
            }
        });

        tablalistadoP.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        tablalistadoP.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Nombres", "Apellidos", "Documento"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tablalistadoP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablalistadoPMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tablalistadoP);

        lbltotalregistrop.setText("Total registro");

        javax.swing.GroupLayout jptabla_empleadoLayout = new javax.swing.GroupLayout(jptabla_empleado);
        jptabla_empleado.setLayout(jptabla_empleadoLayout);
        jptabla_empleadoLayout.setHorizontalGroup(
            jptabla_empleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jptabla_empleadoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jptabla_empleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jptabla_empleadoLayout.createSequentialGroup()
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtbuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnbuscar1, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btneliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(47, 47, 47)
                        .addComponent(lbltotalregistrop)
                        .addGap(0, 22, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jptabla_empleadoLayout.setVerticalGroup(
            jptabla_empleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jptabla_empleadoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jptabla_empleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jptabla_empleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnbuscar1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btneliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jptabla_empleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtbuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17)))
                    .addComponent(lbltotalregistrop))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2)
                .addContainerGap())
        );

        txtlogin.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N

        txtpassword.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txtpassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtpasswordActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel1.setText("Usuario:");

        jLabel9.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel9.setText("Contraseña:");

        jLabel5.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel5.setText("Acceso:");

        cboacceso.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        cboacceso.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Seleccionar", "Empleado", "Administrador", "General" }));

        txtnombres.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txtnombres.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnombresActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel2.setText("Nombres:");

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel3.setText("Apellidos:");

        txtapellidos.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txtapellidos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtapellidosActionPerformed(evt);
            }
        });

        txtdocumento.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txtdocumento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtdocumentoActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel6.setText("Documento:");

        jLabel7.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel7.setText("Tipo Documento:");

        comtipodocumento.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        comtipodocumento.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccionar", "Tarjeta de identidad", "Cedula de ciudadania", "Cedula extrangeria ", "Pasaporte", " " }));
        comtipodocumento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comtipodocumentoActionPerformed(evt);
            }
        });

        txtdireccion.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txtdireccion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtdireccionActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel8.setText("Dirección:");

        jLabel10.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel10.setText("Teléfono:");

        txttelefono.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txttelefono.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txttelefonoActionPerformed(evt);
            }
        });

        txtemail.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txtemail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtemailActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel11.setText("Email:");

        txtpais.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txtpais.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtpaisActionPerformed(evt);
            }
        });

        txtciudad.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txtciudad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtciudadActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel13.setText(" Ciudad:");

        jLabel12.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel12.setText("Pais :");

        txteps.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N

        txtarl.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N

        lblarl.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lblarl.setText("ARL:");

        lbleps.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lbleps.setText("EPS:");

        btnGuardarp.setBackground(new java.awt.Color(204, 204, 204));
        btnGuardarp.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        btnGuardarp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/disco-flexible.png"))); // NOI18N
        btnGuardarp.setText("Guardar");
        btnGuardarp.setBorder(null);
        btnGuardarp.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnGuardarp.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        btnGuardarp.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnGuardarp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarpActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(204, 204, 204));
        jButton1.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/registro_1.png"))); // NOI18N
        jButton1.setText("Nuevo");
        jButton1.setBorder(null);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(66, 66, 66)
                                .addComponent(jLabel13))
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel7Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel7Layout.createSequentialGroup()
                                            .addGap(51, 51, 51)
                                            .addComponent(jLabel2))
                                        .addGroup(jPanel7Layout.createSequentialGroup()
                                            .addGap(49, 49, 49)
                                            .addComponent(jLabel3))
                                        .addGroup(jPanel7Layout.createSequentialGroup()
                                            .addGap(34, 34, 34)
                                            .addComponent(jLabel6))
                                        .addComponent(jLabel7)
                                        .addGroup(jPanel7Layout.createSequentialGroup()
                                            .addGap(47, 47, 47)
                                            .addComponent(jLabel8))
                                        .addGroup(jPanel7Layout.createSequentialGroup()
                                            .addGap(52, 52, 52)
                                            .addComponent(jLabel10))
                                        .addGroup(jPanel7Layout.createSequentialGroup()
                                            .addGap(77, 77, 77)
                                            .addComponent(jLabel11))))
                                .addGroup(jPanel7Layout.createSequentialGroup()
                                    .addGap(86, 86, 86)
                                    .addComponent(jLabel12))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                                            .addComponent(jLabel1)
                                            .addGap(1, 1, 1))
                                        .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtnombres, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtapellidos, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtdocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtlogin, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtpassword, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txttelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtemail, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtpais, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtciudad, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblarl, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(lbleps, javax.swing.GroupLayout.Alignment.TRAILING))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txteps, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtarl, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(txtidempleado, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(comtipodocumento, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtdireccion, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE))))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(44, 44, 44)
                                .addComponent(jLabel15))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel5)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cboacceso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboestado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(115, 115, 115))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel7Layout.createSequentialGroup()
                        .addComponent(btnGuardarp, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 76, Short.MAX_VALUE)
                .addComponent(jptabla_empleado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel2))
                    .addComponent(txtnombres, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel3))
                    .addComponent(txtapellidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel6))
                    .addComponent(txtdocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel7))
                    .addComponent(comtipodocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel8))
                    .addComponent(txtdireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel10))
                    .addComponent(txttelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel11))
                    .addComponent(txtemail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbleps)
                            .addComponent(txteps, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblarl)
                            .addComponent(txtarl, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtpais, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(jLabel12)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtciudad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13)
                            .addComponent(txtidempleado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtlogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtpassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboacceso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(cboestado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnGuardarp, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jptabla_empleado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("EMPLEADOS", jPanel7);

        tablalistadot.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        tablalistadot.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null}
            },
            new String [] {
                "TIPO VEHICULO", "GENERAL 12 H", "DESCUENTO RECIBO", "PRECIO * HORAS", "ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tablalistadot.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablalistadotMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tablalistadot);

        lbltotalregistro.setText("Total registros:");

        btneliminart.setBackground(new java.awt.Color(204, 204, 204));
        btneliminart.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        btneliminart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/eliminar.png"))); // NOI18N
        btneliminart.setText("Eliminar");
        btneliminart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btneliminartActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(btneliminart)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 398, Short.MAX_VALUE)
                .addComponent(lbltotalregistro, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbltotalregistro)
                    .addComponent(btneliminart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel4.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jLabel4.setText("PRECIO 12 H;");

        jLabel19.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jLabel19.setText(" DECUENTO RECIBO:");

        txtdescuentorecibo.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txtdescuentorecibo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtdescuentoreciboActionPerformed(evt);
            }
        });

        txtidtarifas.setText("0");

        btnguardart.setBackground(new java.awt.Color(204, 204, 204));
        btnguardart.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        btnguardart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/disco-flexible.png"))); // NOI18N
        btnguardart.setText("Ingresar");
        btnguardart.setBorder(null);
        btnguardart.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnguardart.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        btnguardart.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnguardart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnguardartActionPerformed(evt);
            }
        });

        btnnuevot.setBackground(new java.awt.Color(204, 204, 204));
        btnnuevot.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        btnnuevot.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/registro_1.png"))); // NOI18N
        btnnuevot.setText("Nuevo");
        btnnuevot.setBorder(null);
        btnnuevot.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnnuevot.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        btnnuevot.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnnuevot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnnuevotActionPerformed(evt);
            }
        });

        txtprecio12h.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txtprecio12h.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtprecio12hActionPerformed(evt);
            }
        });

        txttipovehiculo.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txttipovehiculo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txttipovehiculoActionPerformed(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jLabel18.setText("TIPO VEHICULO:");

        txtpreciohoras.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txtpreciohoras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtpreciohorasActionPerformed(evt);
            }
        });
        txtpreciohoras.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtpreciohorasKeyPressed(evt);
            }
        });

        jLabel21.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jLabel21.setText("PRECIO * HORAS:");

        jLabel20.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jLabel20.setText("TIPO SERVICIOS:");

        txttiposervicio.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txttiposervicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txttiposervicioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(btnguardart, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnnuevot, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel19)
                                    .addComponent(jLabel4))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtdescuentorecibo)
                                    .addComponent(txtprecio12h, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(jLabel21)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtidtarifas, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtpreciohoras, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(jLabel20)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txttiposervicio, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(jLabel18)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txttipovehiculo, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel18)
                            .addComponent(txttipovehiculo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(7, 7, 7)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel20)
                            .addComponent(txttiposervicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtprecio12h, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19)
                            .addComponent(txtdescuentorecibo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel21)
                            .addComponent(txtpreciohoras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtidtarifas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(194, 194, 194)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnguardart)
                            .addComponent(btnnuevot))
                        .addGap(0, 65, Short.MAX_VALUE))
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("TARIFAS", jPanel10);

        jButton2.setBackground(new java.awt.Color(204, 204, 204));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Excel.png"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        cboturno1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cboturno1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cboturno2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cboturno2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cboturno3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cboturno3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        tablalistador.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        tablalistador.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6"
            }
        ));
        tablalistador.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablalistadorMouseClicked(evt);
            }
        });
        jScrollPane8.setViewportView(tablalistador);

        btnconsultar.setBackground(new java.awt.Color(204, 204, 204));
        btnconsultar.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        btnconsultar.setForeground(new java.awt.Color(255, 255, 255));
        btnconsultar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/informacion.png"))); // NOI18N
        btnconsultar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnconsultarActionPerformed(evt);
            }
        });

        jLabel29.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jLabel29.setText("Turno 1:");

        jLabel30.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jLabel30.setText("Turno 2:");

        jLabel31.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jLabel31.setText("Turno 3:");

        btnBuscarReporte2.setBackground(new java.awt.Color(204, 204, 204));
        btnBuscarReporte2.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        btnBuscarReporte2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/pdf.png"))); // NOI18N
        btnBuscarReporte2.setBorder(null);
        btnBuscarReporte2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnBuscarReporte2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarReporte2ActionPerformed(evt);
            }
        });

        jLabel41.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jLabel41.setText("Reporte Dia:");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane8)
                .addContainerGap())
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel29)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboturno1, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel30)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboturno2, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel31)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboturno3, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnconsultar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addGap(18, 18, 18)
                .addComponent(jLabel41)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBuscarReporte2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel18Layout.createSequentialGroup()
                            .addGap(4, 4, 4)
                            .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel29)
                                    .addComponent(cboturno1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel30)
                                    .addComponent(cboturno2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel31)
                                    .addComponent(cboturno3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(btnconsultar)
                                .addComponent(jButton2)))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(btnBuscarReporte2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jLabel41)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("REPORTE * TURNOS", jPanel18);

        tablalistadoinfo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        tablalistadoinfo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6"
            }
        ));
        tablalistadoinfo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablalistadoinfoMouseClicked(evt);
            }
        });
        jScrollPane10.setViewportView(tablalistadoinfo);

        txtfechaentrada.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N

        txtfechasalida.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N

        jLabel14.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel14.setText("Fecha inico:");

        jLabel16.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel16.setText("Fecha final:");

        BTNnuevo.setBackground(new java.awt.Color(153, 153, 153));
        BTNnuevo.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        BTNnuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/registro_1.png"))); // NOI18N
        BTNnuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTNnuevoActionPerformed(evt);
            }
        });

        btnExcel.setBackground(new java.awt.Color(204, 204, 204));
        btnExcel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Excel.png"))); // NOI18N
        btnExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcelActionPerformed(evt);
            }
        });

        btnConsultar.setBackground(new java.awt.Color(204, 204, 204));
        btnConsultar.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        btnConsultar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/informacion.png"))); // NOI18N
        btnConsultar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConsultarActionPerformed(evt);
            }
        });

        btnBuscarReporte1.setBackground(java.awt.SystemColor.activeCaptionBorder);
        btnBuscarReporte1.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        btnBuscarReporte1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/pdf.png"))); // NOI18N
        btnBuscarReporte1.setBorder(null);
        btnBuscarReporte1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnBuscarReporte1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarReporte1ActionPerformed(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel23.setText("Fecha('D-M-Y'):");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane10, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtfechaentrada, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtfechasalida, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(28, 28, 28)
                        .addComponent(btnConsultar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnExcel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(BTNnuevo)
                        .addGap(57, 57, 57)
                        .addComponent(jLabel23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtfecha_turno, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBuscarReporte1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel16)
                                    .addComponent(txtfechasalida, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(45, 45, 45))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel14)
                                    .addComponent(txtfechaentrada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(11, 11, 11))))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtfecha_turno, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnConsultar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(BTNnuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnBuscarReporte1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 436, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("REPORTE *FECHAS", jPanel5);

        cboestadozona.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        cboestadozona.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccionar", "Disponible", "Ocupado", "Mantenimiento", " " }));

        jLabel26.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel26.setText("Estado zona:");

        jLabel27.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel27.setText("Numero:");

        jLabel28.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel28.setText("Calle:");

        btnguardarz.setBackground(new java.awt.Color(153, 153, 153));
        btnguardarz.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        btnguardarz.setForeground(new java.awt.Color(255, 255, 255));
        btnguardarz.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/disco-flexible.png"))); // NOI18N
        btnguardarz.setText("Guardar");
        btnguardarz.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnguardarz.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnguardarz.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        btnguardarz.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnguardarz.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnguardarzActionPerformed(evt);
            }
        });

        btnnuevoz.setBackground(new java.awt.Color(153, 153, 153));
        btnnuevoz.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        btnnuevoz.setForeground(new java.awt.Color(255, 255, 255));
        btnnuevoz.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/registro_1.png"))); // NOI18N
        btnnuevoz.setText("Nuevo");
        btnnuevoz.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnnuevoz.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnnuevoz.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        btnnuevoz.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnnuevoz.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnnuevozActionPerformed(evt);
            }
        });

        cbocalle.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        cbocalle.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccionar", "Calle 1", "Calle 2", "Calle 3", "Calle 4" }));

        lblregistrozona.setText("Registro:");

        lblacceso.setText("acceso");

        tablalistadozona.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        tablalistadozona.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tablalistadozona.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablalistadozonaMouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(tablalistadozona);

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGap(237, 237, 237)
                        .addComponent(lblregistrozona))
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel26)
                                    .addComponent(jLabel27, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel28, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtidzona, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtnumerozona, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(cbocalle, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(cboestadozona, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(lblacceso, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(btnguardarz, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnnuevoz, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 745, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(lblregistrozona)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 477, Short.MAX_VALUE))
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cboestadozona, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel26))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbocalle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel28))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel27)
                            .addComponent(txtnumerozona, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addComponent(txtidzona, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblacceso, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnguardarz, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnnuevoz, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );

        jTabbedPane1.addTab("ZONA", jPanel17);

        jLabel42.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jLabel42.setText("Tipo Bloqueo:");

        cbotipo_bloqueo1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccionar", "Eliminar", "Modificar", "Otros", " ", " " }));

        jLabel43.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jLabel43.setText("Codigo:");

        btnguardarBQ.setBackground(new java.awt.Color(204, 204, 204));
        btnguardarBQ.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/disco-flexible.png"))); // NOI18N
        btnguardarBQ.setText("Guardar");
        btnguardarBQ.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnguardarBQ.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        btnguardarBQ.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnguardarBQ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnguardarBQActionPerformed(evt);
            }
        });

        btneliminar1.setBackground(new java.awt.Color(204, 204, 204));
        btneliminar1.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        btneliminar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/eliminar.png"))); // NOI18N
        btneliminar1.setText("Eliminar");
        btneliminar1.setBorder(null);
        btneliminar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btneliminar1ActionPerformed(evt);
            }
        });

        lblregistrosBQ.setText("Registros");

        btnnuevoBQ.setBackground(new java.awt.Color(204, 204, 204));
        btnnuevoBQ.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/registro_1.png"))); // NOI18N
        btnnuevoBQ.setText("Nuevo");
        btnnuevoBQ.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnnuevoBQ.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        btnnuevoBQ.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnnuevoBQ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnnuevoBQActionPerformed(evt);
            }
        });

        tablalistadoBQ.setModel(new javax.swing.table.DefaultTableModel(
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
        tablalistadoBQ.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablalistadoBQMouseClicked(evt);
            }
        });
        tablalistadoBQ.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tablalistadoBQKeyPressed(evt);
            }
        });
        jScrollPane5.setViewportView(tablalistadoBQ);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel42)
                            .addComponent(jLabel43))
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtbloqueo1, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtidbloqueo1, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addComponent(cbotipo_bloqueo1, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(btnnuevoBQ)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnguardarBQ)))
                .addGap(26, 26, 26)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGap(271, 271, 271))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(btneliminar1, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblregistrosBQ)
                        .addGap(280, 280, 280))))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblregistrosBQ)
                    .addComponent(btneliminar1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbotipo_bloqueo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel42))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtbloqueo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel43))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtidbloqueo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnguardarBQ, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnnuevoBQ)))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE)
                        .addGap(14, 14, 14)))
                .addGap(34, 34, 34))
        );

        jTabbedPane1.addTab("BLOQUEO", jPanel12);

        TablaPrepagos.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane6.setViewportView(TablaPrepagos);

        jLabel45.setText("3 Parametro:");

        btnExcel1.setBackground(new java.awt.Color(204, 204, 204));
        btnExcel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnExcel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Excel.png"))); // NOI18N
        btnExcel1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcel1ActionPerformed(evt);
            }
        });

        jLabel46.setText("1 Parametro:");

        jLabel47.setText("2 Parametro:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel46)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtBuscar1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel47)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtBuscar2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel45)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtBuscar3, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnExcel1)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtBuscar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel46))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtBuscar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel47))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtBuscar3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel45)))
                    .addComponent(btnExcel1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("REPORTE PREPAGOS", jPanel1);

        jLabel44.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel44.setText("Buscar * Turno:");

        txtTurnos.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N

        tablalistadoCopiaTurno.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane9.setViewportView(tablalistadoCopiaTurno);

        jButton5.setBackground(new java.awt.Color(204, 204, 204));
        jButton5.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/informacion.png"))); // NOI18N
        jButton5.setText("Buscar");
        jButton5.setBorder(null);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        btnImprimir.setBackground(new java.awt.Color(204, 204, 204));
        btnImprimir.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        btnImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/impresora.png"))); // NOI18N
        btnImprimir.setText("Imprimir");
        btnImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimirActionPerformed(evt);
            }
        });

        jButton7.setBackground(new java.awt.Color(204, 204, 204));
        jButton7.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/pdf.png"))); // NOI18N
        jButton7.setText("Exportar");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane9, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel44)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTurnos, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton7)
                .addContainerGap(379, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtTurnos, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel44))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(15, 15, 15)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 457, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("COPIAS TURNOS", jPanel2);

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addComponent(jTabbedPane1)
                .addGap(0, 0, 0))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnnuevobActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnnuevobActionPerformed
        // TODO add your handling code here:
        txtbloqueo.setText("");
        cbotipo_bloqueo.setSelectedItem("Seleccionar");
    }//GEN-LAST:event_btnnuevobActionPerformed


    private void btneliminarbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btneliminarbActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btneliminarbActionPerformed

    private void jScrollPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane1MouseClicked
        // TODO add your handling code here:
        btnguardarb.setText("Editar");
        btneliminarb.setEnabled(true);
        accion = "editar";

        int fila = tablalistadob.rowAtPoint(evt.getPoint());
        txtidbloqueo.setText(tablalistadob.getValueAt(fila, 0).toString());
        cbotipo_bloqueo.setSelectedItem(tablalistadob.getValueAt(fila, 1).toString());
        txtbloqueo.setText(tablalistadob.getValueAt(fila, 2).toString());
    }//GEN-LAST:event_jScrollPane1MouseClicked

    private void tablalistadobMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablalistadobMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tablalistadobMouseClicked

    //BOTÓN QUE INSERTA EL BLOQUEO 1 POSIBLE DUPLICADO DEL FORMULARIO
    private void btnguardarbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnguardarbActionPerformed

    }//GEN-LAST:event_btnguardarbActionPerformed

    //MIGRADO Y PROBANDO FUNCIONAMIENTO - HACE FALTA PROBAR LA PARTE DEL EXCEL
    private void btnConsultarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConsultarActionPerformed

        //        // Obtener las fechas de los JTextField
        String fechaInicio = txtfechaentrada.getText();
        String fechaFin = txtfechasalida.getText();
        //
        //// Instanciar la clase InformePorFecha
        InformePorFecha informe = new InformePorFecha();

        // Verificar que al menos una fecha esté ingresada
        if (fechaInicio.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese la fecha de inicio.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        //
        //// Si solo se ha ingresado la fecha de inicio
        if (fechaFin.isEmpty()) {
            // Llamar al método con solo la fecha de inicio
            DefaultTableModel modelo = informe.obtenerRegistrosPorFechas(fechaInicio, "", tablalistadoinfo);
            if (modelo != null) {
                tablalistadoinfo.setModel(modelo);
            } else {
                JOptionPane.showMessageDialog(this, "No se pudieron obtener los datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            //            // Si ambas fechas han sido ingresadas
            DefaultTableModel modelo = informe.obtenerRegistrosPorFechas(fechaInicio, fechaFin, tablalistadoinfo);
            if (modelo != null) {
                tablalistadoinfo.setModel(modelo);
            } else {
                JOptionPane.showMessageDialog(this, "No se pudieron obtener los datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

    }//GEN-LAST:event_btnConsultarActionPerformed

    private void btnExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnExcelActionPerformed

    private void BTNnuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTNnuevoActionPerformed
        // TODO add your handling code here:
        habilitar();
    }//GEN-LAST:event_BTNnuevoActionPerformed

    private void tablalistadoinfoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablalistadoinfoMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tablalistadoinfoMouseClicked

    //MIGRADO Y PROBANDO FUNCIONAMIENTO
    private void btnconsultarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnconsultarActionPerformed
        // TODO add your handling code here:
        Metodo_Excel_xlsx consulta = new Metodo_Excel_xlsx();

        cboturno1.getSelectedItem().toString();
        cboturno1.getSelectedItem().toString();
        cboturno1.getSelectedItem().toString();

        consulta.exportarReporteDia(cboturno1, cboturno2, cboturno3, tablalistador);
    }//GEN-LAST:event_btnconsultarActionPerformed

    private void tablalistadorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablalistadorMouseClicked

    }//GEN-LAST:event_tablalistadorMouseClicked

    //MIGRADO Y PROBANDO FUNCIONAMIENTO
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        Metodo_Excel_xlsx metodo = new Metodo_Excel_xlsx();
        metodo.exportarExcel(tablalistador);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnnuevotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnnuevotActionPerformed
        // TODO add your handling code here:
        habilitar();
        btnguardart.setText("Guardar");
        accion = "guardar";
    }//GEN-LAST:event_btnnuevotActionPerformed

    //POR REVISAR BOTON GUARDAR DE TARIFAS - MIGRADO Y PROBANDO FUNCIONAMIENTO
    private void btnguardartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnguardartActionPerformed

        TarifasAdminResDTO dts = new TarifasAdminResDTO();
        TarifasAdminService func = new TarifasAdminService();

        dts.setPrecio12h(Integer.parseInt(txtprecio12h.getText()));
        dts.setPreciohoras(Integer.parseInt(txtpreciohoras.getText()));
        dts.setDescuentorecibo(Integer.parseInt(txtdescuentorecibo.getText()));
        dts.setTipovehiculo(txttipovehiculo.getText());
        dts.setTiposervicio(txttiposervicio.getText());

        if (accion.equals("guardar")) {
            if (func.insertar(dts)) {
                JOptionPane.showMessageDialog(rootPane, "Registrado satisfactoriamente");
                mostrart("");

            }

        } else if (accion.equals("editar")) {
            Integer idtarifas = Integer.valueOf(txtidtarifas.getText());
            if (func.editar(idtarifas, dts)) {
                JOptionPane.showMessageDialog(rootPane, "Editado satisfactoriamente");
                mostrart("");

            }
        }
    }//GEN-LAST:event_btnguardartActionPerformed

    //POR REVISAR BOTÓN ELIMINAR TARIFAS - MIGRADO Y PROBANDO FUNCIONAMIENTO
    private void btneliminartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btneliminartActionPerformed

        if (!txtidtarifas.getText().equals("")) {
            int confirmacion = JOptionPane.showConfirmDialog(rootPane, "Estás seguro de Eliminar el registro?", "Confirmar", 2);

            if (confirmacion == 0) {
                TarifasAdminService func = new TarifasAdminService();
                Integer idtarifas = Integer.valueOf(txtidtarifas.getText());
                func.eliminar(idtarifas);
                mostrart("");
                //                inhabilitar();

            }

        }
    }//GEN-LAST:event_btneliminartActionPerformed

    private void tablalistadotMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablalistadotMouseClicked

        btnguardart.setText("Editar");

        btneliminart.setEnabled(true);
        accion = "editar";

        int fila = tablalistadot.rowAtPoint(evt.getPoint());

        txtidtarifas.setText(tablalistadot.getValueAt(fila, 0).toString());
        txttipovehiculo.setText(tablalistadot.getValueAt(fila, 1).toString());
        txttiposervicio.setText(tablalistadot.getValueAt(fila, 2).toString());
        txtprecio12h.setText(tablalistadot.getValueAt(fila, 3).toString());
        txtdescuentorecibo.setText(tablalistadot.getValueAt(fila, 4).toString());
        txtpreciohoras.setText(tablalistadot.getValueAt(fila, 5).toString());
    }//GEN-LAST:event_tablalistadotMouseClicked

    private void tablalistadozonaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablalistadozonaMouseClicked

        btnguardarz.setText("Editar");
        accion = "editar";

        int fila = tablalistadozona.rowAtPoint(evt.getPoint());
        txtidzona.setText(tablalistadozona.getValueAt(fila, 0).toString());
        cboestadozona.setSelectedItem(tablalistadozona.getValueAt(fila, 1).toString());
        txtnumerozona.setText(tablalistadozona.getValueAt(fila, 2).toString());
        cbocalle.setSelectedItem(tablalistadozona.getValueAt(fila, 3).toString());
    }//GEN-LAST:event_tablalistadozonaMouseClicked

    private void btnnuevozActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnnuevozActionPerformed
        // TODO add your handling code here:
        habilitar();
        btnguardarz.setText("Guardar");
        accion = "guardar";
    }//GEN-LAST:event_btnnuevozActionPerformed

    //MIGRADO Y PROBANDO FUNCIONAMIENTO
    private void btnguardarzActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnguardarzActionPerformed

        ZonaAdminDTO dts = new ZonaAdminDTO();
        ZonaService func = new ZonaService();

        int estado = cboestadozona.getSelectedIndex();
        dts.setEstado(cboestadozona.getItemAt(estado));

        dts.setNumero(txtnumerozona.getText());

        if (accion.equals("guardar")) {
            if (func.insertar(dts)) {
                JOptionPane.showMessageDialog(rootPane, " INGRESO SATISFACTORIAMENTE");
                mostrarzona("");
                //                inhabilitar();
            }

        } else if (accion.equals("editar")) {
            Integer idzona = Integer.valueOf(txtidzona.getText());
            if (func.editar(idzona, dts)) {
                JOptionPane.showMessageDialog(rootPane, "EDITADO SATISFACTORIAMENTE");
                mostrarzona("");
                //                    inhabilitar();

            }
        }
    }//GEN-LAST:event_btnguardarzActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        habilitar();
        btnGuardarp.setText("Guardar");
        accion = "guardar";
    }//GEN-LAST:event_jButton1ActionPerformed

    //MIGRADO Y PROBANDO FUNCIONAMIENTO - REGISTRO Y EDICIÓN DE EMPLEADO
    @SuppressWarnings("deprecation")
    private void btnGuardarpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarpActionPerformed
        // TODO add your handling code here:
        if (txtnombres.getText().length() == 0) {
            JOptionPane.showConfirmDialog(rootPane, "Debes ingresar un Nombre para el empleado");
            txtnombres.requestFocus();
            return;
        }
        if (txtapellidos.getText().length() == 0) {
            JOptionPane.showConfirmDialog(rootPane, "Debes ingresar un apellido para el empleado");
            txtapellidos.requestFocus();
            return;
        }

        if (txtdocumento.getText().length() == 0) {
            JOptionPane.showConfirmDialog(rootPane, "Debes ingresar un Número de Doc para el empleado");
            txtdocumento.requestFocus();
            return;
        }

        if (txtlogin.getText().length() == 0) {
            JOptionPane.showConfirmDialog(rootPane, "Debes ingresar un login para el empleado");
            txtlogin.requestFocus();
            return;
        }
        if (txtpassword.getText().length() == 0) {
            JOptionPane.showConfirmDialog(rootPane, "Debes ingresar un password para el empleado");
            txtpassword.requestFocus();
            return;
        }

        EmpleadoAdminDTO dts = new EmpleadoAdminDTO();
        EmpleadoService func = new EmpleadoService();

        dts.setNombres(txtnombres.getText());
        dts.setApellidos(txtapellidos.getText());

        int seleccionado = comtipodocumento.getSelectedIndex();
        dts.setTipodocumento(comtipodocumento.getItemAt(seleccionado));
        dts.setDocumento(txtdocumento.getText());
        dts.setDireccion(txtdireccion.getText());
        dts.setTelefono(txttelefono.getText());
        dts.setEmail(txtemail.getText());
        dts.setPais(txtpais.getText());
        dts.setCiudad(txtciudad.getText());

        seleccionado = cboacceso.getSelectedIndex();
        dts.setAcceso((String) cboacceso.getItemAt(seleccionado));
        dts.setLogin(txtlogin.getText());
        dts.setPassword(txtpassword.getText());
        dts.setEps(txteps.getText());
        dts.setArl(txtarl.getText());

        seleccionado = cboestado.getSelectedIndex();
        dts.setEstado((String) cboestado.getItemAt(seleccionado));

        if (accion.equals("guardar")) {
            if (func.insertar(dts)) {
                JOptionPane.showMessageDialog(rootPane, "el empleado fue registrado satisfactoriamente");
                mostrarp("");
                inhabilitar();
            }

        } else if (accion.equals("editar")) {
            Integer idempleado = Integer.valueOf(txtidempleado.getText());

            if (func.editar(idempleado, dts)) {
                JOptionPane.showMessageDialog(rootPane, "El empleado fue Editado satisfactoriamente");
                mostrarp("");
                inhabilitar();
            }
        }
    }//GEN-LAST:event_btnGuardarpActionPerformed

    private void txtciudadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtciudadActionPerformed
        // TODO add your handling code here:
        txtciudad.transferFocus();
    }//GEN-LAST:event_txtciudadActionPerformed

    private void txtpaisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtpaisActionPerformed
        // TODO add your handling code here:
        txtpais.transferFocus();
    }//GEN-LAST:event_txtpaisActionPerformed

    private void txtemailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtemailActionPerformed
        // TODO add your handling code here:
        txtemail.transferFocus();
    }//GEN-LAST:event_txtemailActionPerformed

    private void txttelefonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txttelefonoActionPerformed
        // TODO add your handling code here:
        txttelefono.transferFocus();
    }//GEN-LAST:event_txttelefonoActionPerformed

    private void txtdireccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtdireccionActionPerformed
        // TODO add your handling code here:
        txtdireccion.transferFocus();
    }//GEN-LAST:event_txtdireccionActionPerformed

    private void comtipodocumentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comtipodocumentoActionPerformed
        // TODO add your handling code here:
        comtipodocumento.transferFocus();
    }//GEN-LAST:event_comtipodocumentoActionPerformed

    private void txtdocumentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtdocumentoActionPerformed
        // TODO add your handling code here:
        txtdocumento.transferFocus();
    }//GEN-LAST:event_txtdocumentoActionPerformed

    private void txtapellidosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtapellidosActionPerformed
        // TODO add your handling code here:
        txtapellidos.transferFocus();
    }//GEN-LAST:event_txtapellidosActionPerformed

    private void txtnombresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnombresActionPerformed
        // TODO add your handling code here:
        txtnombres.transferFocus();
    }//GEN-LAST:event_txtnombresActionPerformed

    private void txtpasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtpasswordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtpasswordActionPerformed

    private void tablalistadoPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablalistadoPMouseClicked
        // TODO add your handling code here:
        btnGuardarp.setText("Editar");
        habilitar();
        btneliminar.setEnabled(true);
        accion = "editar";

        int fila = tablalistadoP.rowAtPoint(evt.getPoint());

        txtidempleado.setText(tablalistadoP.getValueAt(fila, 0).toString());
        txtnombres.setText(tablalistadoP.getValueAt(fila, 1).toString());
        txtapellidos.setText(tablalistadoP.getValueAt(fila, 2).toString());
        comtipodocumento.setSelectedItem(tablalistadoP.getValueAt(fila, 3).toString());
        txtdocumento.setText(tablalistadoP.getValueAt(fila, 4).toString());
        txttelefono.setText(tablalistadoP.getValueAt(fila, 5).toString());
        txtdireccion.setText(tablalistadoP.getValueAt(fila, 6).toString());
        txtemail.setText(tablalistadoP.getValueAt(fila, 7).toString());
        txtpais.setText(tablalistadoP.getValueAt(fila, 8).toString());
        txtciudad.setText(tablalistadoP.getValueAt(fila, 9).toString());
        cboacceso.setSelectedItem(tablalistadoP.getValueAt(fila, 10).toString());
        txtlogin.setText(tablalistadoP.getValueAt(fila, 11).toString());
        txtpassword.setText(tablalistadoP.getValueAt(fila, 12).toString());
        cboestado.setSelectedItem(tablalistadoP.getValueAt(fila, 13).toString());
        txteps.setText(tablalistadoP.getValueAt(fila, 14).toString());
        txtarl.setText(tablalistadoP.getValueAt(fila, 15).toString());
    }//GEN-LAST:event_tablalistadoPMouseClicked

    //MIGRADO Y PROBANDO FUNCIONAMIENTO
    private void btneliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btneliminarActionPerformed

        if (!txtidempleado.getText().equals("")) {
            int confirmacion = JOptionPane.showConfirmDialog(rootPane, "Estás seguro de Eliminar el empleado?", "Confirmar", 2);

            if (confirmacion == 0) {
                EmpleadoService func = new EmpleadoService();
                Integer idempleado = Integer.valueOf(txtidempleado.getText());

                func.eliminar(idempleado);
                mostrarp("");
                inhabilitar();

            }

        }
    }//GEN-LAST:event_btneliminarActionPerformed

    private void btnbuscar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbuscar1ActionPerformed
        // TODO add your handling code here:
        mostrarp(txtbuscar.getText());
    }//GEN-LAST:event_btnbuscar1ActionPerformed

    private void txtbuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtbuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtbuscarActionPerformed

    private void txtidempleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtidempleadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtidempleadoActionPerformed

    private void txttipovehiculoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txttipovehiculoActionPerformed
        // TODO add your handling code here:
        txttipovehiculo.transferFocus();
    }//GEN-LAST:event_txttipovehiculoActionPerformed

    private void txtprecio12hActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtprecio12hActionPerformed
        // TODO add your handling code here:
        txtprecio12h.transferFocus();
    }//GEN-LAST:event_txtprecio12hActionPerformed

    private void txtdescuentoreciboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtdescuentoreciboActionPerformed
        // TODO add your handling code here:
        txtdescuentorecibo.transferFocus();
    }//GEN-LAST:event_txtdescuentoreciboActionPerformed

    private void txtpreciohorasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtpreciohorasActionPerformed
        // TODO add your handling code here:
        txtpreciohoras.transferFocus();
    }//GEN-LAST:event_txtpreciohorasActionPerformed

    //POR REVISAR KEYPRESSES DE TARIFAS - MIGRADO Y PROBANDO FUNCIONAMIENTO
    private void txtpreciohorasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtpreciohorasKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            TarifasAdminResDTO dts = new TarifasAdminResDTO();
            TarifasAdminService func = new TarifasAdminService();

            dts.setPrecio12h(Integer.parseInt(txtprecio12h.getText()));
            dts.setPreciohoras(Integer.parseInt(txtpreciohoras.getText()));
            dts.setDescuentorecibo(Integer.parseInt(txtdescuentorecibo.getText()));
            dts.setTipovehiculo(txttipovehiculo.getText());

            if (accion.equals("guardar")) {
                if (func.insertar(dts)) {
                    JOptionPane.showMessageDialog(rootPane, "Registrado satisfactoriamente");
                    mostrart("");

                }

            } else if (accion.equals("editar")) {
                Integer idtarifas = Integer.valueOf(txtidtarifas.getText());
                if (func.editar(idtarifas, dts)) {
                    JOptionPane.showMessageDialog(rootPane, "Editado satisfactoriamente");
                    mostrart("");

                }
            }

        }
    }//GEN-LAST:event_txtpreciohorasKeyPressed

    private void txttiposervicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txttiposervicioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txttiposervicioActionPerformed

    @SuppressWarnings("unchecked")
    private void btnBuscarReporte2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarReporte2ActionPerformed
        // TODO add your handling code here:
//        Map p = new HashMap();
//
//        JasperReport report;
//        JasperPrint print;
//
//        try {
//            report = JasperCompileManager.compileReport(new File("").getAbsolutePath()
//                    + "/src/main/java/ReporteP/Reportedia.jrxml");
//            print = JasperFillManager.fillReport(report, p, Connectionp);
//            JasperViewer view = new JasperViewer(print, false);
//            view.setTitle("Reporte");
//            view.setVisible(true);
//
//        } catch (JRException e) {
//        }
    }//GEN-LAST:event_btnBuscarReporte2ActionPerformed

    private void btnBuscarReporte1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarReporte1ActionPerformed

//        String Dia = txtfecha_turno.getText();
//
//        Map<String, Object> parameters = new HashMap<>();
//        parameters.put("TurnoFecha", Dia);  // Asegúrate de que el nombre del parámetro coincide con el del informe
//
//        JasperReport report;
//        JasperPrint print;
//
//        try {
//
//            report = JasperCompileManager.compileReport(new File("").getAbsolutePath()
//                    + "/src/main/java/ReporteP/ReporteFechaTurno.jrxml");
//            print = JasperFillManager.fillReport(report, parameters, Connectionp);
//            JasperViewer view = new JasperViewer(print, false);
//            view.setTitle("Reporte");
//            view.setVisible(true);
//
//        } catch (JRException e) {
//        }
    }//GEN-LAST:event_btnBuscarReporte1ActionPerformed

    //BOTÓN QUE INSERTA EL BLOQUEO 2 (ESTE BOTÓN ES EL QUE EL FORMULARIO ESTÁ LEYENDO)
    private void btnguardarBQActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnguardarBQActionPerformed
        // TODO add your handling code here:

        System.out.println("SOSPECHA DE BOTÓN PARA GUARDAR BLOQUEO");
        System.out.println("TXTBLOQUEO: " + txtbloqueo1.getText());
        txtbloqueo.setText("Probando bloqueo");
        if (txtbloqueo.getText().length() == 0) {
            JOptionPane.showConfirmDialog(rootPane, "DEBES INGRESAR UN CODIGO");
            txtbloqueo.requestFocus();
            return;
        }

        if (cbotipo_bloqueo1.getSelectedIndex() == 0) {
            JOptionPane.showConfirmDialog(rootPane, "DEBES SELECCIONAR UN TIPO DE BLOQUEO");
            cbotipo_bloqueo.requestFocus();
            return;
        }

        BloqueosAdministrativoDTO dts = new BloqueosAdministrativoDTO();
        BloqueoAdminService func = new BloqueoAdminService();

//        dts.set(Integer.parseInt(txtidbloqueo.getText()));
        dts.setCodigo(txtbloqueo1.getText());
        int tipo_bloqueo = cbotipo_bloqueo1.getSelectedIndex();
        dts.setTipobloqueo(cbotipo_bloqueo1.getItemAt(tipo_bloqueo));

        if (accion.equals("guardar")) {
            if (func.insertar(dts)) {
                JOptionPane.showMessageDialog(rootPane, " INGRESO SATISFACTORIAMENTE");
                mostrarBQ("");

            }

        } else if (accion.equals("editar")) {
            System.out.println("IDEBLOQUEO: " + txtidbloqueo.getText());
            Integer idbloqueo = Integer.valueOf(txtidbloqueo.getText());
            if (func.editar(idbloqueo, dts)) {
                JOptionPane.showMessageDialog(rootPane, "EDITADO SATISFACTORIAMENTE");
                mostrarBQ("");
            }
        }
    }//GEN-LAST:event_btnguardarBQActionPerformed

    //MIGRADO Y PROBANDO FUNCIONAMIENTO
    private void btneliminar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btneliminar1ActionPerformed

        System.out.println("PROBANDO SI ESTE ES EL BOTÓN ELIMINAR");
        System.out.println("IDEBLOQUEO PARA ELIMINAR: " + txtidbloqueo.getText());
        // TODO add your handling code here:
        if (!txtidbloqueo.getText().equals("")) {
            int confirmacion = JOptionPane.showConfirmDialog(rootPane, "Confirma para eliminar?", "Confirmar", JOptionPane.YES_NO_OPTION);

            if (confirmacion == JOptionPane.YES_OPTION) {

                Integer idbloqueo = Integer.valueOf(txtidbloqueo.getText());
                BloqueoAdminService func = new BloqueoAdminService();

                func.eliminar(idbloqueo);
                mostrarBQ("");
//                inhabilitar();

            }

        }
    }//GEN-LAST:event_btneliminar1ActionPerformed

    private void btnnuevoBQActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnnuevoBQActionPerformed
        // TODO add your handling code here:
        txtbloqueo.setText("");
        cbotipo_bloqueo.setSelectedItem("Seleccionar");
    }//GEN-LAST:event_btnnuevoBQActionPerformed

    private void tablalistadoBQKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tablalistadoBQKeyPressed


    }//GEN-LAST:event_tablalistadoBQKeyPressed

    private void tablalistadoBQMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablalistadoBQMouseClicked

        btnguardarBQ.setText("Editar");
        btneliminar.setEnabled(true);
        accion = "editar";

        int fila = tablalistadoBQ.rowAtPoint(evt.getPoint());

        txtidbloqueo.setText(tablalistadoBQ.getValueAt(fila, 0).toString());
        cbotipo_bloqueo.setSelectedItem(tablalistadoBQ.getValueAt(fila, 1).toString());
        txtbloqueo.setText(tablalistadoBQ.getValueAt(fila, 2).toString());

    }//GEN-LAST:event_tablalistadoBQMouseClicked

    //MIGRADO Y PROBANDO FUNCIONAMIENTO
    private void btnExcel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcel1ActionPerformed
        // TODO add your handling code here:
        ReportePrepagoService reporte = new ReportePrepagoService();
        reporte.exportarExcel(TablaPrepagos);
    }//GEN-LAST:event_btnExcel1ActionPerformed

    //VAMOS POR AQUÍ
    //MIGRADO Y PROBANDO FUNCIONAMIENTO
    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed
        try {
            // Crear un diálogo de carga con barra de progreso
            JDialog loadingDialog = new JDialog((Frame) null, "Imprimiendo...", true);
            JPanel panel = new JPanel(new BorderLayout());

            // Icono personalizado (reemplaza con tu ruta y ajusta el tamaño si deseas)
            ImageIcon iconoLogo = new ImageIcon(new ImageIcon(getClass().getResource("/Imagenes/impresora.png")).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
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
                    CopiaCierreTurnoService copiaTurno = new CopiaCierreTurnoService();
                    copiaTurno.imprimirFilaSeleccionada(tablalistadoCopiaTurno);
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
    }//GEN-LAST:event_btnImprimirActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
//        mostrarCopia(txtTurnos.getText());
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        try {
            // TODO add your handling code here:
            imprimirFilaSeleccionada(tablalistadoCopiaTurno);
        } catch (IOException ex) {
            Logger.getLogger(Javanzadop.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(() -> {
            new Javanzadop().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BTNnuevo;
    private javax.swing.JTable TablaPrepagos;
    private javax.swing.JButton btnBuscarReporte1;
    private javax.swing.JButton btnBuscarReporte2;
    private javax.swing.JButton btnConsultar;
    private javax.swing.JButton btnExcel;
    private javax.swing.JButton btnExcel1;
    private javax.swing.JButton btnGuardarp;
    private javax.swing.JButton btnImprimir;
    private javax.swing.JButton btnbuscar1;
    private javax.swing.JButton btnconsultar;
    private javax.swing.JButton btneliminar;
    private javax.swing.JButton btneliminar1;
    private javax.swing.JButton btneliminarb;
    private javax.swing.JButton btneliminart;
    private javax.swing.JButton btnguardarBQ;
    private javax.swing.JButton btnguardarb;
    private javax.swing.JButton btnguardart;
    private javax.swing.JButton btnguardarz;
    private javax.swing.JButton btnnuevoBQ;
    private javax.swing.JButton btnnuevob;
    private javax.swing.JButton btnnuevot;
    private javax.swing.JButton btnnuevoz;
    private javax.swing.JComboBox cboacceso;
    private javax.swing.JComboBox<String> cbocalle;
    private javax.swing.JComboBox cboestado;
    private javax.swing.JComboBox<String> cboestadozona;
    private javax.swing.JComboBox<String> cbotipo_bloqueo;
    private javax.swing.JComboBox<String> cbotipo_bloqueo1;
    private javax.swing.JComboBox<String> cboturno1;
    private javax.swing.JComboBox<String> cboturno2;
    private javax.swing.JComboBox<String> cboturno3;
    private javax.swing.JComboBox<String> comtipodocumento;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JPanel jptabla_empleado;
    public static javax.swing.JLabel lblacceso;
    private javax.swing.JLabel lblarl;
    private javax.swing.JLabel lbleps;
    private javax.swing.JLabel lblregistrosBQ;
    private javax.swing.JLabel lblregistrosb;
    private javax.swing.JLabel lblregistrozona;
    private javax.swing.JLabel lbltotalregistro;
    private javax.swing.JLabel lbltotalregistrop;
    private javax.swing.JTable tablalistadoBQ;
    private javax.swing.JTable tablalistadoCopiaTurno;
    private javax.swing.JTable tablalistadoP;
    private javax.swing.JTable tablalistadob;
    private javax.swing.JTable tablalistadoinfo;
    private javax.swing.JTable tablalistador;
    private javax.swing.JTable tablalistadot;
    private javax.swing.JTable tablalistadozona;
    private javax.swing.JTextField txtBuscar1;
    private javax.swing.JTextField txtBuscar2;
    private javax.swing.JTextField txtBuscar3;
    private javax.swing.JTextField txtTurnos;
    private javax.swing.JTextField txtapellidos;
    private javax.swing.JTextField txtarl;
    private javax.swing.JTextField txtbloqueo;
    private javax.swing.JTextField txtbloqueo1;
    private javax.swing.JTextField txtbuscar;
    private javax.swing.JTextField txtciudad;
    private javax.swing.JTextField txtdescuentorecibo;
    private javax.swing.JTextField txtdireccion;
    private javax.swing.JTextField txtdocumento;
    private javax.swing.JTextField txtemail;
    private javax.swing.JTextField txteps;
    private javax.swing.JTextField txtfecha_turno;
    private javax.swing.JTextField txtfechaentrada;
    private javax.swing.JTextField txtfechasalida;
    private javax.swing.JTextField txtidbloqueo;
    private javax.swing.JTextField txtidbloqueo1;
    private javax.swing.JTextField txtidempleado;
    private javax.swing.JTextField txtidtarifas;
    private javax.swing.JTextField txtidzona;
    private javax.swing.JTextField txtlogin;
    private javax.swing.JTextField txtnombres;
    private javax.swing.JTextField txtnumerozona;
    private javax.swing.JTextField txtpais;
    private javax.swing.JPasswordField txtpassword;
    private javax.swing.JTextField txtprecio12h;
    private javax.swing.JTextField txtpreciohoras;
    private javax.swing.JTextField txttelefono;
    private javax.swing.JTextField txttiposervicio;
    private javax.swing.JTextField txttipovehiculo;
    // End of variables declaration//GEN-END:variables
}
