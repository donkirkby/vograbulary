from heapq import heappush, heappop
from itertools import chain

from gutenberg_loader import fetch_all_words


def find_flights(all_words, buffer_size=0, print_buffer=False):
    flight = []
    previous_word = None
    buf = []
    score = 0
    for word in chain(all_words, ['x']):
        if previous_word is None or previous_word == word:
            overlap = 0
        else:
            overlap = _get_overlap(previous_word, word)
        if overlap > 1:
            flight.append(word)
            score *= overlap
        else:
            if len(flight) > 2:
                heappush(buf, (-score, flight))
                if len(buf) > buffer_size:
                    yield heappop(buf)[1]
            flight = [word]
            score = 1
        previous_word = word
    while print_buffer and buf:
        yield heappop(buf)[1]


def _get_overlap(word1, word2):
    """ Returns the length of the overlap between word1 and word2.

    The number of letters from the end of word1 that match the beginning of
    word2.
    """
    max_overlap = min(len(word1), len(word2))
    max_found = 0
    for size in range(1, max_overlap+1):
        suffix = word1[-size:]
        prefix = word2[:size]
        if suffix == prefix:
            max_found = size
    return max_found


def main():
    print('Loading.')
#     loader = WordLoader()
#     loader.load_valid_words_from_aspell("en_GB")
#     loader.load_valid_words_from_aspell("en_US")
#     all_words = brown.words() + gutenberg.words()
    all_words = fetch_all_words()
    seen = set()
    for flight in find_flights(all_words, buffer_size=200):
        flight_text = ' '.join(flight)
        if flight_text not in seen:
            print(flight_text)
            seen.add(flight_text)
            if len(seen) >= 200:
                break
    print('Done.')


if __name__ == '__main__':
    main()
elif __name__ == '__live_coding__':
    import unittest
    from word_stairs_test import WordStairsTest

    suite = unittest.TestSuite()
    suite.addTest(WordStairsTest("test_best_flight"))
    test_results = unittest.TextTestRunner().run(suite)

    print(test_results.errors)
    print(test_results.failures)
