package de.fisp.anwesenheit.web.tags;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.springframework.web.servlet.tags.form.AbstractHtmlElementTag;
import org.springframework.web.servlet.tags.form.TagWriter;

import java.io.IOException;

public class CurrentUserNameTag extends AbstractHtmlElementTag {
  private static final long serialVersionUID = 1L;

  private String getCurrentUser() {
    HttpSession session = pageContext.getSession();
    if (session == null)
      return "";
    return (String) session.getAttribute("benutzerName");
  }

  @Override
  protected int writeTagContent(TagWriter tagWriter) throws JspException {
    try {
      JspWriter out = pageContext.getOut();
      out.append(getCurrentUser());
    } catch (IOException ex) {
      throw new JspException("CurrentUserNameTag", ex);
    }
    return SKIP_BODY;
  }
}
