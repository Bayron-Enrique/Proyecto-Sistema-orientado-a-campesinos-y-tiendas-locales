package inventarios.valledupar.view;

import inventarios.valledupar.model.Alerta;
import inventarios.valledupar.service.AlertaService;
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
    private TableView<Alerta> tabla;

    public VentanaAlertas() {
        alertaService = new AlertaService();
    }

    public void mostrarEnPanel(StackPane panel) {
        tabla = new TableView<>();
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Alerta, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getIdAlerta()).asObject());

        TableColumn<Alerta, Integer> colProducto = new TableColumn<>("Producto");
        colProducto.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getIdProducto()).asObject());

        TableColumn<Alerta, Integer> colStock = new TableColumn<>("Stock al momento");
        colStock.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getStockAlMomento()).asObject());

        TableColumn<Alerta, String> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getFechaAlerta() != null ? d.getValue().getFechaAlerta().toString() : ""));

        TableColumn<Alerta, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getEstadoAlerta()));

        tabla.getColumns().addAll(colId, colProducto, colStock, colFecha, colEstado);

        // Barra superior
        RadioButton rbPendientes = new RadioButton("Pendientes");
        RadioButton rbTodas      = new RadioButton("Todas");
        rbPendientes.setSelected(true);
        ToggleGroup grupo = new ToggleGroup();
        rbPendientes.setToggleGroup(grupo);
        rbTodas.setToggleGroup(grupo);

        Button btnAtender    = new Button("Marcar atendida");
        Button btnActualizar = new Button("Actualizar");

        HBox barraTop = new HBox(10, rbPendientes, rbTodas, btnAtender, btnActualizar);
        barraTop.setPadding(new Insets(8));
        barraTop.setAlignment(Pos.CENTER_LEFT);

        VBox root = new VBox(barraTop, tabla);
        VBox.setVgrow(tabla, Priority.ALWAYS);
        panel.getChildren().add(root);

        cargarPendientes();

        // Acciones
        btnActualizar.setOnAction(e -> {
            if (rbPendientes.isSelected()) cargarPendientes();
            else cargarTodas();
        });

        rbPendientes.setOnAction(e -> cargarPendientes());
        rbTodas.setOnAction(e -> cargarTodas());

        btnAtender.setOnAction(e -> {
            Alerta seleccionada = tabla.getSelectionModel().getSelectedItem();
            if (seleccionada == null) {
                new Alert(Alert.AlertType.WARNING, "Selecciona una alerta primero.").showAndWait();
                return;
            }
            alertaService.atenderAlerta(seleccionada.getIdAlerta());
            new Alert(Alert.AlertType.INFORMATION, "Alerta marcada como atendida.").showAndWait();
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