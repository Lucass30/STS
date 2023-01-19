package com.control.app.sts.models.dao;

import com.control.app.sts.models.entity.Provincia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProvinciaDAO extends JpaRepository<Provincia,Long> {
    List<Provincia> findAllByDepartamento_Id(Long idDepartamento);
}
