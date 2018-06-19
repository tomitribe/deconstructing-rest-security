# deconstructing-rest-security

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

