package org.iesvdm.controlador;

import org.iesvdm.modelo.Pedido;
import org.iesvdm.modelo.PedidoDetalle;
import org.iesvdm.servicios.ClienteService;
import org.iesvdm.servicios.ComercialService;
import org.iesvdm.servicios.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ComercialService comercialService;

    @GetMapping("/pedidos")
    public String listar(Model model) {
        model.addAttribute("Pedidos", pedidoService.listAllDetalles());
        return "pedidos";
    }

    @GetMapping("/pedidos/{id}")
    public String detalle(Model model, @PathVariable Integer id) {
        PedidoDetalle pedido = pedidoService.oneDetalle(id);
        if (pedido == null) {
            return "redirect:/pedidos";
        }
        model.addAttribute("pedido", pedido);
        return "detalle-pedido";
    }

    @GetMapping("/pedidos/crear")
    public String crear(Model model) {
        model.addAttribute("pedido", new Pedido());
        model.addAttribute("formAction", "/pedidos/crear");
        model.addAttribute("titulo", "Crear pedido");
        cargarReferencias(model);
        return "form-pedido";
    }

    @PostMapping("/pedidos/crear")
    public String submitCrear(@ModelAttribute("pedido") Pedido pedido, Model model) {
        if (!referenciasValidas(pedido)) {
            cargarReferencias(model);
            model.addAttribute("formAction", "/pedidos/crear");
            model.addAttribute("titulo", "Crear pedido");
            model.addAttribute("error", "Selecciona un cliente y un comercial válidos.");
            return "form-pedido";
        }
        pedidoService.newPedido(pedido);
        return "redirect:/pedidos";
    }

    @GetMapping("/pedidos/editar/{id}")
    public String editar(Model model, @PathVariable Integer id) {
        Pedido pedido = pedidoService.one(id);
        if (pedido == null) {
            return "redirect:/pedidos";
        }
        model.addAttribute("pedido", pedido);
        model.addAttribute("formAction", "/pedidos/editar/" + id);
        model.addAttribute("titulo", "Editar pedido");
        cargarReferencias(model);
        return "form-pedido";
    }

    @PostMapping("/pedidos/editar/{id}")
    public String submitEditar(@PathVariable Integer id, @ModelAttribute("pedido") Pedido pedido, Model model) {
        if (!referenciasValidas(pedido)) {
            cargarReferencias(model);
            model.addAttribute("formAction", "/pedidos/editar/" + id);
            model.addAttribute("titulo", "Editar pedido");
            model.addAttribute("error", "Selecciona un cliente y un comercial válidos.");
            return "form-pedido";
        }
        pedido.setId(id);
        pedidoService.replacePedido(pedido);
        return "redirect:/pedidos";
    }

    @PostMapping("/pedidos/borrar/{id}")
    public String submitBorrar(@PathVariable Integer id) {
        pedidoService.deletePedido(id);
        return "redirect:/pedidos";
    }

    private void cargarReferencias(Model model) {
        model.addAttribute("clientes", clienteService.listAll());
        model.addAttribute("comerciales", comercialService.listAll());
    }

    private boolean referenciasValidas(Pedido pedido) {
        return pedido.getClienteId() != null
                && pedido.getComercialId() != null
                && clienteService.one(pedido.getClienteId()) != null
                && comercialService.one(pedido.getComercialId()) != null;
    }
}
