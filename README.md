# bsg

## Notes
### Subversion repository is (manually) mirrored from here.  Here are the commands I used:
```
$ git svn clone https://subversion.assembla.com/svn/bsg-ucsf/trunks/bsg
$ cd bsg
$ git remote add -f source https://github.com/BioSystemsGroup/bsg.git
$ git rebase --onto remotes/git-svn --root source/master
$ git log
$ git svn dcommit
```
