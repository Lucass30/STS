package com.control.app.sts.models.service.implementation;

import com.control.app.sts.models.dao.IDepartamentoDAO;
import com.control.app.sts.models.entity.Departamento;
import com.control.app.sts.models.service.interfaces.IDepartamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartamentoServiceImple implements IDepartamentoService {

    @Autowired
    private IDepartamentoDAO departamentoDAO;

    @Override
    public List<Departamento> findAll() {
        return (List<Departamento>) departamentoDAO.findAll();
    }

    @Override
    public void save(Departamento departamento) {
        departamentoDAO.save(departamento);
    }

    @Override
    public Departamento findOne(Long id) {
        return departamentoDAO.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        departamentoDAO.deleteById(id);
    }
}
