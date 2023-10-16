# blockchain-cryptography
Cryptography Assignment-2

Deliverables:

1. Your solution must implement the following  methods: 
 -- createBlock() 
 -- verifyTransaction() 
 -- mineBlock() or something equivalent for proof of work 
 -- viewUser()
   
2. All transactions must be verified before they can be added to a block. As part of the  verification process, you are required to use zero knowledge proof to verify at least one attribute.
3. viewUser() should list all (successful) transactions against the user. 
4. For obtaining the hash value of a block, you are required to use SHA-256 encoding. You can use any library function for this part. You also have to implement a DES (you cannot use library functions here) to encode the 256 bit output of the SHA-256 before returning the hash. Since DES takes a 64 bit input, you will take the first 64 bits of the SHA-256 output and encode using DES, then encode the next 64 bits of the SHA-256 output and so on. You will combine them in the same order. (ex: SHA-256 output = abcd, then you will output DES(a) + DES(b) + DES(c) + DES(d), here ‘+’ represents concatenation). You can get the link to the keys of the DES used here.
