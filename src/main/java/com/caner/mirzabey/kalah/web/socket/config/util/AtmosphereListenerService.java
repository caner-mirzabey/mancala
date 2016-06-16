//package com.caner.mirzabey.kalah.web.socket.config.util;
//
//import com.caner.mirzabey.kalah.game.GameRepository;
//import com.caner.mirzabey.kalah.user.UserRepository;
//
//import org.atmosphere.cache.CacheMessage;
//import org.atmosphere.cpr.*;
//import org.atmosphere.websocket.WebSocketEventListener;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import javax.servlet.Servlet;
//
///**
// * Created by ecanmir on 12.06.2016.
// */
////@AtmosphereFrameworkListenerService
////@AtmosphereResourceListenerService
////@AsyncSupportListenerService
////@BroadcasterListenerService
////@BroadcasterCacheListenerService
//public class AtmosphereListenerService implements BroadcasterListener,
//                                                  AsyncSupportListener,
//                                                  AtmosphereResourceListener,
//                                                  AtmosphereFrameworkListener,
//                                                  BroadcasterCacheListener,
//                                                  AtmosphereResourceEventListener,
//                                                  WebSocketEventListener,
//                                                  BroadcastFilter {
//    public static final Logger logger = LoggerFactory.getLogger(AtmosphereListenerService.class);
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private GameRepository gameRepository;
//
//    @Autowired
//    private BroadcasterFactory broadcasterFactory;
//
//    @Autowired
//    private MetaBroadcaster metaBroadcaster;
//
//    @Autowired
//    AtmosphereResourceFactory atmosphereResourceFactory;
//
//    @Autowired
//    AtmosphereConfig atmosphereConfig;
//
//    @Autowired
//    Servlet servlet;
//
//    ///BroadcasterListener Methods
//    @Override
//    public void onPostCreate(Broadcaster b) {
//        logger.debug("BroadcasterListener.onPostCreate::{}", b.getID());
//        atmosphereConfig.metaBroadcaster().broadcastTo("/", "Caner Mirzabey", true);
//    }
//
//    @Override
//    public void onComplete(Broadcaster b) {
//        logger.debug("BroadcasterListener.onComplete::{}", b.getID());
//    }
//
//    @Override
//    public void onPreDestroy(Broadcaster b) {
//        logger.debug("BroadcasterListener.onPreDestroy::{}", b.getID());
//    }
//
//    @Override
//    public void onAddAtmosphereResource(Broadcaster b, AtmosphereResource r) {
//        logger.debug("BroadcasterListener.onAddAtmosphereResource --> Broadcaster::{}, resource::{}",
//                     b.getID(),
//                     r.toString());
//    }
//
//    @Override
//    public void onRemoveAtmosphereResource(Broadcaster b, AtmosphereResource r) {
//        logger.debug("BroadcasterListener.onRemoveAtmosphereResource --> Broadcaster::{}, resource::{}",
//                     b.getID(),
//                     r.toString());
//    }
//
//    @Override
//    public void onMessage(Broadcaster b, Deliver deliver) {
//        logger.debug("BroadcasterListener.onMessage --> Broadcaster::{}, Deliver::{}", b.getID(), deliver.toString());
//    }
//
//    //AsyncSupport Listener methods
//
//    @Override
//    public void onSuspend(AtmosphereRequest request, AtmosphereResponse response) {
//        logger.trace("AsyncSupportListener.onSuspend --> AtmosphereRequest::{}", request.toString() +
//                                                                                 ", AtmosphereResponse::{}" +
//                                                                                 response.toString());
//    }
//
//    @Override
//    public void onResume(AtmosphereRequest request, AtmosphereResponse response) {
//        logger.trace("AsyncSupportListener.onResume --> AtmosphereRequest::{}, AtmosphereResponse::{}",
//                     request.toString(),
//                     response.toString());
//    }
//
//    @Override
//    public void onTimeout(AtmosphereRequest request, AtmosphereResponse response) {
//        logger.trace("AsyncSupportListener.onTimeout --> AtmosphereRequest::{}, AtmosphereResponse::{}",
//                     request.toString(),
//                     response.toString());
//    }
//
//    @Override
//    public void onClose(AtmosphereRequest request, AtmosphereResponse response) {
//        logger.trace("AsyncSupportListener.onClose --> AtmosphereRequest::{}, AtmosphereResponse::{}",
//                     request.toString(),
//                     response.toString());
//    }
//
//    @Override
//    public void onDestroyed(AtmosphereRequest request, AtmosphereResponse response) {
//        logger.trace("AsyncSupportListener.onDestroyed --> AtmosphereRequest::{}, AtmosphereResponse::{}",
//                     request.toString(),
//                     response.toString());
//    }
//
//    // AtmosphereResourceListener Methods
//    @Override
//    public void onSuspended(String uuid) {
//        logger.debug("AtmosphereResourceListener.onSuspend --> uuid::{}", uuid);
//    }
//
//    @Override
//    public void onDisconnect(String uuid) {
//        logger.debug("AtmosphereResourceListener.onDisconnect --> uuid::{}", uuid);
//    }
//
//    //AtmosphereFrameworkListener Methods
//
//    @Override
//    public void onPreInit(AtmosphereFramework f) {
//        logger.debug("AtmosphereFrameworkListener.onPreInit --> AtmosphereFramework::{}", f.toString());
//    }
//
//    @Override
//    public void onPostInit(AtmosphereFramework f) {
//        logger.debug("AtmosphereFrameworkListener.onPostInit --> AtmosphereFramework::{}", f.toString());
//    }
//
//    @Override
//    public void onPreDestroy(AtmosphereFramework f) {
//        logger.debug("AtmosphereFrameworkListener.onPreDestroy --> AtmosphereFramework::{}", f.toString());
//    }
//
//    @Override
//    public void onPostDestroy(AtmosphereFramework f) {
//        logger.debug("AtmosphereFrameworkListener.onPostDestroy --> AtmosphereFramework::{}", f.toString());
//    }
//
//    //BroadcasterCacheListener methods
//
//    @Override
//    public void onAddCache(String broadcasterId, CacheMessage cacheMessage) {
//        logger.debug("BroadcasterCacheListener.onAddCache --> broadcasterId::{}, CacheMessage::{}",
//                     broadcasterId,
//                     cacheMessage.toString());
//    }
//
//    @Override
//    public void onRemoveCache(String broadcasterId, CacheMessage cacheMessage) {
//        logger.debug("BroadcasterCacheListener.onRemoveCache --> broadcasterId::{}, CacheMessage::{}",
//                     broadcasterId,
//                     cacheMessage.toString());
//    }
//
//    //AtmosphereResourceEventListener methods
//
//    @Override
//    public void onPreSuspend(AtmosphereResourceEvent event) {
//
//    }
//
//    @Override
//    public void onSuspend(AtmosphereResourceEvent event) {
//
//    }
//
//    @Override
//    public void onResume(AtmosphereResourceEvent event) {
//
//    }
//
//    @Override
//    public void onDisconnect(AtmosphereResourceEvent event) {
//
//    }
//
//    @Override
//    public void onBroadcast(AtmosphereResourceEvent event) {
//
//    }
//
//    @Override
//    public void onThrowable(AtmosphereResourceEvent event) {
//
//    }
//
//    @Override
//    public void onClose(AtmosphereResourceEvent event) {
//
//    }
//
//    @Override
//    public void onHeartbeat(AtmosphereResourceEvent event) {
//
//    }
//
//    //WebSocketEventListener methods
//
//    @Override
//    public void onHandshake(WebSocketEvent event) {
//
//    }
//
//    @Override
//    public void onMessage(WebSocketEvent event) {
//
//    }
//
//    @Override
//    public void onClose(WebSocketEvent event) {
//
//    }
//
//    @Override
//    public void onControl(WebSocketEvent event) {
//
//    }
//
//    @Override
//    public void onDisconnect(WebSocketEvent event) {
//
//    }
//
//    @Override
//    public void onConnect(WebSocketEvent event) {
//
//    }
//
//    // BroadcastFilter Methods
//
//    @Override
//    public BroadcastAction filter(String broadcasterId, Object originalMessage, Object message) {
//        return new BroadcastAction(message);
//    }
//}
