All the nodes start from a local copy of an existing chain and are assigned a username from a list of names present in a file created using the
commands below.

## Creating a chain with args specified blow
To create a copy of a Vegvisir DAG using your own chain file, parameter file, and user names run:

python3 chain_creator.py 

- --peers <ip,port,...> : The IPs and ports of nodes, e.g. 127.0.0.1,7001,127.0.0.1,7002

- --usernames <usr1, usr2,...>: The usernames of nodes, e.g. Alpha,Beta,Charlie

- --paramfile < filename >: The name where the above parameters will be saved.

- --chainfile < filename >: The name where the blockchain starting point will be saved.

 ## Running Vegvisir 'remotely' or 'locally' with args specified below
 python3 main.py 

 - --run remote/local : Run the protocol with remote machines or as local processes
 - --username < username > : The username that the program assumes
 - --chainfile < filename > : The file containing the essential parts for recreating the blockchain ([Instructions](https://github.com/dadams39/Vegvisir/blob/send-all-protocol/overhaul/emulator/README.md##creating-a-chain))
 - --paramfile < filename > : The file containing addresses and names of peers
 - --deathprob : A float in range 0-1 for the probability that the node fails during reconciliation
 - --blocklimit : An integer specifying how many blocks the node can create at once (mostly for testing)
 
 If in doubt, run python3 main.py -h
 
 ## Dependencies
 These are in addition to the dependencies of the entire project
 
 [Protobuf 3.0.0](https://github.com/protocolbuffers/protobuf)
 
**_Note_**: Updates will be made to the readme as the protocol changes
