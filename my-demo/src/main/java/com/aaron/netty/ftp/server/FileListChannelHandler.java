package com.aaron.netty.ftp.server;

import com.aaron.netty.ftp.server.service.FileService;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.CharsetUtil;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author FengHaixin
 * @description 一句话描述该文件的用途
 * @date 2018/9/4
 */
@Slf4j
@Component
@Scope ("prototype")
public class FileListChannelHandler extends SimpleChannelInboundHandler<HttpRequest>
{

    private static String TEMPLATE;

    @Autowired
    private FileService fileService;


    static
    {
        InputStream resourceAsStream = FileListChannelHandler.class.getClassLoader().getResourceAsStream("template/index.html");

        try
        {
            TEMPLATE = IOUtils.toString(resourceAsStream);
        }
        catch (IOException e)
        {
            TEMPLATE = "";
            log.error("页面读取错误", e);
        }
    }


    public FileListChannelHandler()
    {
        super(false);
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        log.error("客户端已连接，id：{}", ctx.channel().id());
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpRequest request) throws Exception
    {

        String filePath = "/".equals(request.uri()) ? FtpServer.FTP_SERVER_RESOURCE_LOCATION : request.uri();

        File file = new File(filePath);

        if (file.isDirectory())
        {
            String html = getFileList(file);

            //向客户端会写服务器上指定的文件夹下的文件列表
            HttpMessage message = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                                                              HttpResponseStatus.OK,
                                                              Unpooled.copiedBuffer(html, CharsetUtil.UTF_8));
            ctx.write(message);
            ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        }
        else
        {
            byte[] fileToByteArray = FileUtils.readFileToByteArray(file);

            //向客户端直接返回文件数据
            ctx.write(Unpooled.copiedBuffer(fileToByteArray));
            ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        }
    }


    private String getFileList(File file)
    {
        List<String> allFileList = fileService.getAllFileList(file);

        String result = TEMPLATE.replaceFirst("\\{currentTime}", LocalDateTime.now().toString());
        result = result.replace("\\{parent}", "<a href=\"" + file.getPath() + "\">" + file.getName() + "</a>");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < allFileList.size(); i++)
        {
            stringBuilder.append("<li><a href=\"")
                         .append(file.getPath())
                         .append("/")
                         .append(allFileList.get(i))
                         .append("\">")
                         .append(allFileList.get(i))
                         .append("</a></li>");
        }

        return result.replaceFirst("\\{content}", stringBuilder.toString());
    }
}