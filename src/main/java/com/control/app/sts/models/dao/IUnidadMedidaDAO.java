package com.control.app.sts.models.dao;

import com.control.app.sts.models.entity.UnidadMedida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUnidadMedidaDAO extends JpaRepository<UnidadMedida,Long> {
}
