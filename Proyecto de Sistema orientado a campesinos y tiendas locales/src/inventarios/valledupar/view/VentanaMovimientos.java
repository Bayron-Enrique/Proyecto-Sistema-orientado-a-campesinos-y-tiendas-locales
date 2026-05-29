package inventarios.valledupar.view;

import inventarios.valledupar.model.Movimiento;
import inventarios.valledupar.model.Producto;
import inventarios.valledupar.model.Usuario;
import inventarios.valledupar.service.InventarioService;
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
        colId.setCellValueFactory(d -> new javafx.beans.property.SimpleIntegerProperty(d.getValue().getIdMovimiento()).asObject());

        TableColumn<Movimiento, String> colProducto = new TableColumn<>("Producto");
        colProducto.setCellValueFactory(d -> {
            Producto p = inventarioService.buscarProducto(d.getValue().getIdProducto());
            String nombre = p != null ? p.getNombreProducto() : "ID: " + d.getValue().getIdProducto();
            return new javafx.beans.property.SimpleStringProperty(nombre);
        });

        TableColumn<Movimiento, Integer> colUsuario = new TableColumn<>("Usuario");
        colUsuario.setCellValueFactory(d -> new javafx.beans.property.SimpleIntegerProperty(d.getValue().getIdUsuario()).asObject());

        TableColumn<Movimiento, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getTipoMovimiento()));

        TableColumn<Movimiento, Integer> colCantidad = new TableColumn<>("Cantidad");
        colCantidad.setCellValueFactory(d -> new javafx.beans.property.SimpleIntegerProperty(d.getValue().getCantidad()).asObject());

        TableColumn<Movimiento, String> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
                d.getValue().getFechaMovimiento() != null ? d.getValue().getFechaMovimiento().toString() : ""));

        TableColumn<Movimiento, Integer> colStock = new TableColumn<>("Stock Resultante");
        colStock.setCellValueFactory(d -> new javafx.beans.property.SimpleIntegerProperty(d.getValue().getStockResultante()).asObject());

        tabla.getColumns().addAll(colId, colProducto, colUsuario, colTipo, colCantidad, colFecha, colStock);

        // Barra superior
        Label lblDesde = new Label("Desde:");
        TextField txtDesde = new TextField();
        txtDesde.setPrefWidth(100);
        Label lblHasta = new Label("Hasta:");
        TextField txtHasta = new TextField();
        txtHasta.setPrefWidth(100);
        Button btnFiltrar    = new Button("Filtrar");
        Button btnRegistrar  = new Button("+ Registrar");
        Button btnActualizar = new Button("Actualizar");

        HBox barraTop = new HBox(8, lblDesde, txtDesde, lblHasta, txtHasta, btnFiltrar, btnRegistrar, btnActualizar);
        barraTop.setPadding(new Insets(8));
        barraTop.setAlignment(Pos.CENTER_LEFT);

        VBox root = new VBox(barraTop, tabla);
        VBox.setVgrow(tabla, Priority.ALWAYS);
        panel.getChildren().add(root);

        cargarMovimientos();

        // Acciones
        btnActualizar.setOnAction(e -> cargarMovimientos());

        btnRegistrar.setOnAction(e -> {
            new FormMovimiento(usuarioActivo.getIdUsuario(), () -> cargarMovimientos()).mostrar();
        });

        btnFiltrar.setOnAction(e -> {
            String inicio = txtDesde.getText().trim();
            String fin    = txtHasta.getText().trim();
            if (inicio.isEmpty() || fin.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Ingresa las dos fechas para filtrar.").showAndWait();
                return;
            }
            List<Movimiento> filtrados = inventarioService.listarMovimientosPorPeriodo(inicio, fin);
            tabla.setItems(FXCollections.observableArrayList(filtrados));
        });
    }

    private void cargarMovimientos() {
        List<Movimiento> movimientos = inventarioService.listarMovimientos();
        tabla.setItems(FXCollections.observableArrayList(movimientos));
    }
}