import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

/**
 * This is the Buffer Pool class.
 * 
 * @author Laith Al-Masri
 * @version Oct 18th
 */
public class BufferPool implements BufferPoolADT {
    private SinglyLinkedList<Buffer> bufferList;
    private RandomAccessFile raf;

    private int numOfBuffers;
    private int cacheHits;
    private int diskReads;
    private int diskWrites;

    private final int bufferSize = 4096; // 4096 bytes --> 1024 records.
    private final int numOfRecs = 1024;

    private byte[] current;

    /**
     * This is the constructor for the class.
     * 
     * @param fileName
     *            Represents the file to be sorted.
     * @param buffNumber
     *            Represents the maximum number of buffers that will be used.
     * @throws IOException
     */
    public BufferPool(String fileName, int buffNumber) throws IOException {
        bufferList = new SinglyLinkedList<Buffer>();
        raf = new RandomAccessFile(fileName, "rw");
        numOfBuffers = buffNumber;
        current = new byte[bufferSize];
    }


    /**
     * This is a getter method for the length of the file.
     * 
     * @return The length of the file.
     * 
     * @throws IOException
     */
    public int getFileLength() throws IOException {
        return (int)raf.length();
    }


    /**
     * This is a getter method for the number of cache hits.
     * 
     * @return The number of cache hits.
     */
    public int getCacheHits() {
        return cacheHits;
    }


    /**
     * This is a getter method for the number of disk reads.
     * 
     * @return The number of disk reads.
     */
    public int getDiskReads() {
        return diskReads;
    }


    /**
     * This is a getter method for the number of disk writes.
     * 
     * @return The number of disk writes.
     */
    public int getDiskWrites() {
        return diskWrites;
    }


    /**
     * This method is used to provide the bytes from the buffer pool
     * or the disk itself.
     * 
     * @param space
     *            Represents the array that will store the bytes.
     * @param position
     *            Represents the index of the needed byte.
     * @param recSize
     *            Represents the size of the record.
     * @throws IOExeption
     * @return The byte array.
     */
    @Override
    public byte[] getBytes(byte[] space, int position, int recSize)
        throws IOException {
        int blockID = position / numOfRecs;
        int offset = (position % numOfRecs) * recSize;
        for (int x = 0; x < bufferList.size(); x++) {
            if ((bufferList.get(x).bufferID == blockID) && (bufferList.get(
                x).byteArray != null)) {
                cacheHits++;
                current = bufferList.get(x).byteArray;
                System.arraycopy(current, offset, space, 0, 4);
                if (x != 0) {
                    Buffer topBuffer = bufferList.get(x);
                    bufferList.remove(x);
                    bufferList.add(0, topBuffer);
                }
                return space;
            }
        }
        if (bufferList.size() == numOfBuffers) {
            Buffer evictedBuffer = bufferList.get(bufferList.size() - 1);
            if (evictedBuffer.dirty) {
                writeHelp(evictedBuffer);
            }
            bufferList.remove(bufferList.size() - 1);
        }
        Buffer newBuffer = new Buffer();
        byte[] currentBlock = new byte[bufferSize];
        readHelp(currentBlock, blockID * bufferSize);
        newBuffer.bufferID = blockID;
        newBuffer.dirty = false;
        newBuffer.byteArray = currentBlock;
        System.arraycopy(currentBlock, offset, space, 0, 4);
        bufferList.add(0, newBuffer);
        return space;
    }


    /**
     * This is a private helper method used to write stuff into a buffer.
     * 
     * @param byteArray
     *            Represents the information we want to write.
     * @param index
     *            Represents the position where we want to wrtie the information
     *            to.
     * @param recSize
     *            Represents the size of the record.
     * @throws IOException
     */
    public void write(byte[] byteArray, int index, int recSize)
        throws IOException {
        int blockID = index / numOfRecs;
        int offset = (index % numOfRecs) * recSize;
        for (int x = 0; x < bufferList.size(); x++) {
            if (bufferList.get(x).bufferID == blockID) {
                System.arraycopy(byteArray, 0, bufferList.get(x).byteArray,
                    offset, 4);
                bufferList.get(x).dirty = true;
                if (x != 0) {
                    Buffer topBuffer = bufferList.get(x);
                    bufferList.remove(x);
                    bufferList.add(0, topBuffer);
                }
                return;
            }
        }
        if (bufferList.size() == numOfBuffers) {
            Buffer evictedBuffer = bufferList.get(bufferList.size() - 1);
            if (evictedBuffer.dirty) {
                writeHelp(evictedBuffer);
            }
            bufferList.remove(bufferList.size() - 1);
        }
        Buffer newBuffer = new Buffer();
        byte[] currentBlock = new byte[bufferSize];
        readHelp(currentBlock, blockID * bufferSize);
        newBuffer.bufferID = blockID;
        newBuffer.dirty = true;
        newBuffer.byteArray = currentBlock;
        System.arraycopy(byteArray, 0, newBuffer.byteArray, offset, 4);
        bufferList.add(0, newBuffer);
    }


    /**
     * This method is used to make sure that all dirty buffers are written back
     * to the file.
     * 
     * @throws IOException
     */
    public void flush() throws IOException {
        for (int x = 0; x < bufferList.size(); x++) {
            if (bufferList.get(x).dirty) {
                writeHelp(bufferList.get(x));
            }
        }
    }


    /**
     * This is a private helper function that is used for reading from the
     * disk.
     * 
     * @param space
     *            Represents the array that will store the read part.
     * @param position
     *            Represents the index of the desired byte.
     * @throws IOException
     */
    private void readHelp(byte[] space, int position) throws IOException {
        raf.seek(position);
        raf.read(space);
        diskReads++;
    }


    /**
     * This is a private helper function that is used to writing on the disk.
     * 
     * @param evicted
     *            Represents the buffer that contains the data.
     * @throws IOException
     */
    private void writeHelp(Buffer evicted) throws IOException {
        raf.seek(evicted.bufferID * bufferSize);
        raf.write(evicted.byteArray);
        diskWrites++;
    }


    /**
     * This method is used to close the
     * Random Access File when the program
     * terminates.
     * 
     * @throws IOException
     */
    public void closeRAF() throws IOException {
        raf.close();
    }

    /**
     * This is a private Buffer class
     * 
     * @author Laith Al-Masri
     * @version Oct 16th
     */
    private class Buffer {
        private int bufferID;
        private boolean dirty;
        private byte[] byteArray;
    }

}
