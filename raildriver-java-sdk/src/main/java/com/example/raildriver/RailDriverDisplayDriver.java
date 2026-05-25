package com.example.raildriver;

import java.util.Arrays;

import org.hid4java.HidDevice;
import org.hid4java.HidManager;
import org.hid4java.HidServices;

public class RailDriverDisplayDriver
{
  private static final short VENDOR_ID = 0x05F3;
  private static final short PRODUCT_ID = 0x00D2;
  public static final String SERIAL_NUMBER = null;
  private static final byte LED_COMMAND = (byte) 134; // Command code to set the LEDs.

  private static final int WIDTH = 3;

  private final byte[] framebuffer = new byte[WIDTH];

  private final HidDevice device;

  // calibration result
  private int offset = 1; // default guess
  private int packetSize = 7;

  public RailDriverDisplayDriver(HidDevice device)
  {
    this.device = device;
    clear();
  }

  public static void main(String[] args)
      throws InterruptedException
  {
    HidServices hidServices = HidManager.getHidServices();
    RailDriverDisplayDriver display;

    //    showHidDeviceDetails(hidServices);

    HidDevice hidDevice = hidServices.getHidDevice(VENDOR_ID,
                                                   PRODUCT_ID,
                                                   SERIAL_NUMBER);
    if (hidDevice == null)
    {
      System.out.println("RailDriver not found");
      return;
    }

    display = new RailDriverDisplayDriver(hidDevice);

    // 1. Run calibration once
    //    display.autoCalibrate();

    // 2. Pick best result and lock it
    display.setLayout(7,
                      1); // example only

    // 3. Normal usage
    display.setText("JbS");
    display.send();
    Thread.sleep(1000);

    for (String text : Arrays.asList("012",
                                     "Abc",
                                     "345",
                                     "678",
                                     "0.9",
                                     "def",
                                     "opq",
                                     "xyz",
                                     "JBS")) // TODO: xyz werkt niet, het algoritme 'rammelt' vermoedelijk omdat het een 7- ipv 9-segment display is.
    {
      display.setText(text);
      display.send();
      Thread.sleep(1000);
    }
  }

  // ----------------------------
  // Framebuffer API
  // ----------------------------

  public void clear()
  {
    Arrays.fill(framebuffer,
                (byte) 0x00);
  }

  public void setText(String text)
  {
    clear();

    int pos = 0;

    for (int i = 0; i < text.length() && pos < WIDTH; i++)
    {
      char c = text.charAt(i);

      if (c == '.')
      {
        if (pos > 0)
        {
          framebuffer[pos - 1] |= (byte) 0x80;
        }
        continue;
      }

      framebuffer[pos] = encode(c);
      pos++;
    }
  }

  private byte encode(char c)
  {
    if (Character.isDigit(c))
    {
      return DIGITS[c - '0'];
    }

    if (c >= 'A' && c <= 'Z')
      return ALPHA[c - 'A'];
    if (c >= 'a' && c <= 'z')
      return ALPHA[c - 'a'];

    switch (c)
    {
      case '-':
        return 0x40;
      case '?':
        return 0x53;
      case ' ':
      case '_':
        return 0x00;
      default:
        return 0x00;
    }
  }

  // 7-seg tables (standard common-cathode style)
  private static final byte[] DIGITS =
  {
      0x3f, 0x06, 0x5b, 0x4f, 0x66,
      0x6d, 0x7d, 0x07, 0x7f, 0x6f
  };

  private static final byte[] ALPHA =
  {
      0x77, 0x7c, 0x39, 0x5e, 0x79, 0x71,
      0x6f, 0x76, 0x04, 0x1e,
      0x70, 0x38, 0x54, 0x23, 0x5c,
      0x73, 0x67, 0x50, 0x6d, 0x44,
      0x1c, 0x62, 0x14, 0x36, 0x72, 0x49
  };

  // ----------------------------
  // Send
  // ----------------------------

  public void send()
  {
    byte[] packet = buildPacket();
    device.write(packet,
                 packet.length,
                 LED_COMMAND);
  }

  private byte[] buildPacket()
  {

    byte[] packet = new byte[packetSize];

    // header (RailDriver command byte)
    packet[0] = (byte) 0x86;

    for (int i = 0; i < WIDTH; i++)
    {
      int targetIndex = offset + i;

      if (targetIndex < packet.length)
      {
        packet[targetIndex] = framebuffer[WIDTH - 1 - i];
      }
    }

    return packet;
  }

  // ----------------------------
  // Auto calibration
  // ----------------------------

  public void autoCalibrate()
  {

    System.out.println("Starting RailDriver calibration...");

    int[] candidateSizes =
    {
        7, 8, 9
    };
    int[] candidateOffsets =
    {
        1, 2
    };

    String test = "JbS";

    for (int size : candidateSizes)
    {
      for (int off : candidateOffsets)
      {

        this.packetSize = size;
        this.offset = off;

        setText(test);
        send();

        sleep(400);

        System.out.println(
                           "Tried size=" + size + " offset=" + off);
      }
    }

    System.out.println("Calibration done. Pick the setting that shows correct output.");
  }

  private void sleep(long ms)
  {
    try
    {
      Thread.sleep(ms);
    }
    catch (Exception ignored)
    {
    }
  }

  // ----------------------------
  // Manual override
  // ----------------------------

  public void setLayout(int packetSize,
      int offset)
  {
    this.packetSize = packetSize;
    this.offset = offset;
  }
}
