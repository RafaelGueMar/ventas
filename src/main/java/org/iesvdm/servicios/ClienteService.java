package org.iesvdm.servicios;

import java.util.List;
import java.util.Optional;

import org.iesvdm.dao.ClienteDAO;
import org.iesvdm.modelo.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service 
public class ClienteService {

    @Autowired 
    private ClienteDAO clienteDAO;

    public List<Cliente> listAll(){

        return clienteDAO.getAll();

    }

    public Cliente one(Integer id) {
        Optional<Cliente> cliente = clienteDAO.find(id);
        return cliente.orElse(null);
    }

    public void newCliente(Cliente cliente) {
        clienteDAO.create(cliente);
    }

    public void replaceCliente(Cliente cliente) {
        clienteDAO.update(cliente);
    }

    public void deleteCliente(int id) {
        clienteDAO.delete(id);
    }

}
