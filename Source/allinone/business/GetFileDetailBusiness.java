/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.business;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;

import tools.CacheFileInfo;
import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.AIOConstants;
import allinone.data.FileEntity;
import allinone.data.ResponseCode;
import allinone.protocol.messages.GetFileDetailRequest;
import allinone.protocol.messages.GetFileDetailResponse;

/**
 * 
 * @author mcb
 */
public class GetFileDetailBusiness extends AbstractBusiness {

	private static final org.slf4j.Logger mLog = LoggerContext.getLoggerFactory().getLogger(GetFileDetailBusiness.class);

	public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
		mLog.debug("get file detail");
		MessageFactory msgFactory = aSession.getMessageFactory();
		GetFileDetailResponse resFile = (GetFileDetailResponse) msgFactory.getResponseMessage(aReqMsg.getID());

		try {
			GetFileDetailRequest rqAlb = (GetFileDetailRequest) aReqMsg;
			CacheFileInfo cache = new CacheFileInfo();
			FileEntity fileEntity = cache.getFileDetail(rqAlb.fileId);
			if (fileEntity != null) {
				resFile.mCode = ResponseCode.SUCCESS;
				if (fileEntity.getContent() == null) {
					String fileName = fileEntity.getLocation() + fileEntity.getFileName() + AIOConstants.DEFAUL_NORMAL_BASE64_FILE;
					File fl = new File(fileName);
					Scanner scn = new Scanner(fl);
					String imgbase64 = scn.useDelimiter("\\Z").next();
					fileEntity.setContent(imgbase64);
					cache.updateCacheFileDetail(fileEntity);
					scn.close();
				}
				StringBuilder sb = new StringBuilder();
				sb.append(fileEntity.getRateEntity().getViewCount()).append(AIOConstants.SEPERATOR_BYTE_1);
				sb.append(fileEntity.getRateEntity().getLikeCount()).append(AIOConstants.SEPERATOR_BYTE_1);
				sb.append(fileEntity.getRateEntity().getCommentCount()).append(AIOConstants.SEPERATOR_BYTE_1);
				sb.append(fileEntity.getContent());

				resFile.value = sb.toString();
			} else {
				resFile.mCode = ResponseCode.FAILURE;
				resFile.errMsg = "Ảnh không tồn tại";
			}

		} catch (IOException ex) {
			mLog.error(ex.getMessage(), ex);
			resFile.mCode = ResponseCode.FAILURE;
			// resalbum.errMsg = "";
		} catch (Throwable t) {
			resFile.mCode = ResponseCode.FAILURE;
		}// }
			// catch(BusinessException ex)
			// {
			// mLog.warn(ex.getMessage());
			// resalbum.setFailure(ex.getMessage());
			//
			// }
		finally {
			if (resFile != null) {
				try {
					aSession.write(resFile);
				} catch (ServerException ex) {
					mLog.error(ex.getMessage(), ex);
				}

			}
		}
		return 1;
	}

	@SuppressWarnings("unused")
	private String getImage() throws Throwable {
		BufferedImage img = ImageIO.read(new File("IMG_8509.jpg"));
		return convertImageBase64(img);
	}

	private String convertImageBase64(BufferedImage imageBuf) throws IOException {
		ByteArrayOutputStream f = new ByteArrayOutputStream();
		ImageIO.write(imageBuf, "jpg", f);
		byte[] arrByte = f.toByteArray();
		return new String(com.sun.midp.io.Base64.encode(arrByte, 0, arrByte.length));

	}

	public BufferedImage resizeImage(BufferedImage originalImage, int type, int width, int height) {
		BufferedImage resizedImage = new BufferedImage(width, height, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, width, height, null);
		g.dispose();
		return resizedImage;
	}
}
