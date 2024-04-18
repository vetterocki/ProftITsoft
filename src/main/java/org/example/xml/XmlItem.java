package org.example.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
public class XmlItem implements Comparable<XmlItem> {
  @JacksonXmlProperty(localName = "value")
  private String value;

  @JacksonXmlProperty(localName = "count")
  private long count;

  public XmlItem(String value, long count) {
    this.value = value;
    this.count = count;
  }

  @Override
  public int compareTo(XmlItem o) {
    return Long.compare(this.count, o.count);
  }
}
