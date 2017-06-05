import pandas as pd
import numpy as np
from pandas import DataFrame



df = DataFrame(
    {'integer':[1,2,3,6,7,23,8,3],
     'float':[2,3.4,5,6,2,4.7,4,8],
     'string':['saya',None,'aku','cinta','kamu','a','b','jika']}
)
print df


import numpy as np
from numpy import pi
import matplotlib.pyplot as plt

x = np.arange(-pi, pi, pi/1000)
y1 = np.sin(x)
y2 = np.cos(x)
plt.title("Circular Functions")
plt.xlabel("x")
plt.ylabel("y")
plt.plot(x, y1)
plt.plot(x, y2)
plt.grid(True)
plt.show()