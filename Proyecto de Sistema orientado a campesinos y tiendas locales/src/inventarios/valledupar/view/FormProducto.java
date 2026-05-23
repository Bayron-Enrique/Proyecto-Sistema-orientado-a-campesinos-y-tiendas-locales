package inventarios.valledupar.view;

import inventarios.valledupar.model.Producto;
import inventarios.valledupar.service.InventarioService;
import javax.swing.*;
import java.awt.*;

public class FormProducto extends JDialog {

    private JTextField txtNombre;
    private JTextField txtDescripcion;
    private JTextField txtPrecioCompra;
    private JTextField txtPrecioVenta;
    private JTextField txtStockActual;
    private JTextField txtStockMinimo;
    private JTextField txtUnidadMedida;
    private JTextField txtIdCategoria;
    private JTextField txtIdProveedor;
    private JButton btnGuardar;
    private JButton btnCancelar;
    private InventarioService inventarioService;
    private Producto productoEditar;

    public FormProducto(JFrame parent, Producto producto) {
        super(parent, producto == null ? "Nuevo Producto" : "Editar Producto", true);
        this.productoEditar = producto;
        inventarioService = new InventarioService();
        initComponents();
        if (producto != null) cargarDatos(producto);
    }

    private void initComponents() {
        setSize(400, 450);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        String[] labels = {"Nombre:", "Descripcion:", "Precio Compra:", "Precio Venta:",
                           "Stock Actual:", "Stock Minimo:", "Unidad Medida:",
                           "ID Categoria:", "ID Proveedor:"};
        JTextField[] fields = new JTextField[9];
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i;
            add(new JLabel(labels[i]), gbc);
            fields[i] = new JTextField(20);
            gbc.gridx = 1;
            add(fields[i], gbc);
        }

        txtNombre       = fields[0];
        txtDescripcion  = fields[1];
        txtPrecioCompra = fields[2];
        txtPrecioVenta  = fields[3];
        txtStockActual  = fields[4];
        txtStockMinimo  = fields[5];
        txtUnidadMedida = fields[6];
        txtIdCategoria  = fields[7];
        txtIdProveedor  = fields[8];

        JPanel panelBotones = new JPanel(new FlowLayout());
        btnGuardar  = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        gbc.gridx = 0; gbc.gridy = labels.length; gbc.gridwidth = 2;
        add(panelBotones, gbc);

        btnCancelar.addActionListener(e -> dispose());
        btnGuardar.addActionListener(e -> guardar());
    }

    private void guardar() {
        try {
            Producto p = productoEditar != null ? productoEditar : new Producto();
            p.setNombreProducto(txtNombre.getText().trim());
            p.setDescripcion(txtDescripcion.getText().trim());
            p.setPrecioCompra(Double.parseDouble(txtPrecioCompra.getText().trim()));
            p.setPrecioVenta(Double.parseDouble(txtPrecioVenta.getText().trim()));
            p.setStockActual(Integer.parseInt(txtStockActual.getText().trim()));
            p.setStockMinimo(Integer.parseInt(txtStockMinimo.getText().trim()));
            p.setUnidadMedida(txtUnidadMedida.getText().trim());
            p.setIdCategoria(Integer.parseInt(txtIdCategoria.getText().trim()));
            p.setIdProveedor(Integer.parseInt(txtIdProveedor.getText().trim()));
            p.setEstado("activo");

            if (productoEditar == null) {
                inventarioService.registrarProducto(p);
                JOptionPane.showMessageDialog(this, "Producto guardado correctamente.");
            } else {
                inventarioService.actualizarProducto(p);
                JOptionPane.showMessageDialog(this, "Producto actualizado correctamente.");
            }
            dispose();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Verifica que los campos numericos sean correctos.");
        }
    }

    private void cargarDatos(Producto p) {
        txtNombre.setText(p.getNombreProducto());
        txtDescripcion.setText(p.getDescripcion());
        txtPrecioCompra.setText(String.valueOf(p.getPrecioCompra()));
        txtPrecioVenta.setText(String.valueOf(p.getPrecioVenta()));
        txtStockActual.setText(String.valueOf(p.getStockActual()));
        txtStockMinimo.setText(String.valueOf(p.getStockMinimo()));
        txtUnidadMedida.setText(p.getUnidadMedida());
        txtIdCategoria.setText(String.valueOf(p.getIdCategoria()));
        txtIdProveedor.setText(String.valueOf(p.getIdProveedor()));
    }
}