package com.control.app.sts.models.service.implementation;

import com.control.app.sts.models.dao.IProvinciaDAO;
import com.control.app.sts.models.entity.Provincia;
import com.control.app.sts.models.service.interfaces.IProvinciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProvinciaServiceImpl implements IProvinciaService {

    @Autowired
    private IProvinciaDAO provinciaDAO;

    @Override
    public List<Provincia> findAll() {
        return (List<Provincia>) provinciaDAO.findAll();
    }

    @Override
    public void save(Provincia provincia) {
        provinciaDAO.save(provincia);
    }

    @Override
    public Provincia findOne(Long id) {
        return provinciaDAO.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        provinciaDAO.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Provincia> listarPorDepartamento(Long idDepartmento) {
        return provinciaDAO.findAllByDepartamento_Id(idDepartmento);
    }
}
