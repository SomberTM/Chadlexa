package src.main.java.com.chadlexa.app.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;

public class Audio {

    private Audio() {}
    
    public static List<Mixer.Info> getMixerInfo() {  
        return new ArrayList<Mixer.Info>(Arrays.asList(AudioSystem.getMixerInfo()));
    }

    public static List<Mixer> getMixers() {
        return getMixerInfo().stream().map(mixer -> getMixer(mixer)).collect(Collectors.toList());
    }

    public static List<String> listMixers() {
        return getMixerInfo().stream().map(mixer -> mixer.getName()).collect(Collectors.toList());
    }

    public static <T> Mixer findMixer(Function<Mixer, Boolean> filter) {
        for (Mixer mixer : getMixers())
            if (filter.apply(mixer))
                return mixer;
        return null;
    }

    public static <T> List<T> mapMixerInfo(Function<? super Mixer.Info, ? extends T> function) {
        return getMixerInfo().stream().map(function).collect(Collectors.toList());
    }

    public static <T> List<T> mapMixerInfo(ArrayList<Mixer.Info> info, Function<? super Mixer.Info, ? extends T> function) {
        return info.stream().map(function).collect(Collectors.toList());
    }

    public static Mixer getMixer(Mixer.Info info) {
        return AudioSystem.getMixer(info);
    }

    public static enum Quality {
        LOW,
        MEDIUM,
        HIGH
    }

    public static AudioFormat getAudioFormat(Quality quality) {
        float sampleRate = 8000.0f;
        int sampleSizeInBits = 8;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = false;
        switch (quality) {
            case HIGH:
                sampleRate = 44100.0f;
                sampleSizeInBits = 16;
                channels = 2;
                break;
            case MEDIUM:
                sampleRate = 16000.0f;
                sampleSizeInBits = 16;
                break;
            case LOW:
                break;
        }
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }

    public static AudioFormat getBasicAudioFormat() {
        /**
         Sample rate in samples per second.  (Allowable values include 8000, 11025, 16000, 22050, and 44100 samples per second.)
         Sample size in bits.  (Allowable values include 8 and 16 bits per sample.)
         Number of channels.  (Allowable values include 1 channel for mono and 2 channels for stereo.)
         Signed or unsigned data.  (Allowable values include true and false for signed data or unsigned data.)
         Big-endian or little-endian order.  (This has to do with the order in which the data bytes are stored in memory.  You can learn about this topic here.)
         */
        float sampleRate = 44100.0f;
        int sampleSizeInBits = 16;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = false;

        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }

}
