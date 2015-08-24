#A simple quick start guide

# Quick Start Guide #
Read this first to get started.
## Requirements ##

### System Requirements ###
  * x64 Linux Distribution. The application was tested and developed on ubuntu 9.1
  * Fuse 2.8+ . Debian Packages for this are available at http://code.google.com/p/dedupfilesystem-sdfs/downloads/list
  * 2 GB of RAM
  * Java 7 - available at https://jdk7.dev.java.net/

### Optional Packages ###
  * attr - (setfattr and getfattr) if you plan on doing snapshotting or setting extended file attributes

## Getting Started ##
**Step 1:** Set your JAVA\_HOME to the path of the java 1.7 jdk or jre folder, or edit the path in the shell scripts
> (mount.sdfs and mkfs.sdfs) to reflect the java path
> e.g. export JAVA\_HOME=/usr/lib/jvm/jdk

**Step 2:** Create an sdfs file system.
> To create and SDFS file System you must run the following command:
> > sudo ./mkfs.sdfs --volume-name=`<volume-name>` --volume-capacity=`<capacity>`
> > > e.g.

> > sudo ./mkfs.sdfs --volume-name=sdfs\_vol1 --volume-capacity=100GB

**Step 3:** Mount the sdfs

> To mount SDFS run the following command:
> > sudo ./mount.sdfs -v `<volume-name>` -m `<mount-point>`
> > > e.g.

> > sudo ./mount.sdfs -v sdfs\_vol1 -m /media/sdfs

## Known Limitation(s) ##
  1. Testing has been limited thus far. Please test and report bugs
  1. Deleting of data and reclaiming of space has not been implemented yet. ETA: This will be available in the coming days.
  1. Graceful exit if physical disk capacity is reached. ETA : Will be implemented shortly
  1. Maximum file size it currently 250GB