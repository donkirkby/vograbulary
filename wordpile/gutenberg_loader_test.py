from unittest import TestCase
from zipfile import ZipFile

from mock import patch
from gutenberg_loader import fetch_all_words, GutenbergCache
from tempfile import mkdtemp
import shutil
import os
from StringIO import StringIO
from mock.mock import call


class GutenbergLoaderTest(TestCase):
    @patch('requests.get')
    @patch('gutenberg_loader.GutenbergCache')
    def test_simple(self, mock_cache, mock_requests_get):
        index_lines = [
            'garbage href="http://host/foo.zip" more garbage',
            'line with no links']
        mock_requests_get.return_value.iter_lines.return_value = index_lines
        expected_url = 'http://host/foo.zip'
        text1_lines = ['Title',
                       'The text of a book.']
        mock_cache.return_value.fetch_lines.return_value = text1_lines
        expected_words = ['Title', 'The', 'text', 'of', 'a', 'book.']

        words = list(fetch_all_words())

        self.assertEqual(expected_words, words)
        mock_cache.return_value.fetch_lines.assert_called_once_with(
            expected_url)

    @patch('requests.get')
    @patch('gutenberg_loader.GutenbergCache')
    def test_two_index_pages(self, mock_cache, mock_requests_get):
        index_pages = [['book link href="http://host/foo.zip"',
                        'next page href="local_page.html" link'],
                       ['book link href="http://host/bar.zip"',
                        'last page, no link']]
        mock_requests_get.return_value.iter_lines.side_effect = index_pages
        expected_cache_requests = [call('http://host/foo.zip'),
                                   call('http://host/bar.zip')]
        text1_lines = ['Title',
                       'The text of a book.']
        text2_lines = ['A short book.']
        mock_cache.return_value.fetch_lines.side_effect = [text1_lines,
                                                           text2_lines]
        expected_words = ['Title',
                          'The', 'text', 'of', 'a', 'book.',
                          'A', 'short', 'book.']

        words = list(fetch_all_words())

        self.assertEqual(expected_words, words)
        self.assertEqual(expected_cache_requests,
                         mock_cache.return_value.fetch_lines.mock_calls)
        mock_requests_get.assert_called_with(
            'http://www.gutenberg.org/robot/local_page.html',
            stream=True)

    @patch('requests.get')
    @patch('gutenberg_loader.GutenbergCache')
    def test_dtd_skipped(self, mock_cache, mock_requests_get):
        index_lines = [
            'DTD "http://host/foo.xml" stuff',
            'garbage href="http://host/bar.zip" more garbage',
            'line with no links']
        mock_requests_get.return_value.iter_lines.return_value = index_lines
        expected_url = 'http://host/bar.zip'
        text1_lines = ['Title',
                       'The text of a book.']
        mock_cache.return_value.fetch_lines.return_value = text1_lines
        expected_words = ['Title', 'The', 'text', 'of', 'a', 'book.']

        words = list(fetch_all_words())

        self.assertEqual(expected_words, words)
        mock_cache.return_value.fetch_lines.assert_called_once_with(
            expected_url)


class GutenbergCacheTest(TestCase):
    def setUp(self):
        self.cache_path = mkdtemp(dir='.')

    def tearDown(self):
        shutil.rmtree(self.cache_path, ignore_errors=True)

    @patch('requests.get')
    def test_simple(self, mock_requests_get):
        expected_lines = ['Title',
                          'The text of a book']
        url = 'http://host/child/foo.zip'
        expected_zip_path = os.path.join(self.cache_path, 'child', 'foo.zip')
        content = StringIO()
        with ZipFile(content, 'w') as z:
            z.writestr('book.txt', '\n'.join(expected_lines))
        mock_requests_get.return_value.iter_content.return_value = [
            content.getvalue()]

        cache = GutenbergCache(self.cache_path)
        lines = list(cache.fetch_lines(url))

        self.assertEqual(expected_lines, lines)
        with ZipFile(expected_zip_path, 'r') as z:
            self.assertEqual(['book.txt'], z.namelist())
            self.assertEqual('\n'.join(expected_lines), z.read('book.txt'))

    def test_cache_hit(self):
        expected_lines = ['Title',
                          'The text of a book']
        os.mkdir(os.path.join(self.cache_path, 'child'))
        zip_path = os.path.join(self.cache_path, 'child', 'foo.zip')
        with ZipFile(zip_path, 'w') as z:
            z.writestr('book.txt', '\n'.join(expected_lines))

        cache = GutenbergCache(self.cache_path)
        lines = list(cache.fetch_lines('http://host/child/foo.zip'))

        self.assertEqual(expected_lines, lines)
