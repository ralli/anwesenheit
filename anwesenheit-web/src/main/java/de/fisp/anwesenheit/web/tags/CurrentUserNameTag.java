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
    return (String) session.getAttribute("benutzerId");
  }

  @Override
  protected int writeTagContent(TagWriter tagWriter) throws JspException {
    tagWriter.startTag("div");
    tagWriter.writeAttribute("class", "navbar-form pull-right");
    tagWriter.startTag("span");
    tagWriter.writeAttribute("class", "navbar-text");
    tagWriter.appendValue(getCurrentUser());
    tagWriter.endTag();
    tagWriter.startTag("a");
    tagWriter.writeAttribute("href", getRequestContext().getContextUrl("/logoff"));
    tagWriter.writeAttribute("class", "btn btn-inverse");    
    tagWriter.appendValue("Abmelden");
    tagWriter.endTag(); 
    tagWriter.endTag();    
    return SKIP_BODY;
  }
}
