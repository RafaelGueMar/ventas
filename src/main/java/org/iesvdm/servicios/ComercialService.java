package org.iesvdm.servicios;

import java.util.List;
import java.util.Optional;

import org.iesvdm.dao.ComercialDAO;
import org.iesvdm.modelo.Comercial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComercialService {

	@Autowired
	private ComercialDAO comercialDAO;

	public List<Comercial> listAll() {
		return comercialDAO.getAll();
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