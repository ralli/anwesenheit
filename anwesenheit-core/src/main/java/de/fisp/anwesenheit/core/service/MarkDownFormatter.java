package de.fisp.anwesenheit.core.service;

public interface MarkDownFormatter {
  /**
   * Wandelt einen Markdowntext nach HTML um.
   *
   * @param input der Text, der umgewandelt werden soll.
   *
   * @return Das generierte HTML
   */
  String formatMarkDown(String input);
}
