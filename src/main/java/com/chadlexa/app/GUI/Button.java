package com.chadlexa.app.GUI;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.Icon;
import javax.swing.JButton;

import com.chadlexa.app.Utils.Functional;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Button extends JButton {

    /**
     *
     */
    private static final long serialVersionUID = 4804268917565034346L;

    private List<Consumer<ActionEvent>> listeners = new ArrayList<>();
    private ActionListener listener;

    public Button() {
        this(null, null);
    }

    public Button(Icon icon) {
        this(null, icon);
    }

    public Button(String text) {
        this(text, null);
    }

    public Button(String text, Icon icon) {
        super(text, icon);
    }

    public void onClick(Consumer<ActionEvent> consumer) {
        this.listeners.add(consumer);
        if (this.listener == null) 
            this.addActionListener( (this.listener = Functional.createActionListener((event) -> {
                for (Consumer<ActionEvent> c : this.listeners)
                    c.accept(event);
            })) );
    }

}
