package org.apromore.manager.service_portal;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;
import org.apromore.manager.model_portal.DeleteEditSessionInputMsgType;
import org.apromore.manager.model_portal.DeleteEditSessionOutputMsgType;
import org.apromore.manager.model_portal.DeleteProcessVersionsInputMsgType;
import org.apromore.manager.model_portal.DeleteProcessVersionsOutputMsgType;
import org.apromore.manager.model_portal.EditProcessDataInputMsgType;
import org.apromore.manager.model_portal.EditProcessDataOutputMsgType;
import org.apromore.manager.model_portal.ExportFormatInputMsgType;
import org.apromore.manager.model_portal.ExportFormatOutputMsgType;
import org.apromore.manager.model_portal.ImportProcessInputMsgType;
import org.apromore.manager.model_portal.ImportProcessOutputMsgType;
import org.apromore.manager.model_portal.MergeProcessesInputMsgType;
import org.apromore.manager.model_portal.MergeProcessesOutputMsgType;
import org.apromore.manager.model_portal.ObjectFactory;
import org.apromore.manager.model_portal.ReadAllUsersInputMsgType;
import org.apromore.manager.model_portal.ReadAllUsersOutputMsgType;
import org.apromore.manager.model_portal.ReadDomainsInputMsgType;
import org.apromore.manager.model_portal.ReadDomainsOutputMsgType;
import org.apromore.manager.model_portal.ReadEditSessionInputMsgType;
import org.apromore.manager.model_portal.ReadEditSessionOutputMsgType;
import org.apromore.manager.model_portal.ReadNativeTypesInputMsgType;
import org.apromore.manager.model_portal.ReadNativeTypesOutputMsgType;
import org.apromore.manager.model_portal.ReadProcessSummariesInputMsgType;
import org.apromore.manager.model_portal.ReadProcessSummariesOutputMsgType;
import org.apromore.manager.model_portal.ReadUserInputMsgType;
import org.apromore.manager.model_portal.ReadUserOutputMsgType;
import org.apromore.manager.model_portal.SearchForSimilarProcessesInputMsgType;
import org.apromore.manager.model_portal.SearchForSimilarProcessesOutputMsgType;
import org.apromore.manager.model_portal.UpdateProcessInputMsgType;
import org.apromore.manager.model_portal.UpdateProcessOutputMsgType;
import org.apromore.manager.model_portal.WriteAnnotationInputMsgType;
import org.apromore.manager.model_portal.WriteAnnotationOutputMsgType;
import org.apromore.manager.model_portal.WriteEditSessionInputMsgType;
import org.apromore.manager.model_portal.WriteEditSessionOutputMsgType;
import org.apromore.manager.model_portal.WriteUserInputMsgType;
import org.apromore.manager.model_portal.WriteUserOutputMsgType;

/** This class was generated by the JAX-WS RI. JAX-WS RI 2.2.4-b01 Generated source version: 2.2 */
@WebService(
    name = "ManagerPortalPortType",
    targetNamespace = "http://www.apromore.org/manager/service_portal")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@XmlSeeAlso({ObjectFactory.class})
public interface ManagerPortalPortType {

  /**
   * @param payload
   * @return returns org.apromore.manager.model_portal.ReadProcessSummariesOutputMsgType
   */
  @WebMethod(operationName = "ReadProcessSummaries")
  @WebResult(
      name = "ReadProcessSummariesOutputMsg",
      targetNamespace = "http://www.apromore.org/manager/model_portal",
      partName = "payload")
  public ReadProcessSummariesOutputMsgType readProcessSummaries(
      @WebParam(
              name = "ReadProcessSummariesInputMsg",
              targetNamespace = "http://www.apromore.org/manager/model_portal",
              partName = "payload")
          ReadProcessSummariesInputMsgType payload);

  /**
   * @param payload
   * @return returns org.apromore.manager.model_portal.EditProcessDataOutputMsgType
   */
  @WebMethod(operationName = "EditProcessData")
  @WebResult(
      name = "EditProcessDataOutputMsg",
      targetNamespace = "http://www.apromore.org/manager/model_portal",
      partName = "payload")
  public EditProcessDataOutputMsgType editProcessData(
      @WebParam(
              name = "EditProcessDataInputMsg",
              targetNamespace = "http://www.apromore.org/manager/model_portal",
              partName = "payload")
          EditProcessDataInputMsgType payload);

  /**
   * @param payload
   * @return returns org.apromore.manager.model_portal.ReadDomainsOutputMsgType
   */
  @WebMethod(operationName = "ReadDomains")
  @WebResult(
      name = "ReadDomainsOutputMsg",
      targetNamespace = "http://www.apromore.org/manager/model_portal",
      partName = "payload")
  public ReadDomainsOutputMsgType readDomains(
      @WebParam(
              name = "ReadDomainsInputMsg",
              targetNamespace = "http://www.apromore.org/manager/model_portal",
              partName = "payload")
          ReadDomainsInputMsgType payload);

  /**
   * @param payload
   * @return returns org.apromore.manager.model_portal.ReadNativeTypesOutputMsgType
   */
  @WebMethod(operationName = "ReadNativeTypes")
  @WebResult(
      name = "ReadNativeTypesOutputMsg",
      targetNamespace = "http://www.apromore.org/manager/model_portal",
      partName = "payload")
  public ReadNativeTypesOutputMsgType readNativeTypes(
      @WebParam(
              name = "ReadNativeTypesInputMsg",
              targetNamespace = "http://www.apromore.org/manager/model_portal",
              partName = "payload")
          ReadNativeTypesInputMsgType payload);

  /**
   * @param payload
   * @return returns org.apromore.manager.model_portal.ReadAllUsersOutputMsgType
   */
  @WebMethod(operationName = "ReadAllUsers")
  @WebResult(
      name = "ReadAllUsersOutputMsg",
      targetNamespace = "http://www.apromore.org/manager/model_portal",
      partName = "payload")
  public ReadAllUsersOutputMsgType readAllUsers(
      @WebParam(
              name = "ReadAllUsersInputMsg",
              targetNamespace = "http://www.apromore.org/manager/model_portal",
              partName = "payload")
          ReadAllUsersInputMsgType payload);

  /**
   * @param payload
   * @return returns org.apromore.manager.model_portal.ReadUserOutputMsgType
   */
  @WebMethod(operationName = "ReadUser")
  @WebResult(
      name = "ReadUserOutputMsg",
      targetNamespace = "http://www.apromore.org/manager/model_portal",
      partName = "payload")
  public ReadUserOutputMsgType readUser(
      @WebParam(
              name = "ReadUserInputMsg",
              targetNamespace = "http://www.apromore.org/manager/model_portal",
              partName = "payload")
          ReadUserInputMsgType payload);

  /**
   * @param payload
   * @return returns org.apromore.manager.model_portal.WriteUserOutputMsgType
   */
  @WebMethod(operationName = "WriteUser")
  @WebResult(
      name = "WriteUserOutputMsg",
      targetNamespace = "http://www.apromore.org/manager/model_portal",
      partName = "payload")
  public WriteUserOutputMsgType writeUser(
      @WebParam(
              name = "WriteUserInputMsg",
              targetNamespace = "http://www.apromore.org/manager/model_portal",
              partName = "payload")
          WriteUserInputMsgType payload);

  /**
   * @param payload
   * @return returns org.apromore.manager.model_portal.ImportProcessOutputMsgType
   */
  @WebMethod(operationName = "ImportProcess")
  @WebResult(
      name = "ImportProcessOutputMsg",
      targetNamespace = "http://www.apromore.org/manager/model_portal",
      partName = "payload")
  public ImportProcessOutputMsgType importProcess(
      @WebParam(
              name = "ImportProcessInputMsg",
              targetNamespace = "http://www.apromore.org/manager/model_portal",
              partName = "payload")
          ImportProcessInputMsgType payload);

  /**
   * @param payload
   * @return returns org.apromore.manager.model_portal.ExportFormatOutputMsgType
   */
  @WebMethod(operationName = "ExportFormat")
  @WebResult(
      name = "ExportFormatOutputMsg",
      targetNamespace = "http://www.apromore.org/manager/model_portal",
      partName = "payload")
  public ExportFormatOutputMsgType exportFormat(
      @WebParam(
              name = "ExportFormatInputMsg",
              targetNamespace = "http://www.apromore.org/manager/model_portal",
              partName = "payload")
          ExportFormatInputMsgType payload);

  /**
   * @param payload
   * @return returns org.apromore.manager.model_portal.WriteEditSessionOutputMsgType
   */
  @WebMethod(operationName = "WriteEditSession")
  @WebResult(
      name = "WriteEditSessionOutputMsg",
      targetNamespace = "http://www.apromore.org/manager/model_portal",
      partName = "payload")
  public WriteEditSessionOutputMsgType writeEditSession(
      @WebParam(
              name = "WriteEditSessionInputMsg",
              targetNamespace = "http://www.apromore.org/manager/model_portal",
              partName = "payload")
          WriteEditSessionInputMsgType payload);

  /**
   * @param payload
   * @return returns org.apromore.manager.model_portal.WriteAnnotationOutputMsgType
   */
  @WebMethod(operationName = "WriteAnnotation")
  @WebResult(
      name = "WriteAnnotationOutputMsg",
      targetNamespace = "http://www.apromore.org/manager/model_portal",
      partName = "payload")
  public WriteAnnotationOutputMsgType writeAnnotation(
      @WebParam(
              name = "WriteAnnotationInputMsg",
              targetNamespace = "http://www.apromore.org/manager/model_portal",
              partName = "payload")
          WriteAnnotationInputMsgType payload);

  /**
   * @param payload
   * @return returns org.apromore.manager.model_portal.ReadEditSessionOutputMsgType
   */
  @WebMethod(operationName = "ReadEditSession")
  @WebResult(
      name = "ReadEditSessionOutputMsg",
      targetNamespace = "http://www.apromore.org/manager/model_portal",
      partName = "payload")
  public ReadEditSessionOutputMsgType readEditSession(
      @WebParam(
              name = "ReadEditSessionInputMsg",
              targetNamespace = "http://www.apromore.org/manager/model_portal",
              partName = "payload")
          ReadEditSessionInputMsgType payload);

  /**
   * @param payload
   * @return returns org.apromore.manager.model_portal.DeleteEditSessionOutputMsgType
   */
  @WebMethod(operationName = "DeleteEditSession")
  @WebResult(
      name = "DeleteEditSessionOutputMsg",
      targetNamespace = "http://www.apromore.org/manager/model_portal",
      partName = "payload")
  public DeleteEditSessionOutputMsgType deleteEditSession(
      @WebParam(
              name = "DeleteEditSessionInputMsg",
              targetNamespace = "http://www.apromore.org/manager/model_portal",
              partName = "payload")
          DeleteEditSessionInputMsgType payload);

  /**
   * @param payload
   * @return returns org.apromore.manager.model_portal.UpdateProcessOutputMsgType
   */
  @WebMethod(operationName = "UpdateProcess")
  @WebResult(
      name = "UpdateProcessOutputMsg",
      targetNamespace = "http://www.apromore.org/manager/model_portal",
      partName = "payload")
  public UpdateProcessOutputMsgType updateProcess(
      @WebParam(
              name = "UpdateProcessInputMsg",
              targetNamespace = "http://www.apromore.org/manager/model_portal",
              partName = "payload")
          UpdateProcessInputMsgType payload);

  /**
   * @param payload
   * @return returns org.apromore.manager.model_portal.DeleteProcessVersionsOutputMsgType
   */
  @WebMethod(operationName = "DeleteProcessVersions")
  @WebResult(
      name = "DeleteProcessVersionsOutputMsg",
      targetNamespace = "http://www.apromore.org/manager/model_portal",
      partName = "payload")
  public DeleteProcessVersionsOutputMsgType deleteProcessVersions(
      @WebParam(
              name = "DeleteProcessVersionsInputMsg",
              targetNamespace = "http://www.apromore.org/manager/model_portal",
              partName = "payload")
          DeleteProcessVersionsInputMsgType payload);

  /**
   * @param payload
   * @return returns org.apromore.manager.model_portal.SearchForSimilarProcessesOutputMsgType
   */
  @WebMethod(operationName = "SearchForSimilarProcesses")
  @WebResult(
      name = "SearchForSimilarProcessesOutputMsg",
      targetNamespace = "http://www.apromore.org/manager/model_portal",
      partName = "payload")
  public SearchForSimilarProcessesOutputMsgType searchForSimilarProcesses(
      @WebParam(
              name = "SearchForSimilarProcessesInputMsg",
              targetNamespace = "http://www.apromore.org/manager/model_portal",
              partName = "payload")
          SearchForSimilarProcessesInputMsgType payload);

  /**
   * @param payload
   * @return returns org.apromore.manager.model_portal.MergeProcessesOutputMsgType
   */
  @WebMethod(operationName = "MergeProcesses")
  @WebResult(
      name = "MergeProcessesOutputMsg",
      targetNamespace = "http://www.apromore.org/manager/model_portal",
      partName = "payload")
  public MergeProcessesOutputMsgType mergeProcesses(
      @WebParam(
              name = "MergeProcessesInputMsg",
              targetNamespace = "http://www.apromore.org/manager/model_portal",
              partName = "payload")
          MergeProcessesInputMsgType payload);
}
