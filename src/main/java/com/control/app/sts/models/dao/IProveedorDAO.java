package com.control.app.sts.models.dao;

import com.control.app.sts.models.entity.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProveedorDAO extends JpaRepository<Proveedor,Long> {
}
