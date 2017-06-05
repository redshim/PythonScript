import pandas as pd
text = nltk.tokenize.word_tokenize("And now for something completely different")
pt = nltk.pos_tag(text)
pdt = pd.DataFrame(pt)

nltk.help.upenn_tagset('NN.*')
'''
Created on 2016. 1. 22.

@author: 6546788
'''
