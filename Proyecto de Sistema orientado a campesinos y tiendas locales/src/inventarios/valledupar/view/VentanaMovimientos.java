package inventarios.valledupar.view;

import inventarios.valledupar.model.Movimiento;
import inventarios.valledupar.model.Producto;
import inventarios.valledupar.model.Usuario;
import inventarios.valledupar.service.InventarioService;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.List;

public class VentanaMovimientos {

    private InventarioService inventarioService;
    private Usuario usuarioActivo;
    private TableView<Movimiento> tabla;

    public VentanaMovimientos(Usuario usuarioActivo) {
        this.usuarioActivo = usuarioActivo;
        inventarioService = new InventarioService();
    }

    public void mostrarEnPanel(StackPane panel) {
        tabla = new TableView<>();
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Movimiento, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(d ->
            new SimpleIntegerProperty(d.getValue().getIdMovimiento()).asObject());

        TableColumn<Movimiento, String> colProducto = new TableColumn<>("Producto");
        colProducto.setCellValueFactory(d -> {
            Producto p = inventarioService.buscarProducto(d.getValue().getIdProducto());
            String nombre = p != null ? p.getNombreProducto() : "ID: " + d.getValue().getIdProducto();
            return new SimpleStringProperty(nombre);
        });

        TableColumn<Movimiento, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue().getTipoMovimiento()));

        // Color por tipo
        colTipo.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String tipo, boolean empty) {
                super.updateItem(tipo, empty);
                if (empty || tipo == null) {
                    setText(null); setStyle("");
                } else {
                    setText(tipo);
                    if ("salida".equalsIgnoreCase(tipo)) {
                        setStyle("-fx-text-fill: #c62828; -fx-font-weight: bold;");
                    } else if ("entrada".equalsIgnoreCase(tipo)) {
                        setStyle("-fx-text-fill: #1b5e20; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: #e65100; -fx-font-weight: bold;");
                    }
                }
            }
        });

        TableColumn<Movimiento, Integer> colCantidad = new TableColumn<>("Cantidad");
        colCantidad.setCellValueFactory(d ->
            new SimpleIntegerProperty(d.getValue().getCantidad()).asObject());

        TableColumn<Movimiento, String> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(d -> new SimpleStringProperty(
            d.getValue().getFechaMovimiento() != null
                ? d.getValue().getFechaMovimiento().toString() : ""));

        TableColumn<Movimiento, Integer> colStock = new TableColumn<>("Stock Result.");
        colStock.setCellValueFactory(d ->
            new SimpleIntegerProperty(d.getValue().getStockResultante()).asObject());

        // NUEVO: columna observacion con tooltip para leer texto largo
        TableColumn<Movimiento, String> colObservacion = new TableColumn<>("Observacion");
        colObservacion.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue().getObservacion() != null
                ? d.getValue().getObservacion() : ""));
        colObservacion.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String obs, boolean empty) {
                super.updateItem(obs, empty);
                if (empty || obs == null || obs.isEmpty()) {
                    setText(null);
                    setTooltip(null);
                } else {
                    // Mostrar texto truncado en celda
                    setText(obs.length() > 25 ? obs.substring(0, 25) + "..." : obs);
                    // Tooltip muestra el texto completo al pasar el mouse
                    Tooltip tip = new Tooltip(obs);
                    tip.setWrapText(true);
                    tip.setMaxWidth(300);
                    setTooltip(tip);
                }
            }
        });

        tabla.getColumns().addAll(colId, colProducto, colTipo, colCantidad,
                colFecha, colStock, colObservacion);

        Label lblDesde   = new Label("Desde:");
        TextField txtDesde = new TextField();
        txtDesde.setPrefWidth(100);
        txtDesde.setPromptText("yyyy-MM-dd");
        Label lblHasta   = new Label("Hasta:");
        TextField txtHasta = new TextField();
        txtHasta.setPrefWidth(100);
        txtHasta.setPromptText("yyyy-MM-dd");
        Button btnFiltrar    = new Button("Filtrar");
        Button btnRegistrar  = new Button("+ Registrar");
        Button btnActualizar = new Button("Actualizar");

        HBox barraTop = new HBox(8, lblDesde, txtDesde, lblHasta, txtHasta,
                btnFiltrar, btnRegistrar, btnActualizar);
        barraTop.setPadding(new Insets(8));
        barraTop.setAlignment(Pos.CENTER_LEFT);

        VBox root = new VBox(barraTop, tabla);
        VBox.setVgrow(tabla, Priority.ALWAYS);
        panel.getChildren().add(root);

        cargarMovimientos();

        btnActualizar.setOnAction(e -> cargarMovimientos());

        btnRegistrar.setOnAction(e ->
            new FormMovimiento(usuarioActivo.getIdUsuario(), () -> cargarMovimientos()).mostrar()
        );

        btnFiltrar.setOnAction(e -> {
            String inicio = txtDesde.getText().trim();
            String fin    = txtHasta.getText().trim();
            if (inicio.isEmpty() || fin.isEmpty()) {
                new Alert(Alert.AlertType.WARNING,
                    "Ingresa las dos fechas para filtrar.").showAndWait();
                return;
            }
            List<Movimiento> filtrados =
                inventarioService.listarMovimientosPorPeriodo(inicio, fin);
            tabla.setItems(FXCollections.observableArrayList(filtrados));
        });
    }

    private void cargarMovimientos() {
        List<Movimiento> movimientos = inventarioService.listarMovimientos();
        tabla.setItems(FXCollections.observableArrayList(movimientos));
    }
}