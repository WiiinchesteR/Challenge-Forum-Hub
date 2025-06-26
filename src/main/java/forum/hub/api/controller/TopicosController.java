package forum.hub.api.controller;

import forum.hub.api.domain.topicos.DadosEnviarTopico;
import forum.hub.api.domain.topicos.DadosListagemTopicos;
import forum.hub.api.domain.topicos.Topicos;
import forum.hub.api.domain.topicos.TopicosRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/topicos")
public class TopicosController {

    @Autowired
    private TopicosRepository topicosRepository;

    @PostMapping
    @Transactional
    public void enviarTopico(@RequestBody @Valid DadosEnviarTopico dados) {
        var topicos = new Topicos(dados);
        topicosRepository.save(topicos);
    }

    @GetMapping
    public Page<DadosListagemTopicos> listarTopicos(@PageableDefault(size = 10, sort = {"autor"}) Pageable paginacao) {
        return topicosRepository.findAll(paginacao).map(DadosListagemTopicos::new);
    }
}
