package com.chadlexa.app;

import java.io.ByteArrayInputStream;

import com.chadlexa.app.Audio.*;
import com.chadlexa.app.Chad.File;
import com.chadlexa.app.GUI.*;

import com.ibm.cloud.sdk.core.http.Response;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.speech_to_text.v1.SpeechToText;
import com.ibm.watson.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.speech_to_text.v1.model.SpeechRecognitionResults;

public class Program {


    static Capture audioCapture;

    public static void main(String[] args) {
        File env = new File("env.chad");
        InputDeviceSelector gui = InputDeviceSelector.spawn();
        Class<Button> cast = Button.class;

        IamAuthenticator authenticator = new IamAuthenticator(env.get("ibm_api_key"));
        SpeechToText speech = new SpeechToText(authenticator);
        speech.setServiceUrl(env.get("ibm_service_url"));

        gui.getButton("button_capture", cast).onClick(event -> {
            Program.audioCapture = new Capture();
            Program.audioCapture.start(InputDeviceSelector.getSelectedMixer());
        });

        gui.getButton("button_stop", cast).onClick(event -> {
            Program.audioCapture.stop();
            try {
                byte[] bytes = Program.audioCapture.getOutputStream().toByteArray();
                ByteArrayInputStream stream = new ByteArrayInputStream(bytes);

                int rate = (int) Program.audioCapture.format.getSampleRate();
                RecognizeOptions options = new RecognizeOptions.Builder().contentType(String.format("audio/x-float-array;rate=%d", rate)).audio(stream).build();
                
                Response<SpeechRecognitionResults> results = speech.recognize(options).execute();
                System.out.println(results.getResult().toString());
            } catch (Exception e) {
                System.out.println("Error recognizing audio " + e);
            }
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