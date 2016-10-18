package com.qb.china.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Rect;
import org.bytedeco.javacpp.opencv_core.Size;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.bytedeco.javacpp.opencv_imgproc;

public class ImageUtil2 {
	public static final String DEFAULT_IMAGE_FORMAT = "jpg";

	/**
	 * 普通上传的图片处理
	 * 
	 * @param srcFilePath
	 * @param destFilePath
	 * @param previewFilePath
	 * @param previewScaleWidth
	 * @param thumbFilePath
	 * @param thumbScaleWidth
	 * @param rotate
	 * @param cropCoordinate
	 * @param needGray
	 * @return TODO
	 * @throws IOException
	 */
	public static double processImage(String srcFilePath, String destFilePath, String previewFilePath,
			int previewScaleWidth, String thumbFilePath, int thumbScaleWidth, int rotate, int[] cropCoordinate,
			boolean needGray) throws Exception {
		double previewScale = 1;
		File srcFile = new File(srcFilePath);
		File destFile = new File(destFilePath);
		if (needProcess(rotate, cropCoordinate, needGray, previewScaleWidth, thumbScaleWidth)) {
			Mat src = opencv_imgcodecs.imread(srcFilePath);
			double srcWidth = src.arrayWidth();

			Mat rotateDest = src;
			if (rotate != 0) {
				rotateDest = rotate(src, rotate);
				src.release();
			}

			Mat cropDest = rotateDest;
			if (cropCoordinate != null && cropCoordinate.length == 4) {
				cropDest = crop(rotateDest, cropCoordinate);
				rotateDest.release();
			}

			Mat grayDest = cropDest;
			if (needGray) {
				grayDest = gray(cropDest);
				cropDest.release();
			}

			opencv_imgcodecs.imwrite(destFilePath, grayDest);

			Mat previewDest = grayDest;
			double previewWidth = 0;
			if (previewScaleWidth > 0) {
				previewDest = scaleByWidth(grayDest, previewScaleWidth);
				grayDest.release();
				previewWidth = previewDest.arrayWidth();
				opencv_imgcodecs.imwrite(previewFilePath, previewDest);
			}

			Mat thumbDest = previewDest;
			if (thumbScaleWidth > 0) {
				thumbDest = scaleByWidth(previewDest, thumbScaleWidth);
				previewDest.release();
				opencv_imgcodecs.imwrite(thumbFilePath, thumbDest);
			}
			previewScale = previewWidth / srcWidth;
		} else {
			FileUtils.copyFile(srcFile, destFile);
		}
		return previewScale;
	}

	/**
	 * 基于预览图的图片处理
	 * 
	 * @param srcFilePath
	 * @param destFilePath
	 * @param previewWidth
	 * @param thumbFilePath
	 * @param thumbWidth
	 * @param previewRotate
	 * @param previewCropCoordinate
	 * @param needGray
	 * @throws IOException
	 */
	public static void processImage(String srcFilePath, String destFilePath, String thumbFilePath, int thumbWidth,
			double previewScale, float previewRotate, int[] previewCropCoordinate, boolean needGray) throws IOException {

		Mat src = opencv_imgcodecs.imread(srcFilePath);

		Mat rotateDest = src;
		if (previewRotate != 0) {
			rotateDest = rotate(src, previewRotate);
			src.release();
		}

		Mat cropDest = rotateDest;
		if (previewCropCoordinate != null && previewCropCoordinate.length == 4) {
			int[] cropCoordinate = scaleCropCoordinate(previewScale, previewCropCoordinate);
			cropDest = crop(rotateDest, cropCoordinate);
			rotateDest.release();
		}

		Mat grayDest = cropDest;
		if (needGray) {
			grayDest = gray(cropDest);
			cropDest.release();
		}

		opencv_imgcodecs.imwrite(destFilePath, grayDest);

		Mat thumbDest = grayDest;
		if (thumbWidth > 0) {
			thumbDest = scaleByWidth(grayDest, thumbWidth);
			grayDest.release();
			opencv_imgcodecs.imwrite(thumbFilePath, thumbDest);
		}
	}

	private static int[] scaleCropCoordinate(double previewScale, int[] previewCropCoordinate) {
		int[] cropCoordinate = new int[4];
		for (int i = 0; i < 4; i++) {
			cropCoordinate[i] = (int) (previewCropCoordinate[i] / previewScale);
		}
		return cropCoordinate;
	}

	private static boolean needProcess(float rotate, int[] cropCoordinate, boolean needGray, int previewScaleWidth,
			int thumbScaleWidth) {
		return (rotate != 0) || (cropCoordinate != null && cropCoordinate.length == 4) || needGray
				|| (previewScaleWidth > 0) || (thumbScaleWidth > 0);
	}

	public static Mat gray(Mat src) {
		Mat dest = new Mat();
		opencv_imgproc.cvtColor(src, dest, opencv_imgproc.COLOR_BGR2GRAY);
		return dest;
	}

	public static Mat rotate(Mat src, float angle) {
		Mat dest = new Mat();
		if (angle == 0 || angle == 360) {
			src.assignTo(dest);
		} else if (angle == 90) {
			// Rotate clockwise 90 degrees
			opencv_core.flip(src.t().asMat(), dest, 1);
		} else if (angle == 180) {
			// Rotate clockwise 180 degrees
			opencv_core.flip(src, dest, -1);
		} else if (angle == 270) {
			// Rotate clockwise 270 degrees
			opencv_core.flip(src.t().asMat(), dest, 0);
		}
		return dest;
	}

	public static Mat scale(Mat src, int width, int height) {
		Mat dest = new Mat();
		Size size = new Size(width, height);
		opencv_imgproc.resize(src, dest, size);
		return dest;
	}

	public static Mat scaleByWidth(Mat src, int scaleWidth) {
		Mat dest = new Mat();
		if (scaleWidth <= src.arrayWidth()) {
			Size size = new Size(scaleWidth, src.arrayHeight() * scaleWidth / src.arrayWidth());
			opencv_imgproc.resize(src, dest, size);
		} else {
			src.assignTo(dest);
		}
		return dest;
	}

	public static Mat scaleByHeight(Mat src, int scaledHeight) {
		Mat dest = new Mat();
		if (scaledHeight <= src.arrayHeight()) {
			Size size = new Size(src.arrayWidth() * scaledHeight / src.arrayHeight(), scaledHeight);
			opencv_imgproc.resize(src, dest, size);
		} else {
			src.assignTo(dest);
		}
		return dest;
	}

	public static Mat crop(Mat src, int[] cropCoordinate) {
		//判断裁剪坐标是否越界
		if (cropCoordinate[0] + cropCoordinate[2] > src.arrayWidth()) {
			cropCoordinate[2] = src.arrayWidth() - cropCoordinate[0];
		}

		if (cropCoordinate[1] + cropCoordinate[3] > src.arrayHeight()) {
			cropCoordinate[3] = src.arrayHeight() - cropCoordinate[1];
		}
		Rect roi = new Rect(cropCoordinate[0], cropCoordinate[1], cropCoordinate[2], cropCoordinate[3]);
		return new Mat(src, roi);
	}

	public static void pressWartermarks(String templateImgPath, String destImgPath, List<WatermarkInfo> watermarkInfos)
			throws IOException {

		Image image = ImageIO.read(new File(templateImgPath));
		int width = image.getWidth(null);
		int height = image.getHeight(null);
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bufferedImage.createGraphics();
		g.drawImage(image, 0, 0, width, height, null);

		for (WatermarkInfo watermarkInfo : watermarkInfos) {
			if (watermarkInfo instanceof WatermarkImageInfo) {
				WatermarkImageInfo watermarkImageInfo = (WatermarkImageInfo) watermarkInfo;
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, watermarkImageInfo.alpha));
				Image watermarkImage = ImageIO.read(new File(watermarkImageInfo.imgPath));

				int waterImageWidth = watermarkImageInfo.width;
				if (waterImageWidth == 0) {
					waterImageWidth = watermarkImage.getWidth(null);
				}

				int waterImageHeight = watermarkImageInfo.height;
				if (waterImageHeight == 0) {
					waterImageHeight = watermarkImage.getHeight(null);
				}

				g.drawImage(watermarkImage, watermarkImageInfo.x, watermarkImageInfo.y, waterImageWidth,
						waterImageHeight, null);
			} else if (watermarkInfo instanceof WatermarkTextInfo) {
				WatermarkTextInfo watermarkTextInfo = (WatermarkTextInfo) watermarkInfo;
				g.setFont(watermarkTextInfo.font);
				g.setColor(Color.BLACK);
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, watermarkTextInfo.alpha));
				g.drawString(watermarkTextInfo.text, watermarkTextInfo.x, watermarkTextInfo.y);
			}
		}
		g.dispose();
		ImageIO.write(bufferedImage, "jpg", new File(destImgPath));
	}

	public static class WatermarkInfo {
		protected int x;
		protected int y;
		protected float alpha;
	}

	public static class WatermarkImageInfo extends WatermarkInfo {
		private String imgPath;
		private int width;
		private int height;

		public WatermarkImageInfo(String imgPath, int x, int y, int width, int height, float alpha) {
			super();
			this.imgPath = imgPath;
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			this.alpha = alpha;
		}

		public WatermarkImageInfo(String imgPath, int x, int y, int width, int height) {
			this(imgPath, x, y, width, height, 1);
		}

		public String getImgPath() {
			return imgPath;
		}

		public void setImgPath(String imgPath) {
			this.imgPath = imgPath;
		}
	}

	public static class WatermarkTextInfo extends WatermarkInfo {
		private String text;
		private Font font;
		private Color color;

		public WatermarkTextInfo(String text, int x, int y, Font font, Color color, float alpha) {
			super();
			this.text = text;
			this.x = x;
			this.y = y;
			this.font = font;
			this.color = color;
			this.alpha = alpha;
		}

		public WatermarkTextInfo(String text, int x, int y, Font font, Color color) {
			this(text, x, y, font, color, 1);
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public Color getColor() {
			return color;
		}

		public void setColor(Color color) {
			this.color = color;
		}
	}
}
