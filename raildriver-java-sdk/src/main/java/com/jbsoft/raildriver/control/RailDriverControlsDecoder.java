package com.jbsoft.raildriver.control;

public class RailDriverControlsDecoder
{

  private int lastThrottle = -1;
  private int lastAutoBrakeValue = -1;
  private int lastIndBrakeValue = -1;
  private RDButton lastRDButton = null;

  public RailDriverControlsState getRailDriverControlsState(byte[] data)
  {
    int throttle = Byte.toUnsignedInt(data[1]);
    int autoBreak = Byte.toUnsignedInt(data[2]);
    int indBreak = Byte.toUnsignedInt(data[3]);
    RDButton rdButton;
    Integer sendThrottleValue = null;
    Integer sendAutoBrakeValue = null;
    Integer sendIndBrakeValue = null;
    RDButton sendRDButton = null;

    if (throttle != lastThrottle)
    {
      lastThrottle = throttle;
      sendThrottleValue = throttle;
    }
    if (autoBreak != lastAutoBrakeValue)
    {
      lastAutoBrakeValue = autoBreak;
      sendAutoBrakeValue = autoBreak;
    }
    if (indBreak != lastIndBrakeValue)
    {
      lastIndBrakeValue = indBreak;
      sendIndBrakeValue = indBreak;
    }
    rdButton = getRDButton(data);
    if (rdButton != lastRDButton)
    {
      lastRDButton = rdButton;
      sendRDButton = rdButton;
    }

    return new RailDriverControlsState(sendThrottleValue,
                                              sendAutoBrakeValue,
                                              sendIndBrakeValue,
                                              sendRDButton);
  }

  private RDButton getRDButton(byte[] data)
  {
    for (int i = 7; i <= 10; i++)
    {
      if (data[i] != (byte) 0x00)
      {
        return RDButton.getEnumInstance(i,
                                              data[i]);
      }
    }

    return null;
  }
}
