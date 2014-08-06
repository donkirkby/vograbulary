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
                words.sort()
                score = combo_freq * pair_score
                min_length = min(len(inner), len(outer))
                if prefix in ('un', 'dis') and suffix == 'able':
                    continue
                if suffix in ('ing', 's', 'y'):
                    continue
                if suffix in ('d', 'ed') and combo.endswith('ed'):
                    continue
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
    
    def filter_sandwiches(self, lines, all_words):
        freqdist = self.find_frequent_words(all_words)
        for line in lines:
            _, word1, word2 = line.split()
            self.merge_words(word1, word2, freqdist)
            self.merge_words(word2, word1, freqdist)
    
    def find_barconyms(self, word, reversed_word, seen_words):
        for j in range(0,len(word)-1):
            for k in range(j+1, len(word)):
                swapped_list = list(reversed_word)
                swapped_list[j], swapped_list[k] = (swapped_list[k],
                                                    swapped_list[j])
                swapped_word = ''.join(swapped_list)
                if swapped_word in seen_words:
                    if swapped_word == 'name':
                        print '---' + word
                    return swapped_word
            
    def find_bacronyms(self, all_words):
        freqdist = self.find_frequent_words(all_words)
        sorted_words = freqdist.keys()
        freqdist= None
        seen_words = set()
        self.bacronyms = []
        barconyms = []
        print 'Starting...'
        for i, word in enumerate(sorted_words):
#             if i > 2000:
#                 break
            if len(word) < 4:
                continue
            reversed_word = word[::-1]
            if reversed_word in seen_words:
                self.bacronyms.append(reversed_word)
            else:
                barconym = self.find_barconyms(word, reversed_word, seen_words)
                if barconym is not None:
                    barconyms.append(barconym)
                seen_words.add(word)
        
        self.barconyms = []
        for word in barconyms:
            if word not in self.bacronyms and word not in self.barconyms:
                self.barconyms.append(word)
        print len(self.bacronyms), len(self.barconyms)
        output_count = min(len(self.bacronyms), len(self.barconyms)/2)
        for i in range(output_count):
            words = {self.bacronyms[i],
                     self.barconyms[2*i],
                     self.barconyms[2*i+1]}
            print ' '.join(words)

if __name__ == '__main__':
    loader = WordLoader()
    loader.load_valid_words('/usr/share/dict/british-english')
    loader.load_valid_words('/usr/share/dict/american-english')
    all_words = brown.words() + gutenberg.words()

    loader.find_bacronyms(all_words)
#     loader.dump_words(all_words)
#    loader.print_sandwiches(all_words)
#     f = open("/home/don/Documents/RussianDolls2.txt")
#     try:
#         loader.filter_sandwiches(f, all_words)
#     finally:
#         f.close()
elif __name__ == '__live_coding__':
    loader = WordLoader()
    is_valid = loader.is_valid('potato')
    loader.load_valid_words('sample.txt')
    loader.load_valid_words('sample2.txt')
    loader._valid_words = set("net ten dear read lengthy pear reap mean name amen".split())

    all_words = "net ten ten dear dear read read raed lengthy pear reap mean name name amen".split()
 
#     loader.dump_words(all_words)
#     loader.print_sandwiches(all_words)
    lines = ['1e-5 plain coming\n',
             '1e-6 ot a\n']
    loader.find_bacronyms(all_words)
