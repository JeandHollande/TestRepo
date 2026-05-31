package com.jbsoft.raildriver.control;

import com.jbsoft.raildriver.util.BinaryUtil;

public class ReportDebugger
{

  private byte[] previous;

  public void printDiff(byte[] current)
  {

    if (previous == null)
    {
      previous = current.clone();
      return;
    }

    for (int i = 0; i < current.length; i++)
    {

      int oldVal = Byte.toUnsignedInt(previous[i]);
      int newVal = Byte.toUnsignedInt(current[i]);

      if (oldVal != newVal)
      {

        System.out.printf(
                          "Byte %02d changed: %02X -> %02X bitpattern: %s enum value:%s%n",
                          i,
                          oldVal,
                          newVal,
                          BinaryUtil.showBitPattern((byte) newVal),
                          RDButton.getEnumInstance(i,
                                                         (byte) newVal));
      }
    }

    previous = current.clone();
  }
}