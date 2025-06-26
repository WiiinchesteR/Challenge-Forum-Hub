package forum.hub.api.domain.topicos;

import jakarta.validation.constraints.NotBlank;

public record DadosEnviarTopico(
        @NotBlank
        String autor,

        @NotBlank
        String titulo,

        @NotBlank
        String mensagem,

        @NotBlank
        String curso
) {
}
