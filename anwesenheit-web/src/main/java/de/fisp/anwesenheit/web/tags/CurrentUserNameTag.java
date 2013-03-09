package de.fisp.anwesenheit.web.tags;

import javax.servlet.jsp.JspException;

import org.springframework.web.servlet.tags.form.AbstractHtmlElementTag;
import org.springframework.web.servlet.tags.form.TagWriter;

public class CurrentUserNameTag extends AbstractHtmlElementTag {
	private static final long serialVersionUID = 1L;

	@Override
	protected int writeTagContent(TagWriter tagWriter) throws JspException {
		tagWriter.startTag("ul");
		tagWriter.writeAttribute("class", "nav pull-right");
		tagWriter.startTag("li");
		tagWriter.writeAttribute("class", "navbar-text");
		tagWriter.appendValue("Ralph Juhnke");
		tagWriter.endTag();
		tagWriter.forceBlock();
		return SKIP_BODY;
	}
}
