package de.fisp.anwesenheit.web.tags;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.springframework.web.servlet.tags.form.AbstractHtmlElementTag;
import org.springframework.web.servlet.tags.form.TagWriter;

public class CurrentUserNameTag extends AbstractHtmlElementTag {
  private static final long serialVersionUID = 1L;

  private String getCurrentUser() {
    HttpSession session = pageContext.getSession();
    if(session == null)
      return "";
    return (String) session.getAttribute("benutzerName");
  }

  @Override
  protected int writeTagContent(TagWriter tagWriter) throws JspException {
    tagWriter.startTag("ul");
    tagWriter.writeAttribute("class", "nav pull-right");
    tagWriter.startTag("li");
    tagWriter.writeAttribute("class", "navbar-text");
    tagWriter.appendValue(getCurrentUser());
    tagWriter.endTag();
    tagWriter.startTag("li");
    tagWriter.startTag("a");
    tagWriter.writeAttribute("href", getRequestContext().getContextUrl("/logoff"));
    tagWriter.appendValue("Abmelden");
    tagWriter.endTag();
    tagWriter.endTag(); 
    tagWriter.endTag();    
    return SKIP_BODY;
  }
}
