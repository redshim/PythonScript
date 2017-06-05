# coding=UTF-8
import csv
import pandas as pd
import sys


print sys.argv[1]
qt = sys.argv[1]

### read csv
def readcsv():
    filepath = 'C:\\Python27\\NLPK\\%s.csv' %qt
    print filepath
    f = open(filepath, 'r')
    csvReader = csv.reader(f)
    docid = []
    ####
    qtype = []
    question = []
    subquestion = []
    maker = []
    vehicle_type = []
    contents = []
    ####
    i = 0
    for row in csvReader:
        docids = row[0] + '_' + str(i)
        docid.append(docids)
        qtype.append(row[0])
        question.append(row[1])
        subquestion.append(row[2])
        maker.append(row[3])
        vehicle_type.append(row[4])
        contents.append(row[5])
        i = i +1
    mm = zip(docid,qtype,question,subquestion,maker,vehicle_type,contents)
    print i
    return mm
    f.close()

matrix = readcsv()
filepath_exp = 'C:\\Python27\\NLPK\\docid\\%s_n.csv' % qt

df = pd.DataFrame(matrix, columns=['docid','qtype','question','subquestion','maker','vehicle_type','contents'])
df.to_csv(filepath_exp, sep=',', encoding='utf-8',doublequote=True)