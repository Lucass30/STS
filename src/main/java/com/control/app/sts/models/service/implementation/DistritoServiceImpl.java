package com.control.app.sts.models.service.implementation;

import com.control.app.sts.models.dao.IDistritoDAO;
import com.control.app.sts.models.entity.Distrito;
import com.control.app.sts.models.service.interfaces.IDistritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DistritoServiceImpl implements IDistritoService {

    @Autowired
    private IDistritoDAO distritoDAO;

    @Override
    public List<Distrito> findAll() {
        return (List<Distrito>) distritoDAO.findAll();
    }

    @Override
    public void save(Distrito distrito) {
        distritoDAO.save(distrito);
    }

    @Override
    public Distrito findOne(Long id) {
        return distritoDAO.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        distritoDAO.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Distrito> listarPorProvincia(Long idProvincia) {
        return distritoDAO.findAllByProvincia_Id(idProvincia);
    }
}
