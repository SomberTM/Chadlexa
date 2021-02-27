package Audio;

import java.awt.Component;

import javax.sound.sampled.Mixer;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class InputDeviceSelector extends GUI.Manager {

    public static InputDeviceSelector singleton;

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

        String[] mixers = Utils.Audio.listMixers().toArray(new String[0]);

        JComboBox<String> selection = new JComboBox<String>(mixers);
        selection.setMaximumSize(selection.getPreferredSize());
        selection.setAlignmentX(Component.CENTER_ALIGNMENT);
        singleton.addComboBox(mainPanel, selection, "combo_device_selector");

        // JPanel buttonPanel = new JPanel();
        // buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        // singleton.addPanel(buttonPanel);

        GUI.Button capture = new GUI.Button("Capture");
        capture.setAlignmentX(Component.CENTER_ALIGNMENT);
        singleton.addButton(mainPanel, capture, "button_capture");

        GUI.Button stop = new GUI.Button("Stop");
        stop.setAlignmentX(Component.CENTER_ALIGNMENT);
        singleton.addButton(mainPanel, stop, "button_stop");

        GUI.Button playback = new GUI.Button("Playback");
        playback.setAlignmentX(Component.CENTER_ALIGNMENT);
        singleton.addButton(mainPanel, playback, "button_playback");

        singleton.window.setVisible(true);
        return singleton;
    }

    public static String getSelectedAudioDeviceName() {
        return (String) singleton.getComboBox("combo_device_selector").getSelectedItem();
    }

    public static Mixer getSelectedMixer() {
        return Utils.Audio.findMixer(mixer -> mixer.getMixerInfo().getName().equals(getSelectedAudioDeviceName()));
    }

}