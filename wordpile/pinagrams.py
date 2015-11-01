from collections import defaultdict
from nltk.corpus import brown
from nltk.corpus import gutenberg

from word_loader import WordLoader


def print_anagrams(sorted_words, all_words):
    match_count = 0
    valid_set = set(all_words)
    matches = defaultdict(list)  # {sorted_letters: [word]}
    for word in sorted_words:
        if word in valid_set:
            sorted_letters = ''.join(sorted(word))
            matching_words = matches[sorted_letters]
            matching_words.append(word)
            if len(matching_words) > 1:
                print '\n'.join(matching_words)
                print
                match_count += 1
                if match_count > 100:
                    return


def main():
    loader = WordLoader()
    loader.load_valid_words_from_aspell("en_GB")
    loader.load_valid_words_from_aspell("en_US")
    all_words = brown.words() + gutenberg.words()
    sorted_words_filename = 'sorted_words.txt'
    loader.write_sorted_words(all_words, sorted_words_filename)
    sorted_words = loader.sorted_words
    print_anagrams(sorted_words, all_words)


def test():
    all_words = 'abcd efgh dabc'.split()
    sorted_words = 'abcd dabc efgh'.split()
    print_anagrams(sorted_words, all_words)

if __name__ == '__main__':
    main()
elif __name__ == '__live_coding__':
    test()
