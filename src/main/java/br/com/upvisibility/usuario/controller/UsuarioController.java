package br.com.upvisibility.usuario.controller;

import br.com.upvisibility.usuario.business.UsuarioService;
import br.com.upvisibility.usuario.business.dto.EnderecoDTO;
import br.com.upvisibility.usuario.business.dto.TelefoneDTO;
import br.com.upvisibility.usuario.business.dto.UsuarioDTO;
import br.com.upvisibility.usuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<UsuarioDTO> salvar(@RequestBody UsuarioDTO request) {
       return ResponseEntity.ok(usuarioService.salvar(request));
    }

    @PostMapping("/login")
    public String login(@RequestBody UsuarioDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
        );

        return "Bearer " + jwtUtil.generateToken(authentication.getName());
    }

    @GetMapping
    public ResponseEntity<UsuarioDTO> buscarUsuarioPorEmail(@RequestParam("email") String email) {
        return ResponseEntity.ok(usuarioService.buscarUsuarioPorEmail(email));
    }

    @PutMapping
    public ResponseEntity<UsuarioDTO> atualizaDadosUsuario(
            @RequestBody UsuarioDTO request,
            @RequestHeader("Authorization") String token
            ) {
        return ResponseEntity.ok(usuarioService.atualizaDadosUsuarios(token, request));
    }


    @PutMapping("/endereco")
    public ResponseEntity<EnderecoDTO> atualizaEndereco(
            @RequestBody EnderecoDTO request,
            @RequestParam("id") Long id
    ) {
        return ResponseEntity.ok(usuarioService.atualizaEndereco(id, request));
    }

    @PutMapping("/telefone")
    public ResponseEntity<TelefoneDTO> atualizaTelefone(
            @RequestBody TelefoneDTO request,
            @RequestParam("id") Long id
    ) {
        return ResponseEntity.ok(usuarioService.atualizaTelefone(id, request));
    }


    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deletarUsuarioPorEmail(@PathVariable("email") String email) {
        usuarioService.deletarUsuarioPorEmail(email);
        return ResponseEntity.noContent().build();
    }
}
