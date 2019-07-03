package com.jike.socketServer;

import sun.plugin.javascript.navig.Array;

import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.io.*;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @program: jike_study
 * @description:
 * @author: dyingstraw
 * @create: 2019-03-25 10:30
 **/
public class Util {


    public static byte[] unZip(byte[] ziped) throws IOException {
        ByteArrayOutputStream outputStream1 = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(ziped);
        GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);
        byte r2[] = new byte[256];
        int n=0;
        while ((n=gzipInputStream.read(r2))>0){
            outputStream1.write(r2,0,n);
        }
        gzipInputStream.close();
        outputStream1.close();
        return outputStream1.toByteArray();
    }
    public static byte[] zip(String fileName) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream);
        OutputStream o2 = new ByteArrayOutputStream();
        ImageInputStream imageInputStream = new FileImageInputStream(new File(fileName));
        byte data[] = new byte[256];
        int c =0;
        while(( c= imageInputStream.read(data))>0)
        {
            o2.write(data,0,c);
        }
        data = ((ByteArrayOutputStream) o2).toByteArray();
        gzipOutputStream.write(data);
        gzipOutputStream.finish();
        gzipOutputStream.flush();
        gzipOutputStream.close();

        byte[] result = outputStream.toByteArray();
        o2.close();
        outputStream.close();
        return result;
    }
    public static double bytes2Double(byte[] arr){
        long value = 0;
        for (int i = 0; i < 8; i++) {
            value |= ((long) (arr[i] & 0xff)) << (8 * i);
        }
        return Double.longBitsToDouble(value);
    }
    public static long bytes2Long(byte[] arr){
        long value = 0;
        for (int i = 0; i < 8; i++) {
            value |= ((long) (arr[i] & 0xff)) << (8 * i);
        }
        return value;
    }

    // 解析数据包
    public static void decodePackage(byte[] buff) throws IOException {
        byte[] result =buff;
        int len = buff.length - 1;
        int packageLen=0;
        if (!(result[0]==0xff && result[1]== 0xff) || !((result[len]==0xff && result[len-1]== 0xff))){
            throw new IllegalArgumentException("数据包有问题");
        }else {
            // 获得数据包长度
            packageLen = result[1]*256*256*256 + result[2]*256*256 + result[3]*256*256 + result[4];
            byte[] data = Arrays.copyOfRange(result, 6, 6 + packageLen);
            // 解压数据
            byte[] finalData = unZip(data);
            CarStatus carStatus = new CarStatus();
            StringBuilder stringBuilder = new StringBuilder();
            // 解析carid
            for (int i = 0; i < 10; i++) {
                stringBuilder.append((char)finalData[i]);
            }
            carStatus.setCarId(stringBuilder.toString());
            // 解析carid 解析电池剩余电
            carStatus.setBatteryPower(finalData[10]);
            // 解析车所在经纬度
            carStatus.setLocation(
                    new CarStatus.GlobalLocation(
                            bytes2Double(Arrays.copyOfRange(finalData,11,19)),
                            bytes2Double(Arrays.copyOfRange(finalData,19,27)))
            );

            // 汽车速度
            carStatus.setCarSpeed(bytes2Double(Arrays.copyOfRange(finalData,27,35)));
            // 控制器温度
            carStatus.setTemperatureOfController(bytes2Double(Arrays.copyOfRange(finalData,35,43)));
            // 马达温度
            carStatus.setTemperatureOfMotor(bytes2Double(Arrays.copyOfRange(finalData,43,51)));
            // 上传时间
            carStatus.setTime(bytes2Long(Arrays.copyOfRange(finalData,51,59)));
            // 剪线报警
            carStatus.setAlarmCut(finalData[59]==1);
            // 震动报警
            carStatus.setAlarmVibration(finalData[60]==1);
            // 视频流
            OutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(Arrays.copyOfRange(finalData,61,len));
            carStatus.setVideoFrame(outputStream);
            // 保存信息到数据库
            saveTodb(carStatus);

        }

    }

    private static void saveTodb(CarStatus carStatus) {
    }

    public static byte[] encodePackage(byte[] buff){
        return new byte[10];
    }
}
