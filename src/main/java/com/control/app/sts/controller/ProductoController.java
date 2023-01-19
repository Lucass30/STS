package com.control.app.sts.controller;

import com.control.app.sts.models.entity.Producto;
import com.control.app.sts.models.service.interfaces.*;
import com.control.app.sts.paginator.PageRender;
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
@SessionAttributes("producto")
@RequestMapping("/producto")
public class ProductoController {
    @Autowired
    private IProductoService productoService;

    @Autowired
    private ICategoriaService categoriaService;

    @Autowired
    private ISubCategoriaService subcategoriaService;

    @Autowired
    private IUnidadMedidaService unidadMedidaService;
    @Autowired
    private IUploadFileService uploadFileService;

    @Autowired
    private IProveedorService proveedorService;

    @RequestMapping(value={"/listar"}, method = RequestMethod.GET)
    public String listar(@RequestParam(name="page",defaultValue = "0")int page, Model model){
        Pageable pageRequest = PageRequest.of(page, 10);
        Page<Producto> productos = productoService.findAll(pageRequest);
        PageRender<Producto> pageRender = new PageRender<>("/listar",productos);
        model.addAttribute("titulo", "Listado de Productos");
        model.addAttribute("productos", productos);
        model.addAttribute("page", pageRender);
        return "productos/listar";
    }
    @RequestMapping(value="/form", method = RequestMethod.GET)
    public String crear (Map<String, Object> model){
        Producto producto= new Producto();
        model.put("producto",producto);
        model.put("titulo", "Formulario de Productos");
        model.put("proveedor",proveedorService.findAll());
        model.put("unidadmedida",unidadMedidaService.findAll());
        model.put("categoria",categoriaService.findAll());
        model.put("subcategoria",subcategoriaService.findAll());
        return "productos/form";
    }

    @RequestMapping(value={"/form/{id}"})
    public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash){
        Producto producto= null;
        if(id>0){
            producto=productoService.findOne(id);
            if (producto== null){
                model.put("categoria",categoriaService.findAll());
                model.put("subcategoria",subcategoriaService.findAll());
                flash.addFlashAttribute("error", "El ID del producto no existe en la BBDD!");
                return "redirect:/producto/listar";
            }
        } else {
            flash.addFlashAttribute("error", "El ID del producto no puede ser cero!");
            return "redirect:/producto/listar";
        }
        model.put("producto", producto);
        model.put("categoria",categoriaService.findAll());
        model.put("proveedor",proveedorService.findAll());
        model.put("unidadmedida",unidadMedidaService.findAll());
        model.put("subcategoria",subcategoriaService.findAll());
        model.put("titulo", "Editar Producto");
        return "productos/editar";
    }

    @RequestMapping(value="/save", method = RequestMethod.POST)
    public String guardar(@Valid Producto producto, BindingResult result, Model model,
                          @RequestParam("file") MultipartFile imagen,
                          RedirectAttributes flash, SessionStatus status){
        System.out.println(producto.getCategoria());
        if (result.hasErrors()){
            model.addAttribute("producto",producto);
            model.addAttribute("categoria",categoriaService.findAll());
            model.addAttribute("proveedor",proveedorService.findAll());
            model.addAttribute("unidadmedida",unidadMedidaService.findAll());
            model.addAttribute("subcategoria",subcategoriaService.findAll());
            model.addAttribute("titulo","Registro de Productos");
            return "redirect:/producto/form";
        }
        if (!imagen.isEmpty()) {

            if (producto.getId() != null && producto.getId() > 0 && producto.getImagen() != null
                    && producto.getImagen().length() > 0) {

                uploadFileService.delete(producto.getImagen());
            }

            String uniqueFilename = null;
            try {
                uniqueFilename = uploadFileService.copy(imagen);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            flash.addFlashAttribute("info", "Has subido correctamente '" + uniqueFilename + "'");

            producto.setImagen(uniqueFilename);
        }
        String mensajeFlash = (producto.getId() != null) ? "Producto editado con éxito!" : "Producto creado con éxito!";
        System.out.println();
        productoService.save(producto);
        status.setComplete();
        flash.addFlashAttribute("success", mensajeFlash);
        return "redirect:/producto/listar";
    }

    @RequestMapping(value = "/eliminar/{id}")
    public String eliminar(@PathVariable(value="id") Long id){
        if(id > 0) {
            productoService.delete(id);
        }
        return "redirect:/producto/listar";
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
    @GetMapping(value = "/ver/{id}")
    public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

        Producto producto = productoService.findOne(id);
        if (producto == null) {
            flash.addFlashAttribute("error", "El Producto no existe en la base de datos");
            return "redirect:/producto/listar";
        }

        model.put("producto", producto);
        model.put("titulo", "Detalle Producto: " + producto.getNombre());
        return "productos/ver";
    }

    @RequestMapping(value = "/entregar/{id}")
    public String entregar(@PathVariable(value="id") Long id){

        Producto producto =  productoService.findOne(id);
        if(id > 0) {
            producto.setStock(producto.getStock()+1);
        }
        return "redirect:/usuario/listar";
    }

}
