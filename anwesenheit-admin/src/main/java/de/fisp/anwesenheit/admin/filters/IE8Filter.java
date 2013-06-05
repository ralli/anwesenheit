package de.fisp.anwesenheit.admin.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Der IE8 hat die Eigenschaft, dass er im Intranet standardmäßig auf einen
 * "Kompatibilitätsmodus" eingestellt ist. Das hat zur Folge, dass die Seiten
 * nicht korrekt angezeigt werden.
 * <p/>
 * Dieser Filter fügt einen HTTP-Header hinzu, der den Kompatibilitätsmodus beim
 * IE8 abschaltet.
 */
public class IE8Filter implements Filter {

  /**
   * Empty implementation of the Filter-Interface
   *
   * @param filterConfig the Filterconfig
   * @throws javax.servlet.ServletException
   */
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

  /**
   * Empty implementation of the Filter-Interface
   */
  @Override
  public void destroy() {
  }

}
