import pandas as pd

import numpy as np
from pandas import DataFrame


plot_df = DataFrame(np.random.randn(100,2), columns=['x','y'])
plot_df.head(20)

plot_df.plot()