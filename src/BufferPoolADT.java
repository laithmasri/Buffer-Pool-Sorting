import java.io.IOException;

/**
 * This is the Buffer Pool Interface.
 * 
 * @author Laith Al-Masri
 * @version Oct 18th
 * 
 */
public interface BufferPoolADT {

    /**
     * This is the write method.
     * 
     * @param space
     *            Represents the byte array
     * @param position
     *            Represents the index
     * @param recSize
     *            Represents the size of the record.
     * @throws IOException
     */
    public void write(byte[] space, int position, int recSize)
        throws IOException;


    /**
     * The is the getByte method.
     * 
     * @param space
     *            Represents the byte array.
     * @param position
     *            Represents the position where the bytes will be copied from.
     * @param recSize
     *            Represents the size of the record.
     * @return The byte array that contains the info.
     * @throws IOException
     */
    public byte[] getBytes(byte[] space, int position, int recSize)
        throws IOException;
}
