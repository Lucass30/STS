package com.control.app.sts.controller;

import com.control.app.sts.models.entity.Proveedor;
import com.control.app.sts.models.service.interfaces.IUploadFileService;
import com.control.app.sts.paginator.PageRender;
import com.control.app.sts.models.service.interfaces.IProveedorService;
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
@SessionAttributes("proveedor")
@RequestMapping("/proveedor")
public class ProveedorController {
    @Autowired
    private IProveedorService proveedorService;

    @Autowired
    private IUploadFileService uploadFileService;


    @RequestMapping(value={"/listar"}, method = RequestMethod.GET)
    public String listar(@RequestParam(name="page",defaultValue = "0")int page, Model model){
        Pageable pageRequest = PageRequest.of(page, 10);
        Page<Proveedor> proveedor = proveedorService.findAll(pageRequest);
        PageRender<Proveedor> pageRender = new PageRender<>("/listar",proveedor);
        model.addAttribute("titulo", "Listado de Proveedores");
        model.addAttribute("proveedores", proveedor);
        model.addAttribute("page", pageRender);
        return "proveedores/listar";
    }

    @GetMapping(value = "/ver/{id}")
    public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

        Proveedor proveedor = proveedorService.findOne(id);
        if (proveedor == null) {
            flash.addFlashAttribute("error", "El proveedor no existe en la base de datos");
            return "redirect:/proveedor/listar";
        }

        model.put("proveedor", proveedor);
        model.put("titulo", "Detalle proveedor: " + proveedor.getNombre());
        return "proveedores/ver";
    }
    @RequestMapping(value="/form", method = RequestMethod.GET)
    public String crear (Map<String, Object> model){
        Proveedor proveedor= new Proveedor();
        //model.put("tipo",tipoService.findAll());
        model.put("proveedores",proveedor);
        model.put("titulo", "Formulario de Proveedores");
        return "proveedores/form";
    }

    @RequestMapping(value={"/form/{id}"})
    public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash){
        Proveedor proveedor= null;
        if(id>0){
            proveedor=proveedorService.findOne(id);
            if (proveedor== null){
                flash.addFlashAttribute("error", "El ID del proveedor no existe en la BBDD!");
                return "redirect:/proveedores/listar";
            }
        } else {
            flash.addFlashAttribute("error", "El ID del proveedor no puede ser cero!");
            return "redirect:/proveedores/listar";
        }
        model.put("proveedores", proveedor);
        model.put("titulo", "Editar Proveedor");
        return "proveedores/form";
    }

    @RequestMapping(value="/save", method = RequestMethod.POST)
    public String guardar(@Valid Proveedor proveedor, BindingResult result, Model model,
                          @RequestParam("file") MultipartFile logo,
                          RedirectAttributes flash, SessionStatus status){
        if (result.hasErrors()){
            model.addAttribute("proveedores",proveedor);
            model.addAttribute("titulo","Registro de Proveedor");
            return "redirect:/proveedor/form";
        }
        if (!logo.isEmpty()) {
            if (proveedor.getId() != null && proveedor.getId() > 0 && proveedor.getLogo() != null && proveedor.getLogo().length() > 0) {
                uploadFileService.delete(proveedor.getLogo());
            }
            String uniqueFilename = null;
            try {
                uniqueFilename = uploadFileService.copy(logo);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            flash.addFlashAttribute("info", "Has subido correctamente '" + uniqueFilename + "'");
            proveedor.setLogo(uniqueFilename);
        }
        String mensajeFlash = (proveedor.getId() != null) ? "Proveedor editado con éxito!" : "Proveedor creado con éxito!";

        proveedorService.save(proveedor);
        status.setComplete();
        flash.addFlashAttribute("success", mensajeFlash);
        return "redirect:/proveedor/listar";
    }

    @RequestMapping(value = "/eliminar/{id}")
    public String eliminar(@PathVariable(value="id") Long id){
        if(id > 0) {
            proveedorService.delete(id);
        }
        return "redirect:/proveedor/listar";
    }

    @GetMapping(value = "/uploads/{filename:.+}")
    public ResponseEntity<Resource> verLogo(@PathVariable String filename) {

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
