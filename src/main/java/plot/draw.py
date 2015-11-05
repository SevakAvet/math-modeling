import numpy as np
import matplotlib.pyplot as plt
import sys, getopt

x = tuple(map(float, sys.argv[1].strip("()").split(", ")))
y = tuple(map(float, sys.argv[2].strip("()").split(", ")))

t1 = np.arange(0.0, 10.0, 0.01)
plt.plot(t1, np.sin(t1))
plt.plot(x, y)
plt.show()
