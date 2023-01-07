package com.monkey.automation.monkey_automation_service.configuration.helpers;

import com.monkey.automation.monkey_automation_service.configuration.helpers.enums.BuzzerNote;
import com.monkey.automation.monkey_automation_service.configuration.helpers.enums.PIN;
import com.pi4j.context.Context;
import com.pi4j.io.pwm.Pwm;
import com.pi4j.io.pwm.PwmConfig;
import com.pi4j.io.pwm.PwmType;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class Buzzer {

    /**
     * PI4J PWM used by this buzzer
     */
    protected final Pwm pwm;
    /**
     * the Thread, under which the melodies are played.
     */
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private Runnable playWorker = () -> {};

    /**
     * Creates a new buzzer component with a custom BCM pin.
     *
     * @param pi4j    Pi4J context
     * @param address Custom BCM pin address
     */
    public Buzzer(Context pi4j, PIN address) {
        this.pwm = pi4j.create(buildPwmConfig(pi4j, address));
        log.debug("Created new Buzzer Component");
    }

    /**
     * Builds a new PWM configuration for the buzzer
     *
     * @param pi4j    Pi4J context
     * @param address BCM pin address
     * @return PWM    configuration
     */
    protected static PwmConfig buildPwmConfig(Context pi4j, PIN address) {
        return Pwm.newConfigBuilder(pi4j)
                .id("BCM" + address)
                .name("Buzzer")
                .address(address.getPin())
                .pwmType(PwmType.HARDWARE)
                .initial(100)
                .shutdown(100)
                .provider("pigpio-pwm")
                .build();
    }

    /**
     * Plays a tone with the given frequency in Hz indefinitely.
     * This method is non-blocking and returns immediately.
     * A frequency of zero causes the buzzer to play silence.
     *
     * @param frequency Frequency in Hz
     */
    public void playTone(int frequency) {
        playTone(frequency, 0);
    }

    /**
     * Plays a tone with the given frequency in Hz for a specific duration.
     * This method is blocking and will sleep until the specified duration has passed.
     * A frequency of zero causes the buzzer to play silence.
     * A duration of zero to play the tone indefinitely and return immediately.
     *
     * @param frequency Frequency in Hz
     * @param duration  Duration in milliseconds
     */
    public void playTone(int frequency, int duration) {
        if (frequency > 0) {
            // Activate the PWM with a duty cycle of 50% and the given frequency in Hz.
            // This causes the buzzer to be on for half of the time during each cycle, resulting in the desired frequency.
            pwm.on(50, frequency);

            // If the duration is larger than zero, the tone should be automatically stopped after the given duration.
            if (duration > 0) {
                delay(duration);
            }
        }
        playSilence();
    }

    /**
     * plays a defined Melody out of Sounds, which are compromised from
     * a frequency and an amount of beats
     *
     * @param tempo  how fast should it be played
     * @param sounds the defined sounds, can be an Array
     */
    public void playMelody(int tempo, Sound... sounds){
        //named thread to set it as Daemon and start it
        playWorker = () -> {
            //to begin the melody, we first wait for 8 beats to pass
            playSilence(tempo * 8);
            for (Sound s : sounds) {
                playNote(s.getNote(), tempo, s.beats);
            }
            //when the melody is finished, we turn it off
            playSilence();
        };
        executor.submit(playWorker);
    }

    /**
     * plays a defined Melody out of Sounds, which are compromised from
     * a frequency and an amount of beats for several times
     *
     * @param tempo       how fast should it be played
     * @param repetitions how much it should be played
     * @param sounds      the defined sounds, can be an Array
     */
    public void playMelody(int tempo, int repetitions, Sound... sounds){
        //named thread to set it as Daemon and start it
        playWorker = () -> {
            for (int i = 0; i < repetitions; i++) {
                //to begin the melody, we first wait for 8 beats to pass
                //we can't just call the other playMelody, as it would always
                //wait for the 8 beats to finish between each repetition
                playSilence(tempo * 8);
                for (Sound s : sounds) {
                    playNote(s.getNote(), tempo, s.beats);
                }
                //when the melody is finished, we turn it off
                playSilence();
            }
        };
        executor.submit(playWorker);
    }

    /**
     * Plays a simple note
     *
     * @param note  Which frequency on the buzzer
     * @param tempo how fast shall it be played?
     * @param beats how many beats long
     */
    public void playNote(BuzzerNote note, int tempo, int beats) {
        playTone(note.getFrequency(), tempo * beats);
        //playSilence is used, because if the Note is not long enough,
        //the component wouldn't have enough time to change frequency
        playSilence(20);
    }

    /**
     * Silences the buzzer and returns immediately.
     */
    public void playSilence() {
        pwm.on(100,BuzzerNote.PAUSE.getFrequency());
        //pwm.off();
    }

    /**
     * Silences the buzzer and waits for the given duration.
     * This method is blocking and will sleep until the specified duration has passed.
     *
     * @param duration Duration in milliseconds
     */
    public void playSilence(int duration) {
        playSilence();
        delay(duration);
    }

    /**
     * Returns the created PWM instance for the buzzer
     *
     * @return PWM instance
     */
    protected Pwm getPwm() {
        return this.pwm;
    }

    protected void delay(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
