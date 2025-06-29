package forum.hub.api.domain.topicos;

import forum.hub.api.domain.curso.Curso;
import forum.hub.api.domain.respostas.StatusResposta;

import java.time.LocalDateTime;

public record DadosDetalhamentoTopicos(
        Long id, String autor, String titulo, String mensagem, Curso curso, LocalDateTime data, StatusResposta status
) {

    public DadosDetalhamentoTopicos(Topico topico) {
        this(topico.getId(), topico.getAutor().getNome(), topico.getTitulo(), topico.getMensagem(),
                topico.getCurso(), topico.getData(), topico.getStatus());
    }
}
