package com.asrs.domain.XMLTransportOrder;

import com.asrs.xml.util.MsgUtil;
import com.asrs.domain.XMLbean.XMLList.TransportOrder;
import com.util.hibernate.HibernateUtil;
import org.hibernate.Session;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator Date: 13-8-30 Time:
 * 下午1:37 To change this template use File | Settings | File Templates.
 */
public class XMLTransportOrderUtil {
    /**
     * 添加 @param xmlTransportOrder
     */
    public static void add(XMLTransportOrder xmlTransportOrder) {
        Session session = HibernateUtil.getCurrentSession();
        session.save(xmlTransportOrder);
    }

    /**
     * 修改 @param xmlTransportOrder
     */
    public static void modify(XMLTransportOrder xmlTransportOrder) {
        Session session = HibernateUtil.getCurrentSession();
        session.update(xmlTransportOrder);
    }

    /**
     * 删除 @param xmlTransportOrder
     */
    public static void delete(XMLTransportOrder xmlTransportOrder) {
        Session session = HibernateUtil.getCurrentSession();
        session.delete(xmlTransportOrder);
    }

    public static XMLTransportOrder getTransportOrder(TransportOrder order) {
        XMLTransportOrder xmlTransportOrder = new XMLTransportOrder();
        xmlTransportOrder.setDivision(order.getControlArea().getSender().getDivision());
        xmlTransportOrder.setConfirmation(order.getControlArea().getSender().getConfirmation());
        xmlTransportOrder.setCreationDateTime(getTime(order.getControlArea().getCreationDateTime()));
        xmlTransportOrder.setRequestId(order.getControlArea().getRefId().getReferenceId());
        xmlTransportOrder.setRequestId(order.getDataArea().getRequestId());

        xmlTransportOrder.setPriority(getTime(order.getDataArea().getPriority()));
        xmlTransportOrder.setTransportType(order.getDataArea().getTransportType());
        xmlTransportOrder.setIdentiffiedBy(order.getDataArea().getIdentifiedBy());
        if (order.getDataArea().getPart() != null) {
            xmlTransportOrder.setPartid(order.getDataArea().getPart().getPartId());
            xmlTransportOrder.setPartRev(order.getDataArea().getPart().getPartRev());
            xmlTransportOrder.setPartDivision(order.getDataArea().getPart().getDivision());
            xmlTransportOrder.setZone(order.getDataArea().getPart().getZone());
        }
        String fromMHA = order.getDataArea().getFromLocation().getMHA();
        xmlTransportOrder.setFromMHA(fromMHA);
//        xmlTransportOrder.setFromRACK(order.getDataArea().getFromLocation().getRack());
//        xmlTransportOrder.setFromX(order.getDataArea().getFromLocation().getX());
//        xmlTransportOrder.setFromY(order.getDataArea().getFromLocation().getY());
//        xmlTransportOrder.setFromZ(order.getDataArea().getFromLocation().getZ());
//        String fromLocationNo = null;
//        if (order.getDataArea().getFromLocation().getRack() != null &&
//                order.getDataArea().getFromLocation().getX() != null &&
//                order.getDataArea().getFromLocation().getY() != null) {
//            fromLocationNo = MsgUtil.getLocFromXML(order.getDataArea().getFromLocation().getRack(),
//                    order.getDataArea().getFromLocation().getX(),
//                    order.getDataArea().getFromLocation().getY());
//        }

//        xmlTransportOrder.setFromLocationNo(fromLocationNo);
        xmlTransportOrder.setStunitId(order.getDataArea().getStUnit().getStUnitID());
        xmlTransportOrder.setStunitType(order.getDataArea().getStUnit().getStUnitType());
        xmlTransportOrder.setBlockCode(order.getDataArea().getStUnit().getBlockCode());
        xmlTransportOrder.setRegDate(order.getDataArea().getStUnit().getRegDate());
        String toMHA = order.getDataArea().getToLocation().getMHA();
        xmlTransportOrder.setToMHA(toMHA);
//        xmlTransportOrder.setToRACK(order.getDataArea().getToLocation().getRack());
//        xmlTransportOrder.setToX(order.getDataArea().getToLocation().getX());
//        xmlTransportOrder.setToY(order.getDataArea().getToLocation().getY());
//        xmlTransportOrder.setToZ(order.getDataArea().getToLocation().getZ());
        String toLocationNo = null;
//        if (order.getDataArea().getToLocation().getRack() != null &&
//                order.getDataArea().getToLocation().getX() != null &&
//                order.getDataArea().getToLocation().getY() != null) {
//            toLocationNo = MsgUtil.getLocFromXML(order.getDataArea().getToLocation().getRack(),
//                    order.getDataArea().getToLocation().getX(),
//                    order.getDataArea().getToLocation().getY());
//        }

        xmlTransportOrder.setToLocationNo(toLocationNo);
        xmlTransportOrder.setCalcWeight(order.getDataArea().getCalcWeight());
        xmlTransportOrder.setErrorCode(order.getDataArea().getErrorCode());
        xmlTransportOrder.setTreatmentCode(order.getDataArea().getTreatmentCode());
        xmlTransportOrder.setInformation(order.getDataArea().getInformation());


        return xmlTransportOrder;
    }

    public static String getTime(String date){

        return  date.replaceAll("-","").replaceAll(":","").replaceAll("T","").replaceAll("Z","");

    }
}
