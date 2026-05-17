package inventarios.valledupar.view;

import inventarios.valledupar.model.Producto;
import inventarios.valledupar.service.InventarioService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaProductos extends JFrame {

    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;
    private JButton btnNuevo;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JButton btnActualizar;
    private JTextField txtBuscar;
    private InventarioService inventarioService;

    public VentanaProductos() {
        inventarioService = new InventarioService();
        initComponents();
        cargarProductos();
    }

    private void initComponents() {
        setTitle("Gestion de Productos");
        setSize(700, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel superior con busqueda y botones
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSuperior.add(new JLabel("Buscar:"));
        txtBuscar = new JTextField(15);
        panelSuperior.add(txtBuscar);
        btnNuevo = new JButton("+ Nuevo");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
        btnActualizar = new JButton("Actualizar");
        panelSuperior.add(btnNuevo);
        panelSuperior.add(btnEditar);
        panelSuperior.add(btnEliminar);
        panelSuperior.add(btnActualizar);
        add(panelSuperior, BorderLayout.NORTH);

        // Tabla
        String[] columnas = {"ID", "Nombre", "Categoria", "Stock", "Precio Venta", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaProductos = new JTable(modeloTabla);
        tablaProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tablaProductos), BorderLayout.CENTER);

        // Acciones
        btnActualizar.addActionListener(e -> cargarProductos());
        btnNuevo.addActionListener(e ->
            JOptionPane.showMessageDialog(this, "Formulario de nuevo producto proximamente."));
        btnEditar.addActionListener(e ->
            JOptionPane.showMessageDialog(this, "Formulario de edicion proximamente."));
        btnEliminar.addActionListener(e -> {
            int fila = tablaProductos.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona un producto primero.");
                return;
            }
            int confirmar = JOptionPane.showConfirmDialog(this,
                    "¿Eliminar este producto?", "Confirmar",
                    JOptionPane.YES_NO_OPTION);
            if (confirmar == JOptionPane.YES_OPTION) {
                int id = (int) modeloTabla.getValueAt(fila, 0);
                inventarioService.eliminarProducto(id);
                cargarProductos();
            }
        });
    }

    private void cargarProductos() {
        modeloTabla.setRowCount(0);
        List<Producto> productos = inventarioService.listarProductos();
        for (Producto p : productos) {
            modeloTabla.addRow(new Object[]{
                p.getIdProducto(),
                p.getNombreProducto(),
                p.getIdCategoria(),
                p.getStockActual(),
                p.getPrecioVenta(),
                p.getEstado()
            });
        }
    }
}