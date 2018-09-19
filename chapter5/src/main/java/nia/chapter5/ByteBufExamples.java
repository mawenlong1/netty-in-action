package nia.chapter5;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.ByteProcessor;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Random;

import static io.netty.channel.DummyChannelHandlerContext.DUMMY_INSTANCE;

/**
 * @author mawenlong
 * @date 2018/09/19
 * describe:
 */
public class ByteBufExamples {
    private final static Random random = new Random();
    private static final ByteBuf BYTE_BUF_FROM_SOMEWHERE = Unpooled.buffer(1024);
    private static final Channel CHANNEL_FROM_SOMEWHERE = new NioSocketChannel();
    private static final ChannelHandlerContext CHANNEL_HANDLER_CONTEXT_FROM_SOMEWHERE = DUMMY_INSTANCE;

    /**
     * 代码清单 5-1 支撑数组
     */
    public static void heapBuffer() {
        ByteBuf headBuf = BYTE_BUF_FROM_SOMEWHERE;
        //检查ByteBuf是否有一个支撑数组
        if (headBuf.hasArray()) {
            //获取数组的引用
            byte[] array = headBuf.array();
            //计算第一个字节的偏移量
            int offset = headBuf.arrayOffset() + headBuf.readerIndex();
            //获取可读数组的字节数
            int length = headBuf.readableBytes();
            //使用数组，偏移量和长度作为参数调用你的方法
            handleArray(array, offset, length);
        }
    }


    /**
     * 代码清单 5-2 访问直接缓冲区的数据
     */
    public static void directBuffer() {
        ByteBuf directBuf = BYTE_BUF_FROM_SOMEWHERE;
        if (!directBuf.hasArray()) {
            int length = directBuf.readableBytes();
            byte[] array = new byte[length];
            directBuf.getBytes(directBuf.readerIndex(), array);
            //使用数组，偏移量和长度作为参数调用你的方法
            handleArray(array, 0, length);
        }
    }

    /**
     * 代码清单 5-3 使用 ByteBuffer 的复合缓冲区模式
     */
    public static void byteBufferComposite(ByteBuffer header, ByteBuffer body) {
        ByteBuffer[] message = new ByteBuffer[]{header, body};
        ByteBuffer message2 = ByteBuffer.allocate(header.remaining() + body.remaining());
        message2.put(header);
        message2.put(body);
        message2.flip();
    }

    /**
     * 代码清单 5-4 使用 CompositeByteBuf 的复合缓冲区模式
     */
    public static void byteBufComposite() {
        CompositeByteBuf messageBuf = Unpooled.compositeBuffer();
        ByteBuf headerBuf = BYTE_BUF_FROM_SOMEWHERE;
        ByteBuf bodyBuf = BYTE_BUF_FROM_SOMEWHERE;
        //将 ByteBuf 实例追加到 CompositeByteBuf
        messageBuf.addComponents(headerBuf, bodyBuf);

        //删除索引位置为0（第一个组件的）ByteBuf
        messageBuf.removeComponent(0);
        //循环遍历ByteBuf实例
        for (ByteBuf buf : messageBuf) {
            System.out.println(buf);
        }
    }

    /**
     * 代码清单 5-5 访问 CompositeByteBuf 中的数据
     */
    public static void byteBufCompositeArray() {
        CompositeByteBuf comBuf = Unpooled.compositeBuffer();
        int length = comBuf.readableBytes();
        byte[] array = new byte[length];
        //将字节读到数组
        comBuf.getBytes(comBuf.readerIndex(), array);
        handleArray(array, 0, array.length);
    }

    /**
     * 代码清单 5-6 访问数据
     */
    public static void byteBufRelativeAccess() {
        ByteBuf buffer = BYTE_BUF_FROM_SOMEWHERE;
        for (int i = 0; i < buffer.capacity(); i++) {
            byte b = buffer.getByte(i);
            System.out.println((char) b);
        }
    }

    /**
     * 代码清单 5-7 读取所有数据
     */
    public static void readAllData() {
        ByteBuf buffer = BYTE_BUF_FROM_SOMEWHERE;
        while (buffer.isReadable()) {
            System.out.println(buffer.readByte());
        }
    }

    /**
     * 代码清单 5-8 写数据
     */
    public static void write() {
        ByteBuf buffer = BYTE_BUF_FROM_SOMEWHERE;
        while (buffer.writableBytes() >= 4) {
            buffer.writeInt(random.nextInt());
        }
    }

    /**
     * 代码清单 5-9 使用 ByteBufProcessor 来寻找\r
     * <p>
     * use {@link io.netty.buffer.ByteBufProcessor in Netty 4.0.x}
     */
    public static void byteProcessor() {
        ByteBuf buffer = BYTE_BUF_FROM_SOMEWHERE;
        //寻找回车符（/r）
        int index = buffer.forEachByte(ByteProcessor.FIND_CR);
    }

    /**
     * 代码清单 5-10 对 ByteBuf 进行切片
     */
    public static void byteBufSlice() {
        Charset utf8 = Charset.forName("UTF-8");
        ByteBuf buf = Unpooled.copiedBuffer("Netty in action ", utf8);
        ByteBuf sliced = buf.slice(0, 15);
        System.out.println(sliced.toString(utf8));
        //更新0处索引
        buf.setByte(0, (byte) 'J');
        assert buf.getByte(0) == sliced.getByte(0);
    }

    /**
     * 代码清单 5-11 复制一个 ByteBuf
     */
    public static void byteBufCopy() {
        Charset utf8 = Charset.forName("UTF-8");
        ByteBuf buf = Unpooled.copiedBuffer("Netty in action ", utf8);
        ByteBuf copy = buf.copy(0, 15);
        System.out.println(copy.toString(utf8));
        buf.setByte(0, (byte) 'J');
        //将会成功，因为数据不是共享的
        assert buf.getByte(0) != copy.getByte(0);
    }

    /**
     * 代码清单 5-12 get()和 set()方法的用法
     */
    public static void byteBufSetGet() {
        Charset utf8 = Charset.forName("UTF-8");
        //创建一个新的 ByteBuf以保存给定字符串的字节
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);
        //打印第一个字符'N'
        System.out.println((char) buf.getByte(0));
        //存储当前的 readerIndex 和 writerIndex
        int readerIndex = buf.readerIndex();
        int writerIndex = buf.writerIndex();
        buf.setByte(0, (byte) 'B');
        //打印第一个字符，现在是'B'
        System.out.println((char) buf.getByte(0));
        //将会成功，因为这些操作并不会修改相应的索引
        assert readerIndex == buf.readerIndex();
        assert writerIndex == buf.writerIndex();
    }

    /**
     * 代码清单 5-13 ByteBuf 上的 read()和 write()操作
     */
    public static void byteBufWriteRead() {
        Charset utf8 = Charset.forName("UTF-8");
        //创建一个新的 ByteBuf 以保存给定字符串的字节
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);
        //打印第一个字符'N'
        System.out.println((char) buf.readByte());
        //存储当前的readerIndex
        int readerIndex = buf.readerIndex();
        //存储当前的writerIndex
        int writerIndex = buf.writerIndex();
        //将字符 '?'追加到缓冲区
        buf.writeByte((byte) '?');
        assert readerIndex == buf.readerIndex();
        //将会成功，因为 writeByte()方法移动了 writerIndex
        assert writerIndex != buf.writerIndex();
    }

    /**
     * 代码清单 5-14 获取一个到 ByteBufAllocator 的引用
     */
    public static void obtainingByteBufAllocatorReference() {
        Channel channel = CHANNEL_FROM_SOMEWHERE;
        //从 Channel 获取一个到ByteBufAllocator 的引用
        ByteBufAllocator allocator = channel.alloc();
        //...
        ChannelHandlerContext ctx = null;
        //从 ChannelHandlerContext 获取一个到 ByteBufAllocator 的引用
        ByteBufAllocator allocator2 = ctx.alloc();
        //...
    }

    /**
     * 代码清单 5-15 引用计数
     */
    public static void referenceCounting() {
        Channel channel = CHANNEL_FROM_SOMEWHERE;
        //从 Channel 获取ByteBufAllocator
        ByteBufAllocator allocator = channel.alloc();
        //...
        //从 ByteBufAllocator分配一个 ByteBuf
        ByteBuf buffer = allocator.directBuffer();
        //检查引用计数是否为预期的 1
        assert buffer.refCnt() == 1;
        //...
    }

    /**
     * 代码清单 5-16 释放引用计数的对象
     */
    public static void releaseReferenceCountedObject() {
        ByteBuf buffer = BYTE_BUF_FROM_SOMEWHERE;
        //减少到该对象的活动引用。当减少到 0 时，该对象被释放，并且该方法返回 true
        boolean released = buffer.release();
        //...
    }

    /**
     * 使用数组，偏移量和长度作为参数调用你的方法
     */
    private static void handleArray(byte[] array, int offset, int length) {
    }

    public static void main(String[] args) {
        ByteBufExamples.byteBufSlice();
    }
}
