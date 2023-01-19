package com.control.app.sts.models.service.interfaces;

import com.control.app.sts.models.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface IUsuarioService extends UserDetailsService {

    public List<Usuario> findAll();

    public Page<Usuario> findAll(Pageable pageable);

    public void save(Usuario usuario);

    public Usuario findOne(Long id);

    public void delete(Long id);
    public Usuario findByEmail(String email);

    public List<Usuario> findByNombre(String term);
    public Usuario getUsuario();
}
