package com.jbsoft.raildriver.util;

import org.hid4java.HidDevice;
import org.hid4java.HidManager;
import org.hid4java.HidServices;

import com.jbsoft.raildriver.control.RailDriverControlsListener;
import com.jbsoft.raildriver.control.RailDriverControlsState;
import com.jbsoft.raildriver.control.RailDriverDevice;

public class RailDriverConnectionManager
{
  private static final short VENDOR_ID = 0x05F3;
  private static final short PRODUCT_ID = 0x00D2;
  public static final String SERIAL_NUMBER = null;

  private RailDriverDevice rdDevice;

  public RailDriverConnectionManager()
  {
    connect();
  }

  public boolean connect()
  {
    HidServices hidServices = HidManager.getHidServices();

    HidDevice hidDevice = hidServices.getHidDevice(VENDOR_ID,
                                                   PRODUCT_ID,
                                                   SERIAL_NUMBER);
    if (hidDevice == null)
    {
      System.out.println("RailDriver not found");
      return false;
    }

    rdDevice = new RailDriverDevice(hidDevice);
    new Thread(rdDevice).start();

    return true;
  }

  public boolean isConnected()
  {
    return rdDevice != null;
  }

  public void addRailDriverControlsListener(RailDriverControlsListener rdCrontrolsListener)
  {
    rdDevice.addListener(rdCrontrolsListener);
  }

  public static void main(String... args)
  {
    RailDriverConnectionManager rdConnectionManager = new RailDriverConnectionManager();

    if (rdConnectionManager.isConnected())
    {
      RailDriverControlsListener rdCrontrolsListener = RailDriverConnectionManager::handleThrottleState;
      // Lambda will get executed when change is detected in throttle related byte
      // This lambda is an implementation of RailDriverListener.onState(RailDriverThrottleState)
      rdConnectionManager.addRailDriverControlsListener(rdCrontrolsListener);
    }
  }

  private static void handleThrottleState(RailDriverControlsState rdControlsState)
  {
    if (rdControlsState == null)
    {
      // Indicates controls did not change
      return;
    }

    if (rdControlsState.throttle() != null)
    {
      System.out.printf("Throttle: %1$d (%1$02X)%n",
                        rdControlsState.throttle());
    }
    if (rdControlsState.autoBrake() != null)
    {
      System.out.printf("Autobreak: %1$d (%1$02X)%n",
                        rdControlsState.autoBrake());
    }
    if (rdControlsState.indBrake() != null)
    {
      System.out.printf("Indbrake: %1$d (%1$02X)%n",
                        rdControlsState.indBrake());
    }
    if (rdControlsState.buttonPressed() != null)
    {
      System.out.printf("Button pressed: %s%n",
                        rdControlsState.buttonPressed().name());
    }
  }
}
