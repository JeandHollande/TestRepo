package com.jbsoft.raildriver.control;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public enum RDButton
{
  // example enum instances — replace or extend as needed
  BUTTON_TOP_ROW_1(7, (byte) 0x01),
  BUTTON_TOP_ROW_2(7, (byte) 0x02),
  BUTTON_TOP_ROW_3(7, (byte) 0x04),
  BUTTON_TOP_ROW_4(7, (byte) 0x08),
  BUTTON_TOP_ROW_5(7, (byte) 0x10),
  BUTTON_TOP_ROW_6(7, (byte) 0x20),
  BUTTON_TOP_ROW_7(7, (byte) 0x40),
  BUTTON_TOP_ROW_8(7, (byte) 0x80),
  BUTTON_TOP_ROW_9(8, (byte) 0x01),
  BUTTON_TOP_ROW_10(8, (byte) 0x02),
  BUTTON_TOP_ROW_11(8, (byte) 0x04),
  BUTTON_TOP_ROW_12(8, (byte) 0x08),
  BUTTON_TOP_ROW_13(8, (byte) 0x10),
  BUTTON_TOP_ROW_14(8, (byte) 0x20),
  BUTTON_BOTTOM_ROW_1(8, (byte) 0x40),
  BUTTON_BOTTOM_ROW_2(8, (byte) 0x80),
  BUTTON_BOTTOM_ROW_3(9, (byte) 0x01),
  BUTTON_BOTTOM_ROW_4(9, (byte) 0x02),
  BUTTON_BOTTOM_ROW_5(9, (byte) 0x04),
  BUTTON_BOTTOM_ROW_6(9, (byte) 0x08),
  BUTTON_BOTTOM_ROW_7(9, (byte) 0x10),
  BUTTON_BOTTOM_ROW_8(9, (byte) 0x20),
  BUTTON_BOTTOM_ROW_9(9, (byte) 0x40),
  BUTTON_BOTTOM_ROW_10(9, (byte) 0x80),
  BUTTON_BOTTOM_ROW_11(10, (byte) 0x01),
  BUTTON_BOTTOM_ROW_12(10, (byte) 0x02),
  BUTTON_BOTTOM_ROW_13(10, (byte) 0x04),
  BUTTON_BOTTOM_ROW_14(10, (byte) 0x08);

  private final int m_byteSequenceNumber;
  private final byte m_byteContent;

  // lookup map for efficient reverse lookup
  private static final Map<Key, RDButton> ENUM_INSTANCES_BY_FIELDS = new HashMap<>();

  static
  {
    for (RDButton rdInputControl : values())
    {
      ENUM_INSTANCES_BY_FIELDS.put(new Key(rdInputControl.m_byteSequenceNumber,
                                           rdInputControl.m_byteContent),
                                   rdInputControl);
    }
  }

  RDButton(int byteSequenceNumber,
      byte byteContent)
  {
    m_byteSequenceNumber = byteSequenceNumber;
    m_byteContent = byteContent;

  }

  public byte getId()
  {
    return m_byteContent;
  }

  public int getCode()
  {
    return m_byteSequenceNumber;
  }

  /**
   * Find the enum instance that matches the given id (byte) and code (int). Returns null if none matches.
   */
  public static RDButton getEnumInstance(int byteSequenceNumber,
      byte byteContent)
  {
    return ENUM_INSTANCES_BY_FIELDS.get(new Key(byteSequenceNumber,
                                                byteContent));
  }

  // small key class for map lookup
  private static final class Key
  {
    private final int byteSequenceNumber;
    private final byte byteContent;

    Key(int byteSequenceNumber,
        byte byteContent)
    {
      this.byteSequenceNumber = byteSequenceNumber;
      this.byteContent = byteContent;
    }

    @Override
    public boolean equals(Object o)
    {
      if (this == o)
        return true;
      if (!(o instanceof Key))
        return false;
      Key k = (Key) o;
      return byteContent == k.byteContent && byteSequenceNumber == k.byteSequenceNumber;
    }

    @Override
    public int hashCode()
    {
      return Objects.hash(byteContent,
                          byteSequenceNumber);
    }
  }
}
