/**
 * Created by IntelliJ IDEA.
 * Author: Zhouyue
 * Date: 2008-2-16
 * Time: 12:55:05
 * Copyright Dsl.Worgsoft. 
 */
package com.asrs.communication;

import org.apache.commons.lang.StringUtils;

public class SeqGenerator
{
//    private static SeqGenerator instance = new SeqGenerator();
//
//    public static SeqGenerator Instance()
//    {
//        return instance;
//    }
//
//    private SeqGenerator()
//    {
//    }


    private int seqNoToBeTransmitted = 0;
    private int seqNoToBeReceived = 0;

    public void reset()
    {
        seqNoToBeTransmitted = 0;
        seqNoToBeReceived = 0;
    }


    public synchronized String getSeqNoToBeTransmitted()
    {
//			lock(typeof(SequenceNo))
//			{
        if (9999 == seqNoToBeTransmitted)
        {
            seqNoToBeTransmitted = 1;
            return ("9999");
        }
        else
        {
            return StringUtils.leftPad(String.valueOf(seqNoToBeTransmitted++), 4, '0');
        }
//			}
    }

    public void setSeqNoToBeTransmitted(String seqNo)
    {

        seqNoToBeTransmitted = Integer.parseInt(seqNo);

    }

    public String getSeqNoToBeReceived()
    {
//			lock(typeof(SequenceNo))
        if (9999 == seqNoToBeReceived)
        {
            seqNoToBeReceived = 1;
            return ("9999");
        }
        else
        {
            return StringUtils.leftPad(String.valueOf(seqNoToBeReceived++), 4, '0');
        }
    }

    public void setSeqNoToBeReceived(String seqNo)
    {
        seqNoToBeReceived = Integer.parseInt(seqNo);
    }

    public boolean isValidSeqNo(int seqNo)
    {

            if (seqNo != seqNoToBeReceived - 1)
            {
                seqNoToBeReceived = seqNo + 1;
                return true;
            }
            else
            {
                return false;
            }
        
    }
}
