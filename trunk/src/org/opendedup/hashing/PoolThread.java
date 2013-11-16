package org.opendedup.hashing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;

import java.util.concurrent.locks.ReentrantLock;

import org.opendedup.collections.QuickList;
import org.opendedup.logging.SDFSLogger;
import org.opendedup.sdfs.Main;
import org.opendedup.sdfs.io.BufferClosedException;
import org.opendedup.sdfs.io.SparseDataChunk;
import org.opendedup.sdfs.io.SparseDedupFile;
import org.opendedup.sdfs.io.WritableCacheBuffer;
import org.opendedup.sdfs.servers.HCServiceProxy;

public class PoolThread extends Thread {

	private BlockingQueue<WritableCacheBuffer> taskQueue = null;
	private boolean isStopped = false;
	private final QuickList<WritableCacheBuffer> tasks = new QuickList<WritableCacheBuffer>(
			60);

	public PoolThread(BlockingQueue<WritableCacheBuffer> queue) {
		taskQueue = queue;
	}

	@Override
	public void run() {
		while (!isStopped()) {
			try {
				tasks.clear();
				int ts = taskQueue.drainTo(tasks, 40);
				if (Main.chunkStoreLocal) {
					for (int i = 0; i < ts; i++) {
						WritableCacheBuffer runnable = tasks.get(i);
						try {
							runnable.close();
						} catch (Exception e) {
							SDFSLogger.getLog().fatal(
									"unable to execute thread", e);
						}
					}
				} else {
					if (ts > 0) {
						SparseDataChunk[] cka = new SparseDataChunk[ts];
						for (int i = 0; i < ts; i++) {
							WritableCacheBuffer runnable = tasks.get(i);
							runnable.startClose();
							AbstractHashEngine hc = SparseDedupFile.hashPool
									.borrowObject();
							byte[] hash = null;
							try {
								hash = hc.getHash(runnable.getFlushedBuffer());
								SparseDataChunk ck = new SparseDataChunk(false,
										hash, false, new byte[8]);
								cka[i] = ck;
							} catch (BufferClosedException e) {
								cka[i] = null;
							} finally {
								SparseDedupFile.hashPool.returnObject(hc);
							}

						}

						ArrayList<SparseDataChunk> cks = new ArrayList<SparseDataChunk>(
								Arrays.asList(cka));
						HCServiceProxy.batchHashExists(cks);
						for (int i = 0; i < ts; i++) {
							WritableCacheBuffer runnable = tasks.get(i);
							SparseDataChunk ck = cks.get(i);
							if (ck != null) {
								runnable.setHash(ck.getHash());
								runnable.setHashLoc(ck.getHashLoc());
							}
							try {
								runnable.endClose();
							} catch (Exception e) {
								SDFSLogger.getLog().warn(
										"unable to close block", e);
							}
						}
						cks = null;
					}
				}
				Thread.sleep(1);
			} catch (Exception e) {
				SDFSLogger.getLog().fatal("unable to execute thread", e);
			}
		}
	}

	private ReentrantLock exitLock = new ReentrantLock();

	public void exit() {
		exitLock.lock();
		try {
			isStopped = true;
			this.interrupt(); // break pool thread out of dequeue() call.
		} finally {
			exitLock.unlock();
		}
	}

	public boolean isStopped() {
		return isStopped;
	}
}
