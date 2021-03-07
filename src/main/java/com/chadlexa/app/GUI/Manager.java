package com.chadlexa.app.GUI;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Manager {
    
    protected JFrame window;

    private long pHash = 0L;
    protected Map<String, JPanel> panels = new HashMap<>();

    private long lHash = 0L;
    protected Map<String, JLabel> labels = new HashMap<>();

    private long bHash = 0L;
    protected Map<String, JButton> buttons = new HashMap<>();

    private long cHash = 0L;
    protected Map<String, JComboBox<? extends Object>> comboBoxes = new HashMap<>();

    public Manager() {
        this("Empty Window");
    }

    public Manager(String text) {
        this.window = new JFrame(text);
    }

    public Manager(int width, int height) {
        this();
        this.window.setSize(width, height);
    }

    public Manager(int width, int height, String title) {
        this(width, height);
        this.window.setTitle(title);
    }

    public void centerWindow() {
        Dimension winSize = this.window.getSize();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.window.setLocation((int)(screenSize.getWidth() / 2) - (int)(winSize.getWidth() / 2), (int)(screenSize.getHeight() / 2) - (int)(winSize.getHeight() / 2));
    }

    // ---

    public JPanel addPanel(JPanel panel) {
        this.pHash++;
        return this.addPanel(panel, String.format("Panel#%d", pHash));
    }

    public JPanel addPanel(JPanel panel, String name) {
        this.panels.put(name, panel);
        this.window.add(panel);
        return panel;
    }

    // ---

    public <T extends JButton> T addButton(JPanel parent, T button) {
        this.bHash++;
        return this.addButton(parent, button, String.format("Button#%d", bHash));
    }

    public <T extends JButton> T addButton(JPanel parent, T button, String name) {
        if (!this.panels.containsValue(parent))
            this.addPanel(parent);
        this.buttons.put(name, button);
        parent.add(button);
        return button;
    }

    // ---

    public JLabel addLabel(JPanel parent, JLabel label) {
        this.lHash++;
        return this.addLabel(parent, label, String.format("Label#%d", this.lHash));
    }

    public JLabel addLabel(JPanel parent, JLabel label, String name) {
        if (!this.panels.containsValue(parent))
            this.addPanel(parent);
        this.labels.put(name, label);
        parent.add(label);
        return label;
    }

    // ---

    public <T> JComboBox<T> addComboBox(JPanel parent, JComboBox<T> combo) {
        this.cHash++;
        return this.addComboBox(parent, combo, String.format("ComboBox#$d", this.cHash));
    }

    public <T> JComboBox<T> addComboBox(JPanel parent, JComboBox<T> combo, String name) {
        if (!this.panels.containsValue(parent))
            this.addPanel(parent);
        this.comboBoxes.put(name, combo);
        parent.add(combo);
        return combo;
    }

    @SuppressWarnings("unchecked")
    public <T extends JButton> T getButton(String name) {
        return (T) this.buttons.get(name);
    }

    public <T extends JButton> T getButton(String text, Class<T> clazz) {
        return clazz.cast(this.buttons.get(text));
    }

    @SuppressWarnings("unchecked")
    public <T extends Object> JComboBox<T> getComboBox(String name) {
        return (JComboBox<T>) this.comboBoxes.get(name);
    }

    public <T extends Object> JComboBox<T> getComboBox(String name, Class<JComboBox<T>> clazz) {
        return clazz.cast(this.comboBoxes.get(name));
    }

    public JFrame getWindow() { return this.window; }
    public Map<String, JPanel> getPanels() { return this.panels; }
    public Map<String, JLabel> getLabels() { return this.labels; }
    public Map<String, JButton> getButtons() { return this.buttons; }
    public Map<String, JComboBox<? extends Object>> getComboBoxes() { return this.comboBoxes;}
}
