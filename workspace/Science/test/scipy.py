# coding=UTF-8 
from scipy import special

from numpy import *
from pylab import *
from matplotlib import *
from Tkinter import *

x = arange(0,10,0.01)

for k in arange(0.5,5.5):
    y = special.jv(k,x)
    plot(x,y)
    f = lambda x: -special.jv(k,x)
    x_max = optimize.fminbound(f,0,6)
    plot([x_max], [special.jv(k,x_max)],'ro')

title('different bessel functions and their local maxima')
show()