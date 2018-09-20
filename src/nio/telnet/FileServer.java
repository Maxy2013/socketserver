package nio.telnet;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author lz
 * 2018/9/19 10:14
 */
public class FileServer {

    private static final Integer PORT = 8090;

    private static final String FILE_PATH = "resource/data.txt";

    private void start(){
        try {
            FileChannel fileChannel = new FileOutputStream(FILE_PATH).getChannel();
            //获取通道，改通道允许写操作
            //将字节数组包装到缓冲区
            fileChannel.write(ByteBuffer.wrap("Some file".getBytes()));
            //关闭通道
            fileChannel.close();

            //随机读写文件流创建的通道
            fileChannel = new RandomAccessFile(FILE_PATH, "rw").getChannel();
            //使用fileChannel.position()计算出从文件的开始位置到当前位置的字节数
            System.out.println("此文件的当前位置是："+ fileChannel.position());
            //设置此通道的文件位置，fileChannel.size()获取当前文件的大小
            fileChannel.position(fileChannel.size());//该语句执行完成后，通道中的文件位置在该文件的末尾
            //在文件末尾写入字节
            fileChannel.write(ByteBuffer.wrap("\r\nAdd some text".getBytes()));
            fileChannel.close();


            //用通道读取文件
            fileChannel = new FileInputStream(FILE_PATH).getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            //将文件内容读到指定的缓冲区
            fileChannel.read(buffer);
            //注意：buffer.flip();
            //一定得有，如果没有，就是从文件最后开始读取的，当然读出来的都是byte=0时候的字符。
            //通过buffer.flip();这个语句，就能把buffer的当前位置更改为buffer缓冲区的第一个位置。
            buffer.flip();
            if(buffer.hasRemaining()){
                System.out.println(buffer.remaining());
            }
            while (buffer.hasRemaining()){
                System.out.print((char)buffer.get() + ",");
            }
            fileChannel.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new FileServer().start();
    }

}
