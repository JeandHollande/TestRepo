package com.example.raildriver;

import java.util.Arrays;

import org.hid4java.HidDevice;
import org.hid4java.HidManager;
import org.hid4java.HidServices;

public class RailDriverLedTester
{
  private static final short VENDOR_ID = 0x05F3;
  private static final short PRODUCT_ID = 0x00D2;
  public static final String SERIAL_NUMBER = null;
  private static final byte LED_COMMAND = (byte) 134; // Command code to set the LEDs.

  public static void main(String[] args)
      throws InterruptedException
  {
    HidServices hidServices = HidManager.getHidServices();

    //    showHidDeviceDetails(hidServices);

    HidDevice hidDevice = hidServices.getHidDevice(VENDOR_ID,
                                                   PRODUCT_ID,
                                                   SERIAL_NUMBER);
    if (hidDevice == null)
    {
      System.out.println("RailDriver not found");
      return;
    }

    for (String display : Arrays.asList("012",
                                        "Abc",
                                        "345",
                                        "678",
                                        "0.9",
                                        "def",
                                        "opq",
                                        "xyz")) // TODO: xyz werkt niet, het algoritme 'rammelt' vermoedelijk omdat het een 7- ipv 9-segment display is.
    {
      byte[] ledBufferContent = createLEDBufferContent(display);
      sendMessage(hidDevice,
                  ledBufferContent,
                  LED_COMMAND);
      Thread.sleep(1000);
    }
  }

  public static byte[] createLEDBufferContent(String ledstring)
  {
    byte SevenSegment[] =
    {
        //'0'   '1'   '2'   '3'   '4'   '5'   '6'   '7'   '8'   '9'
        0x3f, 0x06, 0x5b, 0x4f, 0x66, 0x6d, 0x7d, 0x07, 0x7f, 0x6f
    };
    byte SevenSegmentAlpha[] =
    {
        //'A'   'b'   'C'   'd'   'E'   'F'   'g'   'H'   'i'   'J'
        0x77, 0x7C, 0x39, 0x5E, 0x79, 0x71, 0x6F, 0x76, 0x04, 0x1E,
        //'K'   'L'   'm'   'n'   'o'   'P'   'q'   'r'   's'   't'
        0x70, 0x38, 0x54, 0x23, 0x5C, 0x73, 0x67, 0x50, 0x6D, 0x44,
        //'u'   'v'   'W'   'X'   'y'   'z'
        0x1C, 0x62, 0x14, 0x36, 0x72, 0x49
    };
    // other seven segment display patterns
    byte BLANKSEGMENT = 0x00;
    byte QUESTIONMARK = 0x53;
    byte DASHSEGMENT = 0x40;
    byte DPSEGMENT = (byte) 0x80;

    byte[] buff = new byte[7]; // Segment buffer.
    Arrays.fill(buff,
                (byte) 0);

    int outIdx = 3;

    buff[0] = (byte) 0x86;
    for (int i = 0; i < ledstring.length(); i++)
    {
      char c = ledstring.charAt(i);
      if (Character.isDigit(c))
      {
        //log.debug("buff[{}] = {}", outIdx, "" + c);
        // Get seven segment code for digit.
        buff[outIdx] = SevenSegment[c - '0'];
      }
      else if (Character.isWhitespace(c))
      {
        buff[outIdx] = BLANKSEGMENT;
      }
      else if (c == '_')
      {
        buff[outIdx] = BLANKSEGMENT;
      }
      else if (c == '?')
      {
        buff[outIdx] = QUESTIONMARK;
      }
      else if ((c >= 'A') && (c <= 'Z'))
      {
        // Get seven segment code for alpha.
        buff[outIdx] = SevenSegmentAlpha[c - 'A'];
      }
      else if ((c >= 'a') && (c <= 'z'))
      {
        // Get seven segment code for alpha.
        buff[outIdx] = SevenSegmentAlpha[c - 'a'];
      }
      else if (c == '-')
      {
        buff[outIdx] = DASHSEGMENT;
      }
      else // Is it a decimal point?
      if (c == '.')
      {
        // If so, OR in the decimal point segment.
        buff[outIdx + 1] |= DPSEGMENT;
        outIdx++;
      }
      else
      { // everything else is ignored
        System.out.printf("Character cannot be displayed: %s%n",
                          c);
        outIdx++;
      }
      outIdx--;
      if (outIdx < 0)
      {
        if (++i < ledstring.length())
        {
          if (ledstring.charAt(i) == '.')
          {
            buff[0] |= DPSEGMENT;
          }
        }
        break;
      }
    }

    System.out.printf("Sending data: ");
    for (int i = 1; i <= 3; i++)
    {
      System.out.printf("%02X ",
                        buff[i]);
    }
    System.out.println();
    return buff;
  }

  private static void sendMessage(HidDevice hidDevice,
      byte[] message,
      byte reportID)
  {
    if (!hidDevice.isOpen())
    {
      hidDevice.open();
    }

    try
    {
      int ret = hidDevice.write(message,
                                message.length,
                                reportID);
      if (ret >= 0)
      {
        System.out.printf("hidDevice.write returned: %s%n",
                          ret);
      }
      else
      {
        System.out.printf("hidDevice.write error: %s%n",
                          hidDevice.getLastErrorMessage());
      }
    }
    catch (IllegalStateException ex)
    {
      ex.printStackTrace();
    }

  }
}
