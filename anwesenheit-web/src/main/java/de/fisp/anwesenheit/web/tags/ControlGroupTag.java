package de.fisp.anwesenheit.web.tags;

import javax.servlet.jsp.JspException;

import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.tags.form.AbstractHtmlElementTag;
import org.springframework.web.servlet.tags.form.TagWriter;

public class ControlGroupTag extends AbstractHtmlElementTag {
    private static final long serialVersionUID = 1L;
    private TagWriter tagWriter;

    @Override
    protected int writeTagContent(TagWriter tagWriter) throws JspException {
        this.tagWriter = tagWriter;
        tagWriter.startTag("div");
        writeDefaultAttributes(tagWriter);

        tagWriter.forceBlock();
        return EVAL_BODY_INCLUDE;
    }

    /**
     * Close the '<code>label</code>' tag.
     */
    @Override
    public int doEndTag() throws JspException {
        this.tagWriter.endTag();
        return EVAL_PAGE;
    }

    @Override
    protected String getCssClass() {
        String result = super.getCssClass();
        if (StringUtils.hasText(result))
            return result;
        return "control-group";
    }

    @Override
    protected String getCssErrorClass() {
        String result = super.getCssErrorClass();
        if (StringUtils.hasText(result))
            return result;
        return "error";
    }

    /**
     * Gets the appropriate CSS class to use based on the state of the current
     * {@link org.springframework.web.servlet.support.BindStatus} object.
     */
    protected String resolveCssClass() throws JspException {
        if (getBindStatus().isError()) {
            StringBuffer sb = new StringBuffer();
            sb.append(ObjectUtils.getDisplayString(evaluate("cssClass", getCssClass())));
            sb.append(' ');
            sb.append(ObjectUtils.getDisplayString(evaluate("cssErrorClass", getCssErrorClass())));
            return sb.toString();
        } else {
            return ObjectUtils.getDisplayString(evaluate("cssClass", getCssClass()));
        }
    }
}
