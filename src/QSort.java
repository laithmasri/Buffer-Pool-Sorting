import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * This is the quick sort class.
 * 
 * @author Laith Al-Masri
 * @version Oct 18th
 */
public class QSort {
    private long startWatch;
    private long stopWatch;
    private long totalTime;
    private String inputFile;
    private String statsFile;

    private BufferPool bufferPool;
    private final int recSize = 4;
    private int numOfRecords;

    private byte[] pivotByte;

    private byte[] currLeft;
    private byte[] currRight;

    private byte[] leftMost;
    private byte[] rightMost;

    private final int threshold = 50;

    /**
     * This is the constructor for the class.
     * 
     * @param buffPool
     *            Represents the buffer pool that we're working with.
     * @param input
     *            Represents the name of the file to be sorted.
     * @param stats
     *            Represents the file that will have the statistics.
     * @throws IOException
     */
    public QSort(BufferPool buffPool, String input, String stats)
        throws IOException {
        statsFile = stats;
        inputFile = input;
        bufferPool = buffPool;
        numOfRecords = bufferPool.getFileLength() / recSize;
        pivotByte = new byte[recSize];

        currLeft = new byte[recSize];
        currRight = new byte[recSize];

        leftMost = new byte[recSize];
        rightMost = new byte[recSize];
        startWatch = System.currentTimeMillis();
        quickSort(0, numOfRecords - 1);
        bufferPool.flush();
        buffPool.closeRAF();
        stopWatch = System.currentTimeMillis();
        totalTime = stopWatch - startWatch;
        //
        System.out.println(inputFile);
        System.out.println(totalTime);
        System.out.println("----");
        //
        FileWriter statistics = new FileWriter(statsFile);
        statistics.write("Sorting the file named " + inputFile + "\n");
        statistics.write("The number of cache hits: " + buffPool.getCacheHits()
            + "\n");
        statistics.write("The number of disk reads: " + buffPool.getDiskReads()
            + "\n");
        statistics.write("The number of disk writes: " + buffPool
            .getDiskWrites() + "\n");
        statistics.write("Execution Time: " + totalTime + " ms\n");
        statistics.write("\n");
        statistics.close();
    }


    /**
     * This is the quick sort method.
     * 
     * @param left
     *            Represents the starting position of sorting.
     * @param right
     *            Represents the ending position of sorting.
     * @throws IOException
     */
    public void quickSort(int left, int right) throws IOException {

        int pivotIndex = findPivot(left, right);
        bufferPool.getBytes(pivotByte, pivotIndex, recSize);
        ByteBuffer wrappedPivot = ByteBuffer.wrap(pivotByte);
        short pivotShort = wrappedPivot.getShort();
        normalSwap(pivotIndex, right, pivotByte);
        int partition = partition(left, right - 1, pivotShort);
        if (partition == left) {
            if (checkDuplicate(left, right)) {
                return;
            }
        }
        if ((partition - left) > 1) {
            quickSort(left, partition - 1);
        }
        if ((right - partition) > 1) {
            quickSort(partition + 1, right);
        }
    }


    /**
     * This method checks if the subarray is full of duplicates.
     * 
     * @param left
     *            Represents the left index.
     * @param right
     *            Represents the right index.
     * @return True if all duplicates and false otherwise.
     * @throws IOException
     */
    public boolean checkDuplicate(int left, int right) throws IOException {
        boolean duplicate = true;
        int moreLeft = left + 1;
        bufferPool.getBytes(leftMost, left, recSize);
        short leftMostShort = ByteBuffer.wrap(leftMost).getShort();
        bufferPool.getBytes(rightMost, moreLeft, recSize);
        short rightMostShort = ByteBuffer.wrap(rightMost).getShort();

        while (left < right) {
            if (leftMostShort != rightMostShort) {
                duplicate = false;
                return duplicate;
            }
            left++;
            moreLeft++;
            rightMostShort = leftMostShort;
            bufferPool.getBytes(leftMost, left, recSize);
            leftMostShort = ByteBuffer.wrap(leftMost).getShort();
        }
        return duplicate;
    }


    /**
     * This method is used to find the pivot.
     * 
     * @param left
     *            Represents the starting position.
     * @param right
     *            Represents the ending position.
     * @return The index of the pivot.
     */
    public int findPivot(int left, int right) {
        return ((left + right) / 2);
    }


    /**
     * This is the partition method.
     * 
     * @param left
     *            Represents the left index.
     * @param right
     *            Represents the right index.
     * @param comparison
     *            Represents the short value that is being compared.
     * @return The left index.
     * @throws IOException
     */
    public int partition(int left, int right, short comparison)
        throws IOException {
        int initialRight = right + 1;
        while (left <= right) {

            bufferPool.getBytes(currLeft, left, recSize);
            ByteBuffer leftWrapped = ByteBuffer.wrap(currLeft);
            short leftShort = leftWrapped.getShort();

            while (leftShort < comparison) {
                left = left + 1;
                bufferPool.getBytes(currLeft, left, recSize);
                leftWrapped = ByteBuffer.wrap(currLeft);
                leftShort = leftWrapped.getShort();
                if (left >= right) {
                    break;
                }
            }
            bufferPool.getBytes(currRight, right, recSize);
            ByteBuffer rightWrapped = ByteBuffer.wrap(currRight);
            short rightShort = rightWrapped.getShort();
            while ((right >= left) && (rightShort >= comparison)) {
                right = right - 1;
                if (right >= 0) {
                    bufferPool.getBytes(currRight, right, recSize);
                    rightWrapped = ByteBuffer.wrap(currRight);
                    rightShort = rightWrapped.getShort();
                }
                else {
                    break;
                }
            }
            if (right > left) {
                modifiedSwap(left, right, currLeft, currRight);
            }
        }
        normalSwap(left, initialRight, currLeft);
        return left;
    }


    /**
     * This method swaps two records.
     * 
     * @param left
     *            Represents the left index.
     * @param right
     *            Represents the right index.
     * @param leftRecord
     *            Represents the left record.
     * @param rightRecord
     *            Represents the right record.
     * @throws IOException
     */
    public void modifiedSwap(
        int left,
        int right,
        byte[] leftRecord,
        byte[] rightRecord)
        throws IOException {
        bufferPool.write(leftRecord, right, recSize);
        bufferPool.write(rightRecord, left, recSize);
    }


    /**
     * This method swaps two records.
     * 
     * @param left
     *            Represents the left index.
     * @param right
     *            Represents the right index.
     * @param leftRecord
     *            Represents the left record
     * @throws IOException
     */
    public void normalSwap(int left, int right, byte[] leftRecord)
        throws IOException {
        byte[] rightArray = new byte[4];
        bufferPool.write(bufferPool.getBytes(rightArray, right, recSize), left,
            recSize);
        bufferPool.write(leftRecord, right, recSize);
    }
}
