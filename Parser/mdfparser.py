import sys
import os.path
from mdfreader import mdfreader

print sys.argv[1]
qt = sys.argv[1]

print("start")

filepath = '/home/6546788/MDF/%s' %qt
print(filepath)
filepathToCsv = filepath+'.csv'
print(filepathToCsv)


file = mdfreader.mdf(filepath)
file.keys()
file.exportToCSV(filepathToCsv)
print("end")
