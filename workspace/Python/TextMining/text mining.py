#!/usr/bin/env python
# -*- coding: utf-8 -*-

from konlpy.corpus import kobill    # Docs from pokr.kr/bill
files_ko = kobill.fileids()         # Get file ids
doc_ko = kobill.open('test.txt').read() 
# news.txt�� http://boilerpipe-web.appspot.com/ �� ���� ��Ż���� �κп��� �ܾ�Դ�.
# news.txt ��  konlpy�� corpus�Ʒ��� �ִ� kobill directory�� �̸� ����Ǿ��־�� �Ѵ�. 
# /Library/Python/2.7/site-packages/konlpy/data/corpus/kobill

2.Tokenize (�ǹ̴ܾ� ����)
from konlpy.tag import Twitter; t = Twitter()
tokens_ko = t.morphs(doc_ko)

3. Token Wapper Ŭ���� �����(token������ �̷� ���� ó���� �ϱ� ����)
import nltk
ko = nltk.Text(tokens_ko, name='����')

4. ��� ������ ���� ��ū ���� �˾Ƴ���
print(len(ko.tokens))       # returns number of tokens (document length)
print(len(set(ko.tokens)))  # returns number of unique tokens
ko.vocab()                  # returns frequency distribution



#!/usr/bin/env python
# -*- coding: utf-8 -*-
import sys
reload(sys)

sys.setdefaultencoding('utf-8')

#load
from konlpy.corpus import kobill
docs_ko = [kobill.open(i).read() for i in kobill.fileids()]

#Tokenize
from konlpy.tag import Twitter; t = Twitter()
pos = lambda d: ['/'.join(p) for p in t.pos(d)]
texts_ko = [pos(doc) for doc in docs_ko]


#train
from gensim.models import word2vec
wv_model_ko = word2vec.Word2Vec(texts_ko)
wv_model_ko.init_sims(replace=True)
 
wv_model_ko.save('ko_word2vec_e.model')

#test
print(wv_model_ko.most_similar(pos('�޸���')))



#!/usr/bin/env python
# -*- coding: utf-8 -*-
import sys
reload(sys)

sys.setdefaultencoding('utf-8')

#load



from konlpy.corpus import kobill
docs_ko = [kobill.open(i).read() for i in kobill.fileids()]




#Tokenize
from konlpy.tag import Twitter; t = Twitter()
pos = lambda d: ['/'.join(p) for p in t.pos(d)]
texts_ko = [pos(doc) for doc in docs_ko]


#train
from gensim.models import word2vec
wv_model_ko = word2vec.Word2Vec(texts_ko)
wv_model_ko.init_sims(replace=True)

wv_model_ko.save('ko_word2vec_e.model')

#test
print(wv_model_ko.most_similar(pos('�޸���')))
