package org.iesvdm.servicios;

import java.util.List;
import java.util.Optional;

import org.iesvdm.dao.PedidoDAO;
import org.iesvdm.modelo.Pedido;
import org.iesvdm.modelo.PedidoDetalle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PedidoService {

    @Autowired
    private PedidoDAO pedidoDAO;

    public List<Pedido> listAll() {
        return pedidoDAO.getAll();
    }

    public List<PedidoDetalle> listAllDetalles() {
        return pedidoDAO.getAllDetalles();
    }

    public Pedido one(Integer id) {
        Optional<Pedido> pedido = pedidoDAO.find(id);
        return pedido.orElse(null);
    }

    public PedidoDetalle oneDetalle(Integer id) {
        Optional<PedidoDetalle> pedido = pedidoDAO.findDetalle(id);
        return pedido.orElse(null);
    }

    public void newPedido(Pedido pedido) {
        pedidoDAO.create(pedido);
    }

    public void replacePedido(Pedido pedido) {
        pedidoDAO.update(pedido);
    }

    public void deletePedido(int id) {
        pedidoDAO.delete(id);
    }
}
