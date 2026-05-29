package inventarios.valledupar.view;

import inventarios.valledupar.model.Movimiento;
import inventarios.valledupar.model.Producto;
import inventarios.valledupar.service.InventarioService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.sql.Date;
import java.util.List;

public class FormMovimiento {

    private int idUsuario;
    private Runnable onGuardar;
    private InventarioService inventarioService;
    private List<Producto> productosActivos;

    public FormMovimiento(int idUsuario, Runnable onGuardar) {
        this.idUsuario = idUsuario;
        this.onGuardar = onGuardar;
        this.inventarioService = new InventarioService();
    }

    public void mostrar() {
        Stage stage = new Stage();
        stage.setTitle("Registrar Movimiento");
        stage.initModality(Modality.APPLICATION_MODAL);

        productosActivos = inventarioService.listarProductos();
        ComboBox<String> cmbProducto = new ComboBox<>();
        for (Producto p : productosActivos) {
            cmbProducto.getItems().add(p.getIdProducto() + " - " + p.getNombreProducto());
        }
        cmbProducto.setPrefWidth(220);

        ComboBox<String> cmbTipo = new ComboBox<>();
        cmbTipo.getItems().addAll("entrada", "salida", "ajuste");
        cmbTipo.setValue("entrada");
        cmbTipo.setPrefWidth(220);

        TextField txtCantidad    = new TextField();
        TextField txtObservacion = new TextField();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15));
        grid.add(new Label("Producto:"),    0, 0); grid.add(cmbProducto,    1, 0);
        grid.add(new Label("Tipo:"),        0, 1); grid.add(cmbTipo,        1, 1);
        grid.add(new Label("Cantidad:"),    0, 2); grid.add(txtCantidad,    1, 2);
        grid.add(new Label("Observacion:"), 0, 3); grid.add(txtObservacion, 1, 3);

        Button btnGuardar  = new Button("Guardar");
        Button btnCancelar = new Button("Cancelar");
        btnGuardar.setPrefWidth(100);
        btnCancelar.setPrefWidth(100);
        HBox botones = new HBox(10, btnGuardar, btnCancelar);
        botones.setAlignment(Pos.CENTER);
        botones.setPadding(new Insets(10));

        btnCancelar.setOnAction(e -> stage.close());

        btnGuardar.setOnAction(e -> {
            try {
                int index = cmbProducto.getSelectionModel().getSelectedIndex();
                if (index == -1) {
                    mostrarAlerta("Selecciona un producto.");
                    return;
                }

                int idProducto = productosActivos.get(index).getIdProducto();
                Producto producto = inventarioService.buscarProducto(idProducto);

                int cantidad = Integer.parseInt(txtCantidad.getText().trim());
                String tipo = cmbTipo.getValue();
                String observacion = txtObservacion.getText().trim();

                if ("salida".equalsIgnoreCase(tipo) && cantidad > producto.getStockActual()) {
                    mostrarAlerta("Stock insuficiente. Stock actual: " + producto.getStockActual());
                    return;
                }

                Movimiento movimiento = new Movimiento();
                movimiento.setIdProducto(producto.getIdProducto());
                movimiento.setIdUsuario(idUsuario);
                movimiento.setTipoMovimiento(tipo);
                movimiento.setCantidad(cantidad);
                movimiento.setObservacion(observacion);
                movimiento.setFechaMovimiento(new Date(System.currentTimeMillis()));

                inventarioService.registrarMovimiento(movimiento, producto);
                if (onGuardar != null) onGuardar.run();
                stage.close();

            } catch (NumberFormatException ex) {
                mostrarAlerta("Verifica que la cantidad sea un numero.");
            }
        });

        VBox root = new VBox(grid, botones);
        stage.setScene(new Scene(root, 400, 300));
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