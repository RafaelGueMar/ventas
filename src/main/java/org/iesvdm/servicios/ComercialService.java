package org.iesvdm.servicios;

import java.util.List;
import java.util.Optional;

import org.iesvdm.dao.ComercialDAO;
import org.iesvdm.dao.PedidoDAO;
import org.iesvdm.modelo.Comercial;
import org.iesvdm.modelo.PedidoDetalle;
import org.springframework.stereotype.Service;

@Service
public class ComercialService {

	private final ComercialDAO comercialDAO;
	private final PedidoDAO pedidoDAO;

	public ComercialService(ComercialDAO comercialDAO, PedidoDAO pedidoDAO) {
		this.comercialDAO = comercialDAO;
		this.pedidoDAO = pedidoDAO;
	}

	public List<Comercial> listAll() {
		return comercialDAO.getAll();
	}

	public List<PedidoDetalle> listPedidosByComercial(int comercialId) {
		return pedidoDAO.findByComercialId(comercialId);
	}

	public Comercial one(Integer id) {
		Optional<Comercial> comercial = comercialDAO.find(id);
		return comercial.orElse(null);
	}

	public void newComercial(Comercial comercial) {
		comercialDAO.create(comercial);
	}

	public void replaceComercial(Comercial comercial) {
		comercialDAO.update(comercial);
	}

	public void deleteComercial(int id) {
		comercialDAO.delete(id);
	}
}