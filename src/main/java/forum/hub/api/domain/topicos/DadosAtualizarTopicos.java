package forum.hub.api.domain.topicos;

import forum.hub.api.domain.curso.Curso;

public record DadosAtualizarTopicos(
        String titulo,
        String mensagem,
        String curso
) {
}
