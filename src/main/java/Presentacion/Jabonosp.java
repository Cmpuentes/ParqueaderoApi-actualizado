package Presentacion;

import Datos.AbonoDTO;
import Diseños.Estado_Abonos;
import DatosP.Dabonosp;
import Diseños.Estilo_tablas;
import Logica.AbonoService;
import Logica.ApiAbonosService;
import LogicaP.AplicarIcono;
import LogicaP.Fabonosp;
import LogicaP.Tiempopro;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Jabonosp extends javax.swing.JFrame {

    public Jabonosp() {
        initComponents();
        AplicarIcono.icono(this, "/Imagenes/GN_Grande.png");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("ABONOS");
        mostrarabono("");
        mostrarfecha();
        limpiar();
        configurarTablaAbono();
        tablalistadoabono.getColumnModel().getColumn(8).setCellRenderer(new Estado_Abonos());
        AbonoService cliente = new AbonoService();
        cliente.llenarcboClientes(cbocliente);
    }
    private String accion = "guardar";

    //FUNCIÓN ADAPTADA A LA API
    private void mostrarabono(String buscar) {
        try {
            DefaultTableModel modelo;
            AbonoService func = new AbonoService();
            modelo = func.mostrar(buscar);

            tablalistadoabono.setModel(modelo);
            ocultar_columnasabono();
            registrosabono.setText("Total Registros " + Integer.toString(func.totalregistros));

        } catch (Exception e) {
            JOptionPane.showConfirmDialog(rootPane, e);
        }
    }

    private void mostrarfecha() {
        Tiempopro tiempo = new Tiempopro();
        txtfechabono.setText(tiempo.getFechacomp());
    }

    void ocultar_columnasabono() {
        int[] columnasOcultas = {0, 9, 10, 11, 12};
        Estilo_tablas.ocultarColumnas(tablalistadoabono, columnasOcultas);

    }

    private void configurarTablaAbono() {
        Estilo_tablas.configurarTabla(tablalistadoabono);
    }

    private void limpiar() {
        txtidabonos.setVisible(false);
        lblturno.setVisible(false);

        txtvalor.setText("0");
        txtefectivo.setText("0");
        txttarjeta.setText("0");
        txttransferencia.setText("0");
        txttotal.setText("0");
        txtObservaciones.setText("0");
        txtsaldo.setText("0");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel12 = new javax.swing.JPanel();
        jLabel41 = new javax.swing.JLabel();
        txtidabonos = new javax.swing.JTextField();
        cbocliente = new javax.swing.JComboBox<>();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        txtfechabono = new javax.swing.JTextField();
        txtvalor = new javax.swing.JTextField();
        txttransferencia = new javax.swing.JTextField();
        txttotal = new javax.swing.JTextField();
        txtObservaciones = new javax.swing.JTextField();
        txtempleado = new javax.swing.JTextField();
        txtnumero_turno = new javax.swing.JTextField();
        txtsaldo = new javax.swing.JTextField();
        txtefectivo = new javax.swing.JTextField();
        txttarjeta = new javax.swing.JTextField();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        Guardarabono = new javax.swing.JButton();
        Nuevos = new javax.swing.JButton();
        jLabel52 = new javax.swing.JLabel();
        EliminarBtn = new javax.swing.JButton();
        registrosabono = new javax.swing.JLabel();
        lblturno = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tablalistadoabono = new javax.swing.JTable();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel41.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel41.setText("Fecha:");

        txtidabonos.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N

        cbocliente.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        cbocliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboclienteActionPerformed(evt);
            }
        });

        jLabel42.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel42.setText("Turno:");

        jLabel43.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel43.setText("Total:");

        jLabel44.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel44.setText("Transfe:");

        jLabel45.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel45.setText("Cliente:");

        jLabel46.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel46.setText("Valor:");

        jLabel47.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel47.setText("Tarjeta:");

        jLabel49.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel49.setText("Efectivo:");

        txtfechabono.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N

        txtvalor.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        txtvalor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtvalorActionPerformed(evt);
            }
        });

        txttransferencia.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        txttransferencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txttransferenciaActionPerformed(evt);
            }
        });
        txttransferencia.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txttransferenciaKeyPressed(evt);
            }
        });

        txttotal.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N

        txtObservaciones.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N

        txtempleado.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N

        txtnumero_turno.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N

        txtsaldo.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N

        txtefectivo.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        txtefectivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtefectivoActionPerformed(evt);
            }
        });
        txtefectivo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtefectivoKeyPressed(evt);
            }
        });

        txttarjeta.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        txttarjeta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txttarjetaActionPerformed(evt);
            }
        });
        txttarjeta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txttarjetaKeyPressed(evt);
            }
        });

        jLabel50.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel50.setText("Observaciones:");

        jLabel51.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel51.setText("Empleado:");

        Guardarabono.setBackground(new java.awt.Color(204, 204, 204));
        Guardarabono.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        Guardarabono.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/disco-flexible.png"))); // NOI18N
        Guardarabono.setText("Guardar");
        Guardarabono.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        Guardarabono.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        Guardarabono.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        Guardarabono.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GuardarabonoActionPerformed(evt);
            }
        });

        Nuevos.setBackground(new java.awt.Color(204, 204, 204));
        Nuevos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/registro_1.png"))); // NOI18N
        Nuevos.setText("Nuevo");
        Nuevos.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        Nuevos.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        Nuevos.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        Nuevos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NuevosActionPerformed(evt);
            }
        });

        jLabel52.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel52.setText("Saldo:");

        EliminarBtn.setBackground(new java.awt.Color(204, 204, 204));
        EliminarBtn.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        EliminarBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/eliminar.png"))); // NOI18N
        EliminarBtn.setText("Eliminar");
        EliminarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EliminarBtnActionPerformed(evt);
            }
        });

        registrosabono.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        registrosabono.setText("total registros:");

        lblturno.setText("jLabel1");

        tablalistadoabono.setModel(new javax.swing.table.DefaultTableModel(
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
        tablalistadoabono.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablalistadoabonoMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tablalistadoabono);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(63, 63, 63)
                        .addComponent(txtidabonos, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel12Layout.createSequentialGroup()
                                        .addGap(81, 81, 81)
                                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel12Layout.createSequentialGroup()
                                                .addComponent(txtvalor, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel52)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtsaldo, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(cbocliente, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtefectivo, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txttarjeta, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txttotal, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtfechabono, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txttransferencia, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel12Layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel41)
                                            .addComponent(jLabel46)
                                            .addComponent(jLabel44)
                                            .addComponent(jLabel43)
                                            .addComponent(jLabel45)
                                            .addComponent(jLabel49)
                                            .addComponent(jLabel47))))
                                .addGap(35, 35, 35))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel12Layout.createSequentialGroup()
                                        .addGap(9, 9, 9)
                                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel12Layout.createSequentialGroup()
                                                .addGap(79, 79, 79)
                                                .addComponent(txtempleado))
                                            .addGroup(jPanel12Layout.createSequentialGroup()
                                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(jPanel12Layout.createSequentialGroup()
                                                        .addComponent(Guardarabono)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(Nuevos))
                                                    .addGroup(jPanel12Layout.createSequentialGroup()
                                                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                            .addComponent(jLabel42)
                                                            .addComponent(jLabel51))
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(txtnumero_turno, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(lblturno, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGap(0, 0, Short.MAX_VALUE))))
                                    .addGroup(jPanel12Layout.createSequentialGroup()
                                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel12Layout.createSequentialGroup()
                                                .addGap(6, 6, 6)
                                                .addComponent(txtObservaciones, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(jLabel50))
                                        .addGap(0, 0, Short.MAX_VALUE)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(EliminarBtn)
                        .addGap(352, 352, 352)
                        .addComponent(registrosabono, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE))
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 674, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtidabonos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(EliminarBtn)
                    .addComponent(registrosabono))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel41)
                            .addComponent(txtfechabono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbocliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(7, 7, 7)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel46)
                            .addComponent(txtvalor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtsaldo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel52))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel49)
                            .addComponent(txtefectivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel47)
                            .addComponent(txttarjeta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txttransferencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel44))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txttotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel43))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel50)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtObservaciones, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtempleado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel51))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtnumero_turno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel42)
                            .addComponent(lblturno))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Guardarabono)
                            .addComponent(Nuevos))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        jSeparator1.setBackground(new java.awt.Color(51, 153, 255));
        jSeparator1.setForeground(new java.awt.Color(51, 153, 255));
        jSeparator1.setToolTipText("");
        jSeparator1.setAlignmentX(1.0F);
        jSeparator1.setAlignmentY(1.0F);
        jSeparator1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jSeparator1.setOpaque(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 1031, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(458, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tablalistadoabonoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablalistadoabonoMouseClicked
        // TODO add your handling code here:
        Guardarabono.setText("Editar");
        EliminarBtn.setEnabled(true);
        accion = "editar";

        int fila = tablalistadoabono.rowAtPoint(evt.getPoint());

        txtidabonos.setText(tablalistadoabono.getValueAt(fila, 0).toString());
        txtfechabono.setText(tablalistadoabono.getValueAt(fila, 2).toString());
        cbocliente.setSelectedItem(tablalistadoabono.getValueAt(fila, 1).toString());
        txtvalor.setText(tablalistadoabono.getValueAt(fila, 3).toString());
        txtefectivo.setText(tablalistadoabono.getValueAt(fila, 4).toString());
        txttarjeta.setText(tablalistadoabono.getValueAt(fila, 5).toString());
        txttransferencia.setText(tablalistadoabono.getValueAt(fila, 6).toString());
        txttotal.setText(tablalistadoabono.getValueAt(fila, 7).toString());
        txtsaldo.setText(tablalistadoabono.getValueAt(fila, 8).toString());
        txtObservaciones.setText(tablalistadoabono.getValueAt(fila, 11).toString());
    }//GEN-LAST:event_tablalistadoabonoMouseClicked

    private void cboclienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboclienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboclienteActionPerformed

    //MIGRADO Y PROBANDO FUNCIONAMIENTO
    private void GuardarabonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GuardarabonoActionPerformed
        // TODO add your handling code here:
        if (txtvalor.getText().length() == 0) {
            JOptionPane.showConfirmDialog(rootPane, "DEBES COLOCAR UN VALOR");
            txtvalor.requestFocus();
            return;
        }

        if (cbocliente.getSelectedIndex() == 0) {
            JOptionPane.showConfirmDialog(rootPane, "DEBES SELECCIONAR UN CLIENTE");
            cbocliente.requestFocus();
            return;
        }

        AbonoDTO dts = new AbonoDTO();
        AbonoService func = new AbonoService();

        dts.setFecha(txtfechabono.getText());
        int cliente = cbocliente.getSelectedIndex();
        dts.setCliente((String) cbocliente.getItemAt(cliente));
        dts.setValor(Integer.parseInt(txtvalor.getText()));
        dts.setEfectivo(Integer.parseInt(txtefectivo.getText()));
        dts.setTarjeta(Integer.parseInt(txttarjeta.getText()));
        dts.setTransferencia(Integer.parseInt(txttransferencia.getText()));
        dts.setTotal(Integer.parseInt(txttotal.getText().replace(",", "")));
        dts.setObservaciones(txtObservaciones.getText());
        dts.setEmpleado(txtempleado.getText());
        dts.setTurno(lblturno.getText());
        dts.setNumeroturno(txtnumero_turno.getText());
        dts.setSaldo(Integer.parseInt(txtsaldo.getText().replace(",", "")));

        if (accion.equals("guardar")) {
            if (func.insertar(dts)) {
                JOptionPane.showMessageDialog(rootPane, "ABONO INGRESADO SATISFACTORIAMENTE");
                mostrarabono("");
                limpiar();
            } else {
                JOptionPane.showMessageDialog(rootPane, "NO SE PUDO GUARDAR EL ABONO");
            }

        } else if (accion.equals("editar")) {
            Integer idabono = Integer.valueOf(txtidabonos.getText());
            if (func.editar(idabono, dts)) {
                JOptionPane.showMessageDialog(rootPane, "ABONO EDITADO SATISFACTORIAMENTE");
                mostrarabono("");
                limpiar();
            } else {
                JOptionPane.showMessageDialog(rootPane, "NO SE PUDO EDITAR EL ABONO");
            }
        }
    }//GEN-LAST:event_GuardarabonoActionPerformed

    private void NuevosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NuevosActionPerformed
        // TODO add your handling code here:
        limpiar();
        Guardarabono.setText("Guardar");
        accion = "guardar";
    }//GEN-LAST:event_NuevosActionPerformed

    //POR PROBAR FUNCIONAMIENTO
    private void EliminarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EliminarBtnActionPerformed
        // TODO add your handling code here:
        if (!txtidabonos.getText().equals("")) {
            int confirmacion = JOptionPane.showConfirmDialog(rootPane, "Confirma para eliminar?", "Confirmar", JOptionPane.YES_NO_OPTION);

            if (confirmacion == JOptionPane.YES_OPTION) {

                //Dabonosp dts = new Dabonosp();
                AbonoService func = new AbonoService();
                
                Integer idabono = Integer.valueOf(txtidabonos.getText());

                //dts.setIdabono(Integer.parseInt(txtidabonos.getText()));
                func.eliminar(idabono);
                mostrarabono("");
                limpiar();

            }

        }
    }//GEN-LAST:event_EliminarBtnActionPerformed

    private void txtefectivoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtefectivoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                NumberFormat formatoMiles = NumberFormat.getNumberInstance(Locale.US);

                int efectivo = Integer.parseInt(txtefectivo.getText().replace(",", ""));
                int tarjeta = Integer.parseInt(txttarjeta.getText().replace(",", ""));
                int transferencia = Integer.parseInt(txttransferencia.getText().replace(",", ""));

                int total = (int) (efectivo + tarjeta + transferencia);

                txttotal.setText(formatoMiles.format(total));
                txtsaldo.setText(formatoMiles.format(total));

            } catch (NumberFormatException e) {

            }

        }
    }//GEN-LAST:event_txtefectivoKeyPressed

    private void txttarjetaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txttarjetaKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                NumberFormat formatoMiles = NumberFormat.getNumberInstance(Locale.US);

                int efectivo = Integer.parseInt(txtefectivo.getText().replace(",", ""));
                int tarjeta = Integer.parseInt(txttarjeta.getText().replace(",", ""));
                int transferencia = Integer.parseInt(txttransferencia.getText().replace(",", ""));

                int total = (int) (efectivo + tarjeta + transferencia);

                txttotal.setText(formatoMiles.format(total));
                txtsaldo.setText(formatoMiles.format(total));

            } catch (NumberFormatException e) {

            }

        }
    }//GEN-LAST:event_txttarjetaKeyPressed

    private void txttransferenciaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txttransferenciaKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                NumberFormat formatoMiles = NumberFormat.getNumberInstance(Locale.US);

                int efectivo = Integer.parseInt(txtefectivo.getText().replace(",", ""));
                int tarjeta = Integer.parseInt(txttarjeta.getText().replace(",", ""));
                int transferencia = Integer.parseInt(txttransferencia.getText().replace(",", ""));

                int total = (int) (efectivo + tarjeta + transferencia);

                txttotal.setText(formatoMiles.format(total));
                txtsaldo.setText(formatoMiles.format(total));

            } catch (NumberFormatException e) {

            }

        }
    }//GEN-LAST:event_txttransferenciaKeyPressed

    private void txtvalorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtvalorActionPerformed
        // TODO add your handling code here:
        txtvalor.transferFocus();
    }//GEN-LAST:event_txtvalorActionPerformed

    private void txtefectivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtefectivoActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_txtefectivoActionPerformed

    private void txttarjetaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txttarjetaActionPerformed

    }//GEN-LAST:event_txttarjetaActionPerformed

    private void txttransferenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txttransferenciaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txttransferenciaActionPerformed

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
            java.util.logging.Logger.getLogger(Jabonosp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Jabonosp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Jabonosp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Jabonosp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Jabonosp().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton EliminarBtn;
    private javax.swing.JButton Guardarabono;
    private javax.swing.JButton Nuevos;
    private javax.swing.JComboBox<String> cbocliente;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSeparator jSeparator1;
    public static javax.swing.JLabel lblturno;
    private javax.swing.JLabel registrosabono;
    private javax.swing.JTable tablalistadoabono;
    private javax.swing.JTextField txtObservaciones;
    private javax.swing.JTextField txtefectivo;
    public static javax.swing.JTextField txtempleado;
    private javax.swing.JTextField txtfechabono;
    private javax.swing.JTextField txtidabonos;
    public static javax.swing.JTextField txtnumero_turno;
    private javax.swing.JTextField txtsaldo;
    private javax.swing.JTextField txttarjeta;
    private javax.swing.JTextField txttotal;
    private javax.swing.JTextField txttransferencia;
    private javax.swing.JTextField txtvalor;
    // End of variables declaration//GEN-END:variables
}
