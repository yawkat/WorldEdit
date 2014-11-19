package com.sk89q.wepif;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.bukkit.OfflinePlayer;

/**
 * @author yawkat
 */
public class CachingPermissionsResolver implements PermissionsResolver {
    private final PermissionsResolver handle;

    private final Cache<List<String>, Boolean> hasPermission2 = CacheBuilder.newBuilder()
            .expireAfterWrite(10, TimeUnit.SECONDS)
            .build(new CacheLoader<List<String>, Boolean>() {
                @Override
                public Boolean load(List<String> strings) throws Exception {
                    return handle.hasPermission(strings.get(0), strings.get(1));
                }
            });
    private final Cache<List<String>, Boolean> hasPermission3 = CacheBuilder.newBuilder()
            .expireAfterWrite(10, TimeUnit.SECONDS)
            .build(new CacheLoader<List<String>, Boolean>() {
                @Override
                public Boolean load(List<String> strings) throws Exception {
                    return handle.hasPermission(strings.get(0), strings.get(1), strings.get(2));
                }
            });
    private final Cache<List<Object>, Boolean> hasPermission3P = CacheBuilder.newBuilder()
            .expireAfterWrite(10, TimeUnit.SECONDS)
            .build(new CacheLoader<List<Object>, Boolean>() {
                @Override
                public Boolean load(List<Object> objects) throws Exception {
                    return handle.hasPermission((String) objects.get(0), (OfflinePlayer) objects.get(1),
                                                (String) objects.get(2));
                }
            });
    private final Cache<List<Object>, Boolean> hasPermission2P = CacheBuilder.newBuilder()
            .expireAfterWrite(10, TimeUnit.SECONDS)
            .build(new CacheLoader<List<Object>, Boolean>() {
                @Override
                public Boolean load(List<Object> objects) throws Exception {
                    return handle.hasPermission((OfflinePlayer) objects.get(0), (String) objects.get(1));
                }
            });

    public CachingPermissionsResolver(PermissionsResolver handle) {
        this.handle = handle;
    }

    @Override
    public boolean hasPermission(String name, String permission) {
        return hasPermission2.getUnchecked(Arrays.asList(name, permission));
    }

    @Override
    public boolean hasPermission(String worldName, String name, String permission) {
        return hasPermission3.getUnchecked(Arrays.asList(worldName, name, permission));
    }

    @Override
    public boolean hasPermission(String worldName, OfflinePlayer player, String permission) {
        return hasPermission3P.getUnchecked(Arrays.asList(worldName, player, permission));
    }

    @Override
    public boolean inGroup(String player, String group) {
        return handle.inGroup(player, group);
    }

    @Override
    public String[] getGroups(String player) {
        return handle.getGroups(player);
    }

    @Override
    public boolean inGroup(OfflinePlayer player, String group) {
        return handle.inGroup(player, group);
    }

    @Override
    public String[] getGroups(OfflinePlayer player) {
        return handle.getGroups(player);
    }

    @Override
    public boolean hasPermission(OfflinePlayer player, String permission) {
        return hasPermission2P.getUnchecked(Arrays.asList(player, permission));
    }

    @Override
    public void load() {
        handle.load();
    }

    @Override
    public String getDetectionMessage() {
        return handle.getDetectionMessage();
    }
}
