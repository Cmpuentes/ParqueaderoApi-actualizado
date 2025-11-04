package Presentacion;

import Datos.PrepagoResponseDTO;
import Datos.SalidaConsultaDTO;
import Datos.TarifasDTO;
import DatosP.Dcliente;
import DatosP.Dsalidap;
import ImpresionP.ImprimirSalidap;
import Logica.AbonoService;
import Logica.ApiAbonosService;
import Logica.ApiIngresoService;
import Logica.ApiSalidaService;
import Logica.ApiTarifaClient;
import Logica.IngresoService;
import Logica.SalidaService;
import Logica.TarifaService;
import LogicaP.AplicarIcono;
import LogicaP.Fabonosp;
import LogicaP.Fsalidap;
import LogicaP.Tiempopro;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.print.PrinterException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public class Jsalidap extends javax.swing.JFrame {

    Tiempopro time = new Tiempopro();

    public Jsalidap() {
        initComponents();
        AplicarIcono.icono(this, "/Imagenes/GN_Grande.png");
        setLocationRelativeTo(null);
        setTitle("SALIDA VEHICULO");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mostrarTiempo();
        agregarWindowFocusListener();
        limpiar();
        ocultar();
        AutoCompleteDecoreitor();
        configurarEventosComboBox();
        llenarComboBoxPlacasDesdeApi();
    }
    
//Se llena el combobox con las placas desde la base de datos por medio de la api
    private void llenarComboBoxPlacasDesdeApi() {
        List<String> placas = IngresoService.obtenerPlacasActivas();
        cboplaca.removeAllItems();
        for (String placa : placas) {
            cboplaca.addItem(placa);
        }
    }

    private void configurarEventosComboBox() {
        // KeyListener: Solo responde al presionar Enter
        cboplaca.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String placa = cboplaca.getEditor().getItem().toString().trim();
                    if (!placa.isEmpty()) {
                        consultarYMostrarDatos(placa);
                    }
                }
            }
        });
    }

    //Consultar datos valores de la placa que se mostraran en el formulario de salida
    private void consultarYMostrarDatos(String placa) {
        SalidaConsultaDTO dto = IngresoService.consultarIngresoPorPlaca(placa);
        if (dto != null) {
            txtidingreso.setText(String.valueOf(dto.getIdingreso()));
            txtfechaentrada.setText(dto.getFechaingreso());
            txtcliente.setText(dto.getCliente());
            txtzona.setText(dto.getZona());
            txtempleadoentrada.setText(dto.getEmpleado());
            txtturnoentrada.setText(String.valueOf(dto.getNumeroturno()));
            System.out.println("Tipo vehículo desde backend: " + dto.getTipovehiculo());
            System.out.println("Tipo servicio desde backend: " + dto.getTiposervicio());

            cbotipovehiculo.setSelectedItem(dto.getTipovehiculo());
            cbotiposervicio.setSelectedItem(dto.getTiposervicio());

            // Aquí puedes llamar a procesarFechas si tienes la fecha salida ya colocada
            if (!txtfechasalida.getText().isEmpty()) {
                procesarFechas(dto.getFechaingreso(), txtfechasalida.getText());
            }
        }
    }

    private void procesarFechas(String fechaHoraIngreso, String fechaHoraSalida) {
        try {
            DateTimeFormatter formatoFechaHora = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a", Locale.ENGLISH);
            LocalDateTime fechaIngreso = LocalDateTime.parse(fechaHoraIngreso, formatoFechaHora);
            LocalDateTime fechaSalida = LocalDateTime.parse(fechaHoraSalida, formatoFechaHora);

            Duration duracion = Duration.between(fechaIngreso, fechaSalida);
            long totalMinutos = duracion.toMinutes();

            if (totalMinutos <= 15) {
                txtdias.setText("0");
                txthoras.setText("0");
                txtminutos.setText("0");

                JOptionPane.showMessageDialog(null, "Dentro del tiempo de gracia. No hay cobro.");
                return;
            }

            // Calcular horas completas y minutos restantes
            long horasCompletas = totalMinutos / 60;
            long minutosRestantes = totalMinutos % 60;

            boolean horaExtraCobrada = false;

//            // Si los minutos restantes son 15 o más, sumar una hora adicional para el cobro
//            if (minutosRestantes >= 15) {
//                horaExtraCobrada = true;
//            }
            // Calcular días, horas y minutos para la visualización
            long dias = horasCompletas / 24;
            long horas = horasCompletas % 24;

            txtdias.setText(String.valueOf(dias));
            txthoras.setText(String.valueOf(horas));
            txtminutos.setText(String.valueOf(minutosRestantes));

            // Ajustar horas completas para el cálculo del costo
            if (horaExtraCobrada) {
                horasCompletas++;
            }

            // Realizar el cálculo del costo
            calcularCosto(dias, horasCompletas % 24, minutosRestantes);

        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(null, "Error al procesar las fechas: " + e.getMessage());
        }
    }

    //Función para calcular costo
    private void calcularCosto(long dias, long horas, long minutos) {
        System.out.println("#Horas" + horas);
        System.out.println("#Dias" + dias);

        NumberFormat formatoMiles = NumberFormat.getNumberInstance(Locale.US);
        String placa = (String) cboplaca.getSelectedItem();
        String tiposervicio = (String) cbotiposervicio.getSelectedItem();
        String tipovehiculo = (String) cbotipovehiculo.getSelectedItem();

        if (tiposervicio == null || tipovehiculo == null || placa == null) {
            JOptionPane.showMessageDialog(null, "Faltan datos para calcular el costo.");
            return;
        }

        int costoDia = 0;
        int costoHoras = 0;
        TarifasDTO tarifas = null;
        Dcliente Tarifas = null;

        // Si es empleado o cliente del hotel, no se cobra
        if (tiposervicio.equalsIgnoreCase("EMPLEADO") || tiposervicio.equalsIgnoreCase("CLIENTE HOTEL")) {
            txtvalor.setText(formatoMiles.format(0));
            return;
        }

        // Obtener tarifas según el tipo de servicio, se hace desde la api
        if (tiposervicio.equalsIgnoreCase("GENERAL")) {
            tarifas = TarifaService.obtenerTarifa(tipovehiculo, tiposervicio);
            if (tarifas == null) {
                JOptionPane.showMessageDialog(null, "No se pudo obtener los precios para el servicio GENERAL desde la API.");
                return;
            }
        } 

        if (tiposervicio.equalsIgnoreCase("GENERAL")) {
            int precio12H = tarifas.getPrecio12h();
            int precioHora = tarifas.getPreciohoras();

            txtDias.setText(formatoMiles.format(precio12H));
            txtHoras.setText(formatoMiles.format(precioHora));

            if (minutos > 15) {
                horas++; // Si hay más de 15 minutos, se redondea a la siguiente hora
            }

            // 1️⃣ Cobro por días completos (cada día equivale a 2 bloques de 12h)
            if (dias > 0) {
                costoDia = (int) (dias * (precio12H * 2));
            }

            // 2️⃣ Si el tiempo es menor o igual a 3h 15m, se cobra como 3 horas
            if (horas <= 3 || (horas == 3 && minutos <= 15)) {
                costoHoras = (int) (horas * precioHora); // Siempre cobra 3 horas completas
                System.out.println("Valor Horas" + costoHoras);
            } // 3️⃣ Si está entre 3h 15m y 12h, cobra un bloque de 12h
            else if (horas > 3 && horas <= 12) {
                costoDia += precio12H;
                System.out.println("Valor Dias" + costoDia);
            } //Si pasa de 12h pero es menor o igual a 14h 15m, cobra 12h + las 3 primeras horas después del bloque
            else if (horas > 12 && (horas <= 15 || (horas == 15 && minutos <= 15))) {
                costoDia += precio12H; // Cobrar el primer bloque de 12h
                int horasExtra = (int) (horas - 12);
                costoHoras = (int) (horasExtra * precioHora); // Cobrar solo las primeras 2 horas extra
//                System.out.println("Valor dia: " + costoDia);
//                System.out.println("Valor Horas: " + costoHoras);
            } // 5️⃣ Si supera las 14h 15m, cobra otro bloque de 12h completo
            else {
                costoDia += 2 * precio12H; // Cobrar otro bloque de 12h
            }

        }

        int costoTotal = costoDia + costoHoras;

        // Asignar valores a los campos de texto
        if (tiposervicio.equalsIgnoreCase("PREPAGO")) {
            txtvalor.setText(formatoMiles.format(costoTotal));
            txtsubtotal.setText(formatoMiles.format(0));
        } else {
            txtsubtotal.setText(formatoMiles.format(costoTotal));
        }

        txtvalor.setText(formatoMiles.format(costoTotal));
    }

    private void AutoCompleteDecoreitor() {
        AutoCompleteDecorator.decorate(cboplaca);

    }

    private final String accion = "guardar";

    private void mostrarTiempo() {

        txtfechasalida.setText(time.getFechacomp() + " " + time.getHoracomp());
    }

    private void agregarWindowFocusListener() {
        this.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {

                generarnumero();
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                // No hacer nada cuando la ventana pierde el foco
            }
        });
    }

    private void calacularDescuento() {
        NumberFormat formatoMiles = NumberFormat.getNumberInstance(Locale.US);

        // Obtener el valor del descuento
        int descuento = Integer.parseInt(txtnumero_recibo.getText().replace(",", ""));
        int dias = Integer.parseInt(txtdias.getText());
        long horas = Long.parseLong(txthoras.getText());
        long minutos = Long.parseLong(txtminutos.getText());
        String placa = (String) cboplaca.getSelectedItem();
        String tiposervicio = (String) cbotiposervicio.getSelectedItem();
        String tipovehiculo = (String) cbotipovehiculo.getSelectedItem();

        Fsalidap precio = new Fsalidap();

        // Validar datos esenciales
        if (tiposervicio == null || tipovehiculo == null || placa == null) {
            JOptionPane.showMessageDialog(null, "Faltan datos para calcular el costo.");
            return;
        }

        // Inicializamos los costos por días y horas
        double costoPorDias = 0;
        double costoPorHoras = 0;

        if (tiposervicio.equalsIgnoreCase("GENERAL")) {
            // Ejecutamos la consulta para el servicio GENERAL
            // Obtener los valores de tarifa
            TarifasDTO tarifas = TarifaService.obtenerTarifa(tipovehiculo, tiposervicio);

            if (tarifas == null) {
                JOptionPane.showMessageDialog(null, "No se pudo obtener los precios para el servicio GENERAL.");
                return;
            }
            
            int descuentoRecibo = tarifas.getDescuentorecibo();
    int     precioHoras = tarifas.getPreciohoras();
            
            if (descuentoRecibo <= 0 || precioHoras <= 0) {
                JOptionPane.showMessageDialog(null, "Los precios obtenidos no son válidos.");
                return;
            }
            // Calcular el costo por días
            if (dias > 0) {
                costoPorDias = (descuentoRecibo * 2) * dias;
            }
            // Calcular el costo por horas
            if (horas > 0 || minutos > 0) {
                // Si los minutos son 15 o más, sumamos una hora adicional
                if (minutos >= 15) {
                    horas++;
                    minutos = 0; // Reseteamos los minutos a 0 después de sumar la hora
                }
                // Si las horas superan las 24 horas, agregamos días adicionales
                if (horas >= 24) {
                    long diasExtras = horas / 24;  // Calculamos los días extras
                    dias += diasExtras;  // Agregamos los días extras
                    horas = horas % 24;  // Reajustamos las horas que quedan después de agregar los días
                }
                costoPorHoras = precioHoras * horas; // Solo se cobra por las horas ajustadas
//                System.out.println("costo horas: " + costoPorHoras);
            }
            // Ahora, si las horas superan el costo por un día, se ajusta el costo
            if (costoPorHoras > descuentoRecibo) {
                costoPorHoras = descuentoRecibo;
            }

            // Calcular el costo total sin descuento
            double costoTotal = costoPorDias + costoPorHoras;
            txtsubtotal.setText(formatoMiles.format((int) costoTotal));

        } else {
            JOptionPane.showMessageDialog(null, "El tipo de servicio no es válido.");
        }
    }

    private void limpiar() {

        cboplaca.setSelectedItem("");
        cbotipovehiculo.setSelectedItem("SELECCIONAR");
        cbotipovehiculo.setSelectedItem("SELECCIONAR");
        txtfechaentrada.setText("");
        txtcliente.setText("O");
        txtzona.setText("");
        txtdias.setText("0");
        txthoras.setText("0");
        txtminutos.setText("0");
        mostrarTiempo();//actualiza la fecha de salida

        txtvalor.setText("0");
        txtnumero_recibo.setText("0");
        txtsubtotal.setText("0");
        txtefectivo.setText("0");
        txttarjeta.setText("0");
        txttransferencia.setText("0");
        txttotal.setText("0");
        txtidcliente.setText("0");
        txtdescuento.setText("0");
        txtDias.setText("0");
        txtHoras.setText("0");
    }

    private void ocultar() {
        lblturno.setVisible(false);
        txtidingreso.setVisible(false);
        txtidcliente.setVisible(false);

    }

    private void generarnumero() {
        SalidaService func = new SalidaService();
        int numero = func.generarComprobante();
        txtnumerofactura.setText(String.valueOf(numero));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txtcliente = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txtfechaentrada = new javax.swing.JTextField();
        txtfechasalida = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        txtidingreso = new javax.swing.JTextField();
        btnguardar = new javax.swing.JButton();
        btnguardar1 = new javax.swing.JButton();
        txtidcliente = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        txtturnoentrada = new javax.swing.JTextField();
        txtempleadoentrada = new javax.swing.JTextField();
        txtempleadosalida = new javax.swing.JTextField();
        txtturnosalida = new javax.swing.JTextField();
        lblturno = new javax.swing.JLabel();
        txtnumerofactura = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtzona = new javax.swing.JTextField();
        cboplaca = new javax.swing.JComboBox<>();
        cbotiposervicio = new javax.swing.JComboBox<>();
        cbotipovehiculo = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        txtDias = new javax.swing.JTextField();
        txtHoras = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        txtdias = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txthoras = new javax.swing.JTextField();
        txtminutos = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtvalor = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txttotal = new javax.swing.JTextField();
        txttransferencia = new javax.swing.JTextField();
        txttarjeta = new javax.swing.JTextField();
        txtefectivo = new javax.swing.JTextField();
        txtnumero_recibo = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        txtsubtotal = new javax.swing.JTextField();
        txtdescuento = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel5.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel5.setText("Cliente:");

        txtcliente.setEditable(false);
        txtcliente.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txtcliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtclienteActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel3.setText("Tipo servicio:");

        jLabel7.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel7.setText("Placa:");

        jLabel8.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel8.setText("Tipo Vehiculo:");

        jLabel16.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel16.setText("Fecha salida:");

        jLabel17.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel17.setText("Fecha entrada:");

        txtfechaentrada.setEditable(false);
        txtfechaentrada.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N

        txtfechasalida.setEditable(false);
        txtfechasalida.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N

        jLabel19.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel19.setText("ZONA:");

        txtidingreso.setText("0");

        btnguardar.setBackground(new java.awt.Color(204, 204, 204));
        btnguardar.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        btnguardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/salida.png"))); // NOI18N
        btnguardar.setText("Salida");
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

        txtidcliente.setText("0");

        jLabel21.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel21.setText("Turno entrada:");

        jLabel22.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel22.setText("Turno salida:");

        txtturnoentrada.setEditable(false);

        txtempleadoentrada.setEditable(false);

        txtempleadosalida.setEditable(false);

        txtturnosalida.setEditable(false);

        lblturno.setText("turno");

        txtnumerofactura.setEditable(false);
        txtnumerofactura.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txtnumerofactura.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel2.setText("N° Factura:");

        txtzona.setEditable(false);
        txtzona.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txtzona.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtzona.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtzonaActionPerformed(evt);
            }
        });

        cboplaca.setBackground(new java.awt.Color(255, 255, 204));
        cboplaca.setEditable(true);
        cboplaca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboplacaActionPerformed(evt);
            }
        });
        cboplaca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cboplacaKeyPressed(evt);
            }
        });

        cbotiposervicio.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        cbotiposervicio.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SELECCIONAR", "GENERAL", "PREPAGO", "EMPLEADO", "CLIENTE HOTEL" }));
        cbotiposervicio.setEnabled(false);
        cbotiposervicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbotiposervicioActionPerformed(evt);
            }
        });

        cbotipovehiculo.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        cbotipovehiculo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SELECCIONAR", "MOTO", "TRICICLO", "AUTO", "BUSETA", "VOLQUETAS", "VACTOR", "GRUAS", "TRACTOMULA", "TRANSBORDO", "CARRO TANQUE", "CAMION PEQUEÑO", "CAMION GRANDE" }));
        cbotipovehiculo.setEnabled(false);
        cbotipovehiculo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbotipovehiculoActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(204, 204, 204));
        jButton1.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/copia-de-datos.png"))); // NOI18N
        jButton1.setText("copia");
        jButton1.setBorder(null);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        txtDias.setEditable(false);
        txtDias.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N

        txtHoras.setEditable(false);
        txtHoras.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jLabel1.setText("Valor * 12Hs:");

        jLabel11.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jLabel11.setText("Valor * Horas:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(btnguardar, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnguardar1, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel22)
                            .addComponent(jLabel21)
                            .addComponent(lblturno))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtturnoentrada, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtturnosalida, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtempleadoentrada)
                                    .addComponent(txtempleadosalida, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtidcliente, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtidingreso, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(cboplaca, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtcliente, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtfechasalida, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtfechaentrada, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cbotiposervicio, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cbotipovehiculo, 0, 200, Short.MAX_VALUE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txtzona, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel1))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txtnumerofactura, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel11)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtHoras, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtDias, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(14, 14, 14)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(cboplaca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbotipovehiculo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(9, 9, 9)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(cbotiposervicio, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(37, 37, 37)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtfechasalida, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel16)))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtfechaentrada, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel17)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtcliente, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(txtzona, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtnumerofactura, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtHoras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addGap(43, 43, 43)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblturno)
                    .addComponent(txtidcliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtidingreso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(txtturnoentrada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtempleadoentrada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(txtturnosalida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtempleadosalida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnguardar1, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnguardar, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        txtdias.setEditable(false);
        txtdias.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txtdias.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtdias.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtdiasActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel9.setText("Dias:");

        jLabel10.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel10.setText("Horas:");

        txthoras.setEditable(false);
        txthoras.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txthoras.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txthoras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txthorasActionPerformed(evt);
            }
        });

        txtminutos.setEditable(false);
        txtminutos.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txtminutos.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtminutos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtminutosActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel6.setText("Minutos:");

        txtvalor.setEditable(false);
        txtvalor.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel4.setText("Valor:");

        jLabel12.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel12.setText("Subtotal:");

        jLabel13.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel13.setText("Efectivo:");

        jLabel14.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel14.setText("Tarjeta:");

        jLabel15.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel15.setText("Transferencia:");

        jLabel18.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel18.setText("Total:");

        txttotal.setEditable(false);
        txttotal.setBackground(new java.awt.Color(255, 255, 204));
        txttotal.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        txttotal.setBorder(null);

        txttransferencia.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        txttransferencia.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txttransferenciaKeyPressed(evt);
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

        txtnumero_recibo.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txtnumero_recibo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnumero_reciboActionPerformed(evt);
            }
        });
        txtnumero_recibo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtnumero_reciboKeyPressed(evt);
            }
        });

        jLabel26.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel26.setText("No Recibo:");

        txtsubtotal.setEditable(false);
        txtsubtotal.setBackground(new java.awt.Color(255, 255, 204));
        txtsubtotal.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N

        txtdescuento.setEditable(false);
        txtdescuento.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txtdescuento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtdescuentoActionPerformed(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel20.setText("descuento:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addComponent(jLabel9)
                                .addGap(44, 44, 44)
                                .addComponent(jLabel10)
                                .addGap(34, 34, 34)
                                .addComponent(jLabel6))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txtdias, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txthoras, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtminutos, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel26)
                            .addComponent(jLabel12)
                            .addComponent(jLabel13)
                            .addComponent(jLabel14)
                            .addComponent(jLabel15)
                            .addComponent(jLabel18)
                            .addComponent(jLabel4)
                            .addComponent(jLabel20))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtdescuento)
                            .addComponent(txttransferencia, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                            .addComponent(txttarjeta)
                            .addComponent(txtefectivo)
                            .addComponent(txtsubtotal)
                            .addComponent(txtnumero_recibo)
                            .addComponent(txtvalor)
                            .addComponent(txttotal))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel9)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtdias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txthoras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtminutos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtvalor, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtnumero_recibo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtdescuento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20))
                .addGap(5, 5, 5)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txtsubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txtefectivo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txttarjeta, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txttransferencia, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txttotal, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 389, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtclienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtclienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtclienteActionPerformed

    private void txtdiasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtdiasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtdiasActionPerformed

    private void txthorasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txthorasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txthorasActionPerformed

    private void txtminutosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtminutosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtminutosActionPerformed

    private void btnguardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnguardarActionPerformed
        int total = Integer.parseInt(txttotal.getText().replace(",", ""));
        int subtotal = Integer.parseInt(txtsubtotal.getText().replace(",", ""));
        System.out.println("total: " + total + " subtotal: " + subtotal);
        if (total != subtotal) {
            JOptionPane.showMessageDialog(rootPane, "!Por favor verificar los valores¡. \n No coinciden con lo que se debe cobrar");
            txttotal.requestFocus();
            return;
        }
        if (txtturnosalida.getText().length() == 0) {
            JOptionPane.showConfirmDialog(rootPane, "Faltan datos del empleado. por favor actualice el turno para recuperarlos");
            txtturnosalida.requestFocus();
            return;
        }
        if (lblturno.getText().length() == 0) {
            JOptionPane.showConfirmDialog(rootPane, "Faltan datos del empleado. por favor actualice el turno para recuperarlos");
            lblturno.requestFocus();
            return;
        }
        if (txtempleadosalida.getText().length() == 0) {
            JOptionPane.showConfirmDialog(rootPane, "Faltan datos del empleado. por favor actualice el turno para recuperarlos");
            txtempleadosalida.requestFocus();
            return;
        }
        Dsalidap dts = new Dsalidap();
        //Fsalidap func = new Fsalidap();
        SalidaService apiservice = new SalidaService();

        dts.setIdingreso(Integer.parseInt(txtidingreso.getText()));
        int placa = cboplaca.getSelectedIndex();
        dts.setPlaca(cboplaca.getItemAt(placa));
        int tipovehiculo = cbotipovehiculo.getSelectedIndex();
        dts.setTipovehiculo(cbotipovehiculo.getItemAt(tipovehiculo));
        int tiposervicio = cbotiposervicio.getSelectedIndex();
        dts.setTiposervicio(cbotiposervicio.getItemAt(tiposervicio));
        dts.setCliente(txtcliente.getText());
        dts.setFechaentrada(txtfechaentrada.getText());
        dts.setFechasalida(txtfechasalida.getText());
        dts.setZona(txtzona.getText());
        dts.setNumfactura(txtnumerofactura.getText());
        dts.setDias(Integer.parseInt(txtdias.getText()));
        dts.setHoras(Integer.parseInt(txthoras.getText()));
        dts.setMinutos(Integer.parseInt(txtminutos.getText()));
        dts.setValor(Integer.parseInt(txtvalor.getText().replace(",", "")));
        dts.setNumero_recibo(Integer.parseInt(txtnumero_recibo.getText()));
        dts.setDescuento(Integer.parseInt(txtdescuento.getText().replace(",", "")));
        dts.setSubtotal(Integer.parseInt(txtsubtotal.getText().replace(",", "")));
        dts.setEfectivo(Integer.parseInt(txtefectivo.getText().replace(",", "")));
        dts.setTarjeta(Integer.parseInt(txttarjeta.getText().replace(",", "")));
        dts.setTransferencia(Integer.parseInt(txttransferencia.getText().replace(",", "")));
        dts.setTotal(Integer.parseInt(txttotal.getText().replace(",", "")));
        dts.setTurno(lblturno.getText());
        dts.setTurnoentrada(Integer.parseInt(txtturnoentrada.getText()));
        dts.setEmpleadoentrada(txtempleadoentrada.getText());
        dts.setTurnosalida(Integer.parseInt(txtturnosalida.getText()));
        dts.setEmpleadosalida(txtempleadosalida.getText());

        if (accion.equals("guardar")) {
            //Se guarda con la api
            if (apiservice.registrarSalida(dts)) {
                JOptionPane.showMessageDialog(rootPane, "Salida de vehiculo exitosa");

                //Lingresop cambioestado = new Lingresop();
//                Dingresop dtsestado = new Dingresop();
//                dtsestado.setIdingreso(Integer.parseInt(txtidingreso.getText()));
                //cambioestado.CambioEstado(dtsestado);
                int confirmacion = JOptionPane.showConfirmDialog(rootPane, "¿Deseas imprimir?", "Confirmar", JOptionPane.YES_NO_OPTION);

                if (confirmacion == JOptionPane.YES_OPTION) {
                    SalidaService imprimir = new SalidaService();
                    try {
                        imprimir.ImprimirFacturaPago();
                    } catch (FileNotFoundException ex) {
                        JOptionPane.showMessageDialog(rootPane, "Error: Archivo no encontrado.\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (IOException | PrinterException ex) {
                        Logger.getLogger(Jsalidap.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                AbonoService precio = new AbonoService();
                String cliente = txtcliente.getText();
                String fechaentrada = txtfechaentrada.getText();
                String fechasalida = txtfechasalida.getText();

// Formato de las fechas (ajusta el formato según cómo están en tu sistema)
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a", Locale.ENGLISH);

                try {
                    // Convertir las fechas de String a LocalDateTime
                    LocalDateTime entrada = LocalDateTime.parse(fechaentrada, formatter);
                    LocalDateTime salida = LocalDateTime.parse(fechasalida, formatter);

                    // Calcular la diferencia en horas
                    long horasDiferencia = Duration.between(entrada, salida).toHours();

                    // Si se han cumplido 24 horas, obtener la tarifa y proceder con el cobro
                    if (horasDiferencia >= 24) {
                        PrepagoResponseDTO tarifa = precio.obtenerPrepago(cliente);

                    } else {

                    }
                } catch (Exception e) {

                }

            }
            limpiar();
        }

    }//GEN-LAST:event_btnguardarActionPerformed

    private void btnguardar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnguardar1ActionPerformed
        // TODO add your handling code here:
        limpiar();
    }//GEN-LAST:event_btnguardar1ActionPerformed

    private void txtnumero_reciboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnumero_reciboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtnumero_reciboActionPerformed

    private void txtnumero_reciboKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtnumero_reciboKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            NumberFormat formatoMiles = NumberFormat.getNumberInstance(Locale.US);

            try {

                calacularDescuento();

                int valor = Integer.parseInt(txtvalor.getText().replace(",", ""));
                int subtotal = Integer.parseInt(txtsubtotal.getText().replace(",", ""));
                int calculo = valor - subtotal;
                txtdescuento.setText(formatoMiles.format(calculo));

            } catch (NumberFormatException e) {
            }

        }


    }//GEN-LAST:event_txtnumero_reciboKeyPressed

    private void txtzonaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtzonaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtzonaActionPerformed

    private void txtefectivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtefectivoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtefectivoActionPerformed

    private void txtefectivoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtefectivoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            NumberFormat formatoMiles = NumberFormat.getNumberInstance(Locale.US);
            try {
                // Obtener los valores de los campos de texto y convertirlos a enteros
                int efectivo = Integer.parseInt(txtefectivo.getText().replace(",", ""));
                int tarjeta = Integer.parseInt(txttarjeta.getText().replace(",", ""));
                int transferencia = Integer.parseInt(txttransferencia.getText().replace(",", ""));

                int totalPago = efectivo + tarjeta + transferencia;
                txttotal.setText(formatoMiles.format(totalPago));

            } catch (NumberFormatException e) {
                // Manejar la excepción si alguno de los campos de texto no contiene un número válido
                JOptionPane.showMessageDialog(null, "Error: uno o más campos no contienen un número válido");
            }

        }
    }//GEN-LAST:event_txtefectivoKeyPressed

    private void txttarjetaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txttarjetaActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_txttarjetaActionPerformed

    private void txttarjetaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txttarjetaKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            NumberFormat formatoMiles = NumberFormat.getNumberInstance(Locale.US);
            try {
                // Obtener los valores de los campos de texto y convertirlos a enteros
                int efectivo = Integer.parseInt(txtefectivo.getText().replace(",", ""));
                int tarjeta = Integer.parseInt(txttarjeta.getText().replace(",", ""));
                int transferencia = Integer.parseInt(txttransferencia.getText().replace(",", ""));

                int totalPago = efectivo + tarjeta + transferencia;
                txttotal.setText(formatoMiles.format(totalPago));

            } catch (NumberFormatException e) {
                // Manejar la excepción si alguno de los campos de texto no contiene un número válido
                JOptionPane.showMessageDialog(null, "Error: uno o más campos no contienen un número válido");
            }

        }
    }//GEN-LAST:event_txttarjetaKeyPressed

    private void txttransferenciaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txttransferenciaKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            NumberFormat formatoMiles = NumberFormat.getNumberInstance(Locale.US);
            try {
                // Obtener los valores de los campos de texto y convertirlos a enteros
                int efectivo = Integer.parseInt(txtefectivo.getText().replace(",", ""));
                int tarjeta = Integer.parseInt(txttarjeta.getText().replace(",", ""));
                int transferencia = Integer.parseInt(txttransferencia.getText().replace(",", ""));

                int totalPago = efectivo + tarjeta + transferencia;
                txttotal.setText(formatoMiles.format(totalPago));

            } catch (NumberFormatException e) {
                // Manejar la excepción si alguno de los campos de texto no contiene un número válido
                JOptionPane.showMessageDialog(null, "Error: uno o más campos no contienen un número válido");
            }

        }
    }//GEN-LAST:event_txttransferenciaKeyPressed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        int confirmacion = JOptionPane.showConfirmDialog(rootPane, "¿Deseas imprimir?", "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            SalidaService imprimir = new SalidaService();
            try {
                imprimir.ImprimirFacturaPago();
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(rootPane, "Error: Archivo no encontrado.\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IOException | PrinterException ex) {
                Logger.getLogger(Jsalidap.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void cboplacaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboplacaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboplacaActionPerformed

    private void cboplacaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cboplacaKeyPressed
        // TODO add your handling code here:


    }//GEN-LAST:event_cboplacaKeyPressed

    private void cbotiposervicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbotiposervicioActionPerformed
        // TODO add your handling code here:
        cbotiposervicio.transferFocus();
    }//GEN-LAST:event_cbotiposervicioActionPerformed

    private void cbotipovehiculoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbotipovehiculoActionPerformed
        // TODO add your handling code here:
        cbotipovehiculo.transferFocus();
    }//GEN-LAST:event_cbotipovehiculoActionPerformed

    private void txtdescuentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtdescuentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtdescuentoActionPerformed

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
            java.util.logging.Logger.getLogger(Jsalidap.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Jsalidap().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnguardar;
    private javax.swing.JButton btnguardar1;
    private javax.swing.JComboBox<String> cboplaca;
    private javax.swing.JComboBox<String> cbotiposervicio;
    private javax.swing.JComboBox<String> cbotipovehiculo;
    private javax.swing.JButton jButton1;
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
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSeparator jSeparator2;
    public static javax.swing.JLabel lblturno;
    private javax.swing.JTextField txtDias;
    private javax.swing.JTextField txtHoras;
    private javax.swing.JTextField txtcliente;
    private javax.swing.JTextField txtdescuento;
    private javax.swing.JTextField txtdias;
    private javax.swing.JTextField txtefectivo;
    private javax.swing.JTextField txtempleadoentrada;
    public static javax.swing.JTextField txtempleadosalida;
    private javax.swing.JTextField txtfechaentrada;
    private javax.swing.JTextField txtfechasalida;
    private javax.swing.JTextField txthoras;
    private javax.swing.JTextField txtidcliente;
    private javax.swing.JTextField txtidingreso;
    private javax.swing.JTextField txtminutos;
    private javax.swing.JTextField txtnumero_recibo;
    private javax.swing.JTextField txtnumerofactura;
    private javax.swing.JTextField txtsubtotal;
    private javax.swing.JTextField txttarjeta;
    private javax.swing.JTextField txttotal;
    private javax.swing.JTextField txttransferencia;
    private javax.swing.JTextField txtturnoentrada;
    public static javax.swing.JTextField txtturnosalida;
    private javax.swing.JTextField txtvalor;
    private javax.swing.JTextField txtzona;
    // End of variables declaration//GEN-END:variables
}
