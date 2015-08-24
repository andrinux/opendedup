#Mound Command Line options for mount.sdfs

# Command Line Options #
```
usage: mount.sdfs -f -o <fuse options> -m<mount point> -r <path to chunk store routing file> -[v|vc]<volume name to mount | path to volume config file>
 -h    display available options
 -m    mount point for SDFS file system
             e.g. /media/dedup
 -o    fuse mount options.
             Will default to:
             direct_io,big_writes,allow_other,fsname=SDFS
 -r    path to chunkstore routing file.
             Will default to:
             /etc/sdfs/routing-config.xml
 -v    sdfs volume to mount
             e.g. dedup
 -vc   sdfs volume configuration file to mount
             e.g. /etc/sdfs/dedup-volume-cfg.xml
```