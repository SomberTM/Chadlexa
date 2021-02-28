import Audio.*;

public class Program {


    static Audio.Capture audioCapture;

    public static void main(String[] args) {

        InputDeviceSelector gui = InputDeviceSelector.spawn();
        Class<GUI.Button> cast = GUI.Button.class;

        gui.getButton("button_capture", cast).onClick(event -> {
            Program.audioCapture = new Audio.Capture();
            Program.audioCapture.capture(InputDeviceSelector.getSelectedMixer());
        });

        gui.getButton("button_stop", cast).onClick(event -> {
            Program.audioCapture.stopCapture();
        });

        gui.getButton("button_playback", cast).onClick(event -> {
            new Audio.Playback(Program.audioCapture).playback();
        });

        gui.getButton("button_save_device", cast).onClick(event -> {
            InputDeviceSelector.config.set("audio_device", InputDeviceSelector.getSelectedMixerName());
        });

    }

}