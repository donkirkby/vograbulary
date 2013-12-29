from nltk.corpus import brown
from nltk.probability import FreqDist
from re import match

freqdist = FreqDist(w.lower() for w in brown.words())
word_count = 0
for word, freq in freqdist.items():
    if match(r'^[a-z]+$', word):
        print word
        word_count += 1
        if word_count > 70000:
            exit()
