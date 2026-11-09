package org.iesvdm.servicios;

import java.util.List;
import java.util.Optional;

import org.iesvdm.dao.ClienteDAO;
import org.iesvdm.dao.PedidoDAO;
import org.iesvdm.modelo.Cliente;
import org.iesvdm.modelo.PedidoDetalle;
import org.springframework.stereotype.Service;

@Service 
public class ClienteService {

    private final ClienteDAO clienteDAO;
    private final PedidoDAO pedidoDAO;

    public ClienteService(ClienteDAO clienteDAO, PedidoDAO pedidoDAO) {
        this.clienteDAO = clienteDAO;
        this.pedidoDAO = pedidoDAO;
    }

    public List<Cliente> listAll(){

        return clienteDAO.getAll();

    }

    public List<Cliente> getByCiudad(String ciudad) {
        return clienteDAO.getByCiudad(ciudad);
    }

    public Cliente one(Integer id) {
        Optional<Cliente> cliente = clienteDAO.find(id);
        return cliente.orElse(null);
    }

    public List<PedidoDetalle> listPedidosByCliente(int clienteId) {
        return pedidoDAO.findByClienteId(clienteId);
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
