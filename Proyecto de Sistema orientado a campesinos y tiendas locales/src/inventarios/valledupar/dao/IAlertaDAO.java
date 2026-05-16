package inventarios.valledupar.dao;

import inventarios.valledupar.model.Alerta;
import java.util.List;

public interface IAlertaDAO {

    void guardar(Alerta alerta);
    List<Alerta> buscarPendientes();
    List<Alerta> buscarTodas();
    void marcarAtendida(int idAlerta);
}