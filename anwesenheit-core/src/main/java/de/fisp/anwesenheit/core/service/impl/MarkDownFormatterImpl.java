package de.fisp.anwesenheit.core.service.impl;

import de.fisp.anwesenheit.core.service.MarkDownFormatter;
import org.pegdown.PegDownProcessor;
import org.springframework.stereotype.Service;

@Service
public class MarkDownFormatterImpl implements MarkDownFormatter {
  @Override
  public String formatMarkDown(String input) {
    PegDownProcessor pegDownProcessor = new PegDownProcessor();
    return pegDownProcessor.markdownToHtml(input);
  }
}
