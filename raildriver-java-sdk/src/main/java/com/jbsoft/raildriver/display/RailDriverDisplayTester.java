package com.jbsoft.raildriver.display;

import java.util.Arrays;

import org.hid4java.HidDevice;
import org.hid4java.HidManager;
import org.hid4java.HidServices;

import com.jbsoft.raildriver.util.BinaryUtil;
import com.jbsoft.raildriver.util.PlatformUtil;

public class RailDriverDisplayTester
{
  private static final short VENDOR_ID = 0x05F3;
  private static final short PRODUCT_ID = 0x00D2;
  private static final String SERIAL_NUMBER = null;
  private static final byte LED_COMMAND = (byte) 134; // Command code to set the LEDs.
  public static final byte SEG_A = 0x01;
  public static final byte SEG_B = 0x02;
  public static final byte SEG_C = 0x04;
  public static final byte SEG_D = 0x08;
  public static final byte SEG_E = 0x10;
  public static final byte SEG_F = 0x20;
  public static final byte SEG_G = 0x40;
  public static final byte SEG_DP = (byte) 0x80;

  private static final int WIDTH = 3;

  private final byte[] framebuffer = new byte[WIDTH];

  private final HidDevice device;

  // On linux the first byte should be 0x00, on linux this should be 
  private int offset = PlatformUtil.isWindows() ? 1 : 0;
  // Windows: byte 1 is LED_COMMAND, byte 2,3,4 is LED1,LED2,LED3 
  // Linux: byte 0 is LED_COMMAND, byte 1,2,3 is LED1,LED2,LED3 
  private int packetSize = PlatformUtil.isWindows() ? 4 : 3;

  public RailDriverDisplayTester(HidDevice device)
  {
    this.device = device;
    clear();
  }

  public static void main(String[] args)
      throws InterruptedException
  {
    HidServices hidServices = HidManager.getHidServices();
    RailDriverDisplayTester display;

    //    showHidDeviceDetails(hidServices);

    HidDevice hidDevice = hidServices.getHidDevice(VENDOR_ID,
                                                   PRODUCT_ID,
                                                   SERIAL_NUMBER);
    if (hidDevice == null)
    {
      System.out.println("RailDriver not found");
      return;
    }

    display = new RailDriverDisplayTester(hidDevice);

    display.setSegmentsAndSleep(SEG_A,
                                SEG_A,
                                SEG_A,
                                500);

    int ms = 1000;
    testText(display,
             ms);

    for (int i = 0; i < 8; i++)
    {

      byte byteContent = (byte) (1 << i);

      String bitPattern = BinaryUtil.showBitPattern(byteContent);

      System.out.printf(
                        "bit %d = %02X --> %s%n",
                        i,
                        byteContent,
                        bitPattern);

      display.setSegmentsAndSleep(byteContent,
                                  byteContent,
                                  byteContent,
                                  3000);
    }
  }

  private static void testText(RailDriverDisplayTester display,
      int ms)
      throws InterruptedException
  {
    for (String text : Arrays.asList("0",
                                     "1",
                                     "0  ",
                                     " 0 ",
                                     "  0",
                                     "012",
                                     "Abc",
                                     "345",
                                     "678",
                                     "0.9",
                                     "def",
                                     "opq",
                                     "xyz",
                                     "JBS")) // TODO: xyz werkt niet, het algoritme 'rammelt' vermoedelijk omdat het een 7- ipv 9-segment display is.
    {
      displayAndSleep(display,
                      ms,
                      text);
    }
  }

  private static void displayAndSleep(RailDriverDisplayTester display,
      int ms,
      String text)
      throws InterruptedException
  {
    display.setText(text);
    display.send();
    Thread.sleep(ms);
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

    int digitPosition = 0; // 0 is the first digit, 2 is the last digit

    for (int i = 0; i < text.length() && digitPosition < WIDTH; i++)
    {
      char c = text.charAt(i);

      if (c == '.')
      {
        if (digitPosition > 0)
        {
          framebuffer[digitPosition - 1] |= (byte) 0x80;
        }
        continue;
      }

      framebuffer[digitPosition] = encode(c);
      digitPosition++;
    }
  }

  private void setSegmentsAndSleep(byte segmentsDigitPosition1,
      byte segmentsDigitPosition2,
      byte segmentsDigitPosition3,
      int ms)
      throws InterruptedException
  {
    clear();

    framebuffer[0] = segmentsDigitPosition1;
    framebuffer[1] = segmentsDigitPosition2;
    framebuffer[2] = segmentsDigitPosition3;
    send();
    Thread.sleep(ms);
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

  public void setLayout(int packetSize,
      int offset)
  {
    this.packetSize = packetSize;
    this.offset = offset;
  }
}
