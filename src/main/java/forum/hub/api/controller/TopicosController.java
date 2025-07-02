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
    public ResponseEntity enviarTopico(
            @RequestBody @Valid DadosEnviarTopicos dados,
            UriComponentsBuilder uriBuilder,
            @AuthenticationPrincipal Usuario usuario
    ) {

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
            @PageableDefault(size = 10, sort = {"id"}) Pageable paginacao
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

                return ResponseEntity.ok(new DadosDetalhadoRespostas(topico.get()));

            }

        }

        return ResponseEntity.notFound().build();

    }


    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity atualizarTopicos(
            @RequestBody DadosAtualizarTopicos dados,
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario usuario
    ) {

        Optional<Topico> topico = topicosRepository.findById(id);

        if (topico.isPresent()) {

            if (!topico.get().getAutor().getId().equals(usuario.getId())) {

                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para atualizar este tópico.");
            }

            topico.get().atualizarInformacoes(dados);

            if (dados.curso() != null) {

                var curso = cursoRepository.findByNome(dados.curso());

                topico.get().setCurso(curso.get());

            }

            return ResponseEntity.ok(new DadosDetalhamentoTopicos(topico.get()));
        }

        return ResponseEntity.notFound().build();

    }


    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluirTopico(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario usuario
    ) {

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


    @PutMapping("/{topicoId}/resposta/{respostaId}")
    @Transactional
    public ResponseEntity atualizarRespostas(
            @PathVariable Long topicoId,
            @PathVariable Long respostaId,
            @RequestBody @Valid DadosAtualizarResposta dados,
            @AuthenticationPrincipal Usuario usuario
    ) {

        Optional<Topico> topico = topicosRepository.findById(topicoId);
        Optional<Resposta> resposta = respostaRepository.findById(respostaId);

        if (topico.isEmpty() || resposta.isEmpty()) {

            return ResponseEntity.notFound().build();

        }

        if (!resposta.get().getTopico().getId().equals(topico.get().getId())) {

            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("A resposta não pertence ao tópico informado.");

        }

        if (!resposta.get().getAutor().getId().equals(usuario.getId())) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Você não tem permissão para atualizar esta resposta.");

        }

        resposta.get().setMensagem(dados.mensagem());

        return ResponseEntity.ok("Resposta atualizada com sucesso.");

    }


    @DeleteMapping("/{topicoId}/resposta/{respostaId}")
    @Transactional
    public ResponseEntity excluirRespostaDoTopico(
            @PathVariable Long topicoId,
            @PathVariable Long respostaId,
            @AuthenticationPrincipal Usuario usuario
    ) {

        Optional<Topico> topico = topicosRepository.findById(topicoId);
        Optional<Resposta> resposta = respostaRepository.findById(respostaId);

        if (topico.isEmpty() || resposta.isEmpty()) {

            return ResponseEntity.notFound().build();

        }

        if (!resposta.get().getTopico().getId().equals(topico.get().getId())) {

            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("A resposta não pertence ao tópico informado.");

        }

        if (!resposta.get().getAutor().getId().equals(usuario.getId())) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Você não tem permissão para excluir esta resposta.");

        }

        respostaRepository.deleteById(respostaId);

        if (respostaRepository.findAllByTopicoId(topicoId).isEmpty()) {

            topico.get().setStatus(StatusResposta.NAO_RESPONDIDO);

            topicosRepository.save(topico.get());

        }

        return ResponseEntity.ok("Resposta excluida com sucesso.");

    }

}
