package forum.hub.api.controller;

import forum.hub.api.topicos.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@RestController
@RequestMapping("/topicos")
public class TopicosController {

    @Autowired
    private TopicosRepository topicosRepository;


    @PostMapping
    @Transactional
    public ResponseEntity enviarTopico(@RequestBody @Valid DadosEnviarTopicos dados, UriComponentsBuilder uriBuilder) {
        var topicos = new Topicos(dados);

        topicosRepository.save(topicos);

        var uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topicos.getId()).toUri();

        var dto = new DadosDetalhamentoTopicos(topicos);

        return ResponseEntity.created(uri).body(dto);
    }


    @GetMapping
    public ResponseEntity<Page<DadosListagemTopicos>> listarTopicos(
            @PageableDefault(size = 10, sort = {"autor"}) Pageable paginacao
    ) {
        var page = topicosRepository.findAll(paginacao).map(DadosListagemTopicos::new);

        return ResponseEntity.ok(page);
    }


    @GetMapping("/{id}")
    public ResponseEntity detalharTopico(@PathVariable Long id) {
        Optional<Topicos> topico = topicosRepository.findById(id);

        if (topico.isPresent()) {

            return ResponseEntity.ok(new DadosDetalhamentoTopicos(topico.get()));

        }

        return ResponseEntity.notFound().build();

    }


    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity atualizarTopicos(@RequestBody DadosDetalhamentoTopicos dados, @PathVariable Long id) {
        Optional<Topicos> topico = topicosRepository.findById(id);

        if (topico.isPresent()) {
            topico.get().atualizarInformacoes(dados);

            return ResponseEntity.ok(new DadosDetalhamentoTopicos(topico.get()));
        }

        return ResponseEntity.notFound().build();

    }


    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluirTopico(@PathVariable Long id) {
        Optional<Topicos> topicos = topicosRepository.findById(id);

        if (topicos.isPresent()) {
            topicosRepository.deleteById(id);

            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();

    }

}
