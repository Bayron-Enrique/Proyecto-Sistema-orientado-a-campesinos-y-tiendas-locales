package inventarios.valledupar.view;

import inventarios.valledupar.model.Movimiento;
import inventarios.valledupar.model.Producto;
import inventarios.valledupar.service.InventarioService;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FormMovimiento extends JDialog {

    private JTextField txtIdProducto;
    private JTextField txtCantidad;
    private JTextField txtObservacion;
    private JComboBox<String> cmbTipo;
    private JButton btnGuardar;
    private JButton btnCancelar;
    private InventarioService inventarioService;
    private int idUsuario;

    public FormMovimiento(JFrame parent, int idUsuario) {
        super(parent, "Registrar Movimiento", true);
        this.idUsuario = idUsuario;
        inventarioService = new InventarioService();
        initComponents();
    }

    private void initComponents() {
        setSize(380, 320);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("ID Producto:"), gbc);
        txtIdProducto = new JTextField(20);
        gbc.gridx = 1;
        add(txtIdProducto, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Tipo:"), gbc);
        cmbTipo = new JComboBox<>(new String[]{"entrada", "salida", "ajuste"});
        gbc.gridx = 1;
        add(cmbTipo, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Cantidad:"), gbc);
        txtCantidad = new JTextField(20);
        gbc.gridx = 1;
        add(txtCantidad, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Observacion:"), gbc);
        txtObservacion = new JTextField(20);
        gbc.gridx = 1;
        add(txtObservacion, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout());
        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        add(panelBotones, gbc);

        btnCancelar.addActionListener(e -> dispose());
        btnGuardar.addActionListener(e -> guardar());
    }

    private void guardar() {
        try {
            int idProducto = Integer.parseInt(txtIdProducto.getText().trim());
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());
            String tipo = (String) cmbTipo.getSelectedItem();
            String observacion = txtObservacion.getText().trim();

            Producto producto = inventarioService.buscarProducto(idProducto);
            if (producto == null) {
                JOptionPane.showMessageDialog(this, "Producto no encontrado.");
                return;
            }

            Movimiento movimiento = new Movimiento();
            movimiento.setIdProducto(idProducto);
            movimiento.setIdUsuario(idUsuario);
            movimiento.setTipoMovimiento(tipo);
            movimiento.setCantidad(cantidad);
            movimiento.setObservacion(observacion);
            movimiento.setFechaMovimiento(LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            inventarioService.registrarMovimiento(movimiento, producto);
            JOptionPane.showMessageDialog(this, "Movimiento registrado correctamente.");
            dispose();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Verifica que los campos numericos sean correctos.");
        }
    }
}