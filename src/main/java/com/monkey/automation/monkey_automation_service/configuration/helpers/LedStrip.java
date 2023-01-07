package com.monkey.automation.monkey_automation_service.configuration.helpers;

import com.pi4j.context.Context;
import com.pi4j.io.spi.Spi;
import com.pi4j.io.spi.SpiConfig;
import com.pi4j.io.spi.SpiMode;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
public class LedStrip {

    /**
     * Default Channel of the SPI Pins
     */
    protected static final int DEFAULT_SPI_CHANNEL = 0;
    /**
     * Minimum time to wait for reset to occur in nanoseconds.
     */
    private static final int LED_RESET_WAIT_TIME = 300_000;
    /**
     * The PI4J SPI
     */
    protected final Spi spi;
    /**
     * The PI4J context
     */
    protected final Context context;
    /**
     * The amount of all LEDs
     */
    private final int numLEDs;
    /**
     * Default frequency of a WS2812 Neopixel Strip
     */
    private final int frequency = 800_000;
    /**
     * between each rendering of the strip, there has to be a reset-time where nothing is written to the SPI
     */
    private final int renderWaitTime;
    /**
     * The array of all pixels
     */
    private final int[] LEDs;
    /**
     * The raw-data of all pixels, each int of LEDs is split into bits and converted to bytes to write
     */
    private final byte[] pixelRaw;
    /**
     * the conversion from bit's of an integer to a byte we can write on the SPI
     */
    private final byte Bit_0 = (byte) 0b11000000;// 192 in Decimal
    private final byte Bit_1 = (byte) 0b11111000;// 248 in Decimal
    private final byte Bit_Reset = (byte) 0b00000000;// 0 in Decimal
    /**
     * Brightness value between 0 and 1
     */
    private double brightness;
    /**
     * The time, when the last rendering happened
     */
    private long lastRenderTime;

    /**
     * Creates a new simpleLed component with a custom BCM pin.
     *
     * @param pi4j       Pi4J context
     * @param numLEDs    How many LEDs are on this Strand
     * @param brightness How bright the leds can be at max, Range 0 - 255
     */
    public LedStrip(Context pi4j, int numLEDs, double brightness) {
        this(pi4j, numLEDs, brightness, DEFAULT_SPI_CHANNEL);
    }

    /**
     * Creates a new simpleLed component with a custom BCM pin.
     *
     * @param pi4j       Pi4J context
     * @param numLEDs    How many LEDs are on this Strand
     * @param brightness How bright the leds can be at max, range 0 - 1
     * @param channel    which channel to use
     */
    public LedStrip(Context pi4j, int numLEDs, double brightness, int channel) {
        if (numLEDs < 1 || brightness < 0 || brightness > 1 || channel < 0 || channel > 1) {
            throw new IllegalArgumentException("Illegal Constructor");
        }
        log.debug("initialising a LED strip with " + numLEDs + " leds");
        this.numLEDs = numLEDs;
        this.LEDs = new int[numLEDs];
        this.brightness = brightness;
        this.context = pi4j;
        this.spi = pi4j.create(buildSpiConfig(pi4j, channel, frequency));

        // The raw bytes that get sent to the LED strip
        // 3 Color channels per led, at 8 bytes each, with 2 reset bytes
        pixelRaw = new byte[(3 * numLEDs * 8) + 2];

        // 1.25us per bit (1250ns)
        renderWaitTime = numLEDs * 3 * 8 * 1250 + LED_RESET_WAIT_TIME;
    }

    /**
     * Builds a new SPI instance for the LED matrix
     *
     * @param pi4j Pi4J context
     * @return SPI instance
     */
    private SpiConfig buildSpiConfig(Context pi4j, int channel, int frequency) {
        return Spi.newConfigBuilder(pi4j)
                .id("SPI" + 1)
                .name("LED Matrix")
                .address(channel)
                .mode(SpiMode.MODE_0)
                .baud(8 * frequency) //bit-banging from Bit to SPI-Byte
                .provider("pigpio-spi")
                .build();
    }

    /**
     * @return the Pi4J context
     */
    public Context getContext() {
        return this.context;
    }

    /**
     * Setting all LEDS off and closing the strip
     */
    public void close() {
        log.info("Turning all leds off before close");
        allOff();
    }

    /**
     * function to get the amount of the leds on the strip
     *
     * @return int with the amount of pixels
     */
    public int getNumPixels() {
        return numLEDs;
    }

    /**
     * function to get the color (as an int) of a specified led
     *
     * @param pixel which position on the LED strip, range 0 - numLEDS-1
     * @return the color of the specified led on the strip
     */
    public int getPixelColor(int pixel) {
        return LEDs[pixel];
    }

    /**
     * setting the color of a specified led on the strip
     *
     * @param pixel which position on the strip, range 0 - numLEDS-1
     * @param color the color that is set
     */
    public void setPixelColor(int pixel, int color) {
        LEDs[pixel] = color;
    }

    /**
     * Setting all leds to the same color
     *
     * @param color the color that is set
     */
    public void setStripColor(int color) {
        Arrays.fill(LEDs, color);
    }

    /**
     * Pixels are sent as follows: - The first transmitted pixel is the pixel
     * closest to the transmitter. - The most significant bit is always sent first.
     * <p>
     * g7,g6,g5,g4,g3,g2,g1,g0,r7,r6,r5,r4,r3,r2,r1,r0,b7,b6,b5,b4,b3,b2,b1,b0
     * \_____________________________________________________________________/ |
     * _________________... | / __________________... | / / ___________________... |
     * / / / GRB,GRB,GRB,GRB,...
     */
    public void render() {
        //beginning at 1, because the first byte is a reset
        int counter = 1;
        for (int i = 0; i < numLEDs; i++) {

            //Scaling the color to the max brightness
            LEDs[i] = PixelColor.setRedComponent(LEDs[i], (int) (PixelColor.getRedComponent(LEDs[i]) * brightness));
            LEDs[i] = PixelColor.setGreenComponent(LEDs[i], (int) (PixelColor.getGreenComponent(LEDs[i]) * brightness));
            LEDs[i] = PixelColor.setBlueComponent(LEDs[i], (int) (PixelColor.getBlueComponent(LEDs[i]) * brightness));

            // Calculating GRB from RGB
            for (int j = 15; j >= 8; j--) {
                if (((LEDs[i] >> j) & 1) == 1) {
                    pixelRaw[counter++] = Bit_1;
                } else {
                    pixelRaw[counter++] = Bit_0;
                }
            }
            for (int j = 23; j >= 16; j--) {
                if (((LEDs[i] >> j) & 1) == 1) {
                    pixelRaw[counter++] = Bit_1;
                } else {
                    pixelRaw[counter++] = Bit_0;
                }
            }
            for (int j = 7; j >= 0; j--) {
                if (((LEDs[i] >> j) & 1) == 1) {
                    pixelRaw[counter++] = Bit_1;
                } else {
                    pixelRaw[counter++] = Bit_0;
                }
            }
        }

        // While bit-banging, the first and last byte have to be a reset
        pixelRaw[0] = Bit_Reset;
        pixelRaw[pixelRaw.length - 1] = Bit_Reset;

        // waiting since last render time
        if (lastRenderTime != 0) {
            int diff = (int) (System.nanoTime() - lastRenderTime);
            if (renderWaitTime - diff > 0) {
                int millis = (renderWaitTime - diff) / 1_000_000;
                int nanos = (renderWaitTime - diff) % 1_000_000;
                sleep(millis, nanos);
            }
        }

        //writing on the PIN
        spi.write(pixelRaw);
        log.debug("finished rendering");
        lastRenderTime = System.nanoTime();
    }

    /**
     * setting all LEDs off
     */
    public void allOff() {
        Arrays.fill(LEDs, 0);
        render();
    }

    /**
     * Utility function to sleep for the specified amount of milliseconds. An {@link InterruptedException} will be caught and ignored while setting the
     * interrupt flag again.
     */
    protected void sleep(long millis, int nanos) {
        try {
            Thread.sleep(millis, nanos);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * @return the current brightness
     */
    public double getBrightness() {
        return this.brightness;
    }

    /**
     * Set the brightness of all LEDs
     *
     * @param brightness new max. brightness, range 0 - 1
     */
    public void setBrightness(double brightness) {
        if (brightness < 0 || brightness > 1) {
            throw new IllegalArgumentException("Illegal Brightness Value. Must be between 0 and 1");
        }
        this.brightness = brightness;
    }

}
