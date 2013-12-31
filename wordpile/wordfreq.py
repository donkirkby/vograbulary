from nltk.corpus import brown
from nltk.probability import FreqDist
from re import match

word_counts = {}
max_words = 70000
excluded_tags = {'NP'}

class WordCount(object):
    total = 0
    excluded = 0
    seen_lower = False
    
    def is_valid(self):
        return self.seen_lower
#         return self.excluded < self.total / 2 # less than 50% excluded

default_count = WordCount()
default_count.total = 1
default_count.excluded = 1

for word, tag in brown.tagged_words():
    if '-' not in tag: # exclude headlines, titles, and foreign words
        lower_word = word.lower()
        word_count = word_counts.get(lower_word)
        if word_count is None:
            word_count = WordCount()
            word_counts[lower_word] = word_count
        word_count.total += 1
        if tag in excluded_tags:
            word_count.excluded += 1
        if lower_word == word:
            word_count.seen_lower = True
            
freqdist = FreqDist(word.lower() 
                    for word in brown.words()
                    if word_counts.get(word.lower(), default_count).is_valid())
word_count = 0
for word, freq in freqdist.items():
    if match(r'^[a-z]+$', word):
        print word
        word_count += 1
        if word_count > max_words:
            exit()
