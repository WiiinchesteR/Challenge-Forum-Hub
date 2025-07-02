package forum.hub.api.controller;

import forum.hub.api.domain.respostas.RespostaRepository;
import forum.hub.api.domain.topicos.TopicosRepository;
import forum.hub.api.domain.usuario.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuario")
public class UsuariosController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TopicosRepository topicosRepository;

    @Autowired
    private RespostaRepository respostaRepository;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrarUsuario(
            @RequestBody @Valid DadosCadastroUsuario dados,
            UriComponentsBuilder uriBuilder
            ) {

        if (usuarioRepository.existsByEmail(dados.email())) {

            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Já existe um usuário com esse email.");

        }

        Usuario usuario = new Usuario();

        usuario.setNome(dados.nome());
        usuario.setEmail(dados.email());

        var senhaCriptografada = passwordEncoder.encode(dados.senha());
        usuario.setSenha(senhaCriptografada);

        usuarioRepository.save(usuario);

        var uri = uriBuilder.path("/usuarios/{id}").buildAndExpand(usuario.getId()).toUri();

        var dto = new DadosDetalhamentoUsuario(usuario);

        return ResponseEntity.created(uri).body(dto);

    }


    @GetMapping
    public ResponseEntity<Page<DadosListagemUsuario>> listarUsuarios(
            @PageableDefault(size = 10, sort = {"id"}) Pageable paginacao
    ) {

        Page<Usuario> usuarios = usuarioRepository.findAll(paginacao);

        Page<DadosListagemUsuario> resposta = usuarios
                .map(usuario -> {
                    int enviados = topicosRepository.countByAutor(usuario);
                    int respondidos = respostaRepository.countByAutor(usuario);
                    return new DadosListagemUsuario(usuario, enviados, respondidos);
                });

        return ResponseEntity.ok(resposta);

    }

    @PutMapping("/{id}/senha")
    @Transactional
    public ResponseEntity atualizarSenha(
            @PathVariable Long id,
            @RequestBody @Valid DadosAtualizarSenha dados,
            @AuthenticationPrincipal Usuario usuario
    ) {

        if (!usuario.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Você só pode alterar sua própria senha.");
        }

        Optional<Usuario> usuarioOp = usuarioRepository.findById(id);

        if (usuarioOp.isEmpty()) {

            return ResponseEntity.notFound().build();

        }

        var novaSenhaCriptografada = passwordEncoder.encode(dados.novaSenha());

        usuarioOp.get().setSenha(novaSenhaCriptografada);

        return ResponseEntity.ok("Senha atualizada com sucesso.");

    }


    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluirUsuario(
            @PathVariable Long id,
            @RequestBody @Valid DadosConfirmarSenha dados,
            @AuthenticationPrincipal Usuario usuario
    ) {

        if (!usuario.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Você só pode excluir o seu próprio perfil.");
        }

        Optional<Usuario> usuarioOp = usuarioRepository.findById(id);

        if (usuarioOp.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (!passwordEncoder.matches(dados.senha(), usuarioOp.get().getSenha())) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Senha incorreta. Perfil não foi excluído.");

        }

        usuarioRepository.deleteById(id);

        return ResponseEntity.ok("Perfil excluído com sucesso.");

    }
}
