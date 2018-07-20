package com.chewuwuyou.rong.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;

import com.chewuwuyou.app.AppContext;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.location.RealTimeLocationConstant;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.ImageMessage;
import io.rong.message.RecallNotificationMessage;

/**
 * Created by xxy on 2016/9/7 0007.
 */
public class RongApi {
    private RongApi() {
    }

    /**
     * 建立与融云服务器的连接
     *
     * @param token
     */
    public static void connect(Context context, String token, RongIMClient.ConnectCallback connectCallback) {
        if (context.getApplicationInfo().packageName.equals(AppContext.getCurProcessName(context.getApplicationContext())))//判断是否同进程
            RongIMClient.connect(token, connectCallback);
    }

    public static void sendMessage(Message message, String pushContent, String pushData, IRongCallback.ISendMessageCallback callback) {
        RongIMClient.getInstance().sendMessage(message, pushContent, pushData, callback);
    }

    /**
     * 发送普通消息
     *
     * @param conversationType    会话类型
     * @param targetId            会话ID
     * @param content             消息的内容，一般是MessageContent的子类对象
     * @param pushContent         接收方离线时需要显示的push消息内容
     * @param pushData            接收方离线时需要在push消息中携带的非显示内容
     * @param sendMessageCallback 发送消息的回调
     * @param resultCallback      消息存库的回调，可用于获取消息实体
     */
    public static void sendMsg(Conversation.ConversationType conversationType, String targetId, MessageContent content, String pushContent, String pushData, RongIMClient.SendMessageCallback sendMessageCallback, RongIMClient.ResultCallback<Message> resultCallback) {
        RongIMClient.getInstance().sendMessage(conversationType, targetId, content, pushContent, pushData, sendMessageCallback, resultCallback);
    }

    /**
     * 发送图片消息。
     *
     * @param conversationType         会话类型。
     * @param targetId                 会话目标 Id。根据不同的 conversationType，可能是用户 Id、讨论组 Id、群组 Id 或聊天室 Id。
     * @param imgMsg                   消息内容。
     * @param pushContent              接收方离线时需要显示的push消息内容。
     * @param pushData                 接收方离线时需要在push消息中携带的非显示内容。
     * @param SendImageMessageCallback 发送消息的回调。
     */
    public static void sendPicMsg(Context context, Conversation.ConversationType conversationType, String targetId, ImageMessage imgMsg, String pushContent, String pushData, RongIMClient.SendImageMessageCallback SendImageMessageCallback) {
        //发送图片消息
        File imageFileSource = new File(context.getCacheDir(), "source.jpg");
        File imageFileThumb = new File(context.getCacheDir(), "thumb.jpg");
        try {
            // 读取图片。
            InputStream is = context.getAssets().open("emmy.jpg");
            Bitmap bmpSource = BitmapFactory.decodeStream(is);
            imageFileSource.createNewFile();
            FileOutputStream fosSource = new FileOutputStream(imageFileSource);
            // 保存原图。
            bmpSource.compress(Bitmap.CompressFormat.JPEG, 100, fosSource);
            // 创建缩略图变换矩阵。
            Matrix m = new Matrix();
            m.setRectToRect(new RectF(0, 0, bmpSource.getWidth(), bmpSource.getHeight()), new RectF(0, 0, 160, 160), Matrix.ScaleToFit.CENTER);
            // 生成缩略图。
            Bitmap bmpThumb = Bitmap.createBitmap(bmpSource, 0, 0, bmpSource.getWidth(), bmpSource.getHeight(), m, true);
            imageFileThumb.createNewFile();
            FileOutputStream fosThumb = new FileOutputStream(imageFileThumb);
            // 保存缩略图。
            bmpThumb.compress(Bitmap.CompressFormat.JPEG, 60, fosThumb);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ImageMessage imgMsgg = ImageMessage.obtain(Uri.fromFile(imageFileThumb), Uri.fromFile(imageFileSource));
        RongIMClient.getInstance().sendImageMessage(conversationType, targetId, imgMsgg, pushContent, pushData, SendImageMessageCallback);
    }

    /**
     * <p>发送多媒体消息</p>
     * <p>发送前构造 {@link Message} 消息实体，目前仅支持 FileMessage</p>
     * 1. 注册文件消息
     * registerMessageType(FileMessage.class);
     * 2. 构造文件消息
     * <p/>
     * 生成 FileMessage 对象。
     * <p/>
     * 文件的本地地址，必须以file://开头。
     * public static FileMessage obtain(Uri localUrl)
     * <p/>
     * 如： Uri filePath = Uri.parse("file://" + fileInfo.getFilePath());
     * FileMessage fileMessage = FileMessage.obtain(filePath);
     * SDK 默认将文件上传到七牛服务器，发送成功的回调参数 Message 里会携带服务器的存储地址，您可以通过 FileMessage.getFileUrl() 获得。
     *
     * @param message     发送消息的实体。
     * @param pushContent 当下发 push 消息时，在通知栏里会显示这个字段。
     *                    如果发送的是自定义消息，该字段必须填写，否则无法收到 push 消息。
     *                    如果发送 sdk 中默认的消息类型，例如 RC:TxtMsg, RC:VcMsg, RC:ImgMsg，则不需要填写，默认已经指定。
     * @param pushData    push 附加信息。如果设置该字段，用户在收到 push 消息时，能通过 {@link io.rong.push.notification.PushNotificationMessage#getPushData()} 方法获取。
     * @param callback    发送消息的回调 {@link io.rong.imlib.RongIMClient.SendMediaMessageCallback}。
     */
    public static void sendMediaMessage(Message message, String pushContent, String pushData, IRongCallback.ISendMediaMessageCallback callback) {
        RongIMClient.getInstance().sendMediaMessage(message, pushContent, pushData, callback);
    }

    /**
     * 下载多媒体消息。
     * 可以调用 cancelDownloadMediaMessage 实现取消下载中的媒体消息，取消下载成功后，通过 IDownloadMediaMessageCallback 中的 onCanceled(Message) 方法通知用户。
     * 4. 自定义文件保存位置
     * <p/>
     * 下载文件消息，即调用 downloadMediaMessage 后，该文件默认保存在 SD 卡的 /RongCloud/Media/ 下。
     * <p/>
     * 您可以通过更改 SDK 的 res/values/rc_configuration.xml 里面的 rc_media_message_default_save_path 的值，来自定义文件的存储路径。
     *
     * @param message  文件消息。
     * @param callback 下载文件的回调。
     */
    public static void downloadMediaMessage(Message message, IRongCallback.IDownloadMediaMessageCallback callback) {
        RongIMClient.getInstance().downloadMediaMessage(message, callback);
    }

    /**
     * 删除指定的一条或者一组消息，回调获取删除是否成功。
     *
     * @param messageIds 要删除的消息 Id 数组。
     * @param callback   是否删除成功的回调。
     */
    public static void deleteMessages(int[] messageIds, RongIMClient.ResultCallback<Boolean> callback) {
        RongIMClient.getInstance().deleteMessages(messageIds, callback);
    }


    /**
     * 根据会话类型，清空某一会话的所有聊天消息记录，回调方式获取清空是否成功。
     *
     * @param conversationType 会话类型。不支持传入 ConversationType.CHATROOM。
     * @param targetId         目标 Id。根据不同的 conversationType，可能是用户 Id、讨论组 Id、群组 Id。
     * @param callback         清空是否成功的回调。
     */
    public static void clearMessages(Conversation.ConversationType conversationType, String targetId, RongIMClient.ResultCallback<Boolean> callback) {
        RongIMClient.getInstance().clearMessages(conversationType, targetId, callback);
    }

    /**
     * 根据消息 Id，设置接收到的消息状态，回调方式获取设置是否成功。
     *
     * @param messageId      消息 Id。
     * @param receivedStatus 接收到的消息状态。
     * @param callback       是否设置成功的回调。
     */
    public static void setMessageReceivedStatus(int messageId, Message.ReceivedStatus receivedStatus, RongIMClient.ResultCallback<Boolean> callback) {
        RongIMClient.getInstance().setMessageReceivedStatus(messageId, receivedStatus, callback);
    }

    /**
     * 根据消息 Id，设置发送的消息状态，回调方式获取设置是否成功。
     *
     * @param messageId  消息 Id。
     * @param sentStatus 发送的消息状态。
     * @param callback   是否设置成功的回调。
     */
    public static void setMessageSentStatus(final int messageId, final Message.SentStatus sentStatus, final RongIMClient.ResultCallback<Boolean> callback) {
        RongIMClient.getInstance().setMessageSentStatus(messageId, sentStatus, callback);
    }

    /**
     * 取消多媒体消息下载。
     * 取消下载成功后，downloadMediaMessage 中的 IDownloadMediaMessageCallback#onCanceled(Message) 方法被回调。
     *
     * @param message  包含多媒体文件的消息。
     * @param callback 取消下载多媒体文件时的回调。
     */
    public static void cancelDownloadMediaMessage(Message message, RongIMClient.OperationCallback callback) {
        RongIMClient.getInstance().cancelDownloadMediaMessage(message, callback);
    }

    /**
     * 模拟消息，向本地目标 Id 中插入一条消息。
     *
     * @param type         会话类型。
     * @param targetId     目标 Id。根据不同的 conversationType，可能是用户 Id、讨论组 Id、群组 Id 或聊天室 Id。
     * @param senderUserId 发送用户 Id。
     * @param content      消息内容。
     * @param callback     获得消息发送实体的回调。
     */
    public static void insertMessage(Conversation.ConversationType type, String targetId, String senderUserId, MessageContent content, RongIMClient.ResultCallback<Message> callback) {
        RongIMClient.getInstance().insertMessage(type, targetId, senderUserId, content, callback);
    }

    /**
     * 根据会话类型的目标 Id，回调方式获取最新的 N 条消息实体。
     *
     * @param conversationType 会话类型。
     * @param targetId         目标 Id。根据不同的 conversationType，可能是用户 Id、讨论组 Id、群组 Id 或聊天室 Id。
     * @param count            要获取的消息数量。
     * @param callback         获取最新消息记录的回调，按照时间顺序从新到旧排列。
     */
    public static void getLatestMessages(Conversation.ConversationType conversationType, String targetId, int count, RongIMClient.ResultCallback<List<Message>> callback) {
        RongIMClient.getInstance().getLatestMessages(conversationType, targetId, count, callback);
    }

    /**
     * 获取会话中，从指定消息之前、指定数量的最新消息实体
     *
     * @param conversationType 会话类型。不支持传入 ConversationType.CHATROOM。
     * @param targetId         目标 Id。根据不同的 conversationType，可能是用户 Id、讨论组 Id、群组 Id。
     * @param oldestMessageId  最后一条消息的 Id，获取此消息之前的 count 条消息，没有消息第一次调用应设置为:-1。
     * @param count            要获取的消息数量。
     * @param callback         获取历史消息记录的回调，按照时间顺序从新到旧排列。
     */
    public static void getHistoryMessages(Conversation.ConversationType conversationType, String targetId, int oldestMessageId, int count, RongIMClient.ResultCallback<List<Message>> callback) {
        RongIMClient.getInstance().getHistoryMessages(conversationType, targetId, oldestMessageId, count, callback);
    }

    /**
     * 获取会话中，从指定消息之前、指定数量的、指定消息类型的最新消息实体
     *
     * @param conversationType 会话类型。不支持传入 ConversationType.CHATROOM。
     * @param targetId         目标 Id。根据不同的 conversationType，可能是用户 Id、讨论组 Id、群组 Id。
     * @param objectName       消息类型标识。
     * @param oldestMessageId  最后一条消息的 Id，获取此消息之前的 count 条消息,没有消息第一次调用应设置为:-1。
     * @param count            要获取的消息数量
     * @param callback         获取历史消息记录的回调，按照时间顺序从新到旧排列。
     */
    public static void getHistoryMessages(Conversation.ConversationType conversationType, String targetId, String objectName, int oldestMessageId, int count, RongIMClient.ResultCallback<List<Message>> callback) {
        RongIMClient.getInstance().getHistoryMessages(conversationType, targetId, objectName, oldestMessageId, count, callback);
    }

    /**
     * 读取远程服务器的消息- 根据会话类型的目标 Id，回调方式获取某消息类型标识的N条历史消息记录。（需要先开通历史消息漫游功能）。
     *
     * @param conversationType 会话类型。不支持传入 ConversationType.CHATROOM。
     * @param targetId         目标 Id。根据不同的 conversationType，可能是用户 Id、讨论组 Id、群组 Id 或聊天室 Id。
     * @param dateTime         从该时间点开始获取消息。即：消息中的 sentTime；第一次可传 0，获取最新 count 条。
     * @param count            要获取的消息数量，最多 20 条。
     * @param callback         获取历史消息记录的回调，按照时间顺序从新到旧排列。
     */
    public static void getRemoteHistoryMessages(Conversation.ConversationType conversationType, String targetId, long dateTime, int count, RongIMClient.ResultCallback<List<Message>> callback) {
        RongIMClient.getInstance().getRemoteHistoryMessages(conversationType, targetId, dateTime, count, callback);
    }

    /**
     * 获取 RealTimeLocation 实例，每发起一次位置共享业务，就要获取一个实例。
     * 如果获取实例失败，返回 error code，对应具体的失败信息。
     * 使用时，每次进入会话，获取该会话对应的实例，以此判断位置共享业务是否可用或者正在进行中。
     * 如果返回成功，使用者可以设置监听，发起位置共享。
     * 如果返回正在进行中，则是对方已发起位置共享，使用者可以设置监听，加入。
     * 如果返回其他失败信息，使用者可以据此做出相应的提示。
     *
     * @param conversationType 发起位置共享的所在会话的会话类型。
     * @param targetId         发起位置共享的 target id。
     * @return 是否获取实例成功。
     */
    public static RealTimeLocationConstant.RealTimeLocationErrorCode getRealTimeLocation(Conversation.ConversationType conversationType, String targetId) {
        return RongIMClient.getInstance().getRealTimeLocation(conversationType, targetId);
    }

    /**
     * 退出位置共享。
     *
     * @param conversationType 位置共享的会话类型。
     * @param targetId         位置共享的 targetId。
     */
    public static void quitRealTimeLocation(Conversation.ConversationType conversationType, String targetId) {
        RongIMClient.getInstance().quitRealTimeLocation(conversationType, targetId);
    }

    /**
     * 获取参与该位置共享的所有成员。
     *
     * @param conversationType 位置共享的会话类型。
     * @param targetId         位置共享的 targetId。
     * @return 参与成员 id 列表。
     */
    public static List<String> getRealTimeLocationParticipants(Conversation.ConversationType conversationType, String targetId) {
        return RongIMClient.getInstance().getRealTimeLocationParticipants(conversationType, targetId);
    }

    /**
     * 添加位置共享观察者。
     *
     * @param conversationType 位置共享的会话类型。
     * @param targetId         位置共享的 targetId。
     * @param listener         位置共享监听。
     */
    public static void addRealTimeLocationListener(Conversation.ConversationType conversationType, String targetId, RongIMClient.RealTimeLocationListener listener) {
        RongIMClient.getInstance().addRealTimeLocationListener(conversationType, targetId, listener);
    }

    /**
     * 使用者调用此方法更新坐标位置。
     *
     * @param conversationType 位置共享的会话类型。
     * @param targetId         位置共享的会话 targetId。
     * @param latitude         维度
     * @param longitude        经度
     */
    public static void updateRealTimeLocationStatus(Conversation.ConversationType conversationType, String targetId, double latitude, double longitude) {
        RongIMClient.getInstance().updateRealTimeLocationStatus(conversationType, targetId, latitude, longitude);
    }

    /**
     * 获取位置共享状态。
     *
     * @param conversationType 位置共享的会话类型。
     * @param targetId         位置共享的 targetId。
     * @return 正在进行的位置共享状态。
     */
    public static RealTimeLocationConstant.RealTimeLocationStatus getRealTimeLocationCurrentState(Conversation.ConversationType conversationType, String targetId) {
        return RongIMClient.getInstance().getRealTimeLocationCurrentState(conversationType, targetId);
    }

    /**
     * 加入位置共享。
     *
     * @param conversationType 位置共享的会话类型。
     * @param targetId         位置共享的 targetId。
     */
    public static RealTimeLocationConstant.RealTimeLocationErrorCode joinRealTimeLocation(Conversation.ConversationType conversationType, String targetId) {
        return RongIMClient.getInstance().joinRealTimeLocation(conversationType, targetId);
    }

    /**
     * 发起位置共享。
     *
     * @param conversationType 发起位置共享的会话类型。
     * @param targetId         发起位置共享的 targetId。
     */
    public static RealTimeLocationConstant.RealTimeLocationErrorCode startRealTimeLocation(Conversation.ConversationType conversationType, String targetId) {
        return RongIMClient.getInstance().startRealTimeLocation(conversationType, targetId);
    }

    /**
     * 获取未读消息数
     *
     * @param resultCallback
     */
    public static void getTotalUnreadCount(RongIMClient.ResultCallback<Integer> resultCallback) {
        RongIMClient.getInstance().getTotalUnreadCount(resultCallback);
    }

    /**
     * 获取某个会话内未读消息条数
     * conversationType 会话类型
     * targetId         会话目标ID
     */
    public static void getUnreadCount(Conversation.ConversationType conversationType, String targetId, RongIMClient.ResultCallback<Integer> resultCallback) {
        RongIMClient.getInstance().getUnreadCount(conversationType, targetId, resultCallback);
    }

    /**
     * 获取某个类型的会话中所有的未读消息条数
     * conversationType 会话类型
     */
    public static void getUnreadCount(Conversation.ConversationType conversationType, RongIMClient.ResultCallback<Integer> resultCallback) {
        RongIMClient.getInstance().getUnreadCount(resultCallback, conversationType);
    }

    /**
     * 清除某个会话中的未读消息数
     * conversationType 会话类型
     * targetId         会话目标ID
     */
    public static void clearMessagesUnreadStatus(Conversation.ConversationType conversationType, String targetId, RongIMClient.ResultCallback<Boolean> resultCallback) {
        RongIMClient.getInstance().clearMessagesUnreadStatus(conversationType, targetId, resultCallback);
    }

    /**
     * 您可以通过以下代码获取本地存储的会话列表。
     *
     * @param resultCallback
     * @param conversationTypes
     */
    public static void getConversationList(RongIMClient.ResultCallback<List<Conversation>> resultCallback, List<Conversation.ConversationType> conversationTypes) {
        RongIMClient.getInstance().getConversationList(resultCallback);
    }

    /**
     * 根据会话类型，回调方式获取会话列表。
     * 此方法会从本地数据库中，读取会话列表。
     * 返回的会话列表按照时间从前往后排列，如果有置顶的会话，则置顶的会话会排列在前面。
     *
     * @param callback          获取会话列表的回调。
     * @param conversationTypes 会话类型列表。
     */
    public static void getConversationList(RongIMClient.ResultCallback<List<Conversation>> callback, Conversation.ConversationType... conversationTypes) {
        RongIMClient.getInstance().getConversationList(callback, conversationTypes);
    }

    /**
     * 根据不同会话类型的目标 Id，回调方式获取某一会话信息。
     *
     * @param conversationType 会话类型。
     * @param targetId         目标 Id。根据不同的 conversationType，可能是用户 Id、讨论组 Id、群组 Id 或聊天室 Id。
     * @param callback         获取会话信息的回调。
     */
    public static void getConversation(Conversation.ConversationType conversationType, String targetId, RongIMClient.ResultCallback<Conversation> callback) {
        RongIMClient.getInstance().getConversation(conversationType, targetId, callback);
    }

    /**
     * 根据会话类型列表清空所有会话及会话消息，回调方式通知是否清空成功。
     *
     * @param callback          是否清空成功的回调。
     * @param conversationTypes 会话类型。
     */
    public static void clearConversations(RongIMClient.ResultCallback callback, Conversation.ConversationType... conversationTypes) {
        RongIMClient.getInstance().clearConversations(callback, conversationTypes);
    }

    /**
     * 从会话列表中移除某一会话，但是不删除会话内的消息。
     * <p/>
     * 如果此会话中有新的消息，该会话将重新在会话列表中显示，并显示最近的历史消息。
     *
     * @param conversationType 会话类型。
     * @param targetId         目标 Id。根据不同的 conversationType，可能是用户 Id、讨论组 Id、群组 Id 或聊天室 Id。
     * @param callback         移除会话是否成功的回调。
     */
    public static void removeConversation(Conversation.ConversationType conversationType, String targetId, RongIMClient.ResultCallback<Boolean> callback) {
        RongIMClient.getInstance().removeConversation(conversationType, targetId, callback);
    }

    /**
     * 设置某一会话为置顶或者取消置顶，回调方式获取设置是否成功。
     *
     * @param conversationType 会话类型。
     * @param id               目标 Id。根据不同的 conversationType，可能是用户 Id、讨论组 Id、群组 Id 或聊天室 Id。
     * @param isTop            是否置顶。
     * @param callback         设置置顶或取消置顶是否成功的回调。
     */
    public static void setConversationToTop(Conversation.ConversationType conversationType, String id, boolean isTop, RongIMClient.ResultCallback<Boolean> callback) {
        RongIMClient.getInstance().setConversationToTop(conversationType, id, isTop, callback);
    }


    /**
     * 设置会话消息提醒状态。
     *
     * @param conversationType   会话类型。
     * @param targetId           目标 Id。根据不同的 conversationType，可能是用户 Id、讨论组 Id、群组 Id。
     * @param notificationStatus 是否屏蔽。
     * @param callback           设置状态的回调。
     */
    public static void setConversationNotificationStatus(Conversation.ConversationType conversationType, String targetId, Conversation.ConversationNotificationStatus notificationStatus, RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus> callback) {
        RongIMClient.getInstance().setConversationNotificationStatus(conversationType, targetId, notificationStatus, callback);
    }

    /**
     * 获取会话消息提醒状态。
     *
     * @param conversationType 会话类型。
     * @param targetId         目标 Id。根据不同的 conversationType，可能是用户 Id、讨论组 Id、群组 Id。
     * @param callback         获取状态的回调。
     */
    public static void getConversationNotificationStatus(Conversation.ConversationType conversationType, String targetId, RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus> callback) {
        RongIMClient.getInstance().getConversationNotificationStatus(conversationType, targetId, callback);
    }


    /**
     * 发送某个会话中消息阅读的回执
     *
     * @param conversationType 会话类型
     * @param targetId         目标会话ID
     * @param timestamp        该会话中已阅读点最后一条消息的发送时间戳
     *                         消息回执功能目前只支持单聊, 如果使用Lib可以注册监听setReadReceiptListener ,使用kit直接设置rc_config.xml中rc_read_receipt为true
     */
    public static void sendReadReceiptMessage(Conversation.ConversationType conversationType, String targetId, long timestamp) {
        RongIMClient.getInstance().sendReadReceiptMessage(conversationType, targetId, timestamp);
    }

    /**
     * 撤回消息
     *
     * @param message  将被撤回的消息
     * @param callback onSuccess里回调{@link RecallNotificationMessage}，IMLib 已经在数据库里将被撤回的消息用{@link RecallNotificationMessage} 替换，
     *                 用户需要在界面上对{@link RecallNotificationMessage} 进行展示。
     *                 <p/>
     *                 撤回通知消息，当用户撤回消息或者收到一条撤回信令消息时，需要根据此通知消息在界面上进行展示。
     * @MessageTag(value = "RC:RcNtf", flag = MessageTag.ISPERSISTED)
     * public class RecallNotificationMessage extends MessageContent {
     * <p/>
     * 发起撤回消息的用户id
     * <p/>
     * public String getOperatorId();
     * <p/>
     * <p/>
     * 撤回的时间（毫秒）
     * <p/>
     * public long getRecallTime();
     * <p/>
     * <p/>
     * 原消息的消息类型名
     * <p/>
     * public String getOriginalObjectName();
     * 您还需要设置撤回指令的监听器，以便在接收端收到撤回指令时刷新界面。
     * <p/>
     * 撤回消息监听器
     * public interface RecallMessageListener {
     * 撤回消息回调
     * messageId 被撤回消息的消息id
     * recallNotificationMessage 用于界面展示的{@link RecallNotificationMessage}
     * void onMessageRecalled(int messageId, RecallNotificationMessage recallNotificationMessage);
     * }
     * 设置撤回消息监听器
     * listener 撤回消息监听器
     * public static void setRecallMessageListener (final RecallMessageListener listener)
     * }
     */
    public static void recallMessage(Message message, RongIMClient.ResultCallback<RecallNotificationMessage> callback) {
        RongIMClient.getInstance().recallMessage(message, callback);
    }


    /**
     * 将某个用户加到黑名单中。
     *
     * @param userId   用户 Id。
     * @param callback 加到黑名单回调。
     */
    public static void addToBlacklist(String userId, RongIMClient.OperationCallback callback) {
        RongIMClient.getInstance().addToBlacklist(userId, callback);
    }

    /**
     * 将个某用户从黑名单中移出。
     *
     * @param userId   用户 Id。
     * @param callback 移除黑名单回调。
     */
    public static void removeFromBlacklist(String userId, RongIMClient.OperationCallback callback) {
        RongIMClient.getInstance().removeFromBlacklist(userId, callback);
    }

    /**
     * 获取某用户是否在黑名单中。
     *
     * @param userId   用户 Id。
     * @param callback 获取用户是否在黑名单回调。
     */
    public static void getBlacklistStatus(String userId, RongIMClient.ResultCallback<RongIMClient.BlacklistStatus> callback) {
        RongIMClient.getInstance().getBlacklistStatus(userId, callback);
    }

    /**
     * 获取当前用户的黑名单列表。
     *
     * @param callback 获取黑名单回调。
     */
    public static void getBlacklist(RongIMClient.GetBlacklistCallback callback) {
        RongIMClient.getInstance().getBlacklist(callback);
    }


    /**
     * 加入聊天室。
     *
     * @param chatroomId      聊天室 Id。
     * @param defMessageCount 进入聊天室拉取消息数目，为 -1 时不拉取任何消息，默认拉取 10 条消息。
     * @param callback        状态回调。
     */
    public static void joinChatRoom(final String chatroomId, int defMessageCount, RongIMClient.OperationCallback callback) {
        RongIMClient.getInstance().joinChatRoom(chatroomId, defMessageCount, callback);
    }

    /**
     * 退出聊天室。
     *
     * @param chatroomId 聊天室 Id。
     * @param callback   状态回调。
     */
    public static void quitChatRoom(String chatroomId, RongIMClient.OperationCallback callback) {
        RongIMClient.getInstance().quitChatRoom(chatroomId, callback);
    }

    /**
     * 断开连接(默认断开后接收 Push 消息)。
     */
    public static void disconnect() {
        RongIMClient.getInstance().disconnect();
    }

    /**
     * 注销登录(不再接收 Push 消息)。
     */
    public static void logout() {
        RongIMClient.getInstance().logout();
    }


    /**
     * 获取连接状态
     *
     * @return
     */
    public static RongIMClient.ConnectionStatusListener.ConnectionStatus getCurrentConnectionStatus() {
        return RongIMClient.getInstance().getCurrentConnectionStatus();
    }

    /**
     * 重连
     *
     * @param connectCallback
     */
    public static void reconnect(RongIMClient.ConnectCallback connectCallback) {
        if (RongIMClient.getInstance() == null || RongIMClient.getInstance() == null) {
            RongIMClient.getInstance().reconnect(connectCallback);
        }

    }
}
