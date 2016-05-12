from unittest.case import TestCase
from word_stairs import find_flights


class WordStairsTest(TestCase):
    def test_simple(self):
        all_words = "take these seven vendors home".split()
        expected_flights = ["these seven vendors".split()]

        flights = list(find_flights(all_words))

        self.assertEqual(expected_flights, flights)

    def test_single_overlap(self):
        all_words = "best take everyone home".split()
        expected_flights = []

        flights = list(find_flights(all_words))

        self.assertEqual(expected_flights, flights)

    def test_two_word_flight(self):
        all_words = "take these seven people home".split()
        expected_flights = []

        flights = list(find_flights(all_words))

        self.assertEqual(expected_flights, flights)

    def test_repeated_word(self):
        all_words = "go down down down to the river".split()
        expected_flights = []

        flights = list(find_flights(all_words))

        self.assertEqual(expected_flights, flights)

    def test_best_flight(self):
        all_words = """\
take these seven vendors home and read address dressmakers""".split()
        expected_flights = ["read address dressmakers".split()]

        flights = list(find_flights(all_words, buffer_size=1))

        self.assertEqual(expected_flights, flights)
