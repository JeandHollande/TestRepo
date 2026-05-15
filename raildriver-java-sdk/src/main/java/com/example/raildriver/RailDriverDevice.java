package com.example.raildriver;

import java.util.ArrayList;
import java.util.List;

import org.hid4java.HidDevice;

public class RailDriverDevice
    implements Runnable
{

  private final HidDevice m_device;
  private final List<RailDriverListener> m_listeners = new ArrayList<>();
  private final RailDriverDecoder m_decoder = new RailDriverDecoder();

  private volatile boolean m_running = true;

  public RailDriverDevice(HidDevice device)
  {
    this.m_device = device;
  }

  public void addListener(RailDriverListener rdListener)
  {
    m_listeners.add(rdListener);
  }

  public void stop()
  {
    m_running = false;
  }

  @Override
  public void run()
  {
    ReportDebugger reportDebugger = new ReportDebugger();
    m_device.open();

    byte[] buffer = new byte[64];
    while (m_running)
    {

      int n = m_device.read(buffer,
                            1000);
      reportDebugger.printDiff(buffer);

      if (n > 0)
      {
        RailDriverState state = m_decoder.decode(buffer);

        for (RailDriverListener rdListener : m_listeners)
        {
          rdListener.onState(state);
        }
      }
    }

    m_device.close();
  }
}
