package forum.hub.api.controller;

import forum.hub.api.domain.topicos.DadosEnviarTopico;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/topicos")
public class TopicosController {

    @PostMapping
    public void enviarTopico(@RequestBody DadosEnviarTopico dados) {
        System.out.println(dados);
    }
}
