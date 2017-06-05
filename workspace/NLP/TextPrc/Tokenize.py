# coding=UTF-8
#import nltk
#nltk.download()

import csv
import pandas as pd
from nltk.corpus import gutenberg   # Docs from project gutenberg.org
#from scrapy.item import Field
files_en = gutenberg.fileids()      # Get file ids
doc_en = gutenberg.open('C:\\Python27\\NLPK\\pg158.txt').read()


from nltk import regexp_tokenize
pattern = r'''(?x) ([A-Z]\.)+ | \w+(-\w+)* | \$?\d+(\.\d+)?%? | \.\.\. | [][.,;"'?():-_`]'''
tokens_en = regexp_tokenize(doc_en, pattern)


import nltk
en = nltk.Text(tokens_en)



# CSV Field 
# 

import csv
import nltk
import os.path
import sys

### read csv
def readcsv():
    f = open('C:\\temp\\Steer\\1.txt', 'r')
    csvReader = csv.reader(f)
    matrix = []
    for row in csvReader:
        matrix.append(row[0])  
    return matrix

### tokenize
def tokenize(matrix) :
    tokens = []
    for s in matrix:
        tokens += nltk.tokenize.word_tokenize(s.lower())
    return tokens

def get_stemmed_list(tokens) :
    from nltk.corpus import stopwords
    stop_words = stopwords.words('english') + ['.', ',', '--', '\'s', '?', ')', '(', ':', '\'', '\'re', '"',
        '-', '}', '{', u'—', 'rt', 'http', 't', 'co', '@', '#', '/', u'…',
        u'#', u';',  u'amp', u't', u'co', u']', u'[', u'`', u'`', u'&', u'|', u'\u265b', u"''", u'$', u'//', u'/'
        u'via',  u'...', u'!', u'``', u'http']

    from nltk.stem import PorterStemmer
    stemmer = PorterStemmer()
    stemmed = []
    for token in tokens:
        # try to decode token
        try:
            decoded = token.decode('utf8')
            #print decoded
        except UnicodeError:
            decoded = token

        if decoded is '' or decoded in stop_words:
            continue
        stem = stemmer.stem(decoded)
        #print stem
        # Skip a few text. I don't know why stopwords are not working :(
        #skip t.co things
        if stem.find(u't.co') > 0:
            continue
        #skip http things
        elif stem.find(u'http') >= 0:
            #print stem
            continue
        else:
            stemmed.append(stem)
    return stemmed

matrix = readcsv()
tokens = tokenize(matrix)
print tokens

stemmed = get_stemmed_list(tokens)
print stemmed

filepath_exp = 'C:\\temp\\steer\\1_3_out.csv'

df = pd.DataFrame(stemmed, columns=['token'])
df.to_csv(filepath_exp, sep=',', encoding='utf-8')
