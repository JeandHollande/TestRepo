package com.jbsoft.raildriver.util;

public class BinaryUtil
{
  public static String showBitPattern(byte byteContent)
  {
    return String.format("%8s",
                         Integer.toBinaryString(byteContent & 0xFF))
        .replace(' ',
                 '0');
  }

  public static void main(String... args)
  {
    for (int i = 0; i < 256; i++)
    {
      System.out.printf("int %1$3s: bitPattern=%2$s hexValue=%1$02X%n",
                        i,
                        showBitPattern((byte) i));
    }
  }
}
