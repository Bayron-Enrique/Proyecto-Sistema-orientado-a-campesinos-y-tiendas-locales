package inventarios.valledupar.view;

import inventarios.valledupar.model.Alerta;
import inventarios.valledupar.model.Producto;
import inventarios.valledupar.service.AlertaService;
import inventarios.valledupar.service.InventarioService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.List;

public class VentanaAlertas {

    private AlertaService alertaService;
    private InventarioService inventarioService;
    private TableView<Alerta> tabla;

    public VentanaAlertas() {
        alertaService = new AlertaService();
        inventarioService = new InventarioService();
    }

    public void mostrarEnPanel(StackPane panel) {
        tabla = new TableView<>();
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Alerta, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(d ->
            new SimpleIntegerProperty(d.getValue().getIdAlerta()).asObject());

        TableColumn<Alerta, String> colProducto = new TableColumn<>("Producto");
        colProducto.setCellValueFactory(d -> {
            Producto p = inventarioService.buscarProducto(d.getValue().getIdProducto());
            String nombre = p != null ? p.getNombreProducto()
                                      : "ID: " + d.getValue().getIdProducto();
            return new SimpleStringProperty(nombre);
        });

        TableColumn<Alerta, Integer> colStock = new TableColumn<>("Stock al momento");
        colStock.setCellValueFactory(d ->
            new SimpleIntegerProperty(d.getValue().getStockAlMomento()).asObject());

        TableColumn<Alerta, String> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(d -> new SimpleStringProperty(
            d.getValue().getFechaAlerta() != null
                ? d.getValue().getFechaAlerta().toString() : ""));

        TableColumn<Alerta, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue().getEstadoAlerta()));

        colEstado.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String estado, boolean empty) {
                super.updateItem(estado, empty);
                if (empty || estado == null) {
                    setText(null); setStyle("");
                } else {
                    setText(estado);
                    if ("pendiente".equalsIgnoreCase(estado)) {
                        setStyle("-fx-background-color: #ffb3b3; -fx-text-fill: #c62828;");
                    } else {
                        setStyle("-fx-background-color: #b3ffb3; -fx-text-fill: #1b5e20;");
                    }
                }
            }
        });

        tabla.getColumns().addAll(colId, colProducto, colStock, colFecha, colEstado);

        RadioButton rbPendientes = new RadioButton("Pendientes");
        RadioButton rbTodas      = new RadioButton("Todas");
        rbPendientes.setSelected(true);
        ToggleGroup grupo = new ToggleGroup();
        rbPendientes.setToggleGroup(grupo);
        rbTodas.setToggleGroup(grupo);

        Button btnAtender    = new Button("Marcar atendida");
        Button btnActualizar = new Button("Actualizar");
        btnAtender.getStyleClass().add("btn-peligro");

        HBox barraTop = new HBox(10, rbPendientes, rbTodas, btnAtender, btnActualizar);
        barraTop.setPadding(new Insets(8));
        barraTop.setAlignment(Pos.CENTER_LEFT);

        VBox root = new VBox(barraTop, tabla);
        VBox.setVgrow(tabla, Priority.ALWAYS);
        panel.getChildren().add(root);

        cargarPendientes();

        btnActualizar.setOnAction(e -> {
            if (rbPendientes.isSelected()) cargarPendientes();
            else cargarTodas();
        });

        rbPendientes.setOnAction(e -> cargarPendientes());
        rbTodas.setOnAction(e -> cargarTodas());

        btnAtender.setOnAction(e -> {
            Alerta seleccionada = tabla.getSelectionModel().getSelectedItem();
            if (seleccionada == null) {
                new Alert(Alert.AlertType.WARNING,
                    "Selecciona una alerta primero.").showAndWait();
                return;
            }
            alertaService.atenderAlerta(seleccionada.getIdAlerta());
            new Alert(Alert.AlertType.INFORMATION,
                "Alerta marcada como atendida.").showAndWait();
            if (rbPendientes.isSelected()) cargarPendientes();
            else cargarTodas();
        });
    }

    private void cargarPendientes() {
        List<Alerta> alertas = alertaService.listarAlertasPendientes();
        tabla.setItems(FXCollections.observableArrayList(alertas));
    }

    private void cargarTodas() {
        List<Alerta> alertas = alertaService.listarTodasLasAlertas();
        tabla.setItems(FXCollections.observableArrayList(alertas));
    }
}