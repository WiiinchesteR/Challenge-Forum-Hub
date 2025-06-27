package forum.hub.api.topicos;

import java.time.LocalDateTime;

public record DadosDetalhamentoTopicos(
        Long id, String autor, String titulo, String mensagem, String curso, LocalDateTime data
) {

    public DadosDetalhamentoTopicos(Topicos topicos) {
        this(topicos.getId(), topicos.getAutor(), topicos.getTitulo(), topicos.getMensagem(),
                topicos.getCurso(), topicos.getData());
    }
}
