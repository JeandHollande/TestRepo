package com.example.raildriver;

import org.hid4java.HidDevice;
import org.hid4java.HidManager;
import org.hid4java.HidServices;

public class RailDriverApp
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

    rdDevice.addListener(state -> System.out.println("Throttle: " + state.throttle()));

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
}
