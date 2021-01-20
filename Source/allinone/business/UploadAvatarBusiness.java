package allinone.business;

import vn.game.protocol.MessageFactory;
import com.sun.midp.io.Base64;
import org.slf4j.Logger;
import vn.game.session.ISession;
import allinone.data.QueueNewsEntity;
import allinone.data.ResponseCode;
import allinone.data.UploadAvatarEntity;
import allinone.databaseDriven.AvatarsDB;
import allinone.protocol.messages.UploadAvatarRequest;
import allinone.protocol.messages.UploadAvatarResponse;
import allinone.queue.data.GetNewsQueue;
import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.AbstractBusiness;
import vn.game.protocol.IRequestMessage;
import vn.game.protocol.IResponsePackage;

public class UploadAvatarBusiness
        extends AbstractBusiness {

    private static final Logger mLog = LoggerContext.getLoggerFactory().getLogger(UploadAvatarBusiness.class);

    public UploadAvatarBusiness() {
    }

    @Override
    public int handleMessage(ISession aSession, IRequestMessage aReqMsg, IResponsePackage aResPkg) {
        mLog.debug("[Get Category] : Catch");
        MessageFactory msgFactory = aSession.getMessageFactory();
        UploadAvatarResponse resUpload
                = (UploadAvatarResponse) msgFactory.getResponseMessage(aReqMsg.getID());
        UploadAvatarRequest rqImg = (UploadAvatarRequest) aReqMsg;
        try {
            String imageId = String.valueOf(rqImg.imageId);
            if (!imageId.equals("")) {
                AvatarsDB aObj = new AvatarsDB();
                aObj.updateAvatar(aSession.getUID(), imageId);
                resUpload.setSuccess("Cập nhật avatar thành công");
                aResPkg.addMessage(resUpload);
            } else {
                resUpload.setFailure(ResponseCode.FAILURE, "Lỗi dữ liệu");
            }
            
//            aSession.setUploadBytePart(null);
//            aSession.setUploadAvatar(true);
//            if (rqRaoVat.maxParts > 0) {
//
//                UploadAvatarEntity entity = new UploadAvatarEntity();
//                entity.setAlbumId(rqRaoVat.albumId);
//                entity.setImageId(rqRaoVat.imageId);
//                entity.setMaxParts(rqRaoVat.maxParts);
//                aSession.setUploadAvatarEntity(entity);
//                resUpload.setSuccess("");
//                aResPkg.addMessage(resUpload);
//                return 1;
//            }
//
//            UploadAvatarEntity entity = aSession.getUploadAvatarEntity();
//            if (entity == null) {
//                throw new BusinessException("Co loi xay ra ban thu upload lai xem sao");
//            }
//            byte[] arrPart = Base64.decode(rqRaoVat.detail);
//
//            entity.appendRawData(arrPart);
//
//            StringBuilder sb = new StringBuilder();
//            sb.append(rqRaoVat.sequence);
//            if (rqRaoVat.sequence == entity.getMaxParts() - 1) {
//
//                QueueNewsEntity queueEntity = new QueueNewsEntity(entity, aSession, resUpload);
//                GetNewsQueue queue = new GetNewsQueue();
//                queue.insertNews(queueEntity);
//
//            } else {
//
//                resUpload.setSuccess(sb.toString());
//                aResPkg.addMessage(resUpload);
//            }
        } catch (Exception e) {
            mLog.error(e.getMessage(), e);

            resUpload.setFailure(ResponseCode.FAILURE, "Không thể kết nối dữ liệu");
            if (resUpload != null) {
                aResPkg.addMessage(resUpload);
            }
        }

        return 1;
    }

    private static final int MAX_LENGTH = 600;

}
