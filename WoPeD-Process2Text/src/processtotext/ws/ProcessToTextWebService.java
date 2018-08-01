//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package processtotext.ws;

import textGenerator.TextGenerator;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.servlet.ServletContext;
import javax.xml.ws.WebServiceContext;
import java.io.File;
import java.io.PrintWriter;

@SuppressWarnings("unused")
@WebService(serviceName = "ProcessToTextWebService")
public class ProcessToTextWebService {
    @Resource
    private WebServiceContext context;

    @SuppressWarnings({"unused", "ResultOfMethodCallIgnored"})
    @WebMethod(operationName = "generateTextFromProcessSpecification")
    public String generateTextFromProcessSpecification(@WebParam(name = "processSpecification") String processSpecification) {
        try {
            ServletContext servletContext = (ServletContext) this.context.getMessageContext().get("javax.xml.ws.servlet.context");
            String contextPath = servletContext.getRealPath("/");
            TextGenerator textGenerator = new TextGenerator(contextPath);
            String result = textGenerator.toText(processSpecification);
            return result;
        } catch (Exception e) {
            return null;
        }
    }
}