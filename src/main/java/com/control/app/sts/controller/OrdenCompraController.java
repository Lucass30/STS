package com.control.app.sts.controller;

import com.control.app.sts.models.entity.ItemOrden;
import com.control.app.sts.models.entity.OrdenCompra;
import com.control.app.sts.models.entity.Producto;
import com.control.app.sts.models.entity.Proveedor;
import com.control.app.sts.models.service.interfaces.IOrdenCompraService;
import com.control.app.sts.models.service.interfaces.IProductoService;
import com.control.app.sts.models.service.interfaces.IProveedorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/ordencompra")
@SessionAttributes("ordenCompra")
public class OrdenCompraController {

    @Autowired
    private IProveedorService proveedorService;

    @Autowired
    private IProductoService productoService;

    @Autowired
    private IOrdenCompraService ordecompraService;

    private final Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping("/ver/{id}")
    public String ver(@PathVariable(value="id") Long id, Model model,
                      RedirectAttributes flash) {
        OrdenCompra ordenCompra = ordecompraService.findOrdenCompraById(id);

        if(ordenCompra == null) {
            flash.addFlashAttribute("error", "La orden de compra no existe en la base de datos!");
            return "redirect:proveedor/listar";
        }

        model.addAttribute("ordenCompra", ordenCompra);
        model.addAttribute("titulo", "Orden de Compra: ".concat(ordenCompra.getDescripcion()));

        return "ordenescompra/ver";
    }

    @GetMapping("/form/{Id}")
    public String crear(@PathVariable(value = "Id") Long Id, Map<String, Object> model,
                        RedirectAttributes flash) {

        Proveedor proveedor = proveedorService.findOne(Id);

        if (proveedor == null) {
            flash.addFlashAttribute("error", "El proveedor no existe en la base de datos");
            return "redirect:/proveedor/listar";
        }

        OrdenCompra ordenCompra = new OrdenCompra();
        ordenCompra.setProveedor(proveedor);

        model.put("ordenCompra", ordenCompra);
        model.put("titulo", "Crear Orden de Compra");

        return "proveedores/crearorden";
    }

    @GetMapping(value = "/cargar-producto/{id}/{term}", produces = { "application/json" })
    public @ResponseBody List<Producto> cargarProductos(@PathVariable(value = "term") String term, @PathVariable Long id) {

        return productoService.findByProveedor(id,term);
    }
    @PostMapping("/form")
    public String guardar(@Valid OrdenCompra ordenCompra, BindingResult result, Model model,
                          @RequestParam(name = "item_id[]", required = false) Long[] itemId,
                          @RequestParam(name = "cantidad[]", required = false) Integer[] cantidad,
                          RedirectAttributes flash,
                          SessionStatus status) {

        if (result.hasErrors()) {
            model.addAttribute("titulo", "Crear Orden de Compra");
            return "proveedores/crearorden";
        }
        if (itemId == null || itemId.length == 0) {
            model.addAttribute("titulo", "Crear Orden de Compra");
            model.addAttribute("error", "Error: La Orden de Compra NO puede no tener líneas!");
            return "proveedores/crearorden";
        }
        for (int i = 0; i < itemId.length; i++) {
            Producto producto = productoService.findOne(itemId[i]);

            ItemOrden linea = new ItemOrden();
            linea.setCantidad(cantidad[i]);
            linea.setProducto(producto);
            ordenCompra.addItemOrden(linea);
            log.info("ID: " + itemId[i].toString() + ", cantidad: " + cantidad[i].toString());
        }

        System.out.println(ordenCompra.getProveedor().getId());
        ordecompraService.save(ordenCompra);
        status.setComplete();
        flash.addFlashAttribute("success", "Orden de Compra creada con éxito!");

        return "redirect:/proveedor/ver/" + ordenCompra.getProveedor().getId();
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable(value="id") Long id, RedirectAttributes flash) {

        OrdenCompra ordenCompra = ordecompraService.findOrdenCompraById(id);

        if(ordenCompra != null) {
            ordecompraService.delete(id);
            flash.addFlashAttribute("success", "Orden de Compra eliminada con éxito!");
            return "redirect:/proveedor/ver/" + ordenCompra.getProveedor().getId();
        }
        flash.addFlashAttribute("error", "La Orden de Compra no existe en la base de datos, no se pudo eliminar!");

        return "redirect:/proveedor/listar";
    }
}
