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
    RDButton rdButton = getRDButton(data);
    Integer sendThrottleValue = null;
    Integer sendAutoBrakeValue = null;
    Integer sendIndBrakeValue = null;
    RDButton sendRDButton = null;

    if (aboveThreshHold(lastThrottle,
                        throttle))
    {
      lastThrottle = throttle;
      sendThrottleValue = throttle;
    }
    if (aboveThreshHold(lastAutoBrakeValue,
                        autoBreak))
    {
      lastAutoBrakeValue = autoBreak;
      sendAutoBrakeValue = autoBreak;
    }
    if (aboveThreshHold(lastIndBrakeValue,
                        indBreak))
    {
      lastIndBrakeValue = indBreak;
      sendIndBrakeValue = indBreak;
    }
    if (rdButton != lastRDButton)
    {
      lastRDButton = rdButton;
      sendRDButton = rdButton;
    }

    if (sendThrottleValue == null && sendAutoBrakeValue == null && sendIndBrakeValue == null && sendRDButton == null)
    {
      return null;
    }

    return new RailDriverControlsState(sendThrottleValue,
                                       sendAutoBrakeValue,
                                       sendIndBrakeValue,
                                       sendRDButton);
  }

  private boolean aboveThreshHold(int previousValue,
      int newValue)
  {
    return Math.abs(previousValue - newValue) > 5;
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
