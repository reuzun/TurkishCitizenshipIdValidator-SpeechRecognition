import speech_recognition as sr
import time
import sys

r = sr.Recognizer()

choice = int(sys.argv[1]) #--> For getting argument.

strs = "unknown"

#r.energy_threshold = 50
#r.dynamic_energy_threshold = False

with sr.Microphone() as source:
    r.adjust_for_ambient_noise(source)
    try:
        audio = r.listen(source, timeout=2, phrase_time_limit=8)
        if(choice == 0):strs =r.recognize_google(audio, language='en-GB')#en-US
        if(choice == 1):strs =r.recognize_google(audio, language='tr-tr')#en-US
        print(strs.replace(" ", "").replace("-", ""))
    except sr.WaitTimeoutError:
        print("-> Timeout")

    except sr.UnknownValueError:
        print("-> Couldnt understand")

    except sr.RequestError:
        print("-> no network connection")


