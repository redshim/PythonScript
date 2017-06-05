# coding=UTF-8
import csv
import nltk
import os.path
import sys
from collections import Counter
from cssselect.parser import TokenStream

### read csv
def readcsv():
    f = open('C:\\Python27\\NLPK\\AEN02.csv', 'r')
    csvReader = csv.reader(f)
    matrix = []
    vtype = []
    for row in csvReader:
        matrix.append(row)
    return matrix
    f.close()
    
    
### matrix   matrix[1][6]
### 0 = 
###>>> matrix[0][0]
###'AEN02'
###>>> matrix[0][1]
###'Why Speakers Broken/NW Properly'
###>>> matrix[0][2]
###'Vibrate/rattle'
###>>> matrix[0][3]
###'Acura'
###>>> matrix[0][4]
###'Acura TL'
   
### tokenize
def tokenize(matrix) :
    tokens = []
    vtype = []
    #dic = {}
    for s in matrix:
        tokens.append(nltk.tokenize.word_tokenize(s[5].lower()))
        vtype.append(s[3])
    #    dic[s[3]] = nltk.tokenize.word_tokenize(s[5].lower())
    mtoken = zip(vtype, tokens) 
    return mtoken

def get_stemmed_list(tokens) :
    from nltk.corpus import stopwords
    stop_words = stopwords.words('english') + ['.', ',', '--', '\'s', '?', ')', '(', ':', '\'', '"', "'", "n't", 'copi',
        '-', '}', '{', u'—', 'rt', 'http', 't', 'co', '@', '#', '/', 
        u'#', u';',  u'amp', u't', u'co', u']', u'[', u'`', u'`', u'&', u'|', u"''", u'$', u'//', u'/',
        u'via',  u'...', u'!', u'``', u'http', u'copi', u'ext97' ]

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
        if stem.find(u'copi') > 0:
            continue
        #skip http things
        elif stem.find(u'http') >= 0:
            #print stem
            continue
        else:
            stemmed.append(stem)
    return stemmed

def stopword(mtoken) :
    from nltk.corpus import stopwords
    stop_words = set(stopwords.words('english') + ['.', ',', '--', '\'s', '?', ')', '(', ':', '\'', '"', "'", "n't", 'copi',
        '-', '}', '{', u'—', 'rt', 'http', 't', 'co', '@', '#', '/', 
        u'#', u';',  u'amp', u't', u'co', u']', u'[', u'`', u'`', u'&', u'|', u"''", u'$', u'//', u'/',
        u'via',  u'...', u'!', u'``', u'http', u'copi', u'ext97' ])
                     
#    filtered_sentence = [w for w in tokens if not w in stop_words]
    fs = []
    fsf = []
    vtypen=[]
    for w in mtoken:
        fs = []
        vtypen.append(w[0])
        for wt in w[1]:
            wt = wt.replace("'","")
            wt = wt.replace("\\","")
            if wt not in stop_words:
                if wt == 'copied':  
                    continue
                elif len(wt) < 2:
                    continue
                else:
                    fs.append(wt)
        fsf.append(fs)
    mtokenm = zip(vtypen,fsf)
    return mtokenm


def write_file(filename, lines) :
    f = file(filename, 'w')
    for word in lines:
        try:
            f.write(word.encode('utf-8') + '\n')
        except UnicodeEncodeError, e:
            print 'Encoding error ' + word + '\n'
    f.close()

matrix = readcsv()
tokens = tokenize(matrix)
print tokens
#stemmed = get_stemmed_list(tokens)
stop = stopword(tokens)
print stop

#write_file('C:\\Python27\\NLPK\\AEN02_stemmed.csv', stemmed)
write_file('C:\\Python27\\NLPK\\AEN02_tokens.csv', tokens)
write_file('C:\\Python27\\NLPK\\AEN02_stop.csv', stop)
