# coding=UTF-8
import csv
import pandas as pd

f = open('C:\\temp\\Steer\\1_3_comments.csv', 'r')
csvReader = csv.reader(f)
matrix = []
for row in csvReader:
    matrix.append(row)
f.close()

tokens = []
partno = []
seq = []

i = 0
for s in matrix:
    tokens.append(s[0].split())
    partno.append(s[1])
    seq.append(s[0])

mtoken = zip(seq, partno, tokens)

list1 = []
list2 = []
tokentype = []
for line in mtoken:
    vd = line[2]
    pn = line[1]
    s = line[0]
    for word in vd:
        tword = word.replace("<","")
        tword = tword.replace(">","")
        tword = tword.replace(":","")
        tword = tword.replace('"',"")
        tword = tword.replace('.',"")
        list1 = (s,pn,tword)
        list2.append(list1)
                
print(len(list2))
print list2      


filepath_exp = 'C:\\temp\\steer\\1_3_out.csv'

df = pd.DataFrame(list2, columns=['seq','partno','token'])
df.to_csv(filepath_exp, sep=',', encoding='utf-8')