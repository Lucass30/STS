package com.control.app.sts.models.dao;

import com.control.app.sts.models.entity.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDepartamentoDAO extends JpaRepository<Departamento,Long> {
}
