import requests
from zipfile import ZipFile
from urlparse import urlparse
import os
import re
from traceback import print_last


class GutenbergCache(object):
    def __init__(self, cache_path):
        self.cache_path = cache_path

    def fetch_lines(self, url):
        try:
            parts = urlparse(url)
            path = parts.path and parts.path[1:]
            local_path = os.path.join(self.cache_path, path)
            if not os.path.isfile(local_path):
                local_folder = os.path.dirname(local_path)
                if not os.path.isdir(local_folder):
                    os.makedirs(local_folder)
                response = requests.get(url, stream=True)
                with open(local_path, 'wb') as z:
                    for chunk in response.iter_content(1024):
                        z.write(chunk)
            try:
                with ZipFile(local_path, 'r') as z:
                    for filename in z.namelist():
                        for line in z.open(filename):
                            yield line.strip()
            except NotImplementedError:
                # Unsupported compression type
                pass
        except StandardError:
            print_last()


def fetch_all_words(cache_path='gutenberg_cache'):
    cache = GutenbergCache(cache_path)
    index_base = 'http://www.gutenberg.org/robot/'
    # index_url = index_base + 'harvest?filetypes[]=txt&langs[]=en'
    index_url = index_base + 'harvest?offset=40701&filetypes[]=txt&langs[]=en'
    while index_url is not None:
        # print('requesting ' + index_url)
        response = requests.get(index_url, stream=True)
        # print('got ' + index_url)
        index_url = None
        for line in response.iter_lines():
            match = re.match('.*href="([^"]+)"', line)
            if match is not None:
                url = match.group(1).replace('&amp;', '&')
                if not url.startswith('http:'):
                    index_url = index_base + url
                else:
                    text_lines = cache.fetch_lines(url)
                    for text_line in text_lines:
                        for word in text_line.split():
                            yield word

if __name__ == '__main__':
    for word in fetch_all_words():
        print(word)
elif __name__ == '__live_coding__':
    import unittest
    from gutenberg_loader_test import GutenbergLoaderTest

    suite = unittest.TestSuite()
    suite.addTest(GutenbergLoaderTest("test_dtd_skipped"))
    test_results = unittest.TextTestRunner().run(suite)

    print(test_results.errors)
    print(test_results.failures)
