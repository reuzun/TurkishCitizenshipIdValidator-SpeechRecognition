import speech_recognition as sr
import time
import sys

r = sr.Recognizer()

r.energy_threshold = 50
r.dynamic_energy_threshold = False

with sr.Microphone() as source:
    r.adjust_for_ambient_noise(source)
    try:
        audio = r.listen(source, timeout=2, phrase_time_limit=5)
        strs = r.recognize_google(audio, language='en-US')
        print(strs.replace(" ", "").replace("-", ""))
    except sr.WaitTimeoutError:
        print("You did not speak or we did not hear due to background noise.")

    except sr.UnknownValueError:
        print("-> Couldnt understand you!")

    except sr.RequestError:
        print("-> No network Connection!")
