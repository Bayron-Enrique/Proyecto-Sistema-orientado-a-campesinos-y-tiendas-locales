package inventarios.valledupar.view;

import inventarios.valledupar.model.Usuario;
import inventarios.valledupar.service.UsuarioService;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class VentanaLogin extends Application {

    private UsuarioService usuarioService;

    @Override
    public void start(Stage stage) {
        usuarioService = new UsuarioService();

        // Titulo
        Label lblTitulo = new Label("Sistema de Gestión de Inventarios");
        lblTitulo.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        // Correo
        Label lblCorreo = new Label("Correo:");
        TextField txtCorreo = new TextField();
        txtCorreo.setPrefWidth(220);

        // Contraseña
        Label lblContrasena = new Label("Contraseña:");
        PasswordField txtContrasena = new PasswordField();
        txtContrasena.setPrefWidth(220);

        // Boton
        Button btnIngresar = new Button("Ingresar");
        btnIngresar.setPrefWidth(150);

        // Layout del formulario
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(12);
        grid.setAlignment(Pos.CENTER);
        grid.add(lblCorreo, 0, 0);
        grid.add(txtCorreo, 1, 0);
        grid.add(lblContrasena, 0, 1);
        grid.add(txtContrasena, 1, 1);

        // Layout principal
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.getChildren().addAll(lblTitulo, grid, btnIngresar);

        // Accion del boton
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
                mostrarAlerta(Alert.AlertType.INFORMATION, "Bienvenido",
                        "Bienvenido, " + usuario.getNombre());
                new VentanaPrincipal(usuario).start(new Stage());
                stage.close();
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error",
                        "Correo o contraseña incorrectos.");
            }
        });

        Scene scene = new Scene(root, 420, 280);
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