package Presentacion;

import LogicaP.Tiempopro;
import DatosP.Dingresop;
import Diseños.Estilo_tablas;
import Logica.BloqueoService;
import Logica.ConfiguradorApi;
import Logica.IngresoService;
import LogicaP.AplicarIcono;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class Jingresop extends javax.swing.JFrame {

    private static final long serialVersionUID = 1L;

    public Jingresop() {
        initComponents();
        AplicarIcono.icono(this, "/Imagenes/GN_Grande.png");
        mostrar("");//MOstrar la tabla cargada con los datos de ingreso
        configurarTabla();
        setLocationRelativeTo(null);
        setTitle("INGRESO");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        inhabilitar();
        mostrarTiempo();
        FiltrarBusqueda();
        System.out.println("txtidingreso: " + txtidingreso.getText());
        //ingresoAutoCompleteDecoreitor();
//        Estado_tablas.aplicarRenderizador(tablalistado, 11);
    }
    private String accion = "guardar";

    //PARA BUSCAR Y FILTRAR EN LA TABLALISTADO
    private void FiltrarBusqueda() {
        txtbuscar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filtrarTablaGeneral(txtbuscar, tablalistado);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filtrarTablaGeneral(txtbuscar, tablalistado);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filtrarTablaGeneral(txtbuscar, tablalistado);
            }
        });
    }

    private void filtrarTablaGeneral(JTextField txtBuscar, JTable tabla) {
        // Verificar si el modelo es del tipo correcto
        if (!(tabla.getModel() instanceof DefaultTableModel)) {
            return;
        }

        DefaultTableModel model = (DefaultTableModel) tabla.getModel();

        // Asegurar que la tabla tenga un RowSorter asignado
        if (tabla.getRowSorter() == null || !(tabla.getRowSorter() instanceof TableRowSorter)) {
            TableRowSorter<DefaultTableModel> newSorter = new TableRowSorter<>(model);
            tabla.setRowSorter(newSorter);
        }

        TableRowSorter<?> sorter = (TableRowSorter<?>) tabla.getRowSorter();
        String texto = txtBuscar.getText().trim();

        if (texto.isEmpty()) {
            sorter.setRowFilter(null); // Mostrar todos los registros
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(texto)));
        }
    }

    private void mostrarTiempo() {
        Tiempopro time = new Tiempopro();
        txtfechaingreso.setText(time.getFechacomp() + " " + time.getHoracomp());
    }

//    private void ingresoAutoCompleteDecoreitor() {
//        if (txtplaca != null) {
//            // Lista de autocompletado para `txtcliente`
//            //List<String> clientes = obtenerListaClientes(); // Método que obtiene los nombres y apellidos
//            //AutoCompleteDecorator.decorate(txtplaca, clientes, false);
//        }
//    }
//    private List<String> obtenerListaClientes() {
//        PreparedStatement pst;
//        ResultSet rs;
//        List<String> listaClientes = new ArrayList<>();
//        Cconexionp conexion = new Cconexionp();
//        try {
//            // Realiza la consulta para obtener los nombres de clientes
//            Connection conectar = conexion.establecerConexionp();
//            pst = conectar.prepareStatement("SELECT placa FROM cliente ");
//            rs = pst.executeQuery();
//
//            while (rs.next()) {
//                String placa = rs.getString("placa");
//                listaClientes.add(placa);
//            }
//        } catch (SQLException e) {
//        } finally {
//        }
//        return listaClientes;
//    }
    private void configurarTabla() {
        Estilo_tablas.configurarTabla(tablalistado);

    }

    void ocultar_columnas() {
        int[] columnasOcultasIN = {0, 1, 8, 10, 11};
        Estilo_tablas.ocultarColumnas(tablalistado, columnasOcultasIN);

    }

    private void inhabilitar() {
        mostrarTiempo();
        lblturnos.setVisible(false);
        txtidingreso.setVisible(false);
        txtidinicio_turno.setVisible(false);
        txtplaca.setText("");
        cbotipovehiculo.setSelectedItem("SELECCIONAR");
        cbotiposervicio.setSelectedItem("SELECCIONAR");
        txtcliente.setText("O");
        txtobservaciones.setText("Ninguna");
        cboestado.setSelectedItem("Activo");
        txtzona.setText("O");
        txtidinicio_turno.setText("0");

    }

    //MIGRADO Y FUNCIONANDO CON LA API- ESTA FUNCIÓN MUESTRA LOS VEHÍCULOS INGRESADOS QUE SE ENCUENTRAN ACTIVOS
    //EN PARQUEADERO
    private void mostrar(String buscar) {
        try {
            DefaultTableModel modelo;
            IngresoService func = new IngresoService();
            modelo = func.mostrar(buscar, tablalistado);

            tablalistado.setModel(modelo);
            ocultar_columnas();
            System.out.println("\"Total Registros \"" + func.totalregistros);
            lblregistro.setText("Total Registros " + Integer.toString(func.totalregistros));
            System.out.println("\"Total Registros \"" + func.totalregistros);

        } catch (Exception e) {
            JOptionPane.showConfirmDialog(rootPane, e);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtobservaciones = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtplaca = new javax.swing.JTextField();
        txtcliente = new javax.swing.JTextField();
        txtfechaingreso = new javax.swing.JTextField();
        cbotipovehiculo = new javax.swing.JComboBox<>();
        cbotiposervicio = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtnumeroturno = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtempleado = new javax.swing.JTextField();
        btnguardar = new javax.swing.JButton();
        btnguardar1 = new javax.swing.JButton();
        lblturnos = new javax.swing.JLabel();
        txtidinicio_turno = new javax.swing.JTextField();
        cboestado = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        txtidingreso = new javax.swing.JTextField();
        txtzona = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablalistado = new javax.swing.JTable();
        lblregistro = new javax.swing.JLabel();
        btneliminar = new javax.swing.JButton();
        txtbuscar = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel2.setText("Cliente:");

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel3.setText("Tipo Vehiculo:");

        jLabel4.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel4.setText("Tipo Servicio:");

        txtobservaciones.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txtobservaciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtobservacionesActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel5.setText("Placa:");

        txtplaca.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txtplaca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtplacaActionPerformed(evt);
            }
        });
        txtplaca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtplacaKeyPressed(evt);
            }
        });

        txtcliente.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txtcliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtclienteActionPerformed(evt);
            }
        });

        txtfechaingreso.setEditable(false);
        txtfechaingreso.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txtfechaingreso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtfechaingresoActionPerformed(evt);
            }
        });

        cbotipovehiculo.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        cbotipovehiculo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SELECCIONAR", "MOTO", "TRICICLO", "AUTO", "BUSETA", "VOLQUETAS", "VACTOR", "GRUAS", "TRACTOMULA", "TRANSBORDO", "CARRO TANQUE", "CAMION PEQUEÑO", "CAMION GRANDE" }));
        cbotipovehiculo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbotipovehiculoActionPerformed(evt);
            }
        });

        cbotiposervicio.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        cbotiposervicio.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SELECCIONAR", "GENERAL", "PREPAGO", "EMPLEADO", "CLIENTE HOTEL" }));
        cbotiposervicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbotiposervicioActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel6.setText("ZONA:");

        jLabel7.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel7.setText("Fecha ingreso:");

        jLabel8.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel8.setText("Turno:");

        txtnumeroturno.setEditable(false);

        jLabel9.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel9.setText("Responsable:");

        jLabel10.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel10.setText("Observaciones:");

        txtempleado.setEditable(false);
        txtempleado.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txtempleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtempleadoActionPerformed(evt);
            }
        });

        btnguardar.setBackground(new java.awt.Color(204, 204, 204));
        btnguardar.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        btnguardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/disco-flexible.png"))); // NOI18N
        btnguardar.setText("Ingresar");
        btnguardar.setBorder(null);
        btnguardar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnguardar.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        btnguardar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnguardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnguardarActionPerformed(evt);
            }
        });

        btnguardar1.setBackground(new java.awt.Color(204, 204, 204));
        btnguardar1.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        btnguardar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/registro_1.png"))); // NOI18N
        btnguardar1.setText("Nuevo");
        btnguardar1.setBorder(null);
        btnguardar1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnguardar1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        btnguardar1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnguardar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnguardar1ActionPerformed(evt);
            }
        });

        lblturnos.setText("TURNO");

        cboestado.setEditable(true);
        cboestado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activo", "Finalizado" }));
        cboestado.setEnabled(false);
        cboestado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboestadoActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel11.setText("Estado:");

        txtidingreso.setText("IDIN");

        txtzona.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        txtzona.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtzonaKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtnumeroturno, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtempleado, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(117, 117, 117)
                        .addComponent(txtobservaciones, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnguardar, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnguardar1, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel5)
                                        .addComponent(jLabel3)
                                        .addComponent(jLabel4))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(48, 48, 48)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(lblturnos)
                                        .addGap(6, 6, 6))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel6)
                                            .addComponent(jLabel2))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(txtidinicio_turno, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtidingreso, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtcliente, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtfechaingreso, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtplaca, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(cbotiposervicio, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(cbotipovehiculo, javax.swing.GroupLayout.Alignment.LEADING, 0, 200, Short.MAX_VALUE))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtzona, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel11)
                                .addGap(18, 18, 18)
                                .addComponent(cboestado, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(13, 13, 13)))))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtfechaingreso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtplaca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cbotipovehiculo, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbotiposervicio, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtcliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtzona, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboestado, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtobservaciones, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtempleado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtidingreso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtidinicio_turno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(txtnumeroturno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblturnos)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 69, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnguardar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnguardar, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE))
                .addContainerGap())
        );

        tablalistado.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6"
            }
        ));
        tablalistado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablalistadoMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tablalistado);

        lblregistro.setText("TOTAL REGISTROS:");

        btneliminar.setBackground(new java.awt.Color(204, 204, 204));
        btneliminar.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        btneliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/basura.png"))); // NOI18N
        btneliminar.setText("Eliminar");
        btneliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btneliminarActionPerformed(evt);
            }
        });

        txtbuscar.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jLabel1.setText("BUSCAR:");

        jButton1.setBackground(new java.awt.Color(204, 204, 204));
        jButton1.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Excel.png"))); // NOI18N
        jButton1.setText("Excel");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 486, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblregistro, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtbuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btneliminar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtbuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(btneliminar)
                    .addComponent(jButton1))
                .addGap(11, 11, 11)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblregistro)
                .addGap(12, 12, 12))
        );

        jSeparator2.setBackground(new java.awt.Color(51, 153, 255));
        jSeparator2.setForeground(new java.awt.Color(51, 153, 255));
        jSeparator2.setToolTipText("");
        jSeparator2.setAlignmentX(1.0F);
        jSeparator2.setAlignmentY(1.0F);
        jSeparator2.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jSeparator2.setOpaque(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jSeparator2))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtfechaingresoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtfechaingresoActionPerformed
        // TODO add your handling code here:
        txtfechaingreso.transferFocus();
    }//GEN-LAST:event_txtfechaingresoActionPerformed

    private void txtclienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtclienteActionPerformed
        // TODO add your handling code here:
        txtcliente.transferFocus();
    }//GEN-LAST:event_txtclienteActionPerformed

    private void txtobservacionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtobservacionesActionPerformed
        // TODO add your handling code here:
        txtobservaciones.transferFocus();
    }//GEN-LAST:event_txtobservacionesActionPerformed

    private void txtplacaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtplacaActionPerformed
        txtplaca.transferFocus();
    }//GEN-LAST:event_txtplacaActionPerformed

    private void txtempleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtempleadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtempleadoActionPerformed
    //BOTÓN PARA REALIZAR EL GUARDADO Y EDITADO DE VEHÍCULO INGRESADO
    private void btnguardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnguardarActionPerformed

        if (txtplaca.getText().length() == 0) {
            JOptionPane.showConfirmDialog(rootPane, "DEBES INGRESAR UNA PLACA", "Advertencia", JOptionPane.WARNING_MESSAGE);
            txtplaca.requestFocus();
            return;
        }
        if (cbotiposervicio.getSelectedItem().toString().equals("SELECCIONAR")) {
            JOptionPane.showMessageDialog(rootPane, "Por favor, seleccione un tipo de servicio", "Advertencia", JOptionPane.WARNING_MESSAGE);
            cbotiposervicio.requestFocus();
            return;
        }

        if (cbotipovehiculo.getSelectedItem().toString().equals("SELECCIONAR")) {
            JOptionPane.showMessageDialog(rootPane, "Por favor, seleccione un tipo de vehiculo", "Advertencia", JOptionPane.WARNING_MESSAGE);
            cbotipovehiculo.requestFocus();
            return;
        }

        if (txtfechaingreso.getText().length() == 0) {
            JOptionPane.showConfirmDialog(rootPane, "VERIFICA QUE ESTE LA FECHA POR FAVOR", "Advertencia", JOptionPane.WARNING_MESSAGE);
            txtfechaingreso.requestFocus();
            return;
        }

        if (cbotipovehiculo.getSelectedIndex() == 0) {
            JOptionPane.showConfirmDialog(rootPane, "DEBES SELECCIONAR UN TIPO DE VEHICULO", "Advertencia", JOptionPane.WARNING_MESSAGE);
            cbotipovehiculo.requestFocus();
            return;
        }

        if (cbotiposervicio.getSelectedIndex() == 0) {
            JOptionPane.showConfirmDialog(rootPane, "DEBES SELCCIONAR UN TIPO DE SERVICIO", "Advertencia", JOptionPane.WARNING_MESSAGE);
            cbotiposervicio.requestFocus();
            return;
        }
        if (txtzona.getText().length() == 0) {
            JOptionPane.showConfirmDialog(rootPane, "VERIFICA QUE ESTES ASIGNANDO UN LUGAR EN EL PARQUEADERO", "Advertencia", JOptionPane.WARNING_MESSAGE);
            txtzona.requestFocus();
            return;
        }
        if (txtidinicio_turno.getText().length() == 0) {
            JOptionPane.showConfirmDialog(rootPane, "Faltan datos del empleado. por favor actualice el turno para recuperarlos", "Advertencia", JOptionPane.WARNING_MESSAGE);
            txtidinicio_turno.requestFocus();
            return;
        }

        if (txtnumeroturno.getText().length() == 0) {
            JOptionPane.showConfirmDialog(rootPane, "Faltan datos del empleado. por favor actualice el turno para recuperarlos", "Advertencia", JOptionPane.WARNING_MESSAGE);
            txtnumeroturno.requestFocus();
            return;
        }
        if (lblturnos.getText().length() == 0) {
            JOptionPane.showConfirmDialog(rootPane, "Faltan datos del empleado. por favor actualice el turno para recuperarlos", "Advertencia", JOptionPane.WARNING_MESSAGE);
            lblturnos.requestFocus();
            return;
        }
        if (txtempleado.getText().length() == 0) {
            JOptionPane.showConfirmDialog(rootPane, "Faltan datos del empleado. por favor actualice el turno para recuperarlos", "Advertencia", JOptionPane.WARNING_MESSAGE);
            txtempleado.requestFocus();
            return;
        }

        Dingresop dts = new Dingresop();
        IngresoService func = new IngresoService();

        dts.setTurno(lblturnos.getText());
        dts.setNumeroturno(txtnumeroturno.getText().trim());
        dts.setEmpleado(txtempleado.getText().trim());
        dts.setPlaca(txtplaca.getText().trim());
        dts.setFechaingreso(txtfechaingreso.getText().trim());
        int tipovehiculo = cbotipovehiculo.getSelectedIndex();
        dts.setTipovehiculo(cbotipovehiculo.getItemAt(tipovehiculo));
        int tiposervicio = cbotiposervicio.getSelectedIndex();
        dts.setTiposervicio(cbotiposervicio.getItemAt(tiposervicio));
        dts.setCliente(txtcliente.getText());
        dts.setZona(txtzona.getText().trim());
        dts.setObservaciones(txtobservaciones.getText());
        int estado = cboestado.getSelectedIndex();
        dts.setEstado(cboestado.getItemAt(estado));

        // Verificar que la placa no esté activa antes de continuar
        if (accion.equals("guardar")) {
            if (func.verificarPlacaActiva(txtplaca.getText().trim())) {
                JOptionPane.showMessageDialog(rootPane, "ERROR: La placa ya tiene un ingreso activo en el parqueadero.");
                txtplaca.requestFocus();
                return;
            }
        }

        IngresoService api = new IngresoService();

        if (accion.equals("guardar")) {

            //Ingreso de vehículos
            String respuesta = api.guardarIngreso(dts);
            if (respuesta.startsWith("{")) {
                // Asumimos que si devuelve un JSON, fue exitoso
                JOptionPane.showMessageDialog(rootPane, "Ingreso registrado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                mostrar("");
                inhabilitar();
                // Opcional: limpiar campos si deseas
            } else {
                // Si hay un mensaje de error (código 500, 409, etc.)
                JOptionPane.showMessageDialog(rootPane, respuesta, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (accion.equals("editar")) {
            dts.setIdingreso(Integer.parseInt(txtidingreso.getText().trim()));
            Boolean resultado = api.actualizarIngresoEnAPI(dts);
            if (resultado) {
                JOptionPane.showMessageDialog(rootPane, "Registro actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                mostrar("");
                inhabilitar();
            } else {
                JOptionPane.showMessageDialog(rootPane, "Error al actualizar el ingreso.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

    }//GEN-LAST:event_btnguardarActionPerformed

    private void btnguardar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnguardar1ActionPerformed
        // TODO add your handling code here:
//        habilitar();
        btnguardar.setText("Guardar");
        accion = "guardar";
        inhabilitar();
    }//GEN-LAST:event_btnguardar1ActionPerformed

    private void tablalistadoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablalistadoMouseClicked

        btnguardar.setText("Editar");
        btneliminar.setEnabled(true);
        accion = "editar";

        int fila = tablalistado.rowAtPoint(evt.getPoint());

        txtidingreso.setText(tablalistado.getValueAt(fila, 0).toString());   // idingreso
        lblturnos.setText(tablalistado.getValueAt(fila, 1).toString());      // turno
        txtnumeroturno.setText(tablalistado.getValueAt(fila, 2).toString()); // numero turno
        txtempleado.setText(tablalistado.getValueAt(fila, 3).toString());    // empleado
        txtplaca.setText(tablalistado.getValueAt(fila, 4).toString());       // placa
        txtfechaingreso.setText(tablalistado.getValueAt(fila, 5).toString());// fecha ingreso
        cbotipovehiculo.setSelectedItem(tablalistado.getValueAt(fila, 6));   // tipo vehiculo
        cbotiposervicio.setSelectedItem(tablalistado.getValueAt(fila, 7));   // tipo servicio
        txtcliente.setText(tablalistado.getValueAt(fila, 8).toString());     // cliente
        txtzona.setText(tablalistado.getValueAt(fila, 9).toString());        // zona
        txtobservaciones.setText(tablalistado.getValueAt(fila, 10).toString());// observaciones
        cboestado.setSelectedItem(tablalistado.getValueAt(fila, 11));        // estado


    }//GEN-LAST:event_tablalistadoMouseClicked

    //MIGRADO Y FUNCIONANDO
    private void btneliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btneliminarActionPerformed

        if (!txtidingreso.getText().equals("")) {
            String confirmacion = JOptionPane.showInputDialog(
                    null,
                    "Por favor, ingrese el código a eliminar:",
                    "Eliminar Código",
                    JOptionPane.QUESTION_MESSAGE
            );
            System.out.println("codigo" + confirmacion);

            if (confirmacion != null && !confirmacion.trim().isEmpty()) {

                BloqueoService bcodigo = new BloqueoService();
                boolean Eliminado = bcodigo.verificarBloqueo(confirmacion);
                //Fbloqueos codigo = new Fbloqueos();
                //boolean Eliminado = codigo.verificarBloqueo(confirmacion);

                if (Eliminado) {
                    Dingresop dts = new Dingresop();
                    IngresoService apiservice = new IngresoService();
                    //Lingresop func = new Lingresop();

                    dts.setIdingreso(Integer.parseInt(txtidingreso.getText()));
                    apiservice.eliminarIngreso(dts);
                    //func.eliminar(dts);
                    mostrar("");
                    JOptionPane.showMessageDialog(null, " Eliminado correctamente.");
                    inhabilitar();

                } else {
                    JOptionPane.showMessageDialog(null, "El CODIGO NO EXISTE.");
                }

            }

        }
    }//GEN-LAST:event_btneliminarActionPerformed

    private void cbotipovehiculoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbotipovehiculoActionPerformed
        // TODO add your handling code here:
        cbotipovehiculo.transferFocus();
    }//GEN-LAST:event_cbotipovehiculoActionPerformed

    private void cbotiposervicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbotiposervicioActionPerformed
        // TODO add your handling code here:
        cbotiposervicio.transferFocus();
    }//GEN-LAST:event_cbotiposervicioActionPerformed

    private void cboestadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboestadoActionPerformed
        // TODO add your handling code here:
        cboestado.transferFocus();
    }//GEN-LAST:event_cboestadoActionPerformed

    private void txtzonaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtzonaKeyPressed
        // TODO add your handling code here:
//        PreparedStatement pst;
//        ResultSet rs;
//        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
//            Cconexionp conexion = new Cconexionp();
//
//            try {
//                // Preparamos la consulta
//                try (Connection conectar = conexion.establecerConexionp()) {
//                    // Preparamos la consulta
//                    pst = conectar.prepareStatement("select * from zona where numero=?");
//                    pst.setString(1, txtzona.getText());
//
//                    rs = pst.executeQuery();
//
//                    if (rs.next()) {
//                        String estado = rs.getString("estado");
//                        if ("Ocupado".equalsIgnoreCase(estado)) {
//                            JOptionPane.showMessageDialog(null, "El Lugar se encuentra Ocupado.");
//                        }
//                        if ("Mantenimeinto".equalsIgnoreCase(estado)) {
//                            JOptionPane.showMessageDialog(null, "El Lugar se encuentra en Manenimiento.");
//                        }
//
//                        if ("Disponible".equals(estado)) {
//
//                        }
//                    } else {
//                        JOptionPane.showMessageDialog(null, "No se encontró el NUMERO solicitado.");
//                    }
//                }
//
//            } catch (HeadlessException | SQLException ex) {
//
//            }
//        }
    }//GEN-LAST:event_txtzonaKeyPressed

    private void txtplacaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtplacaKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String placa = txtplaca.getText().trim();

            if (placa.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Por favor ingrese una placa válida.");
                return;
            }

            try {

                String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
                URL url = new URL(baseUrl + "/api/clientes/escritorio/buscar?placa=" + URLEncoder.encode(placa, "UTF-8"));

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("Accept", "application/json");

                int responseCode = con.getResponseCode();
                if (responseCode == 200) {
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                        String respuesta = br.readLine();//Es un String simple, tipo "No prepago" o nombre del cliente
                        txtcliente.setText(respuesta);

                        //Si quieres dejar automaticamente "PREPAGO" como tipo servicio cuando si se encontró cliente
                        if (!respuesta.equalsIgnoreCase("No prepago")) {
                            cbotiposervicio.setSelectedItem("Prepago");
                        } else {
                            cbotiposervicio.setSelectedItem("General");//O el que quieras por defecto
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Error al consultar cliente. Código: " + responseCode);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error al conectar con el servidor: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_txtplacaKeyPressed

    //MIGRADO CON APIREPORTEPREPAGO
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        IngresoService reporte = new IngresoService();
        reporte.exportarExcel(tablalistado);
    }//GEN-LAST:event_jButton1ActionPerformed

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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Jingresop.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Jingresop.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Jingresop.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Jingresop.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Jingresop().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btneliminar;
    private javax.swing.JButton btnguardar;
    private javax.swing.JButton btnguardar1;
    private javax.swing.JComboBox<String> cboestado;
    private javax.swing.JComboBox<String> cbotiposervicio;
    private javax.swing.JComboBox<String> cbotipovehiculo;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lblregistro;
    public static javax.swing.JLabel lblturnos;
    private javax.swing.JTable tablalistado;
    private javax.swing.JTextField txtbuscar;
    private javax.swing.JTextField txtcliente;
    public static javax.swing.JTextField txtempleado;
    private javax.swing.JTextField txtfechaingreso;
    private javax.swing.JTextField txtidingreso;
    public static javax.swing.JTextField txtidinicio_turno;
    public static javax.swing.JTextField txtnumeroturno;
    private javax.swing.JTextField txtobservaciones;
    private javax.swing.JTextField txtplaca;
    private javax.swing.JTextField txtzona;
    // End of variables declaration//GEN-END:variables
}
