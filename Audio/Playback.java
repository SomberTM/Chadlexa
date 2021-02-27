package Audio;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class Playback {
    
    private Capture capture;
    private AudioInputStream audioInputStream;
    private SourceDataLine sourceDataLine;
    private byte[] buffer = new byte[10000];

    public Playback(Capture capture) {
        this.capture = capture;
    }

    public void playback() { 
        try {
            this.playback(this.capture.getCapturedData().toByteArray()); 
        } catch (NullPointerException exception) {
            System.out.println("No audio to playback");
        }
    }

    public void playback(byte[] audioData) {
        try {
            InputStream inputStream = new ByteArrayInputStream(audioData);
            this.audioInputStream = new AudioInputStream(inputStream, this.capture.format, audioData.length / this.capture.format.getFrameSize());
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, this.capture.format);
            this.sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            this.sourceDataLine.open(this.capture.format);
            this.sourceDataLine.start();
        } catch (Exception e) { System.out.println("Error formatting playback audio"); }

        if (this.audioInputStream != null && this.sourceDataLine != null)
            Utils.Functional.createThread(() -> {
                try {
                    int content;
        
                    while ( (content = audioInputStream.read(buffer, 0, buffer.length)) != -1) 
                        if (content > 0)
                            sourceDataLine.write(buffer, 0, content);
                    
                    sourceDataLine.drain();
                    sourceDataLine.close();
        
                    System.out.println("Finished audio playback");
                } catch(Exception e) { System.out.println("Error playing back audio"); }
            }).start();              
    }

}
