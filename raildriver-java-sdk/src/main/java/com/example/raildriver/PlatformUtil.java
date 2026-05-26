package com.example.raildriver;

public final class PlatformUtil
{

  private static final String OS =
      System.getProperty("os.name").toLowerCase();

  public static boolean isLinux()
  {
    return OS.contains("linux");
  }

  public static boolean isWindows()
  {
    return OS.contains("win");
  }

  public static boolean isMac()
  {
    return OS.contains("mac");
  }
}
