package inventarios.valledupar.dao;

import inventarios.valledupar.model.Alerta;
import java.util.List;

public class AlertaDAO implements IAlertaDAO {

    @Override
    public void guardar(Alerta alerta) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Alerta> buscarPendientes() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Alerta> buscarTodas() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void marcarAtendida(int idAlerta) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}