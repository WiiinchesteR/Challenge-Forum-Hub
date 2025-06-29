package forum.hub.api.controller;

import forum.hub.api.domain.curso.CursoRepository;
import forum.hub.api.domain.respostas.*;
import forum.hub.api.domain.topicos.*;
import forum.hub.api.domain.usuario.Usuario;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@RestController
@RequestMapping("/topico")
public class TopicosController {

    @Autowired
    private TopicosRepository topicosRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private RespostaRepository respostaRepository;


    @PostMapping
    @Transactional
    public ResponseEntity enviarTopico(@RequestBody @Valid DadosEnviarTopicos dados, UriComponentsBuilder uriBuilder,
                                       @AuthenticationPrincipal Usuario usuario) {
        var topico = new Topico();
        var curso = cursoRepository.findByNome(String.valueOf(dados.curso()));

        topico.setTitulo(dados.titulo());
        topico.setMensagem(dados.mensagem());
        topico.setCurso(curso.get());
        topico.setAutor(usuario);

        topicosRepository.save(topico);

        var uri = uriBuilder.path("/topico/{id}").buildAndExpand(topico.getId()).toUri();

        var dto = new DadosDetalhamentoTopicos(topico);

        return ResponseEntity.created(uri).body(dto);
    }


    @GetMapping
    public ResponseEntity<Page<DadosListagemTopicos>> listarTopicos(
            @PageableDefault(size = 10, sort = {"titulo"}) Pageable paginacao
    ) {
        var page = topicosRepository.findAll(paginacao).map(DadosListagemTopicos::new);

        return ResponseEntity.ok(page);
    }


    @GetMapping("/{id}")
    public ResponseEntity detalharTopico(@PathVariable Long id) {
        Optional<Topico> topico = topicosRepository.findById(id);

        if (topico.isPresent()) {

            if (topico.get().getStatus().equals(topico.get().getStatus().RESPONDIDO)) {

                return ResponseEntity.ok(new DadosDetalhadoRespostas(topico.get()));

            } else {

                return ResponseEntity.ok(new DadosDetalhamentoTopicos(topico.get()));

            }

        }

        return ResponseEntity.notFound().build();

    }


    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity atualizarTopicos(@RequestBody DadosDetalhamentoTopicos dados, @PathVariable Long id,
                                           @AuthenticationPrincipal Usuario usuario) {
        Optional<Topico> topico = topicosRepository.findById(id);

        if (topico.isPresent()) {

            if (!topico.get().getAutor().getId().equals(usuario.getId())) {

                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para atualizar este tópico.");
            }

            topico.get().atualizarInformacoes(dados);

            return ResponseEntity.ok(new DadosDetalhamentoTopicos(topico.get()));
        }

        return ResponseEntity.notFound().build();

    }


    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluirTopico(@PathVariable Long id, @AuthenticationPrincipal Usuario usuario) {
        Optional<Topico> topico = topicosRepository.findById(id);

        if (topico.isPresent()) {

            if (!topico.get().getAutor().getId().equals(usuario.getId())) {

                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para excluir este tópico.");

            }

            topicosRepository.deleteById(id);

            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();

    }


    @PostMapping("/{id}/responder")
    public ResponseEntity responderTopico(@PathVariable Long id, @RequestBody @Valid DadosResposta dados,
                                          @AuthenticationPrincipal Usuario usuario, UriComponentsBuilder uriBuilder) {

        Optional<Topico> topico = topicosRepository.findById(id);

        if (topico.isPresent()) {

            if (topico.get().getAutor().getId().equals(usuario.getId())) {

                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Você não pode responder o seu próprio tópico.");

            }

            Resposta resposta = new Resposta();

            resposta.setMensagem(dados.mensagem());
            resposta.setAutor(usuario);
            resposta.setTopico(topico.get());

            respostaRepository.save(resposta);

            topico.get().setStatus(StatusResposta.RESPONDIDO);

            topicosRepository.save(topico.get());

            return ResponseEntity.status(HttpStatus.CREATED).body("Resposta registrada com sucesso.");
        }

        return ResponseEntity.notFound().build();

    }

}
