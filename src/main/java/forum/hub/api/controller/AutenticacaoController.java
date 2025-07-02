package forum.hub.api.controller;

import forum.hub.api.domain.usuario.DadosAutenticacao;
import forum.hub.api.domain.usuario.Usuario;
import forum.hub.api.infra.seguranca.DadosTokenJWT;
import forum.hub.api.infra.seguranca.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity efetuarLogin(@RequestBody @Valid DadosAutenticacao dados) {

        var autenticadorToken = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());
        var autenticador = manager.authenticate(autenticadorToken);

        var tokenJWT = tokenService.gerarToken((Usuario) autenticador.getPrincipal());

        return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));

    }
}