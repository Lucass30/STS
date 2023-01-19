package com.control.app.sts.controller;

import com.control.app.sts.models.entity.SubCategoria;
import com.control.app.sts.models.service.interfaces.ICategoriaService;
import com.control.app.sts.models.service.interfaces.IProductoService;
import com.control.app.sts.models.service.interfaces.ISubCategoriaService;
import com.control.app.sts.paginator.PageRender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
@SessionAttributes("subcategoria")
@RequestMapping("/subcategoria")
public class SubCategoriaController {
    @Autowired
    private ISubCategoriaService subcategoriaService;
    @Autowired
    private ICategoriaService categoriaService;

    @Autowired
    private IProductoService productoService;


    @RequestMapping(value={"/listar"}, method = RequestMethod.GET)
    public String listar(@RequestParam(name="page",defaultValue = "0")int page, Model model){

        Pageable pageRequest = PageRequest.of(page, 10);
        Page<SubCategoria> subcategorias = subcategoriaService.findAll(pageRequest);
        PageRender<SubCategoria> pageRender = new PageRender<>("/listar",subcategorias);

        model.addAttribute("titulo", "Listado de SubCategorias");
        model.addAttribute("subcategorias",subcategoriaService.findAll());
        model.addAttribute("page", pageRender);
        return "categorias/listar";
    }

    @GetMapping(value = "/ver/{id}")
    public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
        SubCategoria subCategoria = subcategoriaService.findOne(id);
        if (subCategoria == null) {
            flash.addFlashAttribute("error", "La Categoria no existe en la base de datos");
            return "redirect:/categoria/listar";
        }
        model.put("producto",productoService.findBySubCategoria(id));
        model.put("subcategoria", subCategoria);
        model.put("titulo", "Detalle Producto por SubCategoria: " + subCategoria.getNombre());
        return "subcategorias/ver";
    }
    @RequestMapping(value="/form", method = RequestMethod.GET)
    public String crear (Map<String, Object> model){
        SubCategoria subcategoria= new SubCategoria();
        model.put("categoria",categoriaService.findAll());
        model.put("subcategorias",subcategoria);
        model.put("titulo", "Formulario de SubCategoria");
        return "subcategorias/form";
    }

    @GetMapping(value = "/cargar", produces = { "application/json" })
    public @ResponseBody List<SubCategoria> cargarSubCategoria(@RequestParam(value = "id", required = true) Long term) {
        return subcategoriaService.findByCategoria(term);
    }

    @GetMapping(value = "/form/{id}")
    public String editar(@PathVariable(value = "id")Long id, Map<String, Object> model, RedirectAttributes flash) {
        SubCategoria subcategoria =null;
        if (id > 0) {
            subcategoria = subcategoriaService.findOne(id);
            if (subcategoria == null) {
                flash.addFlashAttribute("error", "El ID de la SubCategorias no existe en la BBDD!");
                return "redirect:/categoria/listar";
            }
        } else {
            flash.addFlashAttribute("error", "El ID de la SubCategoria no puede ser cero!");
            return "redirect:/categoria/listar";
        }
        model.put("categoria",categoriaService.findAll());
        model.put("subcategorias", subcategoria);
        model.put("titulo", "Editar SubCategoria");
        return "categorias/form";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String guardar(@Valid SubCategoria subcategoria, BindingResult result, Model model,
                          RedirectAttributes flash, SessionStatus status) {

        if (result.hasErrors()) {
            model.addAttribute("categoria",categoriaService.findAll());
            model.addAttribute("subcategorias", subcategoria);
            model.addAttribute("titulo", "Formulario de SubCategorias");
            return "categorias/form";
        }


        String mensajeFlash = (subcategoria.getId() != null) ? "SubCategoria editada con éxito!" : "SubCategoria creada con éxito!";

        subcategoriaService.save(subcategoria);
        status.setComplete();
        flash.addFlashAttribute("success", mensajeFlash);
        return "redirect:/categoria/listar";
    }

    @RequestMapping(value = "/eliminar/{id}")
    public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {

        if (id != null) {
            subcategoriaService.delete(id);
            flash.addFlashAttribute("success", "SubCategoria eliminada con éxito!");

        }
        return "redirect:/categoria/ver";
    }


}

