#!/usr/bin/env python
# -*- coding: utf-8 -*-
import itertools
import sys
import time
 
def tokenize(file_name):
    # Assumes that sentences are separated by a single '\n'.
    # Assumes that words are separated by a single ' '.
    # Tokenizes each sentence, removes duplicate tokens, sorts tokens.
    return [sorted(list(set(e.split()))) for e in
            open(file_name).read().strip().split('\n')]
    
def stopword(mtoken) :
    from nltk.corpus import stopwords
    stop_words = set(stopwords.words('english') + [u'.', u',', u'--', u'\'s', u'?', u')', u'(', u':', u'\'', u'"', u"'", u"n't", u'copied',
        u'-', u'}', u'{', u'—', u'rt', u'http', u't', u'co', u'@', u'#', u'/', 
        u'#', u';',  u'amp', u't', u'co', u']', u'[', u'`', u'`', u'&', u'|', u"''", u'$', u'//', u'/',
        u'via',  u'...', u'!', u'``', u'http', u'copied', u'ext97',
        u'aen02',u'aen03',u'dexp03',u'dexp05',u'dexp06',u'dexp07',u'dexp08',u'dexp11',
        u'dexp17',u'dexp18',u'engtrn03',u'engtrn09',u'engtrn12',u'engtrn13',u'engtrn16',
        u'engtrn21',u'ext01',u'ext02',u'ext10',u'ext16',u'ext17',u'ext18',u'ext20',
        u'ext21',u'ext23',u'ext24',u'ext25',u'ext26',u'fcd01',u'fcd04',u'fcd06',
        u'fcd07',u'fcd08',u'fcd10',u'fcd11',u'fcd16',u'fcd17',u'fcd21',u'fcd29',
        u'hvac01',u'hvac04',u'int04',u'int05',u'int07',u'int10',u'int12',u'int14',
        u'int15',u'int18',u'int33',u'int34',u'int35',u'int36',u'seat06',u'seat09',u'seat11',u'seat12',u'aen97',u'int97',u'would',u'an',u'it'])
    stop_words = sorted(stop_words) 
    
    print(mtoken)
    print(stop_words)
                    
#    filtered_sentence = [w for w in tokens if not w in stop_words]
    ws = []
    wordset = []
    for wt in mtoken:
        print(wt)
        #wt = ['black', 'door', 'fading', 'frame', 'metal', 'not', 'purchased']
        ws=[]
        for w in wt:
            w0 = w.replace("'","")
            w0 = w0.replace("\\","")
            w0 = w0.replace("(","")
            w0 = w0.replace(")","")
            w0 = w0.replace("-","")
            w0 = w0.replace(":","")
            w0 = w0.replace("!","")
            if w0 not in stop_words:
                if w0 == 'copied':  
                    continue
                elif len(w0) < 2:
                    continue
                else:
                    ws.append(w0)
        wl = (ws)
        wordset.append(wl)
    return wordset
 
def frequent_itemsets(sentences):
    # Counts sets with Apriori algorithm.
    SUPP_THRESHOLD = 4
    supps = []
 
    supp = {}
    for sentence in sentences:
        for key in sentence:
            if key in supp:
                supp[key] += 1
            else:
                supp[key] = 1
    print "|C1| = " + str(len(supp))
    
    supps.append({k:v for k,v in supp.iteritems() if v >= SUPP_THRESHOLD})
    print supps
    print "|L1| = " + str(len(supps[0]))
 
    supp = {}
    for sentence in sentences:
        for combination in itertools.combinations(sentence, 2):
            if combination[0] in supps[0] and combination[1] in supps[0]:
                key = ','.join(combination)
                if key in supp:
                    supp[key] += 1
                else:
                    supp[key] = 1
    print "|C2| = " + str(len(supp))


    supps.append({k:v for k,v in supp.iteritems() if v >= SUPP_THRESHOLD})
    print supps
    print "|L2| = " + str(len(supps[1]))
 
    supp = {}
    for sentence in sentences:
        for combination in itertools.combinations(sentence, 3):
            if (combination[0]+','+combination[1] in supps[1] and
                    combination[0]+','+combination[2] in supps[1] and
                    combination[1]+','+combination[2] in supps[1]):
                key = ','.join(combination)
                if key in supp:
                    supp[key] += 1
                else:
                    supp[key] = 1
    print "|C3| = " + str(len(supp))
    supps.append({k:v for k,v in supp.iteritems() if v >= SUPP_THRESHOLD})
    print "|L3| = " + str(len(supps[2]))
    return supps
 
def measures(supp_ab, supp_a, supp_b, transaction_count):
    # Assumes A -> B, where A and B are sets.
    conf = float(supp_ab) / float(supp_a)
    s = float(supp_b) / float(transaction_count)
    lift = conf / s
    if conf == 1.0:
        conv = float('inf')
    else:
        conv = (1-s) / (1-conf)
    return [conf, lift, conv]
 
def generate_rules(measure, supps, transaction_count):

    rules = []
    CONF_THRESHOLD = 0.5
    LIFT_THRESHOLD = 10
    CONV_THRESHOLD = 5
    if measure == 'conf':
        for i in range(2, len(supps)+1):
            for k,v in supps[i-1].iteritems():
                k = k.split(',')
                for j in range(1, len(k)):
                    for a in itertools.combinations(k, j):
                        b = tuple([w for w in k if w not in a])
                        print b
                        [conf, lift, conv] = measures(v,
                                supps[len(a)-1][','.join(a)],
                                supps[len(b)-1][','.join(b)],
                                transaction_count)
                        if conf >= CONF_THRESHOLD:
                            rules.append((a, b, conf, lift, conv))
            rules = sorted(rules, key=lambda x: (x[0], x[1]))
            rules = sorted(rules, key=lambda x: (x[2]), reverse=True)
    elif measure == 'lift':
        for i in range(2, len(supps)+1):
            for k,v in supps[i-1].iteritems():
                k = k.split(',')
                for j in range(1, len(k)):
                    for a in itertools.combinations(k, j):
                        b = tuple([w for w in k if w not in a])
                        [conf, lift, conv] = measures(v,
                                supps[len(a)-1][','.join(a)],
                                supps[len(b)-1][','.join(b)],
                                transaction_count)
                        if lift >= LIFT_THRESHOLD:
                            rules.append((a, b, conf, lift, conv))
            rules = sorted(rules, key=lambda x: (x[0], x[1]))
            rules = sorted(rules, key=lambda x: (x[3]), reverse=True)
    elif measure == 'conv':
        for i in range(2, len(supps)+1):
            for k,v in supps[i-1].iteritems():
                k = k.split(',')
                for j in range(1, len(k)):
                    for a in itertools.combinations(k, j):
                        b = tuple([w for w in k if w not in a])
                        [conf, lift, conv] = measures(v,
                                supps[len(a)-1][','.join(a)],
                                supps[len(b)-1][','.join(b)],
                                transaction_count)
                        if conv >= CONV_THRESHOLD:
                            rules.append((a, b, conf, lift, conv))
            rules = sorted(rules, key=lambda x: (x[0], x[1]))
            rules = sorted(rules, key=lambda x: (x[4]), reverse=True)
    else:
        for i in range(2, len(supps)+1):
            for k,v in supps[i-1].iteritems():
                k = k.split(',')
                for j in range(1, len(k)):
                    for a in itertools.combinations(k, j):
                        b = tuple([w for w in k if w not in a])
                        [conf, lift, conv] = measures(v,
                                supps[len(a)-1][','.join(a)],
                                supps[len(b)-1][','.join(b)],
                                transaction_count)
                        if (conf >= CONF_THRESHOLD and
                                lift >= LIFT_THRESHOLD and
                                conv >= CONV_THRESHOLD):
                            rules.append((a, b, conf, lift, conv))
            rules = sorted(rules, key=lambda x: (x[0], x[1]))
            rules = sorted(rules, key=lambda x: (x[2],x[3],x[4]), reverse=True)
    return rules
 
def main():
    if len(sys.argv) < 2:
        sys.stderr.write("Usage: python a_priori.py"
                         "transactions.txt [measure]\n")
        sys.exit(1)
    elif len(sys.argv) == 2 or sys.argv[2] not in ['conf', 'lift', 'conv']:
        measure = 'all'
    else:
        measure = sys.argv[2]
    
#     fpath = 'C:\\Python27\\NLPK\\Association\\'+ sys.argv[1]
    fpath = 'C:\\Users\\6546788\Documents\\'+ sys.argv[1]
    
    
    sentences = tokenize(fpath)
    wordset = stopword(sentences)
    
    start_time = time.time()
    supps = frequent_itemsets(wordset)
    
   
    end_time = time.time()
    
    print "Time spent finding frequent itemsets = {:.2f} seconds.".format(
          end_time - start_time)
 
    start_time = time.time()
    rules = generate_rules(measure, supps, len(sentences))
    for rule in rules:
        print ("{{{}}} -> {{{}}}, "
               "conf = {:.2f}, lift = {:.2f}, conv = {:.2f}").format(
              ', '.join(rule[0]), ', '.join(rule[1]), rule[2], rule[3], rule[4])
    import pandas as pd
    df = pd.DataFrame(rules)
    
    d = sys.argv[1]
    #filepath_exp = 'C:\\temp\\%s_asso.csv' % d
    filepath_exp = 'C:\\Users\\6546788\Documents\\%s.out' % d
    
    df.to_csv(filepath_exp, sep=',', encoding='utf-8')
                   
    end_time = time.time()
    print "Time spent finding association rules = {:.2f} second.".format(
          end_time - start_time)
 
if __name__ == "__main__":
    main()