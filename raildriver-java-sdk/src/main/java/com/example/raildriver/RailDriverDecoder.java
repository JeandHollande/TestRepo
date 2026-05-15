package com.example.raildriver;

public class RailDriverDecoder {

    private int lastThrottle = -1;

    public RailDriverState decode(byte[] data) {

        int throttleRaw = Byte.toUnsignedInt(data[1]);

        if (throttleRaw != lastThrottle) {
            lastThrottle = throttleRaw;
        }

        float throttle = throttleRaw / 255.0f;

        return new RailDriverState(throttle, 0.0f);
    }
}
