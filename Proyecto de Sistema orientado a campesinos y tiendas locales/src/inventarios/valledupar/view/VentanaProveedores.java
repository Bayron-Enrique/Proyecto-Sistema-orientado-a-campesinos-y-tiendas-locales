package inventarios.valledupar.view;

import inventarios.valledupar.model.Proveedor;
import inventarios.valledupar.service.ProveedorService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.List;

public class VentanaProveedores {

    private ProveedorService proveedorService;
    private TableView<Proveedor> tabla;

    public VentanaProveedores() {
        proveedorService = new ProveedorService();
    }

    public void mostrarEnPanel(StackPane panel) {
        tabla = new TableView<>();
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Proveedor, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(d ->
            new SimpleIntegerProperty(d.getValue().getIdProveedor()).asObject());

        TableColumn<Proveedor, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue().getNombre()));

        TableColumn<Proveedor, String> colTelefono = new TableColumn<>("Telefono");
        colTelefono.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue().getTelefono()));

        TableColumn<Proveedor, String> colCorreo = new TableColumn<>("Correo");
        colCorreo.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue().getCorreo()));

        TableColumn<Proveedor, String> colDireccion = new TableColumn<>("Direccion");
        colDireccion.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue().getDireccion()));

        TableColumn<Proveedor, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue().getEstado()));

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

        tabla.getColumns().addAll(colId, colNombre, colTelefono,
                colCorreo, colDireccion, colEstado);

        Button btnNuevo      = new Button("+ Nuevo");
        Button btnEditar     = new Button("Editar");
        Button btnEliminar   = new Button("Eliminar");
        Button btnActualizar = new Button("Actualizar");
        btnEliminar.getStyleClass().add("btn-peligro");

        HBox barraTop = new HBox(8, btnNuevo, btnEditar, btnEliminar, btnActualizar);
        barraTop.setPadding(new Insets(8));
        barraTop.setAlignment(Pos.CENTER_LEFT);

        VBox root = new VBox(barraTop, tabla);
        VBox.setVgrow(tabla, Priority.ALWAYS);
        panel.getChildren().add(root);

        cargarProveedores();

        btnActualizar.setOnAction(e -> cargarProveedores());

        btnNuevo.setOnAction(e -> mostrarFormulario(null));

        btnEditar.setOnAction(e -> {
            Proveedor sel = tabla.getSelectionModel().getSelectedItem();
            if (sel == null) { mostrarAlerta("Selecciona un proveedor primero."); return; }
            mostrarFormulario(sel);
        });

        btnEliminar.setOnAction(e -> {
            Proveedor sel = tabla.getSelectionModel().getSelectedItem();
            if (sel == null) { mostrarAlerta("Selecciona un proveedor primero."); return; }
            confirmar("¿Eliminar este proveedor permanentemente?", () -> {
                proveedorService.eliminar(sel.getIdProveedor());
                cargarProveedores();
            });
        });
    }

    private void mostrarFormulario(Proveedor proveedorEditar) {
        Stage stage = new Stage();
        stage.setTitle(proveedorEditar == null ? "Nuevo Proveedor" : "Editar Proveedor");
        stage.initModality(Modality.APPLICATION_MODAL);

        TextField txtNombre    = new TextField();
        TextField txtTelefono  = new TextField();
        TextField txtCorreo    = new TextField();
        TextField txtDireccion = new TextField();
        ComboBox<String> cmbEstado = new ComboBox<>();
        cmbEstado.getItems().addAll("activo", "inactivo");
        cmbEstado.setValue("activo");

        if (proveedorEditar != null) {
            txtNombre.setText(proveedorEditar.getNombre());
            txtTelefono.setText(proveedorEditar.getTelefono());
            txtCorreo.setText(proveedorEditar.getCorreo());
            txtDireccion.setText(proveedorEditar.getDireccion());
            cmbEstado.setValue(proveedorEditar.getEstado());
        }

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15));
        grid.add(new Label("Nombre:"),    0, 0); grid.add(txtNombre,    1, 0);
        grid.add(new Label("Telefono:"),  0, 1); grid.add(txtTelefono,  1, 1);
        grid.add(new Label("Correo:"),    0, 2); grid.add(txtCorreo,    1, 2);
        grid.add(new Label("Direccion:"), 0, 3); grid.add(txtDireccion, 1, 3);
        grid.add(new Label("Estado:"),    0, 4); grid.add(cmbEstado,    1, 4);

        Button btnGuardar  = new Button("Guardar");
        Button btnCancelar = new Button("Cancelar");
        btnCancelar.getStyleClass().add("btn-peligro");
        HBox botones = new HBox(10, btnGuardar, btnCancelar);
        botones.setAlignment(Pos.CENTER);
        botones.setPadding(new Insets(10));

        btnCancelar.setOnAction(e -> stage.close());

        btnGuardar.setOnAction(e -> {
            if (txtNombre.getText().trim().isEmpty()) {
                mostrarAlerta("El nombre es obligatorio.");
                return;
            }
            Proveedor p = proveedorEditar != null ? proveedorEditar : new Proveedor();
            p.setNombre(txtNombre.getText().trim());
            p.setTelefono(txtTelefono.getText().trim());
            p.setCorreo(txtCorreo.getText().trim());
            p.setDireccion(txtDireccion.getText().trim());
            p.setEstado(cmbEstado.getValue());

            if (proveedorEditar == null) {
                proveedorService.registrar(p);
            } else {
                proveedorService.actualizar(p);
            }
            cargarProveedores();
            stage.close();
        });

        VBox root = new VBox(grid, botones);
        stage.setScene(new Scene(root, 380, 310));
        stage.setResizable(false);
        stage.show();
    }

    private void cargarProveedores() {
        List<Proveedor> proveedores = proveedorService.listarTodos();
        tabla.setItems(FXCollections.observableArrayList(proveedores));
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