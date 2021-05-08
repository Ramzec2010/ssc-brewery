package guru.sfg.brewery.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class RestUrlVariableAuthFilter extends RestHeaderAuthFilter{
    public RestUrlVariableAuthFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        String userName = getUsernameFromParam(httpServletRequest);
        String password = getPasswordFromParam(httpServletRequest);

        if (userName == null) {
            userName = "";
        }

        if (password == null) {
            password = "";
        }

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userName, password);

        if(!StringUtils.isEmpty(userName)) {
            return this.getAuthenticationManager().authenticate(token);
        } else {
            return null;
        }
    }

    private String getPasswordFromParam(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getParameter("pathPassword");
    }

    private String getUsernameFromParam(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getParameter("pathUser");
    }
}
