#!/usr/bin/env python
# -*- coding: utf-8 -*-
def tokenize(file_name):
    # Assumes that sentences are separated by a single '\n'.
    # Assumes that words are separated by a single ' '.
    # Tokenizes each sentence, removes duplicate tokens, sorts tokens.
    return [sorted(list(set(e.split()))) for e in
            open(file_name).read().strip().split('\n')]
    
from nltk.corpus import stopwords
stop_words = set(stopwords.words('english') + ['.', ',', '--', '\'s', '?', ')', '(', ':', '\'', '"', "'", "n't", 'copied',
        '-', '}', '{', u'—', 'rt', 'http', 't', 'co', '@', '#', '/', 
        u'#', u';',  u'amp', u't', u'co', u']', u'[', u'`', u'`', u'&', u'|', u"''", u'$', u'//', u'/',
        u'via',  u'...', u'!', u'``', u'http', u'copied', u'ext97',
        u'aen02',u'aen03',u'dexp03',u'dexp05',u'dexp06',u'dexp07',u'dexp08',u'dexp11',
        u'dexp17',u'dexp18',u'engtrn03',u'engtrn09',u'engtrn12',u'engtrn13',u'engtrn16',
        u'engtrn21',u'ext01',u'ext02',u'ext10',u'ext16',u'ext17',u'ext18',u'ext20',
        u'ext21',u'ext23',u'ext24',u'ext25',u'ext26',u'fcd01',u'fcd04',u'fcd06',
        u'fcd07',u'fcd08',u'fcd10',u'fcd11',u'fcd16',u'fcd17',u'fcd21',u'fcd29',
        u'hvac01',u'hvac04',u'int04',u'int05',u'int07',u'int10',u'int12',u'int14',
        u'int15',u'int18',u'int33',u'int34',u'int35',u'int36',u'seat06',u'seat09',u'seat11',u'seat12','aen97',u'int97' ])
stop_words = sorted(stop_words) 
print stop_words


sentences = tokenize('AEN02.csv')
print len(sentences)

                   
#    filtered_sentence = [w for w in tokens if not w in stop_words]
ws = []
wordset = []
for wt in sentences:
    #wt = ['black', 'door', 'fading', 'frame', 'metal', 'not', 'purchased']
    ws=[]
    for w in wt:
        w0 = w.replace("'","")
        w0 = w0.replace("\\","")
        w0 = w0.replace("(","")
        w0 = w0.replace(")","")
        w0 = w0.replace("-","")
        w0 = w0.replace(":","")

        if w0 not in stop_words:
            if w0 == 'copied':  
                continue
            elif len(w0) < 2:
                continue
            else:
                print w0
                ws.append(w0)
    wl = (ws)
    wordset.append(wl)
