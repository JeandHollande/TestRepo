package com.jbsoft.raildriver.control;

import org.hid4java.HidDevice;
import org.hid4java.HidManager;
import org.hid4java.HidServices;

public class RailDriverControlsTester
{
  private static final short VENDOR_ID = 0x05F3;
  private static final short PRODUCT_ID = 0x00D2;
  public static final String SERIAL_NUMBER = null;

  public static void main(String[] args)
  {
    HidServices hidServices = HidManager.getHidServices();

    showHidDeviceDetails(hidServices);

    HidDevice hidDevice = hidServices.getHidDevice(VENDOR_ID,
                                                   PRODUCT_ID,
                                                   SERIAL_NUMBER);
    if (hidDevice == null)
    {
      System.out.println("RailDriver not found");
      return;
    }

    RailDriverDevice rdDevice = new RailDriverDevice(hidDevice);

    // Lambda will get executed when change is detected in throttle related byte
    // This lambda is an implementation of RailDriverListener.onState(RailDriverThrottleState)
    rdDevice.addListener(RailDriverControlsTester::handleThrottleState);

    new Thread(rdDevice).start();
  }

  private static void showHidDeviceDetails(HidServices hidServices)
  {

    for (HidDevice device : hidServices.getAttachedHidDevices())
    {
      System.out.println("----");
      System.out.println("Path: " + device.getPath());
      System.out.println("UsagePage: " + device.getUsagePage());
      System.out.println("Usage: " + device.getUsage());
      System.out.println("Interface: " + device.getInterfaceNumber());
      System.out.println("Product: " + device.getProduct());

      boolean ok = device.open();

      System.out.println("Open result: " + ok);

      if (ok)
      {
        device.close();
      }

      if (device.isVidPidSerial(VENDOR_ID,
                                PRODUCT_ID,
                                SERIAL_NUMBER))
      {
        System.out
            .printf("Found the deviced related to vendor id=%s / product id=%s / serial number=%s. Specific details:%n",
                    VENDOR_ID,
                    PRODUCT_ID,
                    SERIAL_NUMBER);
        System.out.println("Path: " + device.getPath());
        System.out.println("VendorId: " + device.getVendorId());
        System.out.println("ProductId: " + device.getProductId());
        System.out.println("Serial: " + device.getSerialNumber());
        System.out.println("Manufacturer: " + device.getManufacturer());
        System.out.println("Product: " + device.getProduct());
        System.out.println("UsagePage: " + device.getUsagePage());
        System.out.println("Usage: " + device.getUsage());
        System.out.println("Interface: " + device.getInterfaceNumber());
      }
    }
  }

  private static void handleThrottleState(RailDriverControlsState rdControlsState)
  {
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
