#main calculator functions, may have EOA cmds if necessary (put most EOA cmds in interface directly then)
import math
def basicIntercept(mainInput):
    try:
        if mainInput == "*":
            print("x * y")
    except:
        return "False"
    
'''Collatz conjecture is a famous unsolved mathematical conjecture. 
it says that no matter what INTEGER is chosen, taking these statements will always end up at 1.
if n is even, divide it by two
if n is odd, take 3n+1
apparently holds true to n*10^20'''
def Collatz(n):
  while n != 1:
    print(n)
    if n%2 == 0:
        n=n/2
    elif n%2 ==1:
        n = (n*3)+1
  return n
while True:
    start = 1
    stop = 20
    try:
        if start != stop:
            start = Collatz(start) + start
            print(start)
    except KeyboardInterrupt:
            print("Interrupted")
            print("final " + str(start))
    except Exception as e:
        print(f"An error occurred: {e}")

  