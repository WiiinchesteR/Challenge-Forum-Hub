package forum.hub.api.domain.topicos;

public record DadosListagemTopicos(
        String autor, String titulo, String mensagem, String curso
) {

    public DadosListagemTopicos(Topicos topicos) {
        this(topicos.getAutor(), topicos.getTitulo(), topicos.getMensagem(), topicos.getCurso());
    }
}
