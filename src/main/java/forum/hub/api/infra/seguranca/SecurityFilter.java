package forum.hub.api.infra.seguranca;

import forum.hub.api.domain.usuario.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository repository;


    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
    ) throws ServletException, IOException {

        var tokenJWT = recuperarToken(request);

        if (tokenJWT != null) {
            var subject = tokenService.getSubject(tokenJWT);

            var usuario = repository.findByEmail(subject);

            var autenticacao = new UsernamePasswordAuthenticationToken(
                    usuario, null, usuario.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(autenticacao);

        }

        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        var autorizacaoCabecalho = request.getHeader("Authorization");

        if (autorizacaoCabecalho != null) {
            return autorizacaoCabecalho.replace("Bearer ", "").trim();
        }

        return null;
    }
}