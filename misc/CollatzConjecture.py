'''Collatz conjecture is a famous unsolved mathematical conjecture. 
it says that no matter what INTEGER is chosen, taking these statements will always end up at 1.
if n is even, divide it by two
if n is odd, take 3n+1
apparently holds true to n*10^20'''
def Collatz(n):
  while n != 1:
    if n%2 == 0:
        n=n/2
    elif n%2 ==1:
        n = (n*3)+1
  return n
x = 1
stop = int(input("Stop Num: "))
while x != stop:
    try:
        y = Collatz(x)
        x += y
        print(x)
    except KeyboardInterrupt:
        print("Interrupted")
        print("final " + str(x))
    except Exception as e:
        print(f"An error occurred: {e}")
    finally:
        print(f"Collatz verified up to {x}")
        