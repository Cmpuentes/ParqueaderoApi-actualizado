package Presentacion;


import Datos.ClienteRequestDTO;
import Diseños.Estilo_tablas;
import Logica.ApiReportePrepago;
import Logica.ClienteAdministrativoService;
import LogicaP.AplicarIcono;
import LogicaP.Tiempopro;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class RegitrarPrepago extends javax.swing.JFrame {

    private static final long serialVersionUID = 1L;

    public RegitrarPrepago() {
        initComponents();
        AplicarIcono.icono(this, "/Imagenes/GN_Grande.png");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("REGISTRO CLIENTES PREPAGO");
        mostrarcliente();
        mostrarfecha();
        filtrartabla();
        Habilitar();
    }
    private String accion = "guardar";

    private void Habilitar() {
        txtidcliente.setVisible(false);
        txtplaca.setText("");
        cbotipovehiculo.setSelectedItem("SELECCIONAR");
        cbotiposervicio.setSelectedItem("SELECCIONAR");
        txtcliente.setText("");
        cboEstado.setSelectedItem("Activo");
        txtobservaciones.setText("");
    }

    private void limpiar() {
        txtplaca.setText("");
        cbotipovehiculo.setSelectedItem("SELECCIONAR");
        cbotiposervicio.setSelectedItem("SELECCIONAR");
        txtcliente.setText("");
        cboEstado.setSelectedItem("Activo");
        txtobservaciones.setText("");
    }

    private void OcultarColumna() {
        int[] columnasOcultasC = {0, 1,4, 6, 7, 9};
        Estilo_tablas.ocultarColumnas(tablalistadocliente, columnasOcultasC);
        Estilo_tablas.configurarTabla(tablalistadocliente);
    }

    private void mostrarfecha() {
        Tiempopro tiempo = new Tiempopro();
        txtfecharegistro.setText(tiempo.getFechacomp());

    }

    private void filtrartabla() {
        txtbuscarplaca.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filtrarTablaGeneral(txtbuscarplaca, tablalistadocliente);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filtrarTablaGeneral(txtbuscarplaca, tablalistadocliente);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filtrarTablaGeneral(txtbuscarplaca, tablalistadocliente);
            }
        });
    }

    private void filtrarTablaGeneral(JTextField txtBuscar, JTable TablaPrepago) {
        @SuppressWarnings("unchecked")
        TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) TablaPrepago.getRowSorter();

        if (sorter != null) {
            String texto = txtBuscar.getText().trim();
            if (texto.isEmpty()) {
                sorter.setRowFilter(null); // Mostrar todos los registros si no hay búsqueda
            } else {
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto)); // Filtrar sin importar mayúsculas/minúsculas
            }
        }
    }

    //MIGRADO Y PROBANDO FUNCIONAMIENTO
    private void mostrarcliente() {
        try {
            DefaultTableModel modelo;
            ClienteAdministrativoService func = new ClienteAdministrativoService();
            modelo = func.mostrar(tablalistadocliente);

            tablalistadocliente.setModel(modelo);
            OcultarColumna();
            registros.setText("Total Registros " + Integer.toString(func.totalregistros));

        } catch (Exception e) {
            JOptionPane.showConfirmDialog(rootPane, e);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel9 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        txtfecharegistro = new javax.swing.JTextField();
        txtplaca = new javax.swing.JTextField();
        txtcliente = new javax.swing.JTextField();
        Txttelefono = new javax.swing.JTextField();
        txtobservaciones = new javax.swing.JTextField();
        txtidcliente = new javax.swing.JTextField();
        cbotipovehiculo = new javax.swing.JComboBox<>();
        cbotiposervicio = new javax.swing.JComboBox<>();
        cboEstado = new javax.swing.JComboBox<>();
        Guardar = new javax.swing.JButton();
        Nuevo = new javax.swing.JButton();
        txtTarifas = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablalistadocliente = new javax.swing.JTable();
        jButton3 = new javax.swing.JButton();
        EliminarBtn = new javax.swing.JButton();
        txtbuscarplaca = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        registros = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "DATOS EMPRESA", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 12))); // NOI18N

        jLabel24.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jLabel24.setText("FECHA:");

        jLabel25.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jLabel25.setText("PLACA:");

        jLabel32.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jLabel32.setText("TIPO VEHICULO:");

        jLabel33.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jLabel33.setText("TIPO SERVICIO:");

        jLabel34.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jLabel34.setText("EMPRESA:");

        jLabel37.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jLabel37.setText("TELEFONO:");

        jLabel38.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jLabel38.setText("ESTADO:");

        jLabel39.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jLabel39.setText("Observaciones:");

        txtfecharegistro.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N

        txtplaca.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N

        txtcliente.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N

        Txttelefono.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N

        txtobservaciones.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N

        cbotipovehiculo.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        cbotipovehiculo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SELECCIONAR", "MOTO", "TRICICLOS", "AUTO", "BUSETA", "VOLQUETAS", "VACTOR", "GRUAS", "TRACTOMULA", "TRANSBORDO", "CARRO TANQUE", "CAMION PEQUEÑO", "CAMION GRANDE" }));
        cbotipovehiculo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbotipovehiculoActionPerformed(evt);
            }
        });

        cbotiposervicio.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        cbotiposervicio.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SELECCIONAR", "PREPAGO" }));
        cbotiposervicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbotiposervicioActionPerformed(evt);
            }
        });

        cboEstado.setEditable(true);
        cboEstado.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        cboEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activo", "Finalizado" }));
        cboEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboEstadoActionPerformed(evt);
            }
        });

        Guardar.setBackground(new java.awt.Color(204, 204, 204));
        Guardar.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        Guardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/disco-flexible.png"))); // NOI18N
        Guardar.setText("Guardar");
        Guardar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        Guardar.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        Guardar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        Guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GuardarActionPerformed(evt);
            }
        });

        Nuevo.setBackground(new java.awt.Color(204, 204, 204));
        Nuevo.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        Nuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/registro_1.png"))); // NOI18N
        Nuevo.setText("Nuevo");
        Nuevo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        Nuevo.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        Nuevo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        Nuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NuevoActionPerformed(evt);
            }
        });

        jLabel40.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jLabel40.setText("TARIFA:");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel24, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel25, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel32, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel33, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel34, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel37, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel40, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel38, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel39, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbotipovehiculo, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTarifas, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(cboEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtidcliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(Txttelefono, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtcliente, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(cbotiposervicio, javax.swing.GroupLayout.Alignment.LEADING, 0, 200, Short.MAX_VALUE))
                            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txtplaca, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                                .addComponent(txtfecharegistro, javax.swing.GroupLayout.Alignment.LEADING))))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtobservaciones, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(Guardar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Nuevo)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtfecharegistro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24))
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel25)
                            .addComponent(txtplaca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel32)
                            .addComponent(cbotipovehiculo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel33)
                            .addComponent(cbotiposervicio, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel34)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel37)
                                    .addComponent(Txttelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtTarifas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel40)))
                            .addComponent(txtcliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cboEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel38)
                            .addComponent(txtidcliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel39)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtobservaciones, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE)
                        .addComponent(Nuevo))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(Guardar)))
                .addContainerGap())
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "LISTA", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 12))); // NOI18N

        tablalistadocliente.setModel(new javax.swing.table.DefaultTableModel(
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
        tablalistadocliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablalistadoclienteMouseClicked(evt);
            }
        });
        tablalistadocliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tablalistadoclienteKeyPressed(evt);
            }
        });
        jScrollPane3.setViewportView(tablalistadocliente);

        jButton3.setBackground(new java.awt.Color(204, 204, 204));
        jButton3.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Excel.png"))); // NOI18N
        jButton3.setText("Exportar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        EliminarBtn.setBackground(new java.awt.Color(204, 204, 204));
        EliminarBtn.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        EliminarBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/basura.png"))); // NOI18N
        EliminarBtn.setText("Eliminar");
        EliminarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EliminarBtnActionPerformed(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel22.setText("BUSCAR:");

        registros.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        registros.setText("TOTAL REGISTROS:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel22)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtbuscarplaca)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(EliminarBtn))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(registros, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(EliminarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtbuscarplaca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel22))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(registros)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tablalistadoclienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablalistadoclienteMouseClicked
        // TODO add your handling code here:

        Guardar.setText("Editar");
        EliminarBtn.setEnabled(true);
        accion = "editar";

        int fila = tablalistadocliente.rowAtPoint(evt.getPoint());

        txtidcliente.setText(tablalistadocliente.getValueAt(fila, 0).toString());
        txtfecharegistro.setText(tablalistadocliente.getValueAt(fila, 1).toString());
        txtplaca.setText(tablalistadocliente.getValueAt(fila, 2).toString());
        cbotipovehiculo.setSelectedItem(tablalistadocliente.getValueAt(fila, 3).toString());
        cbotiposervicio.setSelectedItem(tablalistadocliente.getValueAt(fila, 4).toString());
        txtcliente.setText(tablalistadocliente.getValueAt(fila, 5).toString());
        Txttelefono.setText(tablalistadocliente.getValueAt(fila, 6).toString());
        txtTarifas.setText(tablalistadocliente.getValueAt(fila, 7).toString());
        cboEstado.setSelectedItem(tablalistadocliente.getValueAt(fila, 8).toString());
        txtobservaciones.setText(tablalistadocliente.getValueAt(fila, 9).toString());

    }//GEN-LAST:event_tablalistadoclienteMouseClicked

    private void tablalistadoclienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tablalistadoclienteKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tablalistadoclienteKeyPressed

    //MIGRADO Y PROBANDO FUCNIONAMIENTO
    private void EliminarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EliminarBtnActionPerformed

        if (!txtidcliente.getText().equals("")) {
            int confirmacion = JOptionPane.showConfirmDialog(rootPane, "Confirma para eliminar?", "Confirmar", JOptionPane.YES_NO_OPTION);

            if (confirmacion == JOptionPane.YES_OPTION) {

                ClienteAdministrativoService func = new ClienteAdministrativoService();

                Integer idcliente = Integer.valueOf(txtidcliente.getText());
                
                func.eliminar(idcliente);
                mostrarcliente();
                Habilitar();

            }

        }
    }//GEN-LAST:event_EliminarBtnActionPerformed

    private void cbotipovehiculoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbotipovehiculoActionPerformed
        // TODO add your handling code here:
        cbotipovehiculo.transferFocus();
    }//GEN-LAST:event_cbotipovehiculoActionPerformed

    private void cbotiposervicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbotiposervicioActionPerformed
        // TODO add your handling code here:
        cbotiposervicio.transferFocus();
    }//GEN-LAST:event_cbotiposervicioActionPerformed

    private void cboEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboEstadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboEstadoActionPerformed

    //MIGRADO Y PROBANDO FUNCIONAMIENTO
    private void GuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GuardarActionPerformed
        // TODO add your handling code here:
        if (txtplaca.getText().length() == 0) {
            JOptionPane.showConfirmDialog(rootPane, "DEBES INGRESAR UNA PLACA");
            txtplaca.requestFocus();
            return;
        }

        if (cbotipovehiculo.getSelectedIndex() == 0) {
            JOptionPane.showConfirmDialog(rootPane, "DEBES SELECCIONAR UN TIPO DE VEHICULO");
            cbotipovehiculo.requestFocus();
            return;
        }
        if (cbotiposervicio.getSelectedIndex() == 0) {
            JOptionPane.showConfirmDialog(rootPane, "DEBES SELECCIONAR UN TIPO DE SERVICIO");
            cbotiposervicio.requestFocus();
            return;
        }
        if (txtcliente.getText().length() == 0) {
            JOptionPane.showConfirmDialog(rootPane, "DEBES INGRESAR UN CLIENTE");
            txtcliente.requestFocus();
            return;
        }
        if (Txttelefono.getText().length() == 0) {
            JOptionPane.showConfirmDialog(rootPane, "DEBES INGRESAR UNA PLACA");
            Txttelefono.requestFocus();
            return;
        }

        ClienteRequestDTO dts = new ClienteRequestDTO();
        ClienteAdministrativoService func = new ClienteAdministrativoService();

        dts.setFecha(txtfecharegistro.getText());
        dts.setPlaca(txtplaca.getText());
        int tipo_vehiculo = cbotipovehiculo.getSelectedIndex();
        dts.setTipovehiculo(cbotipovehiculo.getItemAt(tipo_vehiculo));
        int tipo_servicio = cbotiposervicio.getSelectedIndex();
        dts.setTiposervicio(cbotiposervicio.getItemAt(tipo_servicio));
        dts.setCliente(txtcliente.getText());
        dts.setTelefono(Txttelefono.getText());
        dts.setTarifas(Integer.parseInt(txtTarifas.getText()));
        int estado = cboEstado.getSelectedIndex();
        dts.setEstado(cboEstado.getItemAt(estado));
        dts.setObservaciones(txtobservaciones.getText());

        if (accion.equals("guardar")) {
            if (func.insertar(dts)) {
                JOptionPane.showMessageDialog(rootPane, " INGRESO SATISFACTORIAMENTE");
                mostrarcliente();
                limpiar();

            }

        } else if (accion.equals("editar")) {
            Integer idcliente = Integer.valueOf(txtidcliente.getText());
            if (func.editar(idcliente, dts)) {
                JOptionPane.showMessageDialog(rootPane, "EDITADO SATISFACTORIAMENTE");
                mostrarcliente();
                limpiar();
                
            }
        }
    }//GEN-LAST:event_GuardarActionPerformed

    private void NuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NuevoActionPerformed
        // TODO add your handling code here:
        limpiar();
        Guardar.setText("Guardar");
        accion = "guardar";
    }//GEN-LAST:event_NuevoActionPerformed

    //MIGRADO CON APIREPORTEPREPAGO
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        ApiReportePrepago metodos = new ApiReportePrepago();
        metodos.exportarExcel(tablalistadocliente);
    }//GEN-LAST:event_jButton3ActionPerformed

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
            java.util.logging.Logger.getLogger(RegitrarPrepago.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RegitrarPrepago.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RegitrarPrepago.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RegitrarPrepago.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new RegitrarPrepago().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton EliminarBtn;
    private javax.swing.JButton Guardar;
    private javax.swing.JButton Nuevo;
    private javax.swing.JTextField Txttelefono;
    private javax.swing.JComboBox<String> cboEstado;
    private javax.swing.JComboBox<String> cbotiposervicio;
    private javax.swing.JComboBox<String> cbotipovehiculo;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel registros;
    private javax.swing.JTable tablalistadocliente;
    private javax.swing.JTextField txtTarifas;
    private javax.swing.JTextField txtbuscarplaca;
    private javax.swing.JTextField txtcliente;
    private javax.swing.JTextField txtfecharegistro;
    private javax.swing.JTextField txtidcliente;
    private javax.swing.JTextField txtobservaciones;
    private javax.swing.JTextField txtplaca;
    // End of variables declaration//GEN-END:variables
}
