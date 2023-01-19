package com.control.app.sts.controller;

import com.control.app.sts.models.entity.Producto;
import com.control.app.sts.models.entity.Tarea;
import com.control.app.sts.models.entity.Usuario;
import com.control.app.sts.models.service.interfaces.ITareaService;
import com.control.app.sts.models.service.interfaces.IUsuarioService;
import com.control.app.sts.paginator.PageRender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@SessionAttributes("tarea")
@RequestMapping(value="/tarea")
public class TareaController {

    @Autowired
    private IUsuarioService usuarioService;


    @Autowired
    private ITareaService tareaService;


   /* @Autowired
    private IItemTareaService itemTareaService;*/

    private final Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping(value="/ver/{id}")
    public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
        Tarea tarea =tareaService.findOne(id);
        if(tarea == null) {
            model.put("usuario",usuarioService.findAll());
            flash.addFlashAttribute("error", "La tarea no existe en la base de datos");
            return "redirect:tarea/listar";
        }
        model.put("usuario",usuarioService.findAll());
        model.put("tarea", tarea);
        model.put("titulo", "Detalle Tarea: "+ tarea.getDescripcion());
        return "tareas/ver";
    }

    @GetMapping(value = "/cargar-usuarios/{term}", produces = { "application/json" })
    public @ResponseBody List<Usuario> cargarUsuarios(@PathVariable String term) {

        return usuarioService.findByNombre(term);
    }

    @RequestMapping(value= "/form", method = RequestMethod.GET)
    public String crear(Map<String, Object> model) {

        Tarea tarea = new Tarea();

        model.put("usuario",usuarioService.findAll());
        model.put("tarea", tarea);
        model.put("titulo", "Formulario de Tarea");
        return "tareas/form";
    }

    @RequestMapping(value="/listar", method = RequestMethod.GET)
    public String listar(@RequestParam(name="page",defaultValue = "0")int page, Model model) {
        Pageable pageRequest = PageRequest.of(page, 10);
        Page<Tarea> tarea = tareaService.findAll(pageRequest);
        PageRender<Tarea> pageRender = new PageRender<>("/listar",tarea);
        model.addAttribute("titulo", "Listado de Tareas");
        model.addAttribute("tarea", tarea);
        model.addAttribute("page", pageRender);
        return "tareas/listar";
    }

    @GetMapping(value = "/form/{id}")
    public String editar(@PathVariable(value = "id")Long id,Map<String, Object> model,RedirectAttributes flash) {

        Tarea tarea =null;
        if (id > 0) {
            tarea = tareaService.findOne(id);
            if (tarea == null) {
                model.put("usuario",usuarioService.findAll());
                flash.addFlashAttribute("error", "El ID de la tarea no existe en la BBDD!");
                return "redirect:/tarea/listar";
            }
        } else {
            flash.addFlashAttribute("error", "El ID de la tarea no puede ser cero!");
            return "redirect:/tarea/listar";
        }

        model.put("tarea", tarea);
        model.put("usuario",usuarioService.findAll());
        model.put("titulo", "Editar Tarea");
        return "tareas/editar";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String guardar(@Valid Tarea tarea, BindingResult result, Model model,
                          RedirectAttributes flash, SessionStatus status) {

        Usuario usuario = usuarioService.findOne(tarea.getUsuario().getId());
        if (result.hasErrors()) {
            model.addAttribute("tarea",tarea);
            model.addAttribute("usuario",usuarioService.findAll());
            model.addAttribute("titulo", "Formulario de Tarea");
            return "tareas/form";
        }
        /*String mensajeFlash = "";
        if(tarea.getId() != null && tarea.getId()>0) {
            mensajeFlash= "Tarea editada con éxito!";
        }else{
            mensajeFlash= "Tarea creada con éxito!";
        }

        if (itemId == null || itemId.length == 0) {
            model.addAttribute("titulo", "Crear Tarea");
            model.addAttribute("error", "Error: La Tarea NO puede no tener líneas!");
            return "tareas/form";
        }
        for (int i = 0; i < itemId.length; i++) {
            Usuario usuario= usuarioService.findOne(itemId[i]);

            ItemTarea itemTarea=new ItemTarea();
            itemTarea.setUsuario(usuario);
            tarea.addItemTarea(itemTarea);
            log.info("ID: " + itemId[i].toString());
        }*/

        if(tarea.getUsuario().getCount()==2){
            if(tarea.getEstado().compareTo("Proceso")==0){
                model.addAttribute("tarea",tarea);
                model.addAttribute("usuario",usuarioService.findAll());
                model.addAttribute("error", "Error: El usuario Seleccionado esta ocupado con otras tareas!");
                return "tareas/form";
            }else if(tarea.getEstado().compareTo("Culminado")==0){
                usuario.setCount((tarea.getUsuario().getCount()-1));
                usuario.setEstado("Disponible");
            }
        }else{
            if (tarea.getUsuario().getCount()>=0){
                if(tarea.getEstado().compareTo("Proceso")==0){
                    usuario.setCount((tarea.getUsuario().getCount()+1));
                    if(tarea.getUsuario().getCount()==2){
                        usuario.setEstado("Ocupado");
                    }
                }
            }
        }

        String mensajeFlash = (tarea.getId() != null) ? "Tarea editada con éxito!" : "Tarea creado con éxito!";
        System.out.println(tarea.getUsuario().getEstado());
        System.out.println(tarea.getUsuario().getCount());
        tareaService.save(tarea);
        status.setComplete();
        flash.addFlashAttribute("success", mensajeFlash);
        return "redirect:/tarea/listar";
    }


    @RequestMapping(value = "/eliminar/{id}")
    public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {

        if (id != null) {
            tareaService.delete(id);
            flash.addFlashAttribute("success", "Tarea eliminado con éxito!");

        }
        return "redirect:/tarea/listar";
    }


}
