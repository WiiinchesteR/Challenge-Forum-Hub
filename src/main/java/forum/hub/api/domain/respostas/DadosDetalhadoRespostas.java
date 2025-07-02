package forum.hub.api.domain.respostas;

import forum.hub.api.domain.curso.DadosCurso;
import forum.hub.api.domain.topicos.Topico;

import java.util.List;

public record DadosDetalhadoRespostas(

        Long id,
        String titulo,
        String mensagem,
        String nomeAutor,
        DadosCurso curso,
        StatusResposta status,
        List<DadosListagemRespostas> respostas
) {

    public DadosDetalhadoRespostas(Topico topico) {
        this(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensagem(),
                topico.getAutor().getNome(),
                new DadosCurso(topico.getCurso()),
                topico.getStatus(),
                topico.getRespostas().stream().map(DadosListagemRespostas::new).toList()
        );
    }

}