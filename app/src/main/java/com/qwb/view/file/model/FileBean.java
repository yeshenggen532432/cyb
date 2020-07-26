package com.qwb.view.file.model;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by AlexCheung on 2016/2/2.
 */
public class FileBean implements Parcelable {
    public String path;//文件 | 文件夹  路径
    public String name;//文件 | 文件夹  名称
    public boolean isFile;//是 文件
    public boolean isImage;
    public boolean isShowEditor;//展示 选中框
    public boolean isSelected;
    public int resId;
    
    //自己添加
    public boolean isWanCheng;//是否上传完成
    public String fileType;//文件类型
    public String fsize;//文件大小
    public String imageType;//图片类型1：云盘，2：本地
    public int id;//文件id
    public int memberId;//用户id
    public String fileNm;//文件夹/文件名
    public String ftime;//时间
    public String tp1;//文件类型(如：mp3,mp4）
    public int tp2;//文件位置类型（1自己；2公司）
    public int tp3;//1文件夹；2文件
    public int pid;//上级id
    
    /**
	 *按钮的状态
	 *0 下载异常
	 *1 下载中
	 *2 暂停
	 *3 下载完成
	 * */
	private int model;
	private int fileLength;
	private int fileCurrent;//当前下载的进度
    public enum FileStatus{
        /**空 文件夹*/
        tempFolder,
        /**空 文件*/
        tempFile,
        /**是 文件*/
        isFile,
        /**是 文件夹*/
        isFolder
    }
    
    
    
    
    public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isFile() {
		return isFile;
	}
	public void setFile(boolean isFile) {
		this.isFile = isFile;
	}
	public boolean isImage() {
		return isImage;
	}
	public void setImage(boolean isImage) {
		this.isImage = isImage;
	}
	public boolean isShowEditor() {
		return isShowEditor;
	}
	public void setShowEditor(boolean isShowEditor) {
		this.isShowEditor = isShowEditor;
	}
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	public int getResId() {
		return resId;
	}
	public void setResId(int resId) {
		this.resId = resId;
	}
	public boolean isWanCheng() {
		return isWanCheng;
	}
	public void setWanCheng(boolean isWanCheng) {
		this.isWanCheng = isWanCheng;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	
	public String getFsize() {
		return fsize;
	}
	public void setFsize(String fsize) {
		this.fsize = fsize;
	}
	public int getModel() {
		return model;
	}
	public void setModel(int model) {
		this.model = model;
	}
	public int getFileLength() {
		return fileLength;
	}
	public void setFileLength(int fileLength) {
		this.fileLength = fileLength;
	}
	public int getFileCurrent() {
		return fileCurrent;
	}
	public void setFileCurrent(int fileCurrent) {
		this.fileCurrent = fileCurrent;
	}
	public String getImageType() {
		return imageType;
	}
	public void setImageType(String imageType) {
		this.imageType = imageType;
	}
	
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getMemberId() {
		return memberId;
	}
	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}
	public String getFileNm() {
		return fileNm;
	}
	public void setFileNm(String fileNm) {
		this.fileNm = fileNm;
	}
	public String getFtime() {
		return ftime;
	}
	public void setFtime(String ftime) {
		this.ftime = ftime;
	}
	public String getTp1() {
		return tp1;
	}
	public void setTp1(String tp1) {
		this.tp1 = tp1;
	}
	public int getTp2() {
		return tp2;
	}
	public void setTp2(int tp2) {
		this.tp2 = tp2;
	}
	public int getTp3() {
		return tp3;
	}
	public void setTp3(int tp3) {
		this.tp3 = tp3;
	}
	
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	//***************************Parcelable的一些写法*******************************
	public static Parcelable.Creator<FileBean> getCreator() {
		return CREATOR;
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(path);
		dest.writeString(name);
		dest.writeByte((byte)(isFile ?1:0));//if isFile == true, byte == 1
		dest.writeByte((byte)(isImage ?1:0));//if isFile == true, byte == 1
		dest.writeByte((byte)(isShowEditor ?1:0));//if isFile == true, byte == 1
		dest.writeByte((byte)(isSelected ?1:0));//if isFile == true, byte == 1
		dest.writeInt(resId);
		dest.writeByte((byte)(isWanCheng ?1:0));//if isFile == true, byte == 1
		dest.writeString(fileType);
		dest.writeString(fsize);
		dest.writeInt(model);
		dest.writeInt(fileLength);
		dest.writeInt(fileCurrent);
		dest.writeString(imageType);
		dest.writeInt(id);
		dest.writeInt(memberId);
		dest.writeString(fileNm);
		dest.writeString(ftime);
		dest.writeString(tp1);
		dest.writeInt(tp2);
		dest.writeInt(tp3);
		dest.writeInt(pid);

	}

	public static final Parcelable.Creator<FileBean> CREATOR = new Parcelable.Creator<FileBean>() {

		@Override
		public FileBean createFromParcel(Parcel source) {
			//注意write与read要一一对应
			FileBean fileBean = new FileBean();
			fileBean.path = source.readString();
			fileBean.name = source.readString();
			fileBean.isFile =source.readByte()!=0;
			fileBean.isImage =source.readByte()!=0;
			fileBean.isShowEditor =source.readByte()!=0;
			fileBean.isSelected =source.readByte()!=0;
			fileBean.resId = source.readInt();
			fileBean.isWanCheng =source.readByte()!=0;
			fileBean.fileType = source.readString();
			fileBean.fsize = source.readString();
			fileBean.model = source.readInt();
			fileBean.fileLength = source.readInt();
			fileBean.fileCurrent = source.readInt();
			fileBean.imageType = source.readString();
			
			fileBean.id = source.readInt();
			fileBean.memberId = source.readInt();
			fileBean.fileNm = source.readString();
			fileBean.ftime = source.readString();
			fileBean.tp1 = source.readString();
			fileBean.tp2 = source.readInt();
			fileBean.tp3 = source.readInt();
			fileBean.pid = source.readInt();
			
			return fileBean;
		}

		@Override
		public FileBean[] newArray(int size) {
			return new FileBean[size];
		}
	};

	@Override
	public String toString() {
		return "TreeBean [path=" + path + ", name=" + name + ", isFile="
				+ isFile + ", isImage=" + isImage + ", isShowEditor="
				+ isShowEditor + ", isSelected=" + isSelected + ", resId="
				+ resId + ", isWanCheng=" + isWanCheng + ", fileType="
				+ fileType + ", fsize=" + fsize + ", imageType=" + imageType
				+ ", id=" + id + ", memberId=" + memberId + ", fileNm="
				+ fileNm + ", ftime=" + ftime + ", tp1=" + tp1 + ", tp2=" + tp2
				+ ", tp3=" + tp3 + ", pid=" + pid + ", model=" + model
				+ ", fileLength=" + fileLength + ", fileCurrent=" + fileCurrent
				+ "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + fileCurrent;
		result = prime * result + fileLength;
		result = prime * result + ((fileNm == null) ? 0 : fileNm.hashCode());
		result = prime * result
				+ ((fileType == null) ? 0 : fileType.hashCode());
		result = prime * result + ((fsize == null) ? 0 : fsize.hashCode());
		result = prime * result + ((ftime == null) ? 0 : ftime.hashCode());
		result = prime * result + id;
		result = prime * result
				+ ((imageType == null) ? 0 : imageType.hashCode());
		result = prime * result + (isFile ? 1231 : 1237);
		result = prime * result + (isImage ? 1231 : 1237);
		result = prime * result + (isSelected ? 1231 : 1237);
		result = prime * result + (isShowEditor ? 1231 : 1237);
		result = prime * result + (isWanCheng ? 1231 : 1237);
		result = prime * result + memberId;
		result = prime * result + model;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		result = prime * result + pid;
		result = prime * result + resId;
		result = prime * result + ((tp1 == null) ? 0 : tp1.hashCode());
		result = prime * result + tp2;
		result = prime * result + tp3;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
//		if (obj == null)
//			return false;
		if (getClass() != obj.getClass())
			return false;
		FileBean other = (FileBean) obj;
		if (fileCurrent != other.fileCurrent)
			return false;
		if (fileLength != other.fileLength)
			return false;
		if (fileNm == null) {
			if (other.fileNm != null)
				return false;
		} else if (!fileNm.equals(other.fileNm))
			return false;
		if (fileType == null) {
			if (other.fileType != null)
				return false;
		} else if (!fileType.equals(other.fileType))
			return false;
		if (fsize == null) {
			if (other.fsize != null)
				return false;
		} else if (!fsize.equals(other.fsize))
			return false;
		if (ftime == null) {
			if (other.ftime != null)
				return false;
		} else if (!ftime.equals(other.ftime))
			return false;
		if (id != other.id)
			return false;
		if (imageType == null) {
			if (other.imageType != null)
				return false;
		} else if (!imageType.equals(other.imageType))
			return false;
		if (isFile != other.isFile)
			return false;
		if (isImage != other.isImage)
			return false;
		if (isSelected != other.isSelected)
			return false;
		if (isShowEditor != other.isShowEditor)
			return false;
		if (isWanCheng != other.isWanCheng)
			return false;
		if (memberId != other.memberId)
			return false;
		if (model != other.model)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		if (pid != other.pid)
			return false;
		if (resId != other.resId)
			return false;
		if (tp1 == null) {
			if (other.tp1 != null)
				return false;
		} else if (!tp1.equals(other.tp1))
			return false;
		if (tp2 != other.tp2)
			return false;
		if (tp3 != other.tp3)
			return false;
		return true;
	}
	
	
	
	

}
