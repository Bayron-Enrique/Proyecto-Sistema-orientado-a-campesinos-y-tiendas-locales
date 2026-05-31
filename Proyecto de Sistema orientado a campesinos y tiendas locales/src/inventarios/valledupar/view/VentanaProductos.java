package inventarios.valledupar.view;

import inventarios.valledupar.model.Producto;
import inventarios.valledupar.service.InventarioService;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import java.util.List;

public class VentanaProductos {

    private InventarioService inventarioService;
    private TableView<Producto> tabla;

    public VentanaProductos() {
        inventarioService = new InventarioService();
    }

    public void mostrarEnPanel(StackPane panel) {
        tabla = new TableView<>();
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Producto, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("idProducto"));

        TableColumn<Producto, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));

        TableColumn<Producto, Integer> colCategoria = new TableColumn<>("Categoria");
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("idCategoria"));

        TableColumn<Producto, Integer> colStock = new TableColumn<>("Stock");
        colStock.setCellValueFactory(new PropertyValueFactory<>("stockActual"));

        TableColumn<Producto, Double> colPrecio = new TableColumn<>("Precio Venta");
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precioVenta"));

        TableColumn<Producto, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        colEstado.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String estado, boolean empty) {
                super.updateItem(estado, empty);
                if (empty || estado == null) {
                    setText(null); setStyle("");
                } else {
                    setText(estado);
                    if ("inactivo".equalsIgnoreCase(estado)) {
                        setStyle("-fx-background-color: #ffb3b3; -fx-text-fill: #c62828;");
                    } else {
                        setStyle("-fx-background-color: #b3ffb3; -fx-text-fill: #1b5e20;");
                    }
                }
            }
        });

        tabla.getColumns().addAll(colId, colNombre, colCategoria, colStock, colPrecio, colEstado);

        Label lblBuscar      = new Label("Buscar:");
        TextField txtBuscar  = new TextField();
        txtBuscar.setPrefWidth(150);
        Button btnNuevo      = new Button("+ Nuevo");
        Button btnEditar     = new Button("Editar");
        Button btnDesactivar = new Button("Desactivar");
        Button btnActivar    = new Button("Activar");
        Button btnEliminar   = new Button("Eliminar");
        Button btnActualizar = new Button("Actualizar");

        btnDesactivar.getStyleClass().add("btn-peligro");
        btnEliminar.getStyleClass().add("btn-peligro");
        btnActivar.getStyleClass().add("btn-secundario");

        HBox barraTop = new HBox(8, lblBuscar, txtBuscar, btnNuevo, btnEditar,
                btnDesactivar, btnActivar, btnEliminar, btnActualizar);
        barraTop.setPadding(new Insets(8));
        barraTop.setAlignment(Pos.CENTER_LEFT);

        VBox root = new VBox(barraTop, tabla);
        VBox.setVgrow(tabla, Priority.ALWAYS);
        panel.getChildren().add(root);

        cargarProductos();

        btnActualizar.setOnAction(e -> cargarProductos());

        btnNuevo.setOnAction(e ->
            new FormProducto(null, () -> cargarProductos()).mostrar()
        );

        btnEditar.setOnAction(e -> {
            Producto seleccionado = tabla.getSelectionModel().getSelectedItem();
            if (seleccionado == null) { mostrarAlerta("Selecciona un producto primero."); return; }
            new FormProducto(seleccionado, () -> cargarProductos()).mostrar();
        });

        btnDesactivar.setOnAction(e -> {
            Producto seleccionado = tabla.getSelectionModel().getSelectedItem();
            if (seleccionado == null) { mostrarAlerta("Selecciona un producto primero."); return; }
            confirmar("¿Desactivar este producto?", () -> {
                seleccionado.setEstado("inactivo");
                inventarioService.actualizarProducto(seleccionado);
                cargarProductos();
            });
        });

        btnActivar.setOnAction(e -> {
            Producto seleccionado = tabla.getSelectionModel().getSelectedItem();
            if (seleccionado == null) { mostrarAlerta("Selecciona un producto primero."); return; }
            confirmar("¿Activar este producto?", () -> {
                seleccionado.setEstado("activo");
                inventarioService.actualizarProducto(seleccionado);
                cargarProductos();
            });
        });

        btnEliminar.setOnAction(e -> {
            Producto seleccionado = tabla.getSelectionModel().getSelectedItem();
            if (seleccionado == null) { mostrarAlerta("Selecciona un producto primero."); return; }
            if ("activo".equalsIgnoreCase(seleccionado.getEstado())) {
                mostrarAlerta("Desactiva el producto antes de eliminarlo.");
                return;
            }
            confirmar("¿Eliminar permanentemente este producto? Esta accion no se puede deshacer.", () -> {
                inventarioService.eliminarProducto(seleccionado.getIdProducto());
                cargarProductos();
            });
        });

        // Buscar en tiempo real
        txtBuscar.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.trim().isEmpty()) {
                cargarProductos();
            } else {
                List<Producto> todos = inventarioService.listarProductos();
                List<Producto> filtrados = todos.stream()
                    .filter(p -> p.getNombreProducto().toLowerCase()
                        .contains(newVal.toLowerCase()))
                    .collect(java.util.stream.Collectors.toList());
                tabla.setItems(FXCollections.observableArrayList(filtrados));
            }
        });
    }

    private void cargarProductos() {
        List<Producto> productos = inventarioService.listarProductos();
        tabla.setItems(FXCollections.observableArrayList(productos));
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void confirmar(String mensaje, Runnable accion) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setHeaderText(null);
        confirm.setContentText(mensaje);
        confirm.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) accion.run();
        });
    }
}