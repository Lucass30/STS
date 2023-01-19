package com.control.app.sts.models.service.implementation;

import com.control.app.sts.models.dao.IOrdenCompraDAO;
import com.control.app.sts.models.entity.OrdenCompra;
import com.control.app.sts.models.service.interfaces.IOrdenCompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IOrdenCompraServiceImpl implements IOrdenCompraService {

    @Autowired
    private IOrdenCompraDAO ordenCompraDAO;

    @Override
    public List<OrdenCompra> findAll() {
        return (List<OrdenCompra>) ordenCompraDAO.findAll();
    }

    @Override
    public Page<OrdenCompra> findAll(Pageable pageable) {
        return ordenCompraDAO.findAll(pageable);
    }

    @Override
    public void save(OrdenCompra ordenCompra) {
        ordenCompraDAO.save(ordenCompra);
    }

    @Override
    public OrdenCompra findOne(Long id) {
        return ordenCompraDAO.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        ordenCompraDAO.deleteById(id);
    }

    @Override
    public OrdenCompra findOrdenCompraById(Long id) {
        return ordenCompraDAO.findById(id).orElse(null);
    }

    @Override
    public List<OrdenCompra> findByProveedor(Long id) {
        return (List<OrdenCompra>) ordenCompraDAO.findOrdenCompraByProveedor_Id(id);
    }
}
