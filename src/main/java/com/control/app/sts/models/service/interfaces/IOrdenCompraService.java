package com.control.app.sts.models.service.interfaces;

import com.control.app.sts.models.entity.OrdenCompra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IOrdenCompraService {
    public List<OrdenCompra> findAll();

    public Page<OrdenCompra> findAll(Pageable pageable);

    public void save (OrdenCompra ordenCompra);

    public OrdenCompra findOne(Long id);

    public void delete(Long id);

    public OrdenCompra findOrdenCompraById(Long id);

    public List<OrdenCompra> findByProveedor(Long id);
}
