# ###################################################################
# filelist.mk - unique for each directory

include $(PRJPATH)/source/bm/engine/filelist.mk
include $(PRJPATH)/source/bm/storage/filelist.mk
include $(PRJPATH)/source/bm/decompiler/filelist.mk
include $(PRJPATH)/source/bm/frames/filelist.mk
include $(PRJPATH)/source/bm/menu/filelist.mk

filelist-bm += \
source/bm/BMEnvironment.java \
source/bm/BMMain.java

manifest-bm := source/bm/manifest.mf
