package forum.hub.api.domain.respostas;

import java.time.LocalDateTime;

public record DadosListagemRespostas(String mensagem, String autor, LocalDateTime data) {

    public DadosListagemRespostas(Resposta resposta) {
        this(resposta.getMensagem(), resposta.getAutor().getNome(), resposta.getData());
    }

}