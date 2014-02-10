from nltk.corpus import brown
from nltk.corpus import gutenberg
from nltk.probability import FreqDist
from random import shuffle
from re import match

class WordLoader(object):
    def __init__(self):
        self._max_words = 70000
        self._valid_words = set()
        
    def is_valid(self, word):
        return match(r'^[a-z]+$', word) is not None

    def load_valid_words(self, filename):
        f = open(filename)
        for word in f:
            stripped = word.strip()
            if(self.is_valid(stripped)):
                self._valid_words.add(stripped)
        f.close()

    def dump_words(self, all_words):
        """ Take a long list of words from several texts, and use them as a
        source for word frequencies. Then dump all the valid words, most
        frequent first. """
        
        is_summary_printed = False
        
        if is_summary_printed:
            print '%d words' % len(self._valid_words)
            print '-' * 10
        freq_list = self.find_frequent_words(all_words).keys()
        word_count = 0
        for word in freq_list:
            if word_count >= self._max_words:
                break
            print word
            word_count += 1
            self._valid_words.remove(word)
        for word in self._valid_words:
            if word_count >= self._max_words:
                break
            print word
            word_count += 1
        
        if is_summary_printed:
            print '-' * 10
            print '%d words' % word_count
    

    def merge_words(self, outer, inner, freqdist):
        pair_score = 0
        for i in range(1, len(outer)):
            prefix = outer[:i]
            suffix = outer[i:]
            combo = prefix + inner + suffix
            combo_freq = freqdist.freq(combo)
            if combo_freq > 0:
                if pair_score == 0:
                    pair_score = freqdist.freq(outer) * freqdist.freq(inner)
                words = [inner, outer]
                shuffle(words)
                score = combo_freq * pair_score
                min_length = min(len(inner), len(outer))
                if min_length >= 3:
                    print score, words[0], words[1]

    def print_sandwiches(self, all_words):
        freqdist = self.find_frequent_words(all_words)
        unique_words = freqdist.keys()
        word_count = len(unique_words)
        for i in range(word_count):
            word1 = unique_words[i]
            for j in range(i):
                word2 = unique_words[j]
                self.merge_words(word1, word2, freqdist)
                self.merge_words(word2, word1, freqdist)
            
    def find_frequent_words(self, all_words):
        freqdist = FreqDist(word.lower() for 
            word in all_words if 
            word.lower() in self._valid_words)
        return freqdist

if __name__ == '__main__':
    loader = WordLoader()
    loader.load_valid_words('/usr/share/dict/british-english')
    loader.load_valid_words('/usr/share/dict/american-english')
    all_words = brown.words() + gutenberg.words()

#     loader.dump_words(all_words)
    loader.print_sandwiches(all_words)
elif __name__ == '__live_coding__':
    loader = WordLoader()
    is_valid = loader.is_valid('potato')
    loader.load_valid_words('sample.txt')
    loader.load_valid_words('sample2.txt')
    loader._valid_words = set("ten sentense sense oat".split())

    all_words = "ten sentense sense oat This is it : a sample sentence . It isn't long , is it ?".split()
 
#     loader.dump_words(all_words)
    loader.print_sandwiches(all_words)
