package inventarios.valledupar.view;

import inventarios.valledupar.model.Usuario;
import inventarios.valledupar.service.AlertaService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.util.List;

public class VentanaPrincipal {

    private Usuario usuarioActivo;
    private StackPane panelContenido;

    public VentanaPrincipal(Usuario usuarioActivo) {
        this.usuarioActivo = usuarioActivo;
    }

    public void start(Stage stage) {

        // Logo en panel superior
        ImageView logo = new ImageView(
            new Image(getClass().getResourceAsStream("logo.png"))
        );
        logo.setFitWidth(40);
        logo.setFitHeight(40);
        logo.setPreserveRatio(true);

        Label lblBienvenida = new Label("Usuario: " + usuarioActivo.getNombre()
                + "  |  Rol: " + usuarioActivo.getRol());
        lblBienvenida.getStyleClass().add("label-bienvenida");

        HBox panelSuperior = new HBox(12, logo, lblBienvenida);
        panelSuperior.setPadding(new Insets(10, 20, 10, 20));
        panelSuperior.setAlignment(Pos.CENTER_LEFT);
        panelSuperior.getStyleClass().add("panel-superior");

        panelContenido = new StackPane();
        panelContenido.getStyleClass().add("panel-contenido");
        Label lblInicio = new Label("Selecciona un modulo del menu lateral.");
        lblInicio.getStyleClass().add("label-titulo");
        panelContenido.getChildren().add(lblInicio);

        Button btnProductos   = crearBotonMenu("Productos");
        Button btnMovimientos = crearBotonMenu("Movimientos");
        Button btnAlertas     = crearBotonMenu("Alertas");
        Button btnReportes    = crearBotonMenu("Reportes");
        Button btnUsuarios    = crearBotonMenu("Usuarios");
        Button btnSalir       = new Button("Cerrar sesion");
        btnSalir.setPrefWidth(150);
        btnSalir.setPrefHeight(40);
        btnSalir.getStyleClass().add("btn-salir");

        // Badge de alertas pendientes
        StackPane btnAlertasConBadge = crearBotonConBadge(btnAlertas);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        VBox panelMenu = new VBox(8);
        panelMenu.setPadding(new Insets(15));
        panelMenu.setPrefWidth(165);
        panelMenu.getStyleClass().add("menu-lateral");
        panelMenu.getChildren().addAll(
            btnProductos, btnMovimientos,
            btnAlertasConBadge, btnReportes,
            btnUsuarios, spacer, btnSalir
        );

        btnProductos.setOnAction(e -> {
            panelContenido.getChildren().clear();
            new VentanaProductos().mostrarEnPanel(panelContenido);
        });

        btnMovimientos.setOnAction(e -> {
            panelContenido.getChildren().clear();
            new VentanaMovimientos(usuarioActivo).mostrarEnPanel(panelContenido);
        });

        btnAlertas.setOnAction(e -> {
            panelContenido.getChildren().clear();
            new VentanaAlertas().mostrarEnPanel(panelContenido);
            // Refrescar badge despues de ver alertas
            actualizarBadge(btnAlertasConBadge, btnAlertas);
        });

        btnReportes.setOnAction(e -> {
            panelContenido.getChildren().clear();
            new VentanaReportes(usuarioActivo).mostrarEnPanel(panelContenido);
        });

        btnUsuarios.setOnAction(e -> {
            panelContenido.getChildren().clear();
            new VentanaUsuarios().mostrarEnPanel(panelContenido);
        });

        btnSalir.setOnAction(e -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Cerrar sesion");
            confirm.setHeaderText(null);
            confirm.setContentText("¿Deseas cerrar sesion?");
            confirm.showAndWait().ifPresent(respuesta -> {
                if (respuesta == ButtonType.OK) {
                    try {
                        new VentanaLogin().start(new Stage());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    stage.close();
                }
            });
        });

        BorderPane root = new BorderPane();
        root.setTop(panelSuperior);
        root.setLeft(panelMenu);
        root.setCenter(panelContenido);

        Scene scene = new Scene(root, 980, 620);
        scene.getStylesheets().add(
            getClass().getResource("estilos.css").toExternalForm());
        stage.setTitle("Sistema de Inventarios - Valledupar");
        stage.setScene(scene);
        stage.show();
    }

    private StackPane crearBotonConBadge(Button btn) {
        StackPane contenedor = new StackPane();

        // Contar alertas pendientes
        AlertaService alertaService = new AlertaService();
        int pendientes = alertaService.listarAlertasPendientes().size();

        contenedor.getChildren().add(btn);

        if (pendientes > 0) {
            // Circulo rojo
            Circle circulo = new Circle(10);
            circulo.setFill(Color.RED);

            // Numero dentro del circulo
            Label lblNum = new Label(pendientes > 9 ? "9+" : String.valueOf(pendientes));
            lblNum.setStyle("-fx-text-fill: white; -fx-font-size: 10px; -fx-font-weight: bold;");

            StackPane badge = new StackPane(circulo, lblNum);
            badge.setMaxSize(20, 20);
            StackPane.setAlignment(badge, Pos.TOP_RIGHT);
            badge.setTranslateX(8);
            badge.setTranslateY(-8);

            contenedor.getChildren().add(badge);
        }

        return contenedor;
    }

    private void actualizarBadge(StackPane contenedor, Button btn) {
        contenedor.getChildren().clear();
        contenedor.getChildren().add(btn);

        AlertaService alertaService = new AlertaService();
        int pendientes = alertaService.listarAlertasPendientes().size();

        if (pendientes > 0) {
            Circle circulo = new Circle(10);
            circulo.setFill(Color.RED);
            Label lblNum = new Label(pendientes > 9 ? "9+" : String.valueOf(pendientes));
            lblNum.setStyle("-fx-text-fill: white; -fx-font-size: 10px; -fx-font-weight: bold;");
            StackPane badge = new StackPane(circulo, lblNum);
            badge.setMaxSize(20, 20);
            StackPane.setAlignment(badge, Pos.TOP_RIGHT);
            badge.setTranslateX(8);
            badge.setTranslateY(-8);
            contenedor.getChildren().add(badge);
        }
    }

    private Button crearBotonMenu(String texto) {
        Button btn = new Button(texto);
        btn.setPrefWidth(150);
        btn.setPrefHeight(40);
        btn.getStyleClass().add("btn-menu");
        return btn;
    }
}