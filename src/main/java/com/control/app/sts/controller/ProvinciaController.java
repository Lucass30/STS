package com.control.app.sts.controller;

import com.control.app.sts.models.entity.Provincia;
import com.control.app.sts.models.service.interfaces.IProvinciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ProvinciaController {

    @Autowired
    private IProvinciaService provinciaService;

    @GetMapping(value = "/provincias", produces = { "application/json" })
    public @ResponseBody List<Provincia> cargarProvincias(@RequestParam(value = "id", required = true) Long term) {
        return provinciaService.listarPorDepartamento(term);
    }
}
