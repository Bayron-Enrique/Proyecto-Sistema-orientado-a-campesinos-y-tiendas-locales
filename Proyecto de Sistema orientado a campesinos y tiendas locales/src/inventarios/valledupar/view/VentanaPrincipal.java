package inventarios.valledupar.view;

import inventarios.valledupar.model.Usuario;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class VentanaPrincipal extends Application {

    private Usuario usuarioActivo;

    public VentanaPrincipal(Usuario usuarioActivo) {
        this.usuarioActivo = usuarioActivo;
    }

    @Override
    public void start(Stage stage) {

        // Panel superior
        Label lblBienvenida = new Label("Usuario: " + usuarioActivo.getNombre()
                + "  |  Rol: " + usuarioActivo.getRol());
        lblBienvenida.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        HBox panelSuperior = new HBox(lblBienvenida);
        panelSuperior.setPadding(new Insets(10, 15, 10, 15));
        panelSuperior.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #cccccc; -fx-border-width: 0 0 1 0;");

        // Panel contenido central
        StackPane panelContenido = new StackPane();
        Label lblInicio = new Label("Selecciona un modulo del menu lateral.");
        lblInicio.setFont(Font.font("Arial", 13));
        panelContenido.getChildren().add(lblInicio);

        // Botones del menu lateral
        Button btnProductos   = crearBotonMenu("Productos");
        Button btnMovimientos = crearBotonMenu("Movimientos");
        Button btnAlertas     = crearBotonMenu("Alertas");
        Button btnReportes    = crearBotonMenu("Reportes");
        Button btnSalir       = crearBotonMenu("Cerrar sesion");

        VBox panelMenu = new VBox(8);
        panelMenu.setPadding(new Insets(10));
        panelMenu.setPrefWidth(160);
        panelMenu.setStyle("-fx-background-color: #e8e8e8;");
        panelMenu.getChildren().addAll(btnProductos, btnMovimientos, btnAlertas, btnReportes, btnSalir);

        // Acciones de botones
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
        });

        btnReportes.setOnAction(e -> {
            panelContenido.getChildren().clear();
            new VentanaReportes(usuarioActivo).mostrarEnPanel(panelContenido);
        });

        btnSalir.setOnAction(e -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Cerrar sesion");
            confirm.setHeaderText(null);
            confirm.setContentText("¿Deseas cerrar sesion?");
            confirm.showAndWait().ifPresent(respuesta -> {
                if (respuesta == ButtonType.OK) {
                    new VentanaLogin().start(new Stage());
                    stage.close();
                }
            });
        });

        // Layout principal
        BorderPane root = new BorderPane();
        root.setTop(panelSuperior);
        root.setLeft(panelMenu);
        root.setCenter(panelContenido);

        Scene scene = new Scene(root, 950, 600);
        stage.setTitle("Sistema de Inventarios - Valledupar");
        stage.setScene(scene);
        stage.show();
    }

    private Button crearBotonMenu(String texto) {
        Button btn = new Button(texto);
        btn.setPrefWidth(140);
        btn.setPrefHeight(35);
        return btn;
    }
}