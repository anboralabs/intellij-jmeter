package co.anbora.labs.jmeter.ide.vfs

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileListener
import com.intellij.openapi.vfs.VirtualFileSystem
import com.intellij.openapi.vfs.impl.local.LocalFileSystemBase

class JMeterFileSystem: LocalFileSystemBase(), DumbAware {
    override fun replaceWatchedRoots(
        watchRequests: MutableCollection<WatchRequest>,
        recursiveRoots: MutableCollection<String>?,
        flatRoots: MutableCollection<String>?
    ): MutableSet<WatchRequest> = watchRequests.toMutableSet()
}
