import os
import commands
import time
import sys
from datetime import datetime


print ">>> start: " + datetime.today().strftime("%Y-%m-%d %H:%M:%S")


target_filepath = sys.argv[1]
result_filepath = sys.argv[2]
line_cut = sys.argv[3]

#target_filepath = "/home/6546788/Smartphone/CustomerUsageKorea/BH/2737/2013-11/27/"
#result_filepath = "/home/6546788/Smartphone/CustomerUsageKorea/BH/2737/2013-11/27/"
#result_filename = "BH"
checkList = []
index = 0
arrlen = []
line_count = 0
column = []
column_index = 0
dirList = []
temp = []
arrdata = []
tempList = []

def get_filepaths(directory):
    file_paths = []  # List which will store all of the full filepaths.

    for path, dirs, files in os.walk(directory):
        if files:
            for filename in files:
                filepath = os.path.join(path, filename)
                file_paths.append(filepath)


    return file_paths

def read_line(path, start=1, length=1):  
    for line in (commands.getoutput('head -%s %s | tail -%s' % ((start + (length -1)), path.replace(" ", "\ "), length))).split("\n"):
        yield(line.split(","))
        #yield(line)

full_file_paths = get_filepaths(target_filepath)

for item in full_file_paths:
    for i in read_line(item, 3, 1):
        if not len(arrdata) > 0:
            column_index += 1
            arrdata.append({"len":len(i),"column":i,"index":column_index})
        else:
            for a in arrdata:
                tempList.append(a["column"])
            if not i in tempList:
                column_index += 1
                arrdata.append({"len":len(i),"column":i,"index":column_index})
        checkList.append({"len":len(i), "filename": item, "data": i})

print("write start!!")

with open(result_filepath + "column.txt", "w") as col:
    for li in arrdata:
        col.write("col_cnt : " + str(li["len"]) + " col : " + str(li["column"]) + " index : " + str(li["index"]) )

for i in arrdata:
    with open(result_filepath + str(i["len"]+4) + "_" + str(i["index"])  + ".txt", "w") as outfile:
        for item in checkList:
	    line_count = 0
            if item["data"] == i["column"]:
		with open(item["filename"]) as infile:
		    dirList = item["filename"].split("/")
                    for line in infile:
			line_count += 1
			if line_count >= int(line_cut):
                            temp = line.strip('\r\n').split(",") + dirList[5:10]
                            outfile.write(",".join(temp[1:]) + "\r\n")


print ">>> end: " + datetime.today().strftime("%Y-%m-%d %H:%M:%S")

