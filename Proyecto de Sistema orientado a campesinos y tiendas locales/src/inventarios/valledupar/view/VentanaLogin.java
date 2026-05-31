package inventarios.valledupar.view;

import inventarios.valledupar.model.Usuario;
import inventarios.valledupar.service.UsuarioService;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class VentanaLogin extends Application {

    private UsuarioService usuarioService;

    @Override
    public void start(Stage stage) {
        usuarioService = new UsuarioService();

        // Logo
        ImageView logo = new ImageView(
            new Image(getClass().getResourceAsStream("logo.png"))
        );
        logo.setFitWidth(80);
        logo.setFitHeight(80);
        logo.setPreserveRatio(true);

        Label lblTitulo = new Label("Sistema de Gestión de Inventarios");
        lblTitulo.getStyleClass().add("label-titulo");

        Label lblSubtitulo = new Label("Valledupar - Cesar");
        lblSubtitulo.setStyle("-fx-text-fill: #558b2f; -fx-font-size: 12px;");

        VBox cabecera = new VBox(8, logo, lblTitulo, lblSubtitulo);
        cabecera.setAlignment(Pos.CENTER);

        Label lblCorreo = new Label("Correo:");
        TextField txtCorreo = new TextField();
        txtCorreo.setPromptText("ejemplo@correo.com");
        txtCorreo.setPrefWidth(220);

        Label lblContrasena = new Label("Contraseña:");
        PasswordField txtContrasena = new PasswordField();
        txtContrasena.setPromptText("••••••••");
        txtContrasena.setPrefWidth(220);

        Button btnIngresar = new Button("Ingresar");
        btnIngresar.setPrefWidth(200);
        btnIngresar.setPrefHeight(38);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(12);
        grid.setAlignment(Pos.CENTER);
        grid.add(lblCorreo, 0, 0);
        grid.add(txtCorreo, 1, 0);
        grid.add(lblContrasena, 0, 1);
        grid.add(txtContrasena, 1, 1);

        VBox tarjeta = new VBox(18);
        tarjeta.setAlignment(Pos.CENTER);
        tarjeta.setPadding(new Insets(35, 40, 35, 40));
        tarjeta.setMaxWidth(420);
        tarjeta.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 10;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 12, 0, 0, 4);"
        );
        tarjeta.getChildren().addAll(cabecera, grid, btnIngresar);

        StackPane root = new StackPane(tarjeta);
        root.setStyle("-fx-background-color: #e8f5e9;");

        btnIngresar.setOnAction(e -> {
            String correo = txtCorreo.getText().trim();
            String contrasena = txtContrasena.getText().trim();

            if (correo.isEmpty() || contrasena.isEmpty()) {
                mostrarAlerta(Alert.AlertType.WARNING, "Advertencia",
                        "Por favor completa todos los campos.");
                return;
            }

            Usuario usuario = usuarioService.autenticar(correo, contrasena);
            if (usuario != null) {
                VentanaPrincipal ventana = new VentanaPrincipal(usuario);
                Stage nuevaVentana = new Stage();
                try {
                    ventana.start(nuevaVentana);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                stage.close();
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error",
                        "Correo o contraseña incorrectos.");
            }
        });

        txtContrasena.setOnAction(e -> btnIngresar.fire());

        Scene scene = new Scene(root, 520, 430);
        scene.getStylesheets().add(getClass().getResource("estilos.css").toExternalForm());
        stage.setTitle("Sistema de Inventarios - Valledupar");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}