package Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;

public class Audio {
    
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

    public static AudioFormat getBasicAudioFormat() {
        float sampleRate = 8000.0f;
        int sampleSizeInBits = 16;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = false;

        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }

}
