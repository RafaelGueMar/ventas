package org.iesvdm.controlador;

import org.iesvdm.modelo.Cliente;
import org.iesvdm.servicios.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/clientes")
    public String listar(Model model) {
        model.addAttribute("Clientes", clienteService.listAll());
        return "clientes";
    }

    @GetMapping("/clientes/{id}")
    public String detalle(Model model, @PathVariable Integer id) {
        Cliente cliente = clienteService.one(id);
        if (cliente == null) {
            return "redirect:/clientes";
        }
        model.addAttribute("cliente", cliente);
        return "detalle-cliente";
    }

    @GetMapping("/clientes/crear")
    public String crear(Model model) {
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("formAction", "/clientes/crear");
        model.addAttribute("titulo", "Crear cliente");
        return "form-cliente";
    }

    @PostMapping("/clientes/crear")
    public String submitCrear(@ModelAttribute("cliente") Cliente cliente) {
        clienteService.newCliente(cliente);
        return "redirect:/clientes";
    }

    @GetMapping("/clientes/editar/{id}")
    public String editar(Model model, @PathVariable Integer id) {
        Cliente cliente = clienteService.one(id);
        if (cliente == null) {
            return "redirect:/clientes";
        }
        model.addAttribute("cliente", cliente);
        model.addAttribute("formAction", "/clientes/editar/" + id);
        model.addAttribute("titulo", "Editar cliente");
        return "form-cliente";
    }

    @PostMapping("/clientes/editar/{id}")
    public String submitEditar(@PathVariable Integer id, @ModelAttribute("cliente") Cliente cliente) {
        cliente.setId(id);
        clienteService.replaceCliente(cliente);
        return "redirect:/clientes";
    }

    @PostMapping("/clientes/borrar/{id}")
    public String submitBorrar(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            clienteService.deleteCliente(id);
            redirectAttributes.addFlashAttribute("mensaje", "Cliente eliminado correctamente.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("mensaje",
                    "No se puede eliminar este cliente porque tiene pedidos asociados.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "warning");
        }
        return "redirect:/clientes";
    }

}

