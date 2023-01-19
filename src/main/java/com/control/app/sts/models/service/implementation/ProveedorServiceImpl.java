package com.control.app.sts.models.service.implementation;

import com.control.app.sts.models.dao.IProveedorDAO;
import com.control.app.sts.models.entity.Proveedor;
import com.control.app.sts.models.service.interfaces.IProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProveedorServiceImpl implements IProveedorService {

    @Autowired
    private IProveedorDAO proveedorDAO;

    @Override
    public List<Proveedor> findAll() {
        return (List<Proveedor>) proveedorDAO.findAll();
    }

    @Override
    public Page<Proveedor> findAll(Pageable pageable) {
        return proveedorDAO.findAll(pageable);
    }

    @Override
    public void save(Proveedor proveedor) {
        proveedorDAO.save(proveedor);
    }

    @Override
    public Proveedor findOne(Long id) {
        return proveedorDAO.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        proveedorDAO.deleteById(id);
    }


}
