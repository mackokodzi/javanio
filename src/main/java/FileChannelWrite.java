import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Buffer -> Channel -> Source
 */
public class FileChannelWrite {
    public static void main(String[] args) throws IOException {

        String input = "Some very important data";
        System.out.print("Input string: " + input);

        byte[] inputBytes = input.getBytes(); //convert string to bytes
        ByteBuffer buffer = ByteBuffer.wrap(inputBytes); //apply bytes to a buffer

        String filePath = "writesource.txt";
        FileOutputStream fos = new FileOutputStream(filePath);
        FileChannel fileChannel = fos.getChannel();
        fileChannel.write(buffer); //writes a sequence of bytes to this channel from the given buffer.
        fileChannel.close();
        fos.close();
    }
}
