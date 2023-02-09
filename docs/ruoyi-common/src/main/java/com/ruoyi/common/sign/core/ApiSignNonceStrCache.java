package com.ruoyi.common.sign.core;//package com.ruoyi.common.sign.core;
//
//import com.google.common.cache.Cache;
//import com.google.common.cache.CacheBuilder;
//
//import java.util.concurrent.TimeUnit;
//
///**
// * 随机参数缓存操作
// */
//public class ApiSignNonceStrCache {
//
//    private volatile static com.signature.sign.core.ApiSignNonceStrCache instance = null;
//
//    public  static com.signature.sign.core.ApiSignNonceStrCache getInstance(long expireTme) {
//        if (instance == null) {
//            synchronized (com.signature.sign.core.ApiSignNonceStrCache.class) {
//                if(instance == null) {
//                    instance = new com.signature.sign.core.ApiSignNonceStrCache(expireTme);
//                }
//            }
//        }
//        return instance;
//    }
//
//    private Cache<String, Long> cache;
//
//    private ApiSignNonceStrCache(long expireTme) {
//        cache = CacheBuilder.newBuilder().expireAfterAccess(expireTme, TimeUnit.SECONDS).build();
//    }
//
//    /**
//     * 缓存随机数
//     * @param appId
//     * @param nonce
//     */
//    public void put(int appId, String nonce) {
//        String key = appId + nonce;
//        cache.put(key, System.currentTimeMillis());
//    }
//
//    /**
//     * 判断随机数是否存在
//     * @param appId
//     * @param nonce
//     * @return
//     */
//    public boolean isExist(int appId, String nonce) {
//        String key = appId + nonce;
//        Long val = cache.getIfPresent(key);
//        return val != null;
//    }
//
//
//}
