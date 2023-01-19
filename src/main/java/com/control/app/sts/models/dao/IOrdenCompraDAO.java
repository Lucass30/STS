package com.control.app.sts.models.dao;

import com.control.app.sts.models.entity.OrdenCompra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IOrdenCompraDAO extends JpaRepository<OrdenCompra,Long> {

    public List<OrdenCompra> findOrdenCompraByProveedor_Id(Long id);

}
