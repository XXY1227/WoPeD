
package org.woped.qualanalysis.paraphrasing.webservice;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;

import org.woped.core.config.ConfigurationManager;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 *
 */
@WebServiceClient(name = "ProcessToTextWebService", targetNamespace = "http://ws.processtotext/")
public class ProcessToTextWebServiceImpl
    extends Service
{

    private static URL PROCESSTOTEXTWEBSERVICE_WSDL_LOCATION = null;
    private static WebServiceException PROCESSTOTEXTWEBSERVICE_EXCEPTION = null;
    private static QName PROCESSTOTEXTWEBSERVICE_QNAME = new QName("http://ws.processtotext/", "ProcessToTextWebService");
    private static String CONNECTIONSTRING = "";

    /**
     * Generate new URL for P2T web service.
     * @author allgaier
     */
    public static void generateURL(){
    	URL url = null;
        WebServiceException e = null;
        CONNECTIONSTRING = "http://" + ConfigurationManager.getConfiguration().getProcess2TextServerHost() + ":" + ConfigurationManager.getConfiguration().getProcess2TextServerPort() + ConfigurationManager.getConfiguration().getProcess2TextServerURI() + "/ProcessToTextWebService?wsdl";
        try {
            url = new URL(CONNECTIONSTRING);
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        PROCESSTOTEXTWEBSERVICE_WSDL_LOCATION = url;
        PROCESSTOTEXTWEBSERVICE_EXCEPTION = e;
    }

    public ProcessToTextWebServiceImpl() {
    	super(__getWsdlLocation(), PROCESSTOTEXTWEBSERVICE_QNAME);
    }

    public ProcessToTextWebServiceImpl(WebServiceFeature... features) {
        super(__getWsdlLocation(), PROCESSTOTEXTWEBSERVICE_QNAME, features);
    }

    public ProcessToTextWebServiceImpl(URL wsdlLocation) {
        super(wsdlLocation, PROCESSTOTEXTWEBSERVICE_QNAME);
    }

    public ProcessToTextWebServiceImpl(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, PROCESSTOTEXTWEBSERVICE_QNAME, features);
    }

    public ProcessToTextWebServiceImpl(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public ProcessToTextWebServiceImpl(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     *
     * @return
     *     returns ProcessToTextWebService
     */
    @WebEndpoint(name = "ProcessToTextWebServicePort")
    public ProcessToTextWebService getProcessToTextWebServicePort() {
        return super.getPort(new QName("http://ws.processtotext/", "ProcessToTextWebServicePort"), ProcessToTextWebService.class);
    }

    /**
     *
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns ProcessToTextWebService
     */
    @WebEndpoint(name = "ProcessToTextWebServicePort")
    public ProcessToTextWebService getProcessToTextWebServicePort(WebServiceFeature... features) {
        return super.getPort(new QName("http://ws.processtotext/", "ProcessToTextWebServicePort"), ProcessToTextWebService.class, features);
    }

    private static URL __getWsdlLocation() {
        if (PROCESSTOTEXTWEBSERVICE_EXCEPTION!= null) {
            throw PROCESSTOTEXTWEBSERVICE_EXCEPTION;
        }

        // Ensure that for each call, we generate a new URL to reflect potential
        // configuration changes.
        generateURL();

        return PROCESSTOTEXTWEBSERVICE_WSDL_LOCATION;
    }

}
