package com.control.app.sts.controller;

import com.control.app.sts.models.entity.ItemUsuario;
import com.control.app.sts.models.entity.Producto;
import com.control.app.sts.models.entity.Rol;
import com.control.app.sts.models.entity.Usuario;
import com.control.app.sts.paginator.PageRender;
import com.control.app.sts.models.service.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

@Controller
@SessionAttributes("usuario")
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private IRolService rolService;

    @Autowired
    private IDepartamentoService departamentoService;

    @Autowired
    private IProvinciaService provinciaService;

    @Autowired
    private IDistritoService distritoService;

    @Autowired
    private IProductoService productoService;

    @Autowired
    private IItemUsuarioService itemUsuarioService;


    @Autowired
    private IUploadFileService uploadFileService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping(value = "/listar")
    public String index(@RequestParam(name="page",defaultValue = "0")int page,Model model) {
        Pageable pageRequest = PageRequest.of(page, 10);
        Page<Usuario> usuarios = usuarioService.findAll(pageRequest);
        PageRender<Usuario> pageRender = new PageRender<>("/listar",usuarios);

        model.addAttribute("titulo", "Listado de usuarios");
        model.addAttribute("usuarios", usuarioService.findAll());
        model.addAttribute("page", pageRender);
        return "usuarios/listar";
    }

    @GetMapping(value = "/ver/{id}")
    public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

        Usuario usuario = usuarioService.findOne(id);
        if (usuario == null) {
            flash.addFlashAttribute("error", "El usuario no existe en la base de datos");
            return "redirect:/usuario/listar";
        }
        model.put("roles",rolService.findAll());
        model.put("departamentos",departamentoService.findAll());
        model.put("provincias",provinciaService.findAll());
        model.put("distritos",distritoService.findAll());
        model.put("usuario", usuario);
        model.put("titulo", "Detalle Trabajador: " + usuario.getNombres());
        return "usuarios/ver";
    }

    @GetMapping(value = "/form")
    public String crear(Map<String, Object> model) {
        Usuario usuario= new Usuario();
        model.put("usuario",usuario);
        model.put("titulo", "Registro de usuario");
        model.put("departamentos",departamentoService.findAll());
        model.put("roles", rolService.findAll());
        return "usuarios/form";
    }

    @RequestMapping(value = "/form/{id}")
    public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

        Usuario usuario = null;

        if (id > 0) {
            usuario = usuarioService.findOne(id);
            if (usuario == null) {
                flash.addFlashAttribute("error", "El ID del usuario no existe en la BBDD!");
                return "redirect:/usuario/listar";
            }
        } else {
            flash.addFlashAttribute("error", "El ID del ususario no puede ser cero!");
            return "redirect:/usuario/listar";
        }

        model.put("departamentos",departamentoService.findAll());
        model.put("provincias",provinciaService.findAll());
        model.put("distritos",distritoService.findAll());
        model.put("rol",usuario.getRoles().get(0).getId());
        model.put("roles", rolService.findAll());
        model.put("usuario", usuario);
        model.put("titulo", "Editar Usuario");
        return "usuarios/editar";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String guardar(@Valid Usuario usuario, BindingResult result, Model model,
                          @RequestParam("file") MultipartFile foto,
                          @RequestParam(name = "rol_id", required = false) Long rolId,
                          RedirectAttributes flash,SessionStatus status) {
        if(result.hasErrors()) {
            model.addAttribute("titulo", "Editar Usuario");
            model.addAttribute("roles",usuario.getRoles().get(0).getId());
            model.addAttribute("usuario",usuario);
            model.addAttribute("departamentos",departamentoService.findAll());
            model.addAttribute("provincias",provinciaService.findAll());
            model.addAttribute("distritos",distritoService.findAll());
            model.addAttribute("roles", rolService.findAll());
            return "usuarios/editar";
        }
        if (rolId == null || rolId == 0) {
            model.addAttribute("titulo", "Formulario de Usuarios");
            model.addAttribute("usuario", usuario);
            model.addAttribute("departamentos",departamentoService.findAll());
            model.addAttribute("provincias",provinciaService.findAll());
            model.addAttribute("distritos",distritoService.findAll());
            model.addAttribute("roles", rolService.findAll());
            model.addAttribute("error", "Error: Debe elegir un rol");
            if (usuario.getId() != null && usuario.getId() > 0 ) {
                return "usuarios/editar";
            }else{
                return "usuarios/form";
            }
        }

        if (!foto.isEmpty()) {
            if (usuario.getId() != null && usuario.getId() > 0 && usuario.getFoto() != null && usuario.getFoto().length() > 0) {
                uploadFileService.delete(usuario.getFoto());
            }
            String uniqueFilename = null;
            try {
                uniqueFilename = uploadFileService.copy(foto);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            flash.addFlashAttribute("info", "Has subido correctamente '" + uniqueFilename + "'");
            usuario.setFoto(uniqueFilename);
        }
        if (usuario.getPassword().isEmpty()){
            Usuario usuarioPrev = usuarioService.findOne(usuario.getId());
            usuario.setPassword(usuarioPrev.getPassword());
        }else {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }
        Rol rol= rolService.findOne(rolId);
        usuario.addRol(rol);

        usuarioService.save(usuario);
        status.setComplete();
        return "redirect:/usuario/listar";
    }

    @RequestMapping(value="/eliminar/{id}")
    public String eliminar(@PathVariable(value="id") Long id) {

        if(id > 0) {
            usuarioService.delete(id);
        }
        return "redirect:/usuario/listar";
    }

    @RequestMapping(value = "/perfil")
    public String clientePerfil( Map<String, Object> model, RedirectAttributes flash) {
        Usuario usuario = usuarioService.getUsuario();
        model.put("roles",rolService.findAll());
        model.put("departamentos",departamentoService.findAll());
        model.put("provincias",provinciaService.findAll());
        model.put("distritos",distritoService.findAll());
        model.put("usuario", usuario);
        model.put("titulo", "Editar Perfil");
        return "usuarios/perfil";
    }

    @RequestMapping(value = "/perfil/save", method = RequestMethod.POST)
    public String guardarPerfil(@Valid Usuario usuario, BindingResult result, Model model, SessionStatus status,RedirectAttributes flash) {
        if(result.hasErrors()) {
            model.addAttribute("departamentos",departamentoService.findAll());
            model.addAttribute("provincias",provinciaService.findAll());
            model.addAttribute("distritos",distritoService.findAll());
            model.addAttribute("usuario", usuario);
            model.addAttribute("titulo", "Editar Perfil");
            return "usuarios/perfil";
        }
        if (usuario.getPassword().isEmpty()){
            Usuario usuarioPrev = usuarioService.findOne(usuario.getId());
            usuario.setPassword(usuarioPrev.getPassword());
        }else {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }
        usuarioService.save(usuario);
        status.setComplete();
        flash.addFlashAttribute("success", "Los cambios se han guardado correctamente");
        return "redirect:/perfil";
    }

    @GetMapping(value = "/uploads/{filename:.+}")
    public ResponseEntity<Resource> verFoto(@PathVariable String filename) {

        Resource recurso = null;

        try {
            recurso = uploadFileService.load(filename);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
                .body(recurso);
    }
    @RequestMapping(value = "/asignar/{id}",method = RequestMethod.GET)
    public String asignar(@PathVariable(value = "id") Long id, Map<String, Object> model,
                          RedirectAttributes flash) {

        Usuario usuario = null;

        if (id != null) {
            usuario = usuarioService.findOne(id);
            if (usuario == null) {
                flash.addFlashAttribute("error", "El ID del usuario no existe en la BBDD!");
                return "redirect:/usuario/listar";
            }
        } else {
            flash.addFlashAttribute("error", "El ID del usuario no puede ser cero!");
            return "redirect:/usuario/listar";
        }

        model.put("rol",usuario.getRoles().get(0).getId());
        model.put("usuario", usuario);
        model.put("titulo", "Editar Trabajador");
        model.put("roles", rolService.findAll());
        return "usuarios/asignar";
    }

    @RequestMapping(value = "/asignar", method = RequestMethod.POST)
    public String guardarItemsUsuario(Model model,
                                         @RequestParam(name = "item_id[]", required = false) Long[] itemId,
                                         @RequestParam(name = "cantidad[]", required = false) Integer[] cantidad,
                                         @RequestParam(name = "trabajadorId", required = false) Long trabajadorId,
                                         RedirectAttributes flash, SessionStatus status) {
        Usuario usuario = null;

        if (trabajadorId != null) {
            usuario = usuarioService.findOne(trabajadorId);
            if (usuario == null) {
                flash.addFlashAttribute("error", "El ID del usuario no existe en la BBDD!");
                return "redirect:/usuario/listar";
            }
        } else {
            flash.addFlashAttribute("error", "El ID del usuario no puede ser cero!");
            return "redirect:/usuario/listar";
        }

        if (itemId == null || itemId.length == 0) {
            flash.addFlashAttribute("error", "Error: La lista NO puede no tener líneas!");

            model.addAttribute("usuario", usuario);
            model.addAttribute("titulo", "Formulario de Trabajador");
            return "redirect:/usuario/asignar/"+usuario.getId();
        }
        for (int i = 0; i < itemId.length; i++) {
            ItemUsuario itemUsuario=new ItemUsuario();

            Producto producto= productoService.findOne(itemId[i]);

            if(producto.getStock()>0){
                if((producto.getStock()-cantidad[i])<=0){
                    model.addAttribute("itemUsuario", itemUsuario);
                    model.addAttribute("error", "Error: No queda Stock del producto "+producto.getNombre());
                    return "redirect:/usuario/asignar/"+usuario.getId();
                }else {
                    itemUsuario.setCantidad(cantidad[i]);
                    producto.setStock(producto.getStock()-itemUsuario.getCantidad());
                }
            }
            itemUsuario.setUsuario(usuario);
            itemUsuario.setProducto(producto);

            itemUsuarioService.save(itemUsuario);
        }
        return "redirect:/usuario/listar";
    }

    @GetMapping(value="/cargar-productos/{term}", produces = {"application/json"})
    public @ResponseBody List<Producto> cargarMateriales(@PathVariable String term){

        return productoService.findByNombre(term);
    }

}
