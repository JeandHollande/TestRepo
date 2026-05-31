package com.jbsoft.raildriver.control;

import java.util.ArrayList;
import java.util.List;

import org.hid4java.HidDevice;

public class RailDriverDevice
    implements Runnable
{

  private final HidDevice m_device;
  private final List<RailDriverControlsListener> m_listeners = new ArrayList<>();
  private final RailDriverControlsDecoder m_decoder = new RailDriverControlsDecoder();

  private volatile boolean m_running = true;

  public RailDriverDevice(HidDevice device)
  {
    this.m_device = device;
  }

  public void addListener(RailDriverControlsListener rdListener)
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
      // Uncomment to see detailed information about each control
      //      reportDebugger.printDiff(buffer);

      if (n > 0)
      {
        RailDriverControlsState rdControlsState = m_decoder.getRailDriverControlsState(buffer);

        for (RailDriverControlsListener rdListener : m_listeners)
        {
          // rdListen is an interface which implementation is defined in the addListener call
          // See RailDriverControlsTester.handleThrottleState(RailDriverControlsState)
          rdListener.onState(rdControlsState);
        }
      }
    }

    m_device.close();
  }
}
