package inventarios.valledupar.view;

import inventarios.valledupar.model.Producto;
import inventarios.valledupar.service.InventarioService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FormProducto {

    private Producto productoEditar;
    private Runnable onGuardar;
    private InventarioService inventarioService;

    public FormProducto(Producto producto, Runnable onGuardar) {
        this.productoEditar = producto;
        this.onGuardar = onGuardar;
        this.inventarioService = new InventarioService();
    }

    public void mostrar() {
        Stage stage = new Stage();
        stage.setTitle(productoEditar == null ? "Nuevo Producto" : "Editar Producto");
        stage.initModality(Modality.APPLICATION_MODAL);

        String[] etiquetas = {"Nombre:", "Descripcion:", "Precio Compra:", "Precio Venta:",
                              "Stock Actual:", "Stock Minimo:", "Unidad Medida:",
                              "ID Categoria:", "ID Proveedor:"};
        TextField[] campos = new TextField[etiquetas.length];

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(8);
        grid.setPadding(new Insets(15));

        for (int i = 0; i < etiquetas.length; i++) {
            grid.add(new Label(etiquetas[i]), 0, i);
            campos[i] = new TextField();
            campos[i].setPrefWidth(200);
            grid.add(campos[i], 1, i);
        }

        TextField txtNombre       = campos[0];
        TextField txtDescripcion  = campos[1];
        TextField txtPrecioCompra = campos[2];
        TextField txtPrecioVenta  = campos[3];
        TextField txtStockActual  = campos[4];
        TextField txtStockMinimo  = campos[5];
        TextField txtUnidadMedida = campos[6];
        TextField txtIdCategoria  = campos[7];
        TextField txtIdProveedor  = campos[8];

        // Cargar datos si es edicion
        if (productoEditar != null) {
            txtNombre.setText(productoEditar.getNombreProducto());
            txtDescripcion.setText(productoEditar.getDescripcion());
            txtPrecioCompra.setText(String.valueOf(productoEditar.getPrecioCompra()));
            txtPrecioVenta.setText(String.valueOf(productoEditar.getPrecioVenta()));
            txtStockActual.setText(String.valueOf(productoEditar.getStockActual()));
            txtStockMinimo.setText(String.valueOf(productoEditar.getStockMinimo()));
            txtUnidadMedida.setText(productoEditar.getUnidadMedida());
            txtIdCategoria.setText(String.valueOf(productoEditar.getIdCategoria()));
            txtIdProveedor.setText(String.valueOf(productoEditar.getIdProveedor()));
        }

        Button btnGuardar  = new Button("Guardar");
        Button btnCancelar = new Button("Cancelar");
        HBox botones = new HBox(10, btnGuardar, btnCancelar);
        botones.setAlignment(Pos.CENTER);
        botones.setPadding(new Insets(10));

        btnCancelar.setOnAction(e -> stage.close());

        btnGuardar.setOnAction(e -> {
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
                } else {
                    inventarioService.actualizarProducto(p);
                }

                if (onGuardar != null) onGuardar.run();
                stage.close();

            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Verifica que los campos numericos sean correctos.");
                alert.showAndWait();
            }
        });

        VBox root = new VBox(grid, botones);
        stage.setScene(new Scene(root, 400, 450));
        stage.setResizable(false);
        stage.show();
    }
}