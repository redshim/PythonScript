import Queue
import threading
import time
import os
import sys
from datetime import datetime

print ">>> start: " + datetime.today().strftime("%Y-%m-%d %H:%M:%S")

folder = sys.argv[1]
result_filepath = sys.argv[2]
line_cut = sys.argv[3]

print "Current folder : %s" %folder

file_paths = []
checkList = []
tempList = []
index = 0
line_count = 0
dirList = []
temp = []
column_size = []

for path, dirs, files in os.walk(folder):
    if files:
        for filename in files:
            filepath = os.path.join(path, filename)
            file_paths.append(filepath)

for filename in file_paths:
    index += 1
    checkList.append({"filename": filename, "index": index})
    print(checkList)
print len(checkList)

with open(result_filepath, "w") as outfile:
    for item in checkList:
        dirList = item["filename"].split("/")

        with open(item["filename"]) as infile:
            for line in infile:
                line_count += 1
                if line_count >= int(line_cut):
                    tempList = line.strip('\r\n').split(",")
		    
                    if len(tempList) in colum_size:
                        column_size.append(len(tempList))

		    temp = tempList + dirList[5:10]
                    outfile.write(",".join(temp[1:]) + "\r\n")

print ">>> end: " + datetime.today().strftime("%Y-%m-%d %H:%M:%S")
