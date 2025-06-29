package forum.hub.api.domain.respostas;

import forum.hub.api.domain.curso.Curso;
import forum.hub.api.domain.topicos.Topico;

import java.util.List;

public record DadosDetalhadoRespostas(

        Long id,
        String titulo,
        String mensagem,
        String nomeAutor,
        Curso curso,
        StatusResposta status,
        List<DadosListagemRespostas> respostas
) {

    public DadosDetalhadoRespostas(Topico topico) {
        this(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensagem(),
                topico.getAutor().getNome(),
                topico.getCurso(),
                topico.getStatus(),
                topico.getRespostas().stream().map(DadosListagemRespostas::new).toList()
        );
    }

}