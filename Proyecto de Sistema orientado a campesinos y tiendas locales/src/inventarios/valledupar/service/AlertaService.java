package inventarios.valledupar.service;

import inventarios.valledupar.dao.AlertaDAO;
import inventarios.valledupar.dao.IAlertaDAO;
import inventarios.valledupar.model.Alerta;
import inventarios.valledupar.model.Producto;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AlertaService {

    private IAlertaDAO alertaDAO;

    public AlertaService() {
        this.alertaDAO = new AlertaDAO();
    }

    public void generarAlerta(Producto producto) {
        Alerta alerta = new Alerta();
        alerta.setIdProducto(producto.getIdProducto());
        alerta.setStockAlMomento(producto.getStockActual());
        alerta.setEstadoAlerta("pendiente");
        alerta.setFechaAlerta(LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        alertaDAO.guardar(alerta);
    }

    public List<Alerta> listarAlertasPendientes() {
        return alertaDAO.buscarPendientes();
    }

    public List<Alerta> listarTodasLasAlertas() {
        return alertaDAO.buscarTodas();
    }

    public void atenderAlerta(int idAlerta) {
        alertaDAO.marcarAtendida(idAlerta);
    }
}