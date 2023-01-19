package com.control.app.sts.models.service.interfaces;

import com.control.app.sts.models.entity.Departamento;

import java.util.List;

public interface IDepartamentoService {

    public List<Departamento> findAll();

    public void save(Departamento departamento);

    public Departamento findOne(Long id);

    public void delete(Long id);
}
