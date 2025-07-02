package forum.hub.api.domain.respostas;

import java.time.LocalDateTime;

public record DadosListagemRespostas(
        Long topicoId,
        Long respostaId,
        String mensagem,
        String autor,
        LocalDateTime data
) {

    public DadosListagemRespostas(Resposta resposta) {

        this(   resposta.getTopico().getId(),
                resposta.getId(),
                resposta.getMensagem(),
                resposta.getAutor().getNome(),
                resposta.getData()
        );

    }

}