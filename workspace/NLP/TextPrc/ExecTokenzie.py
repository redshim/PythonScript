# coding=UTF-8
import csv
import nltk
import pandas as pd

import os.path
import sys
from collections import Counter
from cssselect.parser import TokenStream

print sys.argv[1]
qt = sys.argv[1]

### read csv
def readcsv():
    filepath = 'C:\\Python27\\NLPK\\%s.csv' % qt
    f = open(filepath, 'r')
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
###'INT10'
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
    docid = []
    vdetail = []
    i = 0
    #dic = {}
    for s in matrix:
        #tokens.append(nltk.pos_tag(nltk.tokenize.word_tokenize(s[5].lower())))
        tokens.append(nltk.tokenize.word_tokenize(s[5].lower()))
        #print tokens
        vtype.append(s[3])
        docids = s[0] + '_' + str(i)
        docid.append(docids)
        vdetail.append(s[4])
        i = i + 1
    #    dic[s[3]] = nltk.tokenize.word_tokenize(s[5].lower())
    print(len(docid))
    mtoken = zip(docid, vtype, vdetail, tokens) 
    return mtoken

def stopword(mtoken) :
    from nltk.corpus import stopwords
    stop_words = set(stopwords.words('english') + ['.', ',', '--', '\'s', '?', ')', '(', ':', '\'', '"', "'", "n't", 'copi',
        '-', '}', '{', u'—', 'rt', 'http', 't', 'co', '@', '#', '/', 
        u'#', u';',  u'amp', u't', u'co', u']', u'[', u'`', u'`', u'&', u'|', u"''", u'$', u'//', u'/',
        u'via',  u'...', u'!', u'``', u'http', u'copi', u'ext97' ,
        u'aen02',u'aen03',u'dexp03',u'dexp05',u'dexp06',u'dexp07',u'dexp08',u'dexp11',
        u'dexp17',u'dexp18',u'engtrn03',u'engtrn09',u'engtrn12',u'engtrn13',u'engtrn16',
        u'engtrn21',u'ext01',u'ext02',u'ext10',u'ext16',u'ext17',u'ext18',u'ext20',
        u'ext21',u'ext23',u'ext24',u'ext25',u'ext26',u'fcd01',u'fcd04',u'fcd06',
        u'fcd07',u'fcd08',u'fcd10',u'fcd11',u'fcd16',u'fcd17',u'fcd21',u'fcd29',
        u'hvac01',u'hvac04',u'int04',u'int05',u'int07',u'int10',u'int12',u'int14',
        u'int15',u'int18',u'int33',u'int34',u'int35',u'int36',u'seat06',u'seat09',u'seat11',u'seat12','aen97',u'int97' ])
                     
#    filtered_sentence = [w for w in tokens if not w in stop_words]
    fs = []
    fsf = []
    type = []
    typef = []
    vtypen=[]
    docidn=[]
    vdetailn=[]
    for w in mtoken:
        fs = []
        vdetailn.append(w[2])
        vtypen.append(w[1])
        docidn.append(w[0])
        for wt in w[3]:
            wt0 = wt[0].replace("'","")
            wt0 = wt0.replace("\\","")
            if wt0 not in stop_words:
                if wt0 == 'copied':  
                    continue
                elif len(wt0) < 2:
                    continue
                else:
                    fs.append(wt0)
                    type.append(wt[1])
        fsf.append(fs)
        typef.append(type)
    
#    wordcount = {}
#    wc = []
#    for line in fsf:
#        print(line)
#        for word in line:
#            if word not in wordcount:
#                wordcount[word] = 1
#            else:
#                wordcount[word] += 1
        #for k,v in wordcount.items():
        #    print k, v    
#        wc.append(wordcount.items())
            
    mtokenm = zip(docidn,vtypen,vdetailn,fsf,typef)
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


stop = stopword(tokens)


list1 = []
list2 = []

print stop 
for line in stop:
    vd = line[2]
    v = line[1]
    d = line[0]
    tokentype = line[3]
    print line[3]
    print line[2]
    print line[1]
    print line[0]
    print tokentype
    for word in tokentype:
        print word
        list1 = (d,v,vd,word)
        list2.append(list1)

    #tokentype = zip(line[3],line[4])
    #for word in tokentype:
    #    list1 = (d,v,vd,word[0],word[1])
    #    list2.append(list1)

filepath_exp = 'C:\\temp\\%s_stop1.csv' % qt

print "done"
print "list1"
print list1
print "list2"
print list2

df = pd.DataFrame(list2, columns=['docid','vtype','vdetail','token'])
df.to_csv(filepath_exp, sep=',', encoding='utf-8')

#write_file('C:\\Python27\\NLPK\\INT10_tokens.csv', tokens)
#write_file('C:\\Python27\\NLPK\\INT10_stop.csv', stop)
