package com.control.app.sts.controller;

import com.control.app.sts.models.entity.Distrito;
import com.control.app.sts.models.service.interfaces.IDistritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class DistritoController {

    @Autowired
    private IDistritoService distritoService;

    @GetMapping(value = "/distritos", produces = { "application/json" })
    public @ResponseBody List<Distrito> cargarDistritos(@RequestParam(value = "id", required = true) Long term) {
        return distritoService.listarPorProvincia(term);
    }
}
