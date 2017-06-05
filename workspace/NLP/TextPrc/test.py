# coding=UTF-8
import csv
import nltk
from django.test.testcases import FSFilesHandler

f = open('C:\\Python27\\NLPK\\AEN02_test.csv', 'r')
csvReader = csv.reader(f)
matrix = []
for row in csvReader:
    matrix.append(row)
f.close()

tokens = []
vtype = []
docid = []
vdetail = []
i = 0
for s in matrix:
    tokens.append(nltk.pos_tag(nltk.tokenize.word_tokenize(s[5].lower())))
    vtype.append(s[3])
    docids = s[0] + '_' + str(i)
    docid.append(docids)
    vdetail.append(s[4])
    i = i +1
    print(len(docid))
    print(len(tokens))
    print(tokens)


mtoken = zip(vtype, token)    

from nltk.corpus import stopwords
stop_words = set(stopwords.words('english') + ['.', ',', '--', '\'s', '?', ')', '(', ':', '\'', '"', "'", "n't", 'copi',
    '-', '}', '{', u'—', 'rt', 'http', 't', 'co', '@', '#', '/', 
    u'#', u';',  u'amp', u't', u'co', u']', u'[', u'`', u'`', u'&', u'|', u"''", u'$', u'//', u'/',
    u'via',  u'...', u'!', u'``', u'http', u'copi', u'ext97' ])
                 
#    filtered_sentence = [w for w in tokens if not w in stop_words]
fs = []
fsf = []
vtypen=[]
docidn=[]
vdetailn=[]
for w in mtoken:
    fs = []
    vdetailn.append(w[2])
    vtypen.append(w[1])
    docidn.append(w[0])
    for wt in w[3]:
        wt[0] = wt[0].replace("'","")
        wt[0] = wt[0].replace("\\","")
        if wt[0] not in stop_words:
            if wt[0] == 'copied':  
                continue
            elif len(wt[0]) < 2:
                continue
            else:
                fs.append(wt[0])
       
    
    fsf.append(fs)

len(fsf)
len(vtype)
#fsent = zip(vtype,filtered_sentence)
    
mtoken = zip(vtypen,fsf)


temp = []
dictList = []

for key, value in dic.iteritems():
    aKey = key
    aValue = value
    temp.append(aKey)
    temp.append(aValue)
    dictList.append(temp) 
    aKey = ""
    aValue = ""  

print(dictList)



wordcount = {}
wc = []
for line in fsf:
    for word in line:
        if word not in wordcount:
            wordcount[word] = 1
        else:
            wordcount[word] += 1
    #for k,v in wordcount.items():
    #    print k, v    
    wc.append(wordcount.items())
    
    

list1 = []
list2 = []
for line in mtoken:
    v = line[0]
    for word in line[1]:
        list1 = (v,word)
        list2.append(list1)
        print list1

        
        
        