package forum.hub.api.domain.topicos;

import forum.hub.api.domain.respostas.StatusResposta;

import java.time.LocalDateTime;

public record DadosListagemTopicos(
        Long id,
        String autor,
        String titulo,
        String mensagem,
        LocalDateTime data,
        StatusResposta status
) {

    public DadosListagemTopicos(Topico topico) {

        this(   topico.getId(),
                topico.getAutor().getNome(),
                topico.getTitulo(),
                topico.getMensagem(),
                topico.getData(),
                topico.getStatus()
        );

    }
}
