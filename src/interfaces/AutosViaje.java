/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * AutosViaje.java
 *
 * Created on 16-oct-2017, 10:42:01
 */
package interfaces;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Invitado_PC16
 */
public class AutosViaje extends javax.swing.JInternalFrame {

    /**
     * Creates new form AutosViaje
     */
    DefaultTableModel model;

    public AutosViaje() {
        initComponents();
        botonesInicio();
        txtBloqueo(false);

        txtConfiguracion();
        cargarTablaAutos("");
        tblAutos.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override //ir cambiando los valores si se selecciona una fila
            public void valueChanged(ListSelectionEvent e) {

                if (tblAutos.getSelectedRow() != -1) {
                    int fila = tblAutos.getSelectedRow();
                    txtPlaca.setText(String.valueOf(tblAutos.getValueAt(fila, 0)).trim());
                    txtMarca.setText(String.valueOf(tblAutos.getValueAt(fila, 1)).trim());
                    txtModelo.setText(String.valueOf(tblAutos.getValueAt(fila, 2)).trim());
                    txtColor.setText(String.valueOf(tblAutos.getValueAt(fila, 3)).trim());
                    txtAnio.setText(String.valueOf(tblAutos.getValueAt(fila, 4)).trim());
                    txtDescripcion.setText(String.valueOf(tblAutos.getValueAt(fila, 5)).trim());
                    txtBloqueo(true);
                    txtPlaca.setEnabled(false);
                    botonesActualizar();
                }
            }
        });

        model.addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    int columna = e.getColumn();
                    int fila = e.getLastRow();
                    String nombreColumna = null;

                    if (columna == 1) {
                        nombreColumna = "AUT_MARCA";
                    } else if (columna == 2) {
                        nombreColumna = "AUT_MODELO";
                    } else if (columna == 3) {
                        nombreColumna = "AUT_COLOR";
                    } else if (columna == 4) {
                        nombreColumna = "AUT_ANIO";
                    } else if (columna == 5) {
                        nombreColumna = "AUT_DESCRIPCION";
                    }
                    String sql = "update auto set " + nombreColumna + "='" + tblAutos.getValueAt(fila, columna) + "'where AUT_PLACA='" + tblAutos.getValueAt(fila, 0) + "'";
                    conexionViaje cc = new conexionViaje();
                    Connection cn = cc.conectar();
                    PreparedStatement pst;
                    try {
                        pst = cn.prepareStatement(sql);
                        pst.executeUpdate();;
                    } catch (SQLException ex) {
                    }

                }


            }
        });
    }
    //Controlar con decimales el año
    /*
     * Consultar tipos de statement controlar campos de solo texto o solo
     * numeros
     *
     *
     */

    public void cargarTablaAutos(String Dato) {

        String[] titulos = {"PLACA", "MARCA", "MODELO", "COLOR", "AÑO", "DESCRIPCION"};
        String[] registros = new String[6];
        int estado;
        model = new DefaultTableModel(null, titulos) {

            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0) {
                    return false;
                }
                return true;
            }
        };
        conexionViaje cc = new conexionViaje();
        Connection cn = cc.conectar();
        String sql = "";
        sql = "select * from auto where AUT_PLACA like '%" + Dato + "%' order by AUT_PLACA";
        try {
            Statement psd = cn.createStatement();
            ResultSet rs = psd.executeQuery(sql); //Manejar celda por celda el resultado del statement (consulta)
            while (rs.next()) {
                registros[0] = rs.getString("AUT_PLACA");
                registros[1] = rs.getString("AUT_MARCA");
                registros[2] = rs.getString("AUT_MODELO");
                registros[3] = rs.getString("AUT_COLOR");
                registros[4] = rs.getString("AUT_ANIO");
                registros[5] = rs.getString("AUT_DESCRIPCION");
                estado = Integer.valueOf(rs.getString("AUT_ESTADO"));
                if (estado == 1) {
                    model.addRow(registros);
                }
            }
            tblAutos.setModel(model);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

    }

    public void txtConfiguracion() {
        txtColor.tipoDatoYLongitud(10, true);
        txtAnio.longitudTexto(4);
    }

    public void txtLimpiar() {
        txtPlaca.setText("");
        txtAnio.setText("2000");
        txtColor.setText("");
        txtMarca.setText("");
        txtModelo.setText("");
        txtDescripcion.setText("");
        txtBloqueo(false);

    }

    public void txtBloqueo(boolean tutia) {
        txtPlaca.requestFocus();
        txtAnio.setEnabled(tutia);
        txtColor.setEnabled(tutia);
        txtDescripcion.setEnabled(tutia);
        txtMarca.setEnabled(tutia);
        txtModelo.setEnabled(tutia);
        txtPlaca.setEnabled(tutia);

    }

    public void botonesNuevo() {
        jButton_Actualizar_Auto.setEnabled(false);
        jButton_Borrar_Auto.setEnabled(false);
        jButton_Cancelar_Auto.setEnabled(true);
        jButton_Guardar_Auto.setEnabled(true);
        jButton_Nuevo_Auto.setEnabled(false);
        jButton_Salir_Auto.setEnabled(true);
    }

    public void botonesInicio() {
        jButton_Actualizar_Auto.setEnabled(false);
        jButton_Borrar_Auto.setEnabled(false);
        jButton_Cancelar_Auto.setEnabled(false);
        jButton_Guardar_Auto.setEnabled(false);
        jButton_Nuevo_Auto.setEnabled(true);
        jButton_Salir_Auto.setEnabled(true);
    }

    public void botonesActualizar() {
        jButton_Actualizar_Auto.setEnabled(true);
        jButton_Borrar_Auto.setEnabled(true);
        jButton_Cancelar_Auto.setEnabled(true);
        jButton_Guardar_Auto.setEnabled(false);
        jButton_Nuevo_Auto.setEnabled(false);
        jButton_Salir_Auto.setEnabled(true);
    }

    public void guardar() {
        if (txtPlaca.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar la Placa");
            //Posicionar el foco en el texto
            txtPlaca.requestFocus();
        } else if (txtMarca.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar la Marca");
            txtMarca.requestFocus();
        } else if (txtModelo.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar la Modelo");
            txtModelo.requestFocus();
        } else if (txtColor.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar la Color");
            txtColor.requestFocus();
        } else if (txtAnio.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar el año");
            txtAnio.requestFocus();
        } else {
            if (txtDescripcion.getText().isEmpty()) {
                txtDescripcion.setText("Sin Descripción");
            }
            conexionViaje cc = new conexionViaje();
            Connection cn = cc.conectar();
            String AUT_PLACA, AUT_MARCA, AUT_MODELO, AUT_COLOR, AUT_ANIO, AUT_DESCRIPCION;
            AUT_PLACA = txtPlaca.getText();
            AUT_MARCA = txtMarca.getText();
            AUT_MODELO = txtModelo.getText();
            AUT_COLOR = txtColor.getText();
            AUT_ANIO = String.valueOf(txtAnio.getText());//
            AUT_DESCRIPCION = txtDescripcion.getText();
            String sql = "";
            sql = "INSERT INTO AUTO (AUT_PLACA, AUT_MARCA, AUT_MODELO, AUT_COLOR, AUT_ANIO, AUT_DESCRIPCION,AUT_ESTADO) VALUES (?,?,?,?,?,?,?)";
            try {
                PreparedStatement psd = cn.prepareStatement(sql);
                psd.setString(1, AUT_PLACA);
                psd.setString(2, AUT_MARCA);
                psd.setString(3, AUT_MODELO);
                psd.setString(4, AUT_COLOR);
                psd.setString(5, AUT_ANIO);
                psd.setString(6, AUT_DESCRIPCION);
                psd.setString(7, "1");
                int n = psd.executeUpdate();
                if (n > 0) {
                    JOptionPane.showMessageDialog(null, "Datos guardados correctamente");
                    cargarTablaAutos("");
                    txtLimpiar();
                    botonesInicio();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Ocurrió un problema con el ingreso " + ex);
            } catch (NullPointerException exp) {
                //JOptionPane.showMessageDialog(null, "Error: no existe conección a la base"+exp);
            }
        }
    }

    public void actualizar() {
        conexionViaje cc = new conexionViaje();
        Connection cn = cc.conectar();
        String sql = "";
        sql = "update auto set AUT_MARCA='" + txtMarca.getText() + "' "
                + ",AUT_MODELO=' " + txtModelo.getText() + "' "
                + ",AUT_COLOR=' " + txtColor.getText() + "' "
                + ",AUT_ANIO=' " + txtAnio.getText() + "' "
                + ",AUT_DESCRIPCION=' " + txtDescripcion.getText() + "' "
                + "where aut_placa='" + txtPlaca.getText() + "'";
        try {
            PreparedStatement psd = cn.prepareStatement(sql);
            int n = psd.executeUpdate();
            if (n > 0) {
                JOptionPane.showMessageDialog(null, "Se actualizó el registro correctamente");
                cargarTablaAutos("");
                txtLimpiar();
                botonesInicio();
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public void borrar() {
        conexionViaje cc = new conexionViaje();
        Connection cn = cc.conectar();
        String sql = "";
        //sql = "delete from auto where AUT_PLACA='" + txtPlaca.getText() + "'";
        sql = "update auto set AUT_ESTADO='" + 0 + "' where AUT_PLACA='" + txtPlaca.getText() + "'";;
        int confirm = JOptionPane.showConfirmDialog(null, "¿Está seguro que desea borrar?", "Borrar Dato", JOptionPane.YES_NO_OPTION);
        if (confirm == 0) {
            try {
                PreparedStatement psd = cn.prepareStatement(sql);
                int n = psd.executeUpdate();
                if (n > 0) {
                    JOptionPane.showMessageDialog(null, "Se borró el registro correctamente");
                    cargarTablaAutos("");
                    txtLimpiar();
                    botonesInicio();
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtMarca = new javax.swing.JTextField();
        txtModelo = new javax.swing.JTextField();
        txtDescripcion = new javax.swing.JTextField();
        txtPlaca = new javax.swing.JTextField();
        txtColor = new uctextletras.UcTextLetras();
        txtAnio = new uctextletras.ucTextNumerosSinDecimales();
        jLabel7 = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jButton_Nuevo_Auto = new javax.swing.JButton();
        jButton_Guardar_Auto = new javax.swing.JButton();
        jButton_Actualizar_Auto = new javax.swing.JButton();
        jButton_Cancelar_Auto = new javax.swing.JButton();
        jButton_Borrar_Auto = new javax.swing.JButton();
        jButton_Salir_Auto = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAutos = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMaximizable(true);
        setResizable(true);
        setTitle("AUTOS");

        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("Placa:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setText("Marca:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setText("Modelo:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("Color:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setText("Año:");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setText("Descripción:");

        jLabel7.setText("Buscar:");

        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtDescripcion, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txtModelo, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
                                        .addComponent(txtColor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(txtAnio, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(10, 10, 10)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtMarca, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
                                    .addComponent(txtPlaca)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(56, 56, 56)
                                .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(33, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtModelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtAnio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addContainerGap())
        );

        jPanel2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jButton_Nuevo_Auto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/nuevo.png"))); // NOI18N
        jButton_Nuevo_Auto.setText("Nuevo");
        jButton_Nuevo_Auto.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        jButton_Nuevo_Auto.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jButton_Nuevo_Auto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_Nuevo_AutoActionPerformed(evt);
            }
        });

        jButton_Guardar_Auto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guardar.png"))); // NOI18N
        jButton_Guardar_Auto.setText("Guardar");
        jButton_Guardar_Auto.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        jButton_Guardar_Auto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_Guardar_AutoActionPerformed(evt);
            }
        });

        jButton_Actualizar_Auto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/actualizar.png"))); // NOI18N
        jButton_Actualizar_Auto.setText("Actualizar");
        jButton_Actualizar_Auto.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        jButton_Actualizar_Auto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_Actualizar_AutoActionPerformed(evt);
            }
        });

        jButton_Cancelar_Auto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cancelar.png"))); // NOI18N
        jButton_Cancelar_Auto.setText("Cacelar");
        jButton_Cancelar_Auto.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        jButton_Cancelar_Auto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_Cancelar_AutoActionPerformed(evt);
            }
        });

        jButton_Borrar_Auto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/borrar.png"))); // NOI18N
        jButton_Borrar_Auto.setText("Borrar");
        jButton_Borrar_Auto.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        jButton_Borrar_Auto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_Borrar_AutoActionPerformed(evt);
            }
        });

        jButton_Salir_Auto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/salir.png"))); // NOI18N
        jButton_Salir_Auto.setText("Salir");
        jButton_Salir_Auto.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        jButton_Salir_Auto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_Salir_AutoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButton_Borrar_Auto, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton_Cancelar_Auto, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton_Nuevo_Auto, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton_Guardar_Auto, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton_Actualizar_Auto, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton_Salir_Auto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton_Nuevo_Auto)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton_Guardar_Auto)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton_Actualizar_Auto)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton_Cancelar_Auto)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton_Borrar_Auto)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton_Salir_Auto, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tblAutos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tblAutos);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 11, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_Guardar_AutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_Guardar_AutoActionPerformed
        // TODO add your handling code here:
        guardar();
    }//GEN-LAST:event_jButton_Guardar_AutoActionPerformed

    private void jButton_Nuevo_AutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_Nuevo_AutoActionPerformed
        // TODO add your handling code here:
        txtBloqueo(true);
        botonesNuevo();
    }//GEN-LAST:event_jButton_Nuevo_AutoActionPerformed

    private void jButton_Cancelar_AutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_Cancelar_AutoActionPerformed
        // TODO add your handling code here:
        txtLimpiar();
        botonesInicio();
    }//GEN-LAST:event_jButton_Cancelar_AutoActionPerformed

    private void jButton_Salir_AutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_Salir_AutoActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButton_Salir_AutoActionPerformed

    private void jButton_Actualizar_AutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_Actualizar_AutoActionPerformed
        // TODO add your handling code here:
        actualizar();
    }//GEN-LAST:event_jButton_Actualizar_AutoActionPerformed

    private void txtBuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarKeyReleased
        // TODO add your handling code here:
        cargarTablaAutos(txtBuscar.getText());
    }//GEN-LAST:event_txtBuscarKeyReleased

    private void jButton_Borrar_AutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_Borrar_AutoActionPerformed
        // TODO add your handling code here:
        borrar();
    }//GEN-LAST:event_jButton_Borrar_AutoActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AutosViaje.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AutosViaje.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AutosViaje.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AutosViaje.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new AutosViaje().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_Actualizar_Auto;
    private javax.swing.JButton jButton_Borrar_Auto;
    private javax.swing.JButton jButton_Cancelar_Auto;
    private javax.swing.JButton jButton_Guardar_Auto;
    private javax.swing.JButton jButton_Nuevo_Auto;
    private javax.swing.JButton jButton_Salir_Auto;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblAutos;
    private uctextletras.ucTextNumerosSinDecimales txtAnio;
    private javax.swing.JTextField txtBuscar;
    private uctextletras.UcTextLetras txtColor;
    private javax.swing.JTextField txtDescripcion;
    private javax.swing.JTextField txtMarca;
    private javax.swing.JTextField txtModelo;
    private javax.swing.JTextField txtPlaca;
    // End of variables declaration//GEN-END:variables
}
