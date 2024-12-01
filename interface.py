
import FunctionalCalcs

debug = False

def debugPrint(systemIn):
    if debug == True:
        print(systemIn)



print("Copyright 2024, GPL v3.0|FREE TO DISTRIBUTE, USE, AND SELL, NO PROPRIETARY SOFTWARE, Riklik1984")
print("for more information on copyright or licensing, please type 'info'")

while True:
    try:
        user_input = input("Please enter a command: ")
        debugPrint(user_input)
        
    except KeyboardInterrupt:
        print("Exiting...")
        break