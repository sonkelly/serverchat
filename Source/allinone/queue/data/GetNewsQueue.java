/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.queue.data;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.axis.utils.ByteArrayOutputStream;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.core.Stream2BufferedImage;
import org.im4java.process.Pipe;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;

import tools.CacheAlbumInfo;
import tools.CacheNewsInfo;
import vn.game.common.LoggerContext;
import vn.game.common.ServerException;
import vn.game.protocol.MessageFactory;
import vn.game.session.ISession;
import allinone.data.AIOConstants;
import allinone.data.HTTPPoster;
import allinone.data.ImageUrlEntity;
import allinone.data.Messages;
import allinone.data.MessagesID;
import allinone.data.NewsCategoryEntity;
import allinone.data.NewsDetailEntity;
import allinone.data.QueueNewsEntity;
import allinone.data.ResponseCode;
import allinone.data.TruyenCuoiTinTucBoiToan;
import allinone.data.UploadAvatarEntity;
import allinone.databaseDriven.FileDB;
import allinone.protocol.messages.BoiRequest;
import allinone.protocol.messages.BoiResponse;
import allinone.protocol.messages.GetCategoryDetailResponse;
import allinone.protocol.messages.GetNewsDetailResponse;
import allinone.protocol.messages.SendImageURLResponse;
import allinone.protocol.messages.UploadAvatarResponse;



/**
 *
 * @author mcb
 */
public class GetNewsQueue implements Job{
//    private static Queue imgQueue = new ArrayDeque();
    private static ConcurrentHashMap<UUID, QueueNewsEntity> imgQueue = new ConcurrentHashMap<UUID, QueueNewsEntity>();
    
    private static Logger mLog = LoggerContext.getLoggerFactory()
			.getLogger(GetNewsQueue.class);
    
    private static boolean isInProgress = false;
    private static final int MAX_LENGTH = 400;
    private static final int MAX_LEN_IMAGE = 4500000;
    
//    private static ConvertCmd cmd;
    
    
//    private static final Object lockObj = new Object();
//    static{
//        init();
//    }
//            
//    private static void init()
//    {
////        String imPath="C:\\Program Files\\ImageMagick-6.8.1-Q16";
//        cmd = new ConvertCmd();    
//        cmd.setSearchPath(AIOConstants.IMG_PATH_PROCESSING);
//    }
    
    public  void insertNews(QueueNewsEntity entity)
    {
        try
        {
//            imgQueue.add(entity);
            UUID uuid = UUID.randomUUID();
            imgQueue.put(uuid, entity);
        }
        catch(Exception ex)
        {
            mLog.error(ex.getMessage(), ex);
        }
    }
    
    
    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        
        if(!isInProgress)
        {
//            log.debug("begin execute send image to client job");
            isInProgress = true;
            
            try
            {
                
                CacheNewsInfo cache = new CacheNewsInfo();
                 Enumeration<UUID> e = imgQueue.keys();
                 ConvertCmd cmd=null;
                while(e.hasMoreElements())
                {
                    try
                    {
                        QueueNewsEntity queryEntity = null;
                        UUID key = e.nextElement();
                        
                        queryEntity = imgQueue.get(key);    
                        imgQueue.remove(key);
                        
                        if(queryEntity == null)
                                continue;
//                        synchronized(lockObj)
//                        {
//                            queryEntity= (QueueNewsEntity)imgQueue.remove();
//                        }
                        
                        if(queryEntity.getSession() == null)
                            continue;
                        
                        Object queryObj= queryEntity.getNewsInfo();
                        if(queryObj instanceof NewsCategoryEntity)
                        {
                            NewsCategoryEntity catEntity = (NewsCategoryEntity)queryObj;
                            String value = cache.getCategoryDetail(catEntity, catEntity.getQueryPage());
                            GetCategoryDetailResponse res = (GetCategoryDetailResponse)queryEntity.getResponse();
                            if(value != null)
                            {
                                res.mCode = ResponseCode.SUCCESS;
                                res.value = value;
                            }
                            else
                            {
                                res.mCode = ResponseCode.FAILURE;
                                res.errMsg = Messages.NOT_FOUND_RESOURCE;
                            }
                            
                        } else if(queryObj instanceof NewsDetailEntity)
                        {
                            NewsDetailEntity inputEntity = (NewsDetailEntity)queryObj;
                            NewsDetailEntity gotEntity = cache.getNewsDetail(inputEntity.getNewsId(), inputEntity.getCategoryId());
                            GetNewsDetailResponse res = (GetNewsDetailResponse)queryEntity.getResponse();
                            if(gotEntity != null)
                            {
                                res.mCode = ResponseCode.SUCCESS;
                                StringBuilder sb = new StringBuilder();
                                sb.append(Long.toString(inputEntity.getNewsId())).append(AIOConstants.SEPERATOR_BYTE_1);
                                sb.append(gotEntity.getContent());
                                res.value = sb.toString();
                                
                                if(gotEntity.getImgUrl() != null && !gotEntity.getImgUrl().equals(""))
                                {
                                    MessageFactory msgFactory = queryEntity.getSession().getMessageFactory();
                                    SendImageURLResponse queueImgUrl = (SendImageURLResponse) msgFactory.getResponseMessage(MessagesID.SEND_IMAGE_URL);
                                    
                                    ImageUrlEntity imgEntity = new ImageUrlEntity(gotEntity.getImgUrl(),
                                            inputEntity.getNewsId());
                                    imgEntity.setCategoryId(gotEntity.getCategoryId());
                                    QueueNewsEntity queueImgEntity = new QueueNewsEntity(imgEntity, queryEntity.getSession(), queueImgUrl);
                                    GetNewsQueue queue = new GetNewsQueue();
                                    queue.insertNews(queueImgEntity);
                                }
                            }
                            else
                            {
                                res.mCode = ResponseCode.FAILURE;
                                res.errMsg = Messages.NOT_FOUND_RESOURCE;
                            }
                        }
                        else if(queryObj instanceof ImageUrlEntity)
                        {
                            ImageUrlEntity inputEntity = (ImageUrlEntity)queryObj;
                            
                            SendImageURLResponse res = (SendImageURLResponse)queryEntity.getResponse();
                            String value = cache.getImageUrlDetail(inputEntity.getNewsId(), inputEntity.getImgUrl(), inputEntity.getCategoryId());
                            if(value != null)
                            {
                                res.mCode = ResponseCode.SUCCESS;
                                
                                res.value = value;
                                
                            }
                            else
                            {
                                res.mCode = ResponseCode.FAILURE;
                                res.mErrorMsg = Messages.NOT_FOUND_RESOURCE;
                            }
                            
                        }
                        else if(queryObj instanceof BoiRequest)
                        {
                            BoiRequest rq = (BoiRequest) queryObj;
                            
                            BoiResponse resAn = (BoiResponse) queryEntity.getResponse();
                            
                            HTTPPoster p = new HTTPPoster();
                            TruyenCuoiTinTucBoiToan data = new TruyenCuoiTinTucBoiToan(p);
                            data.setUpLink(rq.data);
                            String res = "";
                            boolean isWrongService = false;
                            
                            switch (rq.code) { // @ TODO: Need change boi's code
                                
                                case 2: // Boi TimBanNam
                                    res = data.timBanNamChoBanNu();
                                    break;
                                case 3: // Boi TimBanNam
                                    res = data.timBanNuChoBanNam();
                                    break;
                                
                                case 5: // Boi ngay sihn va tinh cach
                                    res = data.ngaySinhVaTinhCach();
                                    break;
                                case 6: // tu vi tron doi Nu
                                    res = data.tuViTronDoiNu();
                                    break;
                                case 7: // tu vi tron doi Nam
                                    res = data.tuViTronDoiNam();
                                    break;
                                
                                case 15: // so Dien Thoai
                                    res = data.boiSoDienThoai();
                                    break;
                                default: {// Boi dang di
                                    isWrongService = true;
                                    resAn.setFailure("Mã bói không họp lệ");
                                }
                            }
                            if(!isWrongService)
                            {
                                resAn.setSuccess(res);

                            }
                            
                        }
                        
                        else if(queryObj instanceof UploadAvatarEntity)
                        {
                            if(cmd == null)
                            {
                                cmd = new ConvertCmd();    
                                cmd.setSearchPath(AIOConstants.IMG_PATH_PROCESSING);
                            }
                            
                            processUploadAvatar(cmd, queryEntity);
                            
                        }
                        
                        
                        
                        queryEntity.getSession().write(queryEntity.getResponse() );
                        
                    }
                    catch (ServerException ex) {
                        mLog.error(ex.getMessage(), ex);
                    }                    
                    
                    catch(Exception ex)
                    {
                        mLog.error(ex.getMessage(), ex);
                    }   
                        
                }
            }
            catch(Exception ex)
            {
                mLog.error(ex.getMessage(), ex);
            }
//             log.debug("end execute send image to client job");
        }
        isInProgress = false;

        
        
            
       
            
    }
    
    private String saveToFile(ConvertCmd cmd, byte[] arr, int size, String fileName, boolean isIcon, String minimumContent) throws IOException, InterruptedException, IM4JavaException
    {
//        double quality = 1;
        int count = 0;
        String content = "";
        while(count<3)
        {
            ByteArrayOutputStream fSmall = new ByteArrayOutputStream();
            Pipe foSmall = new Pipe(null, fSmall);
    //            ConvertCmd cmd1 = new ConvertCmd();
            cmd.setOutputConsumer(foSmall);
            InputStream inSmall = new ByteArrayInputStream(arr);
            Pipe pipeInSmall  = new Pipe(inSmall,null);
            cmd.setInputProvider(pipeInSmall);

            IMOperation frame = new IMOperation();
            
            
            
            IMOperation opSmall = new IMOperation();
            opSmall.addImage("-");  
            
            
            
            if(count> 0)
            {
                if(count == 1 && size > MAX_LENGTH)
                {
                    size = MAX_LENGTH;
                    frame.resize(size, size);
                }
                else
                {
                    if(size>100)
                        size -=80;
                    frame.thumbnail(size, size);
                }
//                quality /=2;
//                IMOperation qualityOper = new IMOperation();
//                qualityOper.quality(quality);
//                opSmall.addSubOperation(frame);
            }
            else
            {
                if(isIcon)
                    frame.thumbnail(size, size);
                else
                    frame.resize(size, size);
                
                
            }
            opSmall.addSubOperation(frame);
            
            opSmall.addImage("jpg:-");  

            cmd.run(opSmall);

            byte[] imgValue = fSmall.toByteArray();

            inSmall.close();
            fSmall.close();
            content = new String(com.sun.midp.io.Base64.encode(imgValue, 0, imgValue.length));
            if(content.length()< MAX_LEN_IMAGE)
            {
                break;
            }
            count++;
        }
        if(!isIcon && content.length()> MAX_LEN_IMAGE)
        {
            content = minimumContent;
        }
        
        File normalFile = new File(AIOConstants.DEFAULT_UPLOAD_URL + fileName);
        Writer output = null;
        output = new BufferedWriter(new FileWriter(normalFile));
        output.write(content);
        output.close();
        return content;    
    }
    
    private void processUploadAvatar(ConvertCmd cmd, QueueNewsEntity queryEntity )
    {
        Object queryObj= queryEntity.getNewsInfo();
            
        UploadAvatarEntity entity = (UploadAvatarEntity)queryObj;

        UploadAvatarResponse resUpload = (UploadAvatarResponse)queryEntity.getResponse();
        try {
            
            ISession aSession = queryEntity.getSession();
            StringBuilder sb = new StringBuilder();
            sb.append(entity.getMaxParts() -1);
            byte[] compressByte = entity.getRawData();
            
            IMOperation op = new IMOperation();
            op.addImage("-");  
            op.addImage("jpg:-");               // write to stdout in tif-format
            InputStream in = new ByteArrayInputStream(compressByte);
            Pipe pipeIn  = new Pipe(in,null);
            
            
            Stream2BufferedImage s2b = new Stream2BufferedImage();
//            Pipe fos = new Pipe(null, s2b);
            cmd.setOutputConsumer(s2b);
            cmd.setInputProvider(pipeIn);
            
            cmd.run(op);
          
            in.close();
            
            
//            InputStream in = new ByteArrayInputStream(compressByte);
//            Utils util = new Utils();
            BufferedImage bImageFromConvert = s2b.getImage();;
           
            int maxLength = bImageFromConvert.getHeight();
            if(maxLength< bImageFromConvert.getWidth())
            {
                maxLength= bImageFromConvert.getWidth();
            }
            
//            int maxDimession = MAX_LENGTH;
//            if(maxLength< MAX_LENGTH)
//            {
//                maxDimession = maxLength;
//            }
            
            

    //                aSession.setAvatarNull();

            String guid = UUID.randomUUID().toString(); 
            String icon = guid + AIOConstants.DEFAUL_ICON_BASE64_SUFFIX;
            String icon80 = guid + AIOConstants.DEFAUL_ICON80_BASE64_SUFFIX;
            String normal = guid + AIOConstants.DEFAUL_NORMAL_BASE64_FILE;


            
            saveToFile(cmd, compressByte, 40, icon, true, null);
            String minimumContent = saveToFile(cmd, compressByte, 80, icon80, true, null);
            saveToFile(cmd, compressByte, maxLength, normal, false, minimumContent);
            

            //save to DB
            FileDB db = new FileDB();
            resUpload.fileId =  db.insertFile(guid, aSession.getUID(), entity.getAlbumId(), AIOConstants.DEFAULT_UPLOAD_URL);

            if(entity.getAlbumId() >0)
            {
                CacheAlbumInfo album = new CacheAlbumInfo();
                album.deleteCacheAlbum(entity.getAlbumId());
                album.deleteCacheAlbums(aSession.getUID());
            }
            
            sb.append(AIOConstants.SEPERATOR_BYTE_1).append(resUpload.fileId);
            resUpload.setSuccess(sb.toString());
             mLog.debug("upload successful");
             aSession.setUploadAvatar(false);
            
//            aSession.write(resUpload);
        } catch (InterruptedException ex) {
            mLog.error(ex.getMessage(), ex);
            resUpload.setFailure(ResponseCode.FAILURE, "Anh upload bi loi");
        } catch (IM4JavaException ex) {
            mLog.error(ex.getMessage(), ex);
            resUpload.setFailure(ResponseCode.FAILURE, "Anh upload bi loi");
        }  catch (SQLException ex) {
            mLog.error(ex.getMessage(), ex);
            resUpload.setFailure(ResponseCode.FAILURE, "Khong the ket noi den co so du lieu");
        } catch (IOException ex) {
            mLog.error(ex.getMessage(), ex);
            resUpload.setFailure(ResponseCode.FAILURE, "Anh upload bi loi");
        }

     
    }
    
}
