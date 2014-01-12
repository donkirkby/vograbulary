from nltk.corpus import brown
from nltk.corpus import gutenberg
from nltk.probability import FreqDist
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
        is_summary_printed = False
        
        if is_summary_printed:
            print '%d words' % len(self._valid_words)
            print '-' * 10
        freqdist = FreqDist(word.lower() for 
            word in all_words if 
            word.lower() in self._valid_words)
        word_count = 0
        for word in freqdist.keys():
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

if __name__ == '__main__':
    loader = WordLoader()
    loader.load_valid_words('/usr/share/dict/british-english')
    loader.load_valid_words('/usr/share/dict/american-english')
    all_words = brown.words() + gutenberg.words()
    loader.dump_words(all_words)
elif __name__ == '__live_coding__':
    loader = WordLoader()
    is_valid = loader.is_valid('potato')
    loader.load_valid_words('sample.txt')
    loader.load_valid_words('sample2.txt')
    loader._valid_words = set("this is it a sample sentence long two extra".split())

    all_words = "This is it : a sample sentence . It isn't long , is it ?".split()
 
    loader.dump_words(all_words)
