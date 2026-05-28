package inventarios.valledupar.view;

import inventarios.valledupar.model.Movimiento;
import inventarios.valledupar.model.Producto;
import inventarios.valledupar.service.InventarioService;
import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.util.List;

public class FormMovimiento extends JDialog {

    private JComboBox<String> cmbProducto;
    private JTextField txtCantidad;
    private JTextField txtObservacion;
    private JComboBox<String> cmbTipo;
    private JButton btnGuardar;
    private JButton btnCancelar;
    private InventarioService inventarioService;
    private int idUsuario;
    private List<Producto> productosActivos;

    public FormMovimiento(JFrame parent, int idUsuario) {
        super(parent, "Registrar Movimiento", true);
        this.idUsuario = idUsuario;
        inventarioService = new InventarioService();
        initComponents();
    }

    private void initComponents() {
        setSize(400, 320);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Producto:"), gbc);
        productosActivos = inventarioService.listarProductos();
        cmbProducto = new JComboBox<>();
        for (Producto p : productosActivos) {
            cmbProducto.addItem(p.getIdProducto() + " - " + p.getNombreProducto());
        }
        gbc.gridx = 1;
        add(cmbProducto, gbc);

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
            int indexSeleccionado = cmbProducto.getSelectedIndex();
            if (indexSeleccionado == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona un producto.");
                return;
            }

            // recargar producto desde BD para tener el stock real y actualizado
            int idProducto = productosActivos.get(indexSeleccionado).getIdProducto();
            Producto producto = inventarioService.buscarProducto(idProducto);

            int cantidad = Integer.parseInt(txtCantidad.getText().trim());
            String tipo = (String) cmbTipo.getSelectedItem();
            String observacion = txtObservacion.getText().trim();

            // validar stock suficiente antes de registrar una salida
            if ("salida".equalsIgnoreCase(tipo) && cantidad > producto.getStockActual()) {
                JOptionPane.showMessageDialog(this,
                    "Stock insuficiente. Stock actual: " + producto.getStockActual());
                return;
            }

            Movimiento movimiento = new Movimiento();
            movimiento.setIdProducto(producto.getIdProducto());
            movimiento.setIdUsuario(idUsuario);
            movimiento.setTipoMovimiento(tipo);
            movimiento.setCantidad(cantidad);
            movimiento.setObservacion(observacion);
            // CORRECCIÓN: fecha como java.sql.Date, sin formato de texto
            movimiento.setFechaMovimiento(new Date(System.currentTimeMillis()));

            inventarioService.registrarMovimiento(movimiento, producto);
            JOptionPane.showMessageDialog(this, "Movimiento registrado correctamente.");
            dispose();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Verifica que la cantidad sea un numero.");
        }
    }
}