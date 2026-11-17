package org.iesvdm.servicios;

import java.util.Optional;

import org.iesvdm.dao.ClienteDAO;
import org.iesvdm.dao.ClienteComercialDAO;
import org.iesvdm.dao.ComercialDAO;
import org.iesvdm.dao.PedidoDAO;
import org.iesvdm.modelo.Cliente;
import org.iesvdm.modelo.ClienteComercial;
import org.iesvdm.modelo.Comercial;
import org.iesvdm.modelo.PedidoDetalle;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service 
public class ClienteService {

    private final ClienteDAO clienteDAO;
    private final PedidoDAO pedidoDAO;
    private final ClienteComercialDAO clienteComercialDAO;
    private final ComercialDAO comercialDAO;

    public ClienteService(ClienteDAO clienteDAO, PedidoDAO pedidoDAO,
            ClienteComercialDAO clienteComercialDAO, ComercialDAO comercialDAO) {
        this.clienteDAO = clienteDAO;
        this.pedidoDAO = pedidoDAO;
        this.clienteComercialDAO = clienteComercialDAO;
        this.comercialDAO = comercialDAO;
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

    public List<ClienteComercial> listComercialesByCliente(int clienteId) {
        return clienteComercialDAO.findByClienteId(clienteId);
    }

    public List<Comercial> listComercialesDisponibles(int clienteId) {
        List<Integer> vinculados = listComercialesByCliente(clienteId).stream()
                .map(vinculo -> vinculo.getComercial().getId())
                .toList();
        return comercialDAO.getAll().stream()
                .filter(comercial -> !vinculados.contains(comercial.getId()))
                .toList();
    }

    public void addComercial(int clienteId, int comercialId, LocalDate fecha, int prioridad) {
        clienteComercialDAO.create(clienteId, comercialId, fecha, prioridad);
    }

    public void removeComercial(int clienteId, int comercialId) {
        clienteComercialDAO.delete(clienteId, comercialId);
    }

}
