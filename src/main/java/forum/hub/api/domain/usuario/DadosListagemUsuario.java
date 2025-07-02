package forum.hub.api.domain.usuario;

public record DadosListagemUsuario(
        Long id,
        String nome,
        String email,
        int qtdTopicosEnviado,
        int qtdRespostasEnviado
) {

    public DadosListagemUsuario(Usuario usuario, int enviados, int respondidos) {
        this(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                enviados,
                respondidos
        );
    }
}
