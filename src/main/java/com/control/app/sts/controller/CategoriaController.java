package com.control.app.sts.controller;

import com.control.app.sts.models.entity.Categoria;
import com.control.app.sts.models.service.interfaces.ISubCategoriaService;
import com.control.app.sts.models.service.interfaces.IUploadFileService;
import com.control.app.sts.paginator.PageRender;
import com.control.app.sts.models.service.interfaces.ICategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

@Controller
@SessionAttributes("categoria")
@RequestMapping("/categoria")
public class CategoriaController {
    @Autowired
    private ICategoriaService categoriaService;

    @Autowired
    private ISubCategoriaService subcategoriaService;

    @Autowired
    private IUploadFileService uploadFileService;

    @RequestMapping(value={"/listar"}, method = RequestMethod.GET)
    public String listar(@RequestParam(name="page",defaultValue = "0")int page, Model model){
        Pageable pageRequest = PageRequest.of(page, 10);
        Page<Categoria> categorias = categoriaService.findAll(pageRequest);
        PageRender<Categoria> pageRender = new PageRender<>("/listar",categorias);

        model.addAttribute("titulo", "Listado de Categorias");
        model.addAttribute("categorias",categoriaService.findAll());
        model.addAttribute("page", pageRender);
        return "categorias/listar";
    }
    @GetMapping(value = "/ver/{id}")
    public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

        Categoria categoria = categoriaService.findOne(id);
        if (categoria == null) {
            flash.addFlashAttribute("error", "La categoria no existe en la base de datos");
            return "redirect:/categoria/listar";
        }

        model.put("subcategoria",subcategoriaService.findByCategoria(id));
        model.put("categoria", categoria);
        model.put("titulo", "Detalle proveedor: " + categoria.getNombre());
        return "categorias/ver";
    }
    @RequestMapping(value="/form", method = RequestMethod.GET)
    public String crear (Map<String, Object> model){
        Categoria categoria= new Categoria();
        model.put("categoria",categoria);
        model.put("titulo", "Formulario de Categoria");
        return "categorias/form";
    }

    @GetMapping(value = "/form/{id}")
    public String editar(@PathVariable(value = "id")Long id, Map<String, Object> model, RedirectAttributes flash) {
        Categoria categoria =null;
        if (id > 0) {
            categoria = categoriaService.findOne(id);
            if (categoria == null) {
                flash.addFlashAttribute("error", "El ID de la Categoria no existe en la BBDD!");
                return "redirect:/categoria/listar";
            }
        } else {
            flash.addFlashAttribute("error", "El ID de la Categoria no puede ser cero!");
            return "redirect:/categoria/listar";
        }
        model.put("categoria", categoria);
        model.put("titulo", "Editar Categoria");
        return "categorias/form";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String guardar(@Valid Categoria categoria, BindingResult result, Model model,
                          @RequestParam("file") MultipartFile imagen,
                          RedirectAttributes flash, SessionStatus status) {

        if (result.hasErrors()) {
            model.addAttribute("categoria", categoria);
            model.addAttribute("titulo", "Formulario de Categorias");
            return "categorias/form";
        }
        if (!imagen.isEmpty()) {
            if (categoria.getId() != null && categoria.getId() > 0 && categoria.getImagen() != null && categoria.getImagen().length() > 0) {
                uploadFileService.delete(categoria.getImagen());
            }
            String uniqueFilename = null;
            try {
                uniqueFilename = uploadFileService.copy(imagen);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            flash.addFlashAttribute("info", "Has subido correctamente '" + uniqueFilename + "'");
            categoria.setImagen(uniqueFilename);
        }

        String mensajeFlash = (categoria.getId() != null) ? "Categoria editada con éxito!" : "Categoria creada con éxito!";

        categoriaService.save(categoria);
        status.setComplete();
        flash.addFlashAttribute("success", mensajeFlash);
        return "redirect:/categoria/listar";
    }

    @RequestMapping(value = "/eliminar/{id}")
    public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {

        if (id != null) {
            categoriaService.delete(id);
            flash.addFlashAttribute("success", "Categoria eliminada con éxito!");

        }
        return "redirect:/categoria/listar";
    }

    @GetMapping(value = "/uploads/{filename:.+}")
    public ResponseEntity<Resource> verImagen(@PathVariable String filename) {

        Resource recurso = null;

        try {
            recurso = uploadFileService.load(filename);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
                .body(recurso);
    }
}

