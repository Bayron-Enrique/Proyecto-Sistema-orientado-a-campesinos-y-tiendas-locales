package inventarios.valledupar.view;

import inventarios.valledupar.model.Movimiento;
import inventarios.valledupar.model.Producto;
import inventarios.valledupar.service.InventarioService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class FormMovimiento {

    private int idUsuario;
    private Runnable onGuardar;
    private InventarioService inventarioService;
    private List<Producto> productosActivos;

    // Lista temporal de movimientos a registrar
    private ObservableList<FilaMovimiento> filasTemp = FXCollections.observableArrayList();

    public FormMovimiento(int idUsuario, Runnable onGuardar) {
        this.idUsuario = idUsuario;
        this.onGuardar = onGuardar;
        this.inventarioService = new InventarioService();
    }

    // Clase auxiliar para mostrar filas en la tabla temporal
    public static class FilaMovimiento {
        private Producto producto;
        private String tipo;
        private int cantidad;
        private String observacion;

        public FilaMovimiento(Producto producto, String tipo, int cantidad, String observacion) {
            this.producto = producto;
            this.tipo = tipo;
            this.cantidad = cantidad;
            this.observacion = observacion;
        }
        public String getNombreProducto() { return producto.getNombreProducto(); }
        public String getTipo()           { return tipo; }
        public int getCantidad()          { return cantidad; }
        public String getObservacion()    { return observacion; }
        public Producto getProducto()     { return producto; }
    }

    public void mostrar() {
        Stage stage = new Stage();
        stage.setTitle("Registrar Movimientos");
        stage.initModality(Modality.APPLICATION_MODAL);

        productosActivos = inventarioService.listarProductos();

        // === FORMULARIO DE ENTRADA ===
        ComboBox<String> cmbProducto = new ComboBox<>();
        for (Producto p : productosActivos) {
            cmbProducto.getItems().add(p.getIdProducto() + " - " + p.getNombreProducto());
        }
        cmbProducto.setPrefWidth(230);
        cmbProducto.setPromptText("Selecciona producto...");

        ComboBox<String> cmbTipo = new ComboBox<>();
        cmbTipo.getItems().addAll("entrada", "salida", "ajuste");
        cmbTipo.setValue("entrada");
        cmbTipo.setPrefWidth(120);

        TextField txtCantidad    = new TextField();
        txtCantidad.setPrefWidth(80);
        txtCantidad.setPromptText("Cantidad");

        TextField txtObservacion = new TextField();
        txtObservacion.setPrefWidth(200);
        txtObservacion.setPromptText("Observacion (opcional)");

        Button btnAgregar = new Button("+ Agregar");
        btnAgregar.getStyleClass().add("btn-secundario");

        HBox filaEntrada = new HBox(8, cmbProducto, cmbTipo, txtCantidad,
                txtObservacion, btnAgregar);
        filaEntrada.setPadding(new Insets(10));
        filaEntrada.setAlignment(Pos.CENTER_LEFT);

        // === TABLA TEMPORAL ===
        TableView<FilaMovimiento> tablaTemp = new TableView<>(filasTemp);
        tablaTemp.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tablaTemp.setPrefHeight(200);

        TableColumn<FilaMovimiento, String> colProd = new TableColumn<>("Producto");
        colProd.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));

        TableColumn<FilaMovimiento, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));

        TableColumn<FilaMovimiento, Integer> colCant = new TableColumn<>("Cantidad");
        colCant.setCellValueFactory(new PropertyValueFactory<>("cantidad"));

        TableColumn<FilaMovimiento, String> colObs = new TableColumn<>("Observacion");
        colObs.setCellValueFactory(new PropertyValueFactory<>("observacion"));

        // Columna eliminar fila
        TableColumn<FilaMovimiento, Void> colElim = new TableColumn<>("");
        colElim.setCellFactory(col -> new TableCell<>() {
            final Button btn = new Button("✖");
            {
                btn.setStyle("-fx-background-color: #c62828; -fx-text-fill: white; " +
                             "-fx-background-radius: 4; -fx-cursor: hand;");
                btn.setOnAction(e -> filasTemp.remove(getItem() != null
                    ? getItem() : tablaTemp.getItems().get(getIndex())));
            }
            @Override
            protected void updateItem(Void v, boolean empty) {
                super.updateItem(v, empty);
                setGraphic(empty ? null : btn);
            }
        });
        colElim.setMaxWidth(40);
        colElim.setMinWidth(40);

        tablaTemp.getColumns().addAll(colProd, colTipo, colCant, colObs, colElim);

        // Label contador
        Label lblContador = new Label("0 movimientos agregados");
        lblContador.setStyle("-fx-text-fill: #2e7d32; -fx-font-weight: bold;");

        // Botones finales
        Button btnGuardarTodo = new Button("✔ Guardar todo");
        Button btnCancelar    = new Button("Cancelar");
        btnCancelar.getStyleClass().add("btn-peligro");
        HBox botones = new HBox(10, btnGuardarTodo, btnCancelar);
        botones.setAlignment(Pos.CENTER_RIGHT);
        botones.setPadding(new Insets(8, 10, 8, 10));

        // === ACCIONES ===
        btnAgregar.setOnAction(e -> {
            try {
                int index = cmbProducto.getSelectionModel().getSelectedIndex();
                if (index == -1) { mostrarAlerta("Selecciona un producto."); return; }

                int idProducto = productosActivos.get(index).getIdProducto();
                Producto producto = inventarioService.buscarProducto(idProducto);
                int cantidad = Integer.parseInt(txtCantidad.getText().trim());
                String tipo = cmbTipo.getValue();
                String obs  = txtObservacion.getText().trim();

                if ("salida".equalsIgnoreCase(tipo) && cantidad > producto.getStockActual()) {
                    mostrarAlerta("Stock insuficiente para " + producto.getNombreProducto()
                        + ". Stock actual: " + producto.getStockActual());
                    return;
                }

                filasTemp.add(new FilaMovimiento(producto, tipo, cantidad, obs));
                lblContador.setText(filasTemp.size() + " movimiento(s) agregado(s)");

                // Limpiar campos
                cmbProducto.getSelectionModel().clearSelection();
                txtCantidad.clear();
                txtObservacion.clear();
                cmbTipo.setValue("entrada");

            } catch (NumberFormatException ex) {
                mostrarAlerta("Verifica que la cantidad sea un numero.");
            }
        });

        btnGuardarTodo.setOnAction(e -> {
            if (filasTemp.isEmpty()) {
                mostrarAlerta("Agrega al menos un movimiento.");
                return;
            }
            int exitosos = 0;
            List<String> errores = new ArrayList<>();
            for (FilaMovimiento fila : filasTemp) {
                try {
                    Producto prod = inventarioService.buscarProducto(
                        fila.getProducto().getIdProducto());
                    Movimiento mov = new Movimiento();
                    mov.setIdProducto(prod.getIdProducto());
                    mov.setIdUsuario(idUsuario);
                    mov.setTipoMovimiento(fila.getTipo());
                    mov.setCantidad(fila.getCantidad());
                    mov.setObservacion(fila.getObservacion());
                    mov.setFechaMovimiento(new Date(System.currentTimeMillis()));
                    inventarioService.registrarMovimiento(mov, prod);
                    exitosos++;
                } catch (Exception ex) {
                    errores.add(fila.getNombreProducto() + ": " + ex.getMessage());
                }
            }

            if (errores.isEmpty()) {
                new Alert(Alert.AlertType.INFORMATION,
                    exitosos + " movimiento(s) registrados correctamente.").showAndWait();
            } else {
                new Alert(Alert.AlertType.WARNING,
                    exitosos + " registrados. Errores:\n" + String.join("\n", errores)).showAndWait();
            }

            if (onGuardar != null) onGuardar.run();
            stage.close();
        });

        btnCancelar.setOnAction(e -> stage.close());

        // === LAYOUT ===
        Label lblTitulo = new Label("Registrar Movimientos de Inventario");
        lblTitulo.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #1b5e20;");

        Separator sep = new Separator();

        VBox root = new VBox(10,
            new VBox(8, lblTitulo, sep, filaEntrada),
            tablaTemp,
            lblContador,
            botones
        );
        root.setPadding(new Insets(12));

        Scene scene = new Scene(root, 780, 420);
        stage.setScene(scene);
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