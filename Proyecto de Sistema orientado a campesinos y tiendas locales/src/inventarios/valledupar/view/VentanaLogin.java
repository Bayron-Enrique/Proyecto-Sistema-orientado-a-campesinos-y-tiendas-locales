package inventarios.valledupar.view;

import inventarios.valledupar.model.Usuario;
import inventarios.valledupar.service.UsuarioService;
import javax.swing.*;
import java.awt.*;

public class VentanaLogin extends JFrame {

    private JTextField txtCorreo;
    private JPasswordField txtContrasena;
    private JButton btnIngresar;
    private JLabel lblCorreo;
    private JLabel lblContrasena;
    private JLabel lblTitulo;
    private UsuarioService usuarioService;

    public VentanaLogin() {
        usuarioService = new UsuarioService();
        initComponents();
    }

    private void initComponents() {
        setTitle("Sistema de Inventarios - Valledupar");
        setSize(400, 280);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);

        lblTitulo = new JLabel("Sistema de Gestión de Inventarios");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(lblTitulo, gbc);

        lblCorreo = new JLabel("Correo:");
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        add(lblCorreo, gbc);

        txtCorreo = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 1;
        add(txtCorreo, gbc);

        lblContrasena = new JLabel("Contraseña:");
        gbc.gridx = 0; gbc.gridy = 2;
        add(lblContrasena, gbc);

        txtContrasena = new JPasswordField(20);
        gbc.gridx = 1; gbc.gridy = 2;
        add(txtContrasena, gbc);

        btnIngresar = new JButton("Ingresar");
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        add(btnIngresar, gbc);

        btnIngresar.addActionListener(e -> {
            String correo = txtCorreo.getText().trim();
            String contrasena = new String(txtContrasena.getPassword()).trim();

            if (correo.isEmpty() || contrasena.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Por favor completa todos los campos.",
                        "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Usuario usuario = usuarioService.autenticar(correo, contrasena);
            if (usuario != null) {
                JOptionPane.showMessageDialog(this, "Bienvenido, " + usuario.getNombre());
                new VentanaPrincipal(usuario).setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Correo o contraseña incorrectos.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaLogin().setVisible(true));
    }
}