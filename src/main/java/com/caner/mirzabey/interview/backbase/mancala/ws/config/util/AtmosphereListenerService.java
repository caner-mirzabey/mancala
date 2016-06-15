package com.caner.mirzabey.interview.backbase.mancala.ws.config.util;

import org.atmosphere.cache.CacheMessage;
import org.atmosphere.cpr.*;
import org.atmosphere.websocket.WebSocketEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ecanmir on 12.06.2016.
 */
//@AtmosphereFrameworkListenerService
//@AtmosphereResourceListenerService
//@AsyncSupportListenerService
//@BroadcasterListenerService
//@BroadcasterCacheListenerService
public class AtmosphereListenerService implements BroadcasterListener,
                                                  AsyncSupportListener,
                                                  AtmosphereResourceListener,
                                                  AtmosphereFrameworkListener,
                                                  BroadcasterCacheListener,
                                                  AtmosphereResourceEventListener,
                                                  WebSocketEventListener {
    public static final Logger logger = LoggerFactory.getLogger(AtmosphereListenerService.class);

    ///BroadcasterListener Methods
    @Override
    public void onPostCreate(Broadcaster b) {
        logger.debug("BroadcasterListener.onPostCreate::" + b.getID());
    }

    @Override
    public void onComplete(Broadcaster b) {
        logger.debug("BroadcasterListener.onComplete::" + b.getID());
    }

    @Override
    public void onPreDestroy(Broadcaster b) {
        logger.debug("BroadcasterListener.onPreDestroy::" + b.getID());
    }

    @Override
    public void onAddAtmosphereResource(Broadcaster b, AtmosphereResource r) {
        logger.debug("BroadcasterListener.onAddAtmosphereResource --> Broadcaster::" + b.getID() + ", resource::" +
                     r.toString());
    }

    @Override
    public void onRemoveAtmosphereResource(Broadcaster b, AtmosphereResource r) {
        logger.debug("BroadcasterListener.onRemoveAtmosphereResource --> Broadcaster::" + b.getID() + ", resource::" +
                     r.toString());
    }

    @Override
    public void onMessage(Broadcaster b, Deliver deliver) {
        logger.debug(
                "BroadcasterListener.onMessage --> Broadcaster::" + b.getID() + ", Deliver::" + deliver.toString());
    }

    //AsyncSupport Listener methods

    @Override
    public void onSuspend(AtmosphereRequest request, AtmosphereResponse response) {
        logger.trace("AsyncSupportListener.onSuspend --> AtmosphereRequest::" + request.toString() +
                     ", AtmosphereResponse::" +
                     response.toString());
    }

    @Override
    public void onResume(AtmosphereRequest request, AtmosphereResponse response) {
        logger.trace("AsyncSupportListener.onResume --> AtmosphereRequest::" + request.toString() +
                     ", AtmosphereResponse::" +
                     response.toString());
    }

    @Override
    public void onTimeout(AtmosphereRequest request, AtmosphereResponse response) {
        logger.trace("AsyncSupportListener.onTimeout --> AtmosphereRequest::" + request.toString() +
                     ", AtmosphereResponse::" +
                     response.toString());
    }

    @Override
    public void onClose(AtmosphereRequest request, AtmosphereResponse response) {
        logger.trace(
                "AsyncSupportListener.onClose --> AtmosphereRequest::" + request.toString() + ", AtmosphereResponse::" +
                response.toString());
    }

    @Override
    public void onDestroyed(AtmosphereRequest request, AtmosphereResponse response) {
        logger.trace("AsyncSupportListener.onDestroyed --> AtmosphereRequest::" + request.toString() +
                     ", AtmosphereResponse::" +
                     response.toString());
    }

    // AtmosphereResourceListener Methods
    @Override
    public void onSuspended(String uuid) {
        logger.debug("AtmosphereResourceListener.onSuspend --> uuid::" + uuid);
    }

    @Override
    public void onDisconnect(String uuid) {
        logger.debug("AtmosphereResourceListener.onDisconnect --> uuid::" + uuid);
    }

    //AtmosphereFrameworkListener Methods

    @Override
    public void onPreInit(AtmosphereFramework f) {
        logger.debug("AtmosphereFrameworkListener.onPreInit --> AtmosphereFramework::" + f.toString());
    }

    @Override
    public void onPostInit(AtmosphereFramework f) {
        logger.debug("AtmosphereFrameworkListener.onPostInit --> AtmosphereFramework::" + f.toString());
    }

    @Override
    public void onPreDestroy(AtmosphereFramework f) {
        logger.debug("AtmosphereFrameworkListener.onPreDestroy --> AtmosphereFramework::" + f.toString());
    }

    @Override
    public void onPostDestroy(AtmosphereFramework f) {
        logger.debug("AtmosphereFrameworkListener.onPostDestroy --> AtmosphereFramework::" + f.toString());
    }

    //BroadcasterCacheListener methods

    @Override
    public void onAddCache(String broadcasterId, CacheMessage cacheMessage) {
        logger.debug("BroadcasterCacheListener.onAddCache --> broadcasterId::" + broadcasterId + ", CacheMessage::" +
                     cacheMessage.toString());
    }

    @Override
    public void onRemoveCache(String broadcasterId, CacheMessage cacheMessage) {
        logger.debug("BroadcasterCacheListener.onRemoveCache --> broadcasterId::" + broadcasterId + ", CacheMessage::" +
                     cacheMessage.toString());
    }

    //AtmosphereResourceEventListener methods

    @Override
    public void onPreSuspend(AtmosphereResourceEvent event) {

    }

    @Override
    public void onSuspend(AtmosphereResourceEvent event) {

    }

    @Override
    public void onResume(AtmosphereResourceEvent event) {

    }

    @Override
    public void onDisconnect(AtmosphereResourceEvent event) {

    }

    @Override
    public void onBroadcast(AtmosphereResourceEvent event) {

    }

    @Override
    public void onThrowable(AtmosphereResourceEvent event) {

    }

    @Override
    public void onClose(AtmosphereResourceEvent event) {

    }

    @Override
    public void onHeartbeat(AtmosphereResourceEvent event) {

    }

    //WebSocketEventListener methods

    @Override
    public void onHandshake(WebSocketEvent event) {

    }

    @Override
    public void onMessage(WebSocketEvent event) {

    }

    @Override
    public void onClose(WebSocketEvent event) {

    }

    @Override
    public void onControl(WebSocketEvent event) {

    }

    @Override
    public void onDisconnect(WebSocketEvent event) {

    }

    @Override
    public void onConnect(WebSocketEvent event) {

    }
}
