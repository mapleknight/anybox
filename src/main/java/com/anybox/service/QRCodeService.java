package com.anybox.service;

//import java.io.File;
//import java.io.IOException;
//import java.util.Hashtable;
//
//import net.sf.json.JSONObject;

//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.EncodeHintType;
//import com.google.zxing.MultiFormatWriter;
//import com.google.zxing.WriterException;
//import com.google.zxing.client.j2se.MatrixToImageWriter;
//import com.google.zxing.common.BitMatrix;

public class QRCodeService {

	/*
	public String getQRCode(String id, String name, String password)
	{
		JSONObject qrJson = new JSONObject();
		qrJson.put("id", id);
		qrJson.put("name", name);
		qrJson.put("password", password);
		qrJson.put("brand", "anybox");
		
		String fileName = "/qrcode/" + id + "_" + name + ".png";
		
		String qrText = qrJson.toString();
		int width = 100;
		int height = 100;
		String format = "png";
		Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		BitMatrix bitMatrix;
		try {
			bitMatrix = new MultiFormatWriter().encode(qrText,
					BarcodeFormat.QR_CODE, width, height, hints);
			File outputFile = new File(fileName);
			MatrixToImageWriter.writeToFile(bitMatrix, format, outputFile);
			
		} catch (WriterException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return fileName;
	}
	*/
}
