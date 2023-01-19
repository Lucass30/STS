package com.control.app.sts.models.dao;

import com.control.app.sts.models.entity.ItemUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IItemUsuarioDAO extends JpaRepository<ItemUsuario,Long > {
}
