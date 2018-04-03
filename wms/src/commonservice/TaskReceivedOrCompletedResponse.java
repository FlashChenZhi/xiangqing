
package commonservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>anonymous complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TaskReceivedOrCompletedResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "taskReceivedOrCompletedResult"
})
@XmlRootElement(name = "TaskReceivedOrCompletedResponse")
public class TaskReceivedOrCompletedResponse {

    @XmlElement(name = "TaskReceivedOrCompletedResult")
    protected String taskReceivedOrCompletedResult;

    /**
     * 获取taskReceivedOrCompletedResult属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTaskReceivedOrCompletedResult() {
        return taskReceivedOrCompletedResult;
    }

    /**
     * 设置taskReceivedOrCompletedResult属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTaskReceivedOrCompletedResult(String value) {
        this.taskReceivedOrCompletedResult = value;
    }

}
