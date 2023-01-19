package com.control.app.sts.models.dao;

import com.control.app.sts.models.entity.Distrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IDistritoDAO extends JpaRepository<Distrito,Long> {

    List<Distrito> findAllByProvincia_Id(Long idProvincia);
}
