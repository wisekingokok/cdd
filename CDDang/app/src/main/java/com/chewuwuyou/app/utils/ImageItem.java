package com.chewuwuyou.app.utils;

import java.io.IOException;
import java.io.Serializable;

import android.graphics.Bitmap;
import android.net.Uri;

public class ImageItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String imageId;
	public String thumbnailPath;
	public String imagePath;
	private Bitmap bitmap;
	private Uri imageUri;
	private boolean isChanged;// 个人中心编辑那里要用
//	private int status;// 11 add, 12, update, 13 delete
	public boolean isSelected = false;

	public ImageItem() {
	}

	public ImageItem(String imagePath, String imageId) {
		this.imagePath = imagePath;
		this.imageId = imageId;
	}

	public static ImageItem clone(ImageItem imageItem) {
		ImageItem ii = new ImageItem();
		ii.imageId = imageItem.imageId;
		ii.isSelected = imageItem.isSelected;
		ii.thumbnailPath = imageItem.thumbnailPath;
		ii.imagePath = imageItem.imagePath;
		ii.setBitmap(imageItem.getBitmap());
		ii.setImageUri(imageItem.getImageUri());
		ii.setChanged(imageItem.isChanged());
//		ii.setStatus(imageItem.getStatus());

		return ii;
	}

	/**
	 * 11 add, 12, update, 13 delete
	 * 
	 * @param imagePath
	 * @param status
	 */
	public ImageItem(String imagePath, int status) {
		this.imagePath = imagePath;
//		this.status = status;
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public String getThumbnailPath() {
		return thumbnailPath;
	}

	public void setThumbnailPath(String thumbnailPath) {
		this.thumbnailPath = thumbnailPath;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public Uri getImageUri() {
		return imageUri;
	}

	public void setImageUri(Uri imageUri) {
		this.imageUri = imageUri;
	}

	public boolean isChanged() {
		return isChanged;
	}

	public void setChanged(boolean isChanged) {
		this.isChanged = isChanged;
	}

	public Bitmap getBitmap() {
		if (bitmap == null) {
			try {
				bitmap = Bimp.revitionImageSize(imagePath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

//	public int getStatus() {
//		return status;
//	}
//
//	public void setStatus(int status) {
//		this.status = status;
//	}

}
