package forum.hub.api.controller;

import forum.hub.api.domain.respostas.*;
import forum.hub.api.domain.topicos.Topico;
import forum.hub.api.domain.topicos.TopicosRepository;
import forum.hub.api.domain.usuario.Usuario;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/respostas")
public class RespostasController {

    @Autowired
    private RespostaRepository respostaRepository;

    @Autowired
    private TopicosRepository topicosRepository;

    @PostMapping("/topico/{id}")
    @Transactional
    public ResponseEntity responderTopico(
            @PathVariable Long id,
            @RequestBody @Valid DadosResposta dados,
            @AuthenticationPrincipal Usuario usuario
            ) {

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

    @GetMapping
    public ResponseEntity listarRespostas() {


        List<DadosListagemRespostas> respostas = respostaRepository.findAll()
                .stream()
                .map(DadosListagemRespostas::new)
                .toList();

        return ResponseEntity.ok(respostas);

    }

}
