package org.apromore.manager.service_portal;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;
import org.woped.core.config.ConfigurationManager;

/** This class was generated by the JAX-WS RI. JAX-WS RI 2.2.4-b01 Generated source version: 2.2 */
@WebServiceClient(
    name = "ManagerPortalService",
    targetNamespace = "http://www.apromore.org/manager/service_portal",
    wsdlLocation = "http://brahms0.imag.fr:8080/Apromore-manager/services/ManagerPortal?wsdl")
public class ManagerPortalService extends Service {

  private static final URL MANAGERPORTALSERVICE_WSDL_LOCATION;
  private static final WebServiceException MANAGERPORTALSERVICE_EXCEPTION;
  private static final QName MANAGERPORTALSERVICE_QNAME =
      new QName("http://www.apromore.org/manager/service_portal", "ManagerPortalService");

  static {
    URL url = null;
    WebServiceException e = null;
    try {
      String a = "";
      if (ConfigurationManager.getConfiguration().getApromoreServerPort() != 0)
        a = ":" + ConfigurationManager.getConfiguration().getApromoreServerPort();
      url =
          new URL(
              ConfigurationManager.getConfiguration().getApromoreServerURL()
                  + a
                  + "/Apromore-manager/services/ManagerPortal?wsdl");
    } catch (MalformedURLException ex) {
      e = new WebServiceException(ex);
    }
    MANAGERPORTALSERVICE_WSDL_LOCATION = url;
    MANAGERPORTALSERVICE_EXCEPTION = e;
  }

  public ManagerPortalService() {
    super(__getWsdlLocation(), MANAGERPORTALSERVICE_QNAME);
  }

  public ManagerPortalService(WebServiceFeature... features) {
    super(__getWsdlLocation(), MANAGERPORTALSERVICE_QNAME);
  }

  public ManagerPortalService(URL wsdlLocation) {
    super(wsdlLocation, MANAGERPORTALSERVICE_QNAME);
  }

  public ManagerPortalService(URL wsdlLocation, WebServiceFeature... features) {
    super(wsdlLocation, MANAGERPORTALSERVICE_QNAME);
  }

  public ManagerPortalService(URL wsdlLocation, QName serviceName) {
    super(wsdlLocation, serviceName);
  }

  public ManagerPortalService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
    super(wsdlLocation, serviceName);
  }

  /** @return returns ManagerPortalPortType */
  @WebEndpoint(name = "ManagerPortal")
  public ManagerPortalPortType getManagerPortal() {
    return super.getPort(
        new QName("http://www.apromore.org/manager/service_portal", "ManagerPortal"),
        ManagerPortalPortType.class);
  }

  /**
   * @param features A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.
   *     Supported features not in the <code>features</code> parameter will have their default
   *     values.
   * @return returns ManagerPortalPortType
   */
  @WebEndpoint(name = "ManagerPortal")
  public ManagerPortalPortType getManagerPortal(WebServiceFeature... features) {
    return super.getPort(
        new QName("http://www.apromore.org/manager/service_portal", "ManagerPortal"),
        ManagerPortalPortType.class,
        features);
  }

  private static URL __getWsdlLocation() {
    if (MANAGERPORTALSERVICE_EXCEPTION != null) {
      throw MANAGERPORTALSERVICE_EXCEPTION;
    }
    return MANAGERPORTALSERVICE_WSDL_LOCATION;
  }
}
