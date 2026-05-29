package inventarios.valledupar.view;

import inventarios.valledupar.model.Reporte;
import inventarios.valledupar.model.Usuario;
import inventarios.valledupar.service.ReporteService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

public class VentanaReportes {

    private ReporteService reporteService;
    private Usuario usuarioActivo;

    public VentanaReportes(Usuario usuarioActivo) {
        this.usuarioActivo = usuarioActivo;
        reporteService = new ReporteService();
    }

    public void mostrarEnPanel(StackPane panel) {

        // Radio buttons
        RadioButton rbInventario  = new RadioButton("Inventario general");
        RadioButton rbMovimientos = new RadioButton("Movimientos por periodo");
        rbInventario.setSelected(true);
        ToggleGroup grupo = new ToggleGroup();
        rbInventario.setToggleGroup(grupo);
        rbMovimientos.setToggleGroup(grupo);

        HBox panelRadio = new HBox(15, rbInventario, rbMovimientos);
        panelRadio.setPadding(new Insets(8));
        panelRadio.setAlignment(Pos.CENTER_LEFT);

        // Fechas
        Label lblDesde = new Label("Desde (yyyy-MM-dd):");
        TextField txtDesde = new TextField();
        txtDesde.setPrefWidth(130);
        Label lblHasta = new Label("Hasta (yyyy-MM-dd):");
        TextField txtHasta = new TextField();
        txtHasta.setPrefWidth(130);
        Button btnGenerar = new Button("Generar reporte");

        HBox panelFechas = new HBox(8, lblDesde, txtDesde, lblHasta, txtHasta, btnGenerar);
        panelFechas.setPadding(new Insets(8));
        panelFechas.setAlignment(Pos.CENTER_LEFT);

        // Area de contenido
        TextArea txtContenido = new TextArea();
        txtContenido.setEditable(false);
        txtContenido.setFont(Font.font("Monospaced", 12));
        VBox.setVgrow(txtContenido, Priority.ALWAYS);

        VBox root = new VBox(panelRadio, panelFechas, txtContenido);
        root.setPadding(new Insets(8));
        panel.getChildren().add(root);

        // Accion generar
        btnGenerar.setOnAction(e -> {
            int idUsuario = usuarioActivo != null ? usuarioActivo.getIdUsuario() : 0;
            Reporte reporte;

            if (rbInventario.isSelected()) {
                reporte = reporteService.generarReporteInventario(idUsuario);
                txtContenido.setText(reporte.getContenido());
            } else {
                String inicio = txtDesde.getText().trim();
                String fin    = txtHasta.getText().trim();
                if (inicio.isEmpty() || fin.isEmpty()) {
                    new Alert(Alert.AlertType.WARNING,
                            "Ingresa las dos fechas en formato yyyy-MM-dd").showAndWait();
                    return;
                }
                reporte = reporteService.generarReporteMovimientos(idUsuario, inicio, fin);
                txtContenido.setText(reporte.getContenido());
            }
        });
    }
}