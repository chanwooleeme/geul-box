package com.bestbranch.geulboxapi.common.cache;

import java.util.function.Supplier;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EhCacheService {
	private final CacheManager cacheManager;

	public <T> T get(String cacheName, String key, Class<T> clazz, Supplier<T> supplier) {
		T cachedValue = get(cacheName, key, clazz);
		if (cachedValue == null) {
			T value = supplier.get();
			set(cacheName, key, value);
			return value;
		}
		return cachedValue;
	}

	public <T> void set(String cacheName, String key, T value) {
		Cache cache = cacheManager.getCache(cacheName);
		if (cache != null) {
			cache.put(key, value);
			log.info("[EHCACHE] set : cache name = {}, key = {}", cacheName, key);
		}
	}

	public String get(String cacheName, String key) {
		return get(cacheName, key, String.class);
	}

	public <T> T get(String cacheName, String key, Class<T> clazz) {
		Object value = cacheManager.getCache(cacheName).get(key, clazz);
		log.info("[EHCACHE] get : cache name = {}, key = {}", cacheName, key);
		return clazz.cast(value);
	}

	public void delete(String cacheName, String key) {
		Cache cache = cacheManager.getCache(cacheName);
		if (cache != null) {
			cache.evict(key);
			log.info("[EHCACHE] evict : cache name = {}, key = {}", cacheName, key);
		}
	}

	public boolean isValueEmpty(String cacheName, String key) {
		Cache cache = cacheManager.getCache(cacheName);
		if (cache != null) {
			return cache.get(key) == null;
		}
		return true;
	}
}
