package com.control.app.sts.models.service.implementation;

import com.control.app.sts.models.dao.IUnidadMedidaDAO;
import com.control.app.sts.models.entity.UnidadMedida;
import com.control.app.sts.models.service.interfaces.IUnidadMedidaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UnidadMedidaServiceImpl implements IUnidadMedidaService {
    @Autowired
    private IUnidadMedidaDAO unidadMedidaDAO;

    @Override
    @Transactional(readOnly = true)
    public List<UnidadMedida> findAll() {
        return (List<UnidadMedida>) unidadMedidaDAO.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UnidadMedida> findAll(Pageable pageable) {
        return unidadMedidaDAO.findAll(pageable);
    }

    @Override
    @Transactional
    public void save(UnidadMedida unidadmedida) {
        unidadMedidaDAO.save(unidadmedida);
    }

    @Override
    @Transactional(readOnly = true)
    public UnidadMedida findOne(Long id) {
        return unidadMedidaDAO.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        unidadMedidaDAO.deleteById(id);
    }
}

