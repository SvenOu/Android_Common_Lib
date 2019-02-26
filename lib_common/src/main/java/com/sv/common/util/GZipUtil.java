package com.sv.common.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class GZipUtil {

	public static final int BUFF_SIZE = 1024;
	public static final String JSAON_FILE_NAME = "data.json";

	private static ObjectMapper om = new ObjectMapper();
	static {
		om.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	public static void byteArrayToZipFiles(byte[] zipBytes,
			String zipFileFolder, String id, String languageCode) {// zipFileFolder需要加斜杠/,id必须唯一，否则异步操作会crash

		ZipInputStream zipStream = new ZipInputStream(new ByteArrayInputStream(
				zipBytes));
		ZipEntry entry = null;
		try {
			entry = zipStream.getNextEntry();
			// while ((entry = zipStream.getNextEntry()) != null) {//解压第全部文件
			if (entry != null) {// 解压第一个文件

				String entryName = entry.getName();// data.json
				File jsonFile = new File(zipFileFolder + id + languageCode
						+ entryName);
				if (!jsonFile.exists()) {
					FileOutputStream out = new FileOutputStream(jsonFile);
					byte[] byteBuff = new byte[BUFF_SIZE];
					int bytesRead = 0;
					while ((bytesRead = zipStream.read(byteBuff)) != -1) {
						out.write(byteBuff, 0, bytesRead);
					}
					out.close();
				}
			}
			zipStream.closeEntry();
			zipStream.close();
		} catch (FileNotFoundException e) {
			Logger.e("byteArrayToZipFiles", e.getMessage());
		} catch (IOException e) {
			Logger.e("byteArrayToZipFiles", e.getMessage());
		}

	}

	/**
	 * 把一系列文件打包成zip文件
	 * @param files 将要被打包的文件
	 * @param zipFile zip文件路径
	 */
	public static void zip(String[] files, String zipFile) {// 参数均为文件完整路径
		ZipOutputStream out = null;
		try {
			BufferedInputStream origin = null;
			FileOutputStream dest = new FileOutputStream(zipFile);

			out = new ZipOutputStream(new BufferedOutputStream(
					dest));

			byte data[] = new byte[BUFF_SIZE];

			for (int i = 0; i < files.length; i++) {
				Logger.v("Compress", "Adding: " + files[i]);
				FileInputStream fi = new FileInputStream(files[i]);
				origin = new BufferedInputStream(fi, BUFF_SIZE);

				ZipEntry entry = new ZipEntry(files[i].substring(files[i]
						.lastIndexOf("/") + 1));// 从路径提取文件名

				out.putNextEntry(entry);
				int count;
				while ((count = origin.read(data, 0, BUFF_SIZE)) != -1) {
					out.write(data, 0, count);
				}
				origin.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != out) {
                    out.close();
                }
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> loadJSONFromFile(String jsonFilePath) {
		Map<String, Object> jsonData = null;
		try {
			jsonData = (Map<String, Object>) om.readValue(new File(
					jsonFilePath), Map.class);
		} catch (JsonParseException e) {
			Logger.e("loadJSONFromFile", e.getMessage());
		} catch (JsonMappingException e) {
			Logger.e("loadJSONFromFile", e.getMessage());
		} catch (IOException e) {
			Logger.e("loadJSONFromFile", e.getMessage());
		}
		return jsonData;

	}

	public static void gzipFile(String sourceFilePath,
			String destinaton_zip_filepath) {

		byte[] buffer = new byte[BUFF_SIZE];
		FileInputStream fileInput = null;
		GZIPOutputStream gzipOuputStream = null;
		try {

			fileInput = new FileInputStream(sourceFilePath);
			gzipOuputStream = new GZIPOutputStream(new FileOutputStream(
					destinaton_zip_filepath));

			int bytes_read;

			while ((bytes_read = fileInput.read(buffer)) != -1) {
				gzipOuputStream.write(buffer, 0, bytes_read);

			}

			gzipOuputStream.finish();

			gzipOuputStream.flush();

			Logger.e("gzipFile", "The file was compressed successfully!");

		} catch (Exception e) {
			Logger.e("gzipFile-Exception", e.getMessage());
		} finally {
			try {
				if (null != fileInput) {
					fileInput.close();
				}
				if (null != gzipOuputStream) {
					gzipOuputStream.close();
				}
			} catch (IOException e) {
				Logger.e("gzipFile-finally", e.getMessage());
			}
		}
	}

	/**
	 * Encodes the response body using GZIP and adds the corresponding header.
	 */
	public static byte[] gzipByte(byte[] bytes) {
		ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
		OutputStream gzippedOut = null;
		try {
			gzippedOut = new GZIPOutputStream(bytesOut);
			gzippedOut.write(bytes);
			gzippedOut.close();
		} catch (IOException e) {
			Logger.e("gzipByte: ", e.getMessage());
		} finally {
			try {
				gzippedOut.close();
			} catch (IOException e) {
				Logger.e("gzipByte-close: ", e.getMessage());
			}
		}
		return bytesOut.toByteArray();
	}

}
