'''
Deterministic: The same input always produces the same hash.
Fixed Output Size: Regardless of input size, the output (hash) is of a fixed length.
Fast Computation: The hash function should compute efficiently.
Preimage Resistance: It should be computationally infeasible to reverse a hash to obtain the original input.
Collision Resistance: Two different inputs should not produce the same hash.
Avalanche Effect: A small change in input should result in a drastically different hash.
'''

unc = input("Unhashed Input: ")
