package Presentacion;

import Datos.InicioTurnoResponseDTO;
import Datos.SalidaTurnoRequest;
import Datos.TotalesMediosPagoAbonosDTO;
import Datos.TotalesMediosPagoDTO;
import DatosP.SessionData;
import DatosP.SessionStorage;
import Impresionp.CierreTurno;
import Logica.ApiFinTurnoService;
import Logica.ApiSalidaService;
import Logica.IngresoService;
import Logica.InicioTurnoService;
import LogicaP.AplicarIcono;
import LogicaP.Tiempopro;
import ReporteP.SalidaTurnoReporteDTO;
import java.awt.Desktop;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class Jfinturnop extends javax.swing.JFrame {

    private static final long serialVersionUID = 1L;

    public Jfinturnop() throws Exception {
        initComponents();
        AplicarIcono.icono(this, "/Imagenes/GN_Grande.png");
        agregarWindowFocusListener();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        vehiculosEnParqueo();
        inicioceros();
        mostrarTiempo();
        RealizarCalculoAutomatico();

    }
    private final String accion = "guardar";

    private void mostrarTiempo() {
        Tiempopro fecha = new Tiempopro();
        txtfechasalida.setText(fecha.getFechacomp() + " " + fecha.getHoracomp());
    }

    //COMPLETADO
    private void vehiculosEnParqueo() throws Exception {

        IngresoService sumaParqueo = new IngresoService();

        int resultados = sumaParqueo.contarEstadoActivo();
        //int Autos = resultados[0];

        txtAutos.setText(String.valueOf(resultados));

    }

    private void inicioceros() {
        txtidinicioturno.setVisible(false);
        txtbase.setText("0");
        txtotros_ingresos.setText("0");
        txttRrecibidos.setText("0");
        txttotal_recaudo.setText("0");
        txtTotal_abonos.setText("0");
        txtobservaciones.setText("Nunguna");
    }

    private void agregarWindowFocusListener() {
        this.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                try {
                    //mostrarnumeroturno();  // Actualiza el número de turno cuando la ventana recibe el foco
                } catch (Exception ex) {
                    Logger.getLogger(Jfinturnop.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                // No hacer nada cuando la ventana pierde el foco
            }
        });
    }

    public void actualizarTurno(String fecha_hora_inicio, String turno) {

        txtfecha_hora_inicio.setText(fecha_hora_inicio);
        txtturno.setText(turno);
    }

    //COMPLETADO, EN VEREMOS
    private void RealizarCalculoAutomatico() throws Exception {

        SessionData session = SessionStorage.loadSession();
        int numeroTurno = 0;

        if (session != null) {
            numeroTurno = session.getNumeroTurno();
            System.out.println("Número de turno recuperado: " + numeroTurno);
        }

        try {
            InicioTurnoService func = new InicioTurnoService();
            NumberFormat formatoMiles = NumberFormat.getNumberInstance(Locale.US);

            InicioTurnoResponseDTO turno = func.realizarConsulta(numeroTurno);
            if (turno != null) {
                // Obtener datos inicio de turno.
                txtidinicioturno.setText(String.valueOf(turno.getIdturno()));
                txtnumero_turno.setText(String.valueOf(turno.getNumeroturno()));
                txtfecha_hora_inicio.setText(turno.getFechainicio());
                txtempleado.setText(turno.getEmpleado());
                txtturno.setText(turno.getTurno());
                txtestado.setText(turno.getEstado());

                TotalesMediosPagoDTO totalesMediosPago = func.totalmedio_pagos(numeroTurno);

                // Sumar los valores correspondientes de cada arreglo
                int totalEfectivo = totalesMediosPago.getTotalEfectivo();
                int totalTarjeta = totalesMediosPago.getTotalTarjeta();
                int totalTransferencia = totalesMediosPago.getTotalTransferencia();

                // Asignar los valores sumados a los campos de texto
                txtefectivo.setText(formatoMiles.format(totalEfectivo));
                txttarjeta.setText(formatoMiles.format(totalTarjeta));
                txttransferencia.setText(formatoMiles.format(totalTransferencia));
                txtentrega_admon.setText(formatoMiles.format(totalEfectivo));

                TotalesMediosPagoAbonosDTO totalesMediosPago_abonos = func.totalmedio_pagos_abonos(numeroTurno);

                int totalEfectivo_abono = (int) totalesMediosPago_abonos.getEfectivo();
                int totalTarjeta_abono = (int) totalesMediosPago_abonos.getTarjeta();
                int totalTransferencia_abono = (int) totalesMediosPago_abonos.getTransferencia();

                int total_abonos = totalEfectivo_abono + totalTarjeta_abono + totalTransferencia_abono;
                txtTotal_abonos.setText(formatoMiles.format(total_abonos));

                int otrosingresos = Integer.parseInt(txtotros_ingresos.getText().replace(",", ""));
                int totalrecaudo = totalEfectivo + totalTarjeta + totalTransferencia + otrosingresos;
                txttotal_recaudo.setText(formatoMiles.format(totalrecaudo));
            }
        } catch (HeadlessException | SQLException ex) {
            System.err.println("Error: " + ex.getMessage());
        }
    }

    //MIGRADO Y PROBANDO FUNCIONAMIENTO
    public void finalizarTurno() throws Exception {

        SessionData session = SessionStorage.loadSession();
        int numeroTurno = 0;

        if (session != null) {
            numeroTurno = session.getNumeroTurno();
            System.out.println("Número de turno recuperado: " + numeroTurno);
        }
        Jmenuprin.sesionIniciadaP = false; // Cambiar el estado de la sesión
        //Jmenuprin.limpiarDatosUsuario();
        ApiFinTurnoService func1 = new ApiFinTurnoService();
        //Dinicioturnop dts1 = new Dinicioturnop();

        //Long numTurno = Long.valueOf(txtnumero_turno.getText());
        //dts1.setNumeroturno(Integer.parseInt(txtnumero_turno.getText()));
        func1.finalizarturno(numeroTurno);
//        limpiarcajas();
//        JOptionPane.showMessageDialog(this, "Turno cerrado");
        this.setVisible(false);
        this.dispose();

        //Jinicioturnop inicio = new Jinicioturnop();
        //inicio.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtempleado = new javax.swing.JTextField();
        txtfechasalida = new javax.swing.JTextField();
        txtfecha_hora_inicio = new javax.swing.JTextField();
        txtnumero_turno = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        buscarnumeroturno = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txttRrecibidos = new javax.swing.JTextField();
        txtturno = new javax.swing.JTextField();
        txtAutos = new javax.swing.JTextField();
        txtestado = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        txtidinicioturno = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txttotal_recaudo = new javax.swing.JTextField();
        txtTotal_abonos = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txttarjeta = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txttransferencia = new javax.swing.JTextField();
        txtbase = new javax.swing.JTextField();
        txtefectivo = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtentrega_admon = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtotros_ingresos = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtobservaciones = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLayeredPane1.setBackground(new java.awt.Color(204, 204, 204));

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel2.setText("Turno:");

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel3.setText("Responsable:");

        jLabel4.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel4.setText("Fecha de inicio:");

        jLabel5.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel5.setText("Fecha de salida:");

        txtempleado.setEditable(false);
        txtempleado.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N

        txtfechasalida.setEditable(false);
        txtfechasalida.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N

        txtfecha_hora_inicio.setEditable(false);
        txtfecha_hora_inicio.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N

        txtnumero_turno.setEditable(false);
        txtnumero_turno.setBackground(new java.awt.Color(255, 255, 204));
        txtnumero_turno.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        txtnumero_turno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnumero_turnoActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel6.setText("Numero Turno:");

        jLabel10.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel10.setText("Estado:");

        buscarnumeroturno.setBackground(new java.awt.Color(204, 204, 204));
        buscarnumeroturno.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/buscar.png"))); // NOI18N
        buscarnumeroturno.setText("BUSCAR");
        buscarnumeroturno.setBorder(null);
        buscarnumeroturno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarnumeroturnoActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel1.setText("Recibidos:");

        txttRrecibidos.setEditable(false);
        txttRrecibidos.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N

        txtturno.setEditable(false);

        txtAutos.setEditable(false);
        txtAutos.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        txtestado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtestadoActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel7.setText("Total Vehiculos:");

        jSeparator3.setBackground(new java.awt.Color(51, 153, 255));
        jSeparator3.setForeground(new java.awt.Color(51, 153, 255));
        jSeparator3.setToolTipText("");
        jSeparator3.setAlignmentX(1.0F);
        jSeparator3.setAlignmentY(1.0F);
        jSeparator3.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jSeparator3.setOpaque(true);

        txtidinicioturno.setText("IDI");
        txtidinicioturno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtidinicioturnoActionPerformed(evt);
            }
        });

        jLayeredPane1.setLayer(jLabel2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(jLabel3, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(jLabel4, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(jLabel5, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(txtempleado, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(txtfechasalida, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(txtfecha_hora_inicio, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(txtnumero_turno, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(jLabel6, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(jLabel10, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(buscarnumeroturno, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(jLabel1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(txttRrecibidos, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(txtturno, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(txtAutos, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(txtestado, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(jLabel7, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(jSeparator3, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(txtidinicioturno, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jLayeredPane1Layout.createSequentialGroup()
                        .addGap(53, 53, 53)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtAutos, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txttRrecibidos, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtidinicioturno, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jLayeredPane1Layout.createSequentialGroup()
                        .addGap(68, 68, 68)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtnumero_turno, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buscarnumeroturno, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtestado, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jLayeredPane1Layout.createSequentialGroup()
                            .addComponent(jLabel4)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtfecha_hora_inicio, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jLayeredPane1Layout.createSequentialGroup()
                            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                                    .addGap(68, 68, 68)
                                    .addComponent(jLabel3)
                                    .addGap(9, 9, 9))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jLayeredPane1Layout.createSequentialGroup()
                                    .addComponent(jLabel2)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtempleado, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtturno, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jLayeredPane1Layout.createSequentialGroup()
                            .addComponent(jLabel5)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtfechasalida, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 568, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtnumero_turno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buscarnumeroturno, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(txtestado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtturno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtempleado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtfecha_hora_inicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtfechasalida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtAutos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1)
                        .addComponent(txttRrecibidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtidinicioturno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel15.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel15.setText("Total recaudado:");

        jLabel16.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel16.setText("Efectivo liquido:");

        txttotal_recaudo.setEditable(false);
        txttotal_recaudo.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txttotal_recaudo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txttotal_recaudoActionPerformed(evt);
            }
        });

        txtTotal_abonos.setEditable(false);
        txtTotal_abonos.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N

        jLabel17.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel17.setText("Total Abonos:");

        txttarjeta.setEditable(false);
        txttarjeta.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N

        jLabel11.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel11.setText("Tarjetas:");

        jLabel9.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel9.setText("Transferencias:");

        txttransferencia.setEditable(false);
        txttransferencia.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N

        txtbase.setEditable(false);
        txtbase.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txtbase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtbaseActionPerformed(evt);
            }
        });

        txtefectivo.setEditable(false);
        txtefectivo.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N

        jLabel19.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel19.setText("Efectivo:");

        jLabel8.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel8.setText("Base:");

        txtentrega_admon.setEditable(false);
        txtentrega_admon.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txtentrega_admon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtentrega_admonActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel14.setText("Otros ingresos:");

        txtotros_ingresos.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txtotros_ingresos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtotros_ingresosKeyPressed(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel18.setText("Observaciones:");

        txtobservaciones.setColumns(20);
        txtobservaciones.setRows(5);
        jScrollPane1.setViewportView(txtobservaciones);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txttransferencia, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
                    .addComponent(txtotros_ingresos)
                    .addComponent(txttarjeta, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtefectivo)
                    .addComponent(txtbase, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTotal_abonos))
                .addGap(66, 66, 66)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel16)
                            .addComponent(jLabel15)
                            .addComponent(jLabel18))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtentrega_admon, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
                            .addComponent(txttotal_recaudo)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtbase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(txtentrega_admon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(txtefectivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(txttotal_recaudo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txttarjeta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel18))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(txttransferencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(txtotros_ingresos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTotal_abonos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13))
        );

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

        jButton2.setBackground(new java.awt.Color(204, 204, 204));
        jButton2.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/disco-flexible.png"))); // NOI18N
        jButton2.setText("Guardar");
        jButton2.setBorder(null);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jSeparator2.setBackground(new java.awt.Color(51, 153, 255));
        jSeparator2.setForeground(new java.awt.Color(51, 153, 255));
        jSeparator2.setToolTipText("");
        jSeparator2.setAlignmentX(1.0F);
        jSeparator2.setAlignmentY(1.0F);
        jSeparator2.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jSeparator2.setOpaque(true);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jSeparator2)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLayeredPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtnumero_turnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnumero_turnoActionPerformed
        // Generar el número de turno
    }//GEN-LAST:event_txtnumero_turnoActionPerformed

    private void buscarnumeroturnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buscarnumeroturnoActionPerformed
        try {
            RealizarCalculoAutomatico();

            // Ejecuta el SwingWorker
        } catch (Exception ex) {
            Logger.getLogger(Jfinturnop.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_buscarnumeroturnoActionPerformed

    private void txtbaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtbaseActionPerformed

    }//GEN-LAST:event_txtbaseActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    //AREVISAR
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        SalidaTurnoRequest dts = new SalidaTurnoRequest();
        ApiFinTurnoService func = new ApiFinTurnoService();

        dts.setEmpleado(txtempleado.getText());
        dts.setTurno(txtturno.getText());
        dts.setFechaingreso(txtfecha_hora_inicio.getText());
        dts.setFechasalida(txtfechasalida.getText());
        dts.setRecibos(txttRrecibidos.getText());
        dts.setTotalvehiculos(Integer.parseInt(txtAutos.getText()));
        dts.setBase(Integer.parseInt(txtbase.getText().replace(",", "")));
        dts.setTarjeta(Integer.parseInt(txttarjeta.getText().replace(",", "")));
        dts.setEfectivo(Integer.parseInt(txtefectivo.getText().replace(",", "")));
        dts.setTransferencia(Integer.parseInt(txttransferencia.getText().replace(",", "")));
        dts.setOtrosingresos(Integer.parseInt(txtotros_ingresos.getText().replace(",", "")));
        dts.setEstado(txtestado.getText());
        dts.setEfectivoliquido(Integer.parseInt(txtentrega_admon.getText().replace(",", "")));
        dts.setTotalrecaudado(Integer.parseInt(txttotal_recaudo.getText().replace(",", "")));
        dts.setObservaciones(txtobservaciones.getText());
        dts.setNumeroturno(txtnumero_turno.getText());
        dts.setTotalabonos(Integer.parseInt(txtTotal_abonos.getText().replace(",", "")));

        if (accion.equals("guardar")) {
            try {
                if (func.insertar(dts)) {

                    try {
                        finalizarTurno();
                    } catch (Exception ex) {
                        Logger.getLogger(Jfinturnop.class.getName()).log(Level.SEVERE, null, ex);
                    }
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
                        InputStream Combugas = getClass().getResourceAsStream("/Imagenes/Iconocombugas-trans.png");
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

                }

            } catch (Exception ex) {
                Logger.getLogger(Jfinturnop.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void txtentrega_admonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtentrega_admonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtentrega_admonActionPerformed

    private void txtidinicioturnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtidinicioturnoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtidinicioturnoActionPerformed

    private void txtotros_ingresosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtotros_ingresosKeyPressed
        // TODO add your handling code here:

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            NumberFormat formatoMiles = NumberFormat.getNumberInstance(Locale.US);

            int otros_ingresos = Integer.parseInt(txtotros_ingresos.getText().replace(",", ""));
            int total_recudado = Integer.parseInt(txttotal_recaudo.getText().replace(",", ""));

            int Nuevo_total = otros_ingresos + total_recudado;
            txttotal_recaudo.setText(formatoMiles.format(Nuevo_total));

        }
    }//GEN-LAST:event_txtotros_ingresosKeyPressed

    private void txtestadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtestadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtestadoActionPerformed

    private void txttotal_recaudoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txttotal_recaudoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txttotal_recaudoActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Jfinturnop.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            try {
                new Jfinturnop().setVisible(true);
            } catch (Exception ex) {
                Logger.getLogger(Jfinturnop.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buscarnumeroturno;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTextField txtAutos;
    private javax.swing.JTextField txtTotal_abonos;
    private javax.swing.JTextField txtbase;
    private javax.swing.JTextField txtefectivo;
    private javax.swing.JTextField txtempleado;
    private javax.swing.JTextField txtentrega_admon;
    private javax.swing.JTextField txtestado;
    private javax.swing.JTextField txtfecha_hora_inicio;
    private javax.swing.JTextField txtfechasalida;
    public static javax.swing.JTextField txtidinicioturno;
    private javax.swing.JTextField txtnumero_turno;
    private javax.swing.JTextArea txtobservaciones;
    private javax.swing.JTextField txtotros_ingresos;
    private javax.swing.JTextField txttRrecibidos;
    private javax.swing.JTextField txttarjeta;
    private javax.swing.JTextField txttotal_recaudo;
    private javax.swing.JTextField txttransferencia;
    private javax.swing.JTextField txtturno;
    // End of variables declaration//GEN-END:variables
}
