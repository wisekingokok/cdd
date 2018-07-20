package com.chewuwuyou.eim.manager;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.util.Cache;
import org.jivesoftware.smackx.address.provider.MultipleAddressesProvider;
import org.jivesoftware.smackx.chatstates.packet.ChatStateExtension;
import org.jivesoftware.smackx.delay.provider.DelayInfoProvider;
import org.jivesoftware.smackx.delay.provider.DelayInformationProvider;
import org.jivesoftware.smackx.disco.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.disco.provider.DiscoverItemsProvider;
import org.jivesoftware.smackx.iqlast.packet.LastActivity;
import org.jivesoftware.smackx.iqprivate.PrivateDataManager;
import org.jivesoftware.smackx.muc.packet.GroupChatInvitation;
import org.jivesoftware.smackx.muc.provider.MUCAdminProvider;
import org.jivesoftware.smackx.muc.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.muc.provider.MUCUserProvider;
import org.jivesoftware.smackx.offline.packet.OfflineMessageInfo;
import org.jivesoftware.smackx.offline.packet.OfflineMessageRequest;
import org.jivesoftware.smackx.ping.provider.PingProvider;
import org.jivesoftware.smackx.receipts.DeliveryReceipt;
import org.jivesoftware.smackx.receipts.DeliveryReceiptRequest;
import org.jivesoftware.smackx.search.UserSearch;
import org.jivesoftware.smackx.sharedgroups.packet.SharedGroupsInfo;
import org.jivesoftware.smackx.si.provider.StreamInitiationProvider;
import org.jivesoftware.smackx.vcardtemp.provider.VCardProvider;
import org.jivesoftware.smackx.xdata.provider.DataFormProvider;
import org.jivesoftware.smackx.xevent.provider.MessageEventProvider;
import org.jivesoftware.smackx.xhtmlim.provider.XHTMLExtensionProvider;

import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.eim.model.LoginConfig;

/**
 * XMPP服务器连接工具类.
 *
 * @author sxk
 */
public class XmppConnectionManager {
    private XMPPConnection connection;
    private static ConnectionConfiguration connectionConfig;
    private static XmppConnectionManager xmppConnectionManager;

    private XmppConnectionManager() {

    }

    public static XmppConnectionManager getInstance() {
        if (xmppConnectionManager == null) {
            xmppConnectionManager = new XmppConnectionManager();
        }
        return xmppConnectionManager;
    }

    // init
    public XMPPConnection init(LoginConfig loginConfig) {
        SmackConfiguration.DEBUG_ENABLED = false;
        // ProviderManager pm = ProviderManager.getInstance();
        configure();

        connectionConfig = new ConnectionConfiguration(
                loginConfig.getXmppHost(), loginConfig.getXmppPort(),
                loginConfig.getXmppServiceName());
        // connectionConfig.setSASLAuthenticationEnabled(false);//
        // 不使用SASL验证，设置为false
        connectionConfig
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        // 允许自动连接
        connectionConfig.setReconnectionAllowed(true);
        // 允许登陆成功后更新在线状态
        connectionConfig.setSendPresence(false); // 本次暂设定为不自动发送，这样好留下一些时间用来启动service和设置lisenter
        connectionConfig.setRosterLoadedAtLogin(true);
        // 收到好友邀请后manual表示需要经过同意,accept_all表示不经同意自动为好友
        Roster.setDefaultSubscriptionMode(Roster.SubscriptionMode.manual);
        connection = new XMPPTCPConnection(connectionConfig);
        return connection;
    }

    /**
     * 返回一个有效的xmpp连接,如果无效则返回空.
     *
     * @return
     * @author sxk
     * @update 2012-7-4 下午6:54:31
     */
    public XMPPConnection getConnection() {
        if (connection == null) {
            MyLog.e("YUY", "请先初始化XMPPConnection连接");
//			throw new RuntimeException("请先初始化XMPPConnection连接");
        }
        return connection;
    }

    /**
     * 返回一个有效的xmpp连接,如果无效则返回空.
     *
     * @return
     * @author sxk
     * @update 2012-7-4 下午6:54:31
     */
    public XMPPConnection getConnectionNoError() {
        return connection;
    }

    /**
     * 销毁xmpp连接.
     *
     * @author sxk
     * @update 2012-7-4 下午6:55:03
     */
    public void disconnect() {
        if (connection != null) {
            try {
                connection.disconnect();
            } catch (NotConnectedException e) {
                e.printStackTrace();
            }
        }
    }

    public void configure() {

        // Private Data Storage
        ProviderManager.addIQProvider("query", "jabber:iq:private",
                new PrivateDataManager.PrivateDataIQProvider());

        // Time
        try {
            ProviderManager.addIQProvider("query", "jabber:iq:time",
                    Class.forName("org.jivesoftware.smackx.packet.Time"));
        } catch (ClassNotFoundException e) {
        }
        // ping
        ProviderManager.addIQProvider("ping", "urn:xmpp:ping",
                new PingProvider());

        // XHTML
        ProviderManager.addExtensionProvider("html",
                "http://jabber.org/protocol/xhtml-im",
                new XHTMLExtensionProvider());

        // Roster Exchange
        // ProviderManager.addExtensionProvider("x", "jabber:x:roster",
        // new RosterExchangeProvider());
        // Message Events
        ProviderManager.addExtensionProvider("x", "jabber:x:event",
                new MessageEventProvider());
        // Chat State
        ProviderManager.addExtensionProvider("active",
                "http://jabber.org/protocol/chatstates",
                new ChatStateExtension.Provider());
        ProviderManager.addExtensionProvider("composing",
                "http://jabber.org/protocol/chatstates",
                new ChatStateExtension.Provider());
        ProviderManager.addExtensionProvider("paused",
                "http://jabber.org/protocol/chatstates",
                new ChatStateExtension.Provider());
        ProviderManager.addExtensionProvider("inactive",
                "http://jabber.org/protocol/chatstates",
                new ChatStateExtension.Provider());
        ProviderManager.addExtensionProvider("gone",
                "http://jabber.org/protocol/chatstates",
                new ChatStateExtension.Provider());

        // FileTransfer
        ProviderManager.addIQProvider("si", "http://jabber.org/protocol/si",
                new StreamInitiationProvider());

        // Group Chat Invitations
        ProviderManager.addExtensionProvider("x", "jabber:x:conference",
                new GroupChatInvitation.Provider());
        // Service Discovery # Items
        ProviderManager.addIQProvider("query",
                "http://jabber.org/protocol/disco#items",
                new DiscoverItemsProvider());
        // Service Discovery # Info
        ProviderManager.addIQProvider("query",
                "http://jabber.org/protocol/disco#info",
                new DiscoverInfoProvider());
        // Data Forms
        ProviderManager.addExtensionProvider("x", "jabber:x:data",
                new DataFormProvider());
        // MUC User
        ProviderManager.addExtensionProvider("x",
                "http://jabber.org/protocol/muc#user", new MUCUserProvider());
        // MUC Admin
        ProviderManager.addIQProvider("query",
                "http://jabber.org/protocol/muc#admin", new MUCAdminProvider());
        // MUC Owner
        ProviderManager.addIQProvider("query",
                "http://jabber.org/protocol/muc#owner", new MUCOwnerProvider());
        // // add carbons and forwarding
        // ProviderManager.addExtensionProvider("forwarded",
        // Forwarded.NAMESPACE,
        // new ForwardedProvider());
        // ProviderManager.addExtensionProvider("sent",
        // CarbonExtension.NAMESPACE, new CarbonManagerProvider());
        // ProviderManager.addExtensionProvider("received",
        // CarbonExtension.NAMESPACE, new CarbonManagerProvider());

        // Delayed Delivery -- 两套 UTC
        ProviderManager.addExtensionProvider("delay", "urn:xmpp:delay",
                new DelayInfoProvider());
        ProviderManager.addExtensionProvider("x", "jabber:x:delay",
                new DelayInformationProvider());
        // Version
        try {
            ProviderManager.addIQProvider("query", "jabber:iq:version",
                    Class.forName("org.jivesoftware.smackx.packet.Version"));
        } catch (ClassNotFoundException e) {
        }
        // VCard
        ProviderManager.addIQProvider("vCard", "vcard-temp",
                new VCardProvider());
        // Offline Message Requests
        ProviderManager.addIQProvider("offline",
                "http://jabber.org/protocol/offline",
                new OfflineMessageRequest.Provider());
        // Offline Message Indicator
        ProviderManager.addExtensionProvider("offline",
                "http://jabber.org/protocol/offline",
                new OfflineMessageInfo.Provider());
        // add delivery receipts
        ProviderManager.addExtensionProvider(DeliveryReceipt.ELEMENT,
                DeliveryReceipt.NAMESPACE, new DeliveryReceipt.Provider());
        ProviderManager.addExtensionProvider(DeliveryReceiptRequest.ELEMENT,
                DeliveryReceipt.NAMESPACE,
                new DeliveryReceiptRequest.Provider());
        // Last Activity
        ProviderManager.addIQProvider("query", "jabber:iq:last",
                new LastActivity.Provider());
        // User Search
        ProviderManager.addIQProvider("query", "jabber:iq:search",
                new UserSearch.Provider());
        // SharedGroupsInfo
        ProviderManager.addIQProvider("sharedgroup",
                "http://www.jivesoftware.org/protocol/sharedgroup",
                new SharedGroupsInfo.Provider());
        // JEP-33: Extended Stanza Addressing
        ProviderManager.addExtensionProvider("addresses",
                "http://jabber.org/protocol/address",
                new MultipleAddressesProvider());

        // ServiceDiscoveryManager.setIdentityName(XMPP_IDENTITY_NAME);
        // ServiceDiscoveryManager.setIdentityType(XMPP_IDENTITY_TYPE);
    }
}
