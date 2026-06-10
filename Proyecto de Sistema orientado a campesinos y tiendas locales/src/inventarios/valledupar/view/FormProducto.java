package inventarios.valledupar.view;

import inventarios.valledupar.model.Categoria;
import inventarios.valledupar.model.Producto;
import inventarios.valledupar.model.Proveedor;
import inventarios.valledupar.service.CategoriaService;
import inventarios.valledupar.service.InventarioService;
import inventarios.valledupar.service.ProveedorService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.List;

public class FormProducto {

    private Producto productoEditar;
    private Runnable onGuardar;
    private InventarioService inventarioService;
    private CategoriaService categoriaService;
    private ProveedorService proveedorService;

    public FormProducto(Producto producto, Runnable onGuardar) {
        this.productoEditar = producto;
        this.onGuardar = onGuardar;
        this.inventarioService = new InventarioService();
        this.categoriaService  = new CategoriaService();
        this.proveedorService  = new ProveedorService();
    }

    public void mostrar() {
        Stage stage = new Stage();
        stage.setTitle(productoEditar == null ? "Nuevo Producto" : "Editar Producto");
        stage.initModality(Modality.APPLICATION_MODAL);

        // Campos de texto
        TextField txtNombre       = new TextField();
        TextField txtDescripcion  = new TextField();
        TextField txtPrecioCompra = new TextField();
        TextField txtPrecioVenta  = new TextField();
        TextField txtStockActual  = new TextField();
        TextField txtStockMinimo  = new TextField();

        // ComboBox Unidad de medida
        ComboBox<String> cmbUnidad = new ComboBox<>();
        cmbUnidad.getItems().addAll(
            "Unidad", "Kg", "Gramo", "Libra",
            "Litro", "Mililitro", "Caja", "Paquete", "Bulto", "Saco"
        );
        cmbUnidad.setValue("Unidad");
        cmbUnidad.setPrefWidth(200);

        // Tipo de venta
        ToggleGroup grupoTipo = new ToggleGroup();
        RadioButton rbUnidad  = new RadioButton("Unidad");
        RadioButton rbPeso    = new RadioButton("Peso");
        RadioButton rbVolumen = new RadioButton("Volumen");
        rbUnidad.setToggleGroup(grupoTipo);
        rbPeso.setToggleGroup(grupoTipo);
        rbVolumen.setToggleGroup(grupoTipo);
        rbUnidad.setSelected(true);

        // Al cambiar tipo de venta, sugerir unidad correspondiente
        rbPeso.setOnAction(e -> cmbUnidad.setValue("Kg"));
        rbVolumen.setOnAction(e -> cmbUnidad.setValue("Litro"));
        rbUnidad.setOnAction(e -> cmbUnidad.setValue("Unidad"));

        HBox tipoVenta = new HBox(12, rbUnidad, rbPeso, rbVolumen);

        // ComboBox Categoria — cargado desde BD
        ComboBox<String> cmbCategoria = new ComboBox<>();
        List<Categoria> categorias = categoriaService.listarTodas();
        for (Categoria c : categorias) {
            cmbCategoria.getItems().add(c.getIdCategoria() + " - " + c.getNombre());
        }
        cmbCategoria.setPromptText("Selecciona categoria...");
        cmbCategoria.setPrefWidth(200);

        // ComboBox Proveedor — cargado desde BD
        ComboBox<String> cmbProveedor = new ComboBox<>();
        List<Proveedor> proveedores = proveedorService.listarTodos();
        for (Proveedor p : proveedores) {
            cmbProveedor.getItems().add(p.getIdProveedor() + " - " + p.getNombre());
        }
        cmbProveedor.setPromptText("Selecciona proveedor...");
        cmbProveedor.setPrefWidth(200);

        // Cargar datos si es edicion
        if (productoEditar != null) {
            txtNombre.setText(productoEditar.getNombreProducto());
            txtDescripcion.setText(productoEditar.getDescripcion());
            txtPrecioCompra.setText(String.valueOf(productoEditar.getPrecioCompra()));
            txtPrecioVenta.setText(String.valueOf(productoEditar.getPrecioVenta()));
            txtStockActual.setText(String.valueOf(productoEditar.getStockActual()));
            txtStockMinimo.setText(String.valueOf(productoEditar.getStockMinimo()));
            if (productoEditar.getUnidadMedida() != null)
                cmbUnidad.setValue(productoEditar.getUnidadMedida());

            // Preseleccionar categoria
            for (String item : cmbCategoria.getItems()) {
                if (item.startsWith(productoEditar.getIdCategoria() + " -")) {
                    cmbCategoria.setValue(item); break;
                }
            }
            // Preseleccionar proveedor
            for (String item : cmbProveedor.getItems()) {
                if (item.startsWith(productoEditar.getIdProveedor() + " -")) {
                    cmbProveedor.setValue(item); break;
                }
            }
        }

        // Grid del formulario
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15));

        int fila = 0;
        grid.add(new Label("Nombre:"),         0, fila); grid.add(txtNombre,       1, fila++);
        grid.add(new Label("Descripcion:"),    0, fila); grid.add(txtDescripcion,  1, fila++);
        grid.add(new Label("Precio Compra:"),  0, fila); grid.add(txtPrecioCompra, 1, fila++);
        grid.add(new Label("Precio Venta:"),   0, fila); grid.add(txtPrecioVenta,  1, fila++);
        grid.add(new Label("Stock Actual:"),   0, fila); grid.add(txtStockActual,  1, fila++);
        grid.add(new Label("Stock Minimo:"),   0, fila); grid.add(txtStockMinimo,  1, fila++);
        grid.add(new Label("Tipo de venta:"),  0, fila); grid.add(tipoVenta,       1, fila++);
        grid.add(new Label("Unidad Medida:"),  0, fila); grid.add(cmbUnidad,       1, fila++);
        grid.add(new Label("Categoria:"),      0, fila); grid.add(cmbCategoria,    1, fila++);
        grid.add(new Label("Proveedor:"),      0, fila); grid.add(cmbProveedor,    1, fila++);

        Button btnGuardar  = new Button("Guardar");
        Button btnCancelar = new Button("Cancelar");
        btnCancelar.getStyleClass().add("btn-peligro");
        HBox botones = new HBox(10, btnGuardar, btnCancelar);
        botones.setAlignment(Pos.CENTER);
        botones.setPadding(new Insets(10));

        btnCancelar.setOnAction(e -> stage.close());

        btnGuardar.setOnAction(e -> {
            try {
                if (txtNombre.getText().trim().isEmpty()) {
                    mostrarAlerta("El nombre es obligatorio.");
                    return;
                }
                if (cmbCategoria.getValue() == null) {
                    mostrarAlerta("Selecciona una categoria.");
                    return;
                }

                // Extraer IDs de los ComboBox
                int idCategoria = Integer.parseInt(
                    cmbCategoria.getValue().split(" - ")[0].trim());
                int idProveedor = cmbProveedor.getValue() != null
                    ? Integer.parseInt(cmbProveedor.getValue().split(" - ")[0].trim())
                    : 0;

                Producto p = productoEditar != null ? productoEditar : new Producto();
                p.setNombreProducto(txtNombre.getText().trim());
                p.setDescripcion(txtDescripcion.getText().trim());
                p.setPrecioCompra(Double.parseDouble(txtPrecioCompra.getText().trim()));
                p.setPrecioVenta(Double.parseDouble(txtPrecioVenta.getText().trim()));
                p.setStockActual(Integer.parseInt(txtStockActual.getText().trim()));
                p.setStockMinimo(Integer.parseInt(txtStockMinimo.getText().trim()));
                p.setUnidadMedida(cmbUnidad.getValue());
                p.setIdCategoria(idCategoria);
                p.setIdProveedor(idProveedor);
                p.setEstado("activo");

                if (productoEditar == null) {
                    inventarioService.registrarProducto(p);
                } else {
                    inventarioService.actualizarProducto(p);
                }

                if (onGuardar != null) onGuardar.run();
                stage.close();

            } catch (NumberFormatException ex) {
                mostrarAlerta("Verifica que los campos numericos sean correctos.");
            }
        });

        VBox root = new VBox(grid, botones);
        stage.setScene(new Scene(root, 420, 520));
        stage.setResizable(false);
        stage.show();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}