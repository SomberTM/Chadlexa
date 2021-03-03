package src.main.java.com.chadlexa.app;

import src.main.java.com.chadlexa.app.Audio.*;
import src.main.java.com.chadlexa.app.GUI.*;

public class Program {


    static Capture audioCapture;

    public static void main(String[] args) {

        InputDeviceSelector gui = InputDeviceSelector.spawn();
        Class<Button> cast = Button.class;

        gui.getButton("button_capture", cast).onClick(event -> {
            Program.audioCapture = new Capture();
            Program.audioCapture.capture(InputDeviceSelector.getSelectedMixer());
        });

        gui.getButton("button_stop", cast).onClick(event -> {
            Program.audioCapture.stopCapture();
        });

        gui.getButton("button_playback", cast).onClick(event -> {
            new Playback(Program.audioCapture).playback();
        });

        gui.getButton("button_save_device", cast).onClick(event -> {
            String device = InputDeviceSelector.getSelectedMixerName();
            InputDeviceSelector.config.set("audio_device", device);
            System.out.println("Set default audio device to \"" + device + "\"");
        });

    }

}