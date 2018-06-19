# Deconstructing REST Security

This is the git repo to hold any code that supports the Deconstructing REST Security talk.  At the moment, there's only a `hashes` command.

# Hashes command

To build:

    mvn clean install $skip

Then to run the hashes it's:

    ./target/hashes all <file>

For example, to hash the pom.xml file:

    $ ./target/hashes all pom.xml
    XxHash32    a3fef50a
    
    XxHash64    042c35bb910f49da
    
    MD5         f3b2dcb10ac319f2155c48fa38c468f5
    
    SHA-1       5fc4cc45f1b5f1bcb5249c9e33123788766e263d
    
    SHA-256     ec98ea28795ed1878b3bfd49dc35f2e978ce40e5995bd9f22e123c83cf9ba1ef
    
    SHA-512     d659133875910cb0265e3b7d91037efad2b7234bdaceeb7de2b416867242c7ea
                d558507b8a7066f94fc89a9c41013853d289c61f54bb6dbc028ee92a83f6552b
    
The `hashes` binary is self-contained and can be moved into a bin directory in your `PATH`.  I put mine in `~/bin/`, which is where I tend to put small tools and personals scripts.

## Use of `hashes` in the talk

In the part of the talk that helps people understand hashes, a football/soccer game analogy is used.  We tend to use teams local to wherever the talk is given, which means creating a file such as this:

    mingus:/tmp 08:50:50 
    $ mkdir /tmp/worldcup
    
    mingus:/tmp 08:50:59 
    $ cd /tmp/worldcup/
    
    mingus:/tmp/worldcup 08:51:02 
    $ echo "England beats Tunisia 3 to 1" > final-scopre.txt
    
    mingus:/tmp/worldcup 08:52:54 
    $ hashes all final-scopre.txt 
    XxHash32    8a6b9b1a
    
    XxHash64    3e27720f8b1e4acd
    
    MD5         19ac71a9368a191a74d8560e74391dac
    
    SHA-1       e64c2716dc30ed3a7d5e92025319a315de56c558
    
    SHA-256     a58f77d387070041661bea520f2db2f500a7a857dc01524eeb3e0580ef7cecbb
    
    SHA-512     a708aeef8c3e83f910e261e041c2e04cc4b9e82cc164d4eb4baff9993bda0896
                8f75d455f79591e04efbd3dfa3d4283c2342c17cd0a9d641573f48b2523f5fbe

The scenario is then painted that the score is changed, which would result in a different set of hashes.

    mingus:/tmp/worldcup 08:56:06 
    $ echo "England beats Tunisia 3 to 2" > final-scopre.txt
    
    mingus:/tmp/worldcup 08:57:33 
    $ hashes all final-scopre.txt 
    XxHash32    a358e381
    
    XxHash64    60661d15b5e45d50
    
    MD5         7b98c627f6d381e2a91050b946abd384
    
    SHA-1       d5e48067293ab26c8fec9386e1c87b1cfb755dc4
    
    SHA-256     4fbb67613fceebd19ba87f5ad986b08d2bff16a6a35d3dfb83ea75c4643745ad
    
    SHA-512     8f7478b7c253f5aaf0f495427e53de1cac3b1252d5b5206bb730750049ef947f
                5fc076f5928e6d3e15320c4f9b1b27c64fbfc41115dc9da29741f64456590dc6

Typically screenshots are taken of the terminal window and added to the talk.

