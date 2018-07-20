package com.asrs.message;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

public class Message55 extends Message implements Serializable {
    private String id = "55";
    private String plcName = "";


    public String LoadCount = "";
    //public Map<Integer, Map<String, String>> McKeysAndBarcodes = new HashMap<Integer, Map<String, String>>();
    public String mcKey ="";
    public String barcode="";
    public String Load = "";
    public String height = "";
    public String width = "";
    public String weight = "";
    public String machineNo="";

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getPlcName() {
        return plcName;
    }

    public void setPlcName(String plcName) {
        this.plcName = plcName;
    }

    public String DataCount = "";
    //public Map<String, Block> MachineNos = new HashMap<String, Block>();

    public Message55() {
    }

    public Message55(String str) throws MsgException {
        try {
            DataCount = str.substring(0, 1);
            int index = 1;
            machineNo = str.substring(index, index + 4);
            index += 4;
            //Block block = new Block();
            LoadCount = str.substring(index, index + 1);
            index += 1;
            mcKey = str.substring(index, index + 4);
            index += 4;
            barcode = str.substring(index, index + 10);
            index += 10;
            //Map<String, String> mcKeyAndBarcodes = new HashMap<String, String>();
            //mcKeyAndBarcodes.put(mcKey, barcode);
           // McKeysAndBarcodes.put(j, mcKeyAndBarcodes);
            Load = str.substring(index, index + 1);
            index += 1;
            height = str.substring(index, ++index);
            width = str.substring(index, ++index);
            weight = str.substring(index, index + 6);
            index += 6;

            //MachineNos.put(machineNo, block);
        } catch (Exception ex) {
            throw new MsgException("MsgException.Invalid_length   " + str);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(StringUtils.rightPad(DataCount, 1, '0'));
        //for (String machineNo : MachineNos.keySet()) {
            sb.append(StringUtils.rightPad(machineNo, 4, '0'));
            //Block block = MachineNos.get(machineNo);
            sb.append(StringUtils.rightPad(LoadCount, 1, '0'));
            int loadCount = Integer.parseInt(LoadCount);
            //for (int j = 1; j <= loadCount; j++) {
               // Map<String, String> mcKeyAndBarcode = block.McKeysAndBarcodes.get(j);

                sb.append(StringUtils.rightPad(mcKey, 4, '0'));
                sb.append(StringUtils.rightPad(barcode, 10, '0'));
            //}
            sb.append(StringUtils.rightPad(Load, 1, '0'));
            sb.append(StringUtils.rightPad(height, 1, '0'));
            sb.append(StringUtils.rightPad(width, 1, '0'));
            sb.append(StringUtils.rightPad(weight, 6, '0'));
        //}
        return sb.toString();
    }

   /* public class Block  {

        public String LoadCount = "";
        public Map<Integer, Map<String, String>> McKeysAndBarcodes = new HashMap<Integer, Map<String, String>>();
        public String Load = "";
        public String height = "";
        public String width = "";
        public String weight = "";
    }*/
}
