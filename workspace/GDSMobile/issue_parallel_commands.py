import os
import sys
import commands
import time
import threading
from threading import Thread

# ARGS
# 0: directory where compressed data files reside # 1: file pattern to glob for # 2: target table to load into # 1: number of threads of parallel execution

# Defines each thread of execution
class parseit(Thread):
    def __init__ (self,filename,outputpath):
        Thread.__init__(self)
        self.status = -1
        self.canProceed = False
        self.filename = filename
        self.outputpath = outputpath
        
        import re
        self.file=re.search('/([^/]*).csv',self.filename).group(1)
    
    def run(self):
        parse_command='python /home/6546788/GDS_Mobile/script/parse.py %s > %s/%s.csv' % (self.filename,self.outputpath,self.file)
        
        global active_count,threshold,lock
        
        while 1:
            lock.acquire()
            try:
                if( active_count < threshold ):
                    #print 'threshold='+str(threshold)+',active_count='+str(active_count)
                    active_count = active_count + 1
                    self.canProceed=True
            finally:
                lock.release()
                if(self.canProceed):
                    break
                else:
                    time.sleep(5)
            try:
                self.status, out = commands.getstatusoutput(parse_command)
                if (self.status != 0):
                    print str(self.status)+' '+out+' '+parse_command
                else:
                    print str(self.status)+' '+parse_command
            finally:
                lock.acquire()
            try:
                active_count = active_count - 1
            finally:
                lock.release()

#main()
usage = "./parallel_commands.py <directory_where_files_reside> <desired_output_directory> <filepattern> <max number of threads>"

if(len(sys.argv)!=5):
    print usage
    sys.exit(2)

#/home/6546788/GDS_Mobile/src/LF csv /home/6546788/GDS_Mobile/output/LF 2

inputpath=sys.argv[1]
outputpath=sys.argv[2]
filepattern=sys.argv[3]
threshold=int(sys.argv[4])
delimiter=','
active_count=0
lock=threading.Lock()

print time.ctime()
processlist = []

status,filelist = commands.getstatusoutput('find '+inputpath+' -iname "*'+filepattern+'" -type f')

print status
print filelist

if (status != 0):
    print 'Exit code: '+str(status)
    sys.exit(2)

for file in filelist.split('\n'):
    current = parseit(file,outputpath)
    processlist.append(current)
    current.start()

for process in processlist:
    process.join()

print time.ctime()

#ENDOF main()