
package yili.commonservice;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "CommonService", targetNamespace = "CommonService", wsdlLocation = "http://10.60.137.170:5656/ZDHService/WebService/CommonService.asmx?WSDL")
public class CommonService
    extends Service
{

    private final static URL COMMONSERVICE_WSDL_LOCATION;
    private final static WebServiceException COMMONSERVICE_EXCEPTION;
    private final static QName COMMONSERVICE_QNAME = new QName("CommonService", "CommonService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://10.60.137.170:5656/ZDHService/WebService/CommonService.asmx?WSDL");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        COMMONSERVICE_WSDL_LOCATION = url;
        COMMONSERVICE_EXCEPTION = e;
    }

    public CommonService() {
        super(__getWsdlLocation(), COMMONSERVICE_QNAME);
    }

    public CommonService(WebServiceFeature... features) {
        super(__getWsdlLocation(), COMMONSERVICE_QNAME, features);
    }

    public CommonService(URL wsdlLocation) {
        super(wsdlLocation, COMMONSERVICE_QNAME);
    }

    public CommonService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, COMMONSERVICE_QNAME, features);
    }

    public CommonService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public CommonService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns CommonServiceSoap
     */
    @WebEndpoint(name = "CommonServiceSoap")
    public CommonServiceSoap getCommonServiceSoap() {
        return super.getPort(new QName("CommonService", "CommonServiceSoap"), CommonServiceSoap.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns CommonServiceSoap
     */
    @WebEndpoint(name = "CommonServiceSoap")
    public CommonServiceSoap getCommonServiceSoap(WebServiceFeature... features) {
        return super.getPort(new QName("CommonService", "CommonServiceSoap"), CommonServiceSoap.class, features);
    }

    /**
     * 
     * @return
     *     returns CommonServiceSoap
     */
    @WebEndpoint(name = "CommonServiceSoap12")
    public CommonServiceSoap getCommonServiceSoap12() {
        return super.getPort(new QName("CommonService", "CommonServiceSoap12"), CommonServiceSoap.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns CommonServiceSoap
     */
    @WebEndpoint(name = "CommonServiceSoap12")
    public CommonServiceSoap getCommonServiceSoap12(WebServiceFeature... features) {
        return super.getPort(new QName("CommonService", "CommonServiceSoap12"), CommonServiceSoap.class, features);
    }

    private static URL __getWsdlLocation() {
        if (COMMONSERVICE_EXCEPTION!= null) {
            throw COMMONSERVICE_EXCEPTION;
        }
        return COMMONSERVICE_WSDL_LOCATION;
    }

}
