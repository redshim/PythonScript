# coding=UTF-8
import csv
import nltk
from django.test.testcases import FSFilesHandler

f = open('C:\\temp\\86150_utf8.csv', 'r')
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

mtoken = zip(docid, vtype, vdetail, tokens) 


from nltk.corpus import stopwords
stop_words = set(stopwords.words('english') + ['.', ',', '--', '\'s', '?', ')', '(', ':', '\'', '"', "'", "n't", 'copi',
    '-', '}', '{', u'—', 'rt', 'http', 't', 'co', '@', '#', '/', 
    u'#', u';',  u'amp', u't', u'co', u']', u'[', u'`', u'`', u'&', u'|', u"''", u'$', u'//', u'/',
    u'via',  u'...', u'!', u'``', u'http', u'copi', u'ext97' ])
                 
#    filtered_sentence = [w for w in tokens if not w in stop_words]
fs = []
fsf = []
type =[]
typef =[]
vtypen=[]
docidn=[]
vdetailn=[]

for w in mtoken:
    fs = []
    vdetailn.append(w[2])
    vtypen.append(w[1])
    docidn.append(w[0])
    for wt in w[3]:
        print wt[0]
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
    
    print typef
    print len(typef)
    print len(fsf)
    
mtokenm = zip(docidn,vtypen,vdetailn,fsf,typef)

list1 = []
list2 = []
tokentype = []
for line in mtokenm:
    vd = line[2]
    v = line[1]
    d = line[0]
    tokentype = zip(line[3],line[4])
    for word in tokentype:
        print word[0]
        print word[1]
        list1 = (d,v,vd,word[0],word[1])
        print list1
        list2.append(list1)