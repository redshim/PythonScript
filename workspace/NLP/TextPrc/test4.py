# coding=UTF-8
import csv
import pandas as pd

f = open('C:\\temp\\Steer\\24_comments.csv', 'r')
csvReader = csv.reader(f)
matrix = []
for row in csvReader:
    matrix.append(row)
f.close()

tokens = []

i = 0
for s in matrix:
    tokens.append(s[0].split())

print tokens

list2 =[]
for line in tokens:
    for word in line:
        tword = word.replace("<","")
        tword = tword.replace(">","")
        tword = tword.replace(":","")
        tword = tword.replace('"',"")
        tword = tword.replace('.',"")
        list2.append(tword)
                

filepath_exp = 'C:\\temp\\steer\\24_out.csv'

df = pd.DataFrame(list2, columns=['token'])
df.to_csv(filepath_exp, sep=',', encoding='utf-8')