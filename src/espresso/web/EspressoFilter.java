package espresso.web;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * This is the servlet runtime for Espresso. It handles all the dispatch
 * and script loading logic, and is meant for non-standalone environments
 * like Appengine.
 *
 * @author dhanji@gmail.com (Dhanji R. Prasanna)
 */
public final class EspressoFilter implements Filter {
  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
  }

  @Override
  public void destroy() {
  }
}
