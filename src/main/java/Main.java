import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import model.BUILDER_TYPE;
import model.TurkishCitizenshipId;
import model.TurkishCitizenshipIdBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;



/**
 * @author reuzun
 */



public class Main {

    static int lang = -1; // 0 For english, 1 For Turkish

    public static void main(String[] args) throws Exception {
        Installer installer = new Installer();

        installer.install("s2tlang.py");
        installer.install("s2t.py");

        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");

        Voice voice = VoiceManager.getInstance().getVoice("kevin16");
        voice.setRate(180);
        voice.setPitch(90);
        voice.allocate();


        TurkishCitizenshipId validator = new TurkishCitizenshipIdBuilder()
                .build(BUILDER_TYPE.VALIDATOR);


        System.out.println("Select your language with speaking. English for English and Turkish for Turkish.");

        // let the program to wait for speak.
        Thread speakingThread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                voice.speak("English or Turkish?");
            }
        });

        speakingThread1.start();
        speakingThread1.join();


        String c = null;
        boolean isFirst = true;
        while(lang == -1){
            if(!isFirst) voice.speak("Repeat please");
            isFirst = false;
            System.out.println("Now You can speak. If you see before this message. We couldnt understand you in your last try.");
            Process langProcess = Runtime.getRuntime().exec("python "+  installer.getLocation("s2tlang.py"));
            BufferedReader langReader = new BufferedReader(new
                    InputStreamReader(langProcess.getInputStream()));
            while ((c = langReader.readLine()) != null) {
                System.out.println("What i hear is : " + c);
                if (c.startsWith("E") || c.startsWith("e")) lang = 0;
                else if (c.startsWith("T") || c.startsWith("t")) lang = 1;
                else continue;
            }
        }

        System.out.println("Language is set to : " + (lang==0?"English":"Turkish") );
        voice.speak("Language is set to : " + (lang==0?"English":"Turkish"));


        String s = null; //
        boolean flag = true;

        while (flag) {

            Process p = Runtime.getRuntime().exec("python " + installer.getLocation("s2t.py") + " "  + lang);

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));

            // read the output from the command
            voice.speak("Say your Id");
            System.out.println("Say your Id after 1 second:\n");
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s); //227 340 137 98
                try {
                    boolean ans = validator.validate(s);
                    if(ans){flag = false; break;}
                    else System.out.println("Id is not verified. Try Again!");
                } catch (Exception e) {
                    voice.speak("Please retry to tell your ID.");
                    System.out.println("Please retry to tell your ID.");
                }
            }

        }

        System.out.println("Id verified.");
        voice.speak("Your Id is verified!");

    }

}
