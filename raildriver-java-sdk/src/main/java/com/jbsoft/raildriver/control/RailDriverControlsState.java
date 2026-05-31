package com.jbsoft.raildriver.control;

public record RailDriverControlsState(
    Integer throttle,
    Integer autoBrake,
    Integer indBrake,
    RDButton buttonPressed)
{
}
