# abby's stupid question
# is there any number lower than 720 divisible by all numbers 0-10?
def is_divisible_by_all(n):
    for i in range(1, 11):
        if n % i != 0:
            return False
    return True

for i in range(1, 720):
    if is_divisible_by_all(i):
        print(i)
        break
    
print("this was the most useless code i've ever written")