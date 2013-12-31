from nltk.corpus import brown
from nltk.corpus import gutenberg
from nltk.probability import FreqDist
from re import match

max_words = 70000
is_summary_printed = False
valid_words = set()

all_words = brown.words() + gutenberg.words()

for word in all_words:
    lower_word = word.lower()
    if lower_word == word:
        valid_words.add(lower_word)

if is_summary_printed:
    print '%d words' % len(valid_words)
    print '-' * 10
                        
freqdist = FreqDist(word.lower() 
                    for word in all_words
                    if word.lower() in valid_words)
word_count = 0
for word, freq in freqdist.items():
    if match(r'^[a-z]+$', word) and freq > 1:
        print word
        word_count += 1
        if word_count >= max_words:
            break

if is_summary_printed:
    print '-' * 10
    print '%d words' % word_count
