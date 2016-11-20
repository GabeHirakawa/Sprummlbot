package net.scrumplex.sprummlbot.wrapper;

import com.github.theholywaffle.teamspeak3.api.exception.TS3CommandFailedException;
import net.scrumplex.sprummlbot.Sprummlbot;
import net.scrumplex.sprummlbot.tools.Exceptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermissionGroup {

    private final String name;
    private final List<String> clients = new ArrayList<>();
    private final List<Integer> groups = new ArrayList<>();
    private final List<String> includes = new ArrayList<>();
    private final Map<String, Boolean> cachedClients = new HashMap<>();

    private static final Map<String, PermissionGroup> permissionGroups = new HashMap<>();
    private static final Map<String, String> permissionGroupFields = new HashMap<>();

    public PermissionGroup(String name) {
        this.name = name;
    }

    public void addClient(String uid) {
        clients.add(uid);
    }

    public void removeClient(String uid) {
        clients.remove(uid);
    }

    public void addGroup(int groupId) {
        groups.add(groupId);
    }

    public void addIncludingPermissionGroup(String name) {
        includes.add(name);
    }

    public void clearCache() {
        cachedClients.clear();
    }

    public boolean isClientInGroup(String uid) {
        if (uid == null)
            return false;
        if (name.equalsIgnoreCase(".")) {
            return false;
        } else if (name.equalsIgnoreCase("*")) {
            return true;
        }
        if (cachedClients.containsKey(uid))
            return cachedClients.get(uid);

        boolean result = isClientInGroupNoCache(uid);
        cachedClients.put(uid, result);
        return result;
    }

    private boolean isClientInGroupNoCache(String uid) {
        if (uid == null)
            return false;
        try {
            if (clients.contains(uid))
                return true;
            for (String group : includes) {
                if (getPermissionGroupByName(group).isClientInGroup(uid))
                    return true;
            }
            for (int group : Sprummlbot.getSprummlbot().getSyncAPI().getClientByUId(uid).getServerGroups())
                if (groups.contains(group))
                    return true;
        } catch (TS3CommandFailedException ignored) {
        } catch (Exception ex) {
            Exceptions.handle(ex, "Couldn't fetch permission group info for a client!", false);
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public static void addPermissionGroup(PermissionGroup group) {
        permissionGroups.put(group.getName(), group);
    }

    public static List<PermissionGroup> getPermissionGroups() {
        return new ArrayList<>(permissionGroups.values());
    }

    public static PermissionGroup getPermissionGroupByName(String groupName) {
        if (groupName.equals(".") || groupName.equals("*"))
            return new PermissionGroup(groupName);
        return permissionGroups.get(groupName);
    }

    /**
     * <b>Internal</b> ignore this
     *
     * @param field     Field
     * @param groupName Group's name
     */
    public static void setPermissionGroupField(String field, String groupName) {
        permissionGroupFields.put(field, groupName);
    }

    /**
     * <b>Internal</b> ignore this
     *
     * @param field Field
     * @return Group assigned to field
     */
    public static PermissionGroup getPermissionGroupForField(String field) {
        return getPermissionGroupByName(permissionGroupFields.get(field));
    }
}
