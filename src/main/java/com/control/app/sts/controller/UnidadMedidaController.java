package com.control.app.sts.controller;

import com.control.app.sts.models.entity.UnidadMedida;
import com.control.app.sts.models.service.interfaces.IUnidadMedidaService;
import com.control.app.sts.models.service.interfaces.IUploadFileService;
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
@SessionAttributes("unidadmedida")
@RequestMapping("/unidadmedida")
public class UnidadMedidaController {
    @Autowired
    private IUnidadMedidaService unidadMedidaService;

    @Autowired
    private IUploadFileService uploadFileService;

    @RequestMapping(value={"/listar"}, method = RequestMethod.GET)
    public String listar(@RequestParam(name="page",defaultValue = "0")int page, Model model){
        Pageable pageRequest = PageRequest.of(page, 10);
        Page<UnidadMedida> unidadmedidas = unidadMedidaService.findAll(pageRequest);
        PageRender<UnidadMedida> pageRender = new PageRender<>("/listar",unidadmedidas);

        model.addAttribute("titulo", "Listado de unidadmedidas");
        model.addAttribute("unidadmedida",unidadMedidaService.findAll());
        model.addAttribute("page", pageRender);
        return "unidadmedidas/listar";
    }
    @RequestMapping(value="/form", method = RequestMethod.GET)
    public String crear (Map<String, Object> model){
        UnidadMedida unidadmedida= new UnidadMedida();
        model.put("unidadmedida",unidadmedida);
        model.put("titulo", "Formulario de unidadmedida");
        return "unidadmedidas/form";
    }

    @GetMapping(value = "/form/{id}")
    public String editar(@PathVariable(value = "id")Long id, Map<String, Object> model, RedirectAttributes flash) {
        UnidadMedida unidadmedida =null;
        if (id > 0) {
            unidadmedida = unidadMedidaService.findOne(id);
            if (unidadmedida == null) {
                flash.addFlashAttribute("error", "El ID de la unidadmedida no existe en la BBDD!");
                return "redirect:/unidadmedida/listar";
            }
        } else {
            flash.addFlashAttribute("error", "El ID de la unidadmedida no puede ser cero!");
            return "redirect:/unidadmedida/listar";
        }
        model.put("unidadmedida", unidadmedida);
        model.put("titulo", "Editar unidadmedida");
        return "unidadmedidas/form";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String guardar(@Valid UnidadMedida unidadmedida, BindingResult result, Model model,
                          @RequestParam("file") MultipartFile imagen,
                          RedirectAttributes flash, SessionStatus status) {

        if (result.hasErrors()) {
            model.addAttribute("unidadmedida", unidadmedida);
            model.addAttribute("titulo", "Formulario de unidadmedidas");
            return "unidadmedidas/form";
        }
        if (!imagen.isEmpty()) {
            if (unidadmedida.getId() != null && unidadmedida.getId() > 0 && unidadmedida.getImagen() != null && unidadmedida.getImagen().length() > 0) {
                uploadFileService.delete(unidadmedida.getImagen());
            }
            String uniqueFilename = null;
            try {
                uniqueFilename = uploadFileService.copy(imagen);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            flash.addFlashAttribute("info", "Has subido correctamente '" + uniqueFilename + "'");
            unidadmedida.setImagen(uniqueFilename);
        }

        String mensajeFlash = (unidadmedida.getId() != null) ? "Unidad de Medida editada con éxito!" : "unidadmedida creada con éxito!";

        unidadMedidaService.save(unidadmedida);
        status.setComplete();
        flash.addFlashAttribute("success", mensajeFlash);
        return "redirect:/unidadmedida/listar";
    }

    @RequestMapping(value = "/eliminar/{id}")
    public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {

        if (id != null) {
            unidadMedidaService.delete(id);
            flash.addFlashAttribute("success", "unidadmedida eliminada con éxito!");

        }
        return "redirect:/unidadmedida/listar";
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
