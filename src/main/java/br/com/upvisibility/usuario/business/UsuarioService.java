package br.com.upvisibility.usuario.business;

import br.com.upvisibility.usuario.business.converter.UsuarioConverter;
import br.com.upvisibility.usuario.business.dto.UsuarioDTO;
import br.com.upvisibility.usuario.infrastructure.entity.Usuario;
import br.com.upvisibility.usuario.infrastructure.exceptions.ConflictException;
import br.com.upvisibility.usuario.infrastructure.exceptions.ResourceNotFoundException;
import br.com.upvisibility.usuario.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;

    public UsuarioDTO salvar(UsuarioDTO usuarioDTO) {
        emailExiste(usuarioDTO.getEmail());
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        usuario = usuarioRepository.save(usuario);
        return usuarioConverter.paraUsuarioDTO(usuario);
    }

    public void emailExiste(String email) {
        try {
            boolean existe = verificaEmailExistente(email);
            if (existe) {
                throw new ConflictException("Email já cadastrado " + email);
            }
        } catch (ConflictException e) {
            throw new ConflictException("Email já cadastrado " + e.getCause());
        }
    }

    public boolean verificaEmailExistente(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public Usuario buscarUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("Usuário não encontrado com o email: " + email
        ));
    }

    public void deletarUsuarioPorEmail(String email) {
        usuarioRepository.deleteByEmail(email);
    }


}
