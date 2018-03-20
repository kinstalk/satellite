package com.kinstalk.satellite.utils;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by digitZhang on 2018/3/1.
 */
public class FileUtils {

	/**
	 * 将文件读成流
	 *
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static byte[] readFile(String url) throws IOException {

		File f = new File(url);
		if (!f.exists()) {
			throw new FileNotFoundException(url);
		}

		FileChannel channel = null;
		FileInputStream fs = null;
		try {
			fs = new FileInputStream(f);
			channel = fs.getChannel();
			ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
			while ((channel.read(byteBuffer)) > 0) {
				// do nothing
				// System.out.println("reading");
			}

			return byteBuffer.array();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {

				channel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				fs.close();
				// f.delete();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}



	public static void saveDocument(Document doc, String outputPath) {
		// 输出文件
		File outputFile = new File(outputPath);
		try {
			// 美化格式
			OutputFormat format = OutputFormat.createPrettyPrint();
			// 指定XML编码,不指定的话，默认为UTF-8
			format.setEncoding("UTF-8");
			XMLWriter output = new XMLWriter(new FileWriter(outputFile), format);
			output.write(doc);
			output.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}
