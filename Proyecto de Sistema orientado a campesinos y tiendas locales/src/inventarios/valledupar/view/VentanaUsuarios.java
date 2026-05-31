package inventarios.valledupar.view;

import inventarios.valledupar.model.Usuario;
import inventarios.valledupar.service.UsuarioService;
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

public class VentanaUsuarios {

    private UsuarioService usuarioService;
    private TableView<Usuario> tabla;

    public VentanaUsuarios() {
        usuarioService = new UsuarioService();
    }

    public void mostrarEnPanel(StackPane panel) {
        tabla = new TableView<>();
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Usuario, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(d ->
            new SimpleIntegerProperty(d.getValue().getIdUsuario()).asObject());

        TableColumn<Usuario, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue().getNombre()));

        TableColumn<Usuario, String> colCorreo = new TableColumn<>("Correo");
        colCorreo.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue().getCorreo()));

        TableColumn<Usuario, String> colRol = new TableColumn<>("Rol");
        colRol.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue().getRol()));

        TableColumn<Usuario, String> colEstado = new TableColumn<>("Estado");
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

        tabla.getColumns().addAll(colId, colNombre, colCorreo, colRol, colEstado);

        Button btnNuevo      = new Button("+ Nuevo");
        Button btnEditar     = new Button("Editar");
        Button btnDesactivar = new Button("Desactivar");
        Button btnActivar    = new Button("Activar");
        Button btnActualizar = new Button("Actualizar");
        btnDesactivar.getStyleClass().add("btn-peligro");
        btnActivar.getStyleClass().add("btn-secundario");

        HBox barraTop = new HBox(8, btnNuevo, btnEditar,
                btnDesactivar, btnActivar, btnActualizar);
        barraTop.setPadding(new Insets(8));
        barraTop.setAlignment(Pos.CENTER_LEFT);

        VBox root = new VBox(barraTop, tabla);
        VBox.setVgrow(tabla, Priority.ALWAYS);
        panel.getChildren().add(root);

        cargarUsuarios();

        btnActualizar.setOnAction(e -> cargarUsuarios());

        btnNuevo.setOnAction(e ->
            mostrarFormulario(null)
        );

        btnEditar.setOnAction(e -> {
            Usuario seleccionado = tabla.getSelectionModel().getSelectedItem();
            if (seleccionado == null) {
                mostrarAlerta("Selecciona un usuario primero.");
                return;
            }
            mostrarFormulario(seleccionado);
        });

        btnDesactivar.setOnAction(e -> {
            Usuario seleccionado = tabla.getSelectionModel().getSelectedItem();
            if (seleccionado == null) { mostrarAlerta("Selecciona un usuario primero."); return; }
            confirmar("¿Desactivar este usuario?", () -> {
                seleccionado.setEstado("inactivo");
                usuarioService.actualizar(seleccionado);
                cargarUsuarios();
            });
        });

        btnActivar.setOnAction(e -> {
            Usuario seleccionado = tabla.getSelectionModel().getSelectedItem();
            if (seleccionado == null) { mostrarAlerta("Selecciona un usuario primero."); return; }
            confirmar("¿Activar este usuario?", () -> {
                seleccionado.setEstado("activo");
                usuarioService.actualizar(seleccionado);
                cargarUsuarios();
            });
        });
    }

    private void mostrarFormulario(Usuario usuarioEditar) {
        Stage stage = new Stage();
        stage.setTitle(usuarioEditar == null ? "Nuevo Usuario" : "Editar Usuario");
        stage.initModality(Modality.APPLICATION_MODAL);

        TextField txtNombre     = new TextField();
        TextField txtCorreo     = new TextField();
        PasswordField txtClave  = new PasswordField();
        ComboBox<String> cmbRol = new ComboBox<>();
        cmbRol.getItems().addAll("administrador", "operador");
        cmbRol.setValue("operador");
        TextField txtTelefono   = new TextField();

        if (usuarioEditar != null) {
            txtNombre.setText(usuarioEditar.getNombre());
            txtCorreo.setText(usuarioEditar.getCorreo());
            cmbRol.setValue(usuarioEditar.getRol());
            txtTelefono.setText(usuarioEditar.getTelefono());
        }

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15));
        grid.add(new Label("Nombre:"),    0, 0); grid.add(txtNombre,   1, 0);
        grid.add(new Label("Correo:"),    0, 1); grid.add(txtCorreo,   1, 1);
        grid.add(new Label("Contraseña:"),0, 2); grid.add(txtClave,    1, 2);
        grid.add(new Label("Rol:"),       0, 3); grid.add(cmbRol,      1, 3);
        grid.add(new Label("Telefono:"),  0, 4); grid.add(txtTelefono, 1, 4);

        Button btnGuardar  = new Button("Guardar");
        Button btnCancelar = new Button("Cancelar");
        btnCancelar.getStyleClass().add("btn-peligro");
        HBox botones = new HBox(10, btnGuardar, btnCancelar);
        botones.setAlignment(Pos.CENTER);
        botones.setPadding(new Insets(10));

        btnCancelar.setOnAction(e -> stage.close());

        btnGuardar.setOnAction(e -> {
            if (txtNombre.getText().trim().isEmpty() || txtCorreo.getText().trim().isEmpty()) {
                mostrarAlerta("Nombre y correo son obligatorios.");
                return;
            }
            Usuario u = usuarioEditar != null ? usuarioEditar : new Usuario();
            u.setNombre(txtNombre.getText().trim());
            u.setCorreo(txtCorreo.getText().trim());
            if (!txtClave.getText().trim().isEmpty()) {
                u.setContrasena(txtClave.getText().trim());
            }
            u.setRol(cmbRol.getValue());
            u.setTelefono(txtTelefono.getText().trim());
            u.setEstado("activo");

            if (usuarioEditar == null) {
                usuarioService.registrar(u);
            } else {
                usuarioService.actualizar(u);
            }
            cargarUsuarios();
            stage.close();
        });

        VBox root = new VBox(grid, botones);
        stage.setScene(new Scene(root, 360, 300));
        stage.setResizable(false);
        stage.show();
    }

    private void cargarUsuarios() {
        List<Usuario> usuarios = usuarioService.listarTodos();
        tabla.setItems(FXCollections.observableArrayList(usuarios));
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