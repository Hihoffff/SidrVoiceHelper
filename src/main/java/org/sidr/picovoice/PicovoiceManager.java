package org.sidr.picovoice;

import ai.picovoice.porcupine.Porcupine;
import ai.picovoice.porcupine.PorcupineException;
import org.sidr.Sidr;

import javax.sound.sampled.TargetDataLine;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class PicovoiceManager {
    private final Sidr sidr;
    private Porcupine picovoice;
    TargetDataLine microphone;
    private short[] picoVoiceBuffer;
    private ByteBuffer captureBuffer;

    public PicovoiceManager(Sidr sidr, TargetDataLine microphone){
        this.sidr = sidr;
        this.microphone = microphone;
    }
    public void load() throws PorcupineException {
        try{
            this.picovoice = new Porcupine.Builder()
                    .setKeywordPath(sidr.getPropertiesManager().getWakeNamePicoVoicePath())
                    .setModelPath(sidr.getPropertiesManager().getPorcupineNamePicoVoicePath())
                    .setAccessKey(sidr.getPropertiesManager().getTokenPicoVoice())
                    .build();
            picoVoiceBuffer = new short[picovoice.getFrameLength()];
            captureBuffer = ByteBuffer.allocate(picovoice.getFrameLength() * 2);
            captureBuffer.order(ByteOrder.LITTLE_ENDIAN);
        } catch (PorcupineException e) {
            System.err.println("Ошибка при загрузке модели picovoice!");
            e.printStackTrace();
        }
    }
    public void start(){
        try{
            int count=0;



            int numBytesRead;
            boolean isWakeUp= false;
            while (!isWakeUp) {
                numBytesRead = microphone.read(captureBuffer.array(), 0, captureBuffer.capacity());
                if (numBytesRead != picovoice.getFrameLength() * 2) {
                    continue;
                }
                captureBuffer.asShortBuffer().get(picoVoiceBuffer);
                int detected = picovoice.process(picoVoiceBuffer);

                if(detected==0){
                    System.out.println("Ключевое слово распознано!"+count);
                    count++;
                    break;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
