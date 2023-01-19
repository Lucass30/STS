package com.control.app.sts.controller;

import com.control.app.sts.models.entity.Rol;
import com.control.app.sts.models.service.interfaces.IRolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.Valid;
import java.util.Map;

@Controller
@SessionAttributes("rol")
@RequestMapping("/rol")
public class RolController {

    @Autowired
    private IRolService rolService;

    @GetMapping(value = "/listar")
    public String index(Map<String, Object> model) {
        Rol rol= new Rol();
        model.put("rol",rol);
        model.put("titulo", "Listado de roles");
        model.put("roles", rolService.findAll());
        return "roles/listar";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String guardar(@Valid Rol rol, BindingResult result, Model model, SessionStatus status) {
        if(result.hasErrors()) {
            model.addAttribute("rol",rol);
            model.addAttribute("titulo", "Listado de roles");
            model.addAttribute("roles", rolService.findAll());
            return "roles/listar";
        }

        rolService.save(rol);
        status.setComplete();
        return "redirect:/rol/listar";
    }

    @RequestMapping(value="/eliminar/{id}")
    public String eliminar(@PathVariable(value="id") Long id) {

        if(id > 0) {
            rolService.delete(id);
        }
        return "redirect:/rol/listar";
    }
}
