package com.chadlexa.app.Audio;

import java.io.ByteArrayOutputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;

import com.chadlexa.app.Utils.Audio;
import com.chadlexa.app.Utils.Functional;

public class Capture {

    public AudioFormat format = Audio.getAudioFormat(Audio.Quality.HIGH);
    public DataLine.Info dataLineInfo;
    public Mixer mixer;

    private boolean stop = false;
    private byte[] tempBuffer = new byte[10000];
    private ByteArrayOutputStream outputStream;
    private TargetDataLine targetDataLine;

    public Capture() {
        super();

        this.dataLineInfo = new DataLine.Info(TargetDataLine.class, this.format);
    }

    public ByteArrayOutputStream getOutputStream() { return this.outputStream; }
    
    public void start(Mixer mixer) {
        System.out.println("Capture request received");
        this.mixer = mixer;

        try {
            this.targetDataLine = (TargetDataLine) this.mixer.getLine(this.dataLineInfo);
            this.targetDataLine.open(this.format);
            this.targetDataLine.start();
        } catch (Exception exception) {
            System.out.println("[Audio.Capture]: Unsupported mixer");
        } 

        if (this.targetDataLine != null)
            Functional.createThread(() -> {
                outputStream = new ByteArrayOutputStream();

                while (!stop) {
                    int content = targetDataLine.read(tempBuffer, 0, tempBuffer.length);
        
                    if (content > 0)
                        outputStream.write(tempBuffer, 0, content);
                }
            }).start();
    }

    public void stop() {
        System.out.println("Stop request received");
        this.stop = true;
    }
    
}
