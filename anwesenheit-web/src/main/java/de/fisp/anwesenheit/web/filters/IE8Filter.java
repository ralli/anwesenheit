package de.fisp.anwesenheit.web.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * Der IE8 hat die Eigenschaft, dass er im Intranet standardmäßig auf einen 
 * "Kompatibilitätsmodus" eingestellt ist. Das hat zur Folge, dass die Seiten
 * nicht korrekt angezeigt werden.
 *
 * Dieser Filter fügt einen HTTP-Header hinzu, der den Kompatibilitätsmodus beim 
 * IE8 abschaltet.
 *
 */
public class IE8Filter implements Filter {

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response,
                       FilterChain chain) throws IOException, ServletException {
    HttpServletResponse res = (HttpServletResponse) response;
    res.addHeader("X-UA-Compatible", "IE=8");
    chain.doFilter(request, response);
  }

  @Override
  public void destroy() {
  }

}
