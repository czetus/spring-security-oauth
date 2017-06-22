package org.springframework.security.oauth.examples.tonr.impl;

import org.springframework.security.oauth.consumer.client.OAuthRestTemplate;
import org.springframework.security.oauth.examples.tonr.SparklrException;
import org.springframework.security.oauth.examples.tonr.SparklrService;
import org.springframework.web.client.RestOperations;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ryan Heaton
 */
public class SparklrServiceImpl implements SparklrService {

  private String sparklrPhotoListURL;
  private String sparklrPhotoURLPattern;
  private RestOperations sparklrRestTemplate;
  private String sparklrTrustedMessageURL;
  private RestOperations trustedClientRestTemplate;

  public List<String> getSparklrPhotoIds() throws SparklrException {
    try {
      InputStream photosXML = new ByteArrayInputStream(getSparklrRestTemplate().getForObject(URI.create(getSparklrPhotoListURL()), byte[].class));

      final List<String> photoIds = new ArrayList<String>();
      SAXParserFactory parserFactory = SAXParserFactory.newInstance();
      parserFactory.setValidating(false);
      parserFactory.setXIncludeAware(false);
      parserFactory.setNamespaceAware(false);
      SAXParser parser = parserFactory.newSAXParser();
      parser.parse(photosXML, new DefaultHandler() {
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
          if ("photo".equals(qName)) {
            photoIds.add(attributes.getValue("id"));
          }
        }
      });
      return photoIds;
    }
    catch (IOException e) {
      throw new IllegalStateException(e);
    }
    catch (SAXException e) {
      throw new IllegalStateException(e);
    }
    catch (ParserConfigurationException e) {
      throw new IllegalStateException(e);
    }
  }

  public InputStream loadSparklrPhoto(String id) throws SparklrException {
    return new ByteArrayInputStream(getSparklrRestTemplate().getForObject(URI.create(String.format(getSparklrPhotoURLPattern(), id)), byte[].class));
  }

  public String getSparklrPhotoURLPattern() {

    return sparklrPhotoURLPattern;
  }

  public void setSparklrPhotoURLPattern(String sparklrPhotoURLPattern) {
    this.sparklrPhotoURLPattern = sparklrPhotoURLPattern;
  }

  public String getSparklrPhotoListURL() {
    return sparklrPhotoListURL;
  }

  public void setSparklrPhotoListURL(String sparklrPhotoListURL) {
    this.sparklrPhotoListURL = sparklrPhotoListURL;
  }

  public RestOperations getSparklrRestTemplate() {
    return sparklrRestTemplate;
  }

  public void setSparklrRestTemplate(RestOperations sparklrRestTemplate) {
    this.sparklrRestTemplate = sparklrRestTemplate;
  }

public void setSparklrTrustedMessageURL(String sparklrTrustedMessageURL) {
	this.sparklrTrustedMessageURL = sparklrTrustedMessageURL;
}

public void setTrustedClientRestTemplate(RestOperations trustedClientRestTemplate) {
	this.trustedClientRestTemplate = trustedClientRestTemplate;
}
  
  
}
