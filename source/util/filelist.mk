# ###################################################################
# filelist.mk - unique for each directory

include $(PRJPATH)/source/util/netscape/filelist.mk
include $(PRJPATH)/source/util/event/filelist.mk

filelist-lib += \
source/util/ArrayUtilities.java \
source/util/DecHexConverter.java \
source/util/ByteTransformer.java \
source/util/ClassUtilities.java \
source/util/FileUtilities.java \
source/util/FontUtilities.java \
source/util/InvisibleFrame.java \
source/util/MiniStack.java \
source/util/SpecialStack.java \
source/util/ExpandableStack.java \
source/util/Optimizer.java \
source/util/Preferences.java \
source/util/StringUtilities.java \
source/util/TempFileContentsManager.java \
source/util/TemporaryFileOnDisk.java \
source/util/WindowCloser.java \
source/util/WindowTracker.java \
source/util/WindowUtilities.java \
source/util/MenuUtilities.java
