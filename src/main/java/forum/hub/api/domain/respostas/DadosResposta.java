package forum.hub.api.domain.respostas;

import jakarta.validation.constraints.NotBlank;

public record DadosResposta(@NotBlank String mensagem) {
}
