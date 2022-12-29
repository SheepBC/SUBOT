import io
import sys
from sympy import factor
import sympy

sys.stdout = io.TextIOWrapper(sys.stdout.detach(), encoding="utf-8")
sys.stderr = io.TextIOWrapper(sys.stderr.detach(), encoding="utf-8")

type = sys.argv[1]
thing = sys.argv[2]

if type == "factor":
    print(factor(thing))
elif type == "expend":
    print(sympy.expand(thing))
elif type == "solve":
    print(sympy.solve(thing))
elif type == "coalition":
    sys.argv.remove(sys.argv[0])
    sys.argv.remove(sys.argv[0])
    print(sympy.solve(sys.argv))