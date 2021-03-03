package src.main.java.com.chadlexa.app.Audio;

import java.awt.Component;

import javax.sound.sampled.Mixer;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import src.main.java.com.chadlexa.app.Chad.File;
import src.main.java.com.chadlexa.app.GUI.Button;
import src.main.java.com.chadlexa.app.GUI.Manager;
import src.main.java.com.chadlexa.app.Utils.Audio;

public class InputDeviceSelector extends Manager {

    public static InputDeviceSelector singleton;
    public static File config = new File("config.chad");

    // Define as private because we dont want any outside instances to be created
    private InputDeviceSelector() {
        if (singleton == null)
            singleton = this;
    }

    public static InputDeviceSelector spawn() {
        new InputDeviceSelector();

        final int width = 400;
        final int height = 200;
        singleton.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        singleton.window.setSize(width, height);
        singleton.centerWindow();

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        singleton.addPanel(mainPanel);

        JLabel label = new JLabel("Select your preferred audio input device");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        singleton.addLabel(mainPanel, label);

        String[] mixers = Audio.listMixers().toArray(new String[0]);

        JComboBox<String> selection = new JComboBox<String>(mixers);
        
        selection.setMaximumSize(selection.getPreferredSize());
        selection.setAlignmentX(Component.CENTER_ALIGNMENT);
        singleton.addComboBox(mainPanel, selection, "combo_device_selector");

        // Attempt to load in audio device from config
        try {
            String device = config.get("audio_device");
            if (device != null)
                selection.setSelectedItem(device);
        } catch (Exception e) {}

        Button capture = new Button("Capture");
        capture.setAlignmentX(Component.CENTER_ALIGNMENT);
        singleton.addButton(mainPanel, capture, "button_capture");

        Button stop = new Button("Stop");
        stop.setAlignmentX(Component.CENTER_ALIGNMENT);
        singleton.addButton(mainPanel, stop, "button_stop");

        Button playback = new Button("Playback");
        playback.setAlignmentX(Component.CENTER_ALIGNMENT);
        singleton.addButton(mainPanel, playback, "button_playback");

        Button save = new Button("Save device");
        save.setAlignmentX(Component.CENTER_ALIGNMENT);
        singleton.addButton(mainPanel, save, "button_save_device");

        singleton.window.setVisible(true);
        return singleton;
    }

    public static String getSelectedMixerName() {
        return (String) singleton.getComboBox("combo_device_selector").getSelectedItem();
    }

    public static Mixer getSelectedMixer() {
        return Audio.findMixer(mixer -> mixer.getMixerInfo().getName().equals(getSelectedMixerName()));
    }

}